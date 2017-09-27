package com.fjs.cronus.mappers;

import com.fjs.cronus.model.Document;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.util.MyMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface DocumentMapper {

    Document findByFeild(Map<String,Object> paramsMap);

    void addDocument(Document document);

    //List<Document>  ocrDocument(Map<String,Object> paramsMap);
    Document selectByKey(@Param(value = "id")  Integer id);
}