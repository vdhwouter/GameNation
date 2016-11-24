package GameNationBackEnd.Validation;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationError {

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private Map<String, String> errors = new HashMap<String, String>();

        private final String errorMessage;

        public ValidationError(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public void addValidationError(String object, String error) {
            errors.put(object, error);
        }

        public Map<String, String> getErrors() {
        return errors;
    }

        public String getErrorMessage() {
            return errorMessage;
        }
    }