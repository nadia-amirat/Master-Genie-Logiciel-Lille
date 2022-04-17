package Game;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import avl.actors.Player;
import avl.actors.State;
import avl.boardgame.*;
import avl.profiles.Aggressive;

import config.ConfigGame;
import game.Game;

public class GameConfigTest {
	Game game;

	@Before
	public void setUp() {
		Game.setState(new State(1000, 100, 1000));
	}

	@Test
	public void TestCofigNeoLiberalCapitalis() {

		ArrayList<ConfigGame> configs = new ArrayList<>();
		configs.add(ConfigGame.NEOLIBERAL);
		configs.add(ConfigGame.CAPITALIST);

		game = new Game(2, 2, 100, configs);
		assertEquals(game.getPlayers().size(), 4);
		Player pl1 = game.getPlayers().get(0);
		Player pl2 = game.getPlayers().get(1);
		Player pl3 = game.getPlayers().get(2);
		Player pl4 = game.getPlayers().get(3);

		assertEquals(pl1.getAmount(), 100000.00, 0.01);
		assertEquals(pl2.getAmount(), 100.00, 0.01);
		assertEquals(pl3.getAmount(), 100000.00, 0.01);
		assertEquals(pl4.getAmount(), 100.00, 0.01);

		assertEquals(pl1.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl2.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl3.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl4.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(Game.getState().getMaxInvestments(), 4000000, 0.001);

		int i = 0;
		double Invvalues[] = { 15, 70, 20, 17, 12, 20, 20, 20, 30, 15, 13, 30, 10.5, 13, 11, 17 };
		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof Investment) {
				assertEquals(((Investment) cell).getInterestRate(), Invvalues[i], 0.01);
				i++;
			}
		}

	}

	@Test
	public void TestCofigCapitalisNeoLiberal() {

		ArrayList<ConfigGame> configs = new ArrayList<>();

		configs.add(ConfigGame.CAPITALIST);
		configs.add(ConfigGame.NEOLIBERAL);

		game = new Game(2, 2, 100, configs);
		assertEquals(game.getPlayers().size(), 4);
		Player pl1 = game.getPlayers().get(0);
		Player pl2 = game.getPlayers().get(1);
		Player pl3 = game.getPlayers().get(2);
		Player pl4 = game.getPlayers().get(3);

		assertEquals(pl1.getAmount(), 100000.00, 0.01);
		assertEquals(pl2.getAmount(), 100000.00, 0.01);
		assertEquals(pl3.getAmount(), 100.00, 0.01);
		assertEquals(pl4.getAmount(), 100.00, 0.01);

		assertEquals(pl1.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl2.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl3.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl4.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(Game.getState().getMaxInvestments(), 4000000, 0.001);

		int i = 0;
		double Invvalues[] = { 15, 70, 20, 17, 12, 20, 20, 20, 30, 15, 13, 30, 10.5, 13, 11, 17 };
		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof Investment) {
				assertEquals(((Investment) cell).getInterestRate(), Invvalues[i], 0.01);
				i++;
			}
		}

	}

	/*
	 *
	 * 1 - [ NEOLIBERAL, CAPITALIST ] 2 - [ NEOLIBERAL, PROGRESSIST ] 3 - [
	 * SOCIALIST , CAPITALIST ] 4 - [ SOCIALIST , PROGRESSIST ]
	 */
	@Test
	public void TestCofigSocialistCapitalist() {

		ArrayList<ConfigGame> configs = new ArrayList<>();
		configs.add(ConfigGame.SOCIALIST);
		configs.add(ConfigGame.CAPITALIST);

		game = new Game(2, 2, 100, configs);
		assertEquals(game.getPlayers().size(), 4);
		Player pl1 = game.getPlayers().get(0);
		Player pl2 = game.getPlayers().get(1);
		Player pl3 = game.getPlayers().get(2);
		Player pl4 = game.getPlayers().get(3);

		assertEquals(pl1.getAmount(), 10000.00, 0.01);
		assertEquals(pl2.getAmount(), 10000.00, 0.01);
		assertEquals(pl3.getAmount(), 10000.00, 0.01);
		assertEquals(pl4.getAmount(), 10000.00, 0.01);

		assertEquals(pl1.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl2.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl3.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl4.getPlayerProfile().getClass(), Aggressive.class);

		double BOFvalues[] = { 20, 16, 25, 30, 17, 25, 20 };
		int i = 0;
		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof PublicFinanceOffice) {
				assertEquals(((PublicFinanceOffice) cell).getInterestRate(), BOFvalues[i], 0.01);
				i++;
				// limite de 50%
			}
		}
		i = 0;
		double Invvalues[] = { 15, 70, 20, 17, 12, 20, 20, 20, 30, 15, 13, 30, 10.5, 13, 11, 17 };
		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof Investment) {
				assertEquals(((Investment) cell).getInterestRate(), Invvalues[i], 0.01);
				i++;
			}
		}

	}

	@Test
	public void TestCofigCapitalistSocialist() {

		ArrayList<ConfigGame> configs = new ArrayList<>();
		configs.add(ConfigGame.CAPITALIST);
		configs.add(ConfigGame.SOCIALIST);

		game = new Game(2, 2, 100, configs);
		assertEquals(game.getPlayers().size(), 4);
		Player pl1 = game.getPlayers().get(0);
		Player pl2 = game.getPlayers().get(1);
		Player pl3 = game.getPlayers().get(2);
		Player pl4 = game.getPlayers().get(3);

		assertEquals(pl1.getAmount(), 1000.00, 0.01);
		assertEquals(pl2.getAmount(), 1000.00, 0.01);
		assertEquals(pl3.getAmount(), 1000.00, 0.01);
		assertEquals(pl4.getAmount(), 1000.00, 0.01);

		assertEquals(pl1.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl2.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl3.getPlayerProfile().getClass(), Aggressive.class);
		assertEquals(pl4.getPlayerProfile().getClass(), Aggressive.class);

		double BOFvalues[] = { 20, 16, 25, 30, 17, 25, 20 };
		int i = 0;
		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof PublicFinanceOffice) {
				assertEquals(((PublicFinanceOffice) cell).getInterestRate(), BOFvalues[i], 0.01);
				i++;
				// limite de 50%
			}
		}
		i = 0;
		double Invvalues[] = { 15, 70, 20, 17, 12, 20, 20, 20, 30, 15, 13, 30, 10.5, 13, 11, 17 };
		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof Investment) {
				assertEquals(((Investment) cell).getInterestRate(), Invvalues[i], 0.01);
				i++;
			}
		}

	}

	@Test
	public void TestCofigNeoliberalProgessist() {

		ArrayList<ConfigGame> configs = new ArrayList<>();
		configs.add(ConfigGame.NEOLIBERAL);
		configs.add(ConfigGame.PROGRESSIST);

		game = new Game(2, 2, 100, configs);
		assertEquals(game.getPlayers().size(), 4);
		Player pl1 = game.getPlayers().get(0);
		Player pl2 = game.getPlayers().get(1);
		Player pl3 = game.getPlayers().get(2);
		Player pl4 = game.getPlayers().get(3);

		assertEquals(pl1.getAmount(), 100000.00, 0.01);
		assertEquals(pl2.getAmount(), 100.00, 0.01);
		assertEquals(pl3.getAmount(), 100000.00, 0.01);
		assertEquals(pl4.getAmount(), 100.00, 0.01);

		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof Subsidy) {
				assertEquals(((Subsidy) cell).getAmount(), 2000, 0.01);
			} else if (cell instanceof Investment) {
				assertEquals(((Investment) cell).getInterestRate(), 10.0, 0.01);

			}
		}

	}

	@Test
	public void TestCofigProgessistNeoliberal() {

		ArrayList<ConfigGame> configs = new ArrayList<>();
		configs.add(ConfigGame.PROGRESSIST);
		configs.add(ConfigGame.NEOLIBERAL);

		game = new Game(2, 2, 100, configs);
		assertEquals(game.getPlayers().size(), 4);
		Player pl1 = game.getPlayers().get(0);
		Player pl2 = game.getPlayers().get(1);
		Player pl3 = game.getPlayers().get(2);
		Player pl4 = game.getPlayers().get(3);

		assertEquals(pl1.getAmount(), 100000.00, 0.01);
		assertEquals(pl2.getAmount(), 100000.00, 0.01);
		assertEquals(pl3.getAmount(), 100.00, 0.01);
		assertEquals(pl4.getAmount(), 100.00, 0.01);

		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof Subsidy) {
				assertEquals(((Subsidy) cell).getAmount(), 2000, 0.01);
			} else if (cell instanceof Investment) {
				assertEquals(((Investment) cell).getInterestRate(), 10.0, 0.01);

			}
		}

	}

	@Test
	public void TestCofigSocialistProgessist() {

		ArrayList<ConfigGame> configs = new ArrayList<>();
		configs.add(ConfigGame.SOCIALIST);
		configs.add(ConfigGame.PROGRESSIST);

		game = new Game(2, 2, 100, configs);
		assertEquals(game.getPlayers().size(), 4);
		Player pl1 = game.getPlayers().get(0);
		Player pl2 = game.getPlayers().get(1);
		Player pl3 = game.getPlayers().get(2);
		Player pl4 = game.getPlayers().get(3);

		assertEquals(pl1.getAmount(), 10000.00, 0.01);
		assertEquals(pl2.getAmount(), 10000.00, 0.01);
		assertEquals(pl3.getAmount(), 10000.00, 0.01);
		assertEquals(pl4.getAmount(), 10000.00, 0.01);

		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof Subsidy) {
				assertEquals(((Subsidy) cell).getAmount(), 2000, 0.01);
			} else if (cell instanceof Investment) {
				assertEquals(((Investment) cell).getInterestRate(), 10.0, 0.01);

			}
		}

	}

	@Test
	public void TestCofigProgessistSocialist() {

		ArrayList<ConfigGame> configs = new ArrayList<>();
		configs.add(ConfigGame.PROGRESSIST);
		configs.add(ConfigGame.SOCIALIST);

		game = new Game(2, 2, 100, configs);
		assertEquals(game.getPlayers().size(), 4);
		Player pl1 = game.getPlayers().get(0);
		Player pl2 = game.getPlayers().get(1);
		Player pl3 = game.getPlayers().get(2);
		Player pl4 = game.getPlayers().get(3);

		assertEquals(pl1.getAmount(), 1000.00, 0.01);
		assertEquals(pl2.getAmount(), 1000.00, 0.01);
		assertEquals(pl3.getAmount(), 1000.00, 0.01);
		assertEquals(pl4.getAmount(), 1000.00, 0.01);

		for (Cell cell : game.getBoard().getCells()) {
			if (cell instanceof Subsidy) {
				assertEquals(((Subsidy) cell).getAmount(), 2000, 0.01);
			} else if (cell instanceof Investment) {
				assertEquals(((Investment) cell).getInterestRate(), 10.0, 0.01);

			}
		}

	}
}
