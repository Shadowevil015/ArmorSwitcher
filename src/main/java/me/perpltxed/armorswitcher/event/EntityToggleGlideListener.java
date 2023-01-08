package me.perpltxed.armorswitcher.event;

import me.perpltxed.armorswitcher.ArmorSwitcher;
import me.perpltxed.armorswitcher.armormanager.ArmorManager;
import me.perpltxed.armorswitcher.util.EventUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityToggleGlideListener implements Listener {
    ArmorSwitcher plugin;
    ArmorManager armorManager;

    public EntityToggleGlideListener(ArmorSwitcher pl, ArmorManager am){
        this.plugin = pl;
        this.armorManager = am;
        EventUtil.register(this, pl);
    }

    @EventHandler
    public void onPlayerToggleGlide(EntityToggleGlideEvent e){
        if(!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        if(!plugin.hasPlayer(player)) return;

        new BukkitRunnable(){
            @Override
            public void run() {
                armorManager.updatePlayer(player);
            }
        }.runTaskLater(plugin, 1L);
    }
}
