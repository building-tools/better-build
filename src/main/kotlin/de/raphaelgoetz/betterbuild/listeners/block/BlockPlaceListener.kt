package de.raphaelgoetz.betterbuild.listeners.block

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.entity.BlockDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

data class BlockPlaceListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private fun onBlockPlaceEvent(blockPlaceEvent: BlockPlaceEvent) {
        val player = blockPlaceEvent.player
        betterBuild.playerManager.cancelWhenBuilder(player, blockPlaceEvent)

        if (!betterBuild.playerManager.isActiveGhost(player)) return
        val data = blockPlaceEvent.block.blockData
        val location = blockPlaceEvent.block.location

        blockPlaceEvent.isCancelled = true
        val blockDisplay = location.world.spawn(location, BlockDisplay::class.java)
        blockDisplay.block = data

        //TODO: CHECK IF PLAYER PLACED GHOSTBLOCK
    }
}