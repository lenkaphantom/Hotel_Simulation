package validation;

public abstract class Validation {
	public static boolean isStringEmpty(String text) {
		return text.trim().isEmpty();
	}
	
	public static boolean isNumberNegative(int number) {
		return number < 0;
	}
	
	public static boolean isNumberNegative(double number) {
		return number < 0;
	}
	
	public static boolean isDateNull(java.sql.Date date) {
		return date == null;
	}
	
	public static boolean isDateAfterCurrentDate(java.sql.Date date) {
		return date.after(new java.util.Date());
	}
	
	public static boolean isPasswordEmpty(char[] password) {
		return password.length == 0;
	}
	
	public static boolean isPasswordEmpty(String password) {
		return password.isEmpty();
	}
	
	public static boolean isPasswordShort(char[] password) {
		return password.length < 8;
	}
	
	public static boolean isPasswordShort(String password) {
		return password.length() < 8;
	}
	
	public static boolean isNumberInPassword(char[] password) {
		for (char c : password) {
			if (Character.isDigit(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNumberInPassword(String password) {
		for (char c : password.toCharArray()) {
			if (Character.isDigit(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isLetterInPhoneNumber(String phoneNumber) {
		for (char c : phoneNumber.toCharArray()) {
			if (Character.isLetter(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isCharInText(String text, char c) {
		return text.indexOf(c) != -1;
	}
	
	public static boolean isCharInText(char[] text, char c) {
		for (char ch : text) {
			if (ch == c) {
				return true;
			}
		}
		return false;
	}
}