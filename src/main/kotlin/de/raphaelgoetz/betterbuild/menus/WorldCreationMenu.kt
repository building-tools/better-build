package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.BukkitPlayerInventory
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
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

) : BukkitPlayerInventory(title, 6) {

    var world: BuildWorld = BuildWorld("Name", BuildWorldTypes.FLAT, World.Environment.NORMAL, false)

    init {
        generateGeneratorItems(false, true, false)
        generateEnvironmentItems(true, false, false)
        generateStructureToggleItems()
        generateNameItems()
    }

    fun open() {
        player.openInventory(this.inventory)
    }

    private fun generateGeneratorItems(isVoid: Boolean, isFlat: Boolean, isNormal: Boolean) {
        this.setSlot(
            0,
            ItemBuilder(Material.GREEN_STAINED_GLASS).setName("Void Generator").build()
        )
        {
            it.isCancelled = true
            world.types = BuildWorldTypes.VOID
            generateGeneratorItems(true, false, false)
        }

        this.setSlot(1, ItemBuilder(Material.GRASS_BLOCK).setName("Flat").build(), consumer = {
            it.isCancelled = true
            world.types = BuildWorldTypes.FLAT
            generateGeneratorItems(false, true, false)
        })

        this.setSlot(2, ItemBuilder(Material.OAK_SAPLING).setName("Normal").build()){
            it.isCancelled = true
            world.types = BuildWorldTypes.NORMAL
            generateGeneratorItems(false, false, true)
        }
    }

    private fun generateEnvironmentItems(isOverword: Boolean, isNether: Boolean, isEnd: Boolean) {

        this.setSlot(
            9,
            ItemBuilder(Material.GRASS_BLOCK).setName("Overworld").setGlowing(isOverword).build(),
            consumer = {
                it.isCancelled = true
                world.environment = World.Environment.NORMAL
                generateEnvironmentItems(true, false, false)
            })

        this.setSlot(10, ItemBuilder(Material.NETHERRACK).setName("Nether").setGlowing(isNether).build(), consumer = {
            it.isCancelled = true
            world.environment = World.Environment.NETHER
            generateEnvironmentItems(false, true, false)
        })

        this.setSlot(11, ItemBuilder(Material.END_STONE).setName("End").setGlowing(isEnd).build(), consumer = {
            it.isCancelled = true
            world.environment = World.Environment.THE_END
            generateEnvironmentItems(false, false, true)
        })
    }

    private fun generateStructureToggleItems() {

        val trueItem = ItemBuilder(Material.GREEN_DYE).setName("with struc").build()
        val falseItem = ItemBuilder(Material.RED_DYE).setName("without stric").build()

        this.setSlot(17, if (world.generateStructures) trueItem else falseItem, consumer = {
            it.isCancelled = true
            world.generateStructures = !world.generateStructures
            generateStructureToggleItems()
        })
    }

    private fun generateNameItems() {
        this.setSlot(26, ItemBuilder(Material.NAME_TAG).setName("Set name").build(), consumer = {
            it.isCancelled = true
            //WorldNameMenu(betterBuild, world, player, Component.text("Create AAA")).open()
        })
    }
}