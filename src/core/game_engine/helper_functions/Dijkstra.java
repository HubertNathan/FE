package core.game_engine.helper_functions;

import core.Board;
import javafx.util.Pair;

import java.util.*;

public class Dijkstra{
    private static final byte[] moveRow = {-1, 1, 0, 0}, moveCol = {0,0,-1,1};
    public static Pair<ArrayList<Coord>, ArrayList<Coord>> findMoves(Board board, byte i, byte j, byte mov, String unitType){
        byte width = board.getWidth();
        byte height = board.getHeight();

        final PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(a->a.distance));
        short[][] distances = new short[height][width];
        for (short[] row : distances) Arrays.fill(row, Byte.MAX_VALUE);

        distances[i][j] = 0;
        queue.offer(new Node(j,i,(short)0));
        ArrayList<Coord> availableMoves = new ArrayList<>(), tilesInRange = new ArrayList<>(), occupiedTiles = new ArrayList<>();
        Node prev = null;

        while (!queue.isEmpty()){
            Node cur = queue.poll();
            byte x = cur.getFirst(), y = cur.getLast();
            short distance = cur.distance;
            if (prev != null && board.get(y,x).getUnit() != null) occupiedTiles.add(new Coord(x,y));

            if (distance > mov) {
                break;
            }

            availableMoves.add(new Coord(x,y));

            for (int k = 0; k < 4; k++) {
                byte newX = (byte) (x + moveRow[k]), newY = (byte) (y + moveCol[k]);
                if (newX >= 0 && newY >= 0 && newX < width && newY < height){
                    if (board.get(newY,newX).getTerrain().getMovPenalty(unitType) < 0) continue;
                    short newDistance = (short) (distance + board.get(newY,newX).getTerrain().getMovPenalty(unitType));
                    if (newDistance < distances[newY][newX]){
                        distances[newY][newX] = (short) newDistance;
                        queue.offer(new Node(newX,newY,newDistance));
                    }
                }

            }
            prev = cur;
        }
        for (Coord pos : occupiedTiles){
            availableMoves.remove(pos);
        }
        findTilesInRange(availableMoves,tilesInRange,1,width,height);
        return new Pair<>(availableMoves,tilesInRange);
    }
    private static void findTilesInRange(ArrayList<Coord> availableMoves, ArrayList<Coord> tilesInRange, int range, int w, int h){
        Queue<Coord> queue = new LinkedList<>();

        boolean[][] visitedNodes = new boolean[h][w];
        for (Coord coord : availableMoves) {
            queue.offer(coord);
            visitedNodes[coord.getLast()][coord.getFirst()] = false;
        }
        short distance = 0;
        while (!queue.isEmpty() && distance < range){
            int levelSize = queue.size();
            for (short i = 0; i < levelSize; i++) {
                Coord cur = queue.poll();
                assert cur != null;
                for (byte j = 0; j < 4; j++){
                    byte x = (byte) (cur.getFirst() + moveRow[j]);
                    byte y = (byte) (cur.getLast() + moveCol[j]);
                    if (x>=0 && y>=0 && x<w && y<h && !visitedNodes[y][x]){
                        Coord next = new Coord(x,y);
                        visitedNodes[y][x] = true;

                        queue.offer(next);
                        if (!availableMoves.contains(next)) tilesInRange.add(next);
                    }
                }
            }
            distance++;

        }

    }
    public static ArrayList<Coord> findPath(Coord start, Coord end, Board board, String unitType) {
        if (start.equals(end)) return null;  // No path if start and end are the same

        final byte width = board.getWidth();
        final byte height = board.getHeight();

        // Priority queue for Dijkstra's algorithm (sorted by distance)
        final PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.distance));

        // Distance array to track shortest distance from start to each position
        short[][] distances = new short[height][width];
        for (short[] row : distances) Arrays.fill(row, Short.MAX_VALUE);

        // Predecessor array to track the predecessor for each position using a 1D index
        short[] predecessor = new short[width * height];
        Arrays.fill(predecessor, (short) -1);  // -1 indicates no predecessor (unvisited)

        // Map (x, y) to a single index in 1D array: index = y * width + x
        int startIndex = start.getLast() * width + start.getFirst();
        int endIndex = end.getLast() * width + end.getFirst();

        // Initialize distance for the start position
        distances[start.getLast()][start.getFirst()] = 0;
        queue.offer(new Node(start.getFirst(), start.getLast(), (short)0));

        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            int x = cur.getFirst(), y = cur.getLast(), distance = cur.distance;

            // If we've reached the end node, break the loop
            if (cur.getFirst() == end.getFirst() && cur.getLast() == end.getLast()) break;

            // Explore the 4 neighboring tiles
            for (byte i = 0; i < 4; i++) {
                byte newX = (byte) (x + moveRow[i]);
                byte newY = (byte) (y + moveCol[i]);

                // Check if the new position is within the grid and is traversable
                if (newX >= 0 && newY >= 0 && newX < width && newY < height) {
                    if (board.get(newY, newX).getTerrain().getMovPenalty(unitType) < 0) continue;  // Skip if unreachable

                    short newDistance = (short) (distance + board.get(newY, newX).getTerrain().getMovPenalty(unitType));

                    // Relaxation step: update if a shorter path is found
                    if (newDistance < distances[newY][newX]) {
                        distances[newY][newX] = newDistance;
                        queue.offer(new Node(newX, newY, newDistance));

                        // Store the predecessor using 1D indexing
                        short newIndex = (short) (newY * width + newX);
                        predecessor[newIndex] = (short) (y * width + x);  // Store where we came from
                    }
                }
            }
        }

        // Now we backtrack from the end node to start to build the path
        ArrayList<Coord> path = new ArrayList<>();
        int step = endIndex;

        // If the end node has no predecessor, that means no path was found
        if (predecessor[endIndex] == -1) return null;

        // Backtrack from end to start
        while (step != startIndex) {
            byte stepX = (byte) (step % width);  // Convert 1D index back to 2D coordinates
            byte stepY = (byte) (step / width);
            path.add(new Coord(stepX, stepY));
            step = predecessor[step];  // Move to the predecessor node
        }

        // Add the start node
        path.add(start);

        // Reverse the path since we added it from end to start
        Collections.reverse(path);

        return path;
    }
    private static class Node extends Coord {
        private short distance;
        Node(byte x, byte y, short distance){
            super(x,y);
            this.distance = distance;
        }
    }
}