package me.perpltxed.armourswitcher.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;

import me.perpltxed.armourswitcher.ArmourSwitcher;
import me.perpltxed.armourswitcher.util.ItemUtil;

import me.perpltxed.armourswitcher.util.ProtocolUtil;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ArmorOthersPacketListener {
    private ArmourSwitcher pl;

    public ArmorOthersPacketListener(ArmourSwitcher pl, ProtocolManager manager){
        this.pl = pl;
        manager.addPacketListener(new PacketAdapter(pl, PacketType.Play.Server.ENTITY_EQUIPMENT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();

                LivingEntity livingEntity = (LivingEntity) manager.getEntityFromID(player.getWorld(), packet.getIntegers().read(0));
                if(!(livingEntity instanceof Player)) return;
                Player hidPlayer = (Player) livingEntity;

                if(pl.shouldNotHide(hidPlayer)) return;

                List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = packet.getSlotStackPairLists().read(0);

                pairList.stream().filter(ProtocolUtil::isArmorSlot).forEach(e -> {
                    if(e.getSecond().getType().equals(Material.ELYTRA) && ((hidPlayer.isGliding() || pl.isIgnoreElytra()) && !hidPlayer.isInvisible())){
                        e.setSecond(new ItemStack(Material.ELYTRA));
                    }
                    else if(!ignore(e.getSecond()))
                        e.setSecond(new ItemStack(Material.AIR));
                });
                packet.getSlotStackPairLists().write(0, pairList);
            }
        });
    }

    private boolean ignore(ItemStack is){
        if ((pl.isIgnoreLeatherArmor() && is.getType().toString().startsWith("LEATHER")) ||
                (pl.isIgnoreTurtleHelmet() && is.getType().equals(Material.TURTLE_HELMET)) ||
                (!ItemUtil.isArmor(is) &&
                        (is.getType().equals(Material.ELYTRA) && pl.isIgnoreElytra())))
            return true;
        return false;
    }
}
