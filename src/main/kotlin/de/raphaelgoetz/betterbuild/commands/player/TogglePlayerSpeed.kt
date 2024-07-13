package de.raphaelgoetz.betterbuild.commands.player

import de.raphaelgoetz.astralis.text.communication.CommunicationType
import de.raphaelgoetz.astralis.text.translation.sendTransText
import de.raphaelgoetz.betterbuild.BetterBuild
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class TogglePlayerSpeed(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        val player: Player = sender

        if (!sender.hasPermission("betterbuild.player.speed")) {
            player.sendTransText("command.player.speed.permission") {
                type = CommunicationType.ERROR
            }
            return true
        }

        if (args == null || args.size != 1) {
            player.sendTransText("command.player.speed.missing") {
                type = CommunicationType.ERROR
            }
            return true
        }
        val values = (0 until 11).map { it.toString() }

        if (!values.contains(args[0])) {
            player.sendTransText("command.player.speed.error") {
                type = CommunicationType.ERROR
            }
            return true
        }

        val speed = ((args[0]).toFloat() / 10)
        player.sendTransText("command.player.speed.success") {
            type = CommunicationType.SUCCESS
            resolver = arrayOf(Placeholder.parsed("%speed%", speed.toString()))
        }
        if ("fly" == label) sender.flySpeed = speed
        if ("walk" == label) sender.walkSpeed = speed
        return true
    }
}