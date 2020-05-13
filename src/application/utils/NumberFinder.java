package application.utils;


public class NumberFinder {
	/*
	 * This class takes a string and finds a values we need to calculate rabat
	 * discount.
	 */
	public NumberFinder() {

	}

	public Double[] findNumbers(String fullString) {
		String temp = getCleanStringOfNumbersAndDots(fullString);
		int firstDot = temp.indexOf('.');
		String firstNumber = temp.substring(0, firstDot + 3);
		String restOfString = temp.substring(firstDot + 3);
		int secondDot = restOfString.indexOf('.');
		String secondNumber = restOfString.substring(0, secondDot + 3);
		return createAnArrayOfDoubles(firstNumber, secondNumber);
	}

	private Double[] createAnArrayOfDoubles(String firstNumber, String secondNumber) {
		Double[] numbers = new Double[2];
		numbers[0] = Double.parseDouble(firstNumber);
		numbers[1] = Double.parseDouble(secondNumber);
		return numbers;
	}

	private String getCleanStringOfNumbersAndDots(String s) {
		int startIndex = s.indexOf("Celkem v");
		return s.substring(startIndex + 15, startIndex + 35).trim().replace(" ", "");
	}

}