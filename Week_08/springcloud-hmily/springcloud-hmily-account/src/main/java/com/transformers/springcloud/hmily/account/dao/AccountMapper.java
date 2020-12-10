package com.transformers.springcloud.hmily.account.dao;

import com.transformers.springcloud.hmily.account.dto.AccountDTO;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMapper {

    /**
     * 冻结资金
     *
     * @param accountDTO 用户ID和金额对象
     */
    @Update(" update account " +
            " set balance = balance - #{amount}, " +
            " freeze_amount= freeze_amount + #{amount} ,update_time = now() " +
            " where user_id = #{userId}  and  balance > 0  ")
    void update(AccountDTO accountDTO);

    /**
     * 分布式事务成功，将冻结资金释放
     *
     * @param accountDTO 用户ID和金额对象
     * @return
     */
    @Update(" update account " +
            " set freeze_amount= freeze_amount - #{amount}, " +
            " update_time = now() " +
            " where user_id = #{userId}  and freeze_amount >0 ")
    int confirm(AccountDTO accountDTO);

    /**
     * 分布式事务失败，将冻结资金还给账户
     *
     * @param accountDTO 用户ID和金额对象
     * @return
     */
    @Update(" update account " +
            " set balance = balance + #{amount}, " +
            " freeze_amount= freeze_amount - #{amount}," +
            " update_time = now() " +
            " where user_id = #{userId} and freeze_amount >0")
    int cancel(AccountDTO accountDTO);

}
