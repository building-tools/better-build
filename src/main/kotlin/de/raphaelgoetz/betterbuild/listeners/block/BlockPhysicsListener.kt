package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPhysicsEvent

class BlockPhysicsListener : Listener {

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
        //if (!bm.getWorldManager().isPhysics()) blockPhysicsEvent.setCancelled(true);
    }
}