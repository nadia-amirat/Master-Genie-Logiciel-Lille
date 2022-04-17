package avl.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import avl.boardgame.Investment;

/**
 * Computations test class
 * 
 * @author Liticia MEDJOUDJ
 *
 */
public class ComputationsTest {

	private int default_nb_investments = 17;
	private double default_investment_value = 9743;
	private float default_investment_interest_rate = 0;

	@Before
	public void setUp() {

	}

	@Test
	public void checkTotalValueOfInvestmentsAsArrayListIsOk() {
		ArrayList<Investment> investments = new ArrayList<>();
		for (int i = 0; i < default_nb_investments; i++)
			investments.add(new Investment(default_investment_value, default_investment_interest_rate));

		assertTrue(
				Computations.totalValueOfInvestments(investments) == default_investment_value * default_nb_investments);
	}

	@Test
	public void checkTotalValueOfInvestmentsAsHashMapIsOk() {
		HashMap<Integer, Investment> investments = new HashMap<Integer, Investment>();
		for (int i = 0; i < default_nb_investments; i++) {
			Investment investment = new Investment(default_investment_value, default_investment_interest_rate);
			investments.put(investment.getId(), investment);
		}

		assertTrue(
				Computations.totalValueOfInvestments(investments) == default_investment_value * default_nb_investments);
	}

	@Test
	public void checkGetTheMostExpensiveInvestmentIsOk() {
		HashMap<Integer, Investment> investments = new HashMap<Integer, Investment>();
		for (int i = 0; i < default_nb_investments; i++) {
			Investment investment = new Investment(i * 1201, 0);
			investments.put(investment.getId(), investment);
		}
		assertEquals(Computations.getTheMostExpensiveInvestment(investments).getValue(),
				1201 * (default_nb_investments - 1), 0.00001);

	}

	@Test
	public void checkGetCheapestInvestmentIsOk() {
		HashMap<Integer, Investment> investments = new HashMap<Integer, Investment>();
		for (int i = 0; i < default_nb_investments; i++) {
			Investment investment = new Investment(i * 1201, 0);
			investments.put(investment.getId(), investment);
		}
		assertEquals(Computations.getCheapestInvestment(investments).getValue(), 0, 0.00001);
	}

}
