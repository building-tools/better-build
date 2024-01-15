package de.raphaelgoetz.betterbuild.commands.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

data class TogglePlayerMode(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null) return true

        val target = if (args.size == 1) Bukkit.getPlayer(args[0]) else sender

        if (target == null) return true
        if (!target.isOnline) return true

        if ("build" == label) betterBuild.playerManager.toggleBuildMode(target)
        if ("clip" == label) betterBuild.playerManager.toggleNoClipMode(target)

        return false
    }
}