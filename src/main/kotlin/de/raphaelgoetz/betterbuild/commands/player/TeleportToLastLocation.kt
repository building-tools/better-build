package de.raphaelgoetz.betterbuild.commands.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class TeleportToLastLocation(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null || args.isNotEmpty()) return true

        val lastLocation = betterBuild.playerManager.getLastLocation(sender)

        if (!sender.hasPermission("betterbuild.player.back")) {
            betterBuild.languageManager.sendPlayerMessage(sender, "command.player.teleport.permission")
            return true
        }

        if (lastLocation == null) {
            betterBuild.languageManager.sendPlayerMessage(sender, "command.player.teleport.error")
            return true
        }

        if (lastLocation.world == null) {
            betterBuild.languageManager.sendPlayerMessage(sender, "command.player.teleport.unloaded")
            return true
        }

        betterBuild.languageManager.sendPlayerMessage(sender, "command.player.teleport.success")
        sender.teleport(lastLocation)
        return false
    }
}