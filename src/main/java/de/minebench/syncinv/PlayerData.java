package de.minebench.syncinv;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/*
 * Copyright 2017 Phoenix616 All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation,  version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
@ToString
@Getter
public class PlayerData implements Serializable {
    private static final long serialVersionUID = 4782303812031183L;
    private final long timeStamp = System.currentTimeMillis();
    private final UUID playerId;
    private final int totalExperience;
    private final int level;
    private final float exp;
    private final ItemStack[] inventory;
    private final ItemStack[] enderchest;
    private final Collection<PotionEffect> potionEffects;
    private final Set<MapData> maps = new HashSet<>();
    private final double maxHealth;
    private final double health;
    private final boolean isHealthScaled;
    private final double healthScale;
    private final int foodLevel;
    private final float exhaustion;
    private final int maxAir;
    private final int remainingAir;
    private final int fireTicks;
    private final int maxNoDamageTicks;
    private final int noDamageTicks;
    private final Vector velocity;
    private final int heldItemSlot;

    PlayerData(Player player) {
        this.playerId = player.getUniqueId();
        this.totalExperience = player.getTotalExperience();
        this.level = player.getLevel();
        this.exp = player.getExp();
        this.inventory = player.getInventory().getContents();
        this.enderchest = player.getEnderChest().getContents();
        this.potionEffects = player.getActivePotionEffects();
        this.maxHealth = player.getMaxHealth();
        this.health = player.getHealth();
        this.isHealthScaled = player.isHealthScaled();
        this.healthScale = player.getHealthScale();
        this.foodLevel = player.getFoodLevel();
        this.exhaustion = player.getExhaustion();
        this.maxAir = player.getMaximumAir();
        this.remainingAir = player.getRemainingAir();
        this.fireTicks = player.getFireTicks();
        this.maxNoDamageTicks = player.getMaximumNoDamageTicks();
        this.noDamageTicks = player.getNoDamageTicks();
        this.velocity = player.getVelocity();
        this.heldItemSlot = player.getInventory().getHeldItemSlot();
    }

    /**
     * Get a list with the ids of all maps in an array of items
     * @param items The items (e.g. from an inventory) to get the map ids from
     * @return A list of map ids (shorts)
     */
    public static List<Short> getMapIds(ItemStack[] items) {
        List<Short> mapIds = new ArrayList<>();
        for (ItemStack item : items) {
            if (item != null && item.getType() == Material.MAP) {
                mapIds.add(item.getDurability());
            }
        }
        return mapIds;
    }
}
