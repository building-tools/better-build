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

private val buildMode: MutableCollection<UUID> = ArrayList()
private val ghostMode: MutableCollection<UUID> = ArrayList()
private val noClipMode: MutableCollection<UUID> = ArrayList()
private val lastPlayerLocation: MutableMap<UUID, Location> = HashMap()
val worldCreation: MutableMap<UUID, BuildWorld> = mutableMapOf()

fun UUID.getLastLocation(): Location? {
    return lastPlayerLocation[this]
}

fun UUID.setLastLocation(location: Location) {
    lastPlayerLocation[this] = location
}

fun Player.toggleBuildMode() {

    if (this.isActiveBuilder()) {
        LanguageManager.sendPlayerMessage(this, "manager.player.build.remove")
        buildMode.remove(this.uniqueId)
        return
    }

    LanguageManager.sendPlayerMessage(this, "manager.player.build.add")
    buildMode.add(this.uniqueId)
}

fun Player.toggleNoClipMode() {

    if (this.isActiveNoClip()) {
        LanguageManager.sendPlayerMessage(this, "manager.player.clip.remove")
        noClipMode.remove(this.uniqueId)
        return
    }

    LanguageManager.sendPlayerMessage(this, "manager.player.clip.add")
    noClipMode.add(this.uniqueId)
}

fun Player.toggleNightVision() {
    if (this.hasActiveNightVision()) {
        LanguageManager.sendPlayerMessage(this, "gui.main.item.night.disable.message")
        this.removePotionEffect(PotionEffectType.NIGHT_VISION)
        return
    }
    this.addPotionEffect(
        PotionEffect(
            PotionEffectType.NIGHT_VISION,
            PotionEffect.INFINITE_DURATION,
            1,
            false,
            false,
            false
        )
    )

    LanguageManager.sendPlayerMessage(this, "gui.main.item.night.enable.message")
}

fun Player.toggleGhostMode() {
    if (this.isActiveGhost()) {
        LanguageManager.sendPlayerMessage(this, "manager.player.ghost.remove")
        ghostMode.remove(this.uniqueId)
        return
    }

    LanguageManager.sendPlayerMessage(this, "manager.player.ghost.add")
    ghostMode.add(this.uniqueId)
}

fun Player.cancelWhenBuilder(event: Cancellable) {
    event.isCancelled = !this.isActiveBuilder()
}

fun Player.isActiveBuilder(): Boolean {
    return buildMode.contains(this.uniqueId)
}

fun Player.isActiveNoClip(): Boolean {
    return noClipMode.contains(this.uniqueId)
}

fun Player.isActiveGhost(): Boolean {
    return ghostMode.contains(this.uniqueId)
}

fun Player.hasActiveNightVision(): Boolean {

    for (potions in this.activePotionEffects) {

        if (potions.type != PotionEffectType.NIGHT_VISION) continue
        return true
    }

    return false
}

fun Player.clearPlayer() {
    lastPlayerLocation.remove(this.uniqueId)
    buildMode.remove(this.uniqueId)
    noClipMode.remove(this.uniqueId)
    worldCreation.remove(this.uniqueId)
}
