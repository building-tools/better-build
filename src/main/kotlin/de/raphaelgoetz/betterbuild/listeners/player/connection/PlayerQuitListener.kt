package de.raphaelgoetz.betterbuild.listeners.player.connection

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

data class PlayerQuitListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    private fun onPlayerQuitEvent(playerQuitEvent: PlayerQuitEvent) {
        val player = playerQuitEvent.player
        playerQuitEvent.quitMessage(betterBuild.languageManager.getComponent("event.quit.message", "%player%", player.name))
    }
}