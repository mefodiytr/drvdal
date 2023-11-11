package uk.co.controlnetworksolutions.elitedali2.dali.ops;

import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;

public class DaliWrite {
   private BDaliNetwork daliNetwork;
   private boolean daliGroupAddressFlag;
   private int daliAddress;
   private int storeCommandCode;
   private int storeValue;
   private int daliDeviceType;
   private boolean executedFlag;
   private int daliStatus;
   private int sendRepeatCount;
   private int storeRepeatCount;

   public void setDaliDeviceType(int var1) {
      this.daliDeviceType = var1;
   }

   public boolean execute() {
      this.executedFlag = true;
      if (this.daliNetwork == null) {
         return false;
      } else {
         int var1;
         for(var1 = 0; var1 < this.sendRepeatCount; ++var1) {
            DaliSpecialCommand var2 = new DaliSpecialCommand(this.daliNetwork, 163, this.storeValue);
            var2.execute();
            this.daliStatus = var2.getDaliStatus();
            if (this.daliStatus == 4 || this.daliStatus == 34) {
               return false;
            }
         }

         for(var1 = 0; var1 < this.storeRepeatCount; ++var1) {
            if (this.daliDeviceType >= 0 && this.storeCommandCode >= 224 && this.storeCommandCode <= 255) {
               DaliSpecialCommand var4 = new DaliSpecialCommand(this.daliNetwork, 193, this.daliDeviceType);
               var4.execute();
            }

            DaliCommand var3 = new DaliCommand(this.daliNetwork, this.daliGroupAddressFlag, this.daliAddress, this.storeCommandCode);
            if (this.storeCommandCode >= 32 && this.storeCommandCode <= 129) {
               var3.setTransmitRepeatCount(1);
            }

            var3.execute();
            this.daliStatus = var3.getDaliStatus();
            if (this.daliStatus == 4 || this.daliStatus == 34) {
               return false;
            }
         }

         if (this.daliStatus == 53) {
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean success() {
      if (!this.executedFlag) {
         this.execute();
      }

      boolean var10000 = false;
      if (this.daliStatus == 53) {
         var10000 = true;
      }

      return var10000;
   }

   public int getDaliStatus() {
      if (!this.executedFlag) {
         this.execute();
      }

      return this.daliStatus;
   }

   public void setSendRepeat(int var1) {
      this.sendRepeatCount = var1;
   }

   public void setStoreRepeat(int var1) {
      this.storeRepeatCount = var1;
   }

   public DaliWrite(BDaliNetwork var1, int var2, int var3, int var4) {
     this.daliNetwork = null;
     this.daliGroupAddressFlag = false;
     this.daliAddress = -1;
     this.storeCommandCode = -1;
     this.storeValue = -1;
     this.daliDeviceType = -1;
     this.executedFlag = false;
     this.daliStatus = 15;
     this.sendRepeatCount = 2;
     this.storeRepeatCount = 2;
     
      this.daliNetwork = var1;
      this.daliGroupAddressFlag = false;
      this.daliAddress = var2;
      this.storeCommandCode = var3;
      this.storeValue = var4;
   }

   public DaliWrite(BDaliNetwork var1, boolean var2, int var3, int var4, int var5) {
     this.daliNetwork = null;
     this.daliGroupAddressFlag = false;
     this.daliAddress = -1;
     this.storeCommandCode = -1;
     this.storeValue = -1;
     this.daliDeviceType = -1;
     this.executedFlag = false;
     this.daliStatus = 15;
     this.sendRepeatCount = 2;
     this.storeRepeatCount = 2;
     
      this.daliNetwork = var1;
      this.daliGroupAddressFlag = var2;
      this.daliAddress = var3;
      this.storeCommandCode = var4;
      this.storeValue = var5;
   }
}
