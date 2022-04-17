package avl.profiles;

import avl.actors.Player;
import avl.boardgame.Investment;
import avl.utils.Computations;

public class Cautious implements PlayerProfile {

	private int maxInvestments = Integer.MAX_VALUE;

	public Cautious() {

	}

	public Cautious(int maxInvestments) {
		this.maxInvestments = maxInvestments;
	}

	public int getMaxInvestments() {
		return maxInvestments;
	}

	@Override
	public boolean willInvest(Player player, Investment investment) {
		return investment.isFree() && player.getInvestments().size() < maxInvestments
				&& investment.getValue() <= player.computeCapital() * 0.2;
	}

	@Override
	public Investment whichInvestmentToExpropriate(Player player) {
		Investment investmentToExpropriate = Computations.getTheMostExpensiveInvestment(player.getInvestments());
		return investmentToExpropriate;
	}

}
