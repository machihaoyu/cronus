package com.fjs.cronus.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
@RestController
@RequestMapping("/ocr/api/v1/")
public class OcrController {


    @RequestMapping("/crmin/")
    public void ocrInvoke(){

    }


    @RequestMapping("/crmout/")
    public void ocrOutCRM(){



    }

}
