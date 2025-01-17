package core.game_engine.save;

import core.Board;
import units.Bandit;
import units.Cavalier;
import units.Lyn_Lord;
import units.Unit;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Stream;

public class LoadSaveBoard {
    String chapter = "CH1";
    public LoadSaveBoard(String chapter){
        this.chapter = chapter;
    }
    public void loadBoard(Board board) throws IOException {
        File savefile = new File("MapGeneration/Units/"+chapter+".unit");
        Scanner scanner = new Scanner(savefile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(":");
            int[] unitStats = null;
            if (!line[2].isBlank()) unitStats = Stream.of(line[2].split(",")).mapToInt(Integer::parseInt).toArray();
            Unit unit;
            String colour = switch (line[3]) {
                case "b" -> "blue";
                case "g" -> "green";
                default -> "red";
            };
            if (unitStats == null) unit = loadUnit(line, colour);
            else unit = loadUnit(line, unitStats);
            if (line[6].equals("l")) {
                assert unit != null;
                unit.setLeader(true);
            }
            board.setUnit(unit,(short)Integer.parseInt(line[4]),(short)Integer.parseInt(line[5]));
        }
    }

    private Unit loadUnit(String[] line,String colour) throws IOException {
        return switch (line[0]){
            case "Lyn_Lord" -> new Lyn_Lord(line[1],colour);
            case "Cavalier" -> new Cavalier(line[1],colour);
            case "Bandit" -> new Bandit(line[1],colour);
            default -> throw new RuntimeException();
        };
    }
    private Unit loadUnit(String[] line, int[] unitStats){
        return null;
    }

    public static void main(String[] args) throws Exception {

        //new LoadSave("CH1").loadBoard();
    }
}

