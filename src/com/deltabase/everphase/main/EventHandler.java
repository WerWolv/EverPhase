package com.deltabase.everphase.main;

import com.deltabase.everphase.api.event.EventBusSubscriber;
import com.deltabase.everphase.api.event.SubscribeEvent;
import com.deltabase.everphase.api.event.input.ScrollEvent;
import com.deltabase.everphase.api.event.player.OpenGuiEvent;
import com.deltabase.everphase.api.event.player.PlayerMoveEvent;

@EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        event.getPlayer().addPosition(event.getSpeed().x, event.getSpeed().y, event.getSpeed().z);
    }

    @SubscribeEvent
    public void onPlayerGuiOpenend(OpenGuiEvent event) {
        event.getPlayer().setCurrentGui(event.getOpenGui());
        System.out.println(event.getOpenGui());
    }

    @SubscribeEvent
    public void onScroll(ScrollEvent event) {
        Main.getPlayer().setSelectedItem(Main.getPlayer().getSelectedItem() - event.getYScroll());

        if(Main.getPlayer().getSelectedItem() > 8)
            Main.getPlayer().setSelectedItem(0);
        else if(Main.getPlayer().getSelectedItem() < 0)
            Main.getPlayer().setSelectedItem(8);
    }
}
