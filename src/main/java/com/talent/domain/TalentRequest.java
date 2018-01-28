package com.talent.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class TalentRequest implements Serializable {

    private static final long serialVersionUID = -7701233435223421779L;

    private Integer page = 1;

    private Integer limit = 10;

    private String q;

    private String id;

}
