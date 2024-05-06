package de.minebench.syncinv.messenger;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

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

public class PlayerDataQuery {
    private final long timestamp = System.currentTimeMillis();
    private final @NotNull UUID playerId;
    private final long localLastSeen;
    private final @NotNull Consumer<@NotNull PlayerDataQuery> onComplete;
    private @Nullable BukkitTask timeoutTask;
    private boolean completed = false;

    private final @NotNull Map<@NotNull String, @NotNull Long> servers = new ConcurrentHashMap<>();

    public PlayerDataQuery(@NotNull UUID playerId, long localLastSeen, @NotNull Consumer<@NotNull PlayerDataQuery> onComplete) {
        this.playerId = playerId;
        this.localLastSeen = localLastSeen;
        this.onComplete = onComplete;
    }

    public @NotNull UUID getPlayerId() {
        return playerId;
    }

    public long getLocalLastSeen() {
        return localLastSeen;
    }

    public @NotNull Map<@NotNull String, @NotNull Long> getServers() {
        return servers;
    }

    /**
     * Add a new response to this query
     * @param server The name of the server
     * @param lastSeen When the user was last seen on that server
     */
    public void addResponse(@NotNull String server, long lastSeen) {
        servers.put(server, lastSeen);
    }

    /**
     * Get the youngest server recorded by this query
     * @return The name of the server or null if none was younger
     */
    public @Nullable String getYoungestServer() {
        String server = null;
        long timestamp = localLastSeen;
        for (Map.Entry<String, Long> entry : servers.entrySet()) {
            if (entry.getValue() > timestamp) {
                timestamp = entry.getValue();
                server = entry.getKey();
            }
        }
        return server;
    }

    /**
     * Get the time this query was created
     * @return Timestamp in milliseconds since midnight, January 1, 1970 UTC.
     */
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimeoutTask(@Nullable BukkitTask timeoutTask) {
        this.timeoutTask = timeoutTask;
    }

    public void stopTimeout() {
        if (timeoutTask != null) {
            timeoutTask.cancel();
            timeoutTask = null;
        }
    }

    /**
     * Set the status of this query to completed (all servers responded)
     */
    public void complete() {
        completed = true;
    }

    /**
     * @return Whether or not this query was marked as completed (all servers responded)
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Get notified for when this query completes
     * @return The consumer for when the query completes; gets this query objected passed for easier instance creation
     */
    public @NotNull Consumer<@NotNull PlayerDataQuery> getOnComplete() {
        return onComplete;
    }
}
