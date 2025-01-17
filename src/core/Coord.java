package core;

public class Coord {
    private final short x;
    private final short y;
    Coord(short x, short y){
        this.x = x;
        this.y = y;
    }
    public Coord(int x, int y){
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
        return obj instanceof Coord && ((Coord) obj).x == this.x && ((Coord) obj).y == this.y;
    }

    @Override
    public String toString() {
        return "{ "+x+" ; "+y+" }";
    }
}