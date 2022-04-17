package avl.boardgame;

import avl.actors.Player;
import game.Game;

public class Subsidy extends Cell {

	private double amount;

	public Subsidy(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public void action(Player p) {
		// TODO Auto-generated method stub
		p.setAmount(p.getAmount() + this.amount);
		Game.getState().setAmount(Game.getState().getAmount() - this.amount);

	}

}
