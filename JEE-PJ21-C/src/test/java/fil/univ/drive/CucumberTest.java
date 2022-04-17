package fil.univ.drive;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/cucumber",
		plugin = {"pretty", "html:target/cucumber/stocks"},
		extraGlue = "fil.univ.drive")
public class CucumberTest {
}
