package com.transformers.springcloud.hmily.inventory.service.impl;

import com.transformers.springcloud.hmily.inventory.dao.InventoryMapper;
import com.transformers.springcloud.hmily.inventory.dto.InventoryDTO;
import com.transformers.springcloud.hmily.inventory.service.InventoryService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirmDecrease", cancelMethod = "cancelDecrease")
    public Boolean decrease(InventoryDTO inventoryDTO) {
        inventoryMapper.decrease(inventoryDTO);
        return Boolean.TRUE;
    }

    @Override
    @HmilyTCC(confirmMethod = "confirmDecrease", cancelMethod = "cancelDecrease")
    public Boolean mockWithTryException(InventoryDTO inventoryDTO) {
        throw new HmilyRuntimeException("库存扣减异常！");
    }

    public Boolean confirmDecrease(InventoryDTO inventoryDTO) {
        return inventoryMapper.confirm(inventoryDTO) > 0;
    }

    public Boolean cancelDecrease(InventoryDTO inventoryDTO) {
        return inventoryMapper.cancel(inventoryDTO) > 0;
    }

}
