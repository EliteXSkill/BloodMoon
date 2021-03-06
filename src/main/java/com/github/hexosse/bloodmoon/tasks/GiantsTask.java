package com.github.hexosse.bloodmoon.tasks;

import com.github.hexosse.bloodmoon.BloodMoon;
import com.github.hexosse.bloodmoon.nms.EntityGiantZombie;
import com.github.hexosse.pluginframework.pluginapi.PluginTask;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.Random;

public final class GiantsTask extends PluginTask<BloodMoon>
{

    private final Random random = new Random();
    private final World world;

    public GiantsTask(BloodMoon plugin, World world) {
        super(plugin);
        this.world = world;
    }

    @Override
    public void run()
    {
        long worldTime = world.getTime();

        if (worldTime < 13000 || worldTime > 23000) {
            return;
        }

        spawn:
        for (Chunk chunk : world.getLoadedChunks()) {
            if (random.nextInt(100) == 1) {
                int x = (chunk.getX() * 16) + random.nextInt(12) + 2;
                int z = (chunk.getZ() * 16) + random.nextInt(12) + 2;
                int y = world.getHighestBlockYAt(x, z);

                Location spawnLocation = new Location(world, x, y, z);

                for (Entity entity : world.getLivingEntities()) {
                    if (!entity.isDead() && entity.getLocation().distanceSquared(spawnLocation) < 1024) {
                        continue spawn;
                    }
                }

                EntityGiantZombie entity = new EntityGiantZombie(((CraftWorld) world).getHandle());

                entity.setPositionRotation(x, y, z, 0, 90);
                ((CraftWorld) world).getHandle().addEntity(entity, SpawnReason.CUSTOM);
                entity.z(null);
            }
        }
    }

}
