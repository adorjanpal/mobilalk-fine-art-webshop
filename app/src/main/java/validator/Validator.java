package validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Validator {
    private final ArrayList<String> errorMessages;
    public Validator(){
        errorMessages = new ArrayList<>();
    }

    public void validateEmpty(HashMap<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            if (value.isEmpty()) {
                this.errorMessages.add(field + " should not be empty!");
            }
        }
    }

    public void validateMatch(String valueFirst, String valueSecond, String fieldName) {
        if (!Objects.equals(valueFirst, valueSecond)) {
            this.errorMessages.add(fieldName + " must match");
        }
    }

    public String getFirstError() {
        return this.errorMessages.get(0);
    }

    public Boolean hasErrors() {
        return !errorMessages.isEmpty();
    }
}
