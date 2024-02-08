package de.raphaelgoetz.betterbuild.commands.world

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class ToggleWorldPhysics(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true

        if (!sender.hasPermission("betterbuild.world.physics")) {
            LanguageManager.sendPlayerMessage(sender, "command.player.teleport.permission")
            return true
        }

        val world = sender.world
        betterBuild.worldManager.togglePhysics(world)
        return false
    }
}