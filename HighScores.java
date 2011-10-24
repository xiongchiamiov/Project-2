/**
 * HighScores represents the top players and their scores.
 * 
 */
public final class HighScores
{
    public final static String kScoresFile = "HighScores.txt";
    
     /** Constructor is private to defeat instantiation */
    private HighScores(String gamePrefix)
    
    /** Initialize and return the one allowed instance of this class.
     * @param gamePrefix a string representing a folder name containing 
     * all the game configuration files and image folders.
     * @return an instance of this class. 
     */
    public static HighScores createInstance(String gamePrefix)

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

}

