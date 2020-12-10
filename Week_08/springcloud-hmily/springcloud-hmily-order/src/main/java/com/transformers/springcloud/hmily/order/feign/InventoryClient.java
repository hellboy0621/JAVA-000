package com.transformers.springcloud.hmily.order.feign;

import com.transformers.springcloud.hmily.inventory.dto.InventoryDTO;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "inventory-service")
public interface InventoryClient {

    /**
     * 库存扣减.
     *
     * @param inventoryDTO 实体对象
     * @return true 成功
     */
    @RequestMapping("/inventory-service/inventory/decrease")
    // 该注解为hmily分布式事务接口标识，表示该接口参与hmily分布式事务
    @Hmily
    Boolean decrease(@RequestBody InventoryDTO inventoryDTO);

    /**
     * 模拟库存扣减异常.
     *
     * @param inventoryDTO 实体对象
     * @return true 成功
     */
    @Hmily
    @RequestMapping("/inventory-service/inventory/mockWithTryException")
    Boolean mockWithTryException(InventoryDTO inventoryDTO);
}
