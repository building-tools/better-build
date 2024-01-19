package de.raphaelgoetz.betterbuild.commands.world

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.menus.ConfirmDeletionMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class MangeWorlds(val betterBuild: BetterBuild) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null || args.isEmpty()) return true
        if (args.size < 2) {
            betterBuild.languageManager.sendPlayerMessage(sender, "command.world.manage.missing")
            return true
        }

        val operation = args[0]
        val name = args[1]

        if (operation == "create") createWorld(name, sender)
        if (operation == "delete") deleteWorld(name, sender)
        return false
    }

    private fun createWorld(name: String, player: Player) {

        if (player.hasPermission("betterbuild.world.create")) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.permission.create")
            return
        }

        if (betterBuild.worldManager.isWorld(name)) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.manage.exists")
            return
        }

        betterBuild.worldManager.createEmptyWorld(name)
        betterBuild.languageManager.sendPlayerMessage(player, "command.world.manage.create")
    }

    private fun deleteWorld(name: String, player: Player) {

        if (player.hasPermission("betterbuild.world.delete")) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.permission.deletion")
            return
        }

        if (!betterBuild.worldManager.isWorld(name)) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.manage.unknown")
            return
        }

        ConfirmDeletionMenu(betterBuild, player, name, betterBuild.languageManager.getComponent("gui.deletion.title"))
    }
}