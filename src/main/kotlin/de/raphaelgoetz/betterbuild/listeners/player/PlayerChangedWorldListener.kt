package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent

class PlayerChangedWorldListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerChangedWorldEvent(playerChangedWorldEvent: PlayerChangedWorldEvent) {

        val player = playerChangedWorldEvent.player
        val lastWorld = playerChangedWorldEvent.from

        player.playSound(player, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1f, 1f)
        player.sendActionBar(
            MiniMessage.miniMessage()
                .deserialize("<gradient:#00b09b:#96c93d>You entered: " + player.world.name + "</gradient>")
        )

        player.playerListName(
            MiniMessage.miniMessage().deserialize(
                "<gradient:#a8ff78:#78ffd6>" + player.name + " <gradient:#00b09b:#96c93d>[" + player.world.name + "]"
            )
        )
    }
}