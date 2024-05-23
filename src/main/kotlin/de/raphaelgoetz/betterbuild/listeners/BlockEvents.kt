package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listen
import de.raphaelgoetz.astralis.event.listenCancelled
import de.raphaelgoetz.betterbuild.manager.cancelWhenBuilder
import de.raphaelgoetz.betterbuild.manager.hasPhysics
import de.raphaelgoetz.betterbuild.manager.isActiveBuilder
import de.raphaelgoetz.betterbuild.manager.isActiveGhost
import de.raphaelgoetz.betterbuild.utils.blocks.Doors
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.event.block.*

fun registerBlockEvents() {

    listen<BlockBreakEvent> { blockBreakEvent ->
        val player = blockBreakEvent.player

        if (!player.isActiveBuilder()) blockBreakEvent.isCancelled = true
        if (player.itemInHand.type == Material.WOODEN_AXE) blockBreakEvent.isCancelled = true
    }

    listen<BlockPhysicsEvent> { blockPhysicsEvent ->
        val material = blockPhysicsEvent.changedBlockData.material

        if (material.isDoor()) return@listen
        val world = blockPhysicsEvent.block.world
        val value = world.hasPhysics()
        blockPhysicsEvent.isCancelled = !value
    }

    listen<BlockPlaceEvent> { blockPlaceEvent ->
        val player = blockPlaceEvent.player
        player.cancelWhenBuilder(blockPlaceEvent)

        if (!player.isActiveGhost()) return@listen
        val data = blockPlaceEvent.block.blockData
        val location = blockPlaceEvent.block.location

        blockPlaceEvent.isCancelled = true
        val blockDisplay = location.world.spawn(location, BlockDisplay::class.java)
        blockDisplay.block = data
    }

    listenCancelled<BlockBurnEvent>()
    listenCancelled<BlockDispenseArmorEvent>()
    listenCancelled<BlockDispenseEvent>()
    listenCancelled<BlockDropItemEvent>()
    listenCancelled<BlockExplodeEvent>()
    listenCancelled<BlockFadeEvent>()
    listenCancelled<BlockGrowEvent>()
    listenCancelled<BlockIgniteEvent>()
    listenCancelled<BlockPistonExtendEvent>()
    listenCancelled<BlockPistonRetractEvent>()
    listenCancelled<BlockReceiveGameEvent>()
    listenCancelled<BlockSpreadEvent>()
    listenCancelled<FluidLevelChangeEvent>()
    listenCancelled<LeavesDecayEvent>()
}

private fun Material.isDoor(): Boolean {

    for (door in Doors.entries) {
        if (door.material != this) continue
        return true
    }

    return false
}