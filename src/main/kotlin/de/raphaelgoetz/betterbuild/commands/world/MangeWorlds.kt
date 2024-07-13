package de.raphaelgoetz.betterbuild.commands.world

import de.raphaelgoetz.astralis.text.communication.CommunicationType
import de.raphaelgoetz.astralis.text.translation.sendTransText
import de.raphaelgoetz.astralis.world.createBuildingWorld
import de.raphaelgoetz.astralis.world.existingWorlds
import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.changeWorldPermission
import de.raphaelgoetz.betterbuild.manager.isWorld
import de.raphaelgoetz.betterbuild.menus.openConfirmWorldDeletionMenu
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

data class MangeWorlds(val betterBuild: BetterBuild) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) return true
        if (args == null || args.isEmpty()) return true
        val player: Player = sender

        if (args.size < 2) {
            player.sendTransText("command.world.manage.missing.first") {
                type = CommunicationType.ERROR
            }
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
            player.sendTransText("command.world.manage.missing.second") {
                type = CommunicationType.ERROR
            }
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
            return existingWorlds.toMutableList()
        }

        if (args?.size == 3) {
            if (args[0] == "permission") return mutableListOf("none", "low", "medium", "high")
        }

        return mutableListOf()
    }

    private fun createWorld(name: String, player: Player) {

        if (!player.hasPermission("betterbuild.world.create")) {
            player.sendTransText("command.world.permission.create") {
                type = CommunicationType.ERROR
            }
            return
        }

        if (isWorld(name)) {
            player.sendTransText("command.world.manage.exists") {
                type = CommunicationType.ERROR
            }
            return
        }

        val clearedText = name.replace(Regex("\\W"), "")
        createBuildingWorld(clearedText)
        changeWorldPermission(name, "betterbuild.enter.free")
        player.sendTransText("command.world.manage.create") {
            type = CommunicationType.SUCCESS
        }
    }

    private fun deleteWorld(name: String, player: Player) {

        if (!player.hasPermission("betterbuild.world.delete")) {
            player.sendTransText("command.world.permission.deletion") {
                type = CommunicationType.ERROR
            }
            return
        }

        if (!isWorld(name)) {
            player.sendTransText("command.world.manage.unknown") {
                type = CommunicationType.ERROR
            }
            return
        }

        player.openConfirmWorldDeletionMenu(name, "gui.deletion.title")
    }

    private fun setWorldSpawn(player: Player) {

        if (!player.hasPermission("betterbuild.world.spawn")) {
            player.sendTransText("command.world.permission.spawn") {
                type = CommunicationType.ERROR
            }
            return
        }

        player.world.spawnLocation  = player.location
        player.sendTransText("command.world.spawn.changed") {
            type = CommunicationType.SUCCESS
        }
    }

    private fun setWorldEnterPermission(world: String, permission: String, player: Player) {

        if (!player.hasPermission("betterbuild.world.permission")) {
            player.sendTransText("command.world.permission.permission") {
                type = CommunicationType.ERROR
            }
            return
        }

        if (!isWorld(world)) {
            player.sendTransText("command.world.manage.unknown") {
                type = CommunicationType.ERROR
            }
            return
        }

        var newPermission = permission
        if (permission == "none") newPermission = "betterbuild.enter.free"
        if (permission == "low") newPermission = "betterbuild.enter.low"
        if (permission == "medium") newPermission = "betterbuild.enter.medium"
        if (permission == "high") newPermission = "betterbuild.enter.high"

        player.sendTransText("command.world.enter.changed") {
            type = CommunicationType.ERROR
            resolver = arrayOf(Placeholder.parsed("%permission%", newPermission))
        }
        changeWorldPermission(world, newPermission)
    }
}