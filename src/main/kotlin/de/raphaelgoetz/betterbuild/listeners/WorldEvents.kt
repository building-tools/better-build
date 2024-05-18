package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listen
import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Bukkit
import org.bukkit.event.world.WorldLoadEvent

fun registerWorldEvents(betterBuild: BetterBuild) {

    listen<WorldLoadEvent> { worldLoadEvent ->
        val worldName = worldLoadEvent.world.name
        val players = betterBuild.worldManager.getWaitingPlayers(worldName) ?: return@listen

        for (uuid in players) {
            val player = Bukkit.getPlayer(uuid)
            if (player == null || !player.isOnline) continue
            player.teleport(worldLoadEvent.world.spawnLocation)
        }

        betterBuild.worldManager.removeFromQueue(worldName)

    }

}