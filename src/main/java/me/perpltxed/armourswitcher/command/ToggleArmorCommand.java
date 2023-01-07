package me.perpltxed.armorswitcher.command;

import me.perpltxed.armorswitcher.ArmorSwitcher;
import me.perpltxed.armorswitcher.armormanager.ArmorManager;
import me.perpltxed.armorswitcher.util.CommandUtil;
import me.perpltxed.armorswitcher.util.StrUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleArmorCommand {
    ArmorSwitcher plugin;
    ArmorManager armorManager;

    public ToggleArmorCommand(ArmorSwitcher pl, ArmorManager am){
        armorManager = am;
        plugin = pl;
        new CommandUtil(plugin,"togglearmor", 0,1, true, plugin.isToggleDefault()){

            @Override
            public void sendUsage(CommandSender sender) {
                if(sender instanceof Player)
                    sender.sendMessage(plugin.getPrefix() + StrUtil.color("&2Correct use:&f /togglearmor " + (canUseArg(sender, "other") ? "[player]" : "")));
                else
                    sender.sendMessage(plugin.getPrefix() + StrUtil.color("&2Correct use:&f /togglearmor <player>"));
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){
                Player player;
                if(arguments.length==1){
                    if(!canUseArg(sender, "other") && !plugin.isToggleOtherDefault()) return false;
                    String playerName = arguments[0];
                    player = Bukkit.getPlayer(playerName);

                    if(player==null){
                        sender.sendMessage(plugin.getPrefix() + "Player not found.");
                        return true;
                    }
                }else {
                    player = (Player) sender;
                }

                String visibility;

                if(plugin.hasPlayer(player)){
                    plugin.removeHiddenPlayer(player);
                    visibility = StrUtil.color("&bON");
                }else {
                    plugin.addHiddenPlayer(player);
                    visibility =  StrUtil.color("&7OFF") ;
                }

                if(!player.equals((Player) sender)) sender.sendMessage(player.getName() + "'s armor visibility': " + visibility);

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Armor visibility: " + visibility));

                armorManager.updatePlayer(player);

                return true;
            }
        }.setCPermission("toggle").setUsage("/togglearmor").setDescription("Toggle armor invisibility");
    }
}
