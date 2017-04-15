package at.c02.tempus.utils;

import com.fernandocejas.arrow.functions.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public class CollectionUtils {

    public static <S, T> List<T> convertList(Collection<S> sourceCollection, Function<S, T> converter) {
        List<T> targetCollection = new ArrayList<T>();
        for (S sourceItem : sourceCollection) {
            targetCollection.add(converter.apply(sourceItem));
        }
        return targetCollection;
    }
}
