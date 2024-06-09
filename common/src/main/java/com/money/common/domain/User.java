package com.money.common.domain;

import java.io.Serializable;

/**
 * @Author: money
 * @Description: TODO
 * @Date: 2024/6/3 14:18
 * @Version: 1.0
 */

public class User implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
