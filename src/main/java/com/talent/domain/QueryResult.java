package com.talent.domain;

import lombok.Data;

import java.util.ArrayList;

@Data
public class QueryResult<T> {

    private T value;

    private long total;

}
