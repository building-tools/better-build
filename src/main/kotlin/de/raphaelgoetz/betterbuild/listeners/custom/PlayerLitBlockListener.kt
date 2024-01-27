package de.raphaelgoetz.betterbuild.listeners.custom

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.block.data.Lightable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

data class PlayerLitBlockListener(val betterBuild: BetterBuild) : Listener {

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
        if (data is Lightable) {
            data.isLit = !data.isLit
            block.blockData = data
        }

        playerInteractEvent.isCancelled = true
    }
}