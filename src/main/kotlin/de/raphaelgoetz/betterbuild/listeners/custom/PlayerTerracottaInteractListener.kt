package de.raphaelgoetz.betterbuild.listeners.custom

import de.raphaelgoetz.betterbuild.utils.Terracotta
import org.bukkit.Material
import org.bukkit.Rotation
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.MultipleFacing
import org.bukkit.block.data.Rotatable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayerTerracottaInteractListener : Listener {

    @EventHandler
    fun onPlayerCampfireInteractionListener(playerInteractEvent: PlayerInteractEvent) {

        val block = playerInteractEvent.clickedBlock ?: return
        if (!block.isTerracotta()) return
        //TODO: APPLY ROTATION
    }

    private fun Block.isTerracotta(): Boolean {

        for(terracotta in Terracotta.entries) {
            if (terracotta.material != this.type) continue
            return true
        }

        return false
    }
}