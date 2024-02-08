package de.raphaelgoetz.betterbuild.menus.world

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import de.raphaelgoetz.betterbuild.utils.BukkitPlayerInventory
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
                .setName(("gui.world.item.void.name"))
                .setLore(("gui.world.item.generator.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.types = BuildWorldTypes.VOID
                    generateGeneratorItems(false, true, false)
                })

            return
        }

        if (isFlat) {
            this.setSlot(0, ItemBuilder(Material.GRASS_BLOCK)
                .setName(("gui.world.item.flat.name"))
                .setLore(("gui.world.item.generator.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.types = BuildWorldTypes.FLAT
                    generateGeneratorItems(false, false, true)
                })
            return
        }

        if(isNormal) {
            this.setSlot(0, ItemBuilder(Material.OAK_SAPLING)
                .setName(("gui.world.item.land.name"))
                .setLore(("gui.world.item.generator.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.types = BuildWorldTypes.NORMAL
                    generateGeneratorItems(true, false, false)
                })
            return
        }
    }

    private fun generateEnvironmentItems(isOverword: Boolean, isNether: Boolean, isEnd: Boolean) {

        if (isOverword) {
            this.setSlot(1, ItemBuilder(Material.GRASS_BLOCK)
                .setName(("gui.world.item.normal.name"))
                .setLore(("gui.world.item.environment.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.environment = World.Environment.NORMAL
                    generateEnvironmentItems(false, true, false)
                })
            return
        }

        if(isNether) {
            this.setSlot(1, ItemBuilder(Material.NETHERRACK)
                .setName(("gui.world.item.nether.name"))
                .setLore(("gui.world.item.environment.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.environment = World.Environment.NETHER
                    generateEnvironmentItems(false, false, true)
                })

            return
        }

        if (isEnd) {
            this.setSlot(1, ItemBuilder(Material.END_STONE)
                .setName(("gui.world.item.end.name"))
                .setLore(("gui.world.item.environment.lore")).build(),

                consumer = {
                    it.isCancelled = true
                    world.environment = World.Environment.THE_END
                    generateEnvironmentItems(true, false, false)
                })

            return
        }

    }

    private fun generateStructureToggleItems() {

        val trueItem = ItemBuilder(Material.GREEN_DYE)
            .setName(("gui.world.item.true.name"))
            .setLore("gui.world.item.toggle.lore").build()

        val falseItem = ItemBuilder(Material.RED_DYE)
            .setName(("gui.world.item.false.name"))
            .setLore("gui.world.item.toggle.lore").build()

        this.setSlot(2, if (world.generateStructures) trueItem else falseItem, consumer = {
            it.isCancelled = true
            world.generateStructures = !world.generateStructures
            generateStructureToggleItems()
        })
    }

    private fun generateNameItems() {
        this.setSlot(8, ItemBuilder(Material.NAME_TAG)
            .setName(("gui.world.item.name.name"))
            .setLore("gui.world.item.name.lore").build(),

            consumer = {
                it.isCancelled = true
                LanguageManager.sendPlayerMessage(player, "gui.world.message.create")
                betterBuild.worldManager.worldCreation[player.uniqueId] = this.world
                player.closeInventory()
            })
    }
}