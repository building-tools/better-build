package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listenCancelled
import org.bukkit.event.vehicle.VehicleCreateEvent
import org.bukkit.event.vehicle.VehicleEnterEvent

fun registerVehicleEvents() {

    listenCancelled<VehicleCreateEvent>()
    listenCancelled<VehicleEnterEvent>()

}