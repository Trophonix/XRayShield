package com.trophonix.xrayshield;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class XRayOre {

  static List<XRayOre> ORES = new ArrayList<>();

  private Material blockType;
  private int amount;
  private String timeString;
  private long time; // in seconds

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
