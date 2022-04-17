package fr.univ.givr.security;

import fr.univ.givr.exception.user.AccountBannedException;
import fr.univ.givr.exception.user.AccountNotFoundException;
import fr.univ.givr.exception.user.AccountNotVerifiedException;
import fr.univ.givr.service.AuthenticateService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Filter to check the validity of the JWT token.
 */
@Slf4j
@AllArgsConstructor
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    /**
     * Type of authorization.
     */
    public static final String TYPE_AUTHORIZATION = "Bearer";

    /**
     * Header authorization name.
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * Cookie authorization name.
     */
    public static final String COOKIE_NAME = "GIVR_AUTH";

    /**
     * Builder to create authentication details.
     */
    private static final WebAuthenticationDetailsSource WEB_AUTHENTICATION_DETAILS =
            new WebAuthenticationDetailsSource();

    /**
     * Token manager to verify the token.
     */
    private final JWTTokenManager tokenManager;

    /**
     * Service to retrieve data about a valid user.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Authentication service to manage cookie auth.
     */
    private final AuthenticateService authenticateService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() == null) {
            String token = getTokenFromHeaderOrCookie(request);

            if (token != null) {
                String username = getUsernameFromTokenOrNull(token);
                if (username != null) {
                    try {
                        setAuthenticationWithUserAccount(securityContext, request, username);
                    } catch (AccountBannedException | AccountNotVerifiedException | AccountNotFoundException ex) {
                        authenticateService.removeCookieAuth(response);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Get the token to authenticate the user from the header or the cookies.
     *
     * @param request Request.
     * @return The token if found, {@code null} otherwise.
     */
    private String getTokenFromHeaderOrCookie(@NonNull HttpServletRequest request) {
        String authHeader = getAuthorizationOrNull(request);
        if (authHeader != null) {
            return getTokenFromHeaderAuthValue(authHeader);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        Cookie cookieAuth = findCookieForAuth(cookies);
        if (cookieAuth != null) {
            return cookieAuth.getValue();
        }

        return null;
    }

    /**
     * Find the authentication cookie among all cookies in the request.
     *
     * @param cookies All cookies from the request.
     * @return The cookie if one of the good name is found, {@code null} otherwise.
     */
    private Cookie findCookieForAuth(@NonNull Cookie[] cookies) {
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(COOKIE_NAME)).findFirst().orElse(null);
    }

    /**
     * Load the details account for the user linked to the username and define it as authenticate account for the request.
     *
     * @param securityContext Context to define the security account.
     * @param request         Request with which the account will be linked.
     * @param username        Username to retrieve the user account.
     */
    private void setAuthenticationWithUserAccount(
            SecurityContext securityContext,
            HttpServletRequest request,
            String username
    ) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(WEB_AUTHENTICATION_DETAILS.buildDetails(request));
        securityContext.setAuthentication(authenticationToken);
    }

    /**
     * Get the username stored in the token in the [HttpServletRequest].
     * Check if the token is valid.
     *
     * @param token Auth token.
     * @return The username if there is no error, {@code null} otherwise.
     */
    private String getUsernameFromTokenOrNull(@NonNull String token) {
        return tokenManager.getContentFromTokenOrNull(token);
    }

    /**
     * Get the authentication token from the header value.
     *
     * @param authorizationHeader Header value corresponding to the authorization header.
     * @return {@code null} if the type of token is invalid, the token otherwhise.
     */
    private String getTokenFromHeaderAuthValue(String authorizationHeader) {
        String type = TYPE_AUTHORIZATION + " ";
        if (!authorizationHeader.startsWith(type)) {
            log.debug("The authorization header doesn't start by [{}]", type);
            return null;
        }

        return authorizationHeader.substring(type.length());
    }

    /**
     * Get the authorization header from the request.
     *
     * @param request HttpServletRequest Request.
     * @return {@code null} if the header doesn't exist, otherwise the corresponding {@link String} in the header.
     */
    private String getAuthorizationOrNull(@NonNull HttpServletRequest request) {
        return request.getHeader(HEADER_AUTHORIZATION);
    }
}
