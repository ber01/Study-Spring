package com.kyunghwan.Valid;

import javax.validation.constraints.NotEmpty;

public class EventA {

    private Long idx;

    @NotEmpty
    private String title;

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
