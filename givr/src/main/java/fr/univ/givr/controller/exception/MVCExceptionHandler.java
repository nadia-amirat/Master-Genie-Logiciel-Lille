package fr.univ.givr.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * Controller advice to catch exception in MVC.
 */
@ControllerAdvice(basePackages = "fr.univ.givr.controller.mvc")
@Slf4j
public class MVCExceptionHandler {

    /**
     * Handle an exception not caught.
     *
     * @param ex       Exception.
     * @param response HTTP response.
     * @return A view to the error page with data to display error.
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception ex, HttpServletResponse response) {
        return createErrorWithDefaultOrResponseStatus(response, HttpStatus.INTERNAL_SERVER_ERROR,
                                                      "Une erreur imprévue s'est produite",
                                                      ex
        );
    }

    /**
     * Create a model and view to display information on error page.
     * @param response HTTP response.
     * @param defaultStatus Default http status if the annotation {@link ResponseStatus} is not found.
     * @param defaultError  Default http status if the annotation {@link ResponseStatus} is not found or if the reason is empty.
     * @param exception     Exception thrown.
     * @return The instance of Model and view error.
     */
    private ModelAndView createErrorWithDefaultOrResponseStatus(
            HttpServletResponse response,
            HttpStatus defaultStatus,
            String defaultError,
            Throwable exception
    ) {
        var status = defaultStatus;
        var msgError = defaultError;

        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);

        if (responseStatus != null) {
            status = responseStatus.value();
            String reason = responseStatus.reason();

            if (!reason.isEmpty()) {
                msgError = reason;
            }
        } else {
            log.error("An exception without [{}] annotation has been thrown", ResponseStatus.class, exception);
        }

        return createModelAndViewError(response, status, msgError);
    }

    private ModelAndView createModelAndViewError(HttpServletResponse response, HttpStatus status, String error) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", error);
        mav.setViewName("error");

        response.setStatus(status.value());
        return mav;
    }

}
