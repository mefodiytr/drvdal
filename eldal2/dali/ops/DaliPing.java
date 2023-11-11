package uk.co.controlnetworksolutions.elitedali2.dali.ops;

import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;

public class DaliPing extends DaliQuery {
   public DaliPing(BDaliNetwork var1, int var2) {
      super(var1, var2, 145);
   }

   public DaliPing(BGenericDaliDevice var1) {
      super(var1, 145);
   }
}
