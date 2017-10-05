import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class mazedfs extends JFrame implements KeyListener {
    /* default values: */
    protected int mh = 41;    // height and width of maze
    protected int mw = 51;

    protected byte[][] M;    // the array for the maze
    public static final int SOUTH = 0;
    public static final int EAST = 1;
    public static final int NORTH = 2;
    public static final int WEST = 3;

    protected boolean showvalue = false; // affects drawblock
    protected boolean autodelay = false;  // delays automatically between steps

    // graphical properties:
    protected static int bh = 16;    // height of a graphical block
    protected static int bw = 16;    // width of a graphical block
    protected int ah, aw;    // height and width of graphical maze
    protected int yoff = 80;    // init y-cord of maze
    protected Graphics g;
    protected int dtime = 40;   // 40 ms delay time
    protected Color wallcolor = Color.green;
    protected Color pathcolor = Color.black;
    protected Color dotcolor = Color.red;


    // args determine block size, maze height, and maze width
    public mazedfs(int bh0, int mh0, int mw0) {
        bh = bw = bh0;
        mh = mh0;
        mw = mw0;
        ah = bh * mh;
        aw = bw * mw;
        M = new byte[mh][mw];  // initialize maze (all  0's - walls).
        this.setBounds(0, 0, aw + 10, 10 + ah + yoff);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        } // Synch with system
        this.addKeyListener(this);
        g = getGraphics();    //g.setColor(Color.red);
        setup();
    }

    public void paint(Graphics g) {
    } // override automatic repaint

    public void setup() {
        g.setColor(wallcolor);
        g.fill3DRect(0, yoff, aw, ah, true);  // fill raised rectangle
        g.setColor(pathcolor);
        customize(); // optional startupcode
        //     showStatus("Generating maze...");
        digout(mh - 2, mw - 2); // start digging!
        // digout exit (if digout complete)
        if (M[mh - 2][mw - 2] != 0) {
            M[mh - 1][mw - 2] = M[mh - 2][mw - 1] = 1;
            drawblock(mh - 2, mw - 1);
        }
        solve();  // this is the function you will write for lab 3, part 1
        trace();  // for part 2
        play();   // for part 3
    }

    public static void main(String[] args) {
        int blocksize = bh, mheight = 11, mwidth = 11; // need to be odd
        if (args.length == 3) {
            mheight = Integer.parseInt(args[0]);
            mwidth = Integer.parseInt(args[1]);
            blocksize = Integer.parseInt(args[2]);
        }
        mazedfs W = new studentcode(blocksize, mheight, mwidth);
    }

    public void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
        }
    }

    public void drawblock(int y, int x) {
        g.setColor(pathcolor);
        g.fillRect(x * bw, yoff + (y * bh), bw, bh);
        g.setColor(Color.yellow);
        // following line displays value of M[y][x] in the graphical maze:
        if (showvalue)
            g.drawString("" + M[y][x], (x * bw) + (bw / 2 - 4), yoff + (y * bh) + (bh / 2 + 6));
    }

    public void drawdot(int y, int x) {
        g.setColor(dotcolor);
        g.fillOval(x * bw, yoff + (y * bh), bw, bh);
        if (autodelay) try {
            Thread.sleep(dtime);
        } catch (Exception e) {
        }
    }

    public void drawMessage(String m) {
        g.setColor(Color.green);
        g.fillRect(0, yoff, bw * mw, bh);
        g.setColor(Color.blue); // erase line
        g.drawString(m, 10, yoff + bh - 4);
    }

////// the following functions are to be overriden in subclass:

    public void customize()  // user-defined initialization code
    {
    }

/* function to generate random maze */

    public void digout(int y, int x)    // override for lab 3 (maze generation)
    {
        // generates maze
    } // digout


    /* Write a routine to solve the maze.
       Start at coordinates x=1, y=1, and stop at coordinates
       x=mw-1, y=mh-2.  This coordinate was especially dug out
       after the program called your digout function (in the "actionPerformed"
       method).
    */
    public void solve()    // override for lab 4 part 1
    {
        int x = 1, y = 1;
        //      drawdot(y,x);
        // drawblock(y,x) will erase the dot
        // modify this function to move the dot to the end of the maze.  That
        // is, when the dot reaches y==mh-2, x==mw-2
    } // solve

    public void trace()     // override for lab 4 part 2, lab 5
    {  // draw a dot (without erasing it) along the OPTIMAL path
    }

    ///////////////////////////////////////////////////////////////
    /// For part three (save a copy of part 2 version first!), you
    // need to implement the KeyListener interface.

    public void play() // override for lab 4, final part
    {
        // code to setup game
    }
    // for this part you may also define some other instance vars outside of
    // the play function.

    // skeleton implementation of KeyListener interface
    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) // change this one
    {
        int key = e.getKeyCode();       // code for key pressed
        System.out.println("YOU JUST PRESSED KEY " + key);
    }

} // mazedfs

