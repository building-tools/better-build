package de.raphaelgoetz.betterbuild.listeners.block

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.blocks.Doors
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPhysicsEvent

data class BlockPhysicsListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onBlockPhysicsEvent(blockPhysicsEvent: BlockPhysicsEvent) {
        val material = blockPhysicsEvent.changedBlockData.material

        if (material.isDoor()) return
        val world = blockPhysicsEvent.block.world
        val value = betterBuild.worldManager.hasPhysics(world)
        blockPhysicsEvent.isCancelled = !value
    }

    private fun Material.isDoor(): Boolean {

        for (door in Doors.entries) {
            if (door.material != this) continue
            return true
        }

        return false
    }
}