package com.fjs.cronus.mappers;

import com.fjs.cronus.model.DocumentCategory;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface DocumentCategoryMapper extends MyMapper<DocumentCategory> {


    List<DocumentCategory> getNextCategory(Map<String,Object> paramsMap);

    DocumentCategory   findByFeild(Map<String,Object> paraMap);
}