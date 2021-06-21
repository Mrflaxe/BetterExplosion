package ru.mrflaxe.betterexplosion.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.standalone.PlayerOnlyCommand;
import ru.soknight.lib.configuration.Messages;

public class LaunchCommand extends PlayerOnlyCommand {

    public LaunchCommand(Messages messages, JavaPlugin plugin) {
        super("launch", "betterexplosion.command.launch", messages);
        register(plugin);
    }

    @Override
    protected void executeCommand(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        
        Vector vector = player.getLocation().getDirection();
        player.setVelocity(vector.multiply(2));
    }
}
