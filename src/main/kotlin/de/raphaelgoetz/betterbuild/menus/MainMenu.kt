package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.astralis.items.basicSmartTransItem
import de.raphaelgoetz.astralis.items.data.InteractionType
import de.raphaelgoetz.astralis.items.smartTransItem
import de.raphaelgoetz.astralis.ui.data.InventoryRows
import de.raphaelgoetz.astralis.ui.data.InventorySlots
import de.raphaelgoetz.astralis.ui.openTransInventory
import de.raphaelgoetz.betterbuild.manager.*
import de.raphaelgoetz.betterbuild.menus.world.openWorldOverviewMenu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.SkullMeta

fun Player.openMainMenu(title: String) {

    val inventoryHolder = this

    this.openTransInventory(title, "Main-Menu", InventoryRows.ROW1) {

        val worldItem = basicSmartTransItem(
            key = "gui.main.item.world.name",
            descriptionKey = "gui.main.item.world.lore",
            material = Material.GRASS_BLOCK,
            interactionType = InteractionType.CLICK
        )

        val playerItem = smartTransItem<SkullMeta>(
            key = "gui.player.item.world.name",
            descriptionKey = "gui.player.item.world.lore",
            material = Material.PLAYER_HEAD,
            interactionType = InteractionType.CLICK
        ) { owningPlayer = inventoryHolder }

        val bannerItem = basicSmartTransItem(
            key = "gui.banner.item.world.name",
            descriptionKey = "gui.banner.item.world.lore",
            material = Material.GRASS_BLOCK,
            interactionType = InteractionType.CLICK
        )

        this.setBlockedSlot(InventorySlots.SLOT2ROW1, worldItem) {
            inventoryHolder.openWorldOverviewMenu("gui.world.title")
        }

        this.setBlockedSlot(InventorySlots.SLOT3ROW1, playerItem) {
            inventoryHolder.openPlayerOverviewMenu("gui.player.title")
        }

        this.setBlockedSlot(InventorySlots.SLOT4ROW1, bannerItem) {
            inventoryHolder.openBannerCreationMenu("gui.banner.title")
        }

        fun setPhysicItems() {

            val world = inventoryHolder.world
            val name =
                if (world.hasPhysics()) "gui.main.item.physics.enable.name" else "gui.main.item.physics.disable.name"

            val item = basicSmartTransItem(
                key = name,
                descriptionKey = "gui.main.item.physics.lore",
                material = Material.GRAVEL,
                interactionType = InteractionType.CLICK
            )

            this.setBlockedSlot(InventorySlots.SLOT6ROW1, item) {
                world.togglePhysics()
                setPhysicItems()
            }
        }

        fun setBuildItems() {

            val name =
                if (inventoryHolder.isActiveBuilder()) "gui.main.item.build.enable.name" else "gui.main.item.build.disable.name"

            val item = basicSmartTransItem(
                key = name,
                descriptionKey = "gui.main.item.build.lore",
                material = Material.DIAMOND_AXE,
                interactionType = InteractionType.CLICK
            )

            this.setBlockedSlot(InventorySlots.SLOT7ROW1, item) {
                inventoryHolder.toggleBuildMode()
                setBuildItems()
            }
        }

        fun setClipItems() {

            val name =
                if (inventoryHolder.isActiveNoClip()) "gui.main.item.clip.enable.name" else "gui.main.item.clip.disable.name"

            val item = basicSmartTransItem(
                key = name,
                descriptionKey = "gui.main.item.clip.lore",
                material = Material.ELYTRA,
                interactionType = InteractionType.CLICK
            )

            this.setBlockedSlot(InventorySlots.SLOT8ROW1, item) {
                inventoryHolder.toggleNoClipMode()
                setClipItems()
            }
        }

        fun setNightVisionItems() {

            val name =
                if (inventoryHolder.hasActiveNightVision()) "gui.main.item.night.enable.name" else "gui.main.item.night.disable.name"

            val item = basicSmartTransItem(
                key = name,
                descriptionKey = "gui.main.item.night.lore",
                material = Material.ENDER_EYE,
                interactionType = InteractionType.CLICK
            )

            this.setBlockedSlot(InventorySlots.SLOT8ROW1, item) {
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