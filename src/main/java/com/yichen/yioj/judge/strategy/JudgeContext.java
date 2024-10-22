package com.yichen.yioj.judge.strategy;

import com.yichen.yioj.judge.codesandbox.model.JudgeInfo;
import com.yichen.yioj.model.entity.Question;
import com.yichen.yioj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文 用于定义在策略中传递的参数
 */
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;
    private List<String> inputList;
    private List<String> outputList;
    private Question question;
    /**
     * 执行状态
     */
    private Integer status;
}
