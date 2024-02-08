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
            generateWorlds(category, worlds)
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
        }
    }

    private fun generateOverlay() {

        clearSlots()
        val empty = ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("gui.world.item.placeholder.name").build()
        val spawn = ItemBuilder(Material.RESPAWN_ANCHOR).setName("gui.world.item.spawn.name").build()
        val create = ItemBuilder(Material.GRASS_BLOCK).setName("gui.world.item.create.name").build()
        val back = ItemBuilder(Material.STRUCTURE_VOID).setName("gui.world.item.back.name").build()
        val close = ItemBuilder(Material.BARRIER).setName("gui.world.item.close.name").build()
        val archive = ItemBuilder(Material.IRON_DOOR).setName("gui.world.item.archive.name").build()

        this.setSlot(47, empty, onEmptyClick())
        this.setSlot(48, spawn, onSpawnClick())
        this.setSlot(49, create, onCreateWorldClick())
        this.setSlot(50, back, onCloseClick())
        this.setSlot(51, close, onCloseClick())
        this.setSlot(52, archive, onToggleArchive())
        this.setSlot(53, empty, onEmptyClick())
    }

    private fun generateCategories() {
        generateOverlay()

        val categories = betterBuild.worldManager.getCategories(isArchive)
        for (category in categories) {

            category.value.sort()
            val description = getCategoryDescription(category.value)
            val categoryItem = getItemWithURL(Material.NAME_TAG, SkullURL.CATEGORY.url)
                .setName("gui.world.item.category.name", "%category%", category.key)
                .setLore(description).build()

            this.addSlot(categoryItem, onCategoryClick(category.key, category.value))
        }
    }

    private fun generateWorlds(category: String, worlds: List<String>) {
        generateOverlay()

        for (world in worlds) {

            val worldItem = getItemWithURL(Material.GRASS_BLOCK, SkullURL.WORLD.url)
                .setName("gui.world.item.world.name", "%world%", world).build()
            addSlot(worldItem, onWorldClick(world))
        }
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