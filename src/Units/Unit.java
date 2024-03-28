package Units;

import GUI.ResizableImage;
import GameEngine.Board;
import GameEngine.Node;
import GameEngine.Square;
import Weapon.Weapon;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public abstract class Unit extends Pane {
    protected int LVL;
    protected int HP;
    protected int Str;
    protected int Mag;
    protected int Skl;
    protected int Spd;
    protected int Lck;
    protected int Def;
    protected int Res;
    protected int Mov;
    protected int Con;

    protected String name;
    protected String unitType;
    protected ResizableImage standingSprites;
    protected ResizableImage selectSprites;
    protected int x;
    protected int y;
    protected int attack_range = 1;
    private Board board;
    protected boolean isLeader = false;
    protected boolean isSelected = false;
    protected String mode = "standing";
    protected String color = "blue";
    protected ArrayList<Square> availableMoves;
    private ArrayList<Square> squaresInRange;
    protected ImageView imv;
    protected Weapon wieldedWeapon;
    protected String skin = "";

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
        load();
        //resizeSprites(96, 96);
    }
    public abstract void load() throws IOException;

    //getters for stats
    public int getLVL(){
        return LVL;
    }
    public int getHP() {
        return HP;
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
        return mode.equals("standing")?standingSprites:selectSprites;
    }

    public Weapon getWieldedWeapon() {
        return wieldedWeapon;
    }

    public void equipWeapon(Weapon weapon){
        wieldedWeapon = weapon;
    }
    public ImageView getImv(){
        return imv;
    }
    public void setImv(ImageView imv){
        this.imv = imv;
    }
    public int animation(int animFrame, boolean select, boolean moving) {
        if (select) return ((animFrame >= 20) ? 1 : 0) + ((animFrame >= 24) ? 1 : 0) - ((animFrame >= 44) ? 1 : 0);
        if (moving) return ((animFrame >= 12) ? 1 : 0) + ((animFrame >= 18) ? 1 : 0) + ((animFrame >= 30) ? 1 : 0);
        else return ((animFrame >= 32) ? 1 : 0) + ((animFrame >= 36) ? 1 : 0) - ((animFrame >= 68) ? 1 : 0);
    }
    public abstract String getResourceDirectory();
    public int animation(int animFrame){
        if (mode.equals("standing")){
            return animation((2*animFrame)%72, false,false);
        }
        else if (mode.equals("select")){
            return animation((2*animFrame)%48,true,false);
        }
        else return animation((2*animFrame)%36,false,true);
    }

    public Pair<ArrayList<Square>,ArrayList<Square>> findMoves (ArrayList<Square> availableMoves, ArrayList<Square> squaresInRange, int mov, int i, int j){
        if (i+1< board.getHeight()) {
            updateAvailableMov(availableMoves, squaresInRange, mov, i + 1, j, board.get(i,j));
        }
        if (j+1<board.getWidth()) {
            updateAvailableMov(availableMoves, squaresInRange,mov, i, j + 1, board.get(i,j));
        }
        if (i>0) {

            updateAvailableMov(availableMoves, squaresInRange,mov, i-1, j, board.get(i,j));
        }
        if (j>0) {
            updateAvailableMov(availableMoves, squaresInRange,mov, i, j-1, board.get(i,j));
        }
        return new Pair<>(availableMoves,squaresInRange);
    }
    public void findMoves (){
        Pair<ArrayList<Square>,ArrayList<Square>> MOVES = findMoves(new ArrayList<>(),new ArrayList<>(), Mov, y, x);
        availableMoves = MOVES.getKey();
        squaresInRange = MOVES.getValue();
    }
    private void updateAvailableMov(ArrayList<Square> availableMoves, ArrayList<Square> squaresInRange, int mov, int i, int j, Square lastSquare) {
        Square square = board.get(i, j);
        int movPenalty = square.getTerrain().getMovPenalty(unitType);
        if (movPenalty  < 0) return;
        int remainingMov = mov -movPenalty;
        if( remainingMov >= 0 && !availableMoves.contains(square)){
            squaresInRange.remove(square);
            availableMoves.add(square);
        } else if (remainingMov + attack_range*movPenalty >=0 && !squaresInRange.contains(square) && !availableMoves.contains(square) && (lastSquare.getUnit() == this || lastSquare.getUnit() == null)){
            squaresInRange.add(square);
        }
        if (remainingMov +attack_range > 0) {
            findMoves(availableMoves,squaresInRange,remainingMov,i,j);
        }
    }
    public Unit select(){
        if (this instanceof Lyn_Lord) mode = Unit.DOWN;
        else mode = Unit.RIGHT;

        isSelected = true;
        findMoves();
        for (Square square : availableMoves) {
            square.reach(true);
        }
        for (Square square : squaresInRange) {
            square.range(true);
        }
        return this;
    }
    public void unselect(String mode){
        this.mode = mode;
        if (mode.equals("standing")) {
            isSelected = false;
        }
        for (Square square : availableMoves) {
            square.reach(false);
        }
        for (Square square : squaresInRange) {
            square.range(false);
        }
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

    public void makeMove(int row, int col, double width, double height) {
        if (!(row == x && col == y) && availableMoves.contains(board.get(row,col))) {
            board.removeUnit(this,y,x);
            Unit unit = this;
            Transition moveTransition = new Transition() {
                private final ArrayList<Integer> path = BFS_Algorithm(row*board.getWidth()+col);
                private final ArrayList<List<Integer>> vertices =  new ArrayList<>(){{
                    path.forEach(v -> add(Arrays.asList(v%board.getWidth(), v/board.getWidth())));
                }};
                private final int len = vertices.size() - 1;
                double time;
                {
                    setCycleDuration(Duration.seconds((double) len/8));
                    setCycleCount(1);
                    setInterpolator(Interpolator.LINEAR);
                    time = System.nanoTime();
                    setOnFinished(event -> {
                        board.setUnit(unit,row,col);
                        unit.setMode(STANDING);
                        unselect(STANDING);
                    });
                }
                @Override
                protected void interpolate(double k) {
                    if (System.nanoTime()-time> (double) 1000000000/60) {
                        time = System.nanoTime();
                        int index = Math.min((int) (k * len), len - 1);
                        double t = k * len - index;

                        int deltaX = vertices.get(index + 1).getFirst() - vertices.get(index).getFirst();
                        int deltaY = vertices.get(index + 1).getLast() - vertices.get(index).getLast();
                        if (deltaX>0){
                            unit.setMode(RIGHT);
                        } else if (deltaX<0) {
                            unit.setMode(LEFT);
                        } else if (deltaY>0) {
                            unit.setMode(DOWN);
                        } else if (deltaY<0) {
                            unit.setMode(UP);
                        }
                        imv.setTranslateX((vertices.get(index).getFirst() + t * deltaX) * width - width / 2);
                        imv.setTranslateY((vertices.get(index).getLast() + t * deltaY) * height - height);
                    }

                }
            };
            moveTransition.play();

        }
    }
    public ArrayList<Square> getAvailableMoves(){return availableMoves;}

    @Override
    public String toString() {
        return name;
    }
    public ArrayList<Integer> BFS_Algorithm(int end){
        int start = y* board.getWidth()+x;
        Queue<Node> queue = new LinkedList<>();
        boolean[] nodes = new boolean[board.getHeight()*board.getWidth()];
        queue.add(new Node(board,start/board.getWidth(), start%board.getWidth()));
        nodes[start] = true;
        Node n = new Node(board,board.getHeight(),board.getWidth());
        Node node;
        while (!queue.isEmpty()){
            n = queue.poll();
            int i = n.getRow();
            int j = n.getCol();
            if (i>0){
                if (board.get(i-1,j).getTerrain().getMovPenalty(unitType)>=0 && !nodes[(i-1)* board.getWidth()+j] && availableMoves.contains(board.get(i-1,j))){
                    nodes[(i-1)* board.getWidth()+j] = true;
                    node = new Node(board,i-1,j);
                    node.setParent(n);
                    queue.add(node);
                }
            }
            if (j>0){
                if (board.get(i,j-1).getTerrain().getMovPenalty(unitType)>=0 && !nodes[i* board.getWidth()+j-1] && availableMoves.contains(board.get(i,j-1))){
                    nodes[i* board.getWidth()+j] = true;
                    node = new Node(board,i,j-1);
                    node.setParent(n);
                    queue.add(node);
                }
            }
            if (i+1<board.getHeight()){
                if (board.get(i+1,j).getTerrain().getMovPenalty(unitType)>=0 && !nodes[(i+1)* board.getWidth()+j] && availableMoves.contains(board.get(i+1,j))){
                    nodes[(i+1)* board.getWidth()+j] = true;
                    node = new Node(board,i+1,j);
                    node.setParent(n);
                    queue.add(node);
                }
            }
            if (j+1<board.getWidth()){
                if (board.get(i,j+1).getTerrain().getMovPenalty(unitType)>=0 && !nodes[i* board.getWidth()+j+1] && availableMoves.contains(board.get(i,j+1))){
                    nodes[i* board.getWidth()+j+1] = true;
                    node = new Node(board,i,j+1);
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

    public static final String RIGHT = "movRight";
    public static final String LEFT = "movLeft";
    public static final String UP = "movUp";
    public static final String DOWN = "movDown";
    public static final String STANDING = "standing";
    public static final String SELECT = "select";
}
