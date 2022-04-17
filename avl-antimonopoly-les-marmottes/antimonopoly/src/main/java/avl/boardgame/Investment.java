package avl.boardgame;

import avl.actors.Player;
import game.Game;

public class Investment extends Cell {

	private static int count = 0;
	private int id;
	private double value;

	private float interestRate;

	private boolean isFree;
	private Player ownerPlayer;

	public Investment(double value, float interestRate) {
		super();
		this.value = value;
		this.interestRate = interestRate;
		this.isFree = true;
		this.id = count++;

	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Investment.count = count;
	}

	public Player getOwnerPlayer() {
		return ownerPlayer;
	}

	public void setOwnerPlayer(Player ownerPlayer) {
		this.isFree = false;
		this.ownerPlayer = ownerPlayer;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public double getValue() {
		return value;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public void action(Player p) {
		if (this.isFree) {
			if (p.willInvest(this)) {
				p.setAmount(p.getAmount() - this.value);
				double am = Game.getState().getAmount() + this.value;
				Game.getState().setAmount(am);
				this.setOwnerPlayer(p);
				p.addInvestment(this);
			} else {
				// si la cellule est libre et que le joueur décide de ne pas investir
				double gain = p.getAmount() * this.getInterestRate() / 100;
				p.setAmount(p.getAmount() - gain);
				// payer à l'etat un pourcentage
				Game.getState().setAmount(Game.getState().getAmount() + gain);
			}

		} else {
			double gain = p.getAmount() * this.getInterestRate() / 100;
			p.setAmount(p.getAmount() - gain);
			this.ownerPlayer.setAmount(ownerPlayer.getAmount() + gain);
		}
	}

}
