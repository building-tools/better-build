package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class PlayerTeleportListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerTeleportEvent(playerTeleportEvent: PlayerTeleportEvent) {

        var enterPermission = betterBuild.worldManager.getWorldPermission(playerTeleportEvent.to.world.name)
        if (enterPermission == "") enterPermission = "betterbuild.enter.free"
        val player = playerTeleportEvent.player

        if (!player.hasPermission(enterPermission)) {
            betterBuild.languageManager.sendPlayerMessage(player, "event.teleport.permission")
            player.teleport(playerTeleportEvent.from)
            return
        }

        betterBuild.playerManager.setLastLocation(playerTeleportEvent.player, playerTeleportEvent.from)
    }
}