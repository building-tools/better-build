package de.raphaelgoetz.betterbuild.listeners.player.connection

import de.raphaelgoetz.betterbuild.BetterBuild
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

data class PlayerJoinListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    private fun onPlayerJoinEvent(playerJoinEvent: PlayerJoinEvent) {
        val player = playerJoinEvent.player
        player.gameMode = GameMode.CREATIVE

        val world = Bukkit.getWorld("world")
        if (world != null) player.teleport(world.spawnLocation)

        playerJoinEvent.joinMessage(betterBuild.languageManager.getComponent("event.join.message", "%player%", player.name))
        player.playerListName(
            MiniMessage.miniMessage().deserialize(
                "<gradient:#a8ff78:#78ffd6>" + player.name + " <gradient:#00b09b:#96c93d>[" + player.world.name + "]"
            ))
    }
}
