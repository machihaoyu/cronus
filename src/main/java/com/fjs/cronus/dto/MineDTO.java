package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by crm on 2017/4/29.
 */
public class MineDTO extends BaseToStringDTO implements Serializable {

   private static final long serialVersionUID = 193973636240664177L;

   private MineCustomerDTO customer;

   private MineAccountDTO account;

   private MineCommissionDTO commission;

   public MineCustomerDTO getCustomer() {
      return customer;
   }

   public void setCustomer(MineCustomerDTO customer) {
      this.customer = customer;
   }

   public MineAccountDTO getAccount() {
      return account;
   }

   public void setAccount(MineAccountDTO account) {
      this.account = account;
   }

   public MineCommissionDTO getCommission() {
      return commission;
   }

   public void setCommission(MineCommissionDTO commission) {
      this.commission = commission;
   }

}
