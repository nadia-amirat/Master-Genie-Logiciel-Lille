package avl.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import config.ConfigGame;
import config.Configuration;
import exceptions.BadRequestException;
import game.Game;

public class Main {

	public static void main(String[] args) {

		HashMap<String, Integer> numericalConfigs = new HashMap<>();
		ArrayList<ConfigGame> profilConfig = new ArrayList<>();
		boolean areProfilesConfigured = false;
		Game game;

		while (numericalConfigs.size() != 3) {
			try {
				numericalConfigs = Configuration.setNumberOfPlayers();
			} catch (Exception e) {
				System.out.println("Please enter a valid number");
			}
		}
		while (!areProfilesConfigured) {
			try {
				profilConfig = Configuration.getPlayerConfig(profilConfig);
				areProfilesConfigured = true;
			} catch (BadRequestException e) {
				System.out.println("Please enter a valid number");
			}
		}

		game = new Game(numericalConfigs.get("aggressive"), numericalConfigs.get("cautious"),
				numericalConfigs.get("stateCapital"), profilConfig);

		Scanner sc = new Scanner(System.in);
		game.play(sc);
		sc.close();
	}

}
