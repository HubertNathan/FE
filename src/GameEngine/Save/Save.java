package GameEngine.Save;

import java.io.*;

public class Save {
    public static int loadTime(int i) throws IOException {
        File saveFile = new File("Resources/Saves/save"+i+".sav");
        if (!saveFile.exists() || saveFile.isDirectory()) {
            return 0;
        }
        BufferedReader reader = new BufferedReader(new FileReader(saveFile));
        String line = "";
        while (line != null){
            if (line.contains("Time")){
                reader.close();
                return Integer.parseInt(line.split(":")[1]);
            }
            line = reader.readLine();
        }
        reader.close();
        return 0;

    }
    public static boolean exists(int i){
        File saveFile = new File("Resources/Saves/save"+i+".sav");
        return saveFile.exists()&&saveFile.isDirectory();
    }

    public static String loadChapter(int i) {
        File saveFile = new File(Save.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"saveFile");
        if (!saveFile.exists() || saveFile.isDirectory()) {
            return "";
        }
        return "ok";
    }
    public Save(int i){
        File saveFile = new File("Resources/Saves/save"+i+".sav");
        if (!saveFile.exists() || saveFile.isDirectory()) {
            createFile(saveFile);
        }

    }
    public static void createFile(int i){
        File saveFile = new File("Resources/Saves/save"+i+".sav");
        createFile(saveFile);
    }
    private static void createFile(File file){
        try {
            if (file.createNewFile()){
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("#MainInfo");
                writer.newLine();
                writer.write("Name:");
                writer.newLine();
                writer.write("Chapter:Prologue: A Girl From The Plains");
                writer.newLine();
                writer.write("Time:0");
                writer.newLine();
                writer.write("#Roster");
                writer.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

    }
    public static void deleteFile(int i){
        File saveFile = new File("Resources/Saves/save"+i+".sav");
        if (saveFile.delete()) System.out.println("Save "+i+" was deleted");;
    }
    public static void main(String[] args) throws IOException {
        System.out.println(loadTime(1));
    }
}
