package com.fjs.cronus.service;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.DocumentCategoryDTO;
import com.fjs.cronus.dto.cronus.DocumentCategoryDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.DocumentCategoryMapper;
import com.fjs.cronus.model.DocumentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2017/9/20.
 */
@Service
public class DocumentCategoryService {

    @Autowired
    DocumentCategoryMapper documentCategoryMapper;


    public CronusDto getNextCategory(Integer cateGoryParentId){
        CronusDto resultDto = new CronusDto();
        if (cateGoryParentId == null){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW, "非法参数!");
        }
        //根据父类id查找信息
        //拼装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("documentCParentId",cateGoryParentId);
        List<DocumentCategory> documentCategoryList = documentCategoryMapper.getNextCategory(paramsMap);
        List<DocumentCategoryDto> resultList = new ArrayList<>();
        if (documentCategoryList != null && documentCategoryList.size() > 0){
            for (DocumentCategory documentCategory : documentCategoryList) {
                DocumentCategoryDto documentCategoryDTO = new DocumentCategoryDto();
                documentCategoryDTO.setId(documentCategory.getId());
                documentCategoryDTO.setDocumentCNameHeader(documentCategory.getDocumentCNameHeader());
                documentCategoryDTO.setDocumentCName(documentCategory.getDocumentCNameHeader()+" "+documentCategory.getDocumentCName());
                documentCategoryDTO.setDocumentCParentId(documentCategory.getDocumentCParentId());
                resultList.add(documentCategoryDTO);
            }
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(resultList);
            return  resultDto;
        }
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData("");
        return  resultDto;

    }

}
