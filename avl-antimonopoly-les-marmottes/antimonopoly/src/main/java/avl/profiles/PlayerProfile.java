package avl.profiles;

import avl.actors.Player;
import avl.boardgame.Investment;

public interface PlayerProfile {

	/**
	 * Returns True if the player can invest, False otherwise
	 * 
	 * @param player,     the player who wants to invest
	 * @param investment, the investment
	 * @return True if the player can invest, False otherwise
	 */
	public boolean willInvest(Player player, Investment investment);

	/**
	 * Returns the Investment to expropriate
	 * 
	 * @param player, the player who has to expropriate an investment
	 * @return the Investment to expropriate
	 * @throws NoExistingInvestmentException if the player doesn't have any
	 */
	public Investment whichInvestmentToExpropriate(Player player);
}
