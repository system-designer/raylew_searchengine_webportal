package com.searchengine.webportal.utils;

import com.searchengine.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liold on 2014/11/22.
 */
public class ConversionUtils {

    /**
     * 把Long类型的列表转换成“,”分隔的字符串
     * @param items
     * @return
     */
    public static String longListToString(List<Long> items) {
        StringBuilder sb = new StringBuilder("");
        if (items != null && items.size() > 0) {
            for (Long item : items) {
                if(sb.length()>0){
                    sb.append(",");
                }
                sb.append(Long.toString(item));
            }
        }
        return sb.toString();
    }

    /**
     * 把“,”分隔的字符串转成Long类型的列表
     * @param longStr
     * @return
     */
    public static ArrayList<Long> stringToLongList(String longStr){
        if(StringUtils.isEmpty(longStr)) {
            return null;
        }
        String[] arr = longStr.split(",");
        ArrayList<Long> list = new ArrayList<Long>();
        for(int i =0;i<arr.length;i++) {
            list.add(Long.parseLong(arr[i]));
        }
        return list;
    }
}
