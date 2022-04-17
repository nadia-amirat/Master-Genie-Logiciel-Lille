package avl.actors;

import java.util.ArrayList;
import org.junit.Before;

import avl.boardgame.Investment;

/**
 * State class test
 * 
 * @author Liticia MEDJOUDJ
 *
 */
public class StateTest extends EntityTest {

	@Before
	public void setUp() {
		this.entity = new State(100);
		this.investment = new Investment(default_investment_value, default_investment_interest_rate);
		this.investments = new ArrayList<Investment>();
		for (int i = 0; i < default_nb_investments; i++) {
			this.investments.add(new Investment(default_investment_value, default_investment_interest_rate));
		}
	}

}
