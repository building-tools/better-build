package de.raphaelgoetz.betterbuild.commands.player

import de.raphaelgoetz.astralis.text.communication.CommunicationType
import de.raphaelgoetz.astralis.text.translation.sendTransText
import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.getLastLocation
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class TeleportToLastLocation(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null || args.isNotEmpty()) return true

        val player: Player = sender
        val lastLocation = sender.uniqueId.getLastLocation()

        if (!sender.hasPermission("betterbuild.player.back")) {
            player.sendTransText("command.player.teleport.permission") {
                type = CommunicationType.ERROR
            }
            return true
        }

        if (lastLocation == null) {
            player.sendTransText("command.player.teleport.error") {
                type = CommunicationType.ERROR
            }
            return true
        }

        if (lastLocation.world == null) {
            player.sendTransText("command.player.teleport.unloaded") {
                type = CommunicationType.ERROR
            }
            return true
        }

        player.sendTransText("command.player.teleport.success") {
            type = CommunicationType.SUCCESS
        }
        sender.teleport(lastLocation)
        return false
    }
}