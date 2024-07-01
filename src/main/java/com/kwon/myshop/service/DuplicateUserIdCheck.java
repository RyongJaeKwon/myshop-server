package com.kwon.myshop.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DuplicateUserIdCheck {

    private final static Map<Boolean, Function<Boolean, Map<String, String>>> map = new HashMap<>();

    static {
        map.put(true, (Boolean result) -> {
            Map<String, String> map = new HashMap<>();
            map.put("message", "이미 사용중인 아이디 입니다");
            return map;
        });

        map.put(false, (Boolean result) -> {
            Map<String, String> map = new HashMap<>();
            map.put("message", "사용 가능한 아이디 입니다");
            return map;
        });
    }

    public static Map<String, String> getMessage(Boolean result) {
        Function<Boolean, Map<String, String>> msgFunction = map.get(result);
        return msgFunction.apply(result);
    }
}