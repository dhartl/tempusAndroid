package at.c02.tempus.db.converter;

import org.greenrobot.greendao.converter.PropertyConverter;

import at.c02.tempus.db.entity.EntityStatus;

/**
 * Created by Daniel on 09.04.2017.
 */

public class EntityStatusConverter implements PropertyConverter<EntityStatus, Integer> {
    @Override
    public EntityStatus convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        for (EntityStatus role : EntityStatus.values()) {
            if (role.getId() == databaseValue) {
                return role;
            }
        }
        return EntityStatus.UNKNOWN;
    }

    @Override
    public Integer convertToDatabaseValue(EntityStatus entityProperty) {
        return entityProperty == null ? null : entityProperty.getId();
    }
}
