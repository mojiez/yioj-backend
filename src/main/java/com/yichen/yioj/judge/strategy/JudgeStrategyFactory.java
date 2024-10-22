package com.yichen.yioj.judge.strategy;

import com.yichen.yioj.judge.strategy.DefaultJudgeStrategy;
import com.yichen.yioj.judge.strategy.JavaLanguageJudgeStrategy;
import com.yichen.yioj.judge.strategy.JudgeStrategy;

public class JudgeStrategyFactory {
    public static JudgeStrategy getStrategy(String language) {
        if (language.equals("java")) {
            return new JavaLanguageJudgeStrategy();
        }
        return new DefaultJudgeStrategy();
    }
}
