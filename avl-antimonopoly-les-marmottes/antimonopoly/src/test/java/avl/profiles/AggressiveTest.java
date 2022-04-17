package avl.profiles;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import avl.actors.Player;
import avl.boardgame.Investment;

public class AggressiveTest extends PlayerProfileTest {

	@Before
	public void setUp() {

		this.playerProfile = new Aggressive();
		player = new Player(this.playerProfile, default_player_amount);

	}

	@Test
	public void checkWillInvestIsOkWhenPlayerHasEnoughMoney() {

		Investment investment1 = new Investment(player.getAmount(), 0);
		Investment investment2 = new Investment(player.getAmount() - 1, 0);

		assertTrue(investment1.isFree());
		assertTrue(investment2.isFree());

		assertTrue(player.willInvest(investment1));
		assertTrue(player.willInvest(investment2));
	}

	@Test
	public void checkWillInvestIsOkWhenInvestmentIsNotFree() {
		Investment investment1 = new Investment(player.getAmount() + 1, 0);
		Investment investment2 = new Investment(player.getAmount() - 1, 0);

		investment1.setOwnerPlayer(new Player(this.playerProfile, default_player_amount));
		investment2.setOwnerPlayer(new Player(this.playerProfile, default_player_amount));

		assertFalse(investment1.isFree());
		assertFalse(investment2.isFree());

		assertFalse(player.willInvest(investment1));
		assertFalse(player.willInvest(investment2));
	}

	@Test
	public void checkWhichInvestmentToExpropriateReturnsCheapestOne() {

		Investment investment1 = new Investment(1217, 0);
		Investment investment2 = new Investment(1213, 0);
		Investment investment3 = new Investment(1201, 0);

		this.player.addInvestment(investment1);
		this.player.addInvestment(investment2);
		this.player.addInvestment(investment3);

		assertSame(this.playerProfile.whichInvestmentToExpropriate(this.player), investment3);

	}
}
