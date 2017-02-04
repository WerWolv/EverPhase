package com.deltabase.everphase.main;

import com.deltabase.everphase.api.event.EventBusSubscriber;
import com.deltabase.everphase.api.event.SubscribeEvent;
import com.deltabase.everphase.api.event.input.ScrollEvent;
import com.deltabase.everphase.api.event.inventory.InventoryItemClickEvent;
import com.deltabase.everphase.api.event.inventory.InventoryItemHoverEvent;
import com.deltabase.everphase.api.event.player.OpenGuiEvent;
import com.deltabase.everphase.api.event.player.PlayerMoveEvent;
import com.deltabase.everphase.item.ItemStack;

@EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        event.getPlayer().addPosition(event.getSpeed().x, event.getSpeed().y, event.getSpeed().z);
    }

    @SubscribeEvent
    public void onPlayerGuiOpenend(OpenGuiEvent event) {
        System.out.println(event.getOpenGui());
    }

    @SubscribeEvent
    public void onScroll(ScrollEvent event) {

        if (Main.getPlayer().getCurrentGui() != null) return;

        Main.getPlayer().setSelectedItem(Main.getPlayer().getSelectedItem() - event.getYScroll());

        if(Main.getPlayer().getSelectedItem() > 8)
            Main.getPlayer().setSelectedItem(0);
        else if(Main.getPlayer().getSelectedItem() < 0)
            Main.getPlayer().setSelectedItem(8);
    }

    @SubscribeEvent
    public void onItemHover(InventoryItemHoverEvent event) {

    }

    @SubscribeEvent
    public void onInventoryItemClick(InventoryItemClickEvent event) {
        if (event.getPressedButton() == InventoryItemClickEvent.MOUSE_BUTTON_LEFT) {
            ItemStack oldHeldItem = event.getInventory().getPickedUpItemStack();

            event.getInventory().setPickedUpItemStack(event.getSlot().getItemStack());
            event.getSlot().setItemStack(oldHeldItem);
        }
    }
}
