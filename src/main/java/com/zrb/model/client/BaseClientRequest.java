package com.zrb.model.client;

import com.riverstar.tool.DebugTool;
import com.riverstar.tool.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Hardy
 * Date:   2018/12/5
 * Description:
 **/
public abstract class BaseClientRequest {

    private static final Logger log = LoggerFactory.getLogger(BaseClientRequest.class);

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(this) == null) continue;

                if (field.getType() == String.class)
                    map.put(field.getName(), (String) field.get(this));
                else
                    map.put(field.getName(), JsonTool.toJson(field.get(this)));
            } catch (IllegalAccessException e) {
                log.error("[转换Map-异常] class: {}, flied: {}, err: {}", this.getClass(), field.getName(), DebugTool.getMessage(e));
            }
        }
        return map;
    }

    public Map<String, String> toForm() {
        Map<String, String> map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(this) == null) continue;

                if (field.getType() == String.class)
                    map.put(field.getName(), (String) field.get(this));
                else if (field.getType() == List.class) {
                    List list = (List) field.get(this);
                    for (int i = 0; i < list.size(); i++) {
                        Object child = list.get(i);
                        Field[] cFields = child.getClass().getDeclaredFields();
                        for (Field cField : cFields) {
                            cField.setAccessible(true);
                            if (field.getType() == String.class)
                                map.put(field.getName() + "[" + i + "]." + cField.getName(), (String) cField.get(child));
                            else
                                map.put(field.getName() + "[" + i + "]." + cField.getName(), String.valueOf(cField.get(child)));
                        }
                    }
                } else {
                    map.put(field.getName(), String.valueOf(field.get(this)));
                }
            } catch (IllegalAccessException e) {
                log.error("[转换Form-异常] class: {}, flied: {}, err: {}", this.getClass(), field.getName(), DebugTool.getMessage(e));
            }
        }
        return map;
    }
}
