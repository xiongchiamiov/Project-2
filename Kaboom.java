import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.text.*;

/**  Skeleton for Kaboom. 
 *   GUI has a menu bar, a status area, and a 2d playing area.
 *   The GUI will display the game and handle user interaction. 
 * @author J. Dalbey
 * @version 9/24/2011
*/
public class Kaboom extends JFrame implements ActionListener
{
    /* Main components of the GUI */
    // DO NOT CHANGE ANY OF THE GUI COMPONENT DECLARATIONS IN THIS SECTION
    private String[] columns = {"", "", "", "", "", "", "", "", "", "", "", "", };
    private JTable table;
    private JMenuBar menuBar;
    private JMenu mnuGame;
    private JMenuItem[] mnuItems;
    private JLabel lblStatus = new JLabel();
    private ImageIcon background;
    
    /* The game board */
    private Renderable[][] myBoard; 
    private static final int kBoardWidth = 10;
    private static final int kBoardHeight = 10;
    private static final int kMaxBombs = 9;
    private static int gameNumber = 0;
    private int moves = 0;
    private int flagsPlaced = 0;
    private int numBombs = 0;
    private int secondsElapsed = 0;
    
    /* Square dimensions in pixels */
    private static final int kTileWidth = 58;
    private static final int kTileHeight = 78;
    
    
    /** Create a GUI.
     * Will use the System Look and Feel when possible.
     */
    public Kaboom()
    {
        super();
        try
        {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
    }
    
    /** Place all the Swing widgets in the frame of this GUI.
     * @post the GUI is visible.  
     */
    public void layoutGUI()
    {
        loadImages();
        newGame();
        startTimer();
        table = new JTable(this.myBoard, this.columns)
        {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer( renderer, row, column);
                // We want renderer component to be
                // transparent so background image is visible
                if ( c instanceof JComponent)
                    ((JComponent)c).setOpaque(false);
                return c;
            }
            
            // Override paint so as to show the table background
            public void paint( Graphics g )
            {
                // paint an image in the table background
                if (background != null)
                {
                    g.drawImage( background.getImage(), 0, 0, null, null );
                }
                // Now let the paint do its usual work
                super.paint(g);
            }
            
            // Make the table cells not editable
            public boolean isCellEditable(int row,int column)
            {  
                return false;  
            }              
            
            public Class getColumnClass(int c)
            {
                return Tile.class;
            }
        }
        ; // end table def
        
        TableColumn column = null;
        // Does the board exist?
        if (this.myBoard != null)
        {
            // Set the dimensions for each column in the board to match the image */
            for (int index = 0; index < kBoardWidth; index++)
            {
                column = table.getColumnModel().getColumn(index);
                column.setMaxWidth(kTileWidth);
                column.setMinWidth(kTileWidth);
            }
        }
        
        // Define the layout manager that will control order of components
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        // Create the menu options
        layoutMenus();
        
        // Create a panel for the status information
        JPanel statusPane = new JPanel();
        statusPane.add(this.lblStatus);
        this.lblStatus.setName("Status");
        statusPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(statusPane);
        
        // Define the characteristics of the table that shows the game board        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setRowHeight(kTileHeight);
        table.setOpaque(false);
        table.setShowGrid(false);
        table.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(table);
        
        // Define the mouse listener that will handle player's clicks.
        table.addMouseListener(myMouseListener);
        
        // And handle window closing events
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        }
        );
    } // end layout
    
    private void layoutMenus()
    {
        // Add a menubar
        menuBar = new javax.swing.JMenuBar();
        mnuGame = new JMenu("Game");
        menuBar.add(mnuGame);
        mnuItems = new JMenuItem[9];  // allocate space for 9 menu items
        
        // Create the Restart menu item
        mnuItems[0] = new JMenuItem("Restart");
        mnuItems[0].setAccelerator(KeyStroke.getKeyStroke('R', ActionEvent.ALT_MASK));
        mnuItems[0].addActionListener(this);
        mnuGame.add(mnuItems[0]);

        mnuItems[1] = new JMenuItem("New Game");
        mnuItems[1].setAccelerator(KeyStroke.getKeyStroke('N', ActionEvent.ALT_MASK));
        mnuItems[1].addActionListener(this);
        mnuGame.add(mnuItems[1]);

        mnuItems[2] = new JMenuItem("Select Game");
        mnuItems[2].setAccelerator(KeyStroke.getKeyStroke('G', ActionEvent.ALT_MASK));
        mnuItems[2].addActionListener(this);
        mnuGame.add(mnuItems[2]);

        mnuItems[3] = new JMenuItem("Scores");
        mnuItems[3].setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.ALT_MASK));
        mnuItems[3].addActionListener(this);
        mnuGame.add(mnuItems[3]);

        mnuItems[4] = new JMenuItem("Cheat");
        mnuItems[4].setAccelerator(KeyStroke.getKeyStroke('C', ActionEvent.ALT_MASK));
        mnuItems[4].addActionListener(this);
        mnuGame.add(mnuItems[4]);
        
        mnuItems[5] = new JMenuItem("Quit");
        mnuItems[5].setAccelerator(KeyStroke.getKeyStroke('Q', ActionEvent.ALT_MASK));
        mnuItems[5].addActionListener(this);
        mnuGame.add(mnuItems[5]);

        setJMenuBar(menuBar);   // tell the frame which menu bar to use
    }
    
    /* Listener to respond to mouse clicks on the table */
    private MouseAdapter myMouseListener = new MouseAdapter()
    {
        public void mouseReleased(MouseEvent ev)
        {
            int col = table.getSelectedColumn();
            int row = table.getSelectedRow();
            // call methods to handle player's click
            clickTile(row, col);
            repaint();
        }
    };
    
    protected void loadImages()
    {
        // load background image
        background = new ImageIcon( 
                    Toolkit.getDefaultToolkit().getImage(
                        this.getClass().getResource("PieceImages/bkgd.jpg")));
        // Load tile images here
    }
    
    protected void newGame()
    {
        this.gameNumber++;
        restartGame();
    }

    protected void restartGame()
    {
        if (this.myBoard == null)
        {
            this.myBoard = new Tile[this.kBoardHeight][this.kBoardWidth];
        }
        else
        {
            clearBoard();
        }
        
        // Fill the board with normal (non-bomb) pieces.
        for (int line = 0; line < this.kBoardHeight; line++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                this.myBoard[line][column] = new Tile();
            }
        }
        
        // Figure out (deterministically) where we want the bombs.
        // Storing them in a Set allows us to avoid duplicates.  Unfortunately,
        // between Java's lack of type inference, the necessary type-specifying
        // required by a statically-typed language using a system like
        // generics, and the fact that you can't genericize primitives like
        // int, the logic here gets muddled a bit by syntax.
        Set<Pair<Integer, Integer>> bombSet = new TreeSet<Pair<Integer, Integer>>();
        java.util.Random generator = new java.util.Random(this.gameNumber);
        for (int bombNumber = 0; bombNumber < this.kMaxBombs; bombNumber++)
        {
            int row = generator.nextInt(boardsize);
            int column = generator.nextInt(boardsize);
            bombSet.add(new Pair<Integer, Integer>(row, column));
        }
        this.bombs = bombSet.size();
        for (Pair<Integer, Integer> bombCoordinates : bombSet)
        {
            this.myBoard[bombCoordinates.first][bombCoordinates.second].setBombStatus(true);
        }

        this.secondsElapsed = 0;
        updateStatusBar();
        setTitle("Mahjongg - board " + this.gameNumber);
    }
    
    protected void startTimer()
    {
        // Every 1 second.
        Timer timer = new Timer(1000, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Kaboom.this.secondsElapsed++;
                Kaboom.this.updateStatusBar();
            }
        });
        
        Kaboom.this.updateStatusBar();
        timer.start();
    }
    
    protected void updateStatusBar()
    {
       this.lblStatus.setText("Moves: " + this.moves + "  "
                            + "Flags: " + this.flagsPlaced + "/" + this.numBombs + "  "
                            + "Time: " + this.secondsElapsed / 60 + ":" + String.format("%02d", this.secondsElapsed % 60));
    }
    
    /** Handle button clicks
     * @param e The result of what was clicked
     */
    public void actionPerformed(ActionEvent e) 
    {
        // Does the user want to restart the current game?
        if ("Restart".equals(e.getActionCommand()))
        {
            restartGame();
        }
        else if ("New Game".equals(e.getActionCommand()))
        {
            newGame();
        }
        else if ("Select Game".equals(e.getActionCommand()))
        {
            // TODO
        }
        else if ("Scores".equals(e.getActionCommand()))
        {
            // TODO
        }
        else if ("Cheat".equals(e.getActionCommand()))
        {
            // TODO
            updateStatusBar();
        }
        else if ("Quit".equals(e.getActionCommand()))
        {
            System.exit(0);
        }
        repaint();
    }

    protected void clearBoard()
    {
        for (int row = 0; row < this.kBoardHeight; row++)
        {
            for (int column = 0; column < this.kBoardWidth; column++)
            {
                this.myBoard[row][column] = null;
            }
        }
    }
    
    protected void clickTile(final int row, final int column)
    {
        // Basic sanity check.
        if (row < 0 || row > this.kBoardHeight || column < 0 || column > this.kBoardWidth)
        {
            throw new IllegalArgumentException("Tile must be on the board.");
        }
        
        // TODO
    }
    
    // Local main to launch the GUI
    public static void main(String[] args)
    {
        // Create the GUI 
        Kaboom frame = new Kaboom();
        
        frame.layoutGUI();   // do the layout of widgets
        
        // Make the GUI visible and available for user interaction
        frame.pack();
        frame.setVisible(true);
    }
}  // end class

class Tile extends ImageIcon implements Comparable<Tile>, Renderable
{
    private bool isBomb = false;
    
    public boolean equals(Object other)
    {
        if (other instanceof Tile)
        {
            Tile otherTile = (Tile)other;
            if (this.suit == otherTile.suit && this.rank == otherTile.rank)
            {
                return true;
            }
        }
        
        return false;
    }

    public int compareTo(Tile other)
    {
        if (this.suit == other.suit)
        {
            return new Integer(this.rank).compareTo(other.rank);
        }
        
        return new Integer(this.suit.ordinal()).compareTo(other.suit.ordinal());
    }

    public String toString()
    {
        return this.suit.name() + " " + this.rank;
    }
    
    public RenderDescriptor getRenderDescriptor()
    {
        RenderDescriptor renderDescriptor = new RenderDescriptor();
        return renderDescriptor;
    }
    
    public void setBombStatus(boolean isBomb)
    {
        this.isBomb = isBomb;
    }
}

/** This is a silly little class, created because Java doesn't have 2-tuples
 * (or n-tuples of any sort, for that matter).
 */
class Pair<E1, E2> {
    public E1 first;
    public E2 second;
    
    public void Pair<E1, E2>(E1 first, E2 second)
    {
        this.first = first;
        this.second = second;
    }
}

