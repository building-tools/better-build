package de.raphaelgoetz.betterbuild.world

import org.bukkit.World.Environment

data class BuildWorld(
    var name: String,
    var types: BuildWorldTypes,
    var environment: Environment,
    var generateStructures: Boolean
)