package de.raphaelgoetz.betterbuild.utils

import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.*

class VoidGenerator : ChunkGenerator() {

    override fun generateBedrock(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {

        val x = 0; val y = 64; val z = 0

        if (x >= chunkX * 16 && (x < (chunkX + 1) * 16)) return
        if (!(z >= chunkZ * 16) && (z < (chunkZ + 1) * 16)) return
        chunkData.setBlock(x, y, z, Material.BEDROCK);
    }
}