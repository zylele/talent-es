package org.spring.springboot.domain;

import java.io.Serializable;

public class TalentRequest implements Serializable {


    private static final long serialVersionUID = -321088104921945934L;

    private Integer page;

    private Integer limit;

    private String q;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
