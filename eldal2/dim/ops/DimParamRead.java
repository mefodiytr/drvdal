package uk.co.controlnetworksolutions.elitedali2.dim.ops;

import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimNetwork;

public class DimParamRead extends DimOperation {
   private int paramIdentifier;
   private byte[] paramData;

   public boolean execute() {
      this.paramData = null;
      this.bodyData = new byte[2];
      this.bodyData[0] = (byte)(this.paramIdentifier & 255);
      this.bodyData[1] = 82;
      this.sendMessage();
      if (this.dimResponseFlag) {
         if (this.replyData == null) {
            this.setFaultText("System fault (param reply null)");
            return this.dimOperationSuccess;
         }

         if (this.replyData.length < 2) {
            this.setFaultText("System fault (param reply invalid)");
            return this.dimOperationSuccess;
         }

         if ((255 & this.replyData[0]) != this.paramIdentifier) {
            this.setFaultText("System fault (param reply mismatch)");
            return this.dimOperationSuccess;
         }

         if ((255 & this.replyData[1]) != 166) {
            this.setFaultText("DIM parameter failure");
            return this.dimOperationSuccess;
         }

         this.paramData = new byte[this.replyData.length - 2];
         System.arraycopy(this.replyData, 2, this.paramData, 0, this.paramData.length);
         this.dimOperationSuccess = true;
      }

      return this.dimOperationSuccess;
   }

   public byte[] getParamData() {
      if (!this.executedFlag) {
         this.execute();
      }

      return this.paramData;
   }

   public DimParamRead(BDimNetwork var1, int var2, int var3) {
      super(var1, (BDimDevice)null, var2, 84);
      this.paramIdentifier = 0;
      this.paramData = null;
      
      this.paramIdentifier = var3;
   }

   public DimParamRead(BDimDevice var1, int var2) {
      super(var1.getDimNetwork(), var1, var1.getDimAddress(), 84);
      this.paramIdentifier = 0;
      this.paramData = null;
      
      this.paramIdentifier = var2;
   }
}
