package net.snowshock.goldentreasures.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcussiveExplosion extends Explosion {

    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID + ".ConcussiveExplosion");

    /**
     * whether or not the explosion sets fire to blocks around it
     */
    public boolean isFlaming = false;
    public boolean field_82755_b = true;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public Entity exploder;
    public float explosionSize;
    public boolean hurtsPlayer;
    private World worldObj;
    private Map playerVectors = new HashMap();
    private EntityPlayer shootingEntity;

    public ConcussiveExplosion(World par1World, Entity par2Entity, EntityPlayer par3Entity, double par3, double par5, double par7, float par9) {
        super(par1World, par2Entity, par3, par5, par7, par9);
        worldObj = par1World;
        exploder = par2Entity;
        shootingEntity = par3Entity;
        explosionSize = par9;
        explosionX = par3;
        explosionY = par5;
        explosionZ = par7;
    }

    public static ConcussiveExplosion customConcussiveExplosion(Entity par1Entity, EntityPlayer player, double par2, double par4, double par6, float par8, boolean par9, boolean par10) {
        ConcussiveExplosion var11 = new ConcussiveExplosion(par1Entity.worldObj, par1Entity, player, par2, par4, par6, par8);
        var11.isFlaming = par9;
        var11.isSmoking = par10;
        var11.doExplosionA();
        var11.doExplosionB(false);

        return var11;
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    @Override
    public void doExplosionA() {

        // No need to do the destruction part on the client. The server will tell us.
        if(worldObj.isRemote) {
            return;
        }

        // Calling it a radius even though this isn't actually a sphere...
        float explosionRadius = explosionSize * 2.0F;

        final int xStart = MathHelper.floor_double(explosionX - explosionRadius - 1.0D);
        final int xEnd = MathHelper.floor_double(explosionX + explosionRadius + 1.0D);
        final int yStart = MathHelper.floor_double(explosionY - explosionRadius - 1.0D);
        final int yEnd = MathHelper.floor_double(explosionY + explosionRadius + 1.0D);
        final int zStart = MathHelper.floor_double(explosionZ - explosionRadius - 1.0D);
        final int zEnd = MathHelper.floor_double(explosionZ + explosionRadius + 1.0D);
        final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(xStart, yStart, zStart, xEnd, yEnd, zEnd);
        final List<Entity> entitiesInExplosion = worldObj.getEntitiesWithinAABBExcludingEntity(exploder, boundingBox);

        final Vec3 origin = Vec3.createVectorHelper(explosionX, explosionY, explosionZ);

        for (Entity entity : entitiesInExplosion) {

            if (!(entity instanceof EntityLiving)) {
                // Don't damage things that aren't alive
                continue;
            }
            if (entity == shootingEntity && !hurtsPlayer) {
                // Don't hurt the player if we're not meant to.
                continue;
            }

            final Vec3 entityPosition = Vec3.createVectorHelper(entity.posX,
                    entity.posY + entity.getEyeHeight(),
                    entity.posZ);

            final Vec3 explosionToEntity = entityPosition.subtract(origin);
            final Vec3 unitVector = explosionToEntity.normalize();

            final double distanceFromExplosion = explosionToEntity.lengthVector();
            final double distanceAsFractionOfRadius = distanceFromExplosion / explosionRadius;
            if (distanceAsFractionOfRadius <= 1.0D) {

                if (distanceFromExplosion != 0.0D) {
                    final double blockDensity = worldObj.getBlockDensity(origin, entity.boundingBox);
                    final double intensity = (1.0D - distanceAsFractionOfRadius) * blockDensity;
                    final int damageAmount = (int) ((intensity * intensity + intensity) * 6.0D * (explosionRadius * 2) + 3.0D);
                    entity.attackEntityFrom(DamageSource.causePlayerDamage(shootingEntity),
                            damageAmount);

                    pushEntity(entity, intensity, explosionToEntity.normalize());
                    LOGGER.trace("{damage:\"{}\", distance: \"{}\", distanceNorm: \"{}\", density: \"{}\", entity: \"{}\"", damageAmount, distanceFromExplosion, distanceAsFractionOfRadius, blockDensity, entity);
                }
            } else {
                LOGGER.trace("{damage: \"none\", distanceNorm: \"{}\", entity : \"{}\"", distanceAsFractionOfRadius, entity);
            }
        }
    }

    private void pushEntity(Entity entity, double intensity, Vec3 directionVector) {
        entity.motionX += directionVector.xCoord * intensity;
        entity.motionY += directionVector.yCoord * intensity;
        entity.motionZ += directionVector.zCoord * intensity;

        if (entity instanceof EntityPlayer) {
            playerVectors.put(entity, directionVector);
        }
    }

    /**
     * Does the second part of the explosion (sounds, particles, drop spawn)
     */
    @Override
    public void doExplosionB(boolean par1) {
        worldObj.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

        if (explosionSize >= 2.0F && field_82755_b) {
            worldObj.spawnParticle("hugeexplosion", explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D);
        } else {
            worldObj.spawnParticle("largeexplode", explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D);
        }
    }

    @Override
    /**
     * Gets a map of players affected by this explosion and their motion vectors as a result of the explosion.
     *
     * Used to send packets to clients to tell players they've been 'sploded.
     */
    public Map func_77277_b() {
        return playerVectors;
    }

}
