package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.astralis.items.createSmartItem
import de.raphaelgoetz.astralis.items.data.InteractionType
import de.raphaelgoetz.astralis.items.smartItemWithoutMeta
import de.raphaelgoetz.astralis.ui.data.InventoryRows
import de.raphaelgoetz.astralis.ui.data.InventorySlots
import de.raphaelgoetz.astralis.ui.openInventory
import de.raphaelgoetz.betterbuild.manager.*
import de.raphaelgoetz.betterbuild.menus.world.openWorldOverviewMenu
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.SkullMeta

fun Player.openMainMenu(title: Component) {

    val inventoryHolder = this

    this.openInventory(title, InventoryRows.ROW1) {

        val worldItem = smartItemWithoutMeta(
            name = LanguageManager.getStringFromConfig("gui.main.item.world.name"),
            description = LanguageManager.getStringFromConfig("gui.main.item.world.lore"),
            material = Material.GRASS_BLOCK,
            interactionType = InteractionType.CLICK
        )

        val playerItem = createSmartItem<SkullMeta>(
            name = LanguageManager.getStringFromConfig("gui.player.item.world.name"),
            description = LanguageManager.getStringFromConfig("gui.player.item.world.lore"),
            material = Material.PLAYER_HEAD,
            interactionType = InteractionType.CLICK
        ) { owningPlayer = inventoryHolder }

        val bannerItem = smartItemWithoutMeta(
            name = LanguageManager.getStringFromConfig("gui.banner.item.world.name"),
            description = LanguageManager.getStringFromConfig("gui.banner.item.world.lore"),
            material = Material.GRASS_BLOCK,
            interactionType = InteractionType.CLICK
        )

        this.setBlockedSlot(InventorySlots.SLOT1ROW1, worldItem) {
            inventoryHolder.openWorldOverviewMenu(LanguageManager.getComponent("gui.world.title"))
        }

        this.setBlockedSlot(InventorySlots.SLOT2ROW1, playerItem) {
            inventoryHolder.openPlayerOverviewMenu(LanguageManager.getComponent("gui.player.title"))
        }

        this.setBlockedSlot(InventorySlots.SLOT3ROW1, bannerItem) {
            inventoryHolder.openBannerCreationMenu(LanguageManager.getComponent("gui.banner.title"))
        }

        fun setPhysicItems() {

            val world = inventoryHolder.world
            val name =
                if (world.hasPhysics()) "gui.main.item.physics.enable.name" else "gui.main.item.physics.disable.name"

            val item = smartItemWithoutMeta(
                name = name,
                description = LanguageManager.getStringFromConfig("gui.main.item.physics.lore"),
                material = Material.GRAVEL,
                interactionType = InteractionType.CLICK
            )

            this.setBlockedSlot(InventorySlots.SLOT5ROW1, item) {
                world.togglePhysics()
                setPhysicItems()
            }
        }

        fun setBuildItems() {

            val name =
                if (inventoryHolder.isActiveBuilder()) "gui.main.item.build.enable.name" else "gui.main.item.build.disable.name"

            val item = smartItemWithoutMeta(
                name = name,
                description = LanguageManager.getStringFromConfig("gui.main.item.build.lore"),
                material = Material.DIAMOND_AXE,
                interactionType = InteractionType.CLICK
            )

            this.setBlockedSlot(InventorySlots.SLOT6ROW1, item) {
                inventoryHolder.toggleBuildMode()
                setBuildItems()
            }
        }

        fun setClipItems() {

            val name =
                if (inventoryHolder.isActiveNoClip()) "gui.main.item.clip.enable.name" else "gui.main.item.clip.disable.name"

            val item = smartItemWithoutMeta(
                name = name,
                description = LanguageManager.getStringFromConfig("gui.main.item.clip.lore"),
                material = Material.ELYTRA,
                interactionType = InteractionType.CLICK
            )

            this.setBlockedSlot(InventorySlots.SLOT7ROW1, item) {
                inventoryHolder.toggleNoClipMode()
                setClipItems()
            }
        }

        fun setNightVisionItems() {

            val name =
                if (inventoryHolder.hasActiveNightVision()) "gui.main.item.night.enable.name" else "gui.main.item.night.disable.name"

            val item = smartItemWithoutMeta(
                name = name,
                description = LanguageManager.getStringFromConfig("gui.main.item.night.lore"),
                material = Material.ENDER_EYE,
                interactionType = InteractionType.CLICK
            )

            this.setBlockedSlot(InventorySlots.SLOT7ROW1, item) {
                inventoryHolder.toggleNightVision()
                setNightVisionItems()
            }
        }

        fun init() {
            setNightVisionItems()
            setPhysicItems()
            setBuildItems()
        }

        init()
    }
}