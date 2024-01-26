package de.raphaelgoetz.betterbuild.listeners.world

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent

data class WorldLoadListener(val betterBuild: BetterBuild) : Listener  {

    @EventHandler
    fun onWorldLoadEvent(worldLoadEvent: WorldLoadEvent) {
        println(worldLoadEvent.world.name)

        val worldName = worldLoadEvent.world.name
        val players = betterBuild.worldManager.getWaitingPlayers(worldName) ?: return

        for (uuid in players) {
            val player = Bukkit.getPlayer(uuid)
            if (player == null || !player.isOnline) continue
            player.teleport(worldLoadEvent.world.spawnLocation)
        }

        betterBuild.worldManager.removeFromQueue(worldName)
    }
}