package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.utils.BukkitPlayerInventory
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

data class BannerCreationMenu(

    val player: Player,
    val title: Component

) : BukkitPlayerInventory(title, 6) {

    fun open() {
        this.openInventory(player)
    }
}