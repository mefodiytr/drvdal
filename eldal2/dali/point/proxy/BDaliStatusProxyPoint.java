package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import javax.baja.control.BNumericPoint;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BDaliStatusProxyPoint extends BNumericPoint {
   public static final Property statusOfControlGear = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property lampFailure = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property lampArcPowerOn = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property limitError = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property fadeRunning = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property resetState = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property missingShortAddress = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Property powerFailure = newProperty(265, new BStatusBoolean(false), (BFacets)null);
   public static final Type TYPE;

   public BStatusBoolean getStatusOfControlGear() {
      return (BStatusBoolean)this.get(statusOfControlGear);
   }

   public void setStatusOfControlGear(BStatusBoolean var1) {
      this.set(statusOfControlGear, var1, (Context)null);
   }

   public BStatusBoolean getLampFailure() {
      return (BStatusBoolean)this.get(lampFailure);
   }

   public void setLampFailure(BStatusBoolean var1) {
      this.set(lampFailure, var1, (Context)null);
   }

   public BStatusBoolean getLampArcPowerOn() {
      return (BStatusBoolean)this.get(lampArcPowerOn);
   }

   public void setLampArcPowerOn(BStatusBoolean var1) {
      this.set(lampArcPowerOn, var1, (Context)null);
   }

   public BStatusBoolean getLimitError() {
      return (BStatusBoolean)this.get(limitError);
   }

   public void setLimitError(BStatusBoolean var1) {
      this.set(limitError, var1, (Context)null);
   }

   public BStatusBoolean getFadeRunning() {
      return (BStatusBoolean)this.get(fadeRunning);
   }

   public void setFadeRunning(BStatusBoolean var1) {
      this.set(fadeRunning, var1, (Context)null);
   }

   public BStatusBoolean getResetState() {
      return (BStatusBoolean)this.get(resetState);
   }

   public void setResetState(BStatusBoolean var1) {
      this.set(resetState, var1, (Context)null);
   }

   public BStatusBoolean getMissingShortAddress() {
      return (BStatusBoolean)this.get(missingShortAddress);
   }

   public void setMissingShortAddress(BStatusBoolean var1) {
      this.set(missingShortAddress, var1, (Context)null);
   }

   public BStatusBoolean getPowerFailure() {
      return (BStatusBoolean)this.get(powerFailure);
   }

   public void setPowerFailure(BStatusBoolean var1) {
      this.set(powerFailure, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void onExecute(BStatusValue var1, Context var2) {
      int var3 = (int)this.getOut().getValue();
      BStatusBoolean var10000 = this.getStatusOfControlGear();
      boolean var10001 = false;
      if ((var3 & 1) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getLampFailure();
      var10001 = false;
      if ((var3 & 2) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getLampArcPowerOn();
      var10001 = false;
      if ((var3 & 4) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getLimitError();
      var10001 = false;
      if ((var3 & 8) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getFadeRunning();
      var10001 = false;
      if ((var3 & 16) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getResetState();
      var10001 = false;
      if ((var3 & 32) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getMissingShortAddress();
      var10001 = false;
      if ((var3 & 64) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
      var10000 = this.getPowerFailure();
      var10001 = false;
      if ((var3 & 128) != 0) {
         var10001 = true;
      }

      var10000.setValue(var10001);
   }

   static {
      TYPE = Sys.loadType(BDaliStatusProxyPoint.class);
   }
}
