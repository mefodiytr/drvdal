package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import javax.baja.driver.point.BPointDeviceExt;
import javax.baja.naming.BOrd;
import javax.baja.sys.Action;
import javax.baja.sys.BFacets;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;

public class BDaliDevicePointExt extends BPointDeviceExt {
   public static final Action discoverPoints = newAction(4, (BFacets)null);
   public static final Type TYPE;

   public BOrd discoverPoints() {
      return (BOrd)this.invoke(discoverPoints, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public Type getDeviceType() {
      return BGenericDaliDevice.TYPE;
   }

   public Type getPointFolderType() {
      return BDaliPointsFolder.TYPE;
   }

   public Type getProxyExtType() {
      return BPointProxyExt.TYPE;
   }

   public BOrd doDiscoverPoints() {
      BDiscoverPointsJob var1 = new BDiscoverPointsJob((BGenericDaliDevice)this.getParent());
      return var1.submit((Context)null);
   }

   static {
      TYPE = Sys.loadType(BDaliDevicePointExt.class);
   }
}
