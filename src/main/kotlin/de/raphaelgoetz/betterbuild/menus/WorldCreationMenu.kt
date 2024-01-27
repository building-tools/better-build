package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import de.raphaelgoetz.betterbuild.utils.menus.BukkitPlayerInventory
import de.raphaelgoetz.betterbuild.world.BuildWorld
import de.raphaelgoetz.betterbuild.world.BuildWorldTypes
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player

data class WorldCreationMenu(

    val betterBuild: BetterBuild,
    val player: Player,
    val title: Component

) : BukkitPlayerInventory(title, 1) {

    var world: BuildWorld = BuildWorld("Name", BuildWorldTypes.FLAT, World.Environment.NORMAL, false)

    init {
        generateGeneratorItems(false, true, false)
        generateEnvironmentItems(true, false, false)
        generateStructureToggleItems()
        generateNameItems()
    }

    fun open() {
        this.openInventory(player)
    }

    private fun generateGeneratorItems(isVoid: Boolean, isFlat: Boolean, isNormal: Boolean) {

        if (isVoid) {
            this.setSlot(0, ItemBuilder(Material.GREEN_STAINED_GLASS)
                .setName(betterBuild.languageManager.getComponent("gui.world.item.void.name"))
                .setLore(betterBuild.languageManager.getComponents("gui.world.item.void.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.types = BuildWorldTypes.VOID
                    generateGeneratorItems(true, false, false)
                })

            return
        }

        if (isFlat) {
            this.setSlot(0, ItemBuilder(Material.GRASS_BLOCK)
                .setName(betterBuild.languageManager.getComponent("gui.world.item.flat.name"))
                .setLore(betterBuild.languageManager.getComponents("gui.world.item.flat.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.types = BuildWorldTypes.FLAT
                    generateGeneratorItems(false, true, false)
                })
            return
        }

        if(isNormal) {
            this.setSlot(0, ItemBuilder(Material.OAK_SAPLING)
                .setName(betterBuild.languageManager.getComponent("gui.world.item.land.name"))
                .setLore(betterBuild.languageManager.getComponents("gui.world.item.land.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.types = BuildWorldTypes.NORMAL
                    generateGeneratorItems(false, false, true)
                })
            return
        }
    }

    private fun generateEnvironmentItems(isOverword: Boolean, isNether: Boolean, isEnd: Boolean) {

        if (isOverword) {
            this.setSlot(1, ItemBuilder(Material.GRASS_BLOCK)
                .setName(betterBuild.languageManager.getComponent("gui.world.item.normal.name"))
                .setLore(betterBuild.languageManager.getComponents("gui.world.item.normal.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.environment = World.Environment.NORMAL
                    generateEnvironmentItems(true, false, false)
                })
            return
        }

        if(isNether) {
            this.setSlot(1, ItemBuilder(Material.NETHERRACK)
                .setName(betterBuild.languageManager.getComponent("gui.world.item.nether.name"))
                .setLore(betterBuild.languageManager.getComponents("gui.world.item.nether.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.environment = World.Environment.NETHER
                    generateEnvironmentItems(false, true, false)
                })

            return
        }

        if (isEnd) {
            this.setSlot(1, ItemBuilder(Material.END_STONE)
                .setName(betterBuild.languageManager.getComponent("gui.world.item.end.name"))
                .setLore(betterBuild.languageManager.getComponents("gui.world.item.end.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.environment = World.Environment.THE_END
                    generateEnvironmentItems(false, false, true)
                })

            return
        }

    }

    private fun generateStructureToggleItems() {

        val trueItem = ItemBuilder(Material.GREEN_DYE)
            .setName(betterBuild.languageManager.getComponent("gui.world.item.true.name"))
            .setLore(betterBuild.languageManager.getComponents("gui.world.item.true.lore")).build()

        val falseItem = ItemBuilder(Material.RED_DYE)
            .setName(betterBuild.languageManager.getComponent("gui.world.item.false.name"))
            .setLore(betterBuild.languageManager.getComponents("gui.world.item.false.lore")).build()

        this.setSlot(2, if (world.generateStructures) trueItem else falseItem, consumer = {
            it.isCancelled = true
            world.generateStructures = !world.generateStructures
            generateStructureToggleItems()
        })
    }

    private fun generateNameItems() {
        this.setSlot(8, ItemBuilder(Material.NAME_TAG)
            .setName(betterBuild.languageManager.getComponent("gui.world.item.name.name"))
            .setLore(betterBuild.languageManager.getComponents("gui.world.item.name.lore")).build(),

            consumer = {
                it.isCancelled = true
                betterBuild.languageManager.sendPlayerMessage(player, "gui.world.message.create")
                betterBuild.worldManager.worldCreation[player.uniqueId] = this.world
                player.closeInventory()
            })
    }
}