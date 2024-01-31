import java.io.FileNotFoundException;
import java.util.List;

public class Board {
    int height = 0;
    int width = 0;
    private Square[][] board;
    private int[][] terrainTypes;
    Board() {
        board = new Square[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new Square();
            }
        }
    }
    Board(int height, int width){
        this.height = height;
        this.width = width;
        board = new Square[height][width];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                board[i][j] = new Square();
            }
        }
    }
    Board(int[][] terrainTypes){
        int height = terrainTypes.length;
        int length = terrainTypes[0].length;
        board = new Square[height][length];
        this.terrainTypes = terrainTypes;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                board[i][j] = new Square();
                board[i][j].setTerrainType(terrainTypes[i][j]);
            }
        }
    }
    Board(ReadMapFile mapReader) throws FileNotFoundException {
        mapReader.load();
        int[] dimensions = mapReader.getDimensions();
        height = dimensions[0];
        width = dimensions[1];
        board = new Square[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String terrain = mapReader.getMap().get(width*i+j);
                board[i][j] = new Square(mapReader. getTileSetData().getSubimage(16*(Integer.parseInt(terrain)%32), (Integer.parseInt(terrain)/32)*16,16,16));
            }
        }
    }
    public Square get(int i, int j){
        return board[i][j];
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[][] types = {{0,1},{0,0}};
        Board b = new Board(new ReadMapFile());
    }
}