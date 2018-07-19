package com.fjs.cronus.mappers;

import com.fjs.cronus.dto.MediaCustomerCountDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface MediaCustomerCountMapper  {


    List<MediaCustomerCountDTO> utmSourceList(HashMap<String, Object> map);

    Integer utmSourceListCount(HashMap<String, Object> map);
}
