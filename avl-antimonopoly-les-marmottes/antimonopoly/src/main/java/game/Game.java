package game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import avl.actors.Player;
import avl.actors.State;
import avl.boardgame.AntiTrustLaw;
import avl.boardgame.Board;
import avl.boardgame.Cell;
import avl.boardgame.Investment;
import avl.boardgame.PublicFinanceOffice;
import avl.boardgame.Subsidy;
import avl.profiles.Aggressive;
import avl.profiles.Cautious;
import config.ConfigGame;

public class Game {

	public static int nbPlayer = 0;
	public static int nbInvestesments = 0;
	private Board board;
	private ArrayList<Player> players;
	private static State state;
	private ArrayList<ConfigGame> configs;
	private Player currentPlayer;
	// sa position dans la liste des joueurs
	private int currentPLayerIndex;

	public Game(int nbPlayerAgr, int nbPlayerCautious, int capitalState, ArrayList<ConfigGame> configs) {
		Game.nbPlayer = nbPlayerAgr + nbPlayerCautious;
		this.players = new ArrayList<>();
		this.configs = configs;
		this.board = new Board();
		Game.state = new State(capitalState);
		this.initGame(nbPlayerAgr, nbPlayerCautious);
		this.currentPLayerIndex = 0;
		currentPlayer = players.get(this.currentPLayerIndex);
	}

	private void initGame(int nbPlayerAgr, int nbPlayerCautious) {
		for (ConfigGame cg : this.configs) {
			switch (cg) {
			case NEOLIBERAL:
				configNeoLiberal(nbPlayerAgr, nbPlayerCautious);
				break;
			case SOCIALIST:
				configSocialist(nbPlayerAgr, nbPlayerCautious);
				break;
			case CAPITALIST:
				configCapitalist(nbPlayerAgr, nbPlayerCautious);
				break;
			case PROGRESSIST:
				configProgressist(nbPlayerAgr, nbPlayerCautious);
				break;
			default:
				break;
			}
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setName("player" + i);
		}
	}

	/*
	 *
	 * 1 - [ NEOLIBERAL, CAPITALIST ] 2 - [ NEOLIBERAL, PROGRESSIST ] 3 - [
	 * SOCIALIST , CAPITALIST ] 4 - [ SOCIALIST , PROGRESSIST ]
	 */
	public void configNeoLiberal(int nbPlayerAgr, int nbPlayerCautious) {

		// give a half a lot of money
		if (this.players.isEmpty()) {
			int half = nbPlayerAgr / 2;
			for (int i = 0; i < half; i++) {
				this.players.add(new Player(new Aggressive(), 100000));
			}
			for (int i = half; i < nbPlayerAgr; i++) {
				this.players.add(new Player(new Aggressive(), 100));
			}
			half = nbPlayerCautious / 2;
			for (int i = 0; i < half; i++) {
				this.players.add(new Player(new Cautious(), 100000));
			}
			for (int i = half; i < nbPlayerAgr; i++) {
				this.players.add(new Player(new Cautious(), 100));
			}
		} else {
			int half = this.players.size() / 2;
			for (int i = 0; i < half; i++) {
				this.players.get(i).setAmount(100000);
			}
			for (int i = half; i < this.players.size(); i++) {
				this.players.get(i).setAmount(100);
			}
		}

		// L’´etat d´efini des mesures antitrust légères
		Game.state.setMaxInvestments(4000000);
	}

	public void configSocialist(int nbPlayerAgr, int nbPlayerCautious) {
		if (this.players.isEmpty()) {
			for (int i = 0; i < nbPlayerAgr; i++) {
				this.players.add(new Player(new Aggressive(), 10000));
			}
			for (int i = 0; i < nbPlayerCautious; i++) {
				this.players.add(new Player(new Cautious(), 10000));
			}
		} else {
			for (Player pl : this.players)
				pl.setAmount(1000);
		}
		// l"etat fixe beaucoup de taxe: add +15% de taxe sur tous les taxes BFP
		for (Cell cell : this.board.getCells()) {
			if (cell instanceof PublicFinanceOffice) {
				double newTax = ((PublicFinanceOffice) cell).getInterestRate() + 15.0;
				// limite de 50%
				newTax = newTax > 50.0 ? ((PublicFinanceOffice) cell).getInterestRate() : newTax;
				((PublicFinanceOffice) cell).setInterestRate(newTax);
			}
		}

	}

	public void configCapitalist(int nbPlayerAgr, int nbPlayerCautious) {
		if (this.players.isEmpty()) {
			useCapitalistPlayers(nbPlayerAgr, nbPlayerCautious);
		} else {
			for (Player pl : this.players)
				pl.setPlayerProfile(new Aggressive());
		}

		// augment le taux de profits dans les investisments à 10%
		for (Cell cell : this.board.getCells()) {
			if (cell instanceof Investment) {
				float newRate = ((Investment) cell).getInterestRate() + 10;
				// limite de profit à 35%
				newRate = newRate > 35 ? ((Investment) cell).getInterestRate() : newRate;
				((Investment) cell).setInterestRate(newRate);
			}
		}
	}

	public void useCapitalistPlayers(int nbPlayerAgr, int nbPlayerCautious) {
		this.players.clear();
		for (int i = 0; i < nbPlayerAgr + nbPlayerCautious; i++) {
			this.players.add(new Player(new Aggressive(), 100000));
		}
	}

	public void configProgressist(int nbPlayerAgr, int nbPlayerCautious) {
		if (this.players.isEmpty()) {
			useCapitalistPlayers(nbPlayerAgr, nbPlayerCautious);
		}
		for (Cell cell : this.board.getCells()) {
			if (cell instanceof Subsidy) {
				// augmenter les subventions: x->x*2
				double newAmount = ((Subsidy) cell).getAmount() * 2;
				((Subsidy) cell).setAmount(newAmount);
			} else if (cell instanceof AntiTrustLaw) {
				// decremente anti trust: x->x/2;
				double NewMaxInvest = Game.getState().getMaxInvestments() / 2;
				Game.getState().setMaxInvestments(NewMaxInvest);
			} else if (cell instanceof Investment) {
				// set 10% for all investisments
				((Investment) cell).setInterestRate(10);
			}
		}

	}

	public boolean canPlay() {
		int res = 0;
		for (Player p : players) {
			if (!p.getLosed()) {
				res++;
				if (res >= 2)
					return true;
			}
		}
		return false;
	}

	public boolean isEnd(Scanner sc) {
		if (!canPlay() || Game.getState().getAmount() <= 0)
			return true;

		System.out.println("Do you want to stop the game?:");
		System.out.println("	1: Yes");
		System.out.println("	2: No");

		String tmp = sc.next();
		while (!tmp.equals("1") && !tmp.equals("2")) {
			System.out.println("Error: Please choose between 1 or 2");
			System.out.println("Do you want to stop the game?:");
			System.out.println("	1: Yes");
			System.out.println("	2: No");
			tmp = sc.next();
		}
		if (tmp.equals("1"))
			return true;
		return false;
	}

	public void play(Scanner sc) {

		while (!isEnd(sc)) {
			nextRound();
		}
		displayResults();

	}

	public Player getWinner() {
		double amountMax = 0;
		Player winner = null;
		for (Player pl : this.players) {
			if (pl.computeCapital() >= amountMax) {
				amountMax = pl.computeCapital();
				winner = pl;
			}
		}
		return winner;
	}

	private void displayResults() {
		if (Game.getState().getLosed())
			System.out.println("L’Etat à échoue!");
		Player winner = getWinner();
		System.out.println("Gagnant: " + winner.getName());
		System.out.println("================");
		System.out.println("Nom             Investissements    Liquide    Patrimoine");
		for (Player pl : this.getPlayersOrder()) {
			System.out.println(pl.getName() + "           " + pl.getInvestismentsTotalValue() + "         "
					+ pl.getAmount() + "        " + (pl.getInvestismentsTotalValue() + pl.getAmount()));
		}

		System.out.println("Etat -");
		System.out.println("\tInvestisments:" + Game.getState().getInvestismentsTotalValue());
		System.out.println("\tLiquide:" + Game.getState().getAmount());

	}

	public ArrayList<Player> getPlayersOrder() {
		ArrayList<Player> playersList = new ArrayList<>(this.players);
		ArrayList<Player> orderList = new ArrayList<>();

		while (!playersList.isEmpty()) {
			double max = playersList.get(0).computeCapital();
			Player plmax = playersList.get(0);
			for (Player player : playersList) {
				if (player.computeCapital() > max) {
					max = player.computeCapital();
					plmax = player;
				}

			}
			orderList.add(plmax);
			playersList.remove(plmax);
		}
		return orderList;

	}

	public void nextPlayerPlay(Player pl, int position) { // fait jouer le joueur suivant
		if (pl.getLosed())
			return;
		this.currentPlayer = pl;
		this.currentPlayer.setPosition(position);
		this.board.getCell(position).action(currentPlayer);
		pl.checkLoose();
	}

	public void nextRound() { // fait jouer tous les joueurs
		for (Player pl : this.players) {
			// int de = ThreadLocalRandom.current().nextInt(1, 7);
			Random rn = new Random();
			int range = 6 - 1 + 1;
			int randomNum = rn.nextInt(range) + 1;
			int position = (pl.getPosition() + randomNum) % this.board.getCells().size();
			nextPlayerPlay(pl, position);
		}

	}

	public static State getState() {
		return Game.state;
	}

	public static void setState(State state) {
		Game.state = state;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public Board getBoard() {
		return board;
	}

	public void setCurrentPlayer(Player mockPlayer) {
	}

	public static int getNbPlayer() {
		return nbPlayer;
	}

	public static void setNbPlayer(int nbPlayer) {
		Game.nbPlayer = nbPlayer;
	}

	public static int getNbInvestesments() {
		return nbInvestesments;
	}

	public static void setNbInvestesments(int nbInvestesments) {
		Game.nbInvestesments = nbInvestesments;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public ArrayList<ConfigGame> getConfigs() {
		return configs;
	}

	public void setConfigs(ArrayList<ConfigGame> configs) {
		this.configs = configs;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getCurrentPLayerIndex() {
		return currentPLayerIndex;
	}

	public void setCurrentPLayerIndex(int currentPLayerIndex) {
		this.currentPLayerIndex = currentPLayerIndex;
	}

}
