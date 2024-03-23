package GameEngine;

import GUI.ResizableImage;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TextInterpreter {
    String mode;
    BufferedImage symbols;
    ResizableImage symbolsAsImg;
    HashMap<Character,int[]> TxtToIndex;
    HashMap<Character, Image> NumToImg;

    TextInterpreter(String mode) throws IOException {
        this.mode = mode;
        load();
    }
    public TextInterpreter() throws IOException {
        mode = "normal";
        load();
    }
    public void load() throws IOException {
        symbols = ImageIO.read((new File(("Resources/FE7Symbols.png"))));
        symbolsAsImg = new ResizableImage("file:Resources/FE7Symbols.png",472,274);
        NumToImg = new HashMap<>(){{
            for (int i = 48; i < 58; i++) {
            put((char)(i),new Image("file:Resources/TerrainNumbers/"+(i-48)+".png",6*8,6*7,false,false));
            }
        }};

        TxtToIndex = new HashMap<>();
        TxtToIndex.put('A', new int[]{153, 49, 6});
        TxtToIndex.put('B', new int[]{162, 49, 6});
        TxtToIndex.put('C', new int[]{171, 49, 6});
        TxtToIndex.put('D', new int[]{180, 49, 6});
        TxtToIndex.put('E', new int[]{189, 49, 6});
        TxtToIndex.put('F', new int[]{198, 49, 6});
        TxtToIndex.put('G', new int[]{207, 49, 6});
        TxtToIndex.put('H', new int[]{216, 49, 6});
        TxtToIndex.put('I', new int[]{225, 49, 5});
        TxtToIndex.put('J', new int[]{234, 49, 6});
        TxtToIndex.put('K', new int[]{243, 49, 6});
        TxtToIndex.put('L', new int[]{252, 49, 5});
        TxtToIndex.put('M', new int[]{261, 49, 7});
        TxtToIndex.put('N', new int[]{270, 49, 6});
        TxtToIndex.put('O', new int[]{279, 49, 6});
        TxtToIndex.put('P', new int[]{153, 63, 6});
        TxtToIndex.put('Q', new int[]{162, 63, 7});
        TxtToIndex.put('R', new int[]{171, 63, 6});
        TxtToIndex.put('S', new int[]{180, 63, 6});
        TxtToIndex.put('T', new int[]{189, 63, 7});
        TxtToIndex.put('U', new int[]{198, 63, 6});
        TxtToIndex.put('V', new int[]{207, 63, 7});
        TxtToIndex.put('W', new int[]{216, 63, 7});
        TxtToIndex.put('X', new int[]{225, 63, 7});
        TxtToIndex.put('Y', new int[]{234, 63, 7});
        TxtToIndex.put('Z', new int[]{243, 63, 6});
        TxtToIndex.put('!', new int[]{252, 63, 6});
        TxtToIndex.put('?', new int[]{261, 63, 8});
        TxtToIndex.put(',', new int[]{270, 63, 6});
        TxtToIndex.put('.', new int[]{279, 63, 6});
        TxtToIndex.put('a', new int[]{153, 77, 7});
        TxtToIndex.put('b', new int[]{162, 77, 6});
        TxtToIndex.put('c', new int[]{171, 77, 6});
        TxtToIndex.put('d', new int[]{180, 77, 6});
        TxtToIndex.put('e', new int[]{189, 77, 6});
        TxtToIndex.put('f', new int[]{198, 77, 5});
        TxtToIndex.put('g', new int[]{207, 77, 6});
        TxtToIndex.put('h', new int[]{216, 77, 6});
        TxtToIndex.put('i', new int[]{225, 77, 3});
        TxtToIndex.put('j', new int[]{234, 77, 5});
        TxtToIndex.put('k', new int[]{243, 77, 6});
        TxtToIndex.put('l', new int[]{252, 77, 3});
        TxtToIndex.put('m', new int[]{261, 77, 7});
        TxtToIndex.put('n', new int[]{270, 77, 6});
        TxtToIndex.put('o', new int[]{279, 77, 6});
        TxtToIndex.put('p', new int[]{153, 91, 6});
        TxtToIndex.put('q', new int[]{162, 91, 6});
        TxtToIndex.put('r', new int[]{171, 91, 5});
        TxtToIndex.put('s', new int[]{180, 91, 6});
        TxtToIndex.put('t', new int[]{189, 91, 5});
        TxtToIndex.put('u', new int[]{198, 91, 6});
        TxtToIndex.put('v', new int[]{207, 91, 7});
        TxtToIndex.put('w', new int[]{216, 91, 7});
        TxtToIndex.put('x', new int[]{225, 91, 7});
        TxtToIndex.put('y', new int[]{234, 91, 6});
        TxtToIndex.put('z', new int[]{243, 91, 6});
        TxtToIndex.put(':', new int[]{252, 91, 5});
        TxtToIndex.put('/', new int[]{261, 91, 6});
        TxtToIndex.put('&', new int[]{270, 91, 7});
        TxtToIndex.put('-', new int[]{279, 91, 5});
        TxtToIndex.put('1', new int[]{153, 104, 8});
        TxtToIndex.put('2', new int[]{162, 104, 8});
        TxtToIndex.put('3', new int[]{171, 104, 8});
        TxtToIndex.put('4', new int[]{180, 104, 8});
        TxtToIndex.put('5', new int[]{189, 104, 8});
        TxtToIndex.put('6', new int[]{198, 104, 8});
        TxtToIndex.put('7', new int[]{207, 104, 8});
        TxtToIndex.put('8', new int[]{216, 104, 8});
        TxtToIndex.put('9', new int[]{225, 104, 8});
        TxtToIndex.put('0', new int[]{234, 104, 8});
    }
    public Image convertTNumToImg(String txt) {
        char[] txtToChar = txt.toCharArray();
        Canvas canvas = new Canvas(6*8*txt.length(),6*7);
        GraphicsContext g = canvas.getGraphicsContext2D();
        for (int i = 0; i < txtToChar.length; i++) {
            g.drawImage(NumToImg.get(txtToChar[i]),i*6*8,0);
        }
        return g.getCanvas().snapshot(new SnapshotParameters() {{setFill(Color.TRANSPARENT);}},null);
    }
    public Image convertTxt(String colour, String txt) throws IOException {
        int colourOffset = switch (colour) {
            case "green" -> 67;
            case "gray" -> 134;
            default -> 0;
        };
        int len = 1;
        char[] txtToChar = txt.toCharArray();
        for (int i = 0; i < txt.length(); i++) {
            if (txtToChar[i] == ' ') {
                len += 3;
            } else len += TxtToIndex.get(txtToChar[i])[2] - 1;
        }
        Canvas canvas = new Canvas(len*6,13*6);
        GraphicsContext g = canvas.getGraphicsContext2D();
        for (int i = 0; i < txt.length(); i++) {
            if (txtToChar[i] == ' '){
                len+=3;
            } else len+= TxtToIndex.get(txtToChar[i])[2]-1;
        }
        int offset = 0;
        for (int i = 0; i < txt.length(); i++) {
            if (txtToChar[i] == ' ') {
                offset += 3;
            } else {
                int yOffset;
                if ("0123456789".indexOf(txtToChar[i]) != -1) yOffset = 6;
                else yOffset = 0;
                g.drawImage(symbolsAsImg.getSubimage(6 * TxtToIndex.get(txtToChar[i])[0], 6 * (TxtToIndex.get(txtToChar[i])[1] + colourOffset), 6 * TxtToIndex.get(txtToChar[i])[2], 6 * 13 - yOffset), 6 * offset, 0);
                offset += TxtToIndex.get(txtToChar[i])[2] - 1;
            }
        }
        return g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null);
    }
    public Image convertBattleNumbers(int n) throws IOException {
        int len = Integer.toString(n).length();
        char[] intToChar = Integer.toString(n).toCharArray();
        Canvas canvas = new Canvas(len*8*6,8*6);
        int offset = 0;
        for (int i = 0; i < len; i++) {
            canvas.getGraphicsContext2D().drawImage(symbolsAsImg.getSubimage(6+9*6*Integer.parseInt(Character.toString(intToChar[i])),34*6,8*6,8*6),offset,0);
            offset+=8*6;
        }
        return canvas.snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null);
    }

}