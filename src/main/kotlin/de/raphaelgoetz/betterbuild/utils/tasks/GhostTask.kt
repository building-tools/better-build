package de.raphaelgoetz.betterbuild.utils.tasks

import de.raphaelgoetz.astralis.task.Task
import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Bukkit
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player

class GhostTask(
        val betterBuild: BetterBuild,
        val player: Player,
) : Task {

    private var taskId: Int = 0;
    private val actions: MutableMap<BlockDisplay, Interaction> = mutableMapOf()

    override fun getTaskHolder(): Player {
        return player
    }

    override fun getTask(): Runnable {
        return Runnable {
            for (entity in player.getNearbyEntities(20.0, 20.0, 20.0)) {
                if (entity is BlockDisplay) setupInteraction(entity)
            }
        }
    }

    override fun run() {
        val task = Bukkit.getScheduler().runTaskTimerAsynchronously(betterBuild, getTask(), 20, 20)
        taskId = task.taskId
    }

    override fun stop() {
        if (taskId == 0) return
        Bukkit.getScheduler().cancelTask(taskId)
        actions.forEach { actions ->
            actions.value.remove()
        }
    }

    private fun setupInteraction(entity: BlockDisplay) {
        if (actions[entity] == null) {
            val interaction = entity.world.spawn(entity.location, Interaction::class.java)

            interaction.isGlowing = true
            interaction.interactionHeight = 1.0f
            interaction.interactionWidth = 1.0f

            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                if (onlinePlayer == player) continue
                onlinePlayer.hideEntity(betterBuild, interaction)
            }
        }
    }
}