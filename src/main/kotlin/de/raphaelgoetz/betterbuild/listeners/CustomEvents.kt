package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listen
import de.raphaelgoetz.betterbuild.manager.isActiveBuilder
import de.raphaelgoetz.betterbuild.utils.blocks.Terracotta
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Lightable
import org.bukkit.block.data.type.TrapDoor
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

fun registerCustomEvents() {

    //iron door interaction
    listen<PlayerInteractEvent> { playerInteractEvent ->
        if (!playerInteractEvent.defaultCheck()) return@listen
        if (playerInteractEvent.item != null) return@listen
        val block = playerInteractEvent.clickedBlock ?: return@listen

        if (block.type != Material.IRON_TRAPDOOR) return@listen
        val data = block.blockData

        if (data is TrapDoor) {
            data.isOpen = !data.isOpen
            println(data.isOpen)
            block.blockData = data
        }

        playerInteractEvent.isCancelled = true

    }

    //Lit unlit candles with hand
    listen<PlayerInteractEvent> { playerInteractEvent ->
        if (!playerInteractEvent.defaultCheck()) return@listen
        if (playerInteractEvent.item != null && playerInteractEvent.item!!.type != Material.FLINT_AND_STEEL) return@listen

        val block = playerInteractEvent.clickedBlock ?: return@listen

        val data = block.blockData
        if (data is Lightable) {
            data.isLit = !data.isLit
            block.blockData = data
        }

        playerInteractEvent.isCancelled = true

    }

    //Rotate terracotta
    listen<PlayerInteractEvent> { playerInteractEvent ->
        if (!playerInteractEvent.defaultCheck()) return@listen
        if (playerInteractEvent.item != null) return@listen

        val block = playerInteractEvent.clickedBlock ?: return@listen
        val data = block.blockData
        if (!block.isTerracotta()) return@listen

        if (data is Directional) {
            data.facing = getNextBlockFace(data.facing)
            block.blockData = data
        }

        playerInteractEvent.isCancelled = true
    }

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

private fun PlayerInteractEvent.defaultCheck(): Boolean {
    if (!player.isActiveBuilder()) return false
    if (this.action != Action.RIGHT_CLICK_BLOCK) return false
    if (!this.action.isRightClick) return false
    if (!this.player.isSneaking) return false
    if (this.hand == EquipmentSlot.OFF_HAND) return false
    return true
}