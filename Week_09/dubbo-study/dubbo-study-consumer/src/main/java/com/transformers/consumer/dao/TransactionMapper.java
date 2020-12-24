package com.transformers.consumer.dao;

import com.transformers.api.entity.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TransactionMapper {

    @Insert(" insert into t_transaction(from_account_id, to_account_id, `type`, amount, create_time, update_time)" +
            " values(#{transaction.fromAccountId}, #{transaction.toAccountId}, #{transaction.type} " +
            " , #{transaction.amount}, #{transaction.createTime}, #{transaction.updateTime}) ")
    int insert(@Param("transaction") Transaction transaction);

}
