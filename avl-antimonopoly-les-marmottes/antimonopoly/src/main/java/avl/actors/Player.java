package avl.actors;

import avl.boardgame.Investment;
import avl.profiles.PlayerProfile;
import avl.utils.Computations;
import game.Game;

public class Player extends Entity {

	private int id;
	private int position;
	private PlayerProfile playerProfile;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Player(PlayerProfile playerProfile, double amount) {
		super(amount);
		this.playerProfile = playerProfile;
		this.id = Game.nbPlayer++;
		this.position = 0;
		losed = false;
	}

	/**
	 * Returns the id of the player
	 * 
	 * @return the id of the player
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the position of the player
	 * 
	 * @return the position of the player
	 */
	public int getPosition() {
		return position;
	}

	public PlayerProfile getPlayerProfile() {
		return playerProfile;
	}

	public void setPlayerProfile(PlayerProfile playerProfile) {
		this.playerProfile = playerProfile;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets a new position for the player
	 * 
	 * @param position, the new position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Returns the capital of the player
	 * 
	 * @return the capital of the player
	 */
	public double computeCapital() {
		return this.amount + Computations.totalValueOfInvestments(this.investments);
	}

	public boolean willInvest(Investment investment) {

		return this.playerProfile.willInvest(this, investment);
	}

	public Investment whichInvestmentToExpropriate() {
		return this.playerProfile.whichInvestmentToExpropriate(this);
	}

}
