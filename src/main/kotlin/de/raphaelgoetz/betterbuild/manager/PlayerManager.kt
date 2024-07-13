package de.raphaelgoetz.betterbuild.manager

import de.raphaelgoetz.astralis.text.communication.CommunicationType
import de.raphaelgoetz.astralis.text.translation.sendTransText
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

fun UUID.getLastLocation(): Location? {
    return lastPlayerLocation[this]
}

fun UUID.setLastLocation(location: Location) {
    lastPlayerLocation[this] = location
}

fun Player.toggleBuildMode() {

    if (this.isActiveBuilder()) {
        this.sendTransText("manager.player.build.remove") {
            type = CommunicationType.UPDATE
        }
        buildMode.remove(this.uniqueId)
        return
    }
    this.sendTransText("manager.player.build.add") {
        type = CommunicationType.UPDATE
    }
    buildMode.add(this.uniqueId)
}

fun Player.toggleNoClipMode() {

    if (this.isActiveNoClip()) {
        this.sendTransText("manager.player.clip.remove") {
            type = CommunicationType.UPDATE
        }
        noClipMode.remove(this.uniqueId)
        return
    }
    this.sendTransText("manager.player.clip.add") {
        type = CommunicationType.UPDATE
    }
    noClipMode.add(this.uniqueId)
}

fun Player.toggleNightVision() {
    if (this.hasActiveNightVision()) {
        this.sendTransText("gui.main.item.night.disable.message") {
            type = CommunicationType.UPDATE
        }
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
    this.sendTransText("gui.main.item.night.enable.message") {
        type = CommunicationType.UPDATE
    }
}

fun Player.toggleGhostMode() {
    if (this.isActiveGhost()) {
        this.sendTransText("manager.player.ghost.remove") {
            type = CommunicationType.UPDATE
        }
        ghostMode.remove(this.uniqueId)
        return
    }
    this.sendTransText("manager.player.ghost.add") {
        type = CommunicationType.UPDATE
    }
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
}
