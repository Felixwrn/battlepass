package de.wrn.battlepass;

import org.bukkit.plugin.java.JavaPlugin;
import de.wrn.battlepass.gui.BattlePassGUI;
import de.wrn.battlepass.system.XPManager;
import de.wrn.battlepass.listener.XPListener;

public class Main extends JavaPlugin {

    private static Main instance;
    private XPManager xpManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        xpManager = new XPManager();

        getCommand("bp").setExecutor(new BattlePassGUI());
        getServer().getPluginManager().registerEvents(new XPListener(), this);

        getLogger().info("wrnpass aktiviert!");
    }

    public static Main getInstance() {
        return instance;
    }

    public XPManager getXpManager() {
        return xpManager;
    }
}
