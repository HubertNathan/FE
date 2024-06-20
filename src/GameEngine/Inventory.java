package GameEngine;

import Items.Item;
import Items.Weapons.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Inventory extends ArrayList<Item> {
    public Inventory(Item... items){
        super(List.of(items));
    }

    public ArrayList<Weapon> getWeapons() {
        ArrayList<Weapon> weaponInventory = new ArrayList<>();
        for (Item item : this){
            if (item instanceof Weapon) weaponInventory.add((Weapon)item);
        }
        return weaponInventory;
    }
}
