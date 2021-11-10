package com.riverstar.client;

import com.riverstar.component.common.GlobalEnv;
import com.riverstar.exception.ErrCode;
import com.riverstar.exception.SrvException;
import com.riverstar.model.client.BaseClientResp;
import com.riverstar.model.client.TestServerRequest;
import com.riverstar.model.client.TestServerResp;
import com.riverstar.tool.HttpTool;
import com.riverstar.tool.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: Hardy
 * Date:   2019/7/10
 * Description:
 **/
@Component
public class TestClient extends BaseClient {

    private static final Logger log = LoggerFactory.getLogger(TestClient.class);

    private final String domain;
    private final GlobalEnv env;

    private static final Integer APP_ID = 123;
    private static final String TEST_SERVER = "/test/server";

    @Autowired
    public TestClient(@Value("${zrb.test-domain:}") String domain, GlobalEnv env) {
        this.domain = domain;
        this.env = env;
    }

    public TestServerResp test(TestServerRequest param) {
        // 环境mock
        //if (!env.isProd()) return "1";

        String json = JsonTool.toJson(param);

        // 加密
        //Map<String, String> paramMap = param.toMap();
        //SecZrbTool.encDataToHex(paramMap, GlobalConst.APP_ID);
        //String body = JsonTool.toJson(paramMap);

        String resJson = HttpTool.postJson(domain + TEST_SERVER, json);
        if (resJson == null)
            throw new SrvException(ErrCode.CALL_ERROR, "[test] param: {}, 调用失败: 返回值错误: null", json);

        // 解析调用结果
        BaseClientResp<TestServerResp> resp = parseJson(resJson, TestServerResp.class);
        if (resp == null)
            throw new SrvException(ErrCode.CALL_ERROR, "[test] param: {}, 调用失败: 返回值解析失败: {}", json, resJson);

        // 调用错误 错误码
        if (!check(resp))
            throw new SrvException(ErrCode.CALL_ERROR, "[test] param: {}, 调用失败: 返回值错误码错误: {}", json, resJson);

        return resp.getData();
    }
}
