package GameEngine;

public class Node {
    int row;
    int col;
    Board board;
    Node parent;

    public Node(Board board, int row, int col){
        this.board = board;
        this.row = row;
        this.col = col;
    }
    public void setParent(Node node){
        this.parent = node;
    }
    public int getSquareIndex(){
        return row*board.getWidth()+col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    public Node getParent(){
        return parent;
    }
}
