package com.jh.automatic_titrator.common.utils;

import com.jh.automatic_titrator.entity.common.titrator.TitratorEndPoint;

import java.util.Collection;
import java.util.List;

public class CollectionUtils {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static int size(List titratorEndPoints) {
        return titratorEndPoints != null ? titratorEndPoints.size() : 0;
    }
}
