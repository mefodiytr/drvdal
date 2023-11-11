package uk.co.controlnetworksolutions.elitedali2.dali.devices;

import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;

public final class BRelay extends BBallast {
   public static final Type TYPE;

   public final Type getType() {
      return TYPE;
   }

   public final BDaliDeviceType getDaliDeviceType() {
      return BDaliDeviceType.relay;
   }

   static {
      TYPE = Sys.loadType(BRelay.class);
   }
}
