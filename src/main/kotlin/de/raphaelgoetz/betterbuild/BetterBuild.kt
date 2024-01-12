package de.raphaelgoetz.betterbuild

import de.raphaelgoetz.betterbuild.commands.player.TeleportToLastLocation
import de.raphaelgoetz.betterbuild.listeners.block.*
import de.raphaelgoetz.betterbuild.listeners.hanging.HangingBreakByEntityListener
import de.raphaelgoetz.betterbuild.listeners.hanging.HangingPlaceListener
import de.raphaelgoetz.betterbuild.listeners.player.*
import de.raphaelgoetz.betterbuild.listeners.player.connection.PlayerJoinListener
import de.raphaelgoetz.betterbuild.listeners.player.connection.PlayerQuitListener
import de.raphaelgoetz.betterbuild.listeners.raid.RaidTriggerListener
import de.raphaelgoetz.betterbuild.listeners.vehicle.VehicleCreateListener
import de.raphaelgoetz.betterbuild.listeners.vehicle.VehicleEnterListener
import de.raphaelgoetz.betterbuild.manager.PlayerManager
import de.raphaelgoetz.betterbuild.manager.WorldManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.block.BlockGrowEvent
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.block.BlockPistonRetractEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class BetterBuild : JavaPlugin() {

    val playerManager: PlayerManager = PlayerManager()
    val worldManager: WorldManager = WorldManager()

    override fun onEnable() {

        registerListener()
    }
    
    private fun registerListener() {

        val playerManager = Bukkit.getPluginManager()

        //BLOCK
        playerManager.registerEvents(BlockBurnListener(), this)
        playerManager.registerEvents(BlockDispenseArmorListener(), this)
        playerManager.registerEvents(BlockDispenseListener(), this)
        playerManager.registerEvents(BlockDropItemListener(), this)
        playerManager.registerEvents(BlockExplodeListener(), this)
        playerManager.registerEvents(BlockFadeListener(), this)
        playerManager.registerEvents(BlockGrowListener(), this)
        playerManager.registerEvents(BlockIgniteListener(), this)
        playerManager.registerEvents(BlockPhysicsListener(), this)
        playerManager.registerEvents(BlockPistonExtendListener(), this)
        playerManager.registerEvents(BlockPistonListener(), this)
        playerManager.registerEvents(BlockPistonRetractListener(), this)
        playerManager.registerEvents(BlockPlaceListener(this), this)
        playerManager.registerEvents(BlockReceiveGameListener(), this)
        playerManager.registerEvents(BlockSpreadListener(), this)
        playerManager.registerEvents(FluidLevelChangeListener(), this)
        playerManager.registerEvents(LeavesDecayListener(), this)

        //PICTURES
        playerManager.registerEvents(HangingBreakByEntityListener(this), this)
        playerManager.registerEvents(HangingPlaceListener(this), this)

        //PLAYER-CONNECTION
        playerManager.registerEvents(PlayerJoinListener(), this)
        playerManager.registerEvents(PlayerQuitListener(), this)

        //PLAYER-INTERACTION
        playerManager.registerEvents(PlayerArmorStandManipulateListener(this), this)
        playerManager.registerEvents(PlayerBedEnterListener(), this)
        playerManager.registerEvents(PlayerBucketEmptyListener(this), this)
        playerManager.registerEvents(PlayerBucketEntityListener(this), this)
        playerManager.registerEvents(PlayerBucketFillListener(this), this)
        playerManager.registerEvents(PlayerChangedWorldListener(this), this)
        playerManager.registerEvents(PlayerDeathListener(), this)
        playerManager.registerEvents(PlayerDropItemListener(), this)
        playerManager.registerEvents(PlayerEggThrowListener(), this)
        playerManager.registerEvents(PlayerFishListener(), this)
        playerManager.registerEvents(PlayerInteractListener(this), this)
        playerManager.registerEvents(PlayerItemConsumeListener(), this)
        playerManager.registerEvents(PlayerPortalListener(), this)
        playerManager.registerEvents(PlayerSwapHandItemsListener(this), this)
        playerManager.registerEvents(PlayerTeleportListener(this), this)

        //RAID
        playerManager.registerEvents(RaidTriggerListener(), this)

        //VEHICLE
        playerManager.registerEvents(VehicleCreateListener(), this)
        playerManager.registerEvents(VehicleEnterListener(), this)

    }
}