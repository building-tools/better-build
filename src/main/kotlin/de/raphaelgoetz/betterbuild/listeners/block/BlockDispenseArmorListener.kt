package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseArmorEvent

class BlockDispenseArmorListener : Listener {

    @EventHandler
    fun onBlockDispenseArmorEvent(blockDispenseArmorEvent: BlockDispenseArmorEvent) {
        blockDispenseArmorEvent.isCancelled = true
    }
}