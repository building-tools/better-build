package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.utils.BukkitPlayerInventory
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

data class PlayerOverviewMenu(

    val player: Player,
    val title: Component

) : BukkitPlayerInventory(
    title, 6
) {

    init {
        setPlayerItems()
    }

    fun open() {
        this.openInventory(player)
    }

    private fun setPlayerItems() {

        val players = Bukkit.getOnlinePlayers()

        for (player in players) {

            if (player == this.player) continue

            this.addSlot(
                ItemBuilder(Material.PLAYER_HEAD)
                    .setPlayerHead(player)
                    .setName(player.name)
                    .build(),

                consumer = {
                    onClick(it, player.name)
                }

            )
        }
    }

    private fun onClick(inventoryClickEvent: InventoryClickEvent, name: String) {
        val player = inventoryClickEvent.whoClicked as Player
        val target = Bukkit.getPlayer(name)

        if (target == null || !target.isOnline) return
        player.teleport(target)
    }
}