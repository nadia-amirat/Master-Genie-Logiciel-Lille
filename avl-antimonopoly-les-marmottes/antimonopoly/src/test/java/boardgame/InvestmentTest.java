package boardgame;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import avl.actors.Player;
import avl.actors.State;
import avl.boardgame.Investment;
import avl.profiles.Aggressive;
import game.Game;

/**
 * @author AMIRAT Nadia
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class InvestmentTest {
	Player player = null;
	Player mockPlayer = null;
	Player p2;
	private Investment investment;

	@Before
	public void setUp() {
		player = new Player(new Aggressive(), 1000.0);
		p2 = new Player(new Aggressive(), 1000.0);
		investment = new Investment(50, 10);
		investment.setOwnerPlayer(null);
		mockPlayer = Mockito.spy(player);
		Game.setState(new State(1000));
	}

	@Test
	public void testShouldDebitPlayerAmountWhenHeInvest() {

		investment.setFree(true);
		Mockito.when(mockPlayer.willInvest(investment)).thenReturn(true);
		assertTrue(mockPlayer.getAmount() == 1000);
		investment.action(mockPlayer);
		assertTrue(mockPlayer.getAmount() == 950.0);

	}

	@Test
	public void testShouldCreditStateAmountWhenPlayerInvest() {

		investment.setFree(true);
		Mockito.when(mockPlayer.willInvest(investment)).thenReturn(true);
		assertTrue(Game.getState().getAmount() == 1000);
		investment.action(mockPlayer);
		assertTrue(Game.getState().getAmount() == 1050);

	}

	@Test
	public void testDebitPlayerAmountWhenHePayTaxToState() {
		investment.setFree(true);
		Mockito.when(mockPlayer.willInvest(investment)).thenReturn(false);
		assertTrue(mockPlayer.getAmount() == 1000);
		investment.action(mockPlayer);
		assertTrue(mockPlayer.getAmount() == 900);

	}

	@Test
	public void testCreditStateWhenPlayerPayTax() {
		investment.setFree(true);
		Mockito.when(mockPlayer.willInvest(investment)).thenReturn(false);
		assertTrue(Game.getState().getAmount() == 1000);
		investment.action(mockPlayer);
		assertTrue(Game.getState().getAmount() == 1100);

	}

	@Test
	public void testCreditOwnerWhenPlayerPayTaxToOwner() {
		investment.setFree(false);
		investment.setOwnerPlayer(p2);
		assertTrue(p2.getAmount() == 1000);
		investment.action(player);
		assertTrue(investment.getOwnerPlayer() == p2);
		assertTrue(p2.getAmount() == 1100);

	}

	@Test
	public void testDebitPlayerWhenHePayTaxToOwner() {
		investment.setFree(false);
		investment.setOwnerPlayer(p2);
		assertTrue(player.getAmount() == 1000);
		investment.action(player);
		assertTrue(player.getAmount() == 900);

	}
}
