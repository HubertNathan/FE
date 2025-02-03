package core.game_engine.helper_functions;

public class Coord {
    private final byte x;
    private final byte y;
    public Coord(byte x, byte y){
        this.x = x;
        this.y = y;
    }
    public byte getFirst(){
        return x;
    }
    public byte getLast(){
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