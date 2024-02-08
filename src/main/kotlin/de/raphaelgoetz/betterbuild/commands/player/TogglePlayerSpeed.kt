package de.raphaelgoetz.betterbuild.commands.player

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class TogglePlayerSpeed(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true

        if (!sender.hasPermission("betterbuild.player.speed")) {
            LanguageManager.sendPlayerMessage(sender, "command.player.speed.permission")
            return true
        }

        if (args == null || args.size != 1) {
            LanguageManager.sendPlayerMessage(sender, "command.player.speed.missing")
            return true
        }
        val values = (0 until 11).map { it.toString() }

        if (!values.contains(args[0])) {
            LanguageManager.sendPlayerMessage(sender, "command.player.speed.error")
            return true
        }

        val speed = ((args[0]).toFloat() / 10)
        LanguageManager.sendPlayerMessage(sender, "command.player.speed.success", "%speed%", speed.toString())

        if ("fly" == label) sender.flySpeed = speed
        if ("walk" == label) sender.walkSpeed = speed
        return true
    }
}