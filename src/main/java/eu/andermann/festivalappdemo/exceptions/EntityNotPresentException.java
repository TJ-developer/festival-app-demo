package eu.andermann.festivalappdemo.exceptions;

import java.util.UUID;

public class EntityNotPresentException extends RuntimeException {
    public EntityNotPresentException(UUID entityId, Class<?> classType) {
        super("Entity of type " + classType.getName() + " with id " + entityId.toString());
    }

    public EntityNotPresentException(String entityName, Class<?> classType) {
        super("Entity of type " + classType.getName() + " with id " + entityName);
    }
}
