package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.astralis.items.basicSmartTransItem
import de.raphaelgoetz.astralis.items.data.InteractionType
import de.raphaelgoetz.astralis.text.communication.CommunicationType
import de.raphaelgoetz.astralis.text.translation.sendTransText
import de.raphaelgoetz.astralis.ui.data.InventoryRows
import de.raphaelgoetz.astralis.ui.data.InventorySlots
import de.raphaelgoetz.astralis.ui.openTransInventory
import de.raphaelgoetz.betterbuild.manager.deleteWorld
import org.bukkit.Material
import org.bukkit.entity.Player

fun Player.openConfirmWorldDeletionMenu(name: String, title: String) {

    val inventoryPlayer = this

    val confirm = basicSmartTransItem(
        key = "gui.deletion.item.confirm.name",
        descriptionKey = "gui.deletion.item.confirm.lore",
        material = Material.GREEN_DYE,
        interactionType = InteractionType.SUCCESS
    )

    val cancel = basicSmartTransItem(
        key = "gui.deletion.item.delete.name",
        descriptionKey = "gui.deletion.item.delete.lore",
        material = Material.RED_DYE,
        interactionType = InteractionType.ERROR
    )

    this.openTransInventory(title, rows = InventoryRows.ROW1) {

        setBlockedSlot(InventorySlots.SLOT3ROW1, confirm) {
            deleteWorld(name)
            inventoryPlayer.sendTransText("gui.message.world.delete") {
                type = CommunicationType.SUCCESS
            }

            inventoryPlayer.closeInventory()
        }

        setBlockedSlot(InventorySlots.SLOT6ROW1, cancel) {
            inventoryPlayer.sendTransText("gui.message.world.delete.error") {
                type = CommunicationType.ERROR
            }

            inventoryPlayer.closeInventory()
        }

    }

}