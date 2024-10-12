package com.yichen.yioj.judge.codesandbox.impl;
import com.google.common.collect.Lists;
import com.yichen.yioj.judge.codesandbox.model.JudgeInfo;

import java.util.List;

import com.yichen.yioj.judge.codesandbox.CodeSandBox;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeResponse;

public class ExampleCodeSandbox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();

//        System.out.println("run ExampleCodeSandbox");
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("这是模拟代码沙箱");
        executeCodeResponse.setStatus(0);
        executeCodeResponse.setJudgeInfo(new JudgeInfo("moni",100L,100L));

        return executeCodeResponse;
    }
}
