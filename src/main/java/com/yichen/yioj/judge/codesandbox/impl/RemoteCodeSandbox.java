package com.yichen.yioj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yichen.yioj.common.ErrorCode;
import com.yichen.yioj.exception.BusinessException;
import com.yichen.yioj.judge.codesandbox.CodeSandBox;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

public class RemoteCodeSandbox implements CodeSandBox {
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("run RemoteCodeSandbox");
        String url = "http://localhost:8091/executeCode";
        // 将对象转为json 前端有js对象， 后端java也有对象， json格式是用于前后端之前传递的
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        // hutool工具发送http请求
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                // 取返回结果中的body信息
                .body();
        System.out.println(responseStr);
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "执行远程沙箱失败");
        }

        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
        return executeCodeResponse;
    }
}
