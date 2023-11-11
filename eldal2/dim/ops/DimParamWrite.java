package uk.co.controlnetworksolutions.elitedali2.dim.ops;

import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimNetwork;

public class DimParamWrite extends DimOperation {
   private int paramIdentifier;
   private byte[] paramData;

   public boolean execute() {
      int var1;
      if (this.paramData == null) {
         var1 = 0;
      } else {
         var1 = this.paramData.length;
      }

      this.bodyData = new byte[2 + var1];
      this.bodyData[0] = (byte)(this.paramIdentifier & 255);
      this.bodyData[1] = 84;
      if (var1 > 0) {
         System.arraycopy(this.paramData, 0, this.bodyData, 2, this.paramData.length);
      }

      this.sendMessage();
      if (this.dimResponseFlag) {
         if (this.replyData == null) {
            this.setFaultText("System fault (null reply)");
            return this.dimOperationSuccess;
         }

         if (this.replyData.length < 2) {
            this.setFaultText("System fault (reply invalid)");
            return this.dimOperationSuccess;
         }

         if ((255 & this.replyData[0]) != this.paramIdentifier) {
            this.setFaultText("System fault (reply mismatch)");
            return this.dimOperationSuccess;
         }

         if ((255 & this.replyData[1]) != 166) {
            this.setFaultText("DIM parameter failure");
            return this.dimOperationSuccess;
         }

         this.dimOperationSuccess = true;
      }

      return this.dimOperationSuccess;
   }

   public DimParamWrite(BDimNetwork var1, int var2, int var3, byte[] var4) {
      super(var1, (BDimDevice)null, var2, 84);
      this.paramIdentifier = 0;
      this.paramData = null;
      
      this.paramIdentifier = var3;
      this.paramData = var4;
   }

   public DimParamWrite(BDimDevice var1, int var2, byte[] var3) {
      super(var1.getDimNetwork(), var1, var1.getDimAddress(), 84);
      this.paramIdentifier = 0;
      this.paramData = null;
      
      this.paramIdentifier = var2;
      this.paramData = var3;
   }
}
