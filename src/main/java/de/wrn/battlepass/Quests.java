package de.wrn.battlepass;

import org.bukkit.Material;
import org.bukkit.entity.*;

public class Quests {

    Main m;

    public Quests(Main m){
        this.m=m;
    }

    public void handleBlock(Player p, Material mat){
        if(mat==Material.STONE)
            m.addXP(p.getUniqueId(),5);
    }

    public void handleKill(Player p, EntityType t){
        if(t==EntityType.ZOMBIE)
            m.addXP(p.getUniqueId(),20);
    }
}
