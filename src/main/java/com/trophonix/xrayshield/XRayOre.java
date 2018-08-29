package com.trophonix.xrayshield;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public class XRayOre {

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

}
