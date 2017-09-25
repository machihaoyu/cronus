package com.fjs.cronus.mappers;

import com.fjs.cronus.model.Document;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;


public interface DocumentMapper extends MyMapper<Document> {

    Document findByFeild(Map<String,Object> paramsMap);

    void addDocument(Document document);

    //List<Document>  ocrDocument(Map<String,Object> paramsMap);
}