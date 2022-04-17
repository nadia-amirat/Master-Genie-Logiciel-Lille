package config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import exceptions.BadRequestException;

public class Configuration {

	public static HashMap<String, Integer> setNumberOfPlayers() throws BadRequestException {
		Scanner scanner = new Scanner(System.in);
		return setNumberOfPlayers(scanner);
	}

	protected static HashMap<String, Integer> setNumberOfPlayers(Scanner sc) throws BadRequestException {
		HashMap<String, Integer> config = new HashMap<>();
		String tmp;
		int cautious, aggressive, capital;
		try {
			// System.out.println("Max number of players: 10");
			System.out.println("How much Cautious players do you want ?");
			tmp = sc.nextLine();
			cautious = Integer.parseInt(tmp);
			/*
			 * if(cautious>10) { System.out.println("Too much players"); throw new
			 * BadRequestException(); }
			 */
			config.put("cautious", cautious);
			System.out.println("How much Aggressive players do you want ?");
			tmp = sc.nextLine();
			aggressive = Integer.parseInt(tmp);
			/*
			 * if(aggressive+cautious>10) { System.out.println("Too much players"); throw
			 * new BadRequestException(); }
			 */
			config.put("aggressive", aggressive);
			/*
			 * System.out.println("How much capital do you want for players ?"); tmp =
			 * sc.nextLine(); capital = Integer.parseInt(tmp); config.put("playerCapital",
			 * capital);
			 */
			System.out.println("How much capital do you want for state ?");
			tmp = sc.nextLine();
			capital = Integer.parseInt(tmp);
			config.put("stateCapital", capital);
		} catch (Exception e) {
			throw new BadRequestException();
		}
		return config;
	}

	public static ArrayList<ConfigGame> getPlayerConfig(ArrayList<ConfigGame> playerConfigChoices)
			throws BadRequestException {
		Scanner scanner = new Scanner(System.in);
		return getPlayerConfig(scanner, playerConfigChoices);
	}

	protected static ArrayList<ConfigGame> getPlayerConfig(Scanner sc, ArrayList<ConfigGame> playerConfigChoices)
			throws BadRequestException {
		ArrayList<ConfigGame> possibleChoices;
		possibleChoices = getPossibleConfigChoices(playerConfigChoices);
		while (possibleChoices != null) {
			System.out.println("What configuration do you want for your game ?");
			for (int i = 0; i < possibleChoices.size(); i++) {
				System.out.println("	" + (i + 1) + ": " + possibleChoices.get(i));
			}
			try {
				String tmp = sc.nextLine();
				int num = Integer.parseInt(tmp);
				if (num == 0 || num > possibleChoices.size()) {
					throw new RuntimeException("");
				}
				playerConfigChoices.add(possibleChoices.get(num - 1));
				possibleChoices = getPossibleConfigChoices(playerConfigChoices);
			} catch (Exception e) {
				System.out.println("Please enter the corresponding integer to your choice\n");
				throw new BadRequestException();
			}
		}
		System.out.println("Your configuration is saved\n");
		return playerConfigChoices;
	}

	/*
	 * POSSIBLE CONFIGURATIONS NEOLIBERAL, SOCIALIST, CAPITALIST,PROGRESSIST,
	 *
	 * 1 - [ NEOLIBERAL, CAPITALIST ] 2 - [ NEOLIBERAL, PROGRESSIST ] 3 - [
	 * SOCIALIST , CAPITALIST ] 4 - [ SOCIALIST , PROGRESSIST ]
	 * 
	 * getPossibleConfigChoices return what are the left possibilities according to
	 * what is already chosen
	 */
	public static ArrayList<ConfigGame> getPossibleConfigChoices(ArrayList<ConfigGame> config) {
		if (config.size() >= 2)
			return null;
		ArrayList<ConfigGame> res = new ArrayList<>();
		if (config.size() == 0) {
			res.add(ConfigGame.NEOLIBERAL);
			res.add(ConfigGame.SOCIALIST);
			res.add(ConfigGame.CAPITALIST);
			res.add(ConfigGame.PROGRESSIST);
			return res;
		}
		switch (config.get(0)) {
		case NEOLIBERAL:
			res.add(ConfigGame.CAPITALIST);
			res.add(ConfigGame.PROGRESSIST);
			return res;
		case SOCIALIST:
			res.add(ConfigGame.CAPITALIST);
			res.add(ConfigGame.PROGRESSIST);
			return res;
		case CAPITALIST:
			res.add(ConfigGame.NEOLIBERAL);
			res.add(ConfigGame.SOCIALIST);
			return res;
		default:
			res.add(ConfigGame.NEOLIBERAL);
			res.add(ConfigGame.SOCIALIST);
			return res;
		}

	}
}
