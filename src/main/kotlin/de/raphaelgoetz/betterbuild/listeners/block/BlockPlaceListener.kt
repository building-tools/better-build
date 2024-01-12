package de.raphaelgoetz.betterbuild.listeners.block

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

data class BlockPlaceListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private fun onBlockPlaceEvent(blockPlaceEvent: BlockPlaceEvent) {
        betterBuild.playerManager.cancelWhenBuilder(blockPlaceEvent.player, blockPlaceEvent)
    }
}
