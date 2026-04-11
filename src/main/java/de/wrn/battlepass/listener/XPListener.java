package de.wrn.battlepass.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;

import de.wrn.battlepass.Main;

public class XPListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        int xp = Main.getInstance().getConfig().getInt("xp.block-break");
        Main.getInstance().getXpManager().addXP(p.getUniqueId(), xp);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;

        Player p = e.getEntity().getKiller();
        int xp = Main.getInstance().getConfig().getInt("xp.mob-kill");
        Main.getInstance().getXpManager().addXP(p.getUniqueId(), xp);
    }
}
