// Name:  Najm Sheikh

import java.util.PriorityQueue;

public class myastar extends astar{
    public myastar(int r, int c) { super(r,c); }

    public coord search(int sy, int sx, int ty, int tx) {
        // Initialize the frontier alongwith the status matrix
        PriorityQueue<coord> frontier = new PriorityQueue<coord>();
        coord[][] status = new coord[ROWS][COLS];

        // Initialize starting coordinate
        coord curr = new coord(sy, sx);
        curr.estcost = hexdist(sy, sx, ty, tx);

        // Add the starting coord to the frontier and update status
        frontier.add(curr);
        status[sy][sx] = curr;

        boolean stop = false;

        // Keep searching until the target coord is found or if there
        // is no more frontier left to search from.
        while (!stop && frontier.size() != 0) {
            curr = frontier.poll();                     // Frontier coord with lowest cost
            curr.interior = true;                       // Is now part of the interior

            status[curr.y][curr.x] = curr;              // Update status

            // Search through every neighbor in each direction
            for (int i = 0; i < 6; i++) {
                int ny = curr.y + DY[i];                // Y-Coordinate using shift instruction
                int nx = curr.x + DX[curr.y % 2][i];    // X-Coordinate using shift instruction

                coord neighbor = new coord(ny, nx);     // A specific neighbor

                // Check if neighbor is not out of bounds. If accessible, make sure
                // it hasn't been updated yet nor is it part of the interior.
                if (ny >= 0 && ny < ROWS && nx >= 0 && nx < COLS && (status[ny][nx] == null || !status[ny][nx].interior)) {
                    neighbor.knowndist = costof[M[ny][nx]] + curr.knowndist;            // Calculate neighor's distance so far
                    neighbor.estcost = neighbor.knowndist + hexdist(ny, nx, ty, tx);    // Estimate cost using heuristic
                    neighbor.prev = curr;                                               // Setup trail path back

                    frontier.add(neighbor);                                             // Push to the frontier

                    // If the newly found path is better, make it our neighbor.
                    if (status[ny][nx] == null || status[ny][nx].compareTo(neighbor) < 0) {
                        status[ny][nx] = neighbor;
                    }
                }
            }

            // If we are at the target coord, stop.
            if (curr.y == ty && curr.x == tx) {
                stop = true;
            }
        }
        return curr;
    }   
}
