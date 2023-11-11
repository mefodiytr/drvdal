package uk.co.controlnetworksolutions.elitedali2.dim.ops;

import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimNetwork;

public class DimAction extends DimOperation {
   private int actionIdentifier;
   private byte[] outgoingData;
   private byte[] returnData;

   public boolean execute() {
      this.returnData = null;
      int var1;
      if (this.outgoingData == null) {
         var1 = 0;
      } else {
         var1 = this.outgoingData.length;
      }

      this.bodyData = new byte[1 + var1];
      this.bodyData[0] = (byte)(this.actionIdentifier & 255);
      if (var1 > 0) {
         System.arraycopy(this.outgoingData, 0, this.bodyData, 1, this.outgoingData.length);
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

         if ((255 & this.replyData[0]) != this.actionIdentifier) {
            this.setFaultText("System fault (reply mismatch)");
            return this.dimOperationSuccess;
         }

         if ((255 & this.replyData[1]) != 166) {
            this.setFaultText("DIM action failure");
            return this.dimOperationSuccess;
         }

         this.returnData = new byte[this.replyData.length - 2];
         System.arraycopy(this.replyData, 2, this.returnData, 0, this.returnData.length);
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

   public DimAction(BDimNetwork var1, int var2, int var3) {
      super(var1, (BDimDevice)null, var2, 82);
      this.actionIdentifier = 0;
      this.outgoingData = null;
      this.returnData = null;
      
      this.actionIdentifier = var3;
      this.outgoingData = null;
   }

   public DimAction(BDimNetwork var1, int var2, int var3, byte[] var4) {
      super(var1, (BDimDevice)null, var2, 82);
      this.actionIdentifier = 0;
      this.outgoingData = null;
      this.returnData = null;
      
      this.actionIdentifier = var3;
      this.outgoingData = var4;
   }

   public DimAction(BDimDevice var1, int var2) {
      super(var1.getDimNetwork(), var1, var1.getDimAddress(), 82);
      this.actionIdentifier = 0;
      this.outgoingData = null;
      this.returnData = null;
      
      this.actionIdentifier = var2;
      this.outgoingData = null;
   }

   public DimAction(BDimDevice var1, int var2, byte[] var3) {
      super(var1.getDimNetwork(), var1, var1.getDimAddress(), 82);
      this.actionIdentifier = 0;
      this.outgoingData = null;
      this.returnData = null;
      
      this.actionIdentifier = var2;
      this.outgoingData = var3;
   }
}
