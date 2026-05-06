package de.minebench.syncinv;

/*
 * SyncInv
 * Copyright (c) 2021 Max Lee aka Phoenix616 (max@themoep.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class PlayerData implements Serializable {
    @Serial
    private static final long serialVersionUID = -5703536933548893803L;
    private final long timeStamp = System.currentTimeMillis();
    private final int dataVersion = Bukkit.getUnsafe().getDataVersion();
    private final UUID playerId;
    private final String playerName;
    private final GameMode gamemode;
    private final int totalExperience;
    private final int level;
    private final float exp;
    protected final byte @NotNull [] inventory;
    protected final byte @NotNull [] enderchest;
    private final Collection<PotionEffect> potionEffects;
    private final Set<MapData> maps = new HashSet<>();
    private final double maxHealth;
    private final double health;
    private final boolean isHealthScaled;
    private final double healthScale;
    private final int foodLevel;
    private final float saturation;
    private final float exhaustion;
    private final int maxAir;
    private final int remainingAir;
    private final int fireTicks;
    private final int maxNoDamageTicks;
    private final int noDamageTicks;
    private final float fallDistance;
    private final Vector velocity;
    private final int heldItemSlot;
    private final byte @Nullable [] persistentData;
    private final Map<String, Map<String, Long>> advancementProgress = new HashMap<>();
    private final Table<Statistic, String, Integer> statistics =  HashBasedTable.create();
    private final long lastSeen;

    public PlayerData(final @NotNull Player player, final long lastSeen, final byte @Nullable [] persistentData) {
        this.playerId = player.getUniqueId();
        this.playerName = player.getName();
        this.gamemode = player.getGameMode();
        this.totalExperience = player.getTotalExperience();
        this.level = player.getLevel();
        this.exp = player.getExp();
        this.inventory = ItemStack.serializeItemsAsBytes(player.getInventory().getContents());
        this.enderchest = ItemStack.serializeItemsAsBytes(player.getEnderChest().getContents());
        this.potionEffects = player.getActivePotionEffects();
        this.maxHealth = player.getMaxHealth();
        this.health = player.getHealth();
        this.isHealthScaled = player.isHealthScaled();
        this.healthScale = player.getHealthScale();
        this.foodLevel = player.getFoodLevel();
        this.saturation = player.getSaturation();
        this.exhaustion = player.getExhaustion();
        this.maxAir = player.getMaximumAir();
        this.remainingAir = player.getRemainingAir();
        this.fireTicks = player.getFireTicks();
        this.maxNoDamageTicks = player.getMaximumNoDamageTicks();
        this.noDamageTicks = player.getNoDamageTicks();
        this.fallDistance = player.getFallDistance();
        this.velocity = player.getVelocity();
        this.heldItemSlot = player.getInventory().getHeldItemSlot();
        this.persistentData = persistentData;
        this.lastSeen = lastSeen;
    }

    public ItemStack[] getInventoryContents() {
        return ItemStack.deserializeItemsFromBytes(inventory);
    }

    public ItemStack[] getEnderchestContents() {
        return ItemStack.deserializeItemsFromBytes(enderchest);
    }

    /**
     * Get a map with the IDS and MapViews of all maps in an array of items
     *
     * @param items The items (e.g. from an inventory) to get the maps
     * @return A map of IDs to MapView
     */
    public static Map<? extends Integer, ? extends MapView> getMapIds(ItemStack[] items) {
        Map<Integer, MapView> maps = new HashMap<>();
        for (ItemStack item : items) {
            if (item != null && item.getType() == Material.FILLED_MAP) {
                ItemMeta meta = item.getItemMeta();
                if (meta instanceof MapMeta && ((MapMeta) meta).hasMapView()) {
                    MapView view = ((MapMeta) meta).getMapView();
                    if (view != null) {
                        maps.put(view.getId(), view);
                    }
                }
            }
        }
        return maps;
    }

    public long timeStamp() {
        return timeStamp;
    }

    public int dataVersion() {
        return dataVersion;
    }

    public UUID playerId() {
        return playerId;
    }

    public String playerName() {
        return playerName;
    }

    public GameMode gamemode() {
        return gamemode;
    }

    public int totalExperience() {
        return totalExperience;
    }

    public int level() {
        return level;
    }

    public float exp() {
        return exp;
    }

    public Collection<PotionEffect> potionEffects() {
        return potionEffects;
    }

    public Set<MapData> maps() {
        return maps;
    }

    public double maxHealth() {
        return maxHealth;
    }

    public double health() {
        return health;
    }

    public boolean isHealthScaled() {
        return isHealthScaled;
    }

    public double healthScale() {
        return healthScale;
    }

    public int foodLevel() {
        return foodLevel;
    }

    public float saturation() {
        return saturation;
    }

    public float exhaustion() {
        return exhaustion;
    }

    public int maxAir() {
        return maxAir;
    }

    public int remainingAir() {
        return remainingAir;
    }

    public int fireTicks() {
        return fireTicks;
    }

    public int maxNoDamageTicks() {
        return maxNoDamageTicks;
    }

    public int noDamageTicks() {
        return noDamageTicks;
    }

    public float fallDistance() {
        return fallDistance;
    }

    public Vector velocity() {
        return velocity;
    }

    public int heldItemSlot() {
        return heldItemSlot;
    }

    public byte @Nullable [] persistentData() {
        return persistentData;
    }

    public @NotNull Map<String, Map<String, Long>> advancementProgress() {
        return advancementProgress;
    }

    public @NotNull Table<Statistic, String, Integer> statistics() {
        return statistics;
    }

    public long lastSeen() {
        return lastSeen;
    }

    protected boolean looseEquals(@Nullable Object obj) {
        if (obj == this) return true;
        return obj instanceof final @NotNull PlayerData other &&
            this.timeStamp == other.timeStamp &&
            this.dataVersion == other.dataVersion &&
            Objects.equals(this.playerId, other.playerId) &&
            Objects.equals(this.playerName, other.playerName) &&
            Objects.equals(this.gamemode, other.gamemode) &&
            this.totalExperience == other.totalExperience &&
            this.level == other.level &&
            Float.floatToIntBits(this.exp) == Float.floatToIntBits(other.exp) &&
            Arrays.equals(this.inventory, other.inventory) &&
            Arrays.equals(this.enderchest, other.enderchest) &&
            Objects.equals(this.potionEffects, other.potionEffects) &&
            Objects.equals(this.maps, other.maps) &&
            Double.doubleToLongBits(this.maxHealth) == Double.doubleToLongBits(other.maxHealth) &&
            Double.doubleToLongBits(this.health) == Double.doubleToLongBits(other.health) &&
            this.isHealthScaled == other.isHealthScaled &&
            Double.doubleToLongBits(this.healthScale) == Double.doubleToLongBits(other.healthScale) &&
            this.foodLevel == other.foodLevel &&
            Float.floatToIntBits(this.saturation) == Float.floatToIntBits(other.saturation) &&
            Float.floatToIntBits(this.exhaustion) == Float.floatToIntBits(other.exhaustion) &&
            this.maxAir == other.maxAir &&
            this.remainingAir == other.remainingAir &&
            this.fireTicks == other.fireTicks &&
            this.maxNoDamageTicks == other.maxNoDamageTicks &&
            this.noDamageTicks == other.noDamageTicks &&
            Float.floatToIntBits(this.fallDistance) == Float.floatToIntBits(other.fallDistance) &&
            Objects.equals(this.velocity, other.velocity) &&
            this.heldItemSlot == other.heldItemSlot &&
            Arrays.equals(this.persistentData, other.persistentData) &&
            Objects.equals(this.advancementProgress, other.advancementProgress) &&
            Objects.equals(this.statistics, other.statistics) &&
            this.lastSeen == other.lastSeen;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        return looseEquals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp, dataVersion, playerId, playerName, gamemode, totalExperience, level, exp, inventory, enderchest, potionEffects, maps, maxHealth, health, isHealthScaled, healthScale, foodLevel, saturation, exhaustion, maxAir, remainingAir, fireTicks, maxNoDamageTicks, noDamageTicks, fallDistance, velocity, heldItemSlot, persistentData, advancementProgress, statistics, lastSeen);
    }

    @Override
    public String toString() {
        return "PlayerData[" +
            "timeStamp=" + timeStamp + ", " +
            "dataVersion=" + dataVersion + ", " +
            "playerId=" + playerId + ", " +
            "playerName=" + playerName + ", " +
            "gamemode=" + gamemode + ", " +
            "totalExperience=" + totalExperience + ", " +
            "level=" + level + ", " +
            "exp=" + exp + ", " +
            "inventory=" + Arrays.toString(inventory) + ", " +
            "enderchest=" + Arrays.toString(enderchest) + ", " +
            "potionEffects=" + potionEffects + ", " +
            "maps=" + maps + ", " +
            "maxHealth=" + maxHealth + ", " +
            "health=" + health + ", " +
            "isHealthScaled=" + isHealthScaled + ", " +
            "healthScale=" + healthScale + ", " +
            "foodLevel=" + foodLevel + ", " +
            "saturation=" + saturation + ", " +
            "exhaustion=" + exhaustion + ", " +
            "maxAir=" + maxAir + ", " +
            "remainingAir=" + remainingAir + ", " +
            "fireTicks=" + fireTicks + ", " +
            "maxNoDamageTicks=" + maxNoDamageTicks + ", " +
            "noDamageTicks=" + noDamageTicks + ", " +
            "fallDistance=" + fallDistance + ", " +
            "velocity=" + velocity + ", " +
            "heldItemSlot=" + heldItemSlot + ", " +
            "persistentData=" + persistentData + ", " +
            "advancementProgress=" + advancementProgress + ", " +
            "statistics=" + statistics + ", " +
            "lastSeen=" + lastSeen + ']';
    }
}
