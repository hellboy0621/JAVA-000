package com.transformer.dao;

import com.transformer.entity.OrderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {

    OrderDO selectById(@Param("id") Long id);

    int insert(@Param("orderDO") OrderDO orderDO);

    int delete(@Param("id") Long id);

    int update(@Param("orderDO") OrderDO orderDO);

}
