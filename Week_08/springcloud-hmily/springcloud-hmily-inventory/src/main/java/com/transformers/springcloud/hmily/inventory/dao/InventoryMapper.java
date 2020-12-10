package com.transformers.springcloud.hmily.inventory.dao;

import com.transformers.springcloud.hmily.inventory.dto.InventoryDTO;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMapper {

    /**
     * Try 阶段，减库存，增加冻结库存
     * @param inventoryDTO
     * @return
     */
    @Update(" update inventory " +
            " set total_inventory = total_inventory - #{count}," +
            " lock_inventory= lock_inventory + #{count} " +
            " where product_id = #{productId} and total_inventory > 0  ")
    int decrease(InventoryDTO inventoryDTO);

    /**
     * Confirm 阶段，分布式事务成功提交，把冻结库存清空
     * @param inventoryDTO
     * @return
     */
    @Update(" update inventory " +
            " set lock_inventory = lock_inventory - #{count} " +
            " where product_id =#{productId} and lock_inventory > 0 ")
    int confirm(InventoryDTO inventoryDTO);

    /**
     * Cancel 阶段，分布式事务失败，把 Try 阶段减少的库存加回去，把 Try 阶段增加的冻结库存减回去
     * @param inventoryDTO
     * @return
     */
    @Update(" update inventory " +
            " set total_inventory = total_inventory + #{count}," +
            " lock_inventory= lock_inventory - #{count} " +
            " where product_id = #{productId} and total_inventory > 0  ")
    int cancel(InventoryDTO inventoryDTO);

}
