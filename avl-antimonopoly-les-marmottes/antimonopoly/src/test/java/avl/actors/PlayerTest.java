package avl.actors;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import avl.boardgame.Investment;
import avl.utils.Computations;

/**
 * Player test class
 * 
 * @author Liticia MEDJOUDJ
 *
 */
public class PlayerTest extends EntityTest {

	@Before
	public void setUp() {
		this.entity = new Player(null, 9719);
		this.investment = new Investment(0, 0);
		this.investments = new ArrayList<Investment>();
		for (int i = 0; i < default_nb_investments; i++) {
			this.investments.add(new Investment(default_investment_value, default_investment_interest_rate));
		}
	}

	@Test
	public void checkComputeCapitalIsOK() {
		for (int i = 0; i < default_nb_investments; i++) {
			this.entity.addInvestment(investments.get(i));
		}
		assertTrue(((Player) this.entity).computeCapital() == this.entity.getAmount()
				+ Computations.totalValueOfInvestments(this.investments));
	}

}
