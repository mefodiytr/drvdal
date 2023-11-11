package uk.co.controlnetworksolutions.elitedali2.dim.network;

import javax.baja.sys.BComponent;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BDimLearnDeviceEntry extends BComponent {
   public static final Property address = newProperty(0, 241, (BFacets)null);
   public static final Type TYPE;

   public int getAddress() {
      return this.getInt(address);
   }

   public void setAddress(int var1) {
      this.setInt(address, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public BDimLearnDeviceEntry() {
   }

   public BDimLearnDeviceEntry(int var1) {
      this.setAddress(var1);
   }

   static {
      TYPE = Sys.loadType(BDimLearnDeviceEntry.class);
   }
}
