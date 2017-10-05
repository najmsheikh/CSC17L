import java.awt.*;
import java.awt.event.KeyEvent;

class studentcode extends mazedfs {
    class Coord implements Comparable<Coord> {
        public int x, y;

        public Coord(int y, int x) {
            this.y = y;
            this.x = x;
        }

        //        Check that the new coordinates are legal.
//        Essentially making sure that we do not go out of bound, or step into a wall.
        public boolean isValid() {
            return this.x >= 0 && this.x < mw && this.y >= 0 && this.y < mh && M[this.y][this.x] != 0;
        }

        public int compareTo(Coord o) {
            return y == o.y ? x - o.x : y - o.y;
        }

        public String toString() {
            return "[ " + y + ", " + x + " ]";
        }

    }

    public studentcode(int bh0, int mh0, int mw0) {
        super(bh0, mh0, mw0);
    }

    //    We create a CList of Coordinates that, at first, contain coordinates for every block that the
//    the maze solver has stepped over. Then, we will cut out the redundant paths within this list to
//    have the optimal path from the beginning of the maze to the end.
    CList<Coord> steps;

    int y = 1, x = 1;           // starting point for solver and player
    boolean won = false;        // game win state
    int[] GDy = {-1, 0, 1, 0};   // shift instruction of y,x pair on the y-axis
    int[] GDx = {0, 1, 0, -1};   // shift instruction of y,x pair on the x-axis

    /*
    *   Name:       digout()
    *   Input:      y-axis coordinate; x-axis coordinate
    *   Output:     N/A
    *   Purpose:    Recursively generate a random maze.
    */
    public void digout(int y, int x) {
        M[y][x] = 1;                // digout maze at coordinate y,x
        drawblock(y, x);            // change graphical display to reflect space dug out
//        delay(40);                 // slows animation


//        N, E, S, W
        int[] P = {0, 1, 2, 3};     // the set of directions, indexed
        int[] Dy = {-1, 0, 1, 0};   // shift instruction of y,x pair on the y-axis
        int[] Dx = {0, 1, 0, -1};   // shift instruction of y,x pair on the x-axis

//        Randomize directions using permutation. Essentially, rewrite the direction set in a
//        randomized order so that on each digout(), the program likely goes in a different direction.
        for (int i = 0; i < P.length; i++) {
            int r = i + (int) (Math.random() * P.length - i);
            int temp = P[i];
            P[i] = P[r];
            P[r] = temp;
        }

//        Go through the directions.
//        For example, if P[3, 1, 2, 0], the directions would be like W->E->S->N
        for (int dir = 0; dir < 4; dir++) {
//            Since we've picked the direction, now we must translate it into a shift instruction.
            int dy = Dy[P[dir]];    // shift instruction for the chosen direction on the y-axis
            int dx = Dx[P[dir]];    // shift instruction for the chosen direction on the x-axis

            int ny = y + 2 * dy;    // y coordinate for the block two steps away
            int nx = x + 2 * dx;    // x coordinate for the block two steps away

//            Check that the point (two steps away) is legal and that the direction is valid.
//            This is essential because we want to make sure we do not hit a wall -- thus creating
//            dug out lumps -- or go outside of the maze.
            if (nx >= 0 && nx < mw && ny >= 0 && ny < mh && M[ny][nx] == 0) {
                M[y + dy][x + dx] = 1;              // digout, following the shift instruction
                drawblock(y + dy, x + dx);          // visually represent the block being dug out
                digout(ny, nx);                     // recurse, with the new y,x coordinates
            }
        }
    }

    /*
    *   Name:       solve()
    *   Input:      N/A
    *   Output:     N/A
    *   Purpose:    Find a way out of the maze.
    */
    public void solve() {
        steps = new CList<>();      // this list contains every coordinate the solver has 'stepped over'
        int y = 1, x = 1;           // begin at the top left corner
        M[y][x] = 2;                // indicate that the first block has been 'stepped over'
        drawdot(y, x);              // draw a red dot to track progress of the solver
        steps.push(new Coord(y, x));// add the first step

//        N, E, S, W
        int[] Dy = {-1, 0, 1, 0};   // shift instruction of y,x pair on the y-axis
        int[] Dx = {0, 1, 0, -1};   // shift instruction of y,x pair on the x-axis

//        The solver will keep trying to find a way out until it is a step away from the bounds.
        while (y != mh - 2 || x != mw - 2) {
            Coord minCoord = new Coord(-1, -1);

            Coord newCoord = minCoord;

//            Go through the directions.
//            Since we are not generating a new maze, we don't have to care about choosing it
//            randomly. As such, the solver will always look for the best path in a clockwise pass.
            for (int dir = 0; dir < 4; dir++) {
                newCoord = new Coord(y + Dy[dir], x + Dx[dir]);

//                Check that the new coordinates are legal.
//                Essentially making sure that we do not go out of bound, or step into a wall.
                if (newCoord.isValid()) {
//                    Find and assign 'The Road Not Taken' (least used path).
                    if (minCoord.x == -1 || M[newCoord.y][newCoord.x] < M[minCoord.y][minCoord.x]) {
                        minCoord = newCoord;
                    }
                }
            }

//            Although there is no need to re-assign values, we do so for the sake of readability.
            newCoord = minCoord;

            M[newCoord.y][newCoord.x]++;            // indicate that the block has been stepped over
            drawblock(y, x);                        // redraw previous block, to remove the dot
            drawdot(newCoord.y, newCoord.x);        // draw dot in the new block, to track progress
            steps.push(newCoord);                   // push coordinate to the list of steps taken

//            Re-assign coordinates to keep the solver moving
            y = newCoord.y;
            x = newCoord.x;
        }

        drawMessage("You made it!");
    }

    /*
    *   Name:       trace()
    *   Input:      N/A
    *   Output:     N/A
    *   Purpose:    Trace back the optimal path through the maze.
    */
    public void trace() {
//        Cut out the redundant paths.
        steps.cut();

//        Go through each step in the optimal path and draw a path with dots
        for (Coord coord :
                steps) {
            drawdot(coord.y, coord.x);
        }
    }

    /*
    *   Name:       play()
    *   Input:      N/A
    *   Output:     N/A
    *   Purpose:    Allow the user to go through the maze, making fun of them in the process.
    */
    public void play() {
//        Wait five seconds before resetting the maze
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        super.autodelay = false;                            // make removal of dots quick
        super.dotcolor = Color.white;                       // change the color of the dot

//        Redraw over every dot to hide the path
        for (Coord coord :
                steps) {
            drawblock(coord.y, coord.x);
        }

        drawdot(1, 1);                              // place user at the beginning
        drawblock(mh - 2, mw - 2);                  // remove last remaining dot
        drawMessage("Go onwards, young padawan!");      // encourage user to try their hand at this madness
    }

    //    Every time an arrow key is pressed, call move()
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int dir = key == KeyEvent.VK_UP ? 0 : key == KeyEvent.VK_RIGHT ? 1 : key == KeyEvent.VK_DOWN ? 2 : key == KeyEvent.VK_LEFT ? 3 : -1;
        if (dir != -1)
            move(dir);
    }


    private void move(int dir) {
        Coord move = new Coord(y + GDy[dir], x + GDx[dir]);     // create temporary Coord for the desired move

//        If the move is valid and the user has not won yet, let them keep trying
        if (move.isValid() && !won) {
//            If they hit the exit, change game state and prevent future moves
            if (move.y == mh - 2 && move.x == mw - 1) {
                drawMessage("Great! You made it out alive.");
                drawblock(y, x);                // remove dot from previous coordinate
                drawdot(move.y, move.x);        // draw dot on final coordinate

                won = true;                     // change game state

                return;
            }

            drawblock(y, x);                    // remove dot from previous coordinate
            drawdot(move.y, move.x);            // draw dot on new coordinate

//            If the move the user made is not part of the optimal path (the only path), make fun of them
            if (!steps.contains(move))
                drawMessage("Hah! Are you sure you want to go down this path?");
            else
                drawMessage("");

            y = move.y;                         // save position on the y-axis
            x = move.x;                         // save position on the x-axis
        }
    }
}



