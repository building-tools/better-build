package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listen
import de.raphaelgoetz.astralis.event.listenCancelled
import de.raphaelgoetz.astralis.text.communication.CommunicationType
import de.raphaelgoetz.astralis.text.components.adventureText
import de.raphaelgoetz.astralis.text.translation.getValue
import de.raphaelgoetz.astralis.text.translation.sendTransText
import de.raphaelgoetz.astralis.ux.color.Colorization
import de.raphaelgoetz.betterbuild.manager.*
import de.raphaelgoetz.betterbuild.menus.openMainMenu
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
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

        player.sendTransText("event.join.message") {
            type = CommunicationType.SUCCESS
            resolver = arrayOf(Placeholder.parsed("player", player.name))
        }

        val list = adventureText(player.name + " in " + player.world.name) {
            color = Colorization.GREEN
        }

        playerJoinEvent.joinMessage(null)
        player.playerListName(list)
    }

    listen<PlayerQuitEvent> { playerQuitEvent ->
        val player = playerQuitEvent.player
        playerQuitEvent.quitMessage(null)
        player.sendTransText("event.quit.message") {
            type = CommunicationType.ERROR
            resolver = arrayOf(Placeholder.parsed("%player%", player.name))
        }

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

        val actionBar = adventureText(player.locale().getValue("event.change.world.action")) {
            color = Colorization.GREEN
            resolver = arrayOf(Placeholder.parsed("%world%", player.world.name))
        }

        val list = adventureText(player.name + " in " + player.world.name) {
            color = Colorization.GREEN
        }

        player.playerListName(list)
        player.sendActionBar(actionBar)

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
        player.openMainMenu("gui.main.title")
    }

    listen<PlayerTeleportEvent> { playerTeleportEvent ->
        var enterPermission = getWorldPermission(playerTeleportEvent.to.world.name)
        if (enterPermission == "") enterPermission = "betterbuild.enter.free"
        val player = playerTeleportEvent.player

        if (!player.hasPermission(enterPermission) && enterPermission != "betterbuild.enter.free") {
            player.sendTransText("event.teleport.permission") {
                type = CommunicationType.ERROR
            }
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