package de.raphaelgoetz.betterbuild.utils.tasks

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

interface Task {

    fun getTaskHolder(): Player

    fun getTask(): BukkitTask

    fun run()
}