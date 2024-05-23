package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listen
import de.raphaelgoetz.betterbuild.manager.getWaitingPlayers
import de.raphaelgoetz.betterbuild.manager.removeFromQueue
import org.bukkit.Bukkit
import org.bukkit.event.world.WorldLoadEvent

fun registerWorldEvents() {

    listen<WorldLoadEvent> { worldLoadEvent ->
        val worldName = worldLoadEvent.world.name
        val players = getWaitingPlayers(worldName) ?: return@listen

        for (uuid in players) {
            val player = Bukkit.getPlayer(uuid)
            if (player == null || !player.isOnline) continue
            player.teleport(worldLoadEvent.world.spawnLocation)
        }

        removeFromQueue(worldName)

    }

}