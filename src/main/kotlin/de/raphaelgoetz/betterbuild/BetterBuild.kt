package de.raphaelgoetz.betterbuild

import de.raphaelgoetz.betterbuild.commands.player.TeleportToLastLocation
import de.raphaelgoetz.betterbuild.commands.player.TogglePlayerGamemode
import de.raphaelgoetz.betterbuild.commands.player.TogglePlayerMode
import de.raphaelgoetz.betterbuild.commands.player.TogglePlayerSpeed
import de.raphaelgoetz.betterbuild.commands.world.MangeWorlds
import de.raphaelgoetz.betterbuild.commands.world.ToggleWorldPhysics
import de.raphaelgoetz.betterbuild.listeners.*
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import org.bukkit.plugin.java.JavaPlugin

class BetterBuild : JavaPlugin() {

    override fun onEnable() {
        LanguageManager.readConfig()

        registerCommands()
        registerListener()
    }

    private fun registerCommands() {

        //PLAYER
        getCommand("back")?.setExecutor(TeleportToLastLocation(this))
        getCommand("build")?.setExecutor(TogglePlayerMode(this))
        getCommand("clip")?.setExecutor(TogglePlayerMode(this))
        getCommand("fly")?.setExecutor(TogglePlayerSpeed(this))
        getCommand("walk")?.setExecutor(TogglePlayerSpeed(this))
        getCommand("gm")?.setExecutor(TogglePlayerGamemode(this))

        //WORLD
        getCommand("world")?.setExecutor(MangeWorlds(this))
        getCommand("world")?.tabCompleter = MangeWorlds(this)
        getCommand("physics")?.setExecutor(ToggleWorldPhysics(this))
    }

    private fun registerListener() {
        registerBlockEvents()
        registerCustomEvents()
        registerHangingEvents()
        registerPlayerEvents()
        registerRaidEvents()
        registerVehicleEvents()
        registerWorldEvents()
    }
}