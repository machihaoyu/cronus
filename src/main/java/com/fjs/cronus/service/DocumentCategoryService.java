package com.fjs.cronus.service;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.DocumentCategoryDTO;
import com.fjs.cronus.dto.cronus.ThreeCategoryDto;
import com.fjs.cronus.dto.cronus.ThreelinkageDTO;
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
        List<DocumentCategoryDTO> resultList = new ArrayList<>();
        if (documentCategoryList != null && documentCategoryList.size() > 0){
            for (DocumentCategory documentCategory : documentCategoryList) {
                DocumentCategoryDTO documentCategoryDTO = new DocumentCategoryDTO();
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
    public CronusDto<ThreeCategoryDto> getThreeCategory(){
        CronusDto resultDto = new CronusDto();
        //根据父类id查找信息
        //拼装参数
        List<ThreeCategoryDto> threeCategoryDtos = new ArrayList<>();
        Map<String,Object> paramsMap = new HashMap<>();
        //获取paraentId 为0的
        paramsMap.put("documentCParentId",0);
        List<DocumentCategory> documentCategoryList = documentCategoryMapper.getNextCategory(paramsMap);
        paramsMap.clear();
        if (documentCategoryList != null && documentCategoryList.size() > 0){
            for (DocumentCategory documentCategory : documentCategoryList) {
                ThreeCategoryDto threeCategoryDto = new ThreeCategoryDto();
                DocumentCategoryDTO documentCategoryDTO = new DocumentCategoryDTO();
                documentCategoryDTO.setId(documentCategory.getId());
                documentCategoryDTO.setDocumentCNameHeader(documentCategory.getDocumentCNameHeader());
                documentCategoryDTO.setDocumentCName(documentCategory.getDocumentCNameHeader()+" "+documentCategory.getDocumentCName());
                documentCategoryDTO.setDocumentCParentId(documentCategory.getDocumentCParentId());
                threeCategoryDto.setName(documentCategory.getId().toString());
                threeCategoryDto.setValue(documentCategoryDTO);
                threeCategoryDto.setParent(documentCategory.getDocumentCParentId().toString());
                threeCategoryDtos.add(threeCategoryDto);
                //找到所有的第二级
                paramsMap.put("documentCParentId",documentCategory.getId());
                List<DocumentCategory> documentCategoryTwoList = documentCategoryMapper.getNextCategory(paramsMap);
                paramsMap.clear();
                if (documentCategoryTwoList != null && documentCategoryTwoList.size() > 0){
                    for (DocumentCategory documentCategory2 : documentCategoryTwoList) {
                        ThreeCategoryDto threeCategoryDto2 = new ThreeCategoryDto();
                        DocumentCategoryDTO documentCategoryDTO2 = new DocumentCategoryDTO();
                        documentCategoryDTO2.setId(documentCategory2.getId());
                        documentCategoryDTO2.setDocumentCNameHeader(documentCategory2.getDocumentCNameHeader());
                        documentCategoryDTO2.setDocumentCName(documentCategory2.getDocumentCNameHeader()+" "+documentCategory.getDocumentCName());
                        documentCategoryDTO2.setDocumentCParentId(documentCategory2.getDocumentCParentId());
                        threeCategoryDto2.setName(documentCategory2.getId().toString());
                        threeCategoryDto2.setValue(documentCategoryDTO2);
                        threeCategoryDto2.setParent(documentCategory2.getDocumentCParentId().toString());
                        threeCategoryDtos.add(threeCategoryDto2);
                        //查询第三级
                        paramsMap.put("documentCParentId",documentCategory2.getId());
                        List<DocumentCategory> documentCategoryThreeList = documentCategoryMapper.getNextCategory(paramsMap);
                        paramsMap.clear();
                        if (documentCategoryThreeList != null && documentCategoryThreeList.size() > 0){
                            for (DocumentCategory documentCategory3 : documentCategoryThreeList) {
                                ThreeCategoryDto threeCategoryDto3 = new ThreeCategoryDto();
                                DocumentCategoryDTO documentCategoryDTO3 = new DocumentCategoryDTO();
                                documentCategoryDTO3.setId(documentCategory3.getId());
                                documentCategoryDTO3.setDocumentCNameHeader(documentCategory3.getDocumentCNameHeader());
                                documentCategoryDTO3.setDocumentCName(documentCategory3.getDocumentCNameHeader()+" "+documentCategory.getDocumentCName());
                                documentCategoryDTO3.setDocumentCParentId(documentCategory3.getDocumentCParentId());
                                threeCategoryDto3.setName(documentCategory3.getId().toString());
                                threeCategoryDto3.setValue(documentCategoryDTO3);
                                threeCategoryDto3.setParent(documentCategory3.getDocumentCParentId().toString());
                                threeCategoryDtos.add(threeCategoryDto3);
                            }

                        }
                    }


                }

            }
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(threeCategoryDtos);
            return  resultDto;
        }
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData("");
        return  resultDto;

    }

}
