package application.calculator;

public class RabatCalculator {

	public RabatCalculator() {
	}

	public String getResult(String priceAsString, String priceWithTaxesAsString) {
		if (isLegitInput(priceAsString.trim()) && isLegitInput(priceWithTaxesAsString.trim())) {
			Double price = Double.valueOf(changeCommaAndRemoveWhiteSpaces(priceAsString));
			Double priceWithTax = Double.valueOf(changeCommaAndRemoveWhiteSpaces(priceWithTaxesAsString));
			Double result = calculate(price, priceWithTax);
			if (isNegative(result)) {
				return String.format("%.2f", result);
			}
			return String.format("-%.2f", result);
		}
		return "";
	}
	
	

	private boolean isLegitInput(String s) {
		return isANumber(s) && !s.isEmpty();
	}

	private boolean isANumber(String s) {
		int commaCount = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '.' || s.charAt(i) == ',') {
				commaCount++;
			}
			if (Character.isLetter(s.charAt(i)) && s.charAt(i) != '.' && s.charAt(i) != ',' && commaCount > 1) {
				return false;
			}
		}
		return true;
	}

	private String changeCommaAndRemoveWhiteSpaces(String input) {
		input = input.replaceAll(",", ".");
		return input.replaceAll(" ", "");
	}

	private double calculate(Double price, Double priceWithTax) {
		Double result = (price - priceWithTax) / (price * 0.01);
		return result;
	}

	private boolean isNegative(Double s) {
		double temp = Double.valueOf(s);
		return temp < 0;
	}

}
