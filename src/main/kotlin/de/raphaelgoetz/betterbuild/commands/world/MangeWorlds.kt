package de.raphaelgoetz.betterbuild.commands.world

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.menus.ConfirmDeletionMenu
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class MangeWorlds(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null || args.isEmpty()) return true

        val operation = args[0]
        val name = args[1]

        if (operation == "create") createWorld(name, sender)
        if (operation == "delete") deleteWorld(name, sender)
        return false
    }

    private fun createWorld(name: String, player: Player) {

        if (betterBuild.worldManager.isWorld(name)) {
            player.sendMessage("already exist")
            return
        }

        betterBuild.worldManager.createEmptyWorld(name)
        player.sendMessage("world has been created")

    }

    private fun deleteWorld(name: String, player: Player) {

        if (!betterBuild.worldManager.isWorld(name)) {
            player.sendMessage("don't exist")
            return
        }

        ConfirmDeletionMenu(betterBuild, player, name, Component.text("Confirm"))
    }
}