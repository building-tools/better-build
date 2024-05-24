package de.raphaelgoetz.betterbuild.utils

import de.raphaelgoetz.astralis.items.basicItemWithoutMeta
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class BannerColors(val dyeColor: DyeColor, val itemStack: ItemStack, val banner: ItemStack) {
    WHITE(DyeColor.WHITE, basicItemWithoutMeta(Material.WHITE_DYE), basicItemWithoutMeta(Material.WHITE_BANNER)),
    ORANGE(DyeColor.ORANGE, basicItemWithoutMeta(Material.ORANGE_DYE), basicItemWithoutMeta(Material.ORANGE_BANNER)),
    MAGENTA(DyeColor.MAGENTA, basicItemWithoutMeta(Material.MAGENTA_DYE), basicItemWithoutMeta(Material.MAGENTA_BANNER)),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE, basicItemWithoutMeta(Material.LIGHT_BLUE_DYE), basicItemWithoutMeta(Material.LIGHT_BLUE_BANNER)),
    YELLOW(DyeColor.YELLOW, basicItemWithoutMeta(Material.YELLOW_DYE), basicItemWithoutMeta(Material.YELLOW_BANNER)),
    LIME(DyeColor.LIME, basicItemWithoutMeta(Material.LIME_DYE), basicItemWithoutMeta(Material.LIME_BANNER)),
    PINK(DyeColor.PINK, basicItemWithoutMeta(Material.PINK_DYE), basicItemWithoutMeta(Material.PINK_BANNER)),
    GRAY(DyeColor.GRAY, basicItemWithoutMeta(Material.GRAY_DYE), basicItemWithoutMeta(Material.GRAY_BANNER)),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY, basicItemWithoutMeta(Material.LIGHT_GRAY_DYE), basicItemWithoutMeta(Material.LIGHT_GRAY_BANNER)),
    CYAN(DyeColor.CYAN, basicItemWithoutMeta(Material.CYAN_DYE), basicItemWithoutMeta(Material.CYAN_BANNER)),
    PURPLE(DyeColor.PURPLE, basicItemWithoutMeta(Material.PURPLE_DYE), basicItemWithoutMeta(Material.PURPLE_BANNER)),
    BLUE(DyeColor.BLUE, basicItemWithoutMeta(Material.BLUE_DYE), basicItemWithoutMeta(Material.BLUE_BANNER)),
    BROWN(DyeColor.BROWN, basicItemWithoutMeta(Material.BROWN_DYE), basicItemWithoutMeta(Material.BROWN_BANNER)),
    GREEN(DyeColor.GREEN, basicItemWithoutMeta(Material.GREEN_DYE), basicItemWithoutMeta(Material.GREEN_BANNER)),
    RED(DyeColor.RED, basicItemWithoutMeta(Material.RED_DYE), basicItemWithoutMeta(Material.RED_BANNER)),
    BLACK(DyeColor.BLACK, basicItemWithoutMeta(Material.BLACK_DYE), basicItemWithoutMeta(Material.BLACK_BANNER))
}

val bannerColors: List<BannerColors>
    get() = BannerColors.entries.toList()