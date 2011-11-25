import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * HighScores represents the top players and their scores.
 * 
 */
public final class HighScores
{
    public final static String kScoresFile = "HighScores.txt";
    private final String gamePrefix;
    
    // Use a factory to ensure we only ever have one instance of the class.
    private static HighScores instance = null;
    private HighScores(String gamePrefix)
    {
        this.gamePrefix = gamePrefix;
    }
    
    /** Initialize and return the one allowed instance of this class.
     * @param gamePrefix a string representing a folder name containing 
     * all the game configuration files and image folders.
     * @return an instance of this class. 
     */
    public static HighScores createInstance(String gamePrefix)
    {
        if (HighScores.instance == null)
        {
            HighScores.instance = new HighScores(gamePrefix);
        }

        return HighScores.instance;
    }

    /** Read the HighScores.txt file, sort it, and return the first 10 items. 
     * @param ascending true if the items should be sorted in ascending order, 
     *                  false if descending order is desired.  For example, 
     *                  times are usually sorted with fastest first which would be
     *                  ascending order, where as numeric scores are usually highest 
     *                  first (descending).
     *
     * @return A string representing the top players (up to ten), e.g., 
<pre>
299  Bill
152  James
121  Sally
</pre>
Scores are sorted lexicographically (like Strings), not numerically.
     */
    public String getHighScores(boolean ascending) throws IOException
    {
        List<String> highScores = new ArrayList<String>();
        
        // Ugh.  Java I/O is so *verbose*.
        BufferedReader in;
        try
        {
            in = new BufferedReader(new FileReader(this.gamePrefix + this.kScoresFile));
        }
        catch (java.io.FileNotFoundException e)
        {
            // If the file hasn't been created, we have no scores.
            return "";
        }

        while (in.ready())
        {
            highScores.add(in.readLine());
        }
        // Be a lazy programmer and sort the whole thing, even if we only need
        // the top (or bottom) 10.
        Collections.sort(highScores);
        
        if (!ascending)
        {
            Collections.reverse(highScores);
        }
        
        // Strings are immutable in Java, so appending a bunch of them is
        // generally a Bad Thing.  With only 10 items, it's probably not a big
        // deal, but still.
        // Returning a blob of text is generally also a Bad Thing, but the
        // spec's the spec and I'm not allowed to make this prettier.
        StringBuilder blob = new StringBuilder();
        for (int i = 0; i < 10 && i < highScores.size(); i++)
        {
            blob.append(highScores.get(i)).append("\n");
        }
        return blob.toString();
    }
    
    public void saveScore(String valueToSave, String playerName) throws IOException
    {
        PrintWriter out = new PrintWriter(new FileWriter(this.gamePrefix + this.kScoresFile, true));
        out.println(valueToSave + "  " + playerName);
        out.close();
    }
}

