package de.raphaelgoetz.betterbuild.menus.world

import de.raphaelgoetz.astralis.items.builder.SmartItem
import de.raphaelgoetz.astralis.items.createSmartItem
import de.raphaelgoetz.astralis.items.smartItemWithoutMeta
import de.raphaelgoetz.astralis.ui.builder.InventoryBuilder
import de.raphaelgoetz.astralis.ui.data.InventoryRows
import de.raphaelgoetz.astralis.ui.data.InventorySlots
import de.raphaelgoetz.astralis.ui.openInventory
import de.raphaelgoetz.betterbuild.manager.*
import de.raphaelgoetz.betterbuild.utils.SkullURL
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.meta.SkullMeta
import java.net.URL
import java.util.*
import java.util.function.Consumer

fun Player.openWorldOverviewMenu(title: Component, isArchive: Boolean = false) =
    WorldOverviewMenu(isArchive, this, title).open()

class WorldOverviewMenu(
    private var isArchive: Boolean,
    private val player: Player,
    private val title: Component,
) {

    fun open() {

        player.openInventory(title, InventoryRows.ROW6) {
            generateCategories()
        }

    }

    //Click Events
    private fun onCloseClick(): Consumer<InventoryClickEvent> {
        return Consumer {
            val player = (it.whoClicked as Player)
            player.closeInventory()
        }
    }

    private fun onSpawnClick(): Consumer<InventoryClickEvent> {
        return Consumer {
            val world = Bukkit.getWorld("world") ?: return@Consumer
            val player = (it.whoClicked as Player)
            player.teleport(world.spawnLocation)
        }
    }

    private fun onCreateWorldClick(): Consumer<InventoryClickEvent> {
        return Consumer {
            val player = (it.whoClicked as Player)
            //TODO: WorldCreationMenu gets called here but currently I cant find it
            //WorldCreationMenu(betterBuild, player, Component.text("create new World")).open()
        }
    }

    private fun InventoryBuilder.onCategoryClick(worlds: List<String>): Consumer<InventoryClickEvent> {
        return Consumer {
            generateWorlds(worlds)
        }
    }

    private fun InventoryBuilder.onToggleArchive(): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true
            isArchive = !isArchive
            generateCategories()
        }
    }

    private fun InventoryBuilder.onBackClick(): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true
            generateCategories()
        }
    }

    private fun InventoryBuilder.onWorldClick(worldName: String): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true

            val player = it.whoClicked as Player

            if (it.isRightClick) {
                val bool = !isArchived(worldName)
                toggleWorldArchive(worldName, bool)
                generateCategories()
                return@Consumer
            }

            var enterPermission = getWorldPermission(worldName)
            if (enterPermission == "") enterPermission = "betterbuild.enter.free"

            if (!player.hasPermission(enterPermission) && enterPermission != "betterbuild.enter.free") {
                LanguageManager.sendPlayerMessage(player, "event.teleport.permission")
                return@Consumer
            }

            val bukkitWorld = Bukkit.getWorld(worldName)

            if (bukkitWorld == null) {
                player.uniqueId.addToQueue(worldName)
                Bukkit.createWorld(WorldCreator(worldName))
                player.closeInventory()
                return@Consumer
            }

            player.teleport(bukkitWorld.spawnLocation)
            player.closeInventory()
        }
    }

    //GENERATE INVENTORY ITEMS
    private fun InventoryBuilder.generateOverlay() {
        clear()

        val empty = smartItemWithoutMeta(
            material = Material.BLUE_STAINED_GLASS_PANE,
            name = LanguageManager.getStringFromConfig("gui.world.item.placeholder.name")
        )

        val spawn = getItemWithURL(
            Material.RESPAWN_ANCHOR, SkullURL.GUI_SPAWN.url,
            LanguageManager.getStringFromConfig("gui.world.item.spawn.name")
        )

        val create = getItemWithURL(
            Material.GRASS_BLOCK, SkullURL.GUI_WORLD.url,
            LanguageManager.getStringFromConfig("gui.world.item.create.name")
        )

        val back = getItemWithURL(
            Material.STRUCTURE_VOID, SkullURL.GUI_BACK.url,
            LanguageManager.getStringFromConfig("gui.world.item.back.name")
        )

        val close = getItemWithURL(
            Material.BARRIER, SkullURL.GUI_CLOSE.url,
            LanguageManager.getStringFromConfig("gui.world.item.close.name")
        )

        val archiveName =
            if (isArchive) "gui.world.item.archives.name.active" else "gui.world.item.archives.name.inactive"
        val archive = getItemWithURL(Material.IRON_DOOR, SkullURL.GUI_ARCHIVE.url, archiveName)

        this.setBlockedSlot(InventorySlots.SLOT1ROW6, empty)
        this.setBlockedSlot(InventorySlots.SLOT2ROW6, empty)
        this.setBlockedSlot(InventorySlots.SLOT3ROW6, spawn, onSpawnClick())
        this.setBlockedSlot(InventorySlots.SLOT4ROW6, create, onCreateWorldClick())
        this.setBlockedSlot(InventorySlots.SLOT5ROW6, back, onBackClick())
        this.setBlockedSlot(InventorySlots.SLOT6ROW6, close, onCloseClick())
        this.setBlockedSlot(InventorySlots.SLOT7ROW6, archive, onToggleArchive())
        this.setBlockedSlot(InventorySlots.SLOT8ROW6, empty)
        this.setBlockedSlot(InventorySlots.SLOT9ROW6, empty)
    }

    private fun InventoryBuilder.generateCategories() {
        generateOverlay()

        val categories = getCategories(isArchive)
        for (category in categories) {
            category.value.sort()
            val description = getCategoryDescription(category.value)

            val categoryItem = getItemWithURL(
                Material.NAME_TAG, SkullURL.ITEM_CATEGORY.url,
                LanguageManager.getString("gui.world.item.category.name", "%category%", category.key),
                description
            )

            this.addBlockedSlot(categoryItem, onCategoryClick(category.value))
        }
    }

    private fun InventoryBuilder.generateWorlds(worlds: List<String>) {
        generateOverlay()
        for (world in worlds) addBlockedSlot(getWorldItem(world), onWorldClick(world))
    }

    //GET DATA
    private fun getWorldItem(world: String): SmartItem {
        val url = when (getWorldPermission(world)) {
            "betterbuild.enter.free" -> SkullURL.ITEM_WORLD_1.url
            "betterbuild.enter.low" -> SkullURL.ITEM_WORLD_2.url
            "betterbuild.enter.medium" -> SkullURL.ITEM_WORLD_3.url
            "betterbuild.enter.high" -> SkullURL.ITEM_WORLD_4.url
            else -> SkullURL.ITEM_WORLD_5.url
        }

        val name = if (isArchived(world)) "gui.world.item.archive.name" else "gui.world.item.world.name"
        return getItemWithURL(
            Material.GRASS_BLOCK,
            url,
            LanguageManager.getString(name, "%world%", world),
            LanguageManager.getStringFromConfig("gui.world.item.archive.lore")
        )
    }

    private fun getCategoryDescription(worlds: MutableList<String>): String {
        val description = StringBuilder()
        description.append("Contains: ")
        for (i in worlds.indices) description.append(worlds[i] + ", ")
        return description.toString()
    }

    private fun getItemWithURL(material: Material, url: String, name: String, description: String = ""): SmartItem {
        try {
            val categoryTextureURL = URL(url)
            return createSmartItem<SkullMeta>(name, Material.PLAYER_HEAD, description) {
                val playerProfile = Bukkit.createProfile(UUID.randomUUID())
                val playerTextures = playerProfile.textures
                playerTextures.skin = categoryTextureURL

                this.playerProfile = playerProfile
            }
        } catch (exception: Exception) {
            player.sendMessage("Player Textures couldn't be loaded, so used normal items instead")
            return smartItemWithoutMeta(name, material)
        }
    }
}