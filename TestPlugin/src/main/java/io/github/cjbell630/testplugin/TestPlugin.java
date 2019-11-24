package io.github.cjbell630.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public final class TestPlugin extends JavaPlugin {
    private HashMap<String, String> playerList = new HashMap<String, String>();

    @Override
    public void onEnable() {
        // TODO Insert logic to be performed when the plugin is enabled
        getLogger().info("onEnable has been invoked!");
        /*
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playerList.put(player.getName(), playerData(player));
        }
        */
    }

    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
        getLogger().info("onDisable has been invoked!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("basic") && checkArgs(sender, args, 0) && sender.hasPermission("basic")) {
            sender.sendMessage("bruh");
        }
        if (cmd.getName().equalsIgnoreCase("name") && checkArgs(sender, args, 1) && sender.hasPermission("name") && senderIsType(sender, "player")) {
            Player player = Bukkit.getServer().getPlayer(sender.getName());
            assert player != null;
            ItemStack item = player.getInventory().getItemInMainHand();
            item.setItemMeta(Bukkit.getItemFactory().getItemMeta(item.getType()));
            Objects.requireNonNull(item.getItemMeta()).setDisplayName(args[0]);
        }
        return false;
    }

    public boolean checkArgs(CommandSender sender, String[] args, int requiredAmount) {
        if (args.length == requiredAmount) {
            return true;
        } else if (args.length < requiredAmount) {
            sender.sendMessage("Not enough arguments!");
        } else {
            sender.sendMessage("Too many arguments!");
        }
        return false;
    }

    public boolean senderIsType(CommandSender sender, String type) {
        if(sender instanceof Player && !type.equals("player")) {
            sender.sendMessage("Players can't use this command!");
            return false;
        }
        if (!(sender instanceof Player) && type.equals("player")){
            sender.sendMessage("You must be a player to use this command!");
            return false;
        }
        return true;
    }

    public boolean playerIsOnline(CommandSender sender, String player){
        if (Bukkit.getServer().getPlayer(player) == null) {
            sender.sendMessage(player + " is not online!");
            return false;
        }
        return true;
    }

}
