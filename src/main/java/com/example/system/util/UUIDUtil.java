package com.example.system.util;


import cn.hutool.core.lang.UUID;

public class UUIDUtil {
    public static long uuid() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
