package com.trophonix.xrayshield;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.stream.Collectors;

public class XRayListener implements Listener {

  private Map<UUID, List<Location>> blockPlacements = new HashMap<>();

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Material blockType = event.getBlock().getType();
    if (blockType == null) return;
    if (XRayOre.ORES.stream().noneMatch(ore -> ore.getBlockType() == blockType)) return;
    OreBreakEvent oreBreakEvent = new OreBreakEvent(event.getPlayer(), event.getBlock().getLocation(), event.getBlock().getType());
    Bukkit.getPluginManager().callEvent(oreBreakEvent);
  }

  @EventHandler
  public void onOreBreak(OreBreakEvent event) {
    List<Location> placements = blockPlacements.values().stream().<Location>flatMap(List::stream).collect(Collectors.toList());
    if (placements != null && placements.contains(event.getLocation())) return;
    XRayShield.get().oreBreak(event);
  }

  @EventHandler
  public void onPlace(BlockPlaceEvent event) {
    List<Location> placements = blockPlacements.get(event.getPlayer().getUniqueId());
    if (placements == null) placements = new ArrayList<>();
    placements.add(event.getBlock().getLocation());
    blockPlacements.put(event.getPlayer().getUniqueId(), placements);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    blockPlacements.remove(event.getPlayer().getUniqueId());
  }

}
