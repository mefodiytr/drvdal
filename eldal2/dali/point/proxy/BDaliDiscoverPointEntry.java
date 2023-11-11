package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import javax.baja.sys.BComponent;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BDaliDiscoverPointEntry extends BComponent {
   public static final Property pointIdentifier = newProperty(0, "unknown", (BFacets)null);
   public static final Property pointType = newProperty(0, "unknown", (BFacets)null);
   public static final Type TYPE;

   public final String getPointIdentifier() {
      return this.getString(pointIdentifier);
   }

   public final void setPointIdentifier(String var1) {
      this.setString(pointIdentifier, var1, (Context)null);
   }

   public final String getPointType() {
      return this.getString(pointType);
   }

   public final void setPointType(String var1) {
      this.setString(pointType, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public BDaliDiscoverPointEntry() {
   }

   public BDaliDiscoverPointEntry(String var1, Type var2) {
      this.setPointIdentifier(var1);
      this.setPointType(var2.toString());
   }

   static {
      TYPE = Sys.loadType(BDaliDiscoverPointEntry.class);
   }
}
