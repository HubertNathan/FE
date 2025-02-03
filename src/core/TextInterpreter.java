package core;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashMap;

import static Items.Item.icons;
import static core.game_engine.Battle.BattleEngine.tileSize;

public class TextInterpreter {
    private static final Image
            symbolsAsImg = new Image("file:Resources/FE7Symbols.png", 472, 274, false, false),
            numAsImg = new Image("file:Resources/FE7Numbers.png", 144, 64, false, false);
    static double scale = tileSize / 16.;
    private static final HashMap<Character, short[]> TxtToIndex = new HashMap<>() {{
        put('A', new short[]{153, 49, 6});
        put('B', new short[]{162, 49, 6});
        put('C', new short[]{171, 49, 6});
        put('D', new short[]{180, 49, 6});
        put('E', new short[]{189, 49, 6});
        put('F', new short[]{198, 49, 6});
        put('G', new short[]{207, 49, 6});
        put('H', new short[]{216, 49, 6});
        put('I', new short[]{225, 49, 5});
        put('J', new short[]{234, 49, 6});
        put('K', new short[]{243, 49, 6});
        put('L', new short[]{252, 49, 5});
        put('M', new short[]{261, 49, 7});
        put('N', new short[]{270, 49, 6});
        put('O', new short[]{279, 49, 6});
        put('P', new short[]{153, 63, 6});
        put('Q', new short[]{162, 63, 7});
        put('R', new short[]{171, 63, 6});
        put('S', new short[]{180, 63, 6});
        put('T', new short[]{189, 63, 7});
        put('U', new short[]{198, 63, 6});
        put('V', new short[]{207, 63, 7});
        put('W', new short[]{216, 63, 7});
        put('X', new short[]{225, 63, 7});
        put('Y', new short[]{234, 63, 7});
        put('Z', new short[]{243, 63, 6});
        put('!', new short[]{252, 63, 6});
        put('?', new short[]{261, 63, 8});
        put(',', new short[]{270, 63, 6});
        put('.', new short[]{279, 63, 6});
        put('a', new short[]{153, 77, 7});
        put('b', new short[]{162, 77, 6});
        put('c', new short[]{171, 77, 6});
        put('d', new short[]{180, 77, 6});
        put('e', new short[]{189, 77, 6});
        put('f', new short[]{198, 77, 5});
        put('g', new short[]{207, 77, 6});
        put('h', new short[]{216, 77, 6});
        put('i', new short[]{225, 77, 3});
        put('j', new short[]{234, 77, 5});
        put('k', new short[]{243, 77, 6});
        put('l', new short[]{252, 77, 3});
        put('m', new short[]{261, 77, 7});
        put('n', new short[]{270, 77, 6});
        put('o', new short[]{279, 77, 6});
        put('p', new short[]{153, 91, 6});
        put('q', new short[]{162, 91, 6});
        put('r', new short[]{171, 91, 5});
        put('s', new short[]{180, 91, 6});
        put('t', new short[]{189, 91, 5});
        put('u', new short[]{198, 91, 6});
        put('v', new short[]{207, 91, 7});
        put('w', new short[]{216, 91, 7});
        put('x', new short[]{225, 91, 7});
        put('y', new short[]{234, 91, 6});
        put('z', new short[]{243, 91, 6});
        put(':', new short[]{252, 91, 5});
        put('/', new short[]{261, 91, 6});
        put('&', new short[]{270, 91, 7});
        put('-', new short[]{279, 91, 5});
        put('1', new short[]{153, 104, 8});
        put('2', new short[]{162, 104, 8});
        put('3', new short[]{171, 104, 8});
        put('4', new short[]{180, 104, 8});
        put('5', new short[]{189, 104, 8});
        put('6', new short[]{198, 104, 8});
        put('7', new short[]{207, 104, 8});
        put('8', new short[]{216, 104, 8});
        put('9', new short[]{225, 104, 8});
        put('0', new short[]{234, 104, 8});
    }};
    private static final HashMap<Character, Image> NumToImg = new HashMap<>() {{
        for (byte i = 48; i < 58; i++) {
            put((char) i, new Image("file:Resources/TerrainNumbers/" + (i - 48) + ".png", 6 * 8, 6 * 7, false, false));
        }
    }};;

    public static Image convertTNumToImg(String txt) {
        char[] txtToChar = txt.toCharArray();
        Canvas canvas = new Canvas(6*8*txt.length(),6*7);
        GraphicsContext g = canvas.getGraphicsContext2D();
        for (byte i = 0; i < txtToChar.length; i++) {
            g.drawImage(NumToImg.get(txtToChar[i]),i*6*8,0);
        }
        return g.getCanvas().snapshot(new SnapshotParameters() {{setFill(Color.TRANSPARENT);}},null);
    }
    public static void convertTxt(String txt,String color, GraphicsContext g, double x, double y) {
        convertTxt(txt,color,g,x,y,false);
    }
    public static void convertTxt(String txt,String color, GraphicsContext g, double x, double y, boolean center) {
        int colourOffset = switch (color) {
            case "green" -> 67;
            case "gray" -> 134;
            default -> 0;
        };
        short len = 1;
        char[] txtToChar = txt.toCharArray();
        for (short i = 0; i < txt.length(); i++) {
            if (txtToChar[i] == ' ') {
                len += 3;
            } else len += TxtToIndex.get(txtToChar[i])[2] - 1;
        }

        g.setImageSmoothing(false);
        int offset = 0;
        for (short i = 0; i < txt.length(); i++) {
            if (txtToChar[i] == ' ') {
                offset += 3;
            } else {
                int yOffset;
                if ("0123456789".indexOf(txtToChar[i]) != -1) yOffset = 1;
                else yOffset = 0;
                g.drawImage(
                        symbolsAsImg,
                        TxtToIndex.get(txtToChar[i])[0], (TxtToIndex.get(txtToChar[i])[1] + colourOffset),
                        TxtToIndex.get(txtToChar[i])[2], 13 - yOffset,
                        scale * offset + x - (center?len*scale/2:0), y,
                        scale * TxtToIndex.get(txtToChar[i])[2], scale * 13 - yOffset*scale);
                offset += TxtToIndex.get(txtToChar[i])[2] - 1;
            }
        }
    }
    public static void convertNum(byte n, GraphicsContext g, double x, double y) throws IOException {
        convertNum(n, g, x, y, false);
    }
    public static void convertNum(byte n, GraphicsContext g, double x, double y, boolean b) throws IOException {
        convertNum(n, "blue", g, x, y, b);
    }
    public static void convertNum(byte n, String color, GraphicsContext g, double x, double y, boolean b) throws IOException {
        int yOffset = switch (color){
            case "green" -> 12;
            case "gray" -> 23;
            default -> 1;
        };
        if (n==100 && b){
            g.setImageSmoothing(false);
            g.drawImage(
                    icons,
                    122,yOffset,
                    17,10,
                    x-17*scale,y,
                    17*scale,10*scale);
            g.getCanvas().snapshot(new SnapshotParameters() {{
                setFill(Color.TRANSPARENT);
            }}, null);
            return;
        }
        int xOffset = 0;
        int len = (int)Math.log10(n)+1;
        for (char c : Integer.toString(n).toCharArray()){
            g.drawImage(
                    icons,
                    Character.getNumericValue(c)*9+1, yOffset,
                    8,10,
                    x+xOffset*scale - len*8*scale,y,
                    8*scale,10*scale
                    );
            //g.drawImage(symbolsAsImg.getSubimage(Character.getNumericValue(c)*9*6+6,yOffset*6,8*6,10*6),xOffset*6,0);
            xOffset+=8;
        }
        g.getCanvas().snapshot(new SnapshotParameters() {{
            setFill(Color.TRANSPARENT);
        }}, null);
    }
    public static Image convertBattleNumbers(int n) throws IOException {
        int len = Integer.toString(n).length();
        char[] intToChar = Integer.toString(n).toCharArray();
        Canvas canvas = new Canvas(len*8*6,8*6);
        int offset = 0;
        for (int i = 0; i < len; i++) {
            //canvas.getGraphicsContext2D().drawImage(symbolsAsImg.getSubimage(6+9*6*Integer.parseInt(Character.toString(intToChar[i])),34*6,8*6,8*6),offset,0);
            offset+=8*6;
        }
        return canvas.snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null);
    }
    public static Image convertTime(long time, String colour) throws IOException {
        int colourOffset = switch (colour){
            default -> 1;
            case "green" -> 10;
            case "gray" -> 19;
        };
        int hour = (int)(time/3600);
        time = Math.max(time - hour * 3600L,0);
        int min = (int)(time/60);
        time = Math.max(time - min * 60L,0);
        int second = (int)(time);
        int l = 1 + (hour>9?1:0) + (hour>99?1:0);
        GraphicsContext g = new Canvas((l+6)*6*9,10*6).getGraphicsContext2D();
        int xOffset = 0;
        for (int i = 0; i < l; i++) {
            int n = (int)((hour%Math.pow(10,l-i))/Math.pow(10,l-1-i));
            //g.drawImage(numAsImg.getSubimage(n*9*6,0,6*9,6*10),xOffset,0);
            xOffset+=8*6;
        }
        //g.drawImage(numAsImg.getSubimage(13*9*6,0,6*9,6*10),xOffset,0);
        xOffset+=8*6;
        //g.drawImage(numAsImg.getSubimage(((min%100)/10*9*6),0,6*9,6*10),xOffset,0);
        xOffset+=8*6;
        //g.drawImage(numAsImg.getSubimage(((min%10)*9*6),0,6*9,6*10),xOffset,0);
        xOffset+=8*6;
        //g.drawImage(numAsImg.getSubimage(14*9*6,0,6*9,6*10),xOffset,0);
        xOffset+=6*8;
        //g.drawImage(numAsImg.getSubimage(((second%100)/10*9*6),3*11*6,6*9,6*8),xOffset,12);
        xOffset+=8*6;
        //g.drawImage(numAsImg.getSubimage(((second%10)*9*6),3*11*6,6*9,6*8),xOffset,12);
        xOffset+=8*6;
        return g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null);
    }

}