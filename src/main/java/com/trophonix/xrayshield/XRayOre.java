package com.trophonix.xrayshield;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

class XRayOre {

  static List<XRayOre> ORES = new ArrayList<>();

  private final Material blockType;
  private final int amount;
  private final String timeString;
  private final long time; // in seconds

  XRayOre(Material blockType, int amount, String timeString, long time) {
    this.blockType = blockType;
    this.amount = amount;
    this.timeString = timeString;
    this.time = time;
  }

  public Material getBlockType() {
    return blockType;
  }

  int getAmount() {
    return amount;
  }

  public String getTimeString() {
    return timeString;
  }

  public long getTime() {
    return time;
  }

}
