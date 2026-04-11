package de.wrn.battlepass.system;

import java.util.HashMap;
import java.util.UUID;
import de.wrn.battlepass.Main;

public class XPManager {

    private final HashMap<UUID, Integer> xp = new HashMap<>();

    public void addXP(UUID uuid, int amount) {
        xp.put(uuid, getXP(uuid) + amount);
    }

    public int getXP(UUID uuid) {
        return xp.getOrDefault(uuid, 0);
    }

    public int getTier(UUID uuid) {
        int expPerTier = Main.getInstance().getConfig().getInt("season.exp-per-tier");
        return getXP(uuid) / expPerTier;
    }
}
