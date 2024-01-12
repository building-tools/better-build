package de.raphaelgoetz.betterbuild.utils

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class BannerColors(val dyeColor: DyeColor, val itemStack: ItemStack, val banner: ItemStack) {
    WHITE(DyeColor.WHITE, ItemBuilder(Material.WHITE_DYE).build(), ItemBuilder(Material.WHITE_BANNER).build()),
    ORANGE(DyeColor.ORANGE, ItemBuilder(Material.ORANGE_DYE).build(), ItemBuilder(Material.ORANGE_BANNER).build()),
    MAGENTA(DyeColor.MAGENTA, ItemBuilder(Material.MAGENTA_DYE).build(), ItemBuilder(Material.MAGENTA_BANNER).build()),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE, ItemBuilder(Material.LIGHT_BLUE_DYE).build(), ItemBuilder(Material.LIGHT_BLUE_BANNER).build()),
    YELLOW(DyeColor.YELLOW, ItemBuilder(Material.YELLOW_DYE).build(), ItemBuilder(Material.YELLOW_BANNER).build()),
    LIME(DyeColor.LIME, ItemBuilder(Material.LIME_DYE).build(), ItemBuilder(Material.LIME_BANNER).build()),
    PINK(DyeColor.PINK, ItemBuilder(Material.PINK_DYE).build(), ItemBuilder(Material.PINK_BANNER).build()),
    GRAY(DyeColor.GRAY, ItemBuilder(Material.GRAY_DYE).build(), ItemBuilder(Material.GRAY_BANNER).build()),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY, ItemBuilder(Material.LIGHT_GRAY_DYE).build(), ItemBuilder(Material.LIGHT_GRAY_BANNER).build()),
    CYAN(DyeColor.CYAN, ItemBuilder(Material.CYAN_DYE).build(), ItemBuilder(Material.CYAN_BANNER).build()),
    PURPLE(DyeColor.PURPLE, ItemBuilder(Material.PURPLE_DYE).build(), ItemBuilder(Material.PURPLE_BANNER).build()),
    BLUE(DyeColor.BLUE, ItemBuilder(Material.BLUE_DYE).build(), ItemBuilder(Material.BLUE_BANNER).build()),
    BROWN(DyeColor.BROWN, ItemBuilder(Material.BROWN_DYE).build(), ItemBuilder(Material.BROWN_BANNER).build()),
    GREEN(DyeColor.GREEN, ItemBuilder(Material.GREEN_DYE).build(), ItemBuilder(Material.GREEN_BANNER).build()),
    RED(DyeColor.RED, ItemBuilder(Material.RED_DYE).build(), ItemBuilder(Material.RED_BANNER).build()),
    BLACK(DyeColor.BLACK, ItemBuilder(Material.BLACK_DYE).build(), ItemBuilder(Material.BLACK_BANNER).build())
}