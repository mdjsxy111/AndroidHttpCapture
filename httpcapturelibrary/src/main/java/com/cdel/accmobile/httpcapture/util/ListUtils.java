package com.cdel.accmobile.httpcapture.util;

import java.util.List;

/**
 * ListUtils工具类
 *
 * @author zhangbaoyu
 * @time 2/7/21 4:07 PM
 */
public class ListUtils {

    /**
     * 判断list是否为空
     *
     * @param sourceList 资源集合
     * @author zhangbaoyu
     * @time 2/7/21 4:09 PM
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
        return sourceList == null || sourceList.size() == 0;
    }
}
