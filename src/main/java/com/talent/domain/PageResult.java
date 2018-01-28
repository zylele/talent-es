package com.talent.domain;

import lombok.Data;

import java.util.ArrayList;

@Data
public class PageResult {

    private Object rows = new ArrayList<>();

    private Long total = 0L;

}
