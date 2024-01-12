package de.raphaelgoetz.betterbuild.listeners.raid

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.raid.RaidTriggerEvent

class RaidTriggerListener : Listener {
    @EventHandler
    fun onRaidTriggerEvent(raidTriggerEvent: RaidTriggerEvent) {
        raidTriggerEvent.isCancelled = true
    }
}