package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import de.raphaelgoetz.betterbuild.utils.menus.BukkitPlayerInventory
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import java.net.URL

data class WorldOverviewMenu(

    val betterBuild: BetterBuild,
    val player: Player,
    val title: Component

) : BukkitPlayerInventory(title, 6) {

    init {
        generateCategories()
    }

    fun open() {
        openInventory(player)
    }

    private fun generateCategories() {
        clearSlots()
        val categories = categories

        for (index in 45..53) {
            this.setSlot(index,
                ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(betterBuild.languageManager.getComponent("gui.world.item.placeholder.name"))
                    .build(),
                consumer = { inventoryClickEvent ->
                    inventoryClickEvent.isCancelled = true
                })
        }

        this.setSlot(
            48, ItemBuilder(Material.RESPAWN_ANCHOR)
                .setName(betterBuild.languageManager.getComponent("gui.world.item.spawn.name")).build()
        ) { inventoryClickEvent ->
            inventoryClickEvent.isCancelled = true
            val world = Bukkit.getWorld("world")
            if (world != null) {
                player.teleport(world.spawnLocation)
            }
        }

        /*
    this.setSlot(49, ItemBuilder(Material.GRASS_BLOCK)
        .setName(betterBuild.languageManager.getComponent("gui.world.item.create.name")).build(), consumer =  {
        it.isCancelled = true
        WorldCreationMenu(betterBuild, player, Component.text("create")).open()
    })
     */

        this.setSlot(
            50,
            ItemBuilder(Material.BARRIER).setName(betterBuild.languageManager.getComponent("gui.world.item.close.name"))
                .build()
        ) { inventoryClickEvent ->
            inventoryClickEvent.isCancelled = true
            player.closeInventory()
        }

        categories.forEach() { (category, worlds) ->
            worlds.sort()

            val lores: MutableList<Component> = ArrayList()
            lores.add(Component.text("Contains for example:"))

            for (i in worlds.indices) {
                if (i > 10) break
                if (i < 10) lores.add(Component.text("- " + worlds[i]))
                if (i == 10) lores.add(Component.text("... and more ..."))
            }

            //Creating the category items in the category inventory
            this.addSlot(getItemWithURL(
                Material.NAME_TAG,
                "http://textures.minecraft.net/texture/56330a4a22ff55871fc8c618e421a37733ac1dcab9c8e1a4bb73ae645a4a4e"
            )
                .setName(
                    betterBuild.languageManager.getComponent(
                        "gui.world.item.category.name",
                        "%category%",
                        category
                    )
                )
                .setLore(lores).build(),

                consumer = { inventoryClickEvent ->
                    inventoryClickEvent.isCancelled = true
                    generateCategory(category, worlds)
                })
        }
    }

    private fun generateCategory(category: String?, content: List<String>) {
        clearSlots()
        val worlds: MutableList<String> = ArrayList()
        val originalNames: MutableMap<String, String> = HashMap()

        content.forEach { string ->

            var newName = string
            if (newName.contains("_")) newName = newName.replace("_", "")
            if (newName.contains(category!!)) newName = newName.replace(category, "")
            worlds.add(newName)
            originalNames[newName] = string
        }

        for (index in 45..53) {
            this.setSlot(
                index,
                ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(betterBuild.languageManager.getComponent("gui.world.item.placeholder.name"))
                    .build()
            )
            { inventoryClickEvent -> inventoryClickEvent.isCancelled = true }
        }

        this.setSlot(
            49,
            ItemBuilder(Material.STRUCTURE_VOID).setName(betterBuild.languageManager.getComponent("gui.world.item.back.name"))
                .build()
        ) { inventoryClickEvent ->
            inventoryClickEvent.isCancelled = true
            generateCategories()
        }

        for (world in worlds) {
            this.addSlot(getItemWithURL(
                Material.GRASS_BLOCK,
                "http://textures.minecraft.net/texture/438cf3f8e54afc3b3f91d20a49f324dca1486007fe545399055524c17941f4dc"
            )
                .setName(betterBuild.languageManager.getComponent("gui.world.item.world.name", "%world%", world))
                .build(),

                consumer = { inventoryClickEvent ->
                    inventoryClickEvent.isCancelled = true
                    player.closeInventory()
                    val bukkitWorld = Bukkit.getWorld(world)
                    if (bukkitWorld == null) {
                        betterBuild.worldManager.addPlayerToQueue(world, player.uniqueId)
                        Bukkit.createWorld(WorldCreator(world))
                    } else player.teleport(bukkitWorld.spawnLocation)
                })
        }
    }

    /*
    If a world contains an "_" in its name, a category by the name of the char-sequence before the "_" will be
    created to sort all the worlds by its first word. The string gets automatically put in its responding category
    if its starts with the same char-sequence as a category
    */
    private val categories: Map<String, MutableList<String>>
        get() {
            val result: MutableMap<String, MutableList<String>> = HashMap()
            val worldNames: Collection<String> = betterBuild.worldManager.getWorldNames()

            if (worldNames.isEmpty()) return result

            val categories: MutableCollection<String> = ArrayList()
            val categoryLessWorld: MutableList<String> = ArrayList()

            worldNames.forEach { worldName: String ->
                if (worldName.contains("_")) {
                    val rest = worldName.substring(0, worldName.indexOf("_"))
                    if (!categories.contains(rest)) categories.add(rest)
                } else categoryLessWorld.add(worldName)
            }

            categories.forEach { category: String ->
                val contents: MutableList<String> = ArrayList()

                worldNames.forEach { worldName: String ->
                    if (worldName.startsWith(category) && worldName.contains("_")) contents.add(worldName)
                }
                result[category] = contents
            }

            result["NONE"] = categoryLessWorld
            return result
        }

    private fun getItemWithURL(material: Material, url: String): ItemBuilder {

        try {

            val categoryTextureURL = URL(url)
            return ItemBuilder(Material.PLAYER_HEAD).setPlayerHeadTexture(categoryTextureURL)

        } catch (exception: Exception) {

            player.sendMessage("Player Textures couldn't be loaded, so used normal items instead")
            return ItemBuilder(material)
        }
    }
}