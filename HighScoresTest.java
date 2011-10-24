import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * The test class WordSearchSolverTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class HighScoresTest
{
    /**
     * Default constructor for test class WordSearchSolverTest
     */
    public HighScoresTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

	@Test
    public void testOneHorizontal() throws Exception
    {
        HighScores highScores = HighScores.createInstance("./Mines");
        String scoresBlob = highScores.getHighScores(true);
        System.out.println(scoresBlob);
        highScores.saveScore("42", "Arthur Dent");
        //assertEquals("wrong number of results", 2, results.size());
    }
}


