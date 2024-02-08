package de.raphaelgoetz.betterbuild.commands.player

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class TogglePlayerGamemode(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null) return true

        if (!sender.hasPermission("betterbuild.player.gamemode")) {
            LanguageManager.sendPlayerMessage(sender, "command.player.gamemode.permission")
            return true
        }

        if (args.size != 1) {
            LanguageManager.sendPlayerMessage(sender, "command.player.gamemode.missing")
            return true
        }

        val value = args[0].toInt()
        if (value > 3) {
            LanguageManager.sendPlayerMessage(sender, "command.player.gamemode.error")
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