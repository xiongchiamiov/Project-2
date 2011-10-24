
/**
 * GridImages represents the images to be used for pieces and backgrounds in
 * a grid game.  GridImages acts like a map between piece names and their
 * corresponding images.  The key values do NOT contain actual filenames.
 * The intent is that the key is a Piece enum name. The underlying implementation
 * provides the appropriate file extensions to read the image file from the filesystem.
 * 
 */
public final class GridImages
{
    /** The name of the directory containing the images */
    public final static String kImageDir = "PieceImages";
    
    /** Initialize and return the one allowed instance of this class.
     * The instance contains images loaded from <code>kImageDir</code>, one for 
     * each Piece enum value and one named "bkgd".
     *
     * @param gamePrefix an empty string. (reserved for future use.)
     * @return an instance of this class. 
     */
    public static GridImages createInstance(String gamePrefix)
    
    /** Retrieve an image for a piece.
     * @param name the name of the desired image (usually the <code>name()</code> of a piece enum)
     * @return ImageIcon the image associated with the specified name
     */
    public ImageIcon getPiece(String name)

    /** Retrieve a background image.
     * @param name the name of the desired image (usually corresponds to a background)
     * @return ImageIcon the image associated with the background
     */
    public ImageIcon getBkgd(String name)

    /** Accessor to the piece height. 
     * @return the piece height in pixels.
     */
    public int getPieceHeight() 

    /** Accessor to the piece width.
     * @return the piece width in pixels.
     */
    public int getPieceWidth() 

    /** Constructor is private to defeat instantiation */
    private GridImages(String gamePrefix)
   
}
