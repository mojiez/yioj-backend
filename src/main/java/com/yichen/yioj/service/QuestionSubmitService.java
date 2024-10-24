package com.yichen.yioj.service;

import com.yichen.yioj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yichen.yioj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.yioj.model.entity.User;
import com.yichen.yioj.model.vo.QuestionSubmitVO;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    QuestionSubmitVO doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    @Transactional(rollbackFor = Exception.class)
    QuestionSubmitVO doQuestionSubmitInner(long userId, QuestionSubmitAddRequest questionSubmitAddRequest);
}
