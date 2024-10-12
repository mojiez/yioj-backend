package com.yichen.yioj.judge.codesandbox.impl;

import com.yichen.yioj.judge.codesandbox.CodeSandBox;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeResponse;

public class ThirdPartyCodeSandbox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("run ThirdPartyCodeSandbox");
        return null;
    }
}
