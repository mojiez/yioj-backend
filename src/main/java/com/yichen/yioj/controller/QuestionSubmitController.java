package com.yichen.yioj.controller;

import com.yichen.yioj.common.BaseResponse;
import com.yichen.yioj.common.ErrorCode;
import com.yichen.yioj.common.ResultUtils;
import com.yichen.yioj.exception.BusinessException;
import com.yichen.yioj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yichen.yioj.model.entity.User;
import com.yichen.yioj.model.vo.QuestionSubmitVO;
import com.yichen.yioj.service.QuestionSubmitService;
import com.yichen.yioj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 问题提交接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
//@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

//    @Resource
//    private QuestionSubmitService questionSubmitService;
//
//    @Resource
//    private UserService userService;

//    /**
//     * 提交题目
//     *
//     * @param questionSubmitAddRequest
//     * @param request
//     * @return resultNum 本次点赞变化数
//     */
//    @PostMapping("/")
//    public BaseResponse<QuestionSubmitVO> doSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
//                                                   HttpServletRequest request) {
//        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 登录才能提交
//        final User loginUser = userService.getLoginUser(request);
//        long questionId = questionSubmitAddRequest.getQuestionId();
//        QuestionSubmitVO result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
//        return ResultUtils.success(result);
//    }
}
