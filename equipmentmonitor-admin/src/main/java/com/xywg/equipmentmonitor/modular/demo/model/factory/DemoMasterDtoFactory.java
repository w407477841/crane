package com.xywg.equipmentmonitor.modular.demo.model.factory;
import com.xywg.equipmentmonitor.modular.demo.model.DemoMaster;
import com.xywg.equipmentmonitor.modular.demo.model.dto.DemoMasterDto;

/**
 * @author wangcw
 *
 */
public class DemoMasterDtoFactory extends BaseFactory<DemoMaster, DemoMasterDto> {

    public DemoMasterDtoFactory(DemoMaster t)
            throws InstantiationException, IllegalAccessException {
        super(t, DemoMasterDto.class);
    }

    @Override
    protected void doOrderThingForVo(DemoMasterDto vo) {

    }
}