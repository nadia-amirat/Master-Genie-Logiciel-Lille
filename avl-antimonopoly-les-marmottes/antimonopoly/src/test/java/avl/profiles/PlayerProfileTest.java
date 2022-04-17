package avl.profiles;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import avl.actors.Player;
import avl.boardgame.Investment;

public abstract class PlayerProfileTest {

	protected double default_player_amount = 4951;

	protected Player player;
	protected PlayerProfile playerProfile;

	@Test
	public void checkWillInvestIsOkWhenPlayerHasNotEnoughMoney() {
		Investment investment = new Investment(player.getAmount() + 1, 0);
		assertTrue(investment.isFree());
		assertFalse(player.willInvest(investment));
	}

}
