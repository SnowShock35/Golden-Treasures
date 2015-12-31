package net.snowshock.goldentreasures.interdiction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.utils.EntityHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static net.snowshock.goldentreasures.utils.EntityHelper.resolveEntityName;

public class InterdictionField {
    private static Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID);
    public final int fieldRadius;

    final Map<EntityHelper.EntityType, Boolean> entityTypesEnabled;
    List<String> whitelist;
    List<String> blacklist;

    public InterdictionField(int fieldRadius, Map<EntityHelper.EntityType, Boolean> entityTypeConfiguration, List<String> whitelist, List<String> blacklist) {
        this.fieldRadius = fieldRadius;
        this.entityTypesEnabled = entityTypeConfiguration;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
        initEntityTypeValues(entityTypeConfiguration);
    }

    /**
     * Sets any entity type which isn't present in {@code entityTypesEnabled} to false;
     *
     * @param entityTypesEnabled Entity type configuration map.
     */
    private void initEntityTypeValues(Map<EntityHelper.EntityType, Boolean> entityTypesEnabled) {
        for (EntityHelper.EntityType type : EntityHelper.EntityType.values()) {
            if (entityTypesEnabled.get(type) == null) {
                entityTypesEnabled.put(type, false);
            }
        }
    }

    public void doInterdictionTick(World world, int x, int y, int z) {
        int radius = fieldRadius;

        List<Entity> entities = (List<Entity>) world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));
        for (Entity entity : entities) {
            if (canIPush(entity)) {
                LOGGER.trace("Pushing entity [{}] of class [{}]", entity, entity.getClass());
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
            final boolean canPush;
            final String entityName = resolveEntityName(entity);
            if (whitelist.contains(entityName)) {
                canPush = false;
            } else if (blacklist.contains(entityName)) {
                canPush = true;
            } else {
                final EntityHelper.EntityType type = EntityHelper.classify(entity);
                canPush = entityTypesEnabled.get(type);
            }
            return canPush;
        }
    }
}