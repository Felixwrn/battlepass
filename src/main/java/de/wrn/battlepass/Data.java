package de.wrn.battlepass;

import org.bukkit.configuration.file.*;
import java.io.File;
import java.util.UUID;

public class Data {

    File file;
    FileConfiguration cfg;

    public Data(Main m){
        file = new File(m.getDataFolder(),"data.yml");
        if(!file.exists()) try{file.createNewFile();}catch(Exception ignored){}
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public int getXP(UUID u){ return cfg.getInt("xp."+u,0); }

    public void addXP(UUID u,int a){
        cfg.set("xp."+u,getXP(u)+a);
        save();
    }

    public boolean claimed(UUID u,int t){
        return cfg.getBoolean("c."+u+"."+t,false);
    }

    public void claim(UUID u,int t){
        cfg.set("c."+u+"."+t,true);
        save();
    }

    public boolean premium(UUID u){
        return cfg.getBoolean("p."+u,false);
    }

    public void save(){
        try{cfg.save(file);}catch(Exception ignored){}
    }
}
