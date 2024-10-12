package com.yichen.yioj.judge.impl;

import com.yichen.yioj.judge.codesandbox.model.JudgeInfo;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.yichen.yioj.common.ErrorCode;
import com.yichen.yioj.exception.BusinessException;
import com.yichen.yioj.judge.JudgeService;
import com.yichen.yioj.judge.codesandbox.CodeSandBox;
import com.yichen.yioj.judge.codesandbox.factory.CodeBoxFactory;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yichen.yioj.model.entity.Question;
import com.yichen.yioj.model.entity.QuestionSubmit;
import com.yichen.yioj.model.enums.QuestionSubmitStatusEnum;
import com.yichen.yioj.model.vo.JudgeCase;
import com.yichen.yioj.model.vo.JudgeConfig;
import com.yichen.yioj.model.vo.QuestionSubmitVO;
import com.yichen.yioj.service.QuestionService;
import com.yichen.yioj.service.QuestionSubmitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmitVO doJudge(long questionSubmitId) {
        // 1. 传入题目的提交id 获取到对应的题目、提交信息（代码、编程语言等）
        // 2. 如果题目的提交状态不为等待中， 就不用重复执行了
        // 2. 更改题目的提交状态为判题中（这样如果用户重复提交（代码一样 语言一样。。），那么就不重复执行, 因为第二步直接return了）
        // todo 3. 代码沙箱是个耗时操作,(消息队列？？？)
        // 3. 调用沙箱， 获取到执行结果
        // 4. 根据沙箱的执行结果， 设置题目的判题状态和信息

        // 1
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);


        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交信息不存在");
        }
        long id = questionSubmit.getId();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeInfo = questionSubmit.getJudgeInfo();
        Integer status = questionSubmit.getStatus();
        long questionId = questionSubmit.getQuestionId();
        long userId = questionSubmit.getUserId();

        // 2 调用沙箱
        CodeSandBox codeSandBox = CodeBoxFactory.newInstance(type);
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
//        executeCodeRequest.setInputList(Lists.newArrayList());
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage(language);
        // inputList怎么搞？    =>  有judgeCase
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 如果不为等待状态
        if (!questionSubmit.getStatus().equals(Integer.parseInt(QuestionSubmitStatusEnum.WATTING.getValue()))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目已经在判题");
        }

        // 更改题目的状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(Integer.parseInt(QuestionSubmitStatusEnum.RUNNING.getValue()));
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新题目状态为Running失败");
        }

        // todo 使用代理的代码沙箱
        // 调用代码沙箱
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        JudgeConfig judgeConfigBean = JSONUtil.toBean(judgeConfig, JudgeConfig.class);

        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        List<String> outputList = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        executeCodeRequest.setInputList(inputList);

        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);

        // 对比执行结果
        List<String> boxOutputList = executeCodeResponse.getOutputList();
        String boxMessage = executeCodeResponse.getMessage();
        int boxStatus = executeCodeResponse.getStatus();
        JudgeInfo boxJudgeInfo = executeCodeResponse.getJudgeInfo();

        QuestionSubmitVO result = new QuestionSubmitVO();
        result.setLanguage(language);
        result.setCode(code);
        result.setJudgeInfo(boxJudgeInfo);
//        result.setStatus(0);
        result.setQuestionId(questionId);
        result.setUserId(userId);

        // todo status封装成Enum
        if (boxStatus == 0) {
            result.setStatus(0);
            return result;
        }

        // todo 对比沙箱运行的结果 和 预期的结果的区别
        if (boxJudgeInfo.getMemory() > judgeConfigBean.getMemoryLimit() || boxJudgeInfo.getTime() > judgeConfigBean.getTimeLimit()) {
            result.setStatus(0);
            return result;
        }
        if (boxOutputList.size() != outputList.size()) {
            result.setStatus(0);
            return result;
        }
        int flag = 0;
        for (int i = 0;i<boxOutputList.size();i++) {
            String s = boxOutputList.get(i);
            if (!s.equals(outputList.get(i))) {
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            result.setStatus(0);
            return result;
        }
        // 1 表示成功 0表示失败

        // todo 这段后面的判题逻辑可以封装成一个函数
        result.setStatus(1);
        return null;
    }

    // todo 使用策略模式 解决针对不同的语言、不同的题目使用不同的代码沙箱的问题
    // 比如原本设定的timeLimit是针对C++的， 执行java可能要额外花10s， 因此需要不同的判题策略
}
