package de.raphaelgoetz.betterbuild.commands.player

import de.raphaelgoetz.betterbuild.BetterBuild
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class TogglePlayerSpeed(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        println(1)
        if (sender !is Player) return true
        println(11)

        if (args?.size!! != 1) return true
        println(2)

        val values = (0 until 11).map { it.toString() }
        println(3)

        if (!values.contains(args[0])) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#EB3349:#F45C43>⚠ Only values from 0 to 10!"));
            return true;
        }

        val speed = ((args[0]).toFloat() / 10);

        sender.sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#a8ff78:#78ffd6>✔ Speed updated to: $speed"));
        if ("fly" == label) sender.flySpeed = speed;
        if ("walk" == label) sender.walkSpeed = speed;
        return true
    }
}