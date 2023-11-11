package uk.co.controlnetworksolutions.elitedali2.dim.network;

import javax.baja.driver.BDeviceFolder;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BDimDeviceFolder extends BDeviceFolder {
   public static final Type TYPE;

   public Type getType() {
      return TYPE;
   }

   public final BDimNetwork getEliteDaliNetwork() {
      return (BDimNetwork)this.getNetwork();
   }

   static {
      TYPE = Sys.loadType(BDimDeviceFolder.class);
   }
}
