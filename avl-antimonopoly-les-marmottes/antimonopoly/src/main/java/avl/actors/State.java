package avl.actors;

public class State extends Entity {

	/* initiall money */

	/* valeur maximale des investissements */
	private double maxInvestments;

	/* Seuil maximale des capitaux propres cf Bureau Finances publiques */
	private double maxShareholderEquity;

	public State(double amount) {
		super(amount);
	}

	public State(double amount, double maxInvestments, double maxShareholderEquity) {
		super(amount);
		this.maxInvestments = maxInvestments;
		this.maxShareholderEquity = maxShareholderEquity;
	}

	/**
	 * Returns the maximum number of the investments defined by the State
	 * 
	 * @return the maximum number of the investments defined by the State
	 */
	public double getMaxInvestments() {
		return maxInvestments;
	}

	public void setMaxInvestments(double max) {
		maxInvestments = max;
	}

	/**
	 * Returns the maximum Shareholder Equity defined by the State
	 * 
	 * @return the maximum Shareholder Equity defined by the State
	 */
	public double getMaxShareholderEquity() {
		return maxShareholderEquity;
	}

}
