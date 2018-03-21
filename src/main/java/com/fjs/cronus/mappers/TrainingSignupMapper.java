package com.fjs.cronus.mappers;


import com.fjs.cronus.model.TrainingSignup;

public interface TrainingSignupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrainingSignup record);

    int insertSelective(TrainingSignup record);

    TrainingSignup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrainingSignup record);

    int updateByPrimaryKey(TrainingSignup record);
}