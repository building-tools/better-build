package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import de.raphaelgoetz.betterbuild.menus.world.WorldOverviewMenu
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
        setNightVisionItems()

        openInventory(player)
    }

    private fun setWorldItems() {
        this.setSlot(1, ItemBuilder(Material.GRASS_BLOCK)
            .setName(("gui.main.item.world.name"))
            .setLore(("gui.main.item.world.lore")).build(),

            consumer = {
                it.isCancelled = true
                WorldOverviewMenu(betterBuild, player, LanguageManager.getComponent("gui.world.title"), false).open()
        })
    }

    private fun setPlayerItems() {
        this.setSlot(2, ItemBuilder(Material.PLAYER_HEAD).setPlayerHead(player)
            .setName(("gui.player.item.world.name"))
            .setLore(("gui.player.item.world.lore")).build(),

            consumer = {
                it.isCancelled = true
                PlayerOverviewMenu(player, LanguageManager.getComponent("gui.player.title")).open()
        })
    }

    private fun setBannerItems() {
        this.setSlot(3, ItemBuilder(Material.GREEN_BANNER)
            .setName(("gui.banner.item.world.name"))
            .setLore(("gui.banner.item.world.lore")).build(),

            consumer = {
                it.isCancelled = true
                BannerCreationMenu(player, LanguageManager.getComponent("gui.banner.title")).open()
        })
    }

    private fun setPhysicItems() {

        val name = if (betterBuild.worldManager.hasPhysics(player.world)) "gui.main.item.physics.enable.name" else "gui.main.item.physics.disable.name"
        this.setSlot(5, ItemBuilder(Material.GRAVEL)
            .setName((name))
            .setLore(("gui.main.item.physics.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.worldManager.togglePhysics(player.world)
                setPhysicItems()
            })
    }

    private fun setBuildItems() {

        val name = if (betterBuild.playerManager.isActiveBuilder(player)) "gui.main.item.build.enable.name" else "gui.main.item.build.disable.name"
        this.setSlot(6, ItemBuilder(Material.DIAMOND_AXE)
            .setName((name))
            .setLore(("gui.main.item.build.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.playerManager.toggleBuildMode(player)
                setBuildItems()
        })
    }

    private fun setClipItems() {

        val name = if (betterBuild.playerManager.isActiveNoClip(player)) "gui.main.item.clip.enable.name" else "gui.main.item.clip.disable.name"
        this.setSlot(7, ItemBuilder(Material.ELYTRA)
            .setName((name))
            .setLore(("gui.main.item.clip.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.playerManager.toggleNoClipMode(player)
                setClipItems()
            })
    }

    private fun setNightVisionItems() {

        val name = if (betterBuild.playerManager.hasActiveNightVision(player)) "gui.main.item.night.enable.name" else "gui.main.item.night.disable.name"
        this.setSlot(7, ItemBuilder(Material.ENDER_EYE)
            .setName((name))
            .setLore(("gui.main.item.night.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.playerManager.toggleNightVision(player)
                setNightVisionItems()
            })
    }
}