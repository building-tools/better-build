package de.raphaelgoetz.betterbuild.listeners.vehicle

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.vehicle.VehicleEnterEvent

class VehicleEnterListener : Listener {
    @EventHandler
    fun onVehicleEnterEvent(vehicleEnterEvent: VehicleEnterEvent) {
        vehicleEnterEvent.isCancelled = true
    }
}
