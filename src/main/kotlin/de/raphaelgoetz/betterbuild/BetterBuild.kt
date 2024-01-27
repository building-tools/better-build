package de.raphaelgoetz.betterbuild

import de.raphaelgoetz.betterbuild.commands.player.*
import de.raphaelgoetz.betterbuild.commands.world.*
import de.raphaelgoetz.betterbuild.listeners.block.*
import de.raphaelgoetz.betterbuild.listeners.custom.PlayerIronDoorInteractListener
import de.raphaelgoetz.betterbuild.listeners.custom.PlayerLitBlockListener
import de.raphaelgoetz.betterbuild.listeners.custom.PlayerSlabBreakListener
import de.raphaelgoetz.betterbuild.listeners.custom.PlayerTerracottaInteractListener
import de.raphaelgoetz.betterbuild.listeners.hanging.*
import de.raphaelgoetz.betterbuild.listeners.player.*
import de.raphaelgoetz.betterbuild.listeners.player.connection.*
import de.raphaelgoetz.betterbuild.listeners.raid.*
import de.raphaelgoetz.betterbuild.listeners.vehicle.*
import de.raphaelgoetz.betterbuild.listeners.world.WorldLoadListener
import de.raphaelgoetz.betterbuild.manager.*

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class BetterBuild : JavaPlugin() {

    val playerManager: PlayerManager = PlayerManager(this)
    val worldManager: WorldManager = WorldManager(this)
    val languageManager: LanguageManager = LanguageManager()

    override fun onEnable() {
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
        register(PlayerJoinListener())
        register(PlayerQuitListener())

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
        register(PlayerSlabBreakListener(this))
    }

    private fun register(listener: Listener) {
        Bukkit.getPluginManager().registerEvents(listener, this)
    }
}