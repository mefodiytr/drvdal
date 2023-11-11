package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import javax.baja.driver.point.BPointFolder;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BDaliPointsFolder extends BPointFolder {
   public static final Type TYPE;

   public final Type getType() {
      return TYPE;
   }

   static {
      TYPE = Sys.loadType(BDaliPointsFolder.class);
   }
}
