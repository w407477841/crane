package com.xywg.iot.common.utils.arithmetic;

import com.xywg.iot.netty.models.Coordinate;
import com.xywg.iot.netty.models.Round;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description 极大似然估计算法
 * Date: Created in 9:35 2019/3/6
 * Modified By : wangyifei
 */
public class MaximumArithmetic extends BaseArithmetic {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaximumArithmetic.class);

    public MaximumArithmetic(Boolean useHypotenuse) {
        super(useHypotenuse);
    }

    @Override
    public Coordinate exe(List<Round> rounds) {
        return null;
    }



}
