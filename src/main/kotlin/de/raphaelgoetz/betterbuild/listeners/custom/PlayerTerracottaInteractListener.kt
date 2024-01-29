package de.raphaelgoetz.betterbuild.listeners.custom

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.blocks.Terracotta
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

data class PlayerTerracottaInteractListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerCampfireInteractionListener(playerInteractEvent: PlayerInteractEvent) {

        val player = playerInteractEvent.player
        if (!betterBuild.playerManager.isActiveBuilder(player)) return

        if (playerInteractEvent.action != Action.RIGHT_CLICK_BLOCK) return
        if (!playerInteractEvent.action.isRightClick) return
        if (!playerInteractEvent.player.isSneaking) return
        if (playerInteractEvent.hand == EquipmentSlot.OFF_HAND) return
        if (playerInteractEvent.item != null) return

        val block = playerInteractEvent.clickedBlock ?: return
        val data = block.blockData
        if (!block.isTerracotta()) return

        if (data is Directional) {
            data.facing = getNextBlockFace(data.facing)
            block.blockData = data
        }

        playerInteractEvent.isCancelled = true
    }

    private fun Block.isTerracotta(): Boolean {

        for (terracotta in Terracotta.entries) {
            if (terracotta.material != this.type) continue
            return true
        }

        return false
    }

    private fun getNextBlockFace(face: BlockFace): BlockFace {
        return when (face) {
            BlockFace.WEST -> BlockFace.NORTH
            BlockFace.NORTH -> BlockFace.EAST
            BlockFace.EAST -> BlockFace.SOUTH
            BlockFace.SOUTH -> BlockFace.WEST
            else -> throw IllegalStateException("Cannot rotate Block!")
        }
    }
}