package de.raphaelgoetz.betterbuild.listeners.block

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPhysicsEvent

data class BlockPhysicsListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onBlockPhysicsEvent(blockPhysicsEvent: BlockPhysicsEvent) {
        val material = blockPhysicsEvent.changedBlockData.material
        val materials = ArrayList<Any>()

        materials.add(Material.OAK_DOOR)
        materials.add(Material.SPRUCE_DOOR)
        materials.add(Material.DARK_OAK_DOOR)
        materials.add(Material.BIRCH_DOOR)
        materials.add(Material.JUNGLE_DOOR)
        materials.add(Material.ACACIA_DOOR)
        materials.add(Material.IRON_DOOR)
        materials.add(Material.OAK_DOOR)
        materials.add(Material.CRIMSON_DOOR)
        materials.add(Material.WARPED_DOOR)

        if (materials.contains(material)) return
        val world = blockPhysicsEvent.block.world
        val value = betterBuild.worldManager.hasPhysics(world)
        blockPhysicsEvent.isCancelled = !value
    }
}