package de.raphaelgoetz.betterbuild.commands.player

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class TogglePlayerMode(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null) return true

        if (!sender.hasPermission("betterbuild.player.mode")) {
            LanguageManager.sendPlayerMessage(sender, "command.player.mode.permission")
            return true
        }


        val target = if (args.size == 1) Bukkit.getPlayer(args[0]) else sender

        if (target == null) return true
        if (!target.isOnline) return true

        if ("build" == label) betterBuild.playerManager.toggleBuildMode(target)
        if ("clip" == label) betterBuild.playerManager.toggleNoClipMode(target)

        return false
    }
}