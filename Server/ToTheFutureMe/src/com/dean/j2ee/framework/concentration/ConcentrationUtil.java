package com.dean.j2ee.framework.concentration;

import java.util.List;
import java.util.Map;

/**
 * Created by Dean on 2016/12/1.
 */
public class ConcentrationUtil {

    public static boolean isEmpty(Map map) {
        return map == null || map.size() <= 0;
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() <= 0;
    }

}
