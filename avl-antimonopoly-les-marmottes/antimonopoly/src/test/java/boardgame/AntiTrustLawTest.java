package boardgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.stubbing.answers.AnswerReturnValuesAdapter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import avl.actors.Entity;
import avl.actors.Player;
import avl.actors.State;
import avl.boardgame.AntiTrustLaw;
import avl.boardgame.Investment;
import avl.profiles.Aggressive;
import avl.profiles.PlayerProfile;
import game.Game;
import avl.boardgame.Cell;

@RunWith(MockitoJUnitRunner.class)
public class AntiTrustLawTest {

	private AntiTrustLaw antiTrust;
	Player player;
	Player mockitoPlayer;

	@Before
	public void setUp() {
		player = new Player(null, 100);
		player.setPlayerProfile(new Aggressive());
		mockitoPlayer = Mockito.spy(player);
		antiTrust = new AntiTrustLaw();

	}

	@Test
	public void AntitrustLawShouldNotRemoveAnyInvestesment() {

		//
		Investment inv = new Investment(100, 10);
		inv.setId(1);
		mockitoPlayer.addInvestment(inv);
		assertTrue(mockitoPlayer.getInvestments().size() == 1);
		Mockito.when(mockitoPlayer.whichInvestmentToExpropriate()).thenReturn(inv);
		Game.setState(new State(1000));
		Game.getState().setMaxInvestments(1000);
		antiTrust.action(mockitoPlayer);
		assertTrue(mockitoPlayer.getInvestments().size() == 1);

	}

	@Test
	public void AntitrustLawShouldRemoveFirstInvestesment() {

		//
		Investment inv = new Investment(100, 10);
		inv.setId(1);
		mockitoPlayer.addInvestment(inv);
		assertTrue(mockitoPlayer.getInvestments().size() == 1);
		Mockito.when(mockitoPlayer.whichInvestmentToExpropriate()).thenReturn(inv);
		Game.setState(new State(1000));
		Game.getState().setMaxInvestments(10);
		antiTrust.action(mockitoPlayer);
		assertTrue(mockitoPlayer.getInvestments().size() == 0);
	}

	@Test
	public void AntitrustLawShouldRemoveFirstAndLastInvestesment() {

		// first investisment
		Investment inv1 = new Investment(500, 10);
		inv1.setId(1);
		mockitoPlayer.addInvestment(inv1);
		assertTrue(mockitoPlayer.getInvestments().size() == 1);

		Investment inv2 = new Investment(500, 15);
		inv2.setId(2);
		mockitoPlayer.addInvestment(inv2);
		assertTrue(mockitoPlayer.getInvestments().size() == 2);

		Investment inv3 = new Investment(500, 20);
		inv3.setId(3);
		mockitoPlayer.addInvestment(inv3);
		assertTrue(mockitoPlayer.getInvestments().size() == 3);

		Mockito.when(mockitoPlayer.whichInvestmentToExpropriate()).thenReturn(inv1, inv3, inv2);

		Game.setState(new State(1000));
		Game.getState().setMaxInvestments(999);
		antiTrust.action(mockitoPlayer);
		assertTrue(mockitoPlayer.getInvestments().size() == 1);
		assertEquals(mockitoPlayer.getInvestments().entrySet().iterator().next().getValue(), inv2);
	}

}
