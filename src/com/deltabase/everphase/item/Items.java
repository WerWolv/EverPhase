package com.deltabase.everphase.item;

import com.deltabase.everphase.main.Main;

public class Items {

    public static Item itemTest = new Item("Test", 0, 0, Main.getLoader().loadGuiTexture("items/test")).setMaxStackSize(1);
    public static Item itemTest2 = new Item("Test2", 1, 0, Main.getLoader().loadGuiTexture("items/test1")).setMaxStackSize(1);

}
