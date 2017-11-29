package com.fjs.cronus.api.thea;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yinzf on 2017/10/24.
 */
public class ConfigDTO {
    private Integer id;
    @ApiModelProperty(value="配置关键字(必填)")
    private String name;
    @ApiModelProperty(value="配置名字（必填）")
    private String title;
    @ApiModelProperty(value="配置详解（必填）")
    private String description;
    @ApiModelProperty(value="值")
    private String value;
    @ApiModelProperty(value="配置类型（必填）")
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ConfigDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
