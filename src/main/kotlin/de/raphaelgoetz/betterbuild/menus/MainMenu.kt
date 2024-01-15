package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.BukkitPlayerInventory
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

data class MainMenu(

    private val betterBuild: BetterBuild,
    private val title: Component,
    private val player: Player

) : BukkitPlayerInventory(title, 1) {

    init {
        setWorldItems()
        setPlayerItems()
        setBannerItems()

        setPhysicItems()
        setBuildItems()
        setClipItems()
        setNightVisionItems()

        openInventory(player)
    }

    private fun setWorldItems() {
        this.setSlot(0, ItemBuilder(Material.GRASS_BLOCK).build(), consumer = {

        })
    }

    private fun setPlayerItems() {
        this.setSlot(1, ItemBuilder(Material.PLAYER_HEAD).build(), consumer = {


        })
    }

    private fun setBannerItems() {
        this.setSlot(2, ItemBuilder(Material.GREEN_BANNER).build(), consumer = {


        })
    }

    private fun setPhysicItems() {
        this.setSlot(5, ItemBuilder(Material.GRAVEL).build(), consumer = {

        })
    }

    private fun setBuildItems() {
        this.setSlot(6, ItemBuilder(Material.DIAMOND_AXE).build(), consumer = {
            betterBuild.playerManager.toggleBuildMode(player)
        })
    }

    private fun setClipItems() {
        this.setSlot(7, ItemBuilder(Material.ELYTRA).build(), consumer = {
            betterBuild.playerManager.toggleNoClipMode(player)
        })
    }

    private fun setNightVisionItems() {
        this.setSlot(8, ItemBuilder(Material.ENDER_EYE).build(), consumer = {
            betterBuild.playerManager.toggleNightVision(player)
        })
    }
}