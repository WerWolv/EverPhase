package com.deltabase.everphase.api.crafting;

import com.deltabase.everphase.item.ItemStack;

public class CraftingRecipe {

    private CraftingType craftingType;
    private ItemStack[] itemStacks;
    private ItemStack output;

    public CraftingRecipe(CraftingType craftingType, ItemStack... itemStacks) {
        this.craftingType = craftingType;
        this.itemStacks = itemStacks;
    }

    public CraftingRecipe setCraftingResult(ItemStack craftingResult) {
        this.output = craftingResult;

        return this;
    }

    public CraftingType getCraftingType() {
        return craftingType;
    }

    public ItemStack[] getItems() {
        return itemStacks;
    }

    public ItemStack getOutput() {
        return output;
    }

    public enum CraftingType {
        COMBINE,
        USE
    }
}
