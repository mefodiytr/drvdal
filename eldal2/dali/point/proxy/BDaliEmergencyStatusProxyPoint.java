package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import javax.baja.control.BNumericPoint;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BDaliEmergencyStatusProxyPoint extends BNumericPoint {
   public static final Property inhibitMode = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property functionTestDoneAndResultValid = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property durationTestDoneAndResultValid = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property batteryFullyCharged = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property functionTestRequestPending = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property durationTestRequestPending = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property identificationActive = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property physicallySelected = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Type TYPE;

   public BStatusBoolean getInhibitMode() {
      return (BStatusBoolean)this.get(inhibitMode);
   }

   public void setInhibitMode(BStatusBoolean var1) {
      this.set(inhibitMode, var1, (Context)null);
   }

   public BStatusBoolean getFunctionTestDoneAndResultValid() {
      return (BStatusBoolean)this.get(functionTestDoneAndResultValid);
   }

   public void setFunctionTestDoneAndResultValid(BStatusBoolean var1) {
      this.set(functionTestDoneAndResultValid, var1, (Context)null);
   }

   public BStatusBoolean getDurationTestDoneAndResultValid() {
      return (BStatusBoolean)this.get(durationTestDoneAndResultValid);
   }

   public void setDurationTestDoneAndResultValid(BStatusBoolean var1) {
      this.set(durationTestDoneAndResultValid, var1, (Context)null);
   }

   public BStatusBoolean getBatteryFullyCharged() {
      return (BStatusBoolean)this.get(batteryFullyCharged);
   }

   public void setBatteryFullyCharged(BStatusBoolean var1) {
      this.set(batteryFullyCharged, var1, (Context)null);
   }

   public BStatusBoolean getFunctionTestRequestPending() {
      return (BStatusBoolean)this.get(functionTestRequestPending);
   }

   public void setFunctionTestRequestPending(BStatusBoolean var1) {
      this.set(functionTestRequestPending, var1, (Context)null);
   }

   public BStatusBoolean getDurationTestRequestPending() {
      return (BStatusBoolean)this.get(durationTestRequestPending);
   }

   public void setDurationTestRequestPending(BStatusBoolean var1) {
      this.set(durationTestRequestPending, var1, (Context)null);
   }

   public BStatusBoolean getIdentificationActive() {
      return (BStatusBoolean)this.get(identificationActive);
   }

   public void setIdentificationActive(BStatusBoolean var1) {
      this.set(identificationActive, var1, (Context)null);
   }

   public BStatusBoolean getPhysicallySelected() {
      return (BStatusBoolean)this.get(physicallySelected);
   }

   public void setPhysicallySelected(BStatusBoolean var1) {
      this.set(physicallySelected, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void onExecute(BStatusValue var1, Context var2) {
      int var3 = (int)this.getOut().getValue();
      BStatusBoolean var10000 = this.getInhibitMode();
      boolean var10001 = false;
      if ((var3 & 1) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getFunctionTestDoneAndResultValid();
      var10001 = false;
      if ((var3 & 2) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getDurationTestDoneAndResultValid();
      var10001 = false;
      if ((var3 & 4) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getBatteryFullyCharged();
      var10001 = false;
      if ((var3 & 8) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getFunctionTestRequestPending();
      var10001 = false;
      if ((var3 & 16) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getDurationTestRequestPending();
      var10001 = false;
      if ((var3 & 32) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getIdentificationActive();
      var10001 = false;
      if ((var3 & 64) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getPhysicallySelected();
      var10001 = false;
      if ((var3 & 128) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
   }

   static {
      TYPE = Sys.loadType(BDaliEmergencyStatusProxyPoint.class);
   }
}
