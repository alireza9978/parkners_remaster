package me.coleo.snapion.constants;

/**
 * تبدیل اعداد فارسی
 */
public class FormatHelper {

    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};

    public static String toPersianNumber(String text) {
        if (text.length() == 0) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out.append(persianNumbers[number]);
            } else if (c == '٫') {
                out.append('،');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
