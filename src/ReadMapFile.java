import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class ReadMapFile {
    Map<Integer, String > terrainNames = new HashMap<>(){{
        put(0, "Void");
        put(1, "Arena");
        put(2, "Bridge");
        put(3, "Cliff");
        put(4, "Deck");
        put(5, "Desert");
        put(6, "Door");
        put(7, "Fence");
        put(8, "Floor");
        put(9, "Forest");
        put(10, "Fort");
        put(11, "Gate");
        put(12, "House");
        put(13, "Lake");
        put(14, "Mountain");
        put(15, "Peak");
        put(16, "Pillar");
        put(17, "Plain");
        put(18, "River");
        put(19, "Road");
        put(20, "Ruins");
        put(21, "Ruins_Village");
        put(22, "Sand");
        put(23, "Sea");
        put(24, "Shop");
        put(25, "Snag");
        put(26, "Stairs");
        put(27, "Throne");
        put(28, "Village");
        put(29, "Wall");
        put(30, "Wall_Weak");
    }};
    private String path = "MapGeneration/Maps/Plains/map.map";
    private final List<String> map = new ArrayList<>();
    private int[] dimensions;
    private String tileSet;
    private BufferedImage tileSetData;
    private String chapter;
    ReadMapFile() {
        path = "MapGeneration/Maps/CH1.map";
    }
    ReadMapFile(String map) {
        path = "MapGeneration/Maps/"+map;
    }
    public void load() throws FileNotFoundException {
        try {
            File mapFile = new File(path);
            Scanner mapReader = new Scanner(mapFile);
            chapter = mapReader.nextLine();
            tileSet = mapReader.nextLine();
            tileSetData = ImageIO.read(new File("MapGeneration/Tilesets/"+tileSet+".png"));
            dimensions = Stream.of(mapReader.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            while (mapReader.hasNextLine()) {
                map.addAll(Arrays.asList(mapReader.nextLine().split(" ")));
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getDimensions() {
        return dimensions;
    }

    public List<String> getMap() {
        return map;
    }

    public BufferedImage getTileSetData() {
        return tileSetData;
    }
    public String getChapter(){
        return chapter;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ReadMapFile mapFile = new ReadMapFile("map.map");
        mapFile.load();


    }
}
