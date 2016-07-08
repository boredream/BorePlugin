package utils;

import java.util.Locale;

public class StringUtils {

	public static String firstToUpperCase(String key) {
		return key.substring(0, 1).toUpperCase(Locale.CHINA) + key.substring(1);
	}
}
