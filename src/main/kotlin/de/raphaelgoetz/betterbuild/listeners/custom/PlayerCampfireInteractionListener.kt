package de.raphaelgoetz.betterbuild.listeners.custom

import org.bukkit.Material
import org.bukkit.block.data.type.Campfire
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayerCampfireInteractionListener : Listener {

    @EventHandler
    fun onPlayerCampfireInteractionListener(playerInteractEvent: PlayerInteractEvent) {

        val block = playerInteractEvent.clickedBlock ?: return
        if (block.type != Material.CAMPFIRE) return

        val data = block.blockData
        if (data is Campfire) {
            data.isLit = !data.isLit
            block.blockData = data
        }
    }
}