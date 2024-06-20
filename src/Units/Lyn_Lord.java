package Units;


import GUI.Animations.Portrait;
import GUI.ResizableImage;
import GameEngine.Inventory;
import Items.Weapons.Sword;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;

public class Lyn_Lord extends Lord{
    public Lyn_Lord() throws IOException {
        super("Lyn", new Inventory(new Sword(Sword.IronSword){{uses = 39;}}));
        load();
        skin = "/Lyn";
    }
    public void load(){
        Sprites = new HashMap<>(){
            {
                put("blue", new Pair<>(new ResizableImage("file:Resources/Sprites/Lord/Lyn/standingSprites_BLUE.png", 32, 96), new ResizableImage("file:Resources/Sprites/Lord/Lyn/movingSprites_BLUE.png", 32, 480)));
                put("red", new Pair<>(new ResizableImage("file:Resources/Sprites/Lord/Lyn/standingSprites_RED.png", 32, 96), new ResizableImage("file:Resources/Sprites/Lord/Lyn/movingSprites_RED.png", 32, 480)));
                put("green", new Pair<>(new ResizableImage("file:Resources/Sprites/Lord/Lyn/standingSprites_GREEN.png", 32, 96), new ResizableImage("file:Resources/Sprites/Lord/Lyn/movingSprites_GREEN.png", 32, 480)));
                put("gray", new Pair<>(new ResizableImage("file:Resources/Sprites/Lord/Lyn/standingSprites_GRAY.png", 32, 96), new ResizableImage("file:Resources/Sprites/Lord/Lyn/movingSprites_GRAY.png", 32, 480)));
            }};
    }

    @Override
    public ImageView getPortrait() {
        return Portrait.getPortrait(Portrait.LYN_NORMAL);
    }
}

