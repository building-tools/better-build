package de.raphaelgoetz.betterbuild.commands.world

import de.raphaelgoetz.astralis.text.communication.CommunicationType
import de.raphaelgoetz.astralis.text.translation.sendTransText
import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.togglePhysics
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class ToggleWorldPhysics(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        val player: Player = sender

        if (!sender.hasPermission("betterbuild.world.physics")) {
            player.sendTransText("command.player.teleport.permission") {
                type = CommunicationType.ERROR
            }
            return true
        }

        val world = sender.world
        world.togglePhysics()
        return false
    }
}