package com.trophonix.xrayshield;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class XRayShield extends JavaPlugin {

  private static XRayShield INSTANCE;

  private List<OreBreakEvent> breakEvents;
  private Map<UUID, Location> playerLastAlerts;

  private XRayListener xRayListener;

  private boolean sendAlertEachVein;
  private boolean sendAlertToOPs;
  private String alertConfig;

  @Override
  public void onEnable() {
    INSTANCE = this;
    xRayListener = new XRayListener();
    getServer().getPluginManager().registerEvents(xRayListener, this);
    breakEvents = new ArrayList<>();
    playerLastAlerts = new HashMap<>();
    if (!getDataFolder().exists()) {
      getDataFolder().mkdirs();
      saveDefaultConfig();
    }
    XRayOre.ORES = new ArrayList<>();
    ConfigurationSection oreSection = getConfig().getConfigurationSection("ores");
    oreSection.getKeys(false).forEach(oreName -> {
      Material blockType = Material.getMaterial(oreName.toUpperCase().replace(' ', '_'));
      if (blockType == null) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[X-Ray Shield] INVALID MATERIAL: " + oreName);
        return;
      }
      String timeString = oreSection.getString(oreName + ".time");
      XRayOre xRayOre = new XRayOre(blockType, oreSection.getInt(oreName + ".amount"), timeString, parseTime(timeString));
      XRayOre.ORES.add(xRayOre);
    });
    getLogger().info("X-Ray Shield loaded " + XRayOre.ORES.size() + " ores:");
    XRayOre.ORES.forEach(ore -> getLogger().info(" - " + ore.getBlockType().name() + " - " + ore.getAmount() + "x - " + ore.getTimeString()));
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
    xRayListener = null;
    breakEvents.clear();
    breakEvents = null;
    playerLastAlerts.clear();
    playerLastAlerts = null;
    XRayOre.ORES.clear();
    XRayOre.ORES = null;
  }

  @Override
  public void reloadConfig() {
    super.reloadConfig();
    sendAlertEachVein = getConfig().getBoolean("sendAlertEachVein", true);
    sendAlertToOPs = getConfig().getBoolean("sendAlertToOPs", false);
    alertConfig = getConfig().getString("lang.alert");
  }

  public void oreBreak(OreBreakEvent event) {
    breakEvents.add(event);
    XRayOre xRayOre = XRayOre.ORES.stream().filter(ore -> ore.getBlockType() == event.getBlockType()).findFirst().orElse(null);
    if (xRayOre == null) return;
    Bukkit.getScheduler().runTaskLater(this, () -> breakEvents.remove(event), xRayOre.getTime() * 20);
    List<OreBreakEvent> events = breakEvents.stream()
            .filter(oreBreakEvent -> oreBreakEvent.getBlockType() == event.getBlockType() &&
                    oreBreakEvent.getPlayer().getUniqueId().equals(event.getPlayer().getUniqueId()))
                    .collect(Collectors.toList());
    if (events.size() >= xRayOre.getAmount()) {
      Location last = playerLastAlerts.get(event.getPlayer().getUniqueId());
      if (last != null && event.getPlayer().getLocation().distance(last) < 5) {
        return;
      }
      String alert = replacePlaceholders(alertConfig, event.getPlayer(), event.getBlockType(), events.size(), xRayOre.getTimeString(), event.getLocation());
      Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("xrayshield.alert") || (sendAlertToOPs && player.isOp()))
              .forEach(player -> player.sendMessage(alert.split("%n")));
      playerLastAlerts.put(event.getPlayer().getUniqueId(), event.getLocation());
      if (!sendAlertEachVein) {
        breakEvents.removeAll(events);
      }
    }
  }

  public static XRayShield get() {
    return INSTANCE;
  }

  static long parseTime(String string) {
    try {
      return Long.parseLong(string);
    } catch (NumberFormatException ignored) {
      if (string.contains("h")) {
        return TimeUnit.HOURS.toSeconds(Long.parseLong(string.replace("h", "")));
      } else if (string.contains("m")) {
        return TimeUnit.MINUTES.toSeconds(Long.parseLong(string.replace("m", "")));
      } else if (string.contains("s")) {
        return Long.parseLong(string.replace("s", ""));
      } else {
        return -1;
      }
    }
  }

  static String locationToString(Location location) {
    return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
  }

  static String replacePlaceholders(String string, Player player, Material blockType, int amount, String time, Location location) {
    return ChatColor.translateAlternateColorCodes('&', string)
            .replace("%player%", player.getName())
            .replace("%ore%", blockType.name().toLowerCase().replace("_", " "))
            .replace("%amount%", Integer.toString(amount))
            .replace("%time%", time)
            .replace("%location%", locationToString(location));
  }

}
