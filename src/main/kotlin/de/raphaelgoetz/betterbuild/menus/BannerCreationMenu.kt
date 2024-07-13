package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.astralis.items.builder.SmartItem
import de.raphaelgoetz.astralis.items.createSmartItem
import de.raphaelgoetz.astralis.items.data.InteractionType
import de.raphaelgoetz.astralis.items.smartItemWithoutMeta
import de.raphaelgoetz.astralis.ui.builder.InventoryBuilder
import de.raphaelgoetz.astralis.ui.data.InventoryRows
import de.raphaelgoetz.astralis.ui.openInventory
import de.raphaelgoetz.astralis.ui.openTransInventory
import de.raphaelgoetz.betterbuild.utils.BannerColors
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta

fun Player.openBannerCreationMenu(title: String) {
    BannerCreationMenu(this, title).open()
}

class BannerCreationMenu(val player: Player, private val title: String) {

    private val bannerHistory: MutableList<SmartItem> = ArrayList()
    private var dyeColor: DyeColor? = null
    private var bannerMeta: BannerMeta? = null
    private var banner: ItemStack? = null

    fun open() {

        player.openTransInventory(title, rows = InventoryRows.ROW6) {
            setBaseColor()
        }

    }

    private fun InventoryBuilder.setBaseColor() {

        if (bannerHistory.size >= 6) {
            player.inventory.addItem(bannerHistory.last().itemStack)
            player.closeInventory()
            return
        }

        BannerColors.entries.forEach { bannerColors ->
            this.addBlockedSlot(SmartItem(bannerColors.banner, InteractionType.CLICK)) {
                banner = ItemStack(bannerColors.banner.type)
                bannerMeta = banner!!.itemMeta as BannerMeta
                generateDyeColor()
            }
        }
    }

    private fun InventoryBuilder.generateDyeColor() {
        clear()

        BannerColors.entries.forEach { bannerColors ->
            this.addBlockedSlot(bannerColors.itemStack) {
                dyeColor = bannerColors.dyeColor
                generatePatterns()
            }
        }
    }

    private fun InventoryBuilder.generatePatterns() {

        if (bannerHistory.size >= 6) {
            player.inventory.addItem(bannerHistory.last().itemStack)
            player.closeInventory()
            return
        }

        if (bannerMeta!!.patterns.isEmpty() && bannerMeta!!.patterns.size > 5) {
            player.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1f, 1f)
            player.inventory.addItem(bannerHistory[bannerHistory.size - 1].itemStack)
            player.inventory.close()
            return
        }

        clear()

        for (i in 0..5) {

            val gray = smartItemWithoutMeta("", Material.GRAY_STAINED_GLASS_PANE)
            val lightGray = smartItemWithoutMeta("", Material.LIGHT_GRAY_STAINED_GLASS_PANE)

            this.setSlot(7 + (i * 9), gray) { inventoryClickEvent ->
                inventoryClickEvent.isCancelled = true
            }

            this.setSlot(8 + (i * 9), lightGray) { inventoryClickEvent ->
                inventoryClickEvent.isCancelled = true
            }
        }

        for (index in bannerHistory.indices) {
            if (8 + (index * 9) > 53) break

            this.setSlot(8 + (index * 9), bannerHistory[index]) { inventoryClickEvent ->
                inventoryClickEvent.isCancelled = true

                if (inventoryClickEvent.currentItem != null) {
                    player.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1f, 1f)
                    player.inventory.addItem(bannerHistory[index].itemStack)
                    player.inventory.close()
                }
            }
        }

        PatternType.entries.forEach { patternType ->
            val nextBanner = SmartItem(banner!!, InteractionType.CLICK)
            val nextBannerMeta = banner!!.itemMeta as BannerMeta

            nextBannerMeta.patterns = bannerMeta!!.patterns
            dyeColor?.let { Pattern(it, patternType) }?.let { nextBannerMeta.addPattern(it) }
            val nextItem = nextBanner.setBannerMeta(nextBannerMeta)

            this.addBlockedSlot(nextItem) {
                dyeColor?.let { it1 -> Pattern(it1, patternType) }?.let { it2 -> bannerMeta!!.addPattern(it2) }
                bannerHistory.add(nextItem)
                generateDyeColor()
            }
        }
    }
}

private fun SmartItem.setBannerMeta(bannerMeta: BannerMeta) : SmartItem {

    return createSmartItem<BannerMeta>("", this.itemStack.type) {
        patterns = bannerMeta.patterns
    }

}