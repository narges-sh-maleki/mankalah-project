package com.sh.maleki.mankalah.web.mapper;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PitsMapper {
    public String[] asStringArray(Integer[] pits){
        String[] result = new String[pits.length];
        result = IntStream.range(0,pits.length)
                .mapToObj(idx-> idx + ":" + pits[idx])
                .collect(Collectors.toList()).toArray(result);
        return result;

    }
}
