package de.raphaelgoetz.betterbuild.menus.world

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.BukkitPlayerInventory
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.function.Consumer

data class WorldConfigureMenu(

    val betterBuild: BetterBuild,
    val player: Player,
    val title: Component,
    val worldName: String

) : BukkitPlayerInventory(title, 1) {

    init {
        setArchiveItems()
    }

    fun open() {
        this.openInventory(player)
    }

    private fun setArchiveItems() {

        val click: Consumer<InventoryClickEvent> = Consumer {
            it.isCancelled = true
            val bool = !betterBuild.worldManager.isArchived(worldName)
            betterBuild.worldManager.toggleWorldArchive(worldName, bool)
            setArchiveItems()
        }

        val item = if (betterBuild.worldManager.isArchived(worldName)) ItemBuilder(Material.GREEN_DYE).setName("archive_true") else ItemBuilder(Material.RED_DYE).setName("archive_false")
        setSlot(0, item.build(), click)
    }

    private fun setPermissionItems() {

    }

    private fun setEnterItems() {

    }

    private fun setUnloadItems() {

    }

    private fun setDeleteItems() {

    }

    private fun setBackItems() {

    }
}