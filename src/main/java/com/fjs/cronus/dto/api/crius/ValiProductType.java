package com.fjs.cronus.dto.api.crius;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by pc on 2017/11/7.
 */
public class ValiProductType implements ConstraintValidator<FlagValidator,Object> {
    private int[] productType;

    @Override
    public void initialize(FlagValidator flagValidator) {
        this.productType = flagValidator.productType();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        //判断产品类型的值
        boolean isFlag = false;
        for(int i = 0;i<productType.length;i++){
            if(productType[i]==1||productType[i]==2||productType[i]==4){
                isFlag = true;
            }
        }

        return isFlag;
    }
}
