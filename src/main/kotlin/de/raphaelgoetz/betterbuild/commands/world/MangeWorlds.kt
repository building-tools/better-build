package de.raphaelgoetz.betterbuild.commands.world

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.menus.ConfirmDeletionMenu
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.io.File

data class MangeWorlds(val betterBuild: BetterBuild) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null || args.isEmpty()) return true
        if (args.size < 2) {
            betterBuild.languageManager.sendPlayerMessage(sender, "command.world.manage.missing.first")
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
            betterBuild.languageManager.sendPlayerMessage(sender, "command.world.manage.missing.second")
            return true
        }

        val value = args[2]

        if (operation == "rename") {
            renameWorld(name, value, sender)
            return true
        }

        if (operation == "permission") setWorldEnterPermission(name, value, sender)
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String> {

        if (args?.size == 0) return mutableListOf()
        if (args?.size == 1) return mutableListOf("create", "delete", "permission", "spawn", "rename")

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
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.permission.create")
            return
        }

        if (betterBuild.worldManager.isWorld(name)) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.manage.exists")
            return
        }

        val clearedText = name.replace(Regex("\\W"), "")
        betterBuild.worldManager.createEmptyWorld(clearedText)
        betterBuild.worldManager.changeWorldPermission(name, "betterbuild.enter.free")
        betterBuild.languageManager.sendPlayerMessage(player, "command.world.manage.create")
    }

    private fun deleteWorld(name: String, player: Player) {

        if (!player.hasPermission("betterbuild.world.delete")) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.permission.deletion")
            return
        }

        if (!betterBuild.worldManager.isWorld(name)) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.manage.unknown")
            return
        }

        ConfirmDeletionMenu(betterBuild, player, name, betterBuild.languageManager.getComponent("gui.deletion.title"))
    }

    private fun renameWorld(from: String, to: String, player: Player) {

        if (!player.hasPermission("betterbuild.world.rename")) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.permission.rename")
            return
        }

        if (!betterBuild.worldManager.isWorld(from)) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.from.unknown")
            return
        }

        if (betterBuild.worldManager.isWorld(to)) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.to.known")
            return
        }

        val world = Bukkit.getWorld(from) ?: Bukkit.createWorld(WorldCreator(from))
        val folder = world?.worldFolder

        Bukkit.unloadWorld(from, true)
        folder?.renameTo(File(to))
    }

    private fun setWorldSpawn(player: Player) {

        if (!player.hasPermission("betterbuild.world.spawn")) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.permission.spawn")
            return
        }

        player.world.spawnLocation  = player.location
        betterBuild.languageManager.sendPlayerMessage(player, "command.world.spawn.changed")
    }

    private fun setWorldEnterPermission(world: String, permission: String, player: Player) {

        if (!player.hasPermission("betterbuild.world.permission")) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.permission.permission")
            return
        }

        if (!betterBuild.worldManager.isWorld(world)) {
            betterBuild.languageManager.sendPlayerMessage(player, "command.world.manage.unknown")
            return
        }

        var newPermission = permission
        if (permission == "none") newPermission = "betterbuild.enter.free"
        if (permission == "low") newPermission = "betterbuild.enter.low"
        if (permission == "medium") newPermission = "betterbuild.enter.medium"
        if (permission == "high") newPermission = "betterbuild.enter.high"

        betterBuild.languageManager.sendPlayerMessage(player, "command.world.enter.changed", "%permission%", newPermission)
        betterBuild.worldManager.changeWorldPermission(world, newPermission)
    }
}