package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.astralis.items.basicItemWithoutMeta
import de.raphaelgoetz.astralis.items.createSmartItem
import de.raphaelgoetz.astralis.items.data.InteractionType
import de.raphaelgoetz.astralis.ui.builder.SmartClick
import de.raphaelgoetz.astralis.ui.data.InventoryRows
import de.raphaelgoetz.astralis.ui.data.InventorySlots
import de.raphaelgoetz.astralis.ui.openPageInventory
import de.raphaelgoetz.astralis.ui.openTransPageInventory
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.meta.SkullMeta
import java.util.function.Consumer

fun Player.openPlayerOverviewMenu(title: String) {

    val players = Bukkit.getOnlinePlayers().map { player ->
        val item = createSmartItem<SkullMeta>(name, Material.PLAYER_HEAD, interactionType = InteractionType.SUCCESS)
        SmartClick(item, onClick(name))
    }

    val inventory = this.openTransPageInventory(
        title,"Players", InventoryRows.ROW6, players, InventorySlots.SLOT1ROW1, InventorySlots.SLOT9ROW5
    ) {
        val left = basicItemWithoutMeta(Material.ARROW)
        val right = basicItemWithoutMeta(Material.ARROW)
        pageLeft(InventorySlots.SLOT1ROW6, left)
        pageRight(InventorySlots.SLOT9ROW6, right)
    }

}

private fun onClick(name: String): Consumer<InventoryClickEvent> {
    return Consumer { event ->
        val player = event.whoClicked as Player
        val target = Bukkit.getPlayer(name)

        if (target == null || !target.isOnline) return@Consumer
        player.teleport(target)
    }

}