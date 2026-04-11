package util;

public class ValidateInput {
    public static boolean validateInput(String[] array) {
        if (array == null) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null || array[i].trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}