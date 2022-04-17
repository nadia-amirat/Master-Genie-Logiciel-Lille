package avl.profiles;

import avl.actors.Player;
import avl.boardgame.Investment;
import avl.utils.Computations;

public class Aggressive implements PlayerProfile {

	@Override
	public boolean willInvest(Player player, Investment investment) {
		return investment.isFree() && (player.getAmount() >= investment.getValue());
	}

	@Override
	public Investment whichInvestmentToExpropriate(Player player) {
		Investment investmentToExpropriate = Computations.getCheapestInvestment(player.getInvestments());
		return investmentToExpropriate;
	}

}
