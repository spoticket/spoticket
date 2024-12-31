package com.spoticket.game.global.util;

import com.spoticket.game.global.exception.CustomException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorUtils {

    public static void validateAndThrowIfInvalid(BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = getErrorMessage(result);
            throw new CustomException(BAD_REQUEST.value(), errorMessage);
        }
    }

    private static String getErrorMessage(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        return fieldErrors.stream()
                .map(
                        fieldError -> fieldError.getField() + " "
                        + fieldError.getDefaultMessage()
                )
                .collect(Collectors.joining(", "));
    }

}
