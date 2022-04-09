package io.rently.searchservice.exceptions;

import io.rently.searchservice.dtos.ResponseContent;
import io.rently.searchservice.utils.Broadcaster;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public static ResponseContent handleInvalidURI(HttpServletResponse response, MissingServletRequestParameterException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        response.setStatus(status.value());
        String msg = "Missing required query string parameter `" + ex.getParameterName() + "` of type "+
                ex.getParameterType() +" in URL. Example: '"+ ex.getParameterName() +"=<" + ex.getParameterType() + " value>'";
        Broadcaster.httpError(msg, status);
        return new ResponseContent.Builder(status).setMessage(msg).build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public static ResponseContent handleResponseException(HttpServletResponse response, ResponseStatusException ex) {
        Broadcaster.httpError(ex);
        response.setStatus(ex.getStatus().value());
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getReason()).build();
    }

}
