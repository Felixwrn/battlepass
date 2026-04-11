package de.wrn.battlepass;

import org.bukkit.*;

public class Rewards {

    Main m;

    public Rewards(Main m){
        this.m=m;
    }

    public void give(Player p,int tier,String type){

        String path="rewards."+tier+"."+type+".";

        String t=m.getConfig().getString(path+"type");
        if(t==null) return;

        if(t.equals("ITEM")){
            Material mat=Material.valueOf(m.getConfig().getString(path+"material"));
            int a=m.getConfig().getInt(path+"amount");
            p.getInventory().addItem(new ItemStack(mat,a));
        }

        if(t.equals("COMMAND")){
            String cmd=m.getConfig().getString(path+"command")
                    .replace("%player%",p.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
        }
    }
}
