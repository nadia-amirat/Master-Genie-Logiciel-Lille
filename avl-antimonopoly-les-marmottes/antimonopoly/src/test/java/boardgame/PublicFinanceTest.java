package boardgame;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import avl.actors.Player;
import avl.actors.State;
import avl.boardgame.PublicFinanceOffice;
import game.Game;

public class PublicFinanceTest {
	Player player = null;
	public PublicFinanceOffice pf;

	@Before
	public void setUp() {
		player = new Player(null, 1000);
		pf = new PublicFinanceOffice(900, 1);
		Game.setState(new State(1000, 950, 800));

	}

	@Test
	public void testInitialisationIsOK() {
		assertTrue(player.getAmount() == 1000);
		assertTrue(pf.getInterestRate() == 1);
		assertTrue(Game.getState().getMaxShareholderEquity() == 800);
	}

	@Test
	public void testDebitPlayerWhenAmountUpperThanMaxShareholderEquity() {
		assertTrue(player.getAmount() == 1000);
		pf.action(player);

		assertTrue(player.getAmount() == 0);

	}

	@Test
	public void testCreditStateWhenPlaerAmountUpperThanMaxShareholderEquity() {
		assertTrue(Game.getState().getAmount() == 1000);
		pf.action(player);
		assertTrue(Game.getState().getAmount() == 2000);

	}

	@Test
	public void testNotDebitPlayerWhenAmountLowerThanMaxShareholderEquity() {
		player.setAmount(500);
		pf.action(player);
		assertTrue(player.getAmount() == 500);

	}

	@Test
	public void testNotCreditStateWhenPlayerAmountLowerThanMaxShareholderEquity() {
		player.setAmount(500);
		assertTrue(Game.getState().getMaxShareholderEquity() == 800);
		pf.action(player);
		assertTrue(Game.getState().getAmount() == 1000);

	}

}
