package uk.co.controlnetworksolutions.elitedali2.dali.ops;

import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;

public class DaliQuery extends DaliOperation {
   private int retryMax;
   private int multipleMax;
   private int queryDelay;
   private boolean confirmationSuccessFlag;

   public boolean execute() {
      if (this.daliDeviceType >= 0 && this.daliDataByte >= 224 && this.daliDataByte <= 255) {
         DaliSpecialCommand var2 = new DaliSpecialCommand(this.daliNetwork, 193, this.daliDeviceType);
         var2.execute();
      }

      boolean var1 = super.execute();

      try {
         Thread.sleep((long)this.queryDelay);
      } catch (Exception var4) {
      }

      return var1;
   }

   public boolean confirmResult() {
      int var4 = -1;
      int var5 = -2;
      int var6 = -3;
      int var7 = -4;
      int var8 = 0;
      this.confirmationSuccessFlag = false;

      for(int var1 = 0; var1 < this.retryMax + 1; ++var1) {
         this.execute();
         int var2 = this.getDaliStatus();
         int var3 = this.getResult();
         if (var2 == var4 && var2 == var5 && var3 == var6 && var3 == var7) {
            this.confirmationSuccessFlag = true;
            break;
         }

         var4 = var2;
         var5 = var2;
         var6 = var3;
         var7 = var3;
         if (var2 == 3) {
            ++var8;
            if (var8 > this.multipleMax) {
               return this.confirmationSuccessFlag;
            }
         }
      }

      return this.confirmationSuccessFlag;
   }

   public boolean verifyResult(int var1) {
      if (this.confirmResult()) {
         return this.getDaliStatus() == 53 && this.getResult() == var1;
      } else {
         return false;
      }
   }

   public void setRetryMax(int var1) {
      this.retryMax = var1;
   }

   public void setMultipleMax(int var1) {
      this.multipleMax = var1;
   }

   public void setQueryDelay(int var1) {
      this.queryDelay = var1;
   }

   public boolean getConfirmationSuccess() {
      return this.confirmationSuccessFlag;
   }

   public DaliQuery(BDaliNetwork var1, int var2, int var3) {
      super(var1, false, true, var2, var3, 8);
      this.retryMax = 5;
      this.multipleMax = 2;
      this.queryDelay = 0;
      this.confirmationSuccessFlag = false;
   }

   public DaliQuery(BGenericDaliDevice var1, int var2) {
      super(var1, true, var2, 8);
      this.retryMax = 5;
      this.multipleMax = 2;
      this.queryDelay = 0;
      this.confirmationSuccessFlag = false;
   }

   public DaliQuery(BDaliNetwork var1, int var2, int var3, boolean var4) {
      super(var1, var2, var3, 8);
      this.retryMax = 5;
      this.multipleMax = 2;
      this.queryDelay = 0;
      this.confirmationSuccessFlag = false;
   }
}
