import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class ReadMapFile {
    private String path = "MapGeneration/Maps/Plains/map.map";
    private final List<String> map = new ArrayList<>();
    private int[] dimensions;
    private String tileSet;
    private BufferedImage tileSetData;
    ReadMapFile() throws FileNotFoundException {
        path = "MapGeneration/Maps/Plains/map.map";
    }
    ReadMapFile(String map) throws FileNotFoundException {
        path = "MapGeneration/Maps/Plains/"+map;
    }
    public void load() throws FileNotFoundException {
        try {
            File mapFile = new File(path);
            Scanner mapReader = new Scanner(mapFile);
            tileSet = mapReader.nextLine();
            System.out.println(tileSet);
            tileSetData = ImageIO.read(new File("MapGeneration/Tilesets/"+tileSet+".png"));
            dimensions = Stream.of(mapReader.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            while (mapReader.hasNextLine()) {
                map.addAll(Arrays.asList(mapReader.nextLine().split(" ")));
            }
            System.out.println(map);
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

    public static void main(String[] args) throws FileNotFoundException {
        ReadMapFile mapFile = new ReadMapFile("map.map");
        mapFile.load();


    }
}
