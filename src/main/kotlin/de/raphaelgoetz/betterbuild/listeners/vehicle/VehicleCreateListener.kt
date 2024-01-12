package de.raphaelgoetz.betterbuild.listeners.vehicle

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.vehicle.VehicleCreateEvent

class VehicleCreateListener : Listener {
    @EventHandler
    fun onVehicleCreateEvent(vehicleCreateEvent: VehicleCreateEvent) {
        vehicleCreateEvent.isCancelled = true
    }
}
