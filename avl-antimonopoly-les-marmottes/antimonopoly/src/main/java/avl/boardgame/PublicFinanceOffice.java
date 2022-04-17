package avl.boardgame;

import avl.actors.Player;
import game.Game;

public class PublicFinanceOffice extends Cell {

	private double maxCapitalAllowed;

	public PublicFinanceOffice(double interestRate) {
		this.interestRate = interestRate;
	}

	public double getMaxCapitalAllowed() {
		return maxCapitalAllowed;
	}

	public void setMaxCapitalAllowed(double maxCapitalAllowed) {
		this.maxCapitalAllowed = maxCapitalAllowed;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	private double interestRate;

	public PublicFinanceOffice(double maxCapitalAllowed, double interestRate) {
		this.maxCapitalAllowed = maxCapitalAllowed;
		this.interestRate = interestRate;
	}

	@Override
	public void action(Player p) {
		boolean b = p.getAmount() > Game.getState().getMaxShareholderEquity();
		if (b) {
			double gain = p.getAmount() * this.interestRate;
			double newPlayerAmount = p.getAmount() - gain;
			p.setAmount(newPlayerAmount);
			double newStateAmount = Game.getState().getAmount() + gain;
			Game.getState().setAmount(newStateAmount);
			return;
		}

	}

}
