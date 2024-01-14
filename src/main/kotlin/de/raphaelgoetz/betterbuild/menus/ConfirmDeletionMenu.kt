package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.BukkitPlayerInventory
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

data class ConfirmDeletionMenu(

    private val betterBuild: BetterBuild,
    private val player: Player,
    private val name: String,
    private val title: Component

) : BukkitPlayerInventory(title, 1) {

    init {
        setConfirmItem()
        setCancelItem()
        openInventory(player)
    }

    private fun setConfirmItem() {

        setSlot(2, ItemBuilder(Material.GREEN_DYE).setName("Confirm delete").build(), consumer = {
            it.isCancelled = true
            betterBuild.worldManager.deleteWorld(name)
            player.sendMessage("world has been deleted")
        })

    }

    private fun setCancelItem() {

        setSlot(4, ItemBuilder(Material.RED_DYE).setName("Cancel delete").build(), consumer = {
            it.isCancelled = true
            player.closeInventory()
            player.sendMessage("world hasn't been deleted")
        })
    }
}