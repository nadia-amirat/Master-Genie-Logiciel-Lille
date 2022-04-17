package boardgame;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import avl.actors.Player;
import avl.actors.State;
import avl.boardgame.Subsidy;
import game.Game;

public class SubsidyTest {

	Player player;
	public Subsidy subsidy;

	@Before
	public void setUp() {

		player = new Player(null, 1000);
		subsidy = new Subsidy(500);
		Game.setState(new State(1000));

	}

	@SuppressWarnings("deprecation")
	@Test
	public void SubsidyShouldGivePlayerAmount() {

		assertTrue(player.getAmount() == 1000);
		subsidy.action(player);
		assertTrue(player.getAmount() == 1500.00);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void SubsidyShouldDebitStateAmount() {

		assertTrue(player.getAmount() == 1000);
		subsidy.action(player);
		assertTrue(Game.getState().getAmount() == 500);
	}

}
