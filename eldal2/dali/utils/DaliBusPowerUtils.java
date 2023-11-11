package uk.co.controlnetworksolutions.elitedali2.dali.utils;

import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.ops.DimParamRead;

public class DaliBusPowerUtils {
   public static boolean getPowerState(BDaliNetwork var0) {
      boolean var1 = false;
      DimParamRead var2 = new DimParamRead((BDimDevice)var0.getParent(), 80);
      byte[] var3 = var2.getParamData();
      if (var3 != null && var3[0] != 0) {
         var1 = true;
      }

      return var1;
   }
}
