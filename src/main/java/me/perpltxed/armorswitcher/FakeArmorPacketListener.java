package me.perpltxed.armorswitcher;

import com.comphenix.protocol.events.PacketContainer;
import me.perpltxed.armorswitcher.armormanager.ArmorManager;

import java.util.Set;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FakeArmorPacketListener implements Listener {
    private ArmorSwitcher plugin;
    private ProtocolManager manager;
    private Set<String> hiddenPlayers;
    private ArmorManager armorManager;


    public FakeArmorPacketListener (ArmorSwitcher plugin, ProtocolManager manager, Set<String> hiddenPlayers, ArmorManager armorManager) {
        this.plugin = plugin;
        this.manager = manager;
        this.hiddenPlayers = hiddenPlayers;
        this.armorManager = armorManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        manager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_EQUIPMENT) {

            @EventHandler
            public void onPlayerTeleport(PlayerTeleportEvent event) {
                Player player = event.getPlayer();
                if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
                    updatePlayer(player);
                }
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player viewer = event.getPlayer();

                LivingEntity livingEntity = (LivingEntity) packet.getEntityModifier(event).read(0);
                if (!(livingEntity instanceof Player)) {
                    return;
                }

                Player player = (Player) livingEntity;
                PlayerInventory inv = player.getInventory();
                ItemStack itemStack = packet.getItemModifier().read(0);
                if (itemStack.getType() == Material.DIAMOND_BOOTS
                        || itemStack.getType() == Material.DIAMOND_LEGGINGS
                        || itemStack.getType() == Material.DIAMOND_CHESTPLATE
                        || itemStack.getType() == Material.DIAMOND_HELMET) {
                    if (!hiddenPlayers.contains(viewer.getName())) {
                        return;
                    }
                    packet.getItemModifier().write(0, new ItemStack(Material.LEATHER_BOOTS));
                }
            }
        });
    }

    public void updatePlayer(Player player) {
        updateSelf(player);
        updateOthers(player);
    }

    public void updateSelf(Player player) {
        armorManager.updateSelf(player);
    }

    public void updateOthers(Player player) {
        for (Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
            if (otherPlayer == player) {
                continue;
            }
            armorManager.updateOther(player, otherPlayer);
        }
    }

    public void addHiddenPlayer(String playerName) {
        hiddenPlayers.add(playerName);
    }

    public void removeHiddenPlayer(String playerName) {
        hiddenPlayers.remove(playerName);
    }
}





