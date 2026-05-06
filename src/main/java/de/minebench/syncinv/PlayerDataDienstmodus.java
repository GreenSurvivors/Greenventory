package de.minebench.syncinv;

import de.greensurvivors.dienstmodus.DienstmodusData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class PlayerDataDienstmodus extends PlayerData {
    protected final @Nullable DienstmodusData dienstmodusData;

    public PlayerDataDienstmodus(final @NotNull Player player, final long lastSeen, final byte @Nullable [] persistentData,
                                 final @Nullable DienstmodusData dienstmodusData) {

        super(player, lastSeen, persistentData);
        this.dienstmodusData = dienstmodusData;
    }

    public @Nullable DienstmodusData dienstmodusData() {
        return dienstmodusData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (!super.looseEquals(obj)) return false;
        return Objects.equals(this.dienstmodusData, ((PlayerDataDienstmodus) obj).dienstmodusData);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(dienstmodusData);
    }

    @Override
    public String toString() {
        return "PlayerDataDienstmodus[" +
            "timeStamp=" + timeStamp() + ", " +
            "dataVersion=" + dataVersion() + ", " +
            "playerId=" + playerId() + ", " +
            "playerName=" + playerName() + ", " +
            "gamemode=" + gamemode() + ", " +
            "totalExperience=" + totalExperience() + ", " +
            "level=" + level() + ", " +
            "exp=" + exp() + ", " +
            "inventory=" + Arrays.toString(inventory) + ", " +
            "enderchest=" + Arrays.toString(enderchest) + ", " +
            "potionEffects=" + potionEffects() + ", " +
            "maps=" + maps() + ", " +
            "maxHealth=" + maxHealth() + ", " +
            "health=" + health() + ", " +
            "isHealthScaled=" + isHealthScaled() + ", " +
            "healthScale=" + healthScale() + ", " +
            "foodLevel=" + foodLevel() + ", " +
            "saturation=" + saturation() + ", " +
            "exhaustion=" + exhaustion() + ", " +
            "maxAir=" + maxAir() + ", " +
            "remainingAir=" + remainingAir() + ", " +
            "fireTicks=" + fireTicks() + ", " +
            "maxNoDamageTicks=" + maxNoDamageTicks() + ", " +
            "noDamageTicks=" + noDamageTicks() + ", " +
            "fallDistance=" + fallDistance() + ", " +
            "velocity=" + velocity() + ", " +
            "heldItemSlot=" + heldItemSlot() + ", " +
            "persistentData=" + Arrays.toString(persistentData()) + ", " +
            "advancementProgress=" + advancementProgress() + ", " +
            "statistics=" + statistics() + ", " +
            "lastSeen=" + lastSeen() +
            "dienstmodusData=" + dienstmodusData + ']';
    }
}
