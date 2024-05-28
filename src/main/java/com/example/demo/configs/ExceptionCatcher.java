package com.example.demo.configs;

import com.example.demo.configs.props.AppInfoProps;
import com.example.demo.utils.client.ClientGlobalVarNames;
import com.example.demo.utils.client.ClientPages;
import com.example.demo.utils.constants.Lengths;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

class ExceptionCatcherHelpers {

    public static String specifyErrorMessage(String error_message) {
        int maxLengthOfErrorMessage = Lengths.maxLengthOfErrorMessage;
        return error_message.length() > maxLengthOfErrorMessage
            ? error_message.substring(0, maxLengthOfErrorMessage) + "..."
            : error_message;
    }

    public static void logException(String exception_title, Exception exception) {
        System.out.printf("\n>>> run this " + exception_title + " catcher");
        System.out.printf("\n>>> :::::::::::::::::::::( \n");
        System.out.print(exception);
        System.out.printf("\n>>> :::::::::::::::::::::) \n");
    }

    public static void logValidationException(MethodArgumentNotValidException exception) {
        System.out.printf("\n>>> run this Method Argument Not Valid Exception catcher\n");
        exception.getBindingResult().getAllErrors().forEach(error -> {
            System.out.printf(">>> error message: %s \n", error.getDefaultMessage());
        });
    }

    public static boolean isAPIRoute(String requestPath) {
        return requestPath.contains("/api/");
    }
}


record ExceptionCatcherResBody(String name, String message) {
}


@ControllerAdvice
public class ExceptionCatcher {

    @Autowired
    private AppInfoProps appInfoProps;

    @ExceptionHandler({Exception.class})
    public Object handleAnyException(Exception exception, Model model, HttpServletRequest request) {
        String exceptionName = "Any Exception";
        String exceptionMessage = ExceptionCatcherHelpers.specifyErrorMessage(exception.getMessage());

        ExceptionCatcherHelpers.logException(exceptionName, exception);

        if (ExceptionCatcherHelpers.isAPIRoute(request.getRequestURI())) {
            return ResponseEntity.status(500).body(new ExceptionCatcherResBody(exceptionName, exceptionMessage));
        }

        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.error, exceptionMessage);

        return ClientPages.internalErrorPage;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, Model model,
        HttpServletRequest request) {
        String exceptionName = "Validation Exception";
        String exceptionMessage = ExceptionCatcherHelpers.specifyErrorMessage(
            "Dữ liệu đầu vào không hợp lệ: " + exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        ExceptionCatcherHelpers.logValidationException(exception);

        if (ExceptionCatcherHelpers.isAPIRoute(request.getRequestURI())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionCatcherResBody(exceptionName, exceptionMessage));
        }

        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.error, exceptionMessage);

        return ClientPages.internalErrorPage;
    }

    @ExceptionHandler({DataAccessException.class})
    public Object handleDataAccessException(DataAccessException exception, Model model, HttpServletRequest request) {
        String exceptionName = "Data Access Exception";
        String exceptionMessage = ExceptionCatcherHelpers.specifyErrorMessage(exception.getMessage());

        ExceptionCatcherHelpers.logException(exceptionName, exception);

        if (ExceptionCatcherHelpers.isAPIRoute(request.getRequestURI())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionCatcherResBody(exceptionName, exceptionMessage));
        }

        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.error, exceptionMessage);

        return ClientPages.internalErrorPage;
    }

    @ExceptionHandler({RuntimeException.class})
    public Object handleRuntimeException(RuntimeException exception, Model model, HttpServletRequest request) {
        String exceptionName = "Runtime Exception";
        String exceptionMessage = ExceptionCatcherHelpers.specifyErrorMessage(exception.getMessage());

        ExceptionCatcherHelpers.logException(exceptionName, exception);

        if (ExceptionCatcherHelpers.isAPIRoute(request.getRequestURI())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionCatcherResBody(exceptionName, exceptionMessage));
        }

        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.error, exceptionMessage);

        if (exception instanceof AccessDeniedException) {
            return ClientPages.unauthorizationPage;
        }

        return ClientPages.internalErrorPage;
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public Object handleNoResourceFoundException(NoResourceFoundException exception, Model model,
        HttpServletRequest request) {
        String exceptionName = "No Resource Found Exception";
        String exceptionMessage = ExceptionCatcherHelpers.specifyErrorMessage(exception.getMessage());

        ExceptionCatcherHelpers.logException(exceptionName, exception);

        if (ExceptionCatcherHelpers.isAPIRoute(request.getRequestURI())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionCatcherResBody(exceptionName, exceptionMessage));
        }

        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.error, exceptionMessage);

        return ClientPages.notFoundPage;
    }
}
