package avl.actors;

import java.util.HashMap;

import avl.boardgame.Investment;
import avl.utils.Computations;

/**
 * Abstract class that represents both of State and Player
 * 
 * @author Liticia MEDJOUDJ
 *
 */
public abstract class Entity {

	protected double amount;
	protected HashMap<Integer, Investment> investments;
	protected boolean losed;

	public Entity(double amount) {
		this.amount = amount;
		this.investments = new HashMap<Integer, Investment>();
		losed = false;
	}

	public boolean getLosed() {
		return losed;
	}

	public void setLosed(boolean losed) {
		this.losed = losed;
	}

	/**
	 * Returns the amount of money the entity holds
	 * 
	 * @return amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Sets the specified amount of money for the entity
	 * 
	 * @param amount, the amount of money
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * Returns all the investments the entity holds
	 * 
	 * @return investments
	 */
	public HashMap<Integer, Investment> getInvestments() {
		return this.investments;
	}

	/**
	 * Returns the specified investment if the entity holds it, null otherwise
	 * 
	 * @param id, the id of the investment to get
	 * @return investment or null
	 */
	public Investment getInvestment(int id) {
		return investments.get(id);
	}

	/**
	 * Adds a new investment for the entity
	 * 
	 * @param investment, the investment to add
	 */
	public void addInvestment(Investment investment) {
		this.investments.put(investment.getId(), investment);
	}

	/**
	 * Removes the specified investment from the entity
	 * 
	 * @param id, the id of the investment to remove
	 * @return the removed investment, null if the specified investment is not
	 *         present
	 */
	public Investment removeInvestment(int id) {
		return this.investments.remove(id);
	}

	public Investment removeInvestment(Investment investment) {
		return this.investments.remove(investment);
	}

	/**
	 * Returns the number of investments the entity holds
	 * 
	 * @return the number of investments the entity holds
	 */
	public int getNumberOfInvestments() {
		return investments.size();
	}

	public double computeCapital() {
		return this.amount + Computations.totalValueOfInvestments(this.investments);
	}

	public void checkLoose() {
		if (computeCapital() <= 0)
			this.losed = true;
	}

	public double getInvestismentsTotalValue() {
		double total = 0;
		for (Investment inv : investments.values()) {
			total += inv.getValue();
		}
		return total;

	}

}
