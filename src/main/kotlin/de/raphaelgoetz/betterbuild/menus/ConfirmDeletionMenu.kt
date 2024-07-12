package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.astralis.items.data.InteractionType
import de.raphaelgoetz.astralis.items.smartItemWithoutMeta
import de.raphaelgoetz.astralis.text.communication.CommunicationType
import de.raphaelgoetz.astralis.text.sendText
import de.raphaelgoetz.astralis.ui.data.InventoryRows
import de.raphaelgoetz.astralis.ui.data.InventorySlots
import de.raphaelgoetz.astralis.ui.openInventory
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import de.raphaelgoetz.betterbuild.manager.deleteWorld
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

fun Player.openConfirmWorldDeletionMenu(name: String, title: Component) {

    val inventoryPlayer = this

    val confirm = smartItemWithoutMeta(
        name = LanguageManager.getStringFromConfig("gui.deletion.item.confirm.name"),
        description = LanguageManager.getStringFromConfig("gui.deletion.item.confirm.lore"),
        material = Material.GREEN_DYE,
        interactionType = InteractionType.SUCCESS
    )

    val cancel = smartItemWithoutMeta(
        name = LanguageManager.getStringFromConfig("gui.deletion.item.delete.name"),
        description = LanguageManager.getStringFromConfig("gui.deletion.item.delete.lore"),
        material = Material.RED_DYE,
        interactionType = InteractionType.ERROR
    )

    this.openInventory(title, InventoryRows.ROW1) {

        setBlockedSlot(InventorySlots.SLOT3ROW1, confirm) {
            deleteWorld(name)
            val message = LanguageManager.getStringFromConfig("gui.message.world.delete")
            inventoryPlayer.sendText(message) {
                type = CommunicationType.SUCCESS
            }

            inventoryPlayer.closeInventory()
        }

        setBlockedSlot(InventorySlots.SLOT6ROW1, cancel) {
            val message = LanguageManager.getStringFromConfig("gui.message.world.delete.error")

            inventoryPlayer.sendText(message) {
                type = CommunicationType.ERROR
            }

            inventoryPlayer.closeInventory()
        }

    }

}