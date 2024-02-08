package de.raphaelgoetz.betterbuild

import de.raphaelgoetz.betterbuild.commands.player.TeleportToLastLocation
import de.raphaelgoetz.betterbuild.commands.player.TogglePlayerGamemode
import de.raphaelgoetz.betterbuild.commands.player.TogglePlayerMode
import de.raphaelgoetz.betterbuild.commands.player.TogglePlayerSpeed
import de.raphaelgoetz.betterbuild.commands.world.MangeWorlds
import de.raphaelgoetz.betterbuild.commands.world.ToggleWorldPhysics
import de.raphaelgoetz.betterbuild.listeners.block.*
import de.raphaelgoetz.betterbuild.listeners.custom.PlayerIronDoorInteractListener
import de.raphaelgoetz.betterbuild.listeners.custom.PlayerLitBlockListener
import de.raphaelgoetz.betterbuild.listeners.custom.PlayerTerracottaInteractListener
import de.raphaelgoetz.betterbuild.listeners.hanging.HangingBreakByEntityListener
import de.raphaelgoetz.betterbuild.listeners.hanging.HangingPlaceListener
import de.raphaelgoetz.betterbuild.listeners.player.*
import de.raphaelgoetz.betterbuild.listeners.player.connection.PlayerJoinListener
import de.raphaelgoetz.betterbuild.listeners.player.connection.PlayerQuitListener
import de.raphaelgoetz.betterbuild.listeners.raid.RaidTriggerListener
import de.raphaelgoetz.betterbuild.listeners.vehicle.VehicleCreateListener
import de.raphaelgoetz.betterbuild.listeners.vehicle.VehicleEnterListener
import de.raphaelgoetz.betterbuild.listeners.world.WorldLoadListener
import de.raphaelgoetz.betterbuild.manager.LanguageManager
import de.raphaelgoetz.betterbuild.manager.PlayerManager
import de.raphaelgoetz.betterbuild.manager.WorldManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class BetterBuild : JavaPlugin() {

    val playerManager: PlayerManager = PlayerManager(this)
    val worldManager: WorldManager = WorldManager(this)

    override fun onEnable() {

        LanguageManager.readConfig()

        registerCommands()
        registerListener()
    }

    private fun registerCommands() {

        //PLAYER
        getCommand("back")?.setExecutor(TeleportToLastLocation(this))
        getCommand("build")?.setExecutor(TogglePlayerMode(this))
        getCommand("clip")?.setExecutor(TogglePlayerMode(this))
        getCommand("fly")?.setExecutor(TogglePlayerSpeed(this))
        getCommand("walk")?.setExecutor(TogglePlayerSpeed(this))
        getCommand("gm")?.setExecutor(TogglePlayerGamemode(this))

        //WORLD
        getCommand("world")?.setExecutor(MangeWorlds(this))
        getCommand("world")?.tabCompleter = MangeWorlds(this)
        getCommand("physics")?.setExecutor(ToggleWorldPhysics(this))
    }

    private fun registerListener() {

        //BLOCK
        register(BlockBurnListener())
        register(BlockDispenseArmorListener())
        register(BlockDispenseListener())
        register(BlockDropItemListener())
        register(BlockExplodeListener())
        register(BlockFadeListener())
        register(BlockGrowListener())
        register(BlockIgniteListener())
        register(BlockPhysicsListener(this))
        register(BlockPistonExtendListener())
        register(BlockPistonRetractListener())
        register(BlockPlaceListener(this))
        register(BlockReceiveGameListener())
        register(BlockSpreadListener())
        register(FluidLevelChangeListener())
        register(LeavesDecayListener())

        //PICTURES
        register(HangingBreakByEntityListener(this))
        register(HangingPlaceListener(this))

        //PLAYER-CONNECTION
        register(PlayerJoinListener(this))
        register(PlayerQuitListener(this))

        //PLAYER-INTERACTION
        register(PlayerArmorStandManipulateListener(this))
        register(PlayerAsyncChatListener(this))
        register(PlayerBedEnterListener())
        register(PlayerBucketEmptyListener(this))
        register(PlayerBucketEntityListener(this))
        register(PlayerBucketFillListener(this))
        register(PlayerChangedWorldListener(this))
        register(PlayerDamageListener())
        register(PlayerDeathListener())
        register(PlayerDropItemListener())
        register(PlayerEggThrowListener())
        register(PlayerFishListener())
        register(PlayerInteractListener(this))
        register(PlayerInteractEntityListener(this))
        register(PlayerItemConsumeListener())
        register(PlayerPortalListener())
        register(PlayerSwapHandItemsListener(this))
        register(PlayerTeleportListener(this))

        //RAID
        register(RaidTriggerListener())

        //VEHICLE
        register(VehicleCreateListener())
        register(VehicleEnterListener())

        //WORLD
        register(WorldLoadListener(this))

        //CUSTOM
        register(PlayerLitBlockListener(this))
        register(PlayerTerracottaInteractListener(this))
        register(PlayerIronDoorInteractListener(this))
    }

    private fun register(listener: Listener) {
        Bukkit.getPluginManager().registerEvents(listener, this)
    }
}