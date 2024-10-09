package com.yichen.yioj.service;

import com.yichen.yioj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.yioj.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 提交题目
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    int doQuestionSubmit(long questionId, User loginUser);

    @Transactional(rollbackFor = Exception.class)
    int doQuestionSubmitInner(long userId, long questionId);
}