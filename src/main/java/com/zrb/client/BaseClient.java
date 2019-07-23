package com.zrb.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.zrb.component.common.GlobalEnv;
import com.zrb.model.client.BaseClientResp;
import com.zrb.tool.JsonTool;
import com.zrb.tool.StringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Hardy
 * Date:   2018/7/20 14:43
 * Description:
 **/
public abstract class BaseClient {

    private static final Logger log = LoggerFactory.getLogger(BaseClient.class);

    final <T> BaseClientResp<T> parseJson(String json, Class<T> type) {
        if (StringTool.isEmpty(json)) return null;
        BaseClientResp<T> resp = null;
        try {
            resp = JSON.parseObject(json,
                    new TypeReference<BaseClientResp<T>>(type) {
                    },
                    Feature.IgnoreNotMatch);
        } catch (Exception e) {
            log.error("[PARSE_JSON] json: {}, type: {}, error: {}", json, type, e.getMessage());
        }
        return resp;
    }

    final <T> T parseJsonBody(String json, Class<T> type) {
        if (StringTool.isEmpty(json)) return null;
        try {
            return JsonTool.toObject(json, type);
        } catch (Exception e) {
            log.error("[PARSE_JSON] json: {}, type: {}, error: {}", json, type, e.getMessage());
        }
        return null;
    }

    final boolean check(BaseClientResp resp, int... duplicateNos) {
        if (resp == null) return false;

        // 幂等判断
        if (duplicateNos != null && duplicateNos.length > 0) {
            for (int duplicateNo : duplicateNos) {
                if (resp.getErrorNo() == duplicateNo) return true;
            }
        }

        // 成功
        return resp.getErrorNo() == 0 && StringTool.isEmpty(resp.getErrorMessage());
    }

    final boolean checkDev(GlobalEnv env, Object o) {
        if ("dev".equals(env.env)) {
            log.info("[{}] 开发环境,跳过调用 ╮(￣▽￣)╭ {}", o.getClass().getSimpleName(), JsonTool.toJson(o));
            return true;
        }
        return false;
    }
}


