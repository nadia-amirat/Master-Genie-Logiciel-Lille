package fr.univ.givr.utils;

import com.google.common.net.HostAndPort;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Objects;

/**
 * Class containing all methods utils to manage a response for an HTTP request.
 */
@Slf4j
public final class URIUtils {

    /**
     * Header protocole (ex : http, https).
     */
    public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";

    /**
     * Header host propagated.
     */
    public static final String X_FORWARDED_HOST = "X-Forwarded-Host";

    /**
     * Header port propagated.
     */
    public static final String X_FORWARDED_PORT = "X-Forwarded-Port";

    /**
     * Header path.
     */
    public static final String X_FORWARDED_PATH = "X-Forwarded-Path";

    private URIUtils() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    /**
     * Get the root of the request <i>Example : http://localhost:8080</i>.
     *
     * @param httpHeaders Header of HTTP request.
     * @return A new instance of UriBuilder with all informations about the root uri (scheme, http..).
     * @see #getUriBuilder(HttpHeaders).
     */
    @NonNull
    public static UriBuilder getRootURI(@NonNull HttpHeaders httpHeaders) {
        UriBuilder builder = getUriBuilder(httpHeaders);
        builder.path(getForwardedPath(httpHeaders));
        return builder;
    }

    /**
     * Create a default {@link UriBuilder} with the host and port presents in {@link HttpHeaders} sent in parameter.
     *
     * @param httpHeaders Header of HTTP request.
     * @return A new instance of UriBuilder with the header's scheme.
     * @see #getScheme(HttpHeaders).
     */
    @NonNull
    private static UriBuilder getUriBuilder(@NonNull HttpHeaders httpHeaders) {
        log.debug("headers {} ", httpHeaders);
        UriBuilder builder = new DefaultUriBuilderFactory().builder();
        builder.scheme(getScheme(httpHeaders));

        HostAndPort hostAndPort = getHostAndPort(httpHeaders);
        if (Objects.nonNull(hostAndPort)) {
            builder.host(hostAndPort.getHost());
            if (hostAndPort.hasPort()) {
                builder.port(hostAndPort.getPort());
            }
        }
        return builder;
    }

    /**
     * Get the scheme of the request.
     *
     * @param httpHeaders Header of the request.
     * @return The value 'https' if the request is an HTTPS request, 'http' otherwise.
     */
    @NonNull
    private static String getScheme(@NonNull HttpHeaders httpHeaders) {
        List<String> valuesHeader = httpHeaders.get(X_FORWARDED_PROTO);
        if (CollectionUtils.isEmpty(valuesHeader) || !"https".equals(valuesHeader.get(0))) {
            return "http";
        }
        return "https";
    }

    /**
     * Get the value of {@link #X_FORWARDED_PATH} in the header.
     *
     * @param httpHeaders Header of the request.
     * @return the value of {@link #X_FORWARDED_PATH}, an empty string if the value is null or empty.
     */
    @NonNull
    public static String getForwardedPath(@NonNull HttpHeaders httpHeaders) {
        List<String> valuesHeaderXForwPath = httpHeaders.get(X_FORWARDED_PATH);
        return CollectionUtils.isEmpty(valuesHeaderXForwPath) ? "" : valuesHeaderXForwPath.get(0);
    }

    /**
     * Get the host and port in the request header.
     *
     * @param httpHeaders Header of the request.
     * @return A new instance of {@link HostAndPort} with the host if it is found, and the port if it is different to [-1,
     * 80, 443].
     */
    private static HostAndPort getHostAndPort(@NonNull HttpHeaders httpHeaders) {
        List<String> hostsChain;
        List<String> valuesHeaderXForwHost = httpHeaders.get(X_FORWARDED_HOST);
        if (CollectionUtils.isEmpty(valuesHeaderXForwHost)) {
            hostsChain = httpHeaders.get(HttpHeaders.HOST);
        } else {
            hostsChain = valuesHeaderXForwHost;
        }

        if (CollectionUtils.isEmpty(hostsChain)) {
            return null;
        }

        String host = hostsChain.get(0);

        List<String> valuesHeaderXForwPort = httpHeaders.get(X_FORWARDED_PORT);
        if (!CollectionUtils.isEmpty(valuesHeaderXForwPort)) {
            String portString = valuesHeaderXForwPort.get(0);
            try {
                int port = Integer.parseInt(portString);
                if (port != 80 && port != 443 && port != -1) {
                    return HostAndPort.fromParts(host, port);
                }
            } catch (NumberFormatException exception) {
                log.error("Unable to parse to int the port : " + portString);
            }
        }

        return HostAndPort.fromString(host);
    }
}
