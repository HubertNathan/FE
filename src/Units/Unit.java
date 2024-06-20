package Units;

import GUI.Animations.ColouredSquaresAnimation;
import GUI.Animations.SpriteAnimation;
import GUI.FireEmblemApp;
import GUI.ResizableImage;
import GameEngine.Board;
import GameEngine.Inventory;
import GameEngine.Node;
import GameEngine.Square;
import Items.Weapons.Weapon;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
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
    protected ArrayList<Square> availableMoves;
    private ArrayList<Square> squaresInRange;
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
            squaresInRange.add(square);
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
    public void unselect(String mode) {
        this.mode = mode;
        if (mode.equals("standing")) {
            isSelected = false;
        }
    }
    public void endMove(){
        for (Square square : availableMoves) {
            square.reach(false);
        }
        for (Square square : squaresInRange) {
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
    public Transition getMoveTransition(){
        return moveTransition;
    }
    public ArrayList<Unit> getAdjacentEnemies(){
        ArrayList<Unit> AdjacentUnits = new ArrayList<>();
        if (y>0 && board.get(y-1,x).getUnit() != null && board.get(y-1,x).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y-1,x).getUnit());
        if (y< board.getHeight() && board.get(y+1,x).getUnit() != null && board.get(y+1,x).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y+1,x).getUnit());
        if (x>0&&board.get(y,x-1).getUnit() != null&& board.get(y,x-1).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y,x-1).getUnit());
        if (x<board.getWidth()&&board.get(y,x+1).getUnit() != null&& board.get(y,x+1).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y,x+1).getUnit());
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

    public void makeMove(int row, int col, double width, double height,ColouredSquaresAnimation c) {
        if (!(row == x && col == y) && availableMoves.contains(board.get(row,col))) {
            board.removeUnit(this,y,x);
            Unit unit = this;
            moveTransition = new Transition() {
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
    public Animation getSpriteAnimation(){
        spriteAnimation = new SpriteAnimation(this, imv){{setCycleCount(INDEFINITE);}};
        return spriteAnimation;
    }
    public void die(){
        FireEmblemApp.getSprites().getChildren().remove(spriteAnimation);
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
