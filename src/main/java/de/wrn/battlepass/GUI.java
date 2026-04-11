package de.wrn.battlepass;

import org.bukkit.*;
import org.bukkit.entity.Player;

public class GUI {

    Main m;

    public GUI(Main m){
        this.m=m;
    }

    public void open(Player p){
        m.openGUI(p);
    }
}
