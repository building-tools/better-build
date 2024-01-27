package de.raphaelgoetz.betterbuild.menus

import de.raphaelgoetz.betterbuild.utils.BannerColors
import de.raphaelgoetz.betterbuild.utils.ItemBuilder
import de.raphaelgoetz.betterbuild.utils.menus.BukkitPlayerInventory
import net.kyori.adventure.text.Component
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta

class BannerCreationMenu(

    val player: Player,
    val title: Component

) : BukkitPlayerInventory(title, 6) {

    private val bannerHistory: MutableList<ItemStack> = ArrayList<ItemStack>()
    private var dyeColor: DyeColor? = null
    private var bannerMeta: BannerMeta? = null
    private var banner: ItemStack? = null

    fun open() {
        setBaseColor()
        openInventory(player)
    }

    private fun setBaseColor() {

        if (bannerHistory.size >= 6) {
            player.inventory.addItem(bannerHistory.last())
            player.closeInventory()
            return
        }

        BannerColors.entries.forEach { bannerColors ->
            this.addSlot(bannerColors.banner, consumer = {
                it.isCancelled = true
                banner = ItemStack(bannerColors.banner.type)
                bannerMeta = banner!!.itemMeta as BannerMeta

                generateDyeColor()
            })
        }
    }

    private fun generateDyeColor() {
        clearSlots()

        BannerColors.entries.forEach { bannerColors ->
            this.addSlot(bannerColors.itemStack, consumer = {
                it.isCancelled = true
                dyeColor = bannerColors.dyeColor
                generatePatterns()
            })

        }
    }

    private fun generatePatterns() {

        if (bannerHistory.size >= 6) {
            player.inventory.addItem(bannerHistory.last())
            player.closeInventory()
            return
        }

        if (bannerMeta!!.patterns.isEmpty() && bannerMeta!!.patterns.size > 5) {
            player.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1f, 1f)
            player.inventory.addItem(bannerHistory[bannerHistory.size - 1])
            player.inventory.close()

            return
        }

        clearSlots()

        for (i in 0..5) {

            this.setSlot(7 + (i * 9), ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build(),
                consumer = { inventoryClickEvent ->
                    inventoryClickEvent.isCancelled = true
                })

            this.setSlot(8 + (i * 9), ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).build(),
                consumer = { inventoryClickEvent ->
                    inventoryClickEvent.isCancelled = true
                })
        }

        for (index in bannerHistory.indices) {
            if (8 + (index * 9) > 53) break

            this.setSlot(8 + (index * 9), bannerHistory[index],
                consumer = { inventoryClickEvent ->
                    inventoryClickEvent.isCancelled = true

                    if (inventoryClickEvent.currentItem != null) {
                        player.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1f, 1f)
                        player.inventory.addItem(bannerHistory[index])
                        player.inventory.close()
                    }
                })
        }

        PatternType.entries.forEach { patternType ->
            val nextBanner = ItemStack(banner!!.type)
            val nextBannerMeta = banner!!.itemMeta as BannerMeta

            nextBannerMeta.patterns = bannerMeta!!.patterns
            dyeColor?.let { Pattern(it, patternType) }?.let { nextBannerMeta.addPattern(it) }
            nextBanner.setItemMeta(nextBannerMeta)

            this.addSlot(nextBanner, consumer = {
                it.isCancelled = true
                dyeColor?.let { it1 -> Pattern(it1, patternType) }?.let { it2 -> bannerMeta!!.addPattern(it2) }
                bannerHistory.add(nextBanner)
                generateDyeColor()
            })
        }
    }
}