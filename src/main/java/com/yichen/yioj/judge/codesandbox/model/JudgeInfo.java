package com.yichen.yioj.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;
    /**
     * 消耗内存
     */
    private Long memory;
    /**
     * 消耗时间
     */
    private Long time;
}
