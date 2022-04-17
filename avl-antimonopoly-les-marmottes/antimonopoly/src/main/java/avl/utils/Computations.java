package avl.utils;

import java.util.ArrayList;
import java.util.HashMap;

import avl.boardgame.Investment;

/**
 * Helper class that do all the computations
 * 
 * @author Liticia MEDJOUDJ
 *
 */
public class Computations {

	/**
	 * Returns the total value of given investments
	 * 
	 * @param investments HashMap
	 * @return total value of given investments
	 */
	public static double totalValueOfInvestments(HashMap<Integer, Investment> investments) {
		double res = 0;
		for (Investment investment : investments.values()) {
			res += investment.getValue();
		}
		return res;
	}

	/**
	 * Returns the total value of given investments
	 * 
	 * @param investments ArrayList
	 * @return total value of given investments
	 */
	public static double totalValueOfInvestments(ArrayList<Investment> investments) {
		double res = 0;
		for (Investment investment : investments) {
			res += investment.getValue();
		}
		return res;
	}

	/**
	 * Returns the cheapest investment
	 * 
	 * @param investments, hashmap containing investments as values and their ids as
	 *                     keys
	 * @return the cheapest investment
	 */
	public static Investment getCheapestInvestment(HashMap<Integer, Investment> investments) {

		int id_cheapest_investment = (int) investments.keySet().toArray()[0];
		Investment cheapest_investment = investments.get(id_cheapest_investment);
		for (Investment investment : investments.values()) {
			if (investment.getValue() < cheapest_investment.getValue())
				cheapest_investment = investment;
		}
		return cheapest_investment;
	}

	/**
	 * Returns the most expensive investment
	 * 
	 * @param investments, hashmap containing investments as values and their ids as
	 *                     keys
	 * @return the most expensive investment
	 */
	public static Investment getTheMostExpensiveInvestment(HashMap<Integer, Investment> investments) {
		int id_most_expensive_investment = (int) investments.keySet().toArray()[0];
		Investment most_expensive_investment = investments.get(id_most_expensive_investment);
		for (Investment investment : investments.values()) {
			if (investment.getValue() > most_expensive_investment.getValue())
				most_expensive_investment = investment;
		}
		return most_expensive_investment;
	}
}
