package uk.co.controlnetworksolutions.elitedali2.dali.ops;

import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.DaliArcPowerUtils;

public class DaliLevel extends DaliOperation {
   public DaliLevel(BDaliNetwork var1, boolean var2, int var3, double var4) {
      super(var1, var2, false, var3, DaliArcPowerUtils.percentToDirectLevel(var4), 0);
   }

   public DaliLevel(BGenericDaliDevice var1, double var2) {
      super(var1, false, DaliArcPowerUtils.percentToDirectLevel(var2), 0);
   }
}
