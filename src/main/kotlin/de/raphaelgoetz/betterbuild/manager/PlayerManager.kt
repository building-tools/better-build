package de.raphaelgoetz.betterbuild.manager

import de.raphaelgoetz.betterbuild.world.BuildWorld
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PlayerManager(

    private val buildMode: MutableCollection<UUID> = ArrayList(),
    private val ghostMode: MutableCollection<UUID> = ArrayList(),
    private val noClipMode: MutableCollection<UUID> = ArrayList(),
    private val lastPlayerLocation: MutableMap<UUID, Location> = HashMap(),
    private val worldCreation: MutableMap<UUID, BuildWorld> = mutableMapOf()

) {

    fun getLastLocation(player: Player): Location? {
        return lastPlayerLocation[player.uniqueId]
    }

    fun setLastLocation(player: Player, location: Location) {
        this.lastPlayerLocation[player.uniqueId] = location
    }

    fun toggleBuildMode(player: Player) {

        if (isActiveBuilder(player)) {
            LanguageManager.sendPlayerMessage(player, "manager.player.build.remove")
            buildMode.remove(player.uniqueId)
            return
        }

        LanguageManager.sendPlayerMessage(player, "manager.player.build.add")
        buildMode.add(player.uniqueId)
    }

    fun toggleNoClipMode(player: Player) {

        if (isActiveNoClip(player)) {
            LanguageManager.sendPlayerMessage(player, "manager.player.clip.remove")
            noClipMode.remove(player.uniqueId)
            return
        }

        LanguageManager.sendPlayerMessage(player, "manager.player.clip.add")
        noClipMode.add(player.uniqueId)
    }

    fun toggleNightVision(player: Player) {
        if (hasActiveNightVision(player)) {
            LanguageManager.sendPlayerMessage(player, "gui.main.item.night.disable.message")
            player.removePotionEffect(PotionEffectType.NIGHT_VISION)
            return
        }
        player.addPotionEffect(
            PotionEffect(
                PotionEffectType.NIGHT_VISION,
                PotionEffect.INFINITE_DURATION,
                1,
                false,
                false,
                false
            )
        )

        LanguageManager.sendPlayerMessage(player, "gui.main.item.night.enable.message")
    }

    fun toggleGhostMode(player: Player) {
        if (isActiveGhost(player)) {
            LanguageManager.sendPlayerMessage(player, "manager.player.ghost.remove")
            ghostMode.remove(player.uniqueId)
            return
        }

        LanguageManager.sendPlayerMessage(player, "manager.player.ghost.add")
        ghostMode.add(player.uniqueId)
    }

    fun cancelWhenBuilder(player: Player, event: Cancellable) {
        event.isCancelled = !isActiveBuilder(player)
    }

    fun isActiveBuilder(player: Player): Boolean {
        return buildMode.contains(player.uniqueId)
    }

    fun isActiveNoClip(player: Player): Boolean {
        return noClipMode.contains(player.uniqueId)
    }

    fun isActiveGhost(player: Player): Boolean {
        return ghostMode.contains(player.uniqueId)
    }

    fun hasActiveNightVision(player: Player): Boolean {

        for (potions in player.activePotionEffects) {

            if (potions.type != PotionEffectType.NIGHT_VISION) continue
            return true
        }

        return false
    }

    fun clearPlayer(player: Player) {
        lastPlayerLocation.remove(player.uniqueId)
        buildMode.remove(player.uniqueId)
        noClipMode.remove(player.uniqueId)
        worldCreation.remove(player.uniqueId)
    }
}