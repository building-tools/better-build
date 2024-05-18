package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listen
import de.raphaelgoetz.astralis.event.listenCancelled
import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import de.raphaelgoetz.betterbuild.menus.MainMenu
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.*

fun registerPlayerEvents(betterBuild: BetterBuild) {

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
        betterBuild.playerManager.clearPlayer(player)
    }

    listen<AsyncChatEvent> { playerAsyncChatEvent ->
        val uuid = playerAsyncChatEvent.player.uniqueId
        if (!betterBuild.playerManager.worldCreation.contains(uuid)) return@listen
        val buildWorld = betterBuild.playerManager.worldCreation[uuid] ?: return@listen
        playerAsyncChatEvent.isCancelled = true

        val message = MiniMessage.miniMessage().serialize(playerAsyncChatEvent.message())
        buildWorld.name = message.replace(Regex("\\W"), "")

        Bukkit.getScheduler().callSyncMethod(betterBuild) {
            betterBuild.worldManager.generateWorld(buildWorld)
            LanguageManager.sendPlayerMessage(playerAsyncChatEvent.player, "message.world.created")
            betterBuild.playerManager.worldCreation.remove(uuid)
        }
    }

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
        MainMenu(betterBuild, LanguageManager.getComponent("gui.main.title"), player)
    }

    listen<PlayerTeleportEvent> { playerTeleportEvent ->
        var enterPermission = betterBuild.worldManager.getWorldPermission(playerTeleportEvent.to.world.name)
        if (enterPermission == "") enterPermission = "betterbuild.enter.free"
        val player = playerTeleportEvent.player

        if (!player.hasPermission(enterPermission) && enterPermission != "betterbuild.enter.free") {
            LanguageManager.sendPlayerMessage(player, "event.teleport.permission")
            player.teleport(playerTeleportEvent.from)
            return@listen
        }

        betterBuild.playerManager.setLastLocation(playerTeleportEvent.player, playerTeleportEvent.from)
    }

    listen<PlayerEggThrowEvent> { event -> event.isHatching = false }

    listenCancelled<PlayerBedEnterEvent>()
    listenCancelled<PlayerFishEvent>()
    listenCancelled<PlayerItemConsumeEvent>()
    listenCancelled<PlayerPortalEvent>()

    listenBuildMode<PlayerArmorStandManipulateEvent>(betterBuild)
    listenBuildMode<PlayerBucketEmptyEvent>(betterBuild)
    listenBuildMode<PlayerBucketEntityEvent>(betterBuild)
    listenBuildMode<PlayerBucketFillEvent>(betterBuild)
    listenBuildMode<PlayerInteractEvent>(betterBuild)
    listenBuildMode<PlayerInteractEntityEvent>(betterBuild)

}

inline fun <reified T : org.bukkit.event.player.PlayerEvent> listenBuildMode(betterBuild: BetterBuild) {
    listen<T> { event ->
        if (betterBuild.playerManager.isActiveBuilder(event.player)) return@listen
        if (event !is Cancellable) return@listen
        event.isCancelled = true
    }
}