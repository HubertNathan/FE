package GameEngine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TextInterpreter {
    String mode;
    BufferedImage symbols;
    HashMap<Character,int[]> TxtToIndex;

    TextInterpreter(String mode) throws IOException {
        this.mode = mode;
        load();
    }
    TextInterpreter() throws IOException {
        new TextInterpreter("normal");
    }
    public void load() throws IOException {
        symbols = ImageIO.read((new File(("Resources/FE7Symbols.png"))));
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
    public BufferedImage convertTerrainNumbersToBImg(String txt) throws IOException {
        char[] txtToChar = txt.toCharArray();
        BufferedImage txtToImg;
        txtToImg = new BufferedImage(8 * txtToChar.length, 7, BufferedImage.TYPE_INT_ARGB);
        Graphics g = txtToImg.createGraphics();
        for (int i = 0; i < txtToChar.length; i++) {
            switch (txtToChar[i]) {
                case '0':
                    g.drawImage(ImageIO.read(new File("Resources/TerrainNumbers/0.png")), 8 * i, 0, null);
                    break;
                case '1':
                    g.drawImage(ImageIO.read(new File("Resources/TerrainNumbers/1.png")), 8 * i, 0, null);
                    break;
                case '2':
                    g.drawImage(ImageIO.read(new File("Resources/TerrainNumbers/2.png")), 8 * i, 0, null);
                    break;
                case '3':
                    g.drawImage(ImageIO.read(new File("Resources/TerrainNumbers/3.png")), 8 * i, 0, null);
                    break;
            }
        }
        g.dispose();
        return txtToImg;
    }
    public BufferedImage convertText(String txt, String colour){
        int colourOffset = switch (colour) {
            case "green" -> 67;
            case "gray" -> 134;
            default -> 0;
        };
        int len = 1;
        char[] txtToChar = txt.toCharArray();
        for (int i = 0; i < txt.length(); i++) {
            if (txtToChar[i] == ' '){
                len+=3;
            } else len+= TxtToIndex.get(txtToChar[i])[2]-1;
        }
        BufferedImage txtToImg = new BufferedImage(len,13,BufferedImage.TYPE_INT_ARGB);
        Graphics g = txtToImg.getGraphics();
        int offset = 0;
        for (int i = 0; i < txt.length(); i++) {
            if (txtToChar[i] == ' '){
                offset+=3;
            } else {
                g.drawImage(symbols.getSubimage(TxtToIndex.get(txtToChar[i])[0], TxtToIndex.get(txtToChar[i])[1] + colourOffset, TxtToIndex.get(txtToChar[i])[2], 13), offset, 0, null);
                offset += TxtToIndex.get(txtToChar[i])[2] - 1;
            }
        }
            g.dispose();
        return txtToImg;
    }
    public BufferedImage convertText(String text1,String text2,String colour1, String colour2) {
        BufferedImage txtToImg1 = convertText(text1, colour1);
        BufferedImage txtToImg2 = convertText(text2, colour2);
        BufferedImage img = new BufferedImage(txtToImg1.getWidth() + txtToImg2.getWidth(), txtToImg1.getHeight() + txtToImg2.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.createGraphics();
        g.drawImage(txtToImg1, 0, 0, null);
        g.drawImage(txtToImg2, txtToImg1.getWidth(), 0, null);
        g.dispose();
        return img;
    }
    public BufferedImage convertNum(String num, String colour){
        char[] numToChar = num.toCharArray();
        int len = 8* numToChar.length;
        BufferedImage numToImg = new BufferedImage(len,10,BufferedImage.TYPE_INT_ARGB);
        Graphics g = numToImg.createGraphics();
        for (int i = 0; i < num.length(); i++) {
            switch (numToChar[i]){
                case '0':
                    g.drawImage(symbols.getSubimage(1,1,8,10),8*i,0,null);
                    break;
                case '1' :
                    g.drawImage(symbols.getSubimage(10,1,8,10),8*i,0,null);
                    break;
                case '2':
                    g.drawImage(symbols.getSubimage(19,1,8,10),8*i,0,null);
                    break;
                case '3' :
                    g.drawImage(symbols.getSubimage(28,1,8,10),8*i,0,null);
                    break;
                case '4':
                    g.drawImage(symbols.getSubimage(37,1,8,10),8*i,0,null);
                    break;
                case '5' :
                    g.drawImage(symbols.getSubimage(46,1,8,10),8*i,0,null);
                    break;
                case '6':
                    g.drawImage(symbols.getSubimage(55,1,8,10),8*i,0,null);
                    break;
                case '7' :
                    g.drawImage(symbols.getSubimage(64,1,8,10),8*i,0,null);
                    break;
                case '8':
                    g.drawImage(symbols.getSubimage(73,1,8,10),8*i,0,null);
                    break;
                case '9' :
                    g.drawImage(symbols.getSubimage(82,1,8,10),8*i,0,null);
                    break;
            }
        }
        g.dispose();
        return numToImg;
    }
}