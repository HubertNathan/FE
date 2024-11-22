package Units;

import GUI.Animations.ColouredSquaresAnimation;
import GUI.Animations.SpriteAnimation;
import GUI.Battle;
import GUI.ResizableImage;
import GameEngine.Board;
import GameEngine.Inventory;
import GameEngine.Node;
import GameEngine.Square;
import Items.Weapons.Weapon;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public abstract class Unit extends Pane {
    protected int LVL, HP, health, Str, Mag, Skl, Spd, Lck, Def, Res, Mov, Con;
    protected String name;
    protected String unitType;
    protected HashMap<String,Pair<ResizableImage,ResizableImage>> Sprites;
    protected int x, y;
    protected int attack_range = 1;
    private Board board;
    protected boolean isLeader = false;
    protected boolean isSelected = false;
    protected String mode = "standing";
    protected String color = "blue";
    protected boolean isEnemy = false;
    protected ArrayList<Position> availableMoves;
    protected ArrayList<Position> squaresInRange;
    protected ImageView imv;
    protected Weapon wieldedWeapon;
    protected Inventory inventory;
    protected String skin = "";
    protected Transition moveTransition;
    protected boolean inRange = false;
    protected boolean promoted = false;
    protected boolean boss = false;
    protected int xp = 0;
    protected  int classPower = 3;
    Animation spriteAnimation;

    Unit(String name, Map<String, Integer> stats, Inventory inventory) throws IOException {
        this.inventory = inventory;
        wieldedWeapon = inventory.getWeapons().getFirst();
        LVL = stats.get("LVL");
        HP = stats.get("HP");
        Str = stats.get("Str");
        Mag = stats.get("Mag");
        Skl = stats.get("Skl");
        Spd = stats.get("Spd");
        Lck = stats.get("Lck");
        Def = stats.get("Def");
        Res = stats.get("Res");
        Mov = stats.get("Mov");
        Con = stats.get("Con");
        this.name = name;
        health = HP;
        load();
    }
    Unit(String name,String colour, Map<String, Integer> stats, Inventory inventory) throws IOException {
        this.color = colour;
        if (colour.equals("red")){
            isEnemy = true;
        }
        this.inventory = inventory;
        wieldedWeapon = inventory.getWeapons().getFirst();
        LVL = stats.get("LVL");
        HP = stats.get("HP");
        Str = stats.get("Str");
        Mag = stats.get("Mag");
        Skl = stats.get("Skl");
        Spd = stats.get("Spd");
        Lck = stats.get("Lck");
        Def = stats.get("Def");
        Res = stats.get("Res");
        Mov = stats.get("Mov");
        Con = stats.get("Con");
        this.name = name;
        health = HP;
        load();
    }
    Unit(String name, Map<String, Integer> stats, Weapon weapon) throws IOException {
        wieldedWeapon = weapon;
        LVL = stats.get("LVL");
        HP = stats.get("HP");
        Str = stats.get("Str");
        Mag = stats.get("Mag");
        Skl = stats.get("Skl");
        Spd = stats.get("Spd");
        Lck = stats.get("Lck");
        Def = stats.get("Def");
        Res = stats.get("Res");
        Mov = stats.get("Mov");
        Con = stats.get("Con");
        this.name = name;
        health = HP;
        load();
    }
    Unit(String name, String colour, Map<String, Integer> stats, Weapon weapon) throws IOException {
        this.color = colour;
        if (colour.equals("red")) isEnemy = true;
        wieldedWeapon = weapon;
        LVL = stats.get("LVL");
        HP = stats.get("HP");
        Str = stats.get("Str");
        Mag = stats.get("Mag");
        Skl = stats.get("Skl");
        Spd = stats.get("Spd");
        Lck = stats.get("Lck");
        Def = stats.get("Def");
        Res = stats.get("Res");
        Mov = stats.get("Mov");
        Con = stats.get("Con");
        this.name = name;
        health = HP;
        load();
    }
    public boolean addXP(Unit enemy, boolean hit, boolean kill){
        int modeDivisor = 2;
        if (hit) xp+=1;
        else {
            xp += Math.max(0,(int) Math.ceil((31 + enemy.getLVL() + (enemy.isPromoted()?20:0)-LVL - (promoted?20:0))/(double)classPower));
            if(kill){
                // replace false by enemy instanceof Bishop , valkyrie, Rogue, Assassin
                int enemyClassBonus = false?40:enemy.isPromoted()?60:0;
                int allyClassBonus = false?40:promoted?60:0;
                // add thief bonus (enemy instanceof Rogue, Thief, Assassin)?20:0
                xp+= Math.max(0,(int)Math.ceil(20+(enemy.isBoss()?40:0)+(enemy.getLVL()*enemy.getClassPower() + enemyClassBonus) - ((double)LVL*classPower +allyClassBonus)/modeDivisor));
            }
        }
        if (xp >100){
            xp = xp%100;
            return true;
        }
        return false;
    }
    public abstract void load() throws IOException;
    public abstract ImageView getPortrait();

    //getters for stats
    public int getLVL(){
        return LVL;
    }

    public int getXP() {
        return xp;
    }
    public int getHP() {
        return HP;
    }
    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getHealth(){return health;}
    public void setHealth(int health){
        this.health = health;
    }
    public int getStr(){
        return Str;
    }
    public int getMag() {
        return Mag;
    }
    public int getSkl() {
        return Skl;
    }
    public int getSpd() {
        return Spd;
    }
    public int getLck() {
        return Lck;
    }
    public int getDef() {
        return Def;
    }
    public int getRes() {
        return Res;
    }
    public int getCon() {
        return Con;
    }

    public String getColor() {
        return color;
    }

    public String getSkin() {
        return skin;
    }
    public Image getSprites(){
        if (mode.equals(STANDING)){
            return Sprites.get(color).getKey();
        }
        return Sprites.get(color).getValue();
    }
    public Inventory getInventory(){
        return inventory;
    }
    public void setWieldedWeapon(Weapon weapon){this.wieldedWeapon = weapon;}
    public Weapon getWieldedWeapon() {
        return wieldedWeapon;
    }
    public boolean isPromoted(){return promoted;}
    public boolean isBoss(){return boss;}
    public void setBoss(boolean boss) {
        this.boss = boss;
    }
    public int getClassPower(){
        return classPower;
    }
    public void equipWeapon(Weapon weapon){
        wieldedWeapon = weapon;
    }
    public ImageView getImv(){
        return imv;
    }

    public boolean isInRange() {
        return inRange;
    }
    public void setInRange(boolean b) {
        inRange = b;
    }

    public void setImv(ImageView imv){
        this.imv = imv;
    }
    public int animation(int animFrame, boolean select, boolean moving) {
        if (select) return ((animFrame >= 20) ? 1 : 0) + ((animFrame >= 24) ? 1 : 0) - ((animFrame >= 44) ? 1 : 0);
        if (moving) return ((animFrame >= 12) ? 1 : 0) + ((animFrame >= 18) ? 1 : 0) + ((animFrame >= 30) ? 1 : 0);
        else return ((animFrame >= 32) ? 1 : 0) + ((animFrame >= 36) ? 1 : 0) - ((animFrame >= 68) ? 1 : 0);
    }
    public String getResourceDirectory(){
        return getBaseResourceDirectory()+"Battle Animations/"+getSkin()+"/"+getWieldedWeapon().getType();
    }
    protected abstract String getBaseResourceDirectory();
    public int animation(int animFrame){
        if (mode.equals("standing")){
            return animation((2*animFrame)%72, false,false);
        }
        else if (mode.equals("select")){
            return animation((2*animFrame)%48,true,false);
        }
        else return animation((2*animFrame)%36,false,true);
    }

     public static class Dijkstra{
        private static final int[] moveRow = {-1, 1, 0, 0}, moveCol = {0,0,-1,1};
        public static Pair<ArrayList<Position>, ArrayList<Position>> findMoves(Board board, int i, int j, int mov, String unitType){
            int width = board.getWidth();
            int height = board.getHeight();

            final PriorityQueue<Unit.Node> queue = new PriorityQueue<>(Comparator.comparingInt(a->a.distance));
            int[][] distances = new int[height][width];
            for (int[] row : distances) Arrays.fill(row, Integer.MAX_VALUE);

            distances[i][j] = 0;
            queue.offer(new Unit.Node(j,i,0));
            ArrayList<Position> availableMoves = new ArrayList<>(), tilesInRange = new ArrayList<>(), occupiedTiles = new ArrayList<>();
            Unit.Node prev = null;

            while (!queue.isEmpty()){
                Unit.Node cur = queue.poll();
                int x = cur.getFirst(), y = cur.getLast(), distance = cur.distance;
                if (prev != null && board.get(y,x).getUnit() != null) occupiedTiles.add(new Position(x,y));

                if (distance > mov) {
                    break;
                }

                availableMoves.add(new Position(x,y));

                for (int k = 0; k < 4; k++) {
                    int newX = x + moveRow[k], newY = y + moveCol[k];
                    if (newX >= 0 && newY >= 0 && newX < width && newY < height){
                        if (board.get(newY,newX).getTerrain().getMovPenalty(unitType) < 0) continue;
                        int newDistance = distance + board.get(newY,newX).getTerrain().getMovPenalty(unitType);
                        if (newDistance < distances[newY][newX]){
                            distances[newY][newX] = newDistance;
                            queue.offer(new Unit.Node(newX,newY,newDistance));
                        }
                    }
                    
                }
                prev = cur;
            }
            for (Position pos : occupiedTiles){
                availableMoves.remove(pos);
            }
            findTilesInRange(availableMoves,tilesInRange,1,width,height);
            return new Pair<>(availableMoves,tilesInRange);
        }
        private static void findTilesInRange(ArrayList<Position> availableMoves, ArrayList<Position> tilesInRange, int range, int w, int h){
            Queue<Position> queue = new LinkedList<>();

            boolean[][] visitedNodes = new boolean[h][w];
            for (Position position : availableMoves) {
                queue.offer(position);
                visitedNodes[position.getLast()][position.getFirst()] = false;
            }
            int distance = 0;
            while (!queue.isEmpty() && distance < range){
                int levelSize = queue.size();
                for (int i = 0; i < levelSize; i++) {
                    Position cur = queue.poll();
                    assert cur != null;
                    for (int j = 0; j < 4; j++){
                        int x = cur.getFirst() + moveRow[j];
                        int y = cur.getLast() + moveCol[j];
                        if (x>=0 && y>=0 && x<w && y<h && !visitedNodes[y][x]){
                            Position next = new Position(x,y);
                            visitedNodes[y][x] = true;

                            queue.offer(next);
                            if (!availableMoves.contains(next)) tilesInRange.add(next);
                        }
                    }
                }
                distance++;

            }

        }
         public static ArrayList<Position> findPath(Position start, Position end, Board board, String unitType) {
             if (start.equals(end)) return null;  // No path if start and end are the same

             final int width = board.getWidth();
             final int height = board.getHeight();

             // Priority queue for Dijkstra's algorithm (sorted by distance)
             final PriorityQueue<Unit.Node> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.distance));

             // Distance array to track shortest distance from start to each position
             int[][] distances = new int[height][width];
             for (int[] row : distances) Arrays.fill(row, Integer.MAX_VALUE);

             // Predecessor array to track the predecessor for each position using a 1D index
             int[] predecessor = new int[width * height];
             Arrays.fill(predecessor, -1);  // -1 indicates no predecessor (unvisited)

             // Map (x, y) to a single index in 1D array: index = y * width + x
             int startIndex = start.getLast() * width + start.getFirst();
             int endIndex = end.getLast() * width + end.getFirst();

             // Initialize distance for the start position
             distances[start.getLast()][start.getFirst()] = 0;
             queue.offer(new Unit.Node(start.getFirst(), start.getLast(), 0));

             while (!queue.isEmpty()) {
                 Unit.Node cur = queue.poll();
                 int x = cur.getFirst(), y = cur.getLast(), distance = cur.distance;

                 // If we've reached the end node, break the loop
                 if (cur.getFirst() == end.getFirst() && cur.getLast() == end.getLast()) break;

                 // Explore the 4 neighboring tiles
                 for (int i = 0; i < 4; i++) {
                     int newX = x + moveRow[i];
                     int newY = y + moveCol[i];

                     // Check if the new position is within the grid and is traversable
                     if (newX >= 0 && newY >= 0 && newX < width && newY < height) {
                         if (board.get(newY, newX).getTerrain().getMovPenalty(unitType) < 0) continue;  // Skip if unreachable

                         int newDistance = distance + board.get(newY, newX).getTerrain().getMovPenalty(unitType);

                         // Relaxation step: update if a shorter path is found
                         if (newDistance < distances[newY][newX]) {
                             distances[newY][newX] = newDistance;
                             queue.offer(new Unit.Node(newX, newY, newDistance));

                             // Store the predecessor using 1D indexing
                             int newIndex = newY * width + newX;
                             predecessor[newIndex] = y * width + x;  // Store where we came from
                         }
                     }
                 }
             }

             // Now we backtrack from the end node to start to build the path
             ArrayList<Position> path = new ArrayList<>();
             int step = endIndex;

             // If the end node has no predecessor, that means no path was found
             if (predecessor[endIndex] == -1) return null;

             // Backtrack from end to start
             while (step != startIndex) {
                 int stepX = step % width;  // Convert 1D index back to 2D coordinates
                 int stepY = step / width;
                 path.add(new Position(stepX, stepY));
                 step = predecessor[step];  // Move to the predecessor node
             }

             // Add the start node
             path.add(start);

             // Reverse the path since we added it from end to start
             Collections.reverse(path);

             return path;
         }
    }
    public static class Position {
        private final short x;
        private final short y;
        Position(short x, short y){
            this.x = x;
            this.y = y;
        }
        public Position(int x, int y){
            this.x = (short) x;
            this.y = (short) y;
        }
        public short getFirst(){
            return x;
        }
        public short getLast(){
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Position && ((Position) obj).x == this.x && ((Position) obj).y == this.y;
        }

        @Override
        public String toString() {
            return "{ "+x+" ; "+y+" }";
        }
    }
    private static class Node extends Position{
        private int distance;
        Node(int x, int y, int distance){
            super(x,y);
            this.distance = distance;
        }

        public Node(short x, short y) {
            super(x,y);
        }
    }
    public void findMoves (){
        Pair<ArrayList<Position>,ArrayList<Position>> reachableTiles = Dijkstra.findMoves(board,y,x,Mov,unitType);
        availableMoves = reachableTiles.getKey();
        squaresInRange = reachableTiles.getValue();

    }
    public Unit select(){
        if (this instanceof Lyn_Lord) mode = Unit.DOWN;
        else mode = Unit.RIGHT;

        isSelected = true;
        findMoves();
        for (Position pos : availableMoves) {
            board.get(pos).reach(true);
        }
        for (Position pos : squaresInRange) {
            board.get(pos).range(true);
        }
        return this;
    }
    public void unselect(String mode) {
        this.mode = mode;
        if (mode.equals("standing")) {
            isSelected = false;
        }
    }
    public void endMove(){
        for (Position pos : availableMoves) {
            board.get(pos).reach(true);
        }
        for (Position pos : squaresInRange) {
            Square square = board.get(pos);
            if (distanceTo(square)>attack_range ||(square.getXValue() == this.x && square.getYValue() == this.y))  {
                square.range(false);
            } else if (square.getUnit() == null || (square.getUnit() != null && !square.getUnit().getColor().equals("red"))) {
                square.range(false);

            } else {
                inRange = true;
            }
        }
    }
    private int distanceTo(Square square){
        return Math.abs(square.getXValue() - this.getXValue()) + Math.abs(square.getYValue() - this.getYValue());
    }
    public void setBoard(Board board){
        this.board = board;
    }
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getXValue(){
        return x;
    }
    public int getYValue(){
        return y;
    }
    public Position getPosition(){
        return new Position(x,y);
    }
    public Transition getMoveTransition(){
        return moveTransition;
    }
    public ArrayList<Unit> getAdjacentEnemies(){
        ArrayList<Unit> AdjacentUnits = new ArrayList<>();
        if (y>0 && board.get(y-1,x).getUnit() != null && board.get(y-1,x).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y-1,x).getUnit());
        if (y< board.getHeight() - 1 && board.get(y+1,x).getUnit() != null && board.get(y+1,x).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y+1,x).getUnit());
        if (x>0&&board.get(y,x-1).getUnit() != null&& board.get(y,x-1).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y,x-1).getUnit());
        if (x<board.getWidth()-1&&board.get(y,x+1).getUnit() != null&& board.get(y,x+1).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y,x+1).getUnit());
        return AdjacentUnits;
    }
    public void setLeader(boolean isLeader){
        this.isLeader = isLeader;

    }
    public boolean isLeader(){
        return isLeader;
    }
    public String getMode() {return mode;}

    public void setMode(String mode){
        this.mode = mode;
    }

    public void makeMove(int row, int col, double width, double height, ArrayList<Position> path) {
        if (!(row == x && col == y) && availableMoves.contains(new Position(col,row))) {
            board.removeUnit(this,y,x);
            Unit unit = this;
            moveTransition = new Transition() {
                private final int len = path.size() - 1;
                double time;
                {
                    setCycleDuration(Duration.seconds((double) len/8));
                    setCycleCount(1);
                    setInterpolator(Interpolator.LINEAR);
                    time = System.nanoTime();
                }
                @Override
                protected void interpolate(double k) {
                    if (System.nanoTime()-time> (double) 1000000000/60) {
                        time = System.nanoTime();
                        int index = Math.min((int) (k * len), len - 1);
                        double t = k * len - index;

                        int deltaX = path.get(index + 1).getFirst() - path.get(index).getFirst();
                        int deltaY = path.get(index + 1).getLast() - path.get(index).getLast();
                        if (deltaX>0){
                            unit.setMode(RIGHT);
                        } else if (deltaX<0) {
                            unit.setMode(LEFT);
                        } else if (deltaY>0) {
                            unit.setMode(DOWN);
                        } else if (deltaY<0) {
                            unit.setMode(UP);
                        }
                        imv.setTranslateX((path.get(index).getFirst() + t * deltaX) * width - width / 2);
                        imv.setTranslateY((path.get(index).getLast() + t * deltaY) * height - height);
                    }

                }
            };
            moveTransition.play();

        }
    }
    public ArrayList<Position> getAvailableMoves(){return availableMoves;}

    public String getUnitType() {
        return unitType;
    }


    public Animation getSpriteAnimation(){
        spriteAnimation = new SpriteAnimation(this, imv){{setCycleCount(INDEFINITE);}};
        return spriteAnimation;
    }
    public void die(){
        Battle.getSprites().getChildren().remove(spriteAnimation);
        spriteAnimation.stop();
        imv.setEffect(new ColorAdjust(){{
            setBrightness(1);
        }});
        new Transition(){
            {
                setCycleCount(1);
                setCycleDuration(Duration.seconds(1));
                setOnFinished(event->board.removeUnit(Unit.this,y,x));
                play();
            }
            @Override
            protected void interpolate(double v) {
                Unit.this.imv.setOpacity(1-v);
            }};

    }
    @Override
    public String toString() {
        return name;
    }
    public ArrayList<Integer> BFS_Algorithm(int end){
        int start = y* board.getWidth()+x;
        Queue<GameEngine.Node> queue = new LinkedList<>();
        boolean[] nodes = new boolean[board.getHeight()*board.getWidth()];
        queue.add(new GameEngine.Node(board,start/board.getWidth(), start%board.getWidth()));
        nodes[start] = true;
        GameEngine.Node n = new GameEngine.Node(board,board.getHeight(),board.getWidth());
        GameEngine.Node node;
        while (!queue.isEmpty()){
            n = queue.poll();
            int i = n.getRow();
            int j = n.getCol();
            if (i>0){
                if (board.get(i-1,j).getTerrain().getMovPenalty(unitType)>=0 && !nodes[(i-1)* board.getWidth()+j] && availableMoves.contains(board.get(i-1,j))){
                    nodes[(i-1)* board.getWidth()+j] = true;
                    node = new GameEngine.Node(board,i-1,j);
                    node.setParent(n);
                    queue.add(node);
                }
            }
            if (j>0){
                if (board.get(i,j-1).getTerrain().getMovPenalty(unitType)>=0 && !nodes[i* board.getWidth()+j-1] && availableMoves.contains(board.get(i,j-1))){
                    nodes[i* board.getWidth()+j] = true;
                    node = new GameEngine.Node(board,i,j-1);
                    node.setParent(n);
                    queue.add(node);
                }
            }
            if (i+1<board.getHeight()){
                if (board.get(i+1,j).getTerrain().getMovPenalty(unitType)>=0 && !nodes[(i+1)* board.getWidth()+j] && availableMoves.contains(board.get(i+1,j))){
                    nodes[(i+1)* board.getWidth()+j] = true;
                    node = new GameEngine.Node(board,i+1,j);
                    node.setParent(n);
                    queue.add(node);
                }
            }
            if (j+1<board.getWidth()){
                if (board.get(i,j+1).getTerrain().getMovPenalty(unitType)>=0 && !nodes[i* board.getWidth()+j+1] && availableMoves.contains(board.get(i,j+1))){
                    nodes[i* board.getWidth()+j+1] = true;
                    node = new GameEngine.Node(board,i,j+1);
                    node.setParent(n);
                    queue.add(node);
                }
            }
            if (n.getRow()*board.getWidth()+ n.getCol() == end) {
                break;
            }
        }
        ArrayList<Integer> path = new ArrayList<>();
        while (n.getParent() != null){
            path.add(n.getSquareIndex());
            n = n.getParent();
        }
        path.add(start);
        path = new ArrayList<>(path.reversed());
        return path;

    }
    /*
     public ArrayList<Square> dijkstraAlgorithm(int start, int end) {
         boolean finished = false;
         ArrayList<Boolean> visitedNodes = new ArrayList<>(Collections.nCopies(board.getHeight()*board.getWidth(),false));
         ArrayList<Integer> backTrackList = new ArrayList<>(Collections.nCopies(board.getHeight()*board.getWidth(),0));
         int x = start% board.getWidth();
         int y = start % board.getHeight();
         while (!finished){
             if (x + 1 < board.getWidth()){

             }
         }
     return null;
     }
    */
    public void endTurn(){
        color = "gray";
    }
    public void newTurn(){
        imv.setEffect(null);
    }

    public static final String RIGHT = "movRight";
    public static final String LEFT = "movLeft";
    public static final String UP = "movUp";
    public static final String DOWN = "movDown";
    public static final String STANDING = "standing";
    public static final String SELECT = "select";
}
