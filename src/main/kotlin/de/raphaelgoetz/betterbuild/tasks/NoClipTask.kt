package de.raphaelgoetz.betterbuild.tasks

import de.raphaelgoetz.astralis.task.Task
import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class NoClipTask(
        val betterBuild: BetterBuild,
        val player: Player
) : Task {

    private var taskId = 0

    override fun getTaskHolder(): Player {
        return player
    }

    override fun getTask(): Runnable {
        return Runnable {
            if (!player.isSprinting) return@Runnable
            val location = player.location.add(getOffset(player.facing))

            if (player.gameMode == GameMode.CREATIVE) {
                if (location.block.isSolid) player.gameMode = GameMode.SPECTATOR
                return@Runnable
            }

            if (player.gameMode == GameMode.SPECTATOR) {
                if (!location.block.isSolid) player.gameMode = GameMode.CREATIVE
                return@Runnable
            }

            stop()
        }
    }

    override fun run() {
        val task = Bukkit.getScheduler().runTaskTimerAsynchronously(betterBuild, getTask(), 20, 20)
        taskId = task.taskId
    }

    override fun stop() {
        if (taskId == 0) return
        Bukkit.getScheduler().cancelTask(taskId)
    }

    private fun getOffset(face: BlockFace): Vector {
        return when(face) {
            BlockFace.NORTH -> Vector(0.0, 0.0, -0.2)
            BlockFace.SOUTH -> Vector(0.0, 0.0, 0.2)
            BlockFace.WEST -> Vector(-0.2, 0.0, 0.0)
            BlockFace.EAST -> Vector(0.2, 0.0, 0.0)
            else -> Vector(0, 0, 0)
        }
    }
}