package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import javax.baja.control.BNumericPoint;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BDaliEmergencyModeProxyPoint extends BNumericPoint {
   public static final Property restModeActive = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property normalModeActive = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property emergencyModeActive = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property extendedEmergencyModeActive = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property functionTestInProgress = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property durationTestInProgress = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property hardwiredInhibitIsActive = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property hardwiredSwitchIsOn = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Type TYPE;

   public BStatusBoolean getRestModeActive() {
      return (BStatusBoolean)this.get(restModeActive);
   }

   public void setRestModeActive(BStatusBoolean var1) {
      this.set(restModeActive, var1, (Context)null);
   }

   public BStatusBoolean getNormalModeActive() {
      return (BStatusBoolean)this.get(normalModeActive);
   }

   public void setNormalModeActive(BStatusBoolean var1) {
      this.set(normalModeActive, var1, (Context)null);
   }

   public BStatusBoolean getEmergencyModeActive() {
      return (BStatusBoolean)this.get(emergencyModeActive);
   }

   public void setEmergencyModeActive(BStatusBoolean var1) {
      this.set(emergencyModeActive, var1, (Context)null);
   }

   public BStatusBoolean getExtendedEmergencyModeActive() {
      return (BStatusBoolean)this.get(extendedEmergencyModeActive);
   }

   public void setExtendedEmergencyModeActive(BStatusBoolean var1) {
      this.set(extendedEmergencyModeActive, var1, (Context)null);
   }

   public BStatusBoolean getFunctionTestInProgress() {
      return (BStatusBoolean)this.get(functionTestInProgress);
   }

   public void setFunctionTestInProgress(BStatusBoolean var1) {
      this.set(functionTestInProgress, var1, (Context)null);
   }

   public BStatusBoolean getDurationTestInProgress() {
      return (BStatusBoolean)this.get(durationTestInProgress);
   }

   public void setDurationTestInProgress(BStatusBoolean var1) {
      this.set(durationTestInProgress, var1, (Context)null);
   }

   public BStatusBoolean getHardwiredInhibitIsActive() {
      return (BStatusBoolean)this.get(hardwiredInhibitIsActive);
   }

   public void setHardwiredInhibitIsActive(BStatusBoolean var1) {
      this.set(hardwiredInhibitIsActive, var1, (Context)null);
   }

   public BStatusBoolean getHardwiredSwitchIsOn() {
      return (BStatusBoolean)this.get(hardwiredSwitchIsOn);
   }

   public void setHardwiredSwitchIsOn(BStatusBoolean var1) {
      this.set(hardwiredSwitchIsOn, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void onExecute(BStatusValue var1, Context var2) {
      int var3 = (int)this.getOut().getValue();
      BStatusBoolean var10000 = this.getRestModeActive();
      boolean var10001 = false;
      if ((var3 & 1) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getNormalModeActive();
      var10001 = false;
      if ((var3 & 2) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getEmergencyModeActive();
      var10001 = false;
      if ((var3 & 4) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getExtendedEmergencyModeActive();
      var10001 = false;
      if ((var3 & 8) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getFunctionTestInProgress();
      var10001 = false;
      if ((var3 & 16) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getDurationTestInProgress();
      var10001 = false;
      if ((var3 & 32) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getHardwiredInhibitIsActive();
      var10001 = false;
      if ((var3 & 64) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getHardwiredSwitchIsOn();
      var10001 = false;
      if ((var3 & 128) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
   }

   static {
      TYPE = Sys.loadType(BDaliEmergencyModeProxyPoint.class);
   }
}
