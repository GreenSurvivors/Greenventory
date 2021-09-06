package de.minebench.syncinv;

import de.greensurvivors.dienstmodus.DienstmodusData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Player;

@ToString
@Getter
@Setter
public class PlayerDataDienstmodus extends PlayerData {
    DienstmodusData dienstmodus;

    public PlayerDataDienstmodus(Player player, long lastSeen, DienstmodusData dienstmodus) {
        super(player, lastSeen);
        this.dienstmodus = dienstmodus;
    }
}
