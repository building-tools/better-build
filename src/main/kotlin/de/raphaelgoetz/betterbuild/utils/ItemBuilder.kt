package de.raphaelgoetz.betterbuild.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.net.URL
import java.util.*

class ItemBuilder(material: Material) {

    private val itemStack = ItemStack(material)

    fun setName(string: String): ItemBuilder {
        val itemMeta = itemStack.itemMeta
        itemMeta.displayName(MiniMessage.miniMessage().deserialize(string))
        itemStack.setItemMeta(itemMeta)
        return this
    }

    fun setLore(components: List<Component?>?): ItemBuilder {
        val itemMeta = itemStack.itemMeta
        itemMeta.lore(components)
        itemStack.setItemMeta(itemMeta)
        return this
    }

    fun setPlayerHeadTexture(skinTextureURL: URL?): ItemBuilder {

        if (Material.PLAYER_HEAD == itemStack.type) {

            if (itemStack.durability.toInt() != 3) itemStack.durability = 3.toShort()
            val skullMeta = itemStack.itemMeta as SkullMeta
            val playerProfile = Bukkit.createProfile(UUID.randomUUID())
            val playerTextures = playerProfile.textures

            playerTextures.skin = skinTextureURL
            playerProfile.setTextures(playerTextures)
            skullMeta.playerProfile = playerProfile
            itemStack.setItemMeta(skullMeta)
        }
        return this
    }

    fun build(): ItemStack {
        return itemStack
    }
}