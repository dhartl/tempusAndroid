package at.c02.tempus.service.mapping;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public class MappingUtils {

    public static Integer fromLong(Long value) {
        if (value == null) {
            return null;
        }
        return value.intValue();
    }

    public static Long fromInt(Integer value) {
        if (value == null) {
            return null;
        }
        return value.longValue();
    }
}
