package Units;

import GUI.ReadMapFile;
import GameEngine.Board;
import GameEngine.Node;
import GameEngine.Square;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public abstract class Unit extends JPanel {
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
    protected BufferedImage standingSprites;
    protected BufferedImage selectSprites;
    protected BufferedImage resizedStandingSprites;
    protected BufferedImage resizedSelectSprites;
    protected int x;
    protected int y;
    private Board board;
    protected boolean isLeader = false;
    protected boolean isOnCursor = false;
    protected boolean isSelected = false;
    protected String mode = "standing";
    protected ArrayList<Square> availableMoves;

    Unit(String name, Map<String, Integer> stats) throws IOException {
        this.board = board;
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
        resizedStandingSprites = standingSprites;
        resizedSelectSprites = selectSprites;
        resizeSprites(96, 96);
    }
    public void draw(Graphics2D g, int i, int j, int scaleX, int scaleY, int animFrame) {
        if(scaleX<=0 || scaleY<=0){return;}
        if (mode.equals("standing")) {
            int animStep = animation(animFrame);;
            BufferedImage sprite = resizedStandingSprites.getSubimage(0, animStep * 2*(int) scaleY, (int)scaleX, 2*(int) scaleY);
            g.drawImage(sprite, (int) (scaleX) * j, (int) (scaleY) * (i-1), null);
        }
        else if (mode.equals("select")) {
            int animStep = animation((2*animFrame)%36);
            BufferedImage sprite = resizedSelectSprites.getSubimage(0, (12 + animStep) *2* (int)scaleY, 2*(int)scaleX, 2*(int)scaleY);
            g.drawImage(sprite, (int) (scaleX) * j - (int) (scaleX)/2, (int) (scaleY) * (i - 1), null);
        }
    }

    public void resizeSprites(int newW, int  newH) {
        if(newW<=0 || newH<=0){return;}
        Image tmp1 = standingSprites.getScaledInstance(newW, 3*2*newH, Image.SCALE_SMOOTH);
        resizedStandingSprites= new BufferedImage(newW, 3*2*newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedStandingSprites.createGraphics();
        g2d.drawImage(tmp1, 0, 0, null);
        g2d.dispose();
        Image tmp2 = selectSprites.getScaledInstance(2*newW, 15*2*newH, Image.SCALE_SMOOTH);
        resizedSelectSprites= new BufferedImage(2*newW, 15*2*newH, BufferedImage.TYPE_INT_ARGB);
        g2d = resizedSelectSprites.createGraphics();
        g2d.drawImage(tmp2, 0, 0, null);
        g2d.dispose();
    }
    public abstract void load() throws IOException;
    public int animation(int animFrame) {
        return ((animFrame > 0) ? 1 : 0) + ((animFrame > 2) ? 1 : 0) - ((animFrame > 18) ? 1 : 0) - ((animFrame > 20) ? 1 : 0);
    }

    public ArrayList<Square> findMoves (ArrayList<Square> availableMoves, int mov, int i, int j){
        if (i+1< board.getHeight()) {
            updateAvailableMov(availableMoves, mov, i + 1, j);
        }
        if (j+1<board.getWidth()) {
            updateAvailableMov(availableMoves, mov, i, j + 1);
        }
        if (i>0) {

            updateAvailableMov(availableMoves, mov, i-1, j);
        }
        if (j>0) {
            updateAvailableMov(availableMoves, mov, i, j-1);
        }
        return availableMoves;
    }
    public void findMoves (){
        availableMoves = findMoves(new ArrayList<>(), Mov, y, x);
    }
    private void updateAvailableMov(ArrayList<Square> availableMoves, int mov, int i, int j) {
        Square square = board.get(i, j);
        int movPenalty = square.getTerrain().getMovPenalty(unitType);
        if (movPenalty < 0){
            return;
        }
        int remainingMov = mov -movPenalty;
        if( remainingMov >= 0 && !availableMoves.contains(square)){
            availableMoves.add(square);
        } if (remainingMov > 0) {
            findMoves(availableMoves,remainingMov,i,j);
        }
    }
    public Unit select(){
        mode = "select";
        isSelected = true;
        findMoves();
        for (Square square : availableMoves) {
            if (square.getUnit() == null || (square.getUnit().getXValue()==x &&square.getUnit().getYValue() == y)) square.reach(true);
        }
        return this;
    }
    public void unselect(){
        mode = "standing";
        isSelected = false;
        for (Square square : availableMoves) {
            square.reach(false);
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
    public void setMode(String mode){
        this.mode = mode;
    }

    public void makeMove(int row, int col) throws InterruptedException {
            board.removeUnit(this,y,x);
            board.setUnit(this,row,col);
    }
    public ArrayList<Square> getAvailableMoves(){return availableMoves;}

    public abstract Unit copy() throws IOException;
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
        Node node = new Node(board,board.getHeight(),board.getWidth());
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
        return path;

    }
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

    public static void main(String[] args) throws IOException {
        ReadMapFile map = new ReadMapFile("CH1");
        Board b = new Board(map);
        Unit lyn = new Lyn_Lord();
        b.setUnit(lyn,2,2);
        lyn.findMoves();
        ArrayList<Integer> path = lyn.BFS_Algorithm(0* b.getWidth()+1);
    }
}
