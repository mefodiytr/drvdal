package uk.co.controlnetworksolutions.elitedali2.dali.devices;

import javax.baja.sys.Action;
import javax.baja.sys.BFacets;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.config.DaliDeviceConfig;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;

public final class BTridonicSensor extends BGenericDaliDevice {
   public static final Action startSensorIdentify = newAction(0, (BFacets)null);
   public static final Action stopSensorIdentify = newAction(0, (BFacets)null);
   public static final Action clearGroups = newAction(4, (BFacets)null);
   public static final Type TYPE;

   public final void startSensorIdentify() {
      this.invoke(startSensorIdentify, (BValue)null, (Context)null);
   }

   public final void stopSensorIdentify() {
      this.invoke(stopSensorIdentify, (BValue)null, (Context)null);
   }

   public final void clearGroups() {
      this.invoke(clearGroups, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void started() throws Exception {
      super.started();
      this.getConfigPoints().setDaliDeviceType(TYPE);
      this.setFlags(this.getSlot("configPoints"), 4);
   }

   public final void doStartSensorIdentify() {
      this.daliCommand(5);
   }

   public final void doStopSensorIdentify() {
      this.daliCommand(6);
   }

   public final void doClearGroups() {
      if (this.isDeviceOk()) {
         DaliDeviceConfig var1 = new DaliDeviceConfig(this.getDaliNetwork(), this.getDaliAddress());
         var1.programGroups(0);
      }

   }

   public final BDaliDeviceType getDaliDeviceType() {
      return BDaliDeviceType.sensorType1;
   }

   public final boolean isSensor() {
      return true;
   }

   static {
      TYPE = Sys.loadType(BTridonicSensor.class);
   }
}
