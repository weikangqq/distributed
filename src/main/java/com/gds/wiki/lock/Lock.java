package com.gds.wiki.lock;

import lombok.Data;

/**
 * Created by wiki on 2020/7/17 20:06
 */
@Data
public class Lock {
    private String key;

    private Long time;

    private Boolean ifLock;
}
