package com.deltabase.everphase.main;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.Log;
import com.deltabase.everphase.api.event.EventBusSubscriber;
import com.deltabase.everphase.api.event.SubscribeEvent;
import com.deltabase.everphase.api.event.advance.AchievementGetEvent;
import com.deltabase.everphase.api.event.input.ScrollEvent;
import com.deltabase.everphase.api.event.inventory.InventoryItemClickEvent;
import com.deltabase.everphase.api.event.inventory.InventoryItemHoverEvent;
import com.deltabase.everphase.api.event.player.OpenGuiEvent;
import com.deltabase.everphase.api.event.player.PlayerMoveEvent;
import com.deltabase.everphase.gui.inventory.GuiInventory;
import com.deltabase.everphase.item.ItemStack;

@EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        event.getPlayer().addPosition(event.getSpeed().x, event.getSpeed().y, event.getSpeed().z);
    }

    @SubscribeEvent
    public void onPlayerGuiOpenend(OpenGuiEvent event) {
    }

    @SubscribeEvent
    public void onScroll(ScrollEvent event) {

        if (EverPhaseApi.getEverPhase().thePlayer.getCurrentGui() != null) return;

        EverPhaseApi.getEverPhase().thePlayer.setSelectedItemIndex(EverPhaseApi.getEverPhase().thePlayer.getSelectedItemIndex() - event.getYScroll());

        if (EverPhaseApi.getEverPhase().thePlayer.getSelectedItemIndex() > 8)
            EverPhaseApi.getEverPhase().thePlayer.setSelectedItemIndex(0);
        else if (EverPhaseApi.getEverPhase().thePlayer.getSelectedItemIndex() < 0)
            EverPhaseApi.getEverPhase().thePlayer.setSelectedItemIndex(8);
    }

    @SubscribeEvent
    public void onItemHover(InventoryItemHoverEvent event) {

    }

    @SubscribeEvent
    public void onInventoryItemClick(InventoryItemClickEvent event) {
        if (EverPhaseApi.getEverPhase().thePlayer.getCurrentGui() instanceof GuiInventory) {
            GuiInventory guiInventory = (GuiInventory) EverPhaseApi.getEverPhase().thePlayer.getCurrentGui();

            if (event.getPressedButton() == InventoryItemClickEvent.MOUSE_BUTTON_LEFT && event.getInventory() == guiInventory.getInventory()) {
                ItemStack oldHeldItem = event.getInventory().getPickedUpItemStack();

                event.getInventory().setPickedUpItemStack(event.getSlot().getItemStack());
                event.getSlot().setItemStack(oldHeldItem);
            }
        }
    }

    @SubscribeEvent
    public void onAchievementGet(AchievementGetEvent event) {
        Log.i("ABC", event.getAchievement().getName());
    }
}
