package de.raphaelgoetz.betterbuild

import de.raphaelgoetz.betterbuild.manager.PlayerManager
import de.raphaelgoetz.betterbuild.manager.WorldManager
import org.bukkit.plugin.java.JavaPlugin

class BetterBuild : JavaPlugin() {

    val playerManager: PlayerManager = PlayerManager()
    val worldManager: WorldManager = WorldManager()

    override fun onEnable() {

    }
}