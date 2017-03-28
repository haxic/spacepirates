package utility;

public class Utility {
	static public String getFirstWord(String text) {
		if (text.indexOf(' ') > -1) { // Check if there is more than one word.
			return text.substring(0, text.indexOf(' ')); // Extract first word.
		} else {
			return text; // Text is the first word itself.
		}
	}
}
