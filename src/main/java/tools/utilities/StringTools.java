package tools.utilities;

public class StringTools {

    public static boolean isNullEmptyBlankString(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }

}
