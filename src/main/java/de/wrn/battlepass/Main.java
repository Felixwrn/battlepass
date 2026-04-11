package de.wrn.battlepass;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    Data data;
    Map<UUID, Integer> setupTier = new HashMap<>();
    Map<UUID, String> setupType = new HashMap<>();
    Map<UUID, Integer> cmdMode = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        data = new Data(this);

        getCommand("bp").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    // ================= XP =================

    public int xp(UUID u){ return data.getXP(u); }

    public void addXP(UUID u,int a){ data.addXP(u,a); }

    public int tier(UUID u){
        return xp(u)/getConfig().getInt("season.exp-per-tier");
    }

    public boolean premium(UUID u){
        return data.premium(u);
    }

    public boolean claimed(UUID u,int t){
        return data.claimed(u,t);
    }

    public void claim(UUID u,int t){
        data.claim(u,t);
    }

    // ================= COMMAND =================

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a){

        if(!(s instanceof Player p)) return true;

        if(a.length==0){
            openGUI(p);
            return true;
        }

        if(a[0].equalsIgnoreCase("setup")){
            openSetup(p);
            return true;
        }

        return true;
    }

    // ================= XP EVENTS =================

    @EventHandler
    public void breakBlock(BlockBreakEvent e){
        if(e.getBlock().getType()==Material.STONE)
            addXP(e.getPlayer().getUniqueId(),5);
    }

    @EventHandler
    public void kill(EntityDeathEvent e){
        if(e.getEntity().getKiller()==null) return;

        if(e.getEntityType()==EntityType.ZOMBIE)
            addXP(e.getEntity().getKiller().getUniqueId(),20);
    }

    // ================= GUI =================

    public void openGUI(Player p){

        Inventory inv = Bukkit.createInventory(null,54,"§6BattlePass");

        int tier = tier(p.getUniqueId());

        for(int i=1;i<=45;i++){

            ItemStack it = new ItemStack(Material.CHEST);
            ItemMeta m = it.getItemMeta();

            if(i>tier){
                m.setDisplayName("§cTier "+i);
            } else if(claimed(p.getUniqueId(),i)){
                m.setDisplayName("§aTier "+i+" (claimed)");
            } else {
                m.setDisplayName("§eTier "+i+" (click)");
            }

            it.setItemMeta(m);
            inv.setItem(i-1,it);
        }

        p.openInventory(inv);
    }

    // ================= CLAIM =================

    @EventHandler
    public void click(InventoryClickEvent e){

        if(e.getView().getTitle().equals("§6BattlePass")){
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            int tier = e.getSlot()+1;

            if(claimed(p.getUniqueId(),tier)) return;
            if(tier(p.getUniqueId()) < tier) return;

            giveReward(p,tier,"free");

            if(premium(p.getUniqueId()))
                giveReward(p,tier,"premium");

            claim(p.getUniqueId(),tier);

            p.sendMessage("§aClaimed!");
        }

        // SETUP GUI
        if(e.getView().getTitle().equals("§cBP Setup")){
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            int tier = e.getSlot()+1;

            setupTier.put(p.getUniqueId(),tier);
            openType(p);
        }

        // TYPE SELECT
        if(e.getView().getTitle().equals("§cWähle Reward Type")){
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();

            if(e.getSlot()==11){
                setupType.put(p.getUniqueId(),"free");
                openItem(p);
            }

            if(e.getSlot()==15){
                setupType.put(p.getUniqueId(),"premium");
                openItem(p);
            }
        }

        // ITEM SAVE
        if(e.getView().getTitle().equals("§aItem setzen")){
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();

            ItemStack hand = p.getInventory().getItemInMainHand();
            if(hand==null || hand.getType()==Material.AIR) return;

            int tier = setupTier.get(p.getUniqueId());
            String type = setupType.get(p.getUniqueId());

            getConfig().set("rewards."+tier+"."+type+".type","ITEM");
            getConfig().set("rewards."+tier+"."+type+".material",hand.getType().name());
            getConfig().set("rewards."+tier+"."+type+".amount",hand.getAmount());

            saveConfig();

            p.sendMessage("§aItem gespeichert!");
        }
    }

    // ================= CHAT COMMAND =================

    @EventHandler
    public void chat(AsyncPlayerChatEvent e){

        UUID u = e.getPlayer().getUniqueId();

        if(!cmdMode.containsKey(u)) return;

        e.setCancelled(true);

        int tier = cmdMode.get(u);

        getConfig().set("rewards."+tier+".premium.type","COMMAND");
        getConfig().set("rewards."+tier+".premium.command",e.getMessage());

        saveConfig();

        cmdMode.remove(u);

        e.getPlayer().sendMessage("§aCommand gespeichert!");
    }

    // ================= REWARD =================

    public void giveReward(Player p,int tier,String type){

        String path = "rewards."+tier+"."+type+".";

        if(!getConfig().contains(path+"type")) return;

        String t = getConfig().getString(path+"type");

        if(t.equals("ITEM")){
            Material m = Material.valueOf(getConfig().getString(path+"material"));
            int a = getConfig().getInt(path+"amount");
            p.getInventory().addItem(new ItemStack(m,a));
        }

        if(t.equals("COMMAND")){
            String cmd = getConfig().getString(path+"command")
                    .replace("%player%",p.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
        }
    }

    // ================= SETUP GUI =================

    public void openSetup(Player p){

        Inventory inv = Bukkit.createInventory(null,54,"§cBP Setup");

        for(int i=1;i<=45;i++){
            ItemStack it = new ItemStack(Material.BOOK);
            ItemMeta m = it.getItemMeta();
            m.setDisplayName("§eTier "+i);
            it.setItemMeta(m);
            inv.setItem(i-1,it);
        }

        p.openInventory(inv);
    }

    public void openType(Player p){

        Inventory inv = Bukkit.createInventory(null,27,"§cWähle Reward Type");

        ItemStack f = new ItemStack(Material.LIME_WOOL);
        ItemMeta fm = f.getItemMeta();
        fm.setDisplayName("§aFREE");
        f.setItemMeta(fm);

        ItemStack pr = new ItemStack(Material.PURPLE_WOOL);
        ItemMeta pm = pr.getItemMeta();
        pm.setDisplayName("§dPREMIUM");
        pr.setItemMeta(pm);

        inv.setItem(11,f);
        inv.setItem(15,pr);

        p.openInventory(inv);
    }

    public void openItem(Player p){

        Inventory inv = Bukkit.createInventory(null,27,"§aItem setzen");

        ItemStack info = new ItemStack(Material.BARRIER);
        ItemMeta m = info.getItemMeta();
        m.setDisplayName("§eItem in Hand halten");
        info.setItemMeta(m);

        inv.setItem(13,info);

        p.openInventory(inv);
    }
}
