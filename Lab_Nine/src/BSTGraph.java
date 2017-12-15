/* Graphical representation of binary trees (Fall 2017)

   To use, this file and "Bst.java" must be in same directory:

   Everytime drawtree is called, the background is cleared, so you
   don't have to create a new Bstgraph object to draw a new tree.

   Bst<T extends Comparable<T>> interface must contain a int depth()
   function that returns the depth or height of the three.

   vertex<T extends Comparable<T>> class expected to include
   method names item(), left(), and right().
   item is of type T and left, right are of type Bst<T>.

   See "main" at end of file for sample usage
*/


import javax.swing.*;
import java.awt.*;

public class BSTGraph extends JFrame {
    public int XDIM, YDIM;
    public Graphics display;
    public BST<?> currenttree;

    public void paint(Graphics g) {
        drawtree(currenttree);
    } // override method

    // constructor sets window dimensions
    public BSTGraph(int x, int y) {
        XDIM = x;
        YDIM = y;
        this.setBounds(0, 0, XDIM + 4, YDIM);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display = this.getGraphics();
        // draw static background as a black rectangle
        display.setColor(Color.black);
        display.fillRect(0, 0, x, y);
        display.setColor(Color.red);
        try {
            Thread.sleep(700);
        } catch (Exception e) {
        } // Synch with system
    }  // drawingwindow


    // internal vars used by drawtree routines:
    public int bheight = 50; // default branch height
    public int yoff = 60;  // static y-offset

    // l is level, lb,rb are the bounds (position of left and right child)

    public void drawtree(BST<?> T) {
        if (T == null) return;
        currenttree = T;
        int d = T.depth();
        display.setColor(Color.white);
        display.fillRect(0, 0, XDIM, YDIM);  // clear background
        if (d < 1) return;
        bheight = (YDIM / d);
        draw(T, 0, 0, XDIM);
    }

    public void draw(BST<?> N, int l, int lb, int rb) {
        if (N.isEmpty()) return;
        //	try{Thread.sleep(10);} catch(Exception e) {} // slow down
        display.setColor(Color.green);
        display.fillOval(((lb + rb) / 2) - 10, yoff + (l * bheight), 20, 20);
        display.setColor(Color.red);
        display.drawString(N.item() + "", ((lb + rb) / 2) - 5, yoff + 15 + (l * bheight));
        display.setColor(Color.blue); // draw branches
        if (!N.left().isEmpty()) {
            display.drawLine((lb + rb) / 2, yoff + 10 + (l * bheight),
                    ((3 * lb + rb) / 4), yoff + (l * bheight + bheight));
            draw(N.left(), l + 1, lb, (lb + rb) / 2);
        }
        if (!N.right().isEmpty()) {
            display.drawLine((lb + rb) / 2, yoff + 10 + (l * bheight),
                    ((3 * rb + lb) / 4), yoff + (l * bheight + bheight));
            draw(N.right(), l + 1, (lb + rb) / 2, rb);
        }
    } // draw


    /* sample use:  (put this in another file) **************
    public static void main(String[] args)
    {
      BSTGraph W = new BSTGraph(1024,768); // for graphical rendering
      Bst<Integer> tree = new Nil<Integer>();
      for(int i = 0;i<64;i++)
	  tree = tree.insert((int)(Math.random()*100));
      Integer rx = tree.item();
      W.drawtree(tree);
      try{Thread.sleep(5000);} catch(Exception e) {} // 5 sec delay
      tree = tree.delete(rx);
      W.drawtree(tree);
      W.display.drawString("Do you like my tree?",20,W.YDIM-50);
      // System.out.println(tree); // should be sorted
    }  // main
    ********************/

} // BSTGraph
