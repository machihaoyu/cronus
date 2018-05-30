package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

public class BasePageableDTO {

    @ApiModelProperty(value = "每页展示item的数量", dataType = "Integer")
    private Integer pageSize = 20;

    @ApiModelProperty(value = "页码", dataType = "Integer")
    private Integer pageNum = 1;

    /**
     * pageSize，对应数据库中的页码
     * <br>
     *     例如，第1页，mysql中对应 Limit 0, pageSize 中的0.
     *     例如，第2页，mysql中对应 Limit (pageNum-1)*pageSize, pageSize 中的 (pageNum-1)*pageSize.
     */
    @ApiModelProperty(hidden = true)
    private Integer pageOffset = 0;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null //
                || pageSize < 1 //
                || pageSize > 100
                ) {
            pageSize = 20;
        }
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum == null //
            || pageNum < 1
                ) {
            pageNum = 1;
        }
        this.pageNum = pageNum;
    }

    public Integer getPageOffset() {
        if (pageNum == null
                || pageSize == null
                || pageSize < 0
                || pageNum < 1) {
            return 0;
        }
        return (pageNum - 1) * pageSize;
    }
}
