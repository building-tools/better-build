package de.raphaelgoetz.betterbuild.commands.player

import de.raphaelgoetz.astralis.text.communication.CommunicationType
import de.raphaelgoetz.astralis.text.translation.sendTransText
import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class TogglePlayerGamemode(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null) return true

        val player: Player = sender

        if (!sender.hasPermission("betterbuild.player.gamemode")) {
            player.sendTransText("command.player.gamemode.permission") {
                type = CommunicationType.ERROR
            }
            return true
        }

        if (args.size != 1) {
            player.sendTransText("command.player.gamemode.missing") {
                type = CommunicationType.ERROR
            }
            return true
        }

        val value = args[0].toInt()
        if (value > 3) {
            player.sendTransText("command.player.gamemode.error") {
                type = CommunicationType.ERROR
            }
            return true
        }

        when (value) {
            0 -> sender.gameMode = GameMode.SURVIVAL
            1 -> sender.gameMode = GameMode.CREATIVE
            2 -> sender.gameMode = GameMode.ADVENTURE
            3 -> sender.gameMode = GameMode.SPECTATOR
        }

        return false
    }
}