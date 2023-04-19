package com.wanclaybarreto.webfood.infrastructure.web.converter;

import com.wanclaybarreto.webfood.util.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

    @Override
    public BigDecimal convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        source = source.replace(",", ".").trim();

        return new BigDecimal(source).setScale(2, RoundingMode.HALF_UP);
    }

}
