package com.fjs.cronus.mappers;

import com.fjs.cronus.model.Document;
import com.fjs.cronus.model.DocumentCategory;
import com.fjs.cronus.util.MyMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface DocumentCategoryMapper {


    List<DocumentCategory> getNextCategory(Map<String,Object> paramsMap);

    DocumentCategory   findByFeild(Map<String,Object> paraMap);

    DocumentCategory selectByKey(@Param(value = "id") Integer id);

    List<DocumentCategory> findListByFeild(Map<String,Object> paraMap);
}