package GameEngine;

import GUI.ReadMapFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OverWorldMenu {
    BufferedImage ObjectivesIcon;
    BufferedImage resizedObjectivesIcon;
    BufferedImage TerrainIcon;
    BufferedImage resizedTerrainIcon;
    Board board;
    TextInterpreter terrainTI;
    public OverWorldMenu(Board board) throws IOException {
        this.board = board;
        terrainTI = new TextInterpreter("terrainNumbers");
        load();
        resizeImage(48,48);
    }
    private void load() throws IOException {
        ObjectivesIcon = ImageIO.read(new File("Resources/MenuSprites/Objectives.png"));
        TerrainIcon = ImageIO.read((new File("Resources/MenuSprites/Terrain.png")));
    }
    public void updateTerrainIcon(String def, String avo, String terrainType) throws IOException {
        int width = resizedTerrainIcon.getWidth();
        int height = resizedTerrainIcon.getHeight();
        TerrainIcon = new BufferedImage(48,54,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) TerrainIcon.createGraphics();
        g.drawImage(ImageIO.read((new File("Resources/MenuSprites/Terrain.png"))),0,0,null);
        if (def.length()>1) g.drawImage(terrainTI.convertTerrainNumbersToBImg(def),24,31,null);
        else g.drawImage(terrainTI.convertTerrainNumbersToBImg(def),32,31,null);
        if (avo.length()>1) g.drawImage(terrainTI.convertTerrainNumbersToBImg(avo),24,39,null);
        else g.drawImage(terrainTI.convertTerrainNumbersToBImg(avo),32,39,null);
        BufferedImage txtImg = terrainTI.convertText(terrainType,"");
        g.drawImage(txtImg,(TerrainIcon.getWidth()-txtImg.getWidth()+2)/2,17,null);
        g.dispose();
        resizeTerrainIcon(width,height);
    }
    public void resizeTerrainIcon(int width, int height){
        Image tmp = TerrainIcon.getScaledInstance(width,height,BufferedImage.TYPE_INT_ARGB);
        resizedTerrainIcon = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics g = resizedTerrainIcon.getGraphics();
        g.drawImage(tmp,0,0,null);
        g.dispose();
    }
    public void resizeImage(int newW, int newH){
        Image tmp = ObjectivesIcon.getScaledInstance((87*newW)/16,(40*newH)/16, BufferedImage.TYPE_INT_ARGB);
        resizedObjectivesIcon = new BufferedImage(87*(newW)/16,40*(newH)/16,BufferedImage.TYPE_INT_ARGB);
        Graphics g = resizedObjectivesIcon.getGraphics();
        g.drawImage(tmp,0,0,null);
        g.dispose();
        tmp = TerrainIcon.getScaledInstance((48*newW)/16,(54*newH)/16,BufferedImage.TYPE_INT_ARGB);
        resizedTerrainIcon = new BufferedImage((48*newW)/16,(54*newH)/16,BufferedImage.TYPE_INT_ARGB);
        g = resizedTerrainIcon.getGraphics();
        g.drawImage(tmp,0,0,null);
        g.dispose();
    }
    public void draw(Graphics2D g,int width, int height,int scaleX, int scaleY){
        g.drawImage(resizedObjectivesIcon, scaleX*width-5*scaleX-7*scaleX/16,0,null);
        g.drawImage(resizedTerrainIcon, scaleX*(width-3) ,scaleY*(height-3)-(6*scaleY)/16,null);
    }

    public static void main(String[] args) throws IOException {
        ReadMapFile r = new ReadMapFile("CH1");
        Board b = new Board(r);
        OverWorldMenu menuIcon = new OverWorldMenu(b);
        JFrame frame = new JFrame("test");
        JPanel panel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paintComponents(g);
                Graphics2D g2 = (Graphics2D) g;
            }
        };

        frame.add(panel);

        frame.setSize(b.getWidth()*48,b.getHeight()*48);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.repaint();
        frame.setVisible(true);
    }
}
