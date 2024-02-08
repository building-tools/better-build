package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import de.raphaelgoetz.betterbuild.menus.MainMenu
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent

class PlayerSwapHandItemsListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    private fun onPlayerSwapHandItemsEvent(playerSwapHandItemsEvent: PlayerSwapHandItemsEvent) {
        playerSwapHandItemsEvent.isCancelled = true
        val player = playerSwapHandItemsEvent.player
        MainMenu(betterBuild, LanguageManager.getComponent("gui.main.title"), player)
    }
}