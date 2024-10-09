package com.yichen.yioj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.yioj.model.dto.post.PostQueryRequest;
import com.yichen.yioj.model.dto.question.QuestionQueryRequest;
import com.yichen.yioj.model.entity.Post;
import com.yichen.yioj.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.yioj.model.vo.PostVO;
import com.yichen.yioj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface QuestionService extends IService<Question> {
    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
