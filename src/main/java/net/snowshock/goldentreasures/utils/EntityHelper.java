package net.snowshock.goldentreasures.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class EntityHelper {
    public static final String THAUMCRAFT_GOLEM_BASE_CLASS_NAME = "thaumcraft.common.entities.golems.EntityGolemBase";
    private static Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID);

    private static Class golemBaseClass = null;
    private static boolean thaumcraftNotInstalled = false;

    /**
     * Convert an entity type name to an entity class as stored in {@link EntityList}.
     *
     * @param entityName The name of the entity type.
     * @return The class of that type of entity.
     */
    public static Class<? extends Entity> resolveEntityClass(String entityName) {
        Map<String, Class> mapping = EntityList.stringToClassMapping;
        LOGGER.debug("Converting entity name [{}] to class", entityName);
        final Class clazz = mapping.get(entityName);
        if (clazz == null || !Entity.class.isAssignableFrom(clazz)) {
            LOGGER.warn("Tried to resolve entity name but result was not an Entity class. Result was [{}]", clazz);
            return null;
        } else {
            LOGGER.debug("Entity [{}] -> [{}]", entityName, clazz.getName());
            return clazz;
        }
    }

    /**
     * Convert an entity instance to the entity name as stored in {@link EntityList}.
     *
     * @param entity Entity to resolve the name of.
     *
     * @return The entity name for that entity type (not entity instance).
     */
    public static String resolveEntityName(Entity entity)
    {
        return resolveEntityName(entity.getClass());
    }

    /**
     * Convert an entity class to the entity name as stored in {@link EntityList}.
     *
     * @param clazz Entity class to resolve the name of.
     *
     * @return The entity name for that entity type (not entity instance).
     */
    public static String resolveEntityName(Class<? extends Entity> clazz) {
        Map<Class, String> mapping = EntityList.classToStringMapping;
        LOGGER.debug("Converting entity class [{}] to name", clazz);
        final String result = mapping.get(clazz);

        if (result == null)
            LOGGER.warn("Tried to resolve entity result from class [{}] but found nothing", clazz.getName());
        else
            LOGGER.trace("Entity [{}] -> [{}]", clazz.getName(), result);
        return result;
    }

    /**
     * Classifies {@code entity} into one of the types supported by {@link EntityHelper}.
     *
     * @param entity Entity to be classified.
     * @return The type of entity.
     */
    public static EntityType classify(Entity entity) {
        EntityType type = EntityType.OTHER;
        // Don't bother testing for the living types if it's not a living type
        if (entity instanceof EntityLiving) {

            // Test thaumcraft golems first because they will probably also fall under another category.
            if (isThaumcraftGolem(entity))
                type = EntityType.TC_GOLEM;
            else if (entity instanceof IMob)
                type = EntityType.HOSTILE;
            else if (entity instanceof IAnimals)
                type = EntityType.PASSIVE;

        } else if (isProjectile(entity)) {
            return EntityType.PROJECTILE;
        }

        return type;
    }

    private static boolean isProjectile(Entity entity) {
        return IProjectile.class.isAssignableFrom(entity.getClass());
    }

    /**
     * Tests whether {@code entity} is a Thaumcraft golem.
     * <p/>
     * If Thaumcraft is not installed this will be detected on first test and subsequent tests will be skipped.
     *
     * @param entity Entity to test
     * @return true if the entity extends the Thaumcraft golem base class
     * (See {@link #THAUMCRAFT_GOLEM_BASE_CLASS_NAME}).
     */
    private static boolean isThaumcraftGolem(Entity entity) {
        // Shortcut if thaumcraft has already been determined missing
        if (thaumcraftNotInstalled)
            return false;

        final Class baseClass;

        LOGGER.debug("Testing whether [{}] is a thaumcraft golem", entity);
        baseClass = getGolemBaseClass();

        final boolean isGolem = isEntityOfType(entity, baseClass);

        LOGGER.trace("[{}] isGolem = [{}]", entity, isGolem);
        return isGolem;
    }

    /**
     * Checks whether {@code entity} is assignable from {@code baseClass}.
     *
     * @param entity    Entity to test.
     * @param baseClass Base class to test against.
     * @return true if the {@code entity} is a subtype or instance of {@code baseClass}. Otherwise false
     * (notably, when {@code baseClass} is {@code null}).
     */
    private static boolean isEntityOfType(Entity entity, Class baseClass) {
        return baseClass != null && baseClass.isAssignableFrom(entity.getClass());
    }

    private static Class getGolemBaseClass() {
        try {
            if (golemBaseClass == null) {
                golemBaseClass = Class.forName(THAUMCRAFT_GOLEM_BASE_CLASS_NAME);
                LOGGER.trace("Class golem base found. Thaumcraft is installed!");
            }

        } catch (ClassNotFoundException e) {
            LOGGER.info("Thaumcraft is not installed or golem base class missing");
            thaumcraftNotInstalled = true;
            golemBaseClass = null;
        }
        return golemBaseClass;
    }

    public enum EntityType {
        /**
         * When thaumcraft is installed and the entity is a subclass of
         * {@link net.snowshock.goldentreasures.utils.EntityHelper#THAUMCRAFT_GOLEM_BASE_CLASS_NAME}.
         **/
        TC_GOLEM,
        /** When the entity implements {@link IMob}. **/
        HOSTILE,
        /** When the entity implements {@link IAnimals}. **/
        PASSIVE,
        /** When the entity implements {@link IProjectile}. **/
        PROJECTILE,
        /** Default if an entity doesn't fall into one of the other categories. **/
        OTHER
    }

}
