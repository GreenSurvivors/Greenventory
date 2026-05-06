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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record PlayerData(long timeStamp, int dataVersion, UUID playerId, String playerName, GameMode gamemode,
                         int totalExperience, int level, float exp, byte[][] inventory, byte[][] enderchest,
                         Collection<PotionEffect> potionEffects, Set<MapData> maps, double maxHealth, double health,
                         boolean isHealthScaled, double healthScale, int foodLevel, float saturation, float exhaustion,
                         int maxAir, int remainingAir, int fireTicks, int maxNoDamageTicks, int noDamageTicks,
                         float fallDistance, Vector velocity, int heldItemSlot, byte[] persistentData,
                         Map<String, Map<String, Long>> advancementProgress,
                         Table<Statistic, String, Integer> statistics, long lastSeen) implements Serializable {

    public static PlayerData create(Player player, long lastSeen, byte[] persistentData) {
        return new PlayerData(
                System.currentTimeMillis(),
                player.getServer().getUnsafe().getDataVersion(),
                player.getUniqueId(),
                player.getName(),
                player.getGameMode(),
                player.getTotalExperience(),
                player.getLevel(),
                player.getExp(),
                serializeItems(player.getInventory().getContents()),
                serializeItems(player.getEnderChest().getContents()),
                player.getActivePotionEffects(),
                new HashSet<>(),
                player.getMaxHealth(),
                player.getHealth(),
                player.isHealthScaled(),
                player.getHealthScale(),
                player.getFoodLevel(),
                player.getSaturation(),
                player.getExhaustion(),
                player.getMaximumAir(),
                player.getRemainingAir(),
                player.getFireTicks(),
                player.getMaximumNoDamageTicks(),
                player.getNoDamageTicks(),
                player.getFallDistance(),
                player.getVelocity(),
                player.getInventory().getHeldItemSlot(),
                persistentData,
                new HashMap<>(),
                HashBasedTable.create(),
                lastSeen
        );
    }

    public ItemStack[] getInventoryContents() {
        return deserializeItems(inventory);
    }

    public ItemStack[] getEnderchestContents() {
        return deserializeItems(enderchest);
    }

    private static byte[][] serializeItems(ItemStack[] items) {
        byte[][] itemByteArray = new byte[items.length][];
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            itemByteArray[i] = item != null ? item.serializeAsBytes() : null;
        }
        return itemByteArray;
    }

    private static ItemStack[] deserializeItems(byte[][] items) {
        ItemStack[] itemsArray = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            byte[] itemBytes = items[i];
            itemsArray[i] = itemBytes != null ? ItemStack.deserializeBytes(itemBytes) : null;
        }
        return itemsArray;
    }

    /**
     * Get a map with the IDS and MapViews of all maps in an array of items
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
}
