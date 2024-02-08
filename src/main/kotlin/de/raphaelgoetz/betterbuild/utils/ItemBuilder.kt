package de.raphaelgoetz.betterbuild.utils

import de.raphaelgoetz.betterbuild.manager.LanguageManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.net.URL
import java.util.*

class ItemBuilder(material: Material) {

    private val itemStack = ItemStack(material)

    fun setName(key: String): ItemBuilder {
        val itemMeta = itemStack.itemMeta
        itemMeta.displayName(LanguageManager.getComponent(key))
        itemStack.setItemMeta(itemMeta)
        return this
    }

    fun setName(component: Component): ItemBuilder {
        val itemMeta = itemStack.itemMeta
        itemMeta.displayName(component)
        itemStack.setItemMeta(itemMeta)
        return this
    }

    fun setName(key: String, value: String, replace: String): ItemBuilder {
        val itemMeta = itemStack.itemMeta
        itemMeta.displayName(LanguageManager.getComponent(key))
        itemStack.setItemMeta(itemMeta)
        return this
    }

    fun setLore(key: String): ItemBuilder {
        val itemMeta = itemStack.itemMeta
        itemMeta.lore(LanguageManager.getComponents(key))
        itemStack.setItemMeta(itemMeta)
        return this
    }

    fun setLore(component: List<Component>): ItemBuilder {
        val itemMeta = itemStack.itemMeta
        itemMeta.lore(component)
        itemStack.setItemMeta(itemMeta)
        return this
    }

    fun setPlayerHead(player: OfflinePlayer): ItemBuilder {

        if (Material.PLAYER_HEAD == itemStack.type) {
            val skullMeta = itemStack.itemMeta as SkullMeta
            skullMeta.setOwningPlayer(player)
            itemStack.setItemMeta(skullMeta)
        }

        return this
    }

    fun setPlayerHeadTexture(skinTextureURL: URL): ItemBuilder {

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

    fun setGlowing(value: Boolean): ItemBuilder {
        if (value.not()) return this
        val itemMeta = itemStack.itemMeta
        itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 1, false)
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        itemStack.setItemMeta(itemMeta)
        return this
    }

    fun build(): ItemStack {
        return itemStack
    }
}