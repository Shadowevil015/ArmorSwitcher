package me.perpltxed.armorswitcher.event;

import me.perpltxed.armorswitcher.ArmorSwitcher;
import me.perpltxed.armorswitcher.armormanager.ArmorManager;
import me.perpltxed.armorswitcher.util.EventUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PotionEffectListener implements Listener {
    ArmorSwitcher plugin;
    ArmorManager armorManager;

    public PotionEffectListener(ArmorSwitcher pl, ArmorManager am){
        this.plugin = pl;
        this.armorManager = am;
        EventUtil.register(this, pl);
    }

    @EventHandler
    public void onPlayerInvisibleEffect(EntityPotionEffectEvent event){
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        new BukkitRunnable(){
            @Override
            public void run() {
                armorManager.updatePlayer(player);
            }
        }.runTaskLater(plugin, 2L);
    }
}
