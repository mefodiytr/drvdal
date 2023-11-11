package uk.co.controlnetworksolutions.elitedali2.dali.network;

import javax.baja.driver.ping.BIPingable;
import javax.baja.driver.ping.BPingMonitor;
import javax.baja.sys.BFacets;
import javax.baja.sys.BRelTime;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BDaliPingMonitor extends BPingMonitor {
   public static final Property pingFrequency = newProperty(0, BRelTime.make(300000L), (BFacets)null);
   public static final Type TYPE;

   public final BRelTime getPingFrequency() {
      return (BRelTime)this.get(pingFrequency);
   }

   public final void setPingFrequency(BRelTime var1) {
      this.set(pingFrequency, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final boolean isPingEnabled(BIPingable var1) {
      return var1.getStatus().isDisabled() ^ true;
   }

   static {
      TYPE = Sys.loadType(BDaliPingMonitor.class);
   }
}
