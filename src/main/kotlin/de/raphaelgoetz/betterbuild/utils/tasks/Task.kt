package de.raphaelgoetz.betterbuild.utils.tasks

import org.bukkit.entity.Player

interface Task {

    fun getTaskHolder(): Player

    fun getTask(): Runnable

    fun run()

    fun stop()

}