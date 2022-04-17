package avl.profiles;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import avl.actors.Player;
import avl.boardgame.Investment;

public class CautiousTest extends PlayerProfileTest {

	@Before
	public void setUp() {
		this.playerProfile = new Cautious();
		player = new Player(this.playerProfile, default_player_amount);

	}

	@Test
	public void checkWillInvestIsOkWhenPlayerCanInvest() {

		Investment investment1 = new Investment((player.computeCapital() * 0.2) - 1, 0);
		assertTrue(investment1.isFree());
		assertTrue(player.willInvest(investment1));

		Investment investment2 = new Investment((player.computeCapital() * 0.2) + 1, 0);
		assertTrue(investment2.isFree());
		assertFalse(player.willInvest(investment2));

	}

	@Test
	public void checkWillInvestIsOkWhenPlayerReachesTheMaximumOfInvestments() {

		PlayerProfile playerProfileWithMaxInvestment = new Cautious(1);
		Player playerOneMaxInvestment = new Player(playerProfileWithMaxInvestment, default_player_amount);

		Investment investment1 = new Investment((playerOneMaxInvestment.computeCapital() * 0.2) - 1, 0);
		assertTrue(investment1.isFree());
		assertTrue(playerOneMaxInvestment.willInvest(investment1));

		playerOneMaxInvestment.addInvestment(investment1);

		Investment investment2 = new Investment((playerOneMaxInvestment.computeCapital() * 0.2) - 1, 0);
		assertTrue(investment2.isFree());
		assertFalse(playerOneMaxInvestment.willInvest(investment2));

	}

	@Test
	public void checkWillInvestIsOkWhenInvestmentIsNotFree() {
		Investment investment1 = new Investment((player.computeCapital() * 0.2) - 1, 0);
		Investment investment2 = new Investment((player.computeCapital() * 0.2) + 1, 0);

		investment1.setOwnerPlayer(new Player(this.playerProfile, default_player_amount));
		investment2.setOwnerPlayer(new Player(this.playerProfile, default_player_amount));

		assertFalse(investment1.isFree());
		assertFalse(investment2.isFree());

		assertFalse(player.willInvest(investment1));
		assertFalse(player.willInvest(investment2));
	}

	@Test
	public void checkWhichInvestmentToExpropriateReturnsMostExpensiveOne() {

		Investment investment1 = new Investment(1217, 0);
		Investment investment2 = new Investment(1213, 0);
		Investment investment3 = new Investment(1201, 0);

		this.player.addInvestment(investment1);
		this.player.addInvestment(investment2);
		this.player.addInvestment(investment3);

		assertSame(this.playerProfile.whichInvestmentToExpropriate(this.player), investment1);
	}

}
