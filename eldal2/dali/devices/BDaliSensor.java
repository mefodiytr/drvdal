package uk.co.controlnetworksolutions.elitedali2.dali.devices;

import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.config.DaliDeviceConfig;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;

public abstract class BDaliSensor extends BGenericDaliDevice {
   public static final Type TYPE;
   public static int MIN_POLL_PERIOD;

   public Type getType() {
      return TYPE;
   }

   public abstract boolean configure(DaliDeviceConfig var1);

   public abstract BDaliDeviceType getDaliDeviceType();

   public boolean isSensor() {
      return true;
   }

   static {
      TYPE = Sys.loadType(BDaliSensor.class);
      MIN_POLL_PERIOD = 400;
   }
}
