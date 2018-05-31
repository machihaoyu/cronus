package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class BasePageableVO<T> {

    @ApiModelProperty(value = "每页item数", dataType = "Integer")
    private Integer pageSize = 20;

    @ApiModelProperty(value = "页码", dataType = "Integer")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "总数据数", dataType = "Integer")
    private Integer count = 0;

    @ApiModelProperty(value = "总页数", dataType = "Integer")
    private Integer pageCount = 0;

    @ApiModelProperty(value = "列表数据", dataType = "java.util.List")
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null
                || pageSize < 1) {
            return;
        }
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum == null
                || pageNum < 1) {
            return;
        }
        this.pageNum = pageNum;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        if (count == null
                || count < 1) {
            return;
        }
        this.count = count;
    }

    public Integer getPageCount() {
        return calculate(count, pageSize);
    }

    /**
     * 计算总页数.
     *
     * @param count 数据总数量
     * @param size  每页展示数据数
     */
    public static int calculate(int count, int size) {
        if (count == 0 || size == 0) {
            return 0;
        }
        if (count > size) {
            return count % size == 0 ? count / size : count / size + 1;
        } else {
            return 1;
        }
    }
}
