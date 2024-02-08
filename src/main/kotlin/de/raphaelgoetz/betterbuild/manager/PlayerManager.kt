package de.raphaelgoetz.betterbuild.manager

import  de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PlayerManager(

    private val betterBuild: BetterBuild,
    private val buildMode: MutableCollection<Player> = ArrayList(),
    private val noClipMode: MutableCollection<Player> = ArrayList(),
    private val lastPlayerLocation: MutableMap<Player, Location> = HashMap()

) {

    fun getLastLocation(player: Player): Location? {
        return lastPlayerLocation[player]
    }

    fun setLastLocation(player: Player, location: Location) {
        this.lastPlayerLocation[player] = location
    }

    fun toggleBuildMode(player: Player) {

        if (isActiveBuilder(player)) {
            LanguageManager.sendPlayerMessage(player, "manager.player.build.remove")
            buildMode.remove(player)
            return
        }

        LanguageManager.sendPlayerMessage(player, "manager.player.build.add")
        buildMode.add(player)
    }

    fun toggleNoClipMode(player: Player) {

        if (isActiveNoClip(player)) {
            LanguageManager.sendPlayerMessage(player, "manager.player.clip.remove")
            noClipMode.remove(player)
            return
        }

        LanguageManager.sendPlayerMessage(player, "manager.player.clip.add")
        noClipMode.add(player)
    }

    fun toggleNightVision(player: Player) {
        if (hasActiveNightVision(player)) {
            LanguageManager.sendPlayerMessage(player, "gui.main.item.night.disable.name")
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

        LanguageManager.sendPlayerMessage(player, "gui.main.item.night.enable.name")
    }

    fun cancelWhenBuilder(player: Player, event: Cancellable) {
        event.isCancelled = !isActiveBuilder(player)
    }

    fun isActiveBuilder(player: Player): Boolean {
        return buildMode.contains(player)
    }

    fun isActiveNoClip(player: Player): Boolean {
        return noClipMode.contains(player)
    }

    fun hasActiveNightVision(player: Player): Boolean {

        for (potions in player.activePotionEffects) {

            if (potions.type != PotionEffectType.NIGHT_VISION) continue
            return true
        }

        return false
    }
}