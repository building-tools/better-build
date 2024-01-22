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
        this.setSlot(0, ItemBuilder(Material.GRASS_BLOCK)
            .setName(betterBuild.languageManager.getComponent("gui.main.item.world.name"))
            .setLore(betterBuild.languageManager.getComponents("gui.main.item.world.lore")).build(),

            consumer = {
                it.isCancelled = true
                WorldOverviewMenu(betterBuild, player, betterBuild.languageManager.getComponent("gui.world.title")).open()
        })
    }

    private fun setPlayerItems() {
        this.setSlot(1, ItemBuilder(Material.PLAYER_HEAD).setPlayerHead(player)
            .setName(betterBuild.languageManager.getComponent("gui.player.item.world.name"))
            .setLore(betterBuild.languageManager.getComponents("gui.player.item.world.lore")).build(),

            consumer = {
                it.isCancelled = true
                PlayerOverviewMenu(player, betterBuild.languageManager.getComponent("gui.player.title")).open()
        })
    }

    private fun setBannerItems() {
        this.setSlot(2, ItemBuilder(Material.GREEN_BANNER)
            .setName(betterBuild.languageManager.getComponent("gui.banner.item.world.name"))
            .setLore(betterBuild.languageManager.getComponents("gui.banner.item.world.lore")).build(),

            consumer = {
                it.isCancelled = true
                BannerCreationMenu(player, betterBuild.languageManager.getComponent("gui.banner.title")).open()
        })
    }

    private fun setPhysicItems() {

        val name = if (betterBuild.worldManager.hasPhysics(player.world)) "gui.main.item.physics.enable.name" else "gui.main.item.physics.disable.name"
        this.setSlot(5, ItemBuilder(Material.GRAVEL)
            .setName(betterBuild.languageManager.getComponent(name))
            .setLore(betterBuild.languageManager.getComponents("gui.main.item.physics.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.worldManager.togglePhysics(player.world)
            })
    }

    private fun setBuildItems() {

        val name = if (betterBuild.worldManager.hasPhysics(player.world)) "gui.main.item.build.enable.name" else "gui.main.item.build.disable.name"
        this.setSlot(6, ItemBuilder(Material.DIAMOND_AXE)
            .setName(betterBuild.languageManager.getComponent(name))
            .setLore(betterBuild.languageManager.getComponents("gui.main.item.build.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.playerManager.toggleBuildMode(player)
        })
    }

    private fun setClipItems() {

        val name = if (betterBuild.worldManager.hasPhysics(player.world)) "gui.main.item.clip.enable.name" else "gui.main.item.clip.disable.name"
        this.setSlot(7, ItemBuilder(Material.ELYTRA)
            .setName(betterBuild.languageManager.getComponent(name))
            .setLore(betterBuild.languageManager.getComponents("gui.main.item.clip.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.playerManager.toggleNoClipMode(player)
            })
    }

    private fun setNightVisionItems() {

        val name = if (betterBuild.worldManager.hasPhysics(player.world)) "gui.main.item.night.enable.name" else "gui.main.item.night.disable.name"
        this.setSlot(8, ItemBuilder(Material.ENDER_EYE)
            .setName(betterBuild.languageManager.getComponent(name))
            .setLore(betterBuild.languageManager.getComponents("gui.main.item.night.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.playerManager.toggleNightVision(player)
            })
    }
}