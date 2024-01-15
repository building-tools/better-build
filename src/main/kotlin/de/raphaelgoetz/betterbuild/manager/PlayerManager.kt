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

        if (player.isActiveBuilder()) {
            player.sendMessage("You have been removed from buildMode")
            buildMode.remove(player)
            return
        }

        player.sendMessage("Add to BuildMode")
        buildMode.add(player)
    }

    fun toggleNoClipMode(player: Player) {

        if (player.isActiveNoClip()) {
            player.sendMessage("You have been removed from noClipMode")
            noClipMode.remove(player)
            return
        }

        player.sendMessage("Add to noClipMode")
        noClipMode.add(player)
    }

    fun toggleNightVision(player: Player) {
        if (player.hasActiveNightVision()) player.removePotionEffect(PotionEffectType.NIGHT_VISION)
        else player.addPotionEffect(
            PotionEffect(
                PotionEffectType.NIGHT_VISION,
                PotionEffect.INFINITE_DURATION,
                1,
                false,
                false,
                false
            )
        )
    }

    fun cancelWhenBuilder(player: Player, event: Cancellable) {
        event.isCancelled = player.isActiveBuilder()
    }

    private fun Player.isActiveBuilder(): Boolean {
        return buildMode.contains(player)
    }

    private fun Player.isActiveNoClip(): Boolean {
        return noClipMode.contains(player)
    }

    private fun Player.hasActiveNightVision(): Boolean {

        for (potions in player?.activePotionEffects!!) {

            if (potions.type != PotionEffectType.NIGHT_VISION) continue
            return true
        }

        return false
    }
}