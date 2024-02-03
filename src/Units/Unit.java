package Units;

import GameEngine.Board;
import GameEngine.Square;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

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
    protected int animState;
    protected int animStep;
    protected String name;
    protected String unitType;
    protected BufferedImage standingSprites;
    protected BufferedImage selectSprites;
    protected int x;
    protected int y;
    private Board board;
    protected boolean isLeader = false;
    protected boolean isOnCursor = false;
    protected boolean isSelected = false;

    Unit(String name, Map<String, Integer> stats) {
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
        animState = 0;
        animStep = 0;
    }
    public BufferedImage resizeImage(int newH, int newW,String mode) {
        Image tmp = getSprite(mode).getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    public abstract BufferedImage getSprite(String mode);

    public abstract void draw(Graphics2D g,int i, int j,float scaleX, float scaleY, String mode);

    public void animation() {
        animStep = (animStep+1)%36;
        if (animStep == 1|| animStep ==3) {
            animState = (animState + 1);
        } else if (animStep == 19 || animStep == 21 ) {
            animState = (animState - 1);
        }
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
    public ArrayList<Square> findMoves (){return findMoves(new ArrayList<>(), Mov, y, x);}
    private void updateAvailableMov(ArrayList<Square> availableMoves, int mov, int i, int j) {
        Square square = board.get(i, j);
        int movPenalty = square.getTerrain().getMovPenalty(unitType);
        if (movPenalty < 0){
            return;
        }
        int remainingMov = mov -movPenalty;
        if( remainingMov >= 0 && !availableMoves.contains(square) && (square.getUnit() == null || (x==j&&y==i))){
            availableMoves.add(square);
        } if (remainingMov > 0) {
            findMoves(availableMoves,remainingMov,i,j);
        }
    }
    public Unit select(){
        isSelected = true;
        ArrayList<Square> availableMoves = findMoves();
        for (Square square : availableMoves) {
            square.reach(true);
        }
        return this;
    }
    public void unselect(){
        isSelected = false;
        ArrayList<Square> availableMoves = findMoves();
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
    public boolean isOnCursor(){
        return isOnCursor;
    }
    public void setCursored(boolean b){
        isOnCursor = b;
    }
    public boolean isSelected(){
        return isSelected;
    }

    public void makeMove(int row, int col) {
        this.unselect();
        board.get(y,x).setUnit(null);
        board.setUnit(this,row,col);
    }
}
