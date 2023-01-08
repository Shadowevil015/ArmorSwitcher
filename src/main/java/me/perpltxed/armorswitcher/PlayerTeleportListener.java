package me.perpltxed.armorswitcher;


import me.perpltxed.armorswitcher.FakeArmorPacketListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {
    private FakeArmorPacketListener fakeArmorPacketListener;

    public PlayerTeleportListener(FakeArmorPacketListener fakeArmorPacketListener) {
        this.fakeArmorPacketListener = fakeArmorPacketListener;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            fakeArmorPacketListener.updatePlayer(event.getPlayer());
        }
    }
}
