package com.talent.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Talent implements Serializable{

    private static final long serialVersionUID = -1927980564776907933L;

    private String id;

    private String doc;

    private Date createTime = new Date();
}
