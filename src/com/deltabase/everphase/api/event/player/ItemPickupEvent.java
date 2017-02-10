package com.deltabase.everphase.api.event.player;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.entity.EntityItem;

public class ItemPickupEvent extends Event {

    private EntityItem entityItem;

    public ItemPickupEvent(EntityItem entityItem) {
        this.entityItem = entityItem;
    }

    public EntityItem getEntityItem() {
        return entityItem;
    }
}
