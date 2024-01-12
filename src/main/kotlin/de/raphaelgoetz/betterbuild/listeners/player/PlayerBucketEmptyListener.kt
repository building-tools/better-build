package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBucketEmptyEvent

class PlayerBucketEmptyListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerBucketEmptyEvent(playerBucketEmptyEvent: PlayerBucketEmptyEvent) {
        betterBuild.playerManager
            .cancelWhenBuilder(playerBucketEmptyEvent.player, playerBucketEmptyEvent)
    }
}