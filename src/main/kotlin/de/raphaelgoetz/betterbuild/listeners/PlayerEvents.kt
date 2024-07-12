package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listen
import de.raphaelgoetz.astralis.event.listenCancelled
import de.raphaelgoetz.betterbuild.manager.*
import de.raphaelgoetz.betterbuild.menus.openMainMenu
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.*

fun registerPlayerEvents() {

    listen<PlayerJoinEvent> { playerJoinEvent ->
        val player = playerJoinEvent.player
        player.gameMode = GameMode.CREATIVE

        val world = Bukkit.getWorld("world")
        if (world != null) player.teleport(world.spawnLocation)

        playerJoinEvent.joinMessage(LanguageManager.getComponent("event.join.message", "%player%", player.name))
        player.playerListName(
            MiniMessage.miniMessage().deserialize(
                "<gradient:#a8ff78:#78ffd6>" + player.name + " <gradient:#00b09b:#96c93d>[" + player.world.name + "]"
            )
        )
    }

    listen<PlayerQuitEvent> { playerQuitEvent ->
        val player = playerQuitEvent.player
        playerQuitEvent.quitMessage(LanguageManager.getComponent("event.quit.message", "%player%", player.name))
        player.clearPlayer()
    }

    //TODO: AWAIT PLAYER-WORLD-CREATE RESPONSE
    /*
    listen<AsyncChatEvent> { playerAsyncChatEvent ->
        val uuid = playerAsyncChatEvent.player.uniqueId
        if (!worldCreation.contains(uuid)) return@listen
        val buildWorld = worldCreation[uuid] ?: return@listen
        playerAsyncChatEvent.isCancelled = true

        val message = MiniMessage.miniMessage().serialize(playerAsyncChatEvent.message())
        buildWorld.name = message.replace(Regex("\\W"), "")

        doNow(function = {
            generateWorld(buildWorld)
            LanguageManager.sendPlayerMessage(playerAsyncChatEvent.player, "message.world.created")
            worldCreation.remove(uuid)
        })

    }
     */

    listen<PlayerChangedWorldEvent> { playerChangedWorldEvent ->
        val player = playerChangedWorldEvent.player
        val lastWorld = playerChangedWorldEvent.from

        player.playSound(player, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1f, 1f)
        player.sendActionBar(LanguageManager.getComponent("event.change.world.action", "%world%", player.world.name))

        player.playerListName(
            MiniMessage.miniMessage().deserialize(
                "<gradient:#a8ff78:#78ffd6>" + player.name + " <gradient:#00b09b:#96c93d>[" + player.world.name + "]"
            )
        )

        if (lastWorld.name == "world") return@listen
        if (lastWorld.players.isNotEmpty()) return@listen
        Bukkit.unloadWorld(lastWorld.name, true)
    }

    listen<EntityDamageEvent> { playerDamageEvent ->
        val entity = playerDamageEvent.entity
        if (entity is Player) playerDamageEvent.isCancelled = true
    }

    listen<PlayerDropItemEvent> { playerDropItemEvent ->
        if (playerDropItemEvent.player.isSneaking) return@listen
        playerDropItemEvent.itemDrop.remove()
    }

    listen<PlayerSwapHandItemsEvent> { playerSwapHandItemsEvent ->
        playerSwapHandItemsEvent.isCancelled = true
        val player = playerSwapHandItemsEvent.player
        player.openMainMenu(LanguageManager.getComponent("gui.main.title"))
    }

    listen<PlayerTeleportEvent> { playerTeleportEvent ->
        var enterPermission = getWorldPermission(playerTeleportEvent.to.world.name)
        if (enterPermission == "") enterPermission = "betterbuild.enter.free"
        val player = playerTeleportEvent.player

        if (!player.hasPermission(enterPermission) && enterPermission != "betterbuild.enter.free") {
            LanguageManager.sendPlayerMessage(player, "event.teleport.permission")
            player.teleport(playerTeleportEvent.from)
            return@listen
        }

        player.uniqueId.setLastLocation(playerTeleportEvent.from)
    }

    listen<PlayerEggThrowEvent> { event -> event.isHatching = false }

    listenCancelled<PlayerBedEnterEvent>()
    listenCancelled<PlayerFishEvent>()
    listenCancelled<PlayerItemConsumeEvent>()
    listenCancelled<PlayerPortalEvent>()

    listenBuildMode<PlayerArmorStandManipulateEvent>()
    listenBuildMode<PlayerBucketEmptyEvent>()
    listenBuildMode<PlayerBucketEntityEvent>()
    listenBuildMode<PlayerBucketFillEvent>()
    listenBuildMode<PlayerInteractEvent>()
    listenBuildMode<PlayerInteractEntityEvent>()

}

inline fun <reified T : PlayerEvent> listenBuildMode() {
    listen<T> { event ->
        if (event.player.isActiveBuilder()) return@listen
        if (event !is Cancellable) return@listen
        event.isCancelled = true
    }
}