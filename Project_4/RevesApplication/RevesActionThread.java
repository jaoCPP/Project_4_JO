import animatedapp.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  A thread that is used to solve Reve's problem - It is animated.
 * 
 * @author Charles Hoot 
 * @version 5.0
 */

    
public class RevesActionThread extends ActionThread
{
    
    /**
     * Constructor for objects of class RevesActionThread
     */
    public RevesActionThread()
    {
        super();

    }

    public String getApplicationTitle()
    {
        return "Reve's Puzzle (Skeleton)";
    }
    
    

    // **************************************************************************
    // This is application specific code
    // **************************************************************************    

    // These are the variables that are parameters of the application and can be
    // set via the application specific GUI
    // Make sure they are initialized
    private int disksToUse = 10;
   
    
    
    private int disks;
    
    // Displayed objects
    private Pole a, b, c, d;
    private int movesMade;
    private String moveString;
    
    public void init() 
    {
        disks = disksToUse;
        movesMade = 0;
        moveString = "";

        // ADD INITIALIZATION CODE HERE
        // Creates four poles with size for maximum disks
        a = new Pole("A", MAX_DISKS);
        b = new Pole("B", MAX_DISKS);
        c = new Pole("C", MAX_DISKS);
        d = new Pole("D", MAX_DISKS);

        // Clears any existing disk from the poles
        while(a.removeDisk() != null){a.removeDisk();}
        while(b.removeDisk() != null){b.removeDisk();}
        while(c.removeDisk() != null){c.removeDisk();}
        while(d.removeDisk() != null){d.removeDisk();}

        // Add disks to pole A in descending order of size
        for(int i = disks; i >= 1; i--){
            a.addDisk(new Disk(i));
        }
    }
        

    public void executeApplication()
    {
        // ADD CODE THAT WILL DO A SINGLE EXECUTION
        reves(disks, a, d, b, c);
    }

    /**
     * Move a disk from one pole to another pole.
     *
     * @param from      The source pole.
     * @param to        The destination pole.
     */
    public void moveDisk(Pole from, Pole to)
    {
        Disk toMove = null;
        
        // ADD CODE HERE TO MOVE A DISK FROM ONE POLE TO THE OTHER
        // Remove top disk from "from" (source) pole
        toMove = from.removeDisk();
        // Adds removed disk to "to" (destination) pole
        if(toMove != null){
            to.addDisk(toMove);
        }

        movesMade++;
        moveString = "Move #" + movesMade 
                        + ": Moved disk " + toMove.getSize() 
                        + " from " + from.getName() 
                        + " to " + to.getName() ;
                        
        animationPause();            
    }

    
    // ADD METHODS HERE
    private void towersOfHanoi(int n, Pole from, Pole to, Pole extra){
        if(n>0) {
            // Moves n-1 disk from "from" (source) to the "extra" pole
            towersOfHanoi(n - 1, from, extra, to);

            // Moves the nth disk from "from" (source) to "to" (destination) pole
            moveDisk(from, to);

            // Moves n-1 disks from the "extra" pole to "to" (destination) pole
            towersOfHanoi(n - 1, extra, to, from);
        }
    }

    private void reves(int n, Pole from, Pole to, Pole extra1, Pole extra2){
        // Base case
        if(n==1){
            moveDisk(from, to);
            return;
        }

        int k = computeK(n);

        reves(n - k, from, extra1, to, extra2);
        towersOfHanoi(k, from, to, extra2);
        reves(n - k, extra1, to, from, extra2);
    }

    private int computeK(int n) {
        // Solves quadratic equation k^2 + k - 2n = 0; gives k where T_k is approx. n
        double unRoundedK = (Math.sqrt((double)(1+8*n) - 1) / 2);

        int roundedK;

        // Increments k until T_k > n
        for(roundedK = (int)unRoundedK; roundedK*(roundedK+1)/2 <= n; roundedK++){}

        // Decrements k until T_k <= n
        while(roundedK*(roundedK+1)/2 > n){
            --roundedK;
        }

        return roundedK;
    }
    
    /***************************************************************************
     * *************************************************************************
     * ALL THE CODE PAST THIS POINT SHOULD NOT BE CHANGED
     * *************************************************************************
     * *************************************************************************
     */
    
    
    
    private static int DISPLAY_HEIGHT = 300;
    private static int DISPLAY_WIDTH = 500;
    
    public JPanel createAnimationPanel()
    {
        return new AnimationPanel();
    }
    
    private static int NORTH_PANEL_HEIGHT = 50;
    private static int INDENT = 50;
    private static int SCALE = 2;
    private static int TEXT_HEIGHT = 30;
    private static int MAX_DISKS = 15;
    

    // This privately defined class does the drawing the application needs
    public class AnimationPanel extends JPanel
    {
        public AnimationPanel()
        {
            super();
            setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        }
        
        public void paintComponent(Graphics g)
        {
            int sw;
            super.paintComponent(g);
            int delta = Disk.BASEWIDTH*SCALE*MAX_DISKS;
            
            // draw the move string if it has a value
            if(moveString != null)
            {
                g.drawString(moveString, INDENT, NORTH_PANEL_HEIGHT + TEXT_HEIGHT);
            }
            
            
            // draw the four poles if they have been created
            FontMetrics fm = g.getFontMetrics();            
            if(d!= null)
            {
                a.drawOn(g, delta/2, DISPLAY_HEIGHT-2*TEXT_HEIGHT, SCALE);
                sw = fm.stringWidth(a.getName());
                g.drawString(a.getName(), delta/2 - sw/2, DISPLAY_HEIGHT-TEXT_HEIGHT);
                
                b.drawOn(g, 3*delta/2, DISPLAY_HEIGHT-2*TEXT_HEIGHT, SCALE);
                sw = fm.stringWidth(b.getName());
                g.drawString(b.getName(), 3*delta/2 - sw/2, DISPLAY_HEIGHT-TEXT_HEIGHT);
                
                c.drawOn(g, 5*delta/2, DISPLAY_HEIGHT-2*TEXT_HEIGHT, SCALE);
                sw = fm.stringWidth(c.getName());
                g.drawString(c.getName(), 5*delta/2 - sw/2, DISPLAY_HEIGHT-TEXT_HEIGHT);
                
                d.drawOn(g, 7*delta/2, DISPLAY_HEIGHT-2*TEXT_HEIGHT, SCALE);
                sw = fm.stringWidth(d.getName());
                g.drawString(d.getName(), 7*delta/2 - sw/2, DISPLAY_HEIGHT-TEXT_HEIGHT);
            }

        }
    }
    
    // **************************************************************************
    // This is the application specific GUI code
    // **************************************************************************    

    private JTextField disksTextField;
    private JLabel setupStatusLabel;
    private JPanel setupPanel;
    
    public void setUpApplicationSpecificControls()
    {
        getAnimationPanel().setLayout(new BorderLayout());
        
        
        disksTextField = new JTextField("");
        disksTextField.addActionListener(
            new ActionListener() 
            {
                public void actionPerformed(ActionEvent event) 
                {
                    disksTextFieldHandler();
                    getAnimationPanel().repaint();
                }
            }
        );


        
        setupStatusLabel = new JLabel("");
        
        setupPanel = new JPanel();
        setupPanel.setLayout(new GridLayout(2,2));
        
        setupPanel.add(new JLabel("Number of disks (1-15):"));
        setupPanel.add(disksTextField);
        setupPanel.add(setupStatusLabel);
        
        getAnimationPanel().add(setupPanel,BorderLayout.NORTH);
               
    }

   
   
    private void disksTextFieldHandler()
    {
    try
        {
            if(applicationControlsAreActive())   // Only change if we are in the setup phase
            {
                String input = disksTextField.getText().trim();
                int value = Integer.parseInt(input);
                if( value>=1 &&value <= MAX_DISKS)
                {
                    disksToUse = value;
                    setupStatusLabel.setText("Set number of disks to " + disksToUse);
                }
                else
                {
                    setupStatusLabel.setText("Bad value for number of disks");
                }
                init();
                getAnimationPanel().repaint();
                
            }
        }
        catch(Exception e)
        {
            // don't change the delta if we had an exception
            setupStatusLabel.setText("Need integer value for number of disks");
        }
    
    }  
            
} // end class RevesActionThread

