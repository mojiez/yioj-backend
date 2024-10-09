package com.yichen.yioj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.yioj.common.ErrorCode;
import com.yichen.yioj.exception.BusinessException;
import com.yichen.yioj.model.entity.*;
import com.yichen.yioj.service.QuestionService;
import com.yichen.yioj.service.QuestionSubmitService;
import com.yichen.yioj.mapper.QuestionSubmitMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{
    @Resource
    private QuestionService questionService;
    @Override
    public int doQuestionSubmit(long questionId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交
        long userId = loginUser.getId();
        // 每个用户串行提交？？
        // todo 这里原本是每个用户串行点赞， 改为每个用户串行提交（可行， 因为可能统计提交数）
        // 锁必须要包裹住事务方法
        // 这段代码的目的是在使用 AOP（面向切面编程）时，通过 AopContext.currentProxy() 获取当前的代理对象，并在对用户提交的问题进行处理时使用 synchronized 关键字进行同步。下面是逐步解析这段代码的每个部分：
        QuestionSubmitService questionSubmitService = (QuestionSubmitService) AopContext.currentProxy();
        // String.valueOf(userId).intern()：首先将 userId 转换为字符串，然后调用 intern() 方法。intern() 方法会返回一个字符串常量池中的字符串实例，确保所有相同的字符串字面量指向同一个内存地址。
        synchronized (String.valueOf(userId).intern()) {
            //将 userId 的字符串形式作为锁对象，这意味着对于同一个 userId，在多个线程中只允许一个线程进入 synchronized 块，其他线程会被阻塞，直到当前线程释放锁。
            // 如果是不同的 userId，这段代码中的 synchronized 块会根据不同的 userId 创建不同的锁对象，因此不同的用户提交操作可以同时执行，而不会相互阻塞。
            /**
             * 	•	当两个不同的 userId 被同时传入时，线程在进入 synchronized 块时会根据不同的锁对象（字符串）来进行锁定。这意味着：
             * 	•	线程 A 调用 synchronized 块时，如果 userId = 1，它会获取 "1" 的锁。
             * 	•	线程 B 调用 synchronized 块时，如果 userId = 2，它会获取 "2" 的锁。
             * 	•	由于这两个锁是不同的，因此线程 A 和线程 B 可以同时执行各自的 doQuestionSubmitInner 方法，而不会互相阻塞。
             */

            /**
             * 	•	对于不同的 userId：由于它们产生不同的锁，多个线程可以并发执行各自的操作，而不会互相影响。这确保了系统在处理不同用户提交时的高效性。
             * 	•	对于相同的 userId：如果多个线程尝试同时提交相同用户的问题，它们会被串行化处理，因为它们共享同一个锁。这样可以避免数据冲突或不一致的状态。
             */
            return questionSubmitService.doQuestionSubmitInner(userId, questionId);
        }
    }
    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionSubmitInner(long userId, long questionId) {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);

        // 根据 userId和questionId进行查询
        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>(questionSubmit);
        QuestionSubmit oldQuestionSubmit = this.getOne(questionSubmitQueryWrapper);
        boolean result;
        // 如果说之前已经提交过 更新提交次数就行
        // todo 更新提交正确错误
        if (oldQuestionSubmit != null) {
            result = questionService.update()
                        .eq("id", questionId)
                        .setSql("submitNum = submitNum + 1")
                        .update();
            if (result) {
//                // 如果删除成功 提交数 -1
//                result = questionService.update()
//                        .eq("id", questionId)
//                        .gt("submitNum", 0)
//                        .setSql("submitNum = submitNum - 1")
//                        .update();
                return -1;
            }else {
                // 删除失败
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }else {
            // 之前没有提交过
            result = this.save(questionSubmit);
            if (result) {
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("submitNum = submitNum + 1")
                        .update();
                return result? 1:0;
            }else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }

    }
}




