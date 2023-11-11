package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import javax.baja.control.BNumericPoint;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BDaliEmergencyFeaturesProxyPoint extends BNumericPoint {
   public static final Property integralEmergencyControlGear = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property maintainedControlGear = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property switchedMaintainedControlGear = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property autoTestCapability = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property adjustableEmergencyLevel = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property hardwiredInhibitSupported = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property physicalSelectionSupported = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property relightInRestModeSupported = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Type TYPE;

   public BStatusBoolean getIntegralEmergencyControlGear() {
      return (BStatusBoolean)this.get(integralEmergencyControlGear);
   }

   public void setIntegralEmergencyControlGear(BStatusBoolean var1) {
      this.set(integralEmergencyControlGear, var1, (Context)null);
   }

   public BStatusBoolean getMaintainedControlGear() {
      return (BStatusBoolean)this.get(maintainedControlGear);
   }

   public void setMaintainedControlGear(BStatusBoolean var1) {
      this.set(maintainedControlGear, var1, (Context)null);
   }

   public BStatusBoolean getSwitchedMaintainedControlGear() {
      return (BStatusBoolean)this.get(switchedMaintainedControlGear);
   }

   public void setSwitchedMaintainedControlGear(BStatusBoolean var1) {
      this.set(switchedMaintainedControlGear, var1, (Context)null);
   }

   public BStatusBoolean getAutoTestCapability() {
      return (BStatusBoolean)this.get(autoTestCapability);
   }

   public void setAutoTestCapability(BStatusBoolean var1) {
      this.set(autoTestCapability, var1, (Context)null);
   }

   public BStatusBoolean getAdjustableEmergencyLevel() {
      return (BStatusBoolean)this.get(adjustableEmergencyLevel);
   }

   public void setAdjustableEmergencyLevel(BStatusBoolean var1) {
      this.set(adjustableEmergencyLevel, var1, (Context)null);
   }

   public BStatusBoolean getHardwiredInhibitSupported() {
      return (BStatusBoolean)this.get(hardwiredInhibitSupported);
   }

   public void setHardwiredInhibitSupported(BStatusBoolean var1) {
      this.set(hardwiredInhibitSupported, var1, (Context)null);
   }

   public BStatusBoolean getPhysicalSelectionSupported() {
      return (BStatusBoolean)this.get(physicalSelectionSupported);
   }

   public void setPhysicalSelectionSupported(BStatusBoolean var1) {
      this.set(physicalSelectionSupported, var1, (Context)null);
   }

   public BStatusBoolean getRelightInRestModeSupported() {
      return (BStatusBoolean)this.get(relightInRestModeSupported);
   }

   public void setRelightInRestModeSupported(BStatusBoolean var1) {
      this.set(relightInRestModeSupported, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void onExecute(BStatusValue var1, Context var2) {
      int var3 = (int)this.getOut().getValue();
      BStatusBoolean var10000 = this.getIntegralEmergencyControlGear();
      boolean var10001 = false;
      if ((var3 & 1) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getMaintainedControlGear();
      var10001 = false;
      if ((var3 & 2) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getSwitchedMaintainedControlGear();
      var10001 = false;
      if ((var3 & 4) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getAutoTestCapability();
      var10001 = false;
      if ((var3 & 8) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getAdjustableEmergencyLevel();
      var10001 = false;
      if ((var3 & 16) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getHardwiredInhibitSupported();
      var10001 = false;
      if ((var3 & 32) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getPhysicalSelectionSupported();
      var10001 = false;
      if ((var3 & 64) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getRelightInRestModeSupported();
      var10001 = false;
      if ((var3 & 128) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
   }

   static {
      TYPE = Sys.loadType(BDaliEmergencyFeaturesProxyPoint.class);
   }
}
