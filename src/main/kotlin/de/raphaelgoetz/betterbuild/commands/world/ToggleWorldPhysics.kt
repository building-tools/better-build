package de.raphaelgoetz.betterbuild.commands.world

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

data class ToggleWorldPhysics(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        //TODO: Toggle Physics of a world
        return false
    }
}