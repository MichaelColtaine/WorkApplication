package test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.calculator.RabatCalculator;

class CalculatorTest {

	RabatCalculator tester;

	@BeforeEach
	void setUp() throws Exception {
		this.tester = new RabatCalculator();
	}

	@Test
	void test() {
		assertEquals("-39.76", tester.getResult("14625", "8809,9"));
	}

}
