package GUI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;
public class ReadMapFile {
    private final String path1;
    private final String path2;
    private final List<String> map = new ArrayList<>();
    private int[] dimensions;
    private BufferedImage tileSetData;
    private String chapter;
    private final List<String> terrains = new ArrayList<>();
    public ReadMapFile() throws FileNotFoundException {
        path1 = "MapGeneration/Maps/CH1.map";
        path2 = "MapGeneration/TerrainTypes/CH1.ter";
        loadMap();
        loadTerrain();
    }
    public ReadMapFile(String map) throws FileNotFoundException {
        path1 = "MapGeneration/Maps/"+map+".map";
        path2 = "MapGeneration/TerrainTypes/"+map+".ter";
        loadMap();
        loadTerrain();
    }
    private void loadMap() throws FileNotFoundException {
        try {
            File mapFile = new File(path1);
            Scanner mapReader = new Scanner(mapFile);
            chapter = mapReader.nextLine();
            String tileSet = mapReader.nextLine();
            tileSetData = ImageIO.read(new File("MapGeneration/Tilesets/" + tileSet + ".png"));
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
    private void loadTerrain () throws FileNotFoundException {
        File terrainFile = new File(path2);
        Scanner terrainReader = new Scanner(terrainFile);
        while (terrainReader.hasNextLine()){
            terrains.addAll(Arrays.asList(terrainReader.nextLine().split(" ")));
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
    public List<String> getTerrains(){
        return terrains;}

    public static void main(String[] args) throws FileNotFoundException {
        ReadMapFile mapFile = new ReadMapFile("CH1");


    }
}
