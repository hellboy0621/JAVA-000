package com.transformers.springcloud.hmily.inventory.controller;

import com.transformers.springcloud.hmily.inventory.dto.InventoryDTO;
import com.transformers.springcloud.hmily.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 库存扣减.
     *
     * @param inventoryDTO 实体对象
     * @return true 成功
     */
    @RequestMapping("/decrease")
    Boolean decrease(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.decrease(inventoryDTO);
    }


    @RequestMapping("/mockWithTryException")
    Boolean mockWithTryException(InventoryDTO inventoryDTO) {
        return inventoryService.mockWithTryException(inventoryDTO);
    }
}
