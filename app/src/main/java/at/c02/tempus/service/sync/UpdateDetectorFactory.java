package at.c02.tempus.service.sync;

import com.fernandocejas.arrow.functions.Function;

import java.util.Objects;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public class UpdateDetectorFactory {

    /**
     * UpdateDetector-Factory für Properties des Items
     *
     * @param properties
     * @param <T>
     * @return
     */
    public static <T> UpdateDetector<T> create(Function<T, Object>... properties) {
        return (source, target) -> {
            // isUpdated?
            for (Function<T, Object> property : properties) {
                if (!Objects.equals(property.apply(source), property.apply(target))) {
                    return true;
                }
            }
            return false;
        };
    }
}
