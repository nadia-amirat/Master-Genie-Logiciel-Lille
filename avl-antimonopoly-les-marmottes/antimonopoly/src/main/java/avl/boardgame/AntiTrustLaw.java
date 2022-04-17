package avl.boardgame;

import avl.actors.Player;
import avl.utils.Computations;
import game.Game;

public class AntiTrustLaw extends Cell {

	@Override
	public void action(Player p) {

		while (Computations.totalValueOfInvestments(p.getInvestments()) > Game.getState().getMaxInvestments()) {
			Investment invToRemove = p.whichInvestmentToExpropriate();
			p.removeInvestment(invToRemove.getId());
			p.setAmount(p.getAmount() + invToRemove.getValue() / 2);
			Game.getState().setAmount(Game.getState().getAmount() - invToRemove.getValue() / 2);
			invToRemove.setOwnerPlayer(null);
		}

	}

}
