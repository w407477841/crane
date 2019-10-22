package com.xywg.attendance.modular.led.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2019/5/16
 */
@Data
public class EntriesExitsQuantityVo {
    private Integer incount;

    private Integer outcount;

    public EntriesExitsQuantityVo(Integer incount, Integer outcount) {
        this.incount = incount;
        this.outcount = outcount;
    }

    public EntriesExitsQuantityVo() {
    }
}
