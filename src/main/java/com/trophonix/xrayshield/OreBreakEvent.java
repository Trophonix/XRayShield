package com.trophonix.xrayshield;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OreBreakEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private Player player;
  private Location location;
  private Material blockType;

  OreBreakEvent(Player player, Location location, Material blockType) {
    this.player = player;
    this.location = location;
    this.blockType = blockType;
  }

  public Player getPlayer() {
    return player;
  }

  public Location getLocation() {
    return location;
  }

  public Material getBlockType() {
    return blockType;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

}
