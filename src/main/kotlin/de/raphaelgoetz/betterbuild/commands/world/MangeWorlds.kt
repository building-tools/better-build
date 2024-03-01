package de.raphaelgoetz.betterbuild.commands.world

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import de.raphaelgoetz.betterbuild.menus.ConfirmDeletionMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

data class MangeWorlds(val betterBuild: BetterBuild) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null || args.isEmpty()) return true
        if (args.size < 2) {
            LanguageManager.sendPlayerMessage(sender, "command.world.manage.missing.first")
            return true
        }

        val operation = args[0]
        val name = args[1]

        if (operation == "create") {
            createWorld(name, sender)
            return true
        }

        if (operation == "delete") {
            deleteWorld(name, sender)
            return true
        }

        if (operation == "spawn") {
            setWorldSpawn(sender)
            return true
        }

        if (args.size < 3) {
            LanguageManager.sendPlayerMessage(sender, "command.world.manage.missing.second")
            return true
        }

        val value = args[2]

        if (operation == "permission") setWorldEnterPermission(name, value, sender)
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String> {

        if (args?.size == 0) return mutableListOf()
        if (args?.size == 1) return mutableListOf("create", "delete", "permission", "spawn")

        if (args?.size == 2) {
            if (args[0] == "create") return mutableListOf()
            return betterBuild.worldManager.getWorldNames()
        }

        if (args?.size == 3) {
            if (args[0] == "permission") return mutableListOf("none", "low", "medium", "high")
        }

        return mutableListOf()
    }

    private fun createWorld(name: String, player: Player) {

        if (!player.hasPermission("betterbuild.world.create")) {
            LanguageManager.sendPlayerMessage(player, "command.world.permission.create")
            return
        }

        if (betterBuild.worldManager.isWorld(name)) {
            LanguageManager.sendPlayerMessage(player, "command.world.manage.exists")
            return
        }

        val clearedText = name.replace(Regex("\\W"), "")
        betterBuild.worldManager.createEmptyWorld(clearedText)
        betterBuild.worldManager.changeWorldPermission(name, "betterbuild.enter.free")
        LanguageManager.sendPlayerMessage(player, "command.world.manage.create")
    }

    private fun deleteWorld(name: String, player: Player) {

        if (!player.hasPermission("betterbuild.world.delete")) {
            LanguageManager.sendPlayerMessage(player, "command.world.permission.deletion")
            return
        }

        if (!betterBuild.worldManager.isWorld(name)) {
            LanguageManager.sendPlayerMessage(player, "command.world.manage.unknown")
            return
        }

        ConfirmDeletionMenu(betterBuild, player, name, LanguageManager.getComponent("gui.deletion.title"))
    }

    private fun setWorldSpawn(player: Player) {

        if (!player.hasPermission("betterbuild.world.spawn")) {
            LanguageManager.sendPlayerMessage(player, "command.world.permission.spawn")
            return
        }

        player.world.spawnLocation  = player.location
        LanguageManager.sendPlayerMessage(player, "command.world.spawn.changed")
    }

    private fun setWorldEnterPermission(world: String, permission: String, player: Player) {

        if (!player.hasPermission("betterbuild.world.permission")) {
            LanguageManager.sendPlayerMessage(player, "command.world.permission.permission")
            return
        }

        if (!betterBuild.worldManager.isWorld(world)) {
            LanguageManager.sendPlayerMessage(player, "command.world.manage.unknown")
            return
        }

        var newPermission = permission
        if (permission == "none") newPermission = "betterbuild.enter.free"
        if (permission == "low") newPermission = "betterbuild.enter.low"
        if (permission == "medium") newPermission = "betterbuild.enter.medium"
        if (permission == "high") newPermission = "betterbuild.enter.high"

        LanguageManager.sendPlayerMessage(player, "command.world.enter.changed", "%permission%", newPermission)
        betterBuild.worldManager.changeWorldPermission(world, newPermission)
    }
}