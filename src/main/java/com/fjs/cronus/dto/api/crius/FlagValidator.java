package com.fjs.cronus.dto.api.crius;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by pc on 2017/11/7.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Constraint(validatedBy = ValiProductType.class)
public @interface FlagValidator {
    int[] productType();
    String message() default "{产品类型不存在}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
