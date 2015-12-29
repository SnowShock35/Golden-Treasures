package net.snowshock.goldentreasures.interdiction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class InterdictionField {
    public final int fieldRadius;

    public InterdictionField(int fieldRadius) {
        this.fieldRadius = fieldRadius;
    }

    public void doInterdictionTick(World world, int x, int y, int z) {
        int radius = fieldRadius;

        List<Entity> entities = (List<Entity>) world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));
        for (Entity entity : entities) {
            if (canIPush(entity)) {
                double distance = entity.getDistance((double) x, (double) y, (double) z);
                if (distance >= radius || distance == 0)
                    continue;

                // the multiplier is based on a set rate added to an inverse
                // proportion to the distance.
                // we raise the distance to 1 if it's less than one, or it becomes a
                // crazy multiplier we don't want/need.
                if (distance < 1D)
                    distance = 1D;
                double knockbackMultiplier = 1D + (1D / distance);

                // we also need a reduction coefficient because the above force is
                // WAY TOO MUCH to apply every tick.
                double reductionCoefficient = 0.04D;

                // the resultant vector between the two 3d coordinates is the
                // difference of each coordinate pair
                // note that we do not add 0.5 to the y coord, if we wanted to be
                // SUPER accurate, we would be using
                // the entity height offset to find its "center of mass"
                Vec3 angleOfAttack = Vec3.createVectorHelper(entity.posX - (x + 0.5D), entity.posY - y, entity.posZ - (z + 0.5D));

                // we use the resultant vector to determine the force to apply.
                double xForce = angleOfAttack.xCoord * knockbackMultiplier * reductionCoefficient;
                double yForce = angleOfAttack.yCoord * knockbackMultiplier * reductionCoefficient;
                double zForce = angleOfAttack.zCoord * knockbackMultiplier * reductionCoefficient;
                entity.motionX += xForce;
                entity.motionY += yForce;
                entity.motionZ += zForce;
            }
        }
    }

    /**
     * Decides whether or not {@code entity} may be pushed by the torch.
     * <p/>
     * TODO Currently says that anything may be pushed. Will update to read a config later.
     *
     * @param entity Entity being tested for pushability
     * @return true if the entity is allowed to be pushed
     */
    public boolean canIPush(Entity entity) {
        if (entity instanceof EntityPlayer)
            return false;
        else {
            //Class entityClass = entity.getClass();
            //String entityName = (String) EntityList.classToStringMapping.get(entityClass);
            //List<String> entitiesThatCanBePushed = (List<String>) ReferencesConfigInfo
            //List<String> projectilesThatCanBePushed = (List<String>) ReferencesConfigInfo.GeneralConfigs.get(ReferencesModBlocks.GOLDEN_TORCH, "projectiles_that_can_be_pushed");
            //ReferencesConfigInfo.GeneralConfigs.CAN_PROJECTILES_BE_PUSHED
            return true;
        }
    }
}