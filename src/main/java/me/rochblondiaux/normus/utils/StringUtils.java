package me.rochblondiaux.normus.utils;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class StringUtils {

    public static int getLastSpace(String str, int index) {
        for (int i = index; i < str.length(); i++) {
            if (str.charAt(i) == ' ' && (i + 1 >= str.length() || str.charAt(i + 1) == ' '))
                return (i);
        }
        return (index);
    }

    public static String replace(String str, int index, String replacement) {
        StringBuilder stringBuilder = new StringBuilder();
        int l = getLastSpace(str, index - 2);
        for (int i = 0; i < l; i++) {
            if (str.charAt(i) == ' ')
                stringBuilder.append(replacement);
            else
                stringBuilder.append(str.charAt(i));
        }
        stringBuilder.append(str, l, str.length());
        return stringBuilder.toString();
    }
}
