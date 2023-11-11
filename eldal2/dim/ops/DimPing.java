package uk.co.controlnetworksolutions.elitedali2.dim.ops;

import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimNetwork;

public class DimPing extends DimOperation {
   public boolean execute() {
      this.sendMessage();
      if (this.dimResponseFlag) {
         this.dimOperationSuccess = true;
      }

      return this.dimOperationSuccess;
   }

   public boolean success() {
      if (!this.executedFlag) {
         this.execute();
      }

      return this.dimOperationSuccess;
   }

   public DimPing(BDimNetwork var1, int var2) {
      super(var1, (BDimDevice)null, var2, 81);
      this.bodyData = null;
   }

   public DimPing(BDimDevice var1) {
      super(var1.getDimNetwork(), var1, var1.getDimAddress(), 81);
      this.bodyData = null;
   }
}
