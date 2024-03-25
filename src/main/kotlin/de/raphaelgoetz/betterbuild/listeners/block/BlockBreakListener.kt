package de.raphaelgoetz.betterbuild.listeners.block

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakListener(private val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onBlockBreakListener(blockBreakEvent: BlockBreakEvent) {
        val player = blockBreakEvent.player

        if (betterBuild.playerManager.isActiveBuilder(player)) blockBreakEvent.isCancelled = true
        if (player.itemInHand.type == Material.WOODEN_AXE) blockBreakEvent.isCancelled = true
    }
}