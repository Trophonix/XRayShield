package com.trophonix.xrayshield.events;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class OreBreakEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Player player;
  private final Location location;
  private final Material blockType;

  OreBreakEvent(Player player, Location location, Material blockType) {
    this.player = player;
    this.location = location;
    this.blockType = blockType;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

}
