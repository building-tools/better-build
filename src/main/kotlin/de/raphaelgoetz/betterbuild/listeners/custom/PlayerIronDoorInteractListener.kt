package de.raphaelgoetz.betterbuild.listeners.custom

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Material
import org.bukkit.block.data.type.TrapDoor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

data class PlayerIronDoorInteractListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerIronDoorInteractEvent(playerInteractEvent: PlayerInteractEvent) {

        val player = playerInteractEvent.player
        if (!betterBuild.playerManager.isActiveBuilder(player)) return

        if (playerInteractEvent.action != Action.RIGHT_CLICK_BLOCK) return
        if (!playerInteractEvent.action.isRightClick) return
        if (!playerInteractEvent.player.isSneaking) return
        if (playerInteractEvent.hand == EquipmentSlot.OFF_HAND) return
        if (playerInteractEvent.item != null) return
        val block = playerInteractEvent.clickedBlock ?: return

        if (block.type != Material.IRON_TRAPDOOR) return
        val data = block.blockData

        if (data is TrapDoor) {
            data.isOpen = !data.isOpen
            println(data.isOpen)
            block.blockData = data
        }

        playerInteractEvent.isCancelled = true
    }
}