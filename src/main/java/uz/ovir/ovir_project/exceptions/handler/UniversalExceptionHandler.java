package uz.ovir.ovir_project.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
;
import uz.ovir.ovir_project.exceptions.UniversalException;

@ControllerAdvice
public class UniversalExceptionHandler  {
    @ExceptionHandler(UniversalException.class)
    public String exceptionHandler(UniversalException e, WebRequest request, Model model) {
        model.addAttribute("message",e.getMessage());
    return "error";
    }

//    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
//    public String IllegalArgumentException(HttpClientErrorException.Forbidden e, WebRequest request, Model model) {
//        model.addAttribute("message","Not permission");
//        return "error";
//    }
//    @ExceptionHandler(value = Exception.class)
//    public String AnyOtherHandler(Exception e,Model model) {
//        model.addAttribute("message",e.getMessage());
//        return "error";
//    }

}
