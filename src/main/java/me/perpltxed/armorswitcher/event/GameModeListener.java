package me.perpltxed.armorswitcher.event;

import me.perpltxed.armorswitcher.ArmorSwitcher;
import me.perpltxed.armorswitcher.armormanager.ArmorManager;
import me.perpltxed.armorswitcher.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class GameModeListener implements Listener {
    ArmorSwitcher plugin;
    ArmorManager armorManager;

    public GameModeListener(ArmorSwitcher pl, ArmorManager am){
        this.plugin = pl;
        this.armorManager = am;
        EventUtil.register(this, pl);
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event){
        if(!plugin.hasPlayer(event.getPlayer())) return;
        if(event.getNewGameMode().equals(GameMode.CREATIVE)){
            plugin.addIgnoredPlayer(event.getPlayer());
            armorManager.updatePlayer(event.getPlayer());
            plugin.removeIgnoredPlayer(event.getPlayer());
        }else{
            new BukkitRunnable(){
                @Override
                public void run() {
                    armorManager.updatePlayer(event.getPlayer());
                }
            }.runTaskLater(plugin, 1L);
        }
    }
}