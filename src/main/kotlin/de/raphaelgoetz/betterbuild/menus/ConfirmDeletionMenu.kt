package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.menus.BukkitPlayerInventory
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

        setSlot(3, ItemBuilder(Material.GREEN_DYE)
                .setName(betterBuild.languageManager.getComponent("gui.deletion.item.confirm.name"))
                .setLore(betterBuild.languageManager.getComponents("gui.deletion.item.confirm.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.worldManager.deleteWorld(name)
                betterBuild.languageManager.sendPlayerMessage(player, "gui.message.world.delete")
                player.closeInventory()
            })
    }

    private fun setCancelItem() {

        setSlot(5, ItemBuilder(Material.RED_DYE)
                .setName(betterBuild.languageManager.getComponent("gui.deletion.item.delete.name"))
                .setLore(betterBuild.languageManager.getComponents("gui.deletion.item.delete.lore")).build(),

            consumer = {
                it.isCancelled = true
                player.closeInventory()
                betterBuild.languageManager.sendPlayerMessage(player, "gui.message.world.delete.error")
            })
    }
}