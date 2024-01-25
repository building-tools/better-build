package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.menus.BukkitAnvilInventory
import de.raphaelgoetz.betterbuild.utils.menus.BukkitAnvilInventorySlots
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import de.raphaelgoetz.betterbuild.world.BuildWorld
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

class WorldNameMenu(

    val betterBuild: BetterBuild,
    val buildWorld: BuildWorld,
    val player: Player,
    val title: Component,

) : BukkitAnvilInventory(title) {

    init {
        setItems()
    }

    fun open() {
        this.open(player)
    }

    private fun setItems() {

        this.setSlot(BukkitAnvilInventorySlots.FIRST, ItemBuilder(Material.RED_DYE).build(), consumer = {
            it.isCancelled = true
            player.closeInventory()
            player.sendMessage("canceled")
        })

        this.setSlot(BukkitAnvilInventorySlots.SECOND, ItemBuilder(Material.GREEN_DYE).build(), consumer = {
            it.isCancelled = true
            player.closeInventory()
            player.sendMessage(this.getText())
        })
    }
}