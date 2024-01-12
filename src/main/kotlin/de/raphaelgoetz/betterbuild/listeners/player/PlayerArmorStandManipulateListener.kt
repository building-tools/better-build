package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerArmorStandManipulateEvent

class PlayerArmorStandManipulateListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerArmorStandManipulateEvent(playerArmorStandManipulateEvent: PlayerArmorStandManipulateEvent) {
        betterBuild.playerManager.cancelWhenBuilder(playerArmorStandManipulateEvent.player, playerArmorStandManipulateEvent)
    }
}