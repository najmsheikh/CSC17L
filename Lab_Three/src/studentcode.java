class studentcode extends mazedfs {
    public studentcode(int bh0, int mh0, int mw0) {
        super(bh0, mh0, mw0);
    }

    /*
    *   Name:       digout()
    *   Input:      y-axis coordinate; x-axis coordinate
    *   Output:     N/A
    *   Purpose:    Recursively generate a random maze.
    */
    public void digout(int y, int x) {
        M[y][x] = 1;                // digout maze at coordinate y,x
        drawblock(y, x);            // change graphical display to reflect space dug out
        delay(40);             // slows animation


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
                drawblock(y + dy, x + dx);   // visually represent the block being dug out
                digout(ny, nx);                     // recurse, with the new y,x coordinates
            }
        }
    }
}


