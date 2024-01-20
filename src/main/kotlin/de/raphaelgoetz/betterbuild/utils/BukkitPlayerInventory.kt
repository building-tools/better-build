package de.raphaelgoetz.betterbuild.utils

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
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.function.Consumer

abstract class BukkitPlayerInventory(title: Component, rows: Int) {

    val clickActions: MutableMap<Int, Consumer<InventoryClickEvent>> = HashMap()
    val closeActions: Collection<Runnable> = ArrayList()
    val inventory: Inventory = Bukkit.createInventory(null, rows * 9, title)

    protected fun openInventory(player: Player) {
        player.openInventory(inventory)
        OPEN_INVENTORIES[player.uniqueId] = this
    }

    protected fun setSlot(slot: Int, itemStack: ItemStack) {
        clickActions.remove(slot)
        inventory.setItem(slot, itemStack)
    }

    protected fun setSlot(slot: Int, itemStack: ItemStack, consumer: Consumer<InventoryClickEvent>) {
        clickActions[slot] = consumer
        inventory.setItem(slot, itemStack)
    }

    protected fun addSlot(itemStack: ItemStack) {
        inventory.addItem(itemStack)
        val slot = inventory.first(itemStack)
        clickActions.remove(slot)
    }

    protected fun addSlot(itemStack: ItemStack, consumer: Consumer<InventoryClickEvent>?) {
        if (consumer == null) {
            this.addSlot(itemStack)
            return
        }
        this.inventory.addItem(itemStack)
        val slot = this.inventory.first(itemStack)
        clickActions[slot] = consumer
    }

    protected fun clearSlot(slot: Int) {
        inventory.clear(slot)
        clickActions.remove(slot)
    }

    protected fun clearSlots() {
        inventory.clear()
        clickActions.clear()
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
            if (player !is Player) return
            if (!(OPEN_INVENTORIES.containsKey(player.getUniqueId()))) return

            val currentInventory = OPEN_INVENTORIES[player.getUniqueId()]
            if (inventoryCloseEvent.inventory != currentInventory!!.inventory) return
            currentInventory.closeActions.forEach(Consumer { obj: Runnable -> obj.run() })

            OPEN_INVENTORIES.remove(player.getUniqueId())
        }
    }

    companion object {
        init {
            Bukkit.getPluginManager().registerEvents(EventAdapter(), JavaPlugin.getPlugin(BetterBuild::class.java))
        }

        val OPEN_INVENTORIES: MutableMap<UUID, BukkitPlayerInventory> = HashMap()
    }
}