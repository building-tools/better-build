package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBucketFillEvent

class PlayerBucketFillListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerBucketFillEvent(playerBucketFillEvent: PlayerBucketFillEvent) {
        betterBuild.playerManager
            .cancelWhenBuilder(playerBucketFillEvent.player, playerBucketFillEvent)
    }
}