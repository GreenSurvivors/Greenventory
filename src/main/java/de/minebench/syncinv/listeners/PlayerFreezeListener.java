package de.minebench.syncinv.listeners;

import de.minebench.syncinv.SyncInv;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

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

public class PlayerFreezeListener implements Listener {
    private final @NotNull SyncInv plugin;

    public PlayerFreezeListener(@NotNull SyncInv plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    private void onDropItem(@NotNull PlayerDropItemEvent e) {
        if(plugin.isLocked(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getLang("cant-drop-items"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerMove(@NotNull PlayerMoveEvent e) {
        if(!sameBlock(e.getFrom(), e.getTo()) && plugin.isLocked(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            if (e.getFrom().getBlockY() == e.getTo().getBlockY()) {
                e.getPlayer().sendMessage(plugin.getLang("cant-move"));
            } else {
                e.getPlayer().setFallDistance(Math.min(0, e.getPlayer().getFallDistance() - 1));
            }
        }
    }

    private boolean sameBlock(@NotNull Location from, @NotNull Location to) {
        return from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ();
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerPickupItem(@NotNull EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player && plugin.isLocked(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
            e.getEntity().sendMessage(plugin.getLang("cant-pickup-items"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerPickupExp(PlayerExpChangeEvent e) {
        if(plugin.isLocked(e.getPlayer().getUniqueId())) {
            e.setAmount(0);
            e.getPlayer().sendMessage(plugin.getLang("cant-pickup-exp"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player && plugin.isLocked(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (plugin.isLocked(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onInventoryInteraction(InventoryClickEvent e) {
        if(plugin.isLocked(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(plugin.getLang("wait-for-loading"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onInventoryInteraction(InventoryDragEvent e) {
        if(plugin.isLocked(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(plugin.getLang("wait-for-loading"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onInventoryInteraction(InventoryOpenEvent e) {
        if(plugin.isLocked(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getLang("wait-for-loading"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onInteraction(PlayerInteractEvent e) {
        if(plugin.isLocked(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getLang("wait-for-loading"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onDamageEntity(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player && plugin.isLocked(e.getDamager().getUniqueId())) {
            e.setCancelled(true);
            e.getDamager().sendMessage(plugin.getLang("wait-for-loading"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockDamage(BlockDamageEvent e) {
        if(plugin.isLocked(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getLang("wait-for-loading"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onChat(AsyncPlayerChatEvent e) {
        if(plugin.isLocked(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getLang("wait-for-loading"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onChat(PlayerCommandPreprocessEvent e) {
        if(plugin.isLocked(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getLang("wait-for-loading"));
        }
    }
}
