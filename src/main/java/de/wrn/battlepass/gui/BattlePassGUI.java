package de.wrn.battlepass.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import de.wrn.battlepass.Main;

public class BattlePassGUI implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        Inventory inv = Bukkit.createInventory(null, 27, "§6BattlePass");

        int xp = Main.getInstance().getXpManager().getXP(p.getUniqueId());
        int tier = Main.getInstance().getXpManager().getTier(p.getUniqueId());

        ItemStack info = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = info.getItemMeta();
        meta.setDisplayName("§eDein Fortschritt");
        meta.setLore(java.util.List.of(
                "§7XP: §a" + xp,
                "§7Tier: §b" + tier,
                "§7Farm XP durch Blöcke & Kills"
        ));
        info.setItemMeta(meta);

        inv.setItem(13, info);

        p.openInventory(inv);
        return true;
    }
}
