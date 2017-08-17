package com.fjs.cronus.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
@RestController
@RequestMapping("/ocr/api")
public class OcrController {

    /**
     * cronus对外提供API给PHP使用
     */
    @RequestMapping("/v1/crmin/")
    public void ocrInvoke(){

    }

    /**
     * Java调用Php接口
     */
    @RequestMapping("/v1/crmout/")
    public void ocrOutCRM(){



    }

}
