package de.minebench.syncinv;

import de.greensurvivors.dienstmodus.data.PartialPlayerData;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

@ToString
@Getter
public class PlayerDataDienstmodus extends PlayerData {
    final PartialPlayerData dienstmodus;

    public PlayerDataDienstmodus(Player player, long lastSeen, PartialPlayerData dienstmodus) {
        super(player, lastSeen);
        this.dienstmodus = dienstmodus;
    }
}
