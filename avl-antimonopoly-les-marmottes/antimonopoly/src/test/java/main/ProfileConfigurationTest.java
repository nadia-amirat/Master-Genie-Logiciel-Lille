package main;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import config.ConfigGame;
import config.Configuration;
import exceptions.BadRequestException;
import game.Game;
import java.io.*;
import java.util.*;

public class ProfileConfigurationTest extends Configuration {

	Game game;
	ArrayList<ConfigGame> alreadyChosenConfigs;
	ArrayList<ConfigGame> possibleConfig;
	ArrayList<ConfigGame> playerConfigChoices;

	@Before
	public void setUp() {
		alreadyChosenConfigs = new ArrayList<>();
		playerConfigChoices = new ArrayList<>();
	}

	@Test
	public void checkGetPossibleConfigChoicesReturnsNullWhenArgumentSizeGreaterThan1() {
		alreadyChosenConfigs.add(ConfigGame.CAPITALIST);
		alreadyChosenConfigs.add(ConfigGame.NEOLIBERAL);

		possibleConfig = Configuration.getPossibleConfigChoices(alreadyChosenConfigs);

		assertTrue(alreadyChosenConfigs.size() == 2);
		assertNull(possibleConfig);

		alreadyChosenConfigs.add(ConfigGame.NEOLIBERAL);
		assertTrue(alreadyChosenConfigs.size() > 2);
		assertNull(possibleConfig);

	}

	@Test
	public void checkGetPossibleConfigChoicesReturnsAllExistingConfigWhenArgumentListIsEmpty() {

		assertTrue(alreadyChosenConfigs.size() == 0);

		possibleConfig = Configuration.getPossibleConfigChoices(alreadyChosenConfigs);

		assertTrue(possibleConfig.size() == 4);
		assertTrue(possibleConfig.contains(ConfigGame.CAPITALIST));
		assertTrue(possibleConfig.contains(ConfigGame.SOCIALIST));
		assertTrue(possibleConfig.contains(ConfigGame.NEOLIBERAL));
		assertTrue(possibleConfig.contains(ConfigGame.PROGRESSIST));

	}

	@Test
	public void checkGetPossibleConfigChoicesReturnsCapitalistProgressistWhenArgumentContainsNeoliberal() {

		alreadyChosenConfigs.add(ConfigGame.NEOLIBERAL);

		possibleConfig = Configuration.getPossibleConfigChoices(alreadyChosenConfigs);
		assertTrue(possibleConfig.size() == 2);
		assertTrue(possibleConfig.contains(ConfigGame.CAPITALIST));
		assertTrue(possibleConfig.contains(ConfigGame.PROGRESSIST));

	}

	@Test
	public void checkGetPossibleConfigChoicesReturnsCapitalistProgressistWhenArgumentContainsSocialist() {

		alreadyChosenConfigs.add(ConfigGame.SOCIALIST);

		possibleConfig = Configuration.getPossibleConfigChoices(alreadyChosenConfigs);
		assertTrue(possibleConfig.size() == 2);
		assertTrue(possibleConfig.contains(ConfigGame.CAPITALIST));
		assertTrue(possibleConfig.contains(ConfigGame.PROGRESSIST));

	}

	@Test
	public void checkGetPossibleConfigChoicesReturnsNeoliberalSocialistWhenArgumentContainsCapitalist() {

		alreadyChosenConfigs.add(ConfigGame.CAPITALIST);

		possibleConfig = Configuration.getPossibleConfigChoices(alreadyChosenConfigs);
		assertTrue(possibleConfig.size() == 2);
		assertTrue(possibleConfig.contains(ConfigGame.NEOLIBERAL));
		assertTrue(possibleConfig.contains(ConfigGame.SOCIALIST));

	}

	@Test
	public void checkGetPossibleConfigChoicesReturnsNeoliberalSocialistWhenArgumentContainsProgressist() {

		alreadyChosenConfigs.add(ConfigGame.PROGRESSIST);

		possibleConfig = Configuration.getPossibleConfigChoices(alreadyChosenConfigs);
		assertTrue(possibleConfig.size() == 2);
		assertTrue(possibleConfig.contains(ConfigGame.NEOLIBERAL));
		assertTrue(possibleConfig.contains(ConfigGame.SOCIALIST));
	}

	@Test
	public void checkGetPlayerConfigWhenPlayerChoosesNeoliberalandCapitalist()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;

		System.setIn(new ByteArrayInputStream("1\n1\n".getBytes()));

		playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);

		System.setIn(is);

		assertTrue(playerConfigChoices.contains(ConfigGame.NEOLIBERAL));
		assertTrue(playerConfigChoices.contains(ConfigGame.CAPITALIST));

	}

	@Test
	public void checkGetPlayerConfigWhenPlayerChoosesNeoliberalandProgressist()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;

		System.setIn(new ByteArrayInputStream("1\n2\n".getBytes()));

		playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);

		System.setIn(is);

		assertTrue(playerConfigChoices.contains(ConfigGame.NEOLIBERAL));
		assertTrue(playerConfigChoices.contains(ConfigGame.PROGRESSIST));

	}

	@Test
	public void checkGetPlayerConfigWhenPlayerChoosesSocialistandCapitalist()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;

		System.setIn(new ByteArrayInputStream("2\n1\n".getBytes()));

		playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);

		System.setIn(is);

		assertTrue(playerConfigChoices.contains(ConfigGame.SOCIALIST));
		assertTrue(playerConfigChoices.contains(ConfigGame.CAPITALIST));

	}

	@Test
	public void checkGetPlayerConfigWhenPlayerChoosesSocialistandProgressist()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;

		System.setIn(new ByteArrayInputStream("2\n2\n".getBytes()));

		playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);

		System.setIn(is);

		assertTrue(playerConfigChoices.contains(ConfigGame.SOCIALIST));
		assertTrue(playerConfigChoices.contains(ConfigGame.PROGRESSIST));

	}

	/////////////////////////
	@Test
	public void checkGetPlayerConfigWhenPlayerChoosesCapitalistandNeoliberal()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;

		System.setIn(new ByteArrayInputStream("3\n1\n".getBytes()));

		playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);

		System.setIn(is);

		assertTrue(playerConfigChoices.contains(ConfigGame.NEOLIBERAL));
		assertTrue(playerConfigChoices.contains(ConfigGame.CAPITALIST));

	}

	@Test
	public void checkGetPlayerConfigWhenPlayerChoosesProgressistAndNeoliberal()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;

		System.setIn(new ByteArrayInputStream("4\n1\n".getBytes()));

		playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);

		System.setIn(is);

		assertTrue(playerConfigChoices.contains(ConfigGame.NEOLIBERAL));
		assertTrue(playerConfigChoices.contains(ConfigGame.PROGRESSIST));

	}

	@Test
	public void checkGetPlayerConfigWhenPlayerChoosesCapitalistAndSocialist()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;

		System.setIn(new ByteArrayInputStream("3\n2\n".getBytes()));

		playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);

		System.setIn(is);

		assertTrue(playerConfigChoices.contains(ConfigGame.SOCIALIST));
		assertTrue(playerConfigChoices.contains(ConfigGame.CAPITALIST));

	}

	@Test
	public void checkGetPlayerConfigWhenPlayerChoosesProgressistAndSocialist()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;

		System.setIn(new ByteArrayInputStream("4\n2\n".getBytes()));

		playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);

		System.setIn(is);

		assertTrue(playerConfigChoices.contains(ConfigGame.SOCIALIST));
		assertTrue(playerConfigChoices.contains(ConfigGame.PROGRESSIST));

	}

	@Test(expected = BadRequestException.class)
	public void checkGetPlayerConfigThrowsExceptionWhenFirstPlayerInputOutOfRange()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;
		System.setIn(new ByteArrayInputStream("5\n1\n".getBytes()));
		try {
			playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);
		} catch (BadRequestException ex) {
			System.setIn(is);
			throw ex;
		}
	}

	@Test(expected = BadRequestException.class)
	public void checkGetPlayerConfigThrowsExceptionWhenSecondPlayerInputOutOfRange()
			throws UnsupportedEncodingException, BadRequestException {
		InputStream is = System.in;
		System.setIn(new ByteArrayInputStream("1\n3\n".getBytes()));
		try {
			playerConfigChoices = Configuration.getPlayerConfig(playerConfigChoices);
		} catch (BadRequestException ex) {
			System.setIn(is);
			throw ex;
		}
	}
}
