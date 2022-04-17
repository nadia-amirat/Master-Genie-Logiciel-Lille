package Game;

import static org.junit.Assert.*;
import config.ConfigGame;

import java.util.ArrayList;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import avl.actors.Player;
import avl.actors.State;
import avl.profiles.Aggressive;
import game.Game;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {

	Player p, mockPlayer = null;
	Game game = null;
	ArrayList<Player> players = null;

	State state;
	Player currentPlayer;
	ArrayList<ConfigGame> configs = new ArrayList<>();
	int currentPLayerIndex;
	Scanner sc = new Scanner(System.in);

	@Before
	public void setUp() {
		configs.add(ConfigGame.NEOLIBERAL);
		configs.add(ConfigGame.CAPITALIST);
		state = new State(100);
		p = new Player(new Aggressive(), 1000.0);
		mockPlayer = Mockito.spy(p);
		game = new Game(2, 2, 100, configs);
	}

	@Test
	public void testInitializationStateAndPlayersListOK() {
		assertTrue(game.getState().getAmount() == 100.0);
		assertTrue(game.getPlayers().size() == 4);
		assertEquals(game.getPlayers().get(0).getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(game.getPlayers().get(1).getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(game.getPlayers().get(2).getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(game.getPlayers().get(3).getPlayerProfile().getClass(), Aggressive.class);
	}

	@Test
	public void testInitializationConfigsOK() {
		assertTrue(game.getConfigs().size() == 2);
		assertTrue(game.getConfigs().get(0).toString() == "NEOLIBERAL");
		assertTrue(game.getConfigs().get(1).toString() == "CAPITALIST");
	}

	@Test
	public void testGameEndsWhenStateAmountAreZero() {
		game.getState().setAmount(0.0);
		assertTrue(game.isEnd(sc) == true);

	}

	@Test
	public void testGameEndsWhenThereIsJustOnePlayer() {
		ArrayList<Player> playerss = new ArrayList<>();
		playerss.add(new Player(null, 10));
		game.setPlayers(playerss);
		assertTrue(game.getPlayers().size() == 1);
		assertTrue(game.isEnd(sc) == true);
	}

	@Test
	public void testGameCannotBeStartedWhenThereIsOnePlayerToPlay() {
		ArrayList<Player> loosers = new ArrayList<>();
		Player mockPlayer1 = Mockito.spy(p);
		Player mockPlayer2 = Mockito.spy(p);
		mockPlayer2.setId(2);
		loosers.add(mockPlayer2);
		loosers.add(mockPlayer1);
		assertTrue(loosers.size() == 2);
		game.setPlayers(loosers);
		Mockito.when(mockPlayer1.getLosed()).thenReturn(true);
		assertFalse(game.canPlay());

	}

	@Test
	public void testGameCannotBeStartedWhenThereIsZeroPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		assertTrue(players.size() == 0);
		game.setPlayers(players);
		assertFalse(game.canPlay());
	}

	@Test
	public void testGameReturnsWinnerOftheGame() {
		ArrayList<Player> players = new ArrayList<>();

		Player p1 = new Player(new Aggressive(), 1500.0);
		Player p2 = new Player(new Aggressive(), 100.0);
		Player p3 = new Player(new Aggressive(), 300.0);

		p1.setName("Nadia");
		assertTrue(p1.getName() == "Nadia");
		players.add(p1);
		players.add(p2);
		players.add(p3);
		assertTrue(players.size() == 3);
		game.setPlayers(players);

		assertEquals(game.getWinner().getName(), "Nadia");

	}

	@Test
	public void testOrderOfPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		Player p1 = new Player(new Aggressive(), 1500.0);
		Player p2 = new Player(new Aggressive(), 1000.0);
		Player p3 = new Player(new Aggressive(), 300.0);
		p1.setName("Nadia");
		p2.setName("Liticia");
		p3.setName("Ali");
		players.add(p1);
		players.add(p2);
		players.add(p3);
		game.setPlayers(players);
		assertEquals(game.getPlayersOrder().get(0).getName(), "Nadia");
		assertEquals(game.getPlayersOrder().get(1).getName(), "Liticia");
		assertEquals(game.getPlayersOrder().get(2).getName(), "Ali");
	}

	@Test
	public void testNextPlayerPlaysTheGameOK() {
		ArrayList<Player> players = new ArrayList<>();
		Player p1 = new Player(new Aggressive(), 1500.0);
		Player p2 = new Player(new Aggressive(), 1000.0);
		Player p3 = new Player(new Aggressive(), 300.0);
		p1.setName("Nadia");
		p2.setName("Liticia");
		p3.setName("Ali");
		players.add(p1);
		players.add(p2);
		players.add(p3);
		game.setPlayers(players);
		game.setCurrentPLayerIndex(p1.getPosition());
		game.setCurrentPlayer(p1);
		game.nextPlayerPlay(p2, 2);
		assertEquals(game.getCurrentPlayer().getName(), "Liticia");

	}

}
