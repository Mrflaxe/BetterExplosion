package ru.mrflaxe.betterexplosion.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.standalone.ArgumentableCommand;
import ru.soknight.lib.configuration.Messages;

public class SetVectorCommand extends ArgumentableCommand {

    public SetVectorCommand(Messages messages, JavaPlugin plugin) {
        super("setvector", null, "betterexplosion.command.setvector", 3, messages);
        register(plugin);
    }

    @Override
    protected void executeCommand(CommandSender sender, CommandArguments args) {
        float x = Float.valueOf(args.get(0));
        float y = Float.valueOf(args.get(1));
        float z = Float.valueOf(args.get(2));
        
        Player player = (Player) sender;
        Location loc = player.getLocation().setDirection(new Vector(x, y, z));
        player.teleport(loc);
    }

}
