package uk.co.controlnetworksolutions.elitedali2.dim.point;

import javax.baja.control.BStringPoint;
import javax.baja.driver.point.BPointDeviceExt;
import javax.baja.driver.point.BPointFolder;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;

public class BDimPointDeviceExt extends BPointDeviceExt {
   public static final Type TYPE;

   public Type getType() {
      return TYPE;
   }

   public Type getDeviceType() {
      return BDimDevice.TYPE;
   }

   public Type getPointFolderType() {
      return BPointFolder.TYPE;
   }

   public Type getProxyExtType() {
      return BDimProxyExt.TYPE;
   }

   public void started() throws Exception {
      super.started();
      this.createPoint("version", 1);
      this.createPoint("serialNumber", 16);
      this.createPoint("manufacturedDate", 17);
   }

   private final void createPoint(String var1, int var2) {
      if (this.get(var1) == null) {
         BStringPoint var3 = new BStringPoint();
         var3.setProxyExt(new BDimProxyExt());
         BDimProxyExt var4 = (BDimProxyExt)var3.getProxyExt();
         var4.setIdentifier(var2);
         this.add(var1, var3);
      }

   }

   static {
      TYPE = Sys.loadType(BDimPointDeviceExt.class);
   }
}
