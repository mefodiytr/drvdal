package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import javax.baja.control.BNumericPoint;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BDaliEmergencyFailureProxyPoint extends BNumericPoint {
   public static final Property circuitFailure = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property batteryDurationFailure = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property batteryFailure = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property emergencyLampFailure = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property functionTestMaxDelayExceeded = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property durationTestMaxDelayExceeded = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property functionTestFailed = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property durationTestFailed = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Type TYPE;

   public BStatusBoolean getCircuitFailure() {
      return (BStatusBoolean)this.get(circuitFailure);
   }

   public void setCircuitFailure(BStatusBoolean var1) {
      this.set(circuitFailure, var1, (Context)null);
   }

   public BStatusBoolean getBatteryDurationFailure() {
      return (BStatusBoolean)this.get(batteryDurationFailure);
   }

   public void setBatteryDurationFailure(BStatusBoolean var1) {
      this.set(batteryDurationFailure, var1, (Context)null);
   }

   public BStatusBoolean getBatteryFailure() {
      return (BStatusBoolean)this.get(batteryFailure);
   }

   public void setBatteryFailure(BStatusBoolean var1) {
      this.set(batteryFailure, var1, (Context)null);
   }

   public BStatusBoolean getEmergencyLampFailure() {
      return (BStatusBoolean)this.get(emergencyLampFailure);
   }

   public void setEmergencyLampFailure(BStatusBoolean var1) {
      this.set(emergencyLampFailure, var1, (Context)null);
   }

   public BStatusBoolean getFunctionTestMaxDelayExceeded() {
      return (BStatusBoolean)this.get(functionTestMaxDelayExceeded);
   }

   public void setFunctionTestMaxDelayExceeded(BStatusBoolean var1) {
      this.set(functionTestMaxDelayExceeded, var1, (Context)null);
   }

   public BStatusBoolean getDurationTestMaxDelayExceeded() {
      return (BStatusBoolean)this.get(durationTestMaxDelayExceeded);
   }

   public void setDurationTestMaxDelayExceeded(BStatusBoolean var1) {
      this.set(durationTestMaxDelayExceeded, var1, (Context)null);
   }

   public BStatusBoolean getFunctionTestFailed() {
      return (BStatusBoolean)this.get(functionTestFailed);
   }

   public void setFunctionTestFailed(BStatusBoolean var1) {
      this.set(functionTestFailed, var1, (Context)null);
   }

   public BStatusBoolean getDurationTestFailed() {
      return (BStatusBoolean)this.get(durationTestFailed);
   }

   public void setDurationTestFailed(BStatusBoolean var1) {
      this.set(durationTestFailed, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void onExecute(BStatusValue var1, Context var2) {
      int var3 = (int)this.getOut().getValue();
      BStatusBoolean var10000 = this.getCircuitFailure();
      boolean var10001 = false;
      if ((var3 & 1) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getBatteryDurationFailure();
      var10001 = false;
      if ((var3 & 2) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getBatteryFailure();
      var10001 = false;
      if ((var3 & 4) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getEmergencyLampFailure();
      var10001 = false;
      if ((var3 & 8) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getFunctionTestMaxDelayExceeded();
      var10001 = false;
      if ((var3 & 16) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getDurationTestMaxDelayExceeded();
      var10001 = false;
      if ((var3 & 32) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getFunctionTestFailed();
      var10001 = false;
      if ((var3 & 64) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getDurationTestFailed();
      var10001 = false;
      if ((var3 & 128) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
   }

   static {
      TYPE = Sys.loadType(BDaliEmergencyFailureProxyPoint.class);
   }
}
