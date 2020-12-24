package com.transformers.consumer.dao;

import com.transformers.api.dto.AccountDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface AccountMapper {

    @Update(" update t_account " +
            " set " +
            " rmb_balance = rmb_balance - #{accountDTO.rmbAmount}, " +
            " rmb_freeze_amount = rmb_freeze_amount + #{accountDTO.rmbAmount} " +
            " where account_id = #{accountDTO.accountId} ")
    int freezeRmb(@Param("accountDTO") AccountDTO accountDTO);

    @Update(" update t_account " +
            " set " +
            " rmb_freeze_amount = rmb_freeze_amount - #{accountDTO.rmbAmount}, " +
            " usd_balance = usd_balance + #{accountDTO.usdAmount} " +
            " where account_id = #{accountDTO.accountId} ")
    int confirmRmbToUsd(@Param("accountDTO") AccountDTO accountDTO);

    @Update(" update t_account " +
            " set " +
            " rmb_balance = rmb_balance + #{accountDTO.rmbAmount}, " +
            " rmb_freeze_amount = rmb_freeze_amount - #{accountDTO.rmbAmount} " +
            " where account_id = #{accountDTO.accountId} ")
    int cancelRmbToUsd(@Param("accountDTO") AccountDTO accountDTO);

    @Update(" update t_account " +
            " set " +
            " usd_balance = usd_balance - #{accountDTO.usdAmount}, " +
            " usd_freeze_amount = usd_freeze_amount + #{accountDTO.usdAmount} " +
            " where account_id = #{accountDTO.accountId} ")
    int freezeUsd(@Param("accountDTO") AccountDTO accountDTO);

    @Update(" update t_account " +
            " set " +
            " usd_freeze_amount = usd_freeze_amount - #{accountDTO.usdAmount}, " +
            " rmb_balance = rmb_balance + #{accountDTO.rmbAmount} " +
            " where account_id = #{accountDTO.accountId} ")
    int confirmUsdToRmb(@Param("accountDTO") AccountDTO accountDTO);

    @Update(" update t_account " +
            " set " +
            " usd_balance = usd_balance + #{accountDTO.usdAmount}, " +
            " usd_freeze_amount = usd_freeze_amount - #{accountDTO.usdAmount} " +
            " where account_id = #{accountDTO.accountId} ")
    int cancelUsdToRmb(@Param("accountDTO") AccountDTO accountDTO);

}
