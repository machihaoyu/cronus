package com.fjs.cronus.dto.uc;

/**
 * Created by Administrator on 2017/7/20 0020.
 */
public class PhpQueryResultDTO extends BaseUcDTO {

    private static final long serialVersionUID = 7389798228216073239L;

    private String total;

    public PhpQueryResultDTO(){}

    public PhpQueryResultDTO(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public static PhpQueryResultDTO getExcetion(int errNum, String errMsg) {
        PhpQueryResultDTO dto = new PhpQueryResultDTO();
        dto.setErrNum(errNum);
        dto.setErrMsg(errMsg);
        return dto;
    }
}
