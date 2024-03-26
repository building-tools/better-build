package de.raphaelgoetz.betterbuild.manager

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.tasks.GhostTask
import de.raphaelgoetz.betterbuild.utils.tasks.NoClipTask
import de.raphaelgoetz.betterbuild.utils.tasks.Task
import org.bukkit.entity.Player
import java.util.UUID

class TaskManager(private val betterBuild: BetterBuild) {

    private val ghostTasks: MutableMap<UUID, GhostTask> = mutableMapOf()
    private val noClipTasks: MutableMap<UUID, NoClipTask> = mutableMapOf()

    fun register(player: Player, task: Task) {
        //TODO: IMPLEMENT REGISTRATION
    }

    fun remove(uuid: UUID) {
        ghostTasks[uuid]?.stop()
        ghostTasks.remove(uuid)

        noClipTasks[uuid]?.stop()
        noClipTasks.remove(uuid)
    }
}