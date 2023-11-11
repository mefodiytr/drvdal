package uk.co.controlnetworksolutions.elitedali2.dim.ops;

import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimNetwork;

public class DimBoot extends DimOperation {
   private int bootIdentifier;
   private byte[] outgoingData;
   private byte[] returnData;

   public void setInterByteDelay(int var1) {
      this.interByteDelay = var1;
   }

   public boolean execute() {
      this.returnData = null;
      int var1;
      if (this.outgoingData == null) {
         var1 = 0;
      } else {
         var1 = this.outgoingData.length;
      }

      this.bodyData = new byte[5 + var1];
      this.bodyData[0] = 67;
      this.bodyData[1] = 78;
      this.bodyData[2] = 83;
      this.bodyData[3] = 49;
      this.bodyData[4] = (byte)(this.bootIdentifier & 255);
      if (var1 > 0) {
         System.arraycopy(this.outgoingData, 0, this.bodyData, 5, this.outgoingData.length);
      }

      this.dimCrcEnable = false;
      this.sendMessage();
      if (this.dimResponseFlag) {
         if (this.replyStatusCode != 172) {
            if (this.replyStatusCode == 166) {
               this.setFaultText("System fault (incorrect mode)");
            } else if (this.replyStatusCode == 165) {
               this.setFaultText("System fault (unsupported message)");
            } else {
               this.setFaultText("System fault (invalid message)");
            }

            return this.dimOperationSuccess;
         }

         if (this.replyData == null) {
            this.setFaultText("System fault (null reply)");
            return this.dimOperationSuccess;
         }

         if (this.replyData.length < 2) {
            this.setFaultText("System fault (reply invalid)");
            return this.dimOperationSuccess;
         }

         if ((255 & this.replyData[0]) != 67 || (255 & this.replyData[1]) != 78 || (255 & this.replyData[2]) != 83 || (255 & this.replyData[3]) != 49) {
            this.setFaultText("System fault (invalid boot signature)");
            return this.dimOperationSuccess;
         }

         if ((255 & this.replyData[4]) != this.bootIdentifier) {
            this.setFaultText("System fault (boot reply mismatch)");
            return this.dimOperationSuccess;
         }

         this.returnData = new byte[this.replyData.length - 5];
         System.arraycopy(this.replyData, 5, this.returnData, 0, this.returnData.length);
         this.dimOperationSuccess = true;
      }

      return this.dimOperationSuccess;
   }

   public byte[] getReturnData() {
      if (!this.executedFlag) {
         this.execute();
      }

      return this.returnData;
   }

   public DimBoot(BDimNetwork var1, int var2, int var3) {
      super(var1, (BDimDevice)null, var2, 161);
      this.bootIdentifier = 0;
      this.outgoingData = null;
      this.returnData = null;
      
      this.bootIdentifier = var3;
      this.outgoingData = null;
   }

   public DimBoot(BDimNetwork var1, int var2, int var3, byte[] var4) {
      super(var1, (BDimDevice)null, var2, 161);
      this.bootIdentifier = 0;
      this.outgoingData = null;
      this.returnData = null;
      
      this.bootIdentifier = var3;
      this.outgoingData = var4;
   }

   public DimBoot(BDimDevice var1, int var2) {
      super(var1.getDimNetwork(), var1, var1.getDimAddress(), 161);
      this.bootIdentifier = 0;
      this.outgoingData = null;
      this.returnData = null;
      
      this.bootIdentifier = var2;
      this.outgoingData = null;
   }

   public DimBoot(BDimDevice var1, int var2, byte[] var3) {
      super(var1.getDimNetwork(), var1, var1.getDimAddress(), 161);
      this.bootIdentifier = 0;
      this.outgoingData = null;
      this.returnData = null;
      
      this.bootIdentifier = var2;
      this.outgoingData = var3;
   }
}
