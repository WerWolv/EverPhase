package com.werwolv.main;

import com.werwolv.api.event.EventBusSubscriber;
import com.werwolv.api.event.SubscribeEvent;
import com.werwolv.api.event.player.PlayerMoveEvent;
import com.werwolv.api.event.player.OpenGuiEvent;

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

}
