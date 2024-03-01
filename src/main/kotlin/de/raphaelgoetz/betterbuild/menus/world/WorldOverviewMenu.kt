package de.raphaelgoetz.betterbuild.menus.world

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import de.raphaelgoetz.betterbuild.utils.BukkitPlayerInventory
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import de.raphaelgoetz.betterbuild.utils.SkullURL
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.net.URL
import java.util.function.Consumer

data class WorldOverviewMenu(

    val betterBuild: BetterBuild,
    val player: Player,
    val title: Component,
    var isArchive: Boolean

) : BukkitPlayerInventory(title, 6) {

    init {
        generateCategories()
    }

    fun open() {
        openInventory(player)
    }

    private fun onEmptyClick(): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true
        }
    }

    private fun onWorldClick(worldName: String): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true

            val player = it.whoClicked as Player

            if (it.isRightClick) {
                WorldConfigureMenu(betterBuild, player, Component.text("aaa"), worldName).open()
                return@Consumer
            }

            var enterPermission = betterBuild.worldManager.getWorldPermission(worldName)
            if (enterPermission == "") enterPermission = "betterbuild.enter.free"

            if (!player.hasPermission(enterPermission) && enterPermission != "betterbuild.enter.free") {
                LanguageManager.sendPlayerMessage(player, "event.teleport.permission")
                return@Consumer
            }

            val bukkitWorld = Bukkit.getWorld(worldName)

            if (bukkitWorld == null) {
                betterBuild.worldManager.addPlayerToQueue(worldName, player.uniqueId)
                Bukkit.createWorld(WorldCreator(worldName))
                player.closeInventory()
                return@Consumer
            }

            player.teleport(bukkitWorld.spawnLocation)
            player.closeInventory()
        }
    }

    private fun onCategoryClick(category: String, worlds: List<String>): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true
            generateWorlds(worlds)
        }
    }

    private fun onCloseClick(): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true
            player.closeInventory()
        }
    }

    private fun onBackClick(): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true
            generateCategories()
        }
    }

    private fun onSpawnClick(): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true
            val world = Bukkit.getWorld("world") ?: return@Consumer
            player.teleport(world.spawnLocation)
        }
    }

    private fun onCreateWorldClick(): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true
            WorldCreationMenu(betterBuild, player, Component.text("create new World")).open()
        }
    }

    private fun onToggleArchive(): Consumer<InventoryClickEvent> {
        return Consumer {
            it.isCancelled = true
            isArchive = !isArchive
            generateCategories()
        }
    }

    private fun generateOverlay() {

        clearSlots()
        val empty = ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("gui.world.item.placeholder.name").build()
        val spawn = getItemWithURL(Material.RESPAWN_ANCHOR, SkullURL.GUI_SPAWN.url).setName("gui.world.item.spawn.name").build()
        val create = getItemWithURL(Material.GRASS_BLOCK, SkullURL.GUI_WORLD.url).setName("gui.world.item.create.name").build()
        val back = getItemWithURL(Material.STRUCTURE_VOID, SkullURL.GUI_BACK.url).setName("gui.world.item.back.name").build()
        val close = getItemWithURL(Material.BARRIER, SkullURL.GUI_CLOSE.url).setName("gui.world.item.close.name").build()
        val archive = getItemWithURL(Material.IRON_DOOR, SkullURL.GUI_ARCHIVE.url).setName("gui.world.item.archive.name").build()

        this.setSlot(45, empty, onEmptyClick())
        this.setSlot(46, empty, onEmptyClick())
        this.setSlot(47, spawn, onSpawnClick())
        this.setSlot(48, create, onCreateWorldClick())
        this.setSlot(49, back, onBackClick())
        this.setSlot(50, close, onCloseClick())
        this.setSlot(51, archive, onToggleArchive())
        this.setSlot(52, empty, onEmptyClick())
        this.setSlot(53, empty, onEmptyClick())
    }

    private fun generateCategories() {
        generateOverlay()

        val categories = betterBuild.worldManager.getCategories(isArchive)
        for (category in categories) {

            category.value.sort()
            val description = getCategoryDescription(category.value)
            val categoryItem = getItemWithURL(Material.NAME_TAG, SkullURL.ITEM_CATEGORY.url)
                .setName("gui.world.item.category.name", "%category%", category.key)
                .setLore(description).build()

            this.addSlot(categoryItem, onCategoryClick(category.key, category.value))
        }
    }

    private fun generateWorlds(worlds: List<String>) {
        generateOverlay()
        for (world in worlds) addSlot(getWorldItem(world), onWorldClick(world))
    }

    private fun getWorldItem(world: String): ItemStack {

        val url = when (betterBuild.worldManager.getWorldPermission(world)) {
            "betterbuild.enter.free" -> SkullURL.ITEM_WORLD_1.url
            "betterbuild.enter.low" -> SkullURL.ITEM_WORLD_2.url
            "betterbuild.enter.medium" -> SkullURL.ITEM_WORLD_3.url
            "betterbuild.enter.high" -> SkullURL.ITEM_WORLD_4.url
            else -> SkullURL.ITEM_WORLD_5.url
        }

        val name = if (betterBuild.worldManager.isArchived(world)) "gui.world.item.archive.name" else "gui.world.item.world.name"
        return getItemWithURL(Material.GRASS_BLOCK, url).setName(name, "%world%", world).build()
    }

    private fun getCategoryDescription(worlds: MutableList<String>): List<Component> {
        val description: MutableList<Component> = ArrayList()
        description.add(MiniMessage.miniMessage().deserialize("<gray>Contains for example:</gray>"))

        for (i in worlds.indices) {
            if (i > 10) break
            if (i < 10) description.add(MiniMessage.miniMessage().deserialize("<gray>- " + worlds[i] + "</gray>"))
            if (i == 10) description.add(MiniMessage.miniMessage().deserialize("<gray>... and more ...</gray>"))
        }

        return description
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