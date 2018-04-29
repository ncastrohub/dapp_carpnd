package model.Exceptions;

import java.util.Map;

public class FormValidationError extends Exception {

    Map<String, String> errors;


    public FormValidationError(Map<String, String> errorList) {
        this.errors = errorList;
    }
}
