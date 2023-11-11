package uk.co.controlnetworksolutions.elitedali2.dali.discovery;

import javax.baja.sys.BComponent;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;

public final class BDaliDiscoverDeviceEntry extends BComponent {
   public static final Property address = newProperty(0, "", (BFacets)null);
   public static final Property deviceType;
   public static final Property status;
   public static final Type TYPE;

   public final String getAddress() {
      return this.getString(address);
   }

   public final void setAddress(String var1) {
      this.setString(address, var1, (Context)null);
   }

   public final BDaliDeviceType getDeviceType() {
      return (BDaliDeviceType)this.get(deviceType);
   }

   public final void setDeviceType(BDaliDeviceType var1) {
      this.set(deviceType, var1, (Context)null);
   }

   public final String getStatus() {
      return this.getString(status);
   }

   public final void setStatus(String var1) {
      this.setString(status, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public BDaliDiscoverDeviceEntry() {
   }

   public BDaliDiscoverDeviceEntry(String var1, BDaliDeviceType var2, String var3) {
      this.setAddress(var1);
      this.setDeviceType(var2);
      this.setStatus(var3);
   }

   static {
      deviceType = newProperty(0, BDaliDeviceType.unknownDevice, (BFacets)null);
      status = newProperty(0, "Unknown", (BFacets)null);
      TYPE = Sys.loadType(BDaliDiscoverDeviceEntry.class);
   }
}
