package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBucketEntityEvent

class PlayerBucketEntityListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerBucketEntityEvent(playerBucketEntityEvent: PlayerBucketEntityEvent) {
        betterBuild.playerManager
            .cancelWhenBuilder(playerBucketEntityEvent.player, playerBucketEntityEvent)
    }
}