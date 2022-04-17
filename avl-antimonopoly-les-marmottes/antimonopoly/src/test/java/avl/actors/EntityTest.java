package avl.actors;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import avl.boardgame.Investment;

/**
 * Abstract class of test for Entity
 * 
 * @author Liticia MEDJOUDJ
 */
public abstract class EntityTest {

	protected int default_nb_investments = 101;
	protected double default_investment_value = 9743;
	protected float default_investment_interest_rate = 0;

	protected Entity entity;
	protected Investment investment;
	protected ArrayList<Investment> investments;

	@Test
	public void checkInvestmentsIsEmptyWhenEntityIsInitilized() {
		assertTrue(entity.getNumberOfInvestments() == 0);
	}

	@Test
	public void checkAddInvestmentIsOK() {
		assertTrue(entity.getInvestments().size() == 0);
		entity.addInvestment(investment);
		assertTrue(entity.getInvestments().size() == 1);
		assertTrue(entity.getInvestments().containsValue(investment));

	}

	@Test
	public void checkRemoveInvestmentIsOK() {
		assertTrue(entity.getInvestments().size() == 0);

		entity.addInvestment(investment);
		assertTrue(entity.getInvestments().size() == 1);
		assertTrue(entity.getInvestments().containsValue(investment));

		entity.removeInvestment(investment.getId());
		assertTrue(entity.getInvestments().size() == 0);
		assertFalse(entity.getInvestments().containsValue(investment));

	}

	@Test
	public void checkGetNumberOfInvestmentsIsOK() {
		assertTrue(entity.getNumberOfInvestments() == 0);
		for (int i = 0; i < investments.size(); i++) {
			entity.addInvestment(investments.get(i));
			assertTrue(entity.getNumberOfInvestments() == i + 1);
		}
	}

	@Test
	public void checkGetInsvetmentByIdIsOK() {
		int id = investment.getId();
		entity.addInvestment(investment);
		assertSame(investment, entity.getInvestment(id));
	}

}
