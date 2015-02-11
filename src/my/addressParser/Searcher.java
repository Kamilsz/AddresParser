package my.addressParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Searcher {

	private String input_text;


	public String getInput_text() {
		return input_text;
	}

	public void setInput_text(String input_text) {
		this.input_text = input_text;
	}

	public String streetNumber() {
		//returns Street number if found. Otherwise, returns null
		String pattern = "(^|\\s|/)\\d{1,4}\\s";
		String initialResult = finder(pattern);
		if (initialResult != null && initialResult.contains("/")) {
			String output = initialResult.substring(1, initialResult.length());
			return output;
		}
		return finder(pattern);
	}

	public String flatNumber() {
		String pattern = "\\d{1,4}/";
		String initialResult = finder(pattern);
		if (initialResult.contains("/")){
			int slashLocation = initialResult.indexOf('/');
			String output = initialResult.substring(0, slashLocation);
			return output;
		}
		return null;
	}

	public String streetType() {
		//regex to find street type s[a-zA-Z]{2,}$"
		String pattern = "\\s[a-zA-Z]{2,}$";
		return finder(pattern);
	}
	
	public String streetName() {
		//regex to find streetName is D{4,}
		String pattern = "\\D{4,}";
		String output = null;
		String streetT = streetType();
		String initialResult = finder(pattern);
		if (streetT != null && initialResult != null && initialResult.contains(streetT)){
		int typeIndex = initialResult.indexOf(streetT);
		
		output = initialResult.substring(0, typeIndex);
		}
		else{
			output = initialResult;
		}
		return output;
		
	}

	public String suburb() {
		String pattern = "(\\s|,\\s|\\d{4},\\s|\\d{4}\\s)*[a-zA-Z]{4,}$";
		String initialResult = finder(pattern);
		if (initialResult != null){
		int firstCodeDigit = initialResult.indexOf("3");
		String output = initialResult.substring(firstCodeDigit+5);
		return output;
		}
		return null;
	}

	public String postCode() {
		String pattern = "3\\d{3}";
		return finder(pattern);
	}

	public boolean isPOBox() {
		String pattern = "\\s(B|b)(O|o)(x|X)\\s";
		boolean box = false;
		if (finder(pattern) != null) {
			box = true;
		}
		return box;
	}
	
	public boolean isLockedBag(){
		String pattern = "\\s(B|b)(A|a)(G|g)\\s";
		boolean bag = false;
		if (finder(pattern) != null) {
			bag = true;
		}
		return bag;
	}

	private String finder(String input) {		
		String result = null;
		Pattern pattern = Pattern.compile(input);
		Matcher matcher = pattern.matcher(input_text);
		if (matcher.find()) {
			result = matcher.group();
		}
		System.out.println(result);
		return result;
	}
}
