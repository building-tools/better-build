package de.raphaelgoetz.betterbuild.utils.menus

import de.raphaelgoetz.betterbuild.BetterBuild
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.function.Consumer

abstract class BukkitAnvilInventory(title: Component) {

    protected val closeActions: Collection<Runnable> = ArrayList()
    private val clickActions: MutableMap<Int, Consumer<InventoryClickEvent>> = HashMap()
    private val inventory: Inventory = Bukkit.createInventory(null, InventoryType.ANVIL)

    protected fun open(player: Player) {
        player.openInventory(inventory)
        OPEN_INVENTORIES[player.uniqueId] = this
    }

    protected fun setSlot(slot: BukkitAnvilInventorySlots, item: ItemStack) {
        this.clickActions.remove(slot.index)
        this.inventory.setItem(slot.index, item)
    }

    protected fun setSlot(slot: BukkitAnvilInventorySlots, item: ItemStack, consumer: Consumer<InventoryClickEvent>) {
        this.clickActions[slot.index] = consumer
        this.inventory.setItem(slot.index, item)
    }

    protected fun removeSlot(slot: BukkitAnvilInventorySlots) {
        this.clickActions.remove(slot.index)
        this.inventory.clear(slot.index)
    }

    protected fun getText(): String {

        if (inventory is AnvilInventory) {
            return inventory.renameText ?: ""
        }

        return ""
    }

    private class EventAdapter : Listener {
        @EventHandler(priority = EventPriority.LOW)
        fun onInventoryClickEvent(inventoryClickEvent: InventoryClickEvent) {
            val player = inventoryClickEvent.whoClicked
            if (player !is Player) return
            if (inventoryClickEvent.clickedInventory == null) return
            if (inventoryClickEvent.currentItem == null) return
            if (!OPEN_INVENTORIES.containsKey(player.getUniqueId())) return

            val currentInventory = OPEN_INVENTORIES[player.getUniqueId()]
            val slot = inventoryClickEvent.slot

            if (inventoryClickEvent.clickedInventory != currentInventory!!.inventory) return
            if (!currentInventory.clickActions.containsKey(slot)) return

            currentInventory.clickActions[slot]!!.accept(inventoryClickEvent)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        }

        @EventHandler
        fun onInventoryCloseEvent(inventoryCloseEvent: InventoryCloseEvent) {
            val player = inventoryCloseEvent.player
            if (inventoryCloseEvent.player !is Player) return
            if (!(BukkitPlayerInventory.OPEN_INVENTORIES.containsKey(player.uniqueId))) return

            val currentInventory = BukkitPlayerInventory.OPEN_INVENTORIES[player.uniqueId]
            if (inventoryCloseEvent.inventory != currentInventory!!.inventory) return
            currentInventory.closeActions.forEach(Consumer { obj: Runnable -> obj.run() })

            BukkitPlayerInventory.OPEN_INVENTORIES.remove(player.uniqueId)
        }
    }

    companion object {

        init {
            Bukkit.getPluginManager().registerEvents(EventAdapter(), JavaPlugin.getPlugin(BetterBuild::class.java))
        }

        private val OPEN_INVENTORIES: MutableMap<UUID, BukkitAnvilInventory> = HashMap()
    }
}