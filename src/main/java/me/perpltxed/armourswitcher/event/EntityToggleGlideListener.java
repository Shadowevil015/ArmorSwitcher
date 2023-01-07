package me.perpltxed.armourswitcher.event;

import me.perpltxed.armourswitcher.ArmourSwitcher;
import me.perpltxed.armourswitcher.armormanager.ArmorManager;
import me.perpltxed.armourswitcher.util.EventUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityToggleGlideListener implements Listener {
    ArmourSwitcher plugin;
    ArmorManager armorManager;

    public EntityToggleGlideListener(ArmourSwitcher pl, ArmorManager am){
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
