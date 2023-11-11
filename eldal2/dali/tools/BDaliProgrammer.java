package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import javax.baja.log.Log;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BComponent;
import javax.baja.sys.BDouble;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BFadeRate;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BFadeTime;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliGroupFolder;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliWrite;

public final class BDaliProgrammer extends BComponent {
   public static final Property statusMessage = newProperty(265, "", (BFacets)null);
   public static final Property networkBroadcast = newProperty(256, true, (BFacets)null);
   public static final Property groupAddress;
   public static final Property scene1;
   public static final Property scene2;
   public static final Property scene3;
   public static final Property scene4;
   public static final Property scene5;
   public static final Property scene6;
   public static final Property scene7;
   public static final Property scene8;
   public static final Property scene9;
   public static final Property scene10;
   public static final Property scene11;
   public static final Property scene12;
   public static final Property scene13;
   public static final Property scene14;
   public static final Property scene15;
   public static final Property scene16;
   public static final Property fadeRate;
   public static final Property fadeTime;
   public static final Property minLevel;
   public static final Property maxLevel;
   public static final Property powerOnLevel;
   public static final Property systemFailureLevel;
   public static final Property programRepeatCount;
   public static final Action programDevices;
   public static final Action clearSensorGroups;
   public static final Action disableAllOccupancyIndicators;
   public static final Action changeAllOccupancyTimeoutValues;
   public static final Type TYPE;
   private static final Log log;
   private BDaliNetwork daliNetwork;
   private BDaliDeviceProgramJob deviceProgrammer;

   public final String getStatusMessage() {
      return this.getString(statusMessage);
   }

   public final void setStatusMessage(String var1) {
      this.setString(statusMessage, var1, (Context)null);
   }

   public final boolean getNetworkBroadcast() {
      return this.getBoolean(networkBroadcast);
   }

   public final void setNetworkBroadcast(boolean var1) {
      this.setBoolean(networkBroadcast, var1, (Context)null);
   }

   public final BStatusNumeric getGroupAddress() {
      return (BStatusNumeric)this.get(groupAddress);
   }

   public final void setGroupAddress(BStatusNumeric var1) {
      this.set(groupAddress, var1, (Context)null);
   }

   public final BStatusNumeric getScene1() {
      return (BStatusNumeric)this.get(scene1);
   }

   public final void setScene1(BStatusNumeric var1) {
      this.set(scene1, var1, (Context)null);
   }

   public final BStatusNumeric getScene2() {
      return (BStatusNumeric)this.get(scene2);
   }

   public final void setScene2(BStatusNumeric var1) {
      this.set(scene2, var1, (Context)null);
   }

   public final BStatusNumeric getScene3() {
      return (BStatusNumeric)this.get(scene3);
   }

   public final void setScene3(BStatusNumeric var1) {
      this.set(scene3, var1, (Context)null);
   }

   public final BStatusNumeric getScene4() {
      return (BStatusNumeric)this.get(scene4);
   }

   public final void setScene4(BStatusNumeric var1) {
      this.set(scene4, var1, (Context)null);
   }

   public final BStatusNumeric getScene5() {
      return (BStatusNumeric)this.get(scene5);
   }

   public final void setScene5(BStatusNumeric var1) {
      this.set(scene5, var1, (Context)null);
   }

   public final BStatusNumeric getScene6() {
      return (BStatusNumeric)this.get(scene6);
   }

   public final void setScene6(BStatusNumeric var1) {
      this.set(scene6, var1, (Context)null);
   }

   public final BStatusNumeric getScene7() {
      return (BStatusNumeric)this.get(scene7);
   }

   public final void setScene7(BStatusNumeric var1) {
      this.set(scene7, var1, (Context)null);
   }

   public final BStatusNumeric getScene8() {
      return (BStatusNumeric)this.get(scene8);
   }

   public final void setScene8(BStatusNumeric var1) {
      this.set(scene8, var1, (Context)null);
   }

   public final BStatusNumeric getScene9() {
      return (BStatusNumeric)this.get(scene9);
   }

   public final void setScene9(BStatusNumeric var1) {
      this.set(scene9, var1, (Context)null);
   }

   public final BStatusNumeric getScene10() {
      return (BStatusNumeric)this.get(scene10);
   }

   public final void setScene10(BStatusNumeric var1) {
      this.set(scene10, var1, (Context)null);
   }

   public final BStatusNumeric getScene11() {
      return (BStatusNumeric)this.get(scene11);
   }

   public final void setScene11(BStatusNumeric var1) {
      this.set(scene11, var1, (Context)null);
   }

   public final BStatusNumeric getScene12() {
      return (BStatusNumeric)this.get(scene12);
   }

   public final void setScene12(BStatusNumeric var1) {
      this.set(scene12, var1, (Context)null);
   }

   public final BStatusNumeric getScene13() {
      return (BStatusNumeric)this.get(scene13);
   }

   public final void setScene13(BStatusNumeric var1) {
      this.set(scene13, var1, (Context)null);
   }

   public final BStatusNumeric getScene14() {
      return (BStatusNumeric)this.get(scene14);
   }

   public final void setScene14(BStatusNumeric var1) {
      this.set(scene14, var1, (Context)null);
   }

   public final BStatusNumeric getScene15() {
      return (BStatusNumeric)this.get(scene15);
   }

   public final void setScene15(BStatusNumeric var1) {
      this.set(scene15, var1, (Context)null);
   }

   public final BStatusNumeric getScene16() {
      return (BStatusNumeric)this.get(scene16);
   }

   public final void setScene16(BStatusNumeric var1) {
      this.set(scene16, var1, (Context)null);
   }

   public final BStatusEnum getFadeRate() {
      return (BStatusEnum)this.get(fadeRate);
   }

   public final void setFadeRate(BStatusEnum var1) {
      this.set(fadeRate, var1, (Context)null);
   }

   public final BStatusEnum getFadeTime() {
      return (BStatusEnum)this.get(fadeTime);
   }

   public final void setFadeTime(BStatusEnum var1) {
      this.set(fadeTime, var1, (Context)null);
   }

   public final BStatusNumeric getMinLevel() {
      return (BStatusNumeric)this.get(minLevel);
   }

   public final void setMinLevel(BStatusNumeric var1) {
      this.set(minLevel, var1, (Context)null);
   }

   public final BStatusNumeric getMaxLevel() {
      return (BStatusNumeric)this.get(maxLevel);
   }

   public final void setMaxLevel(BStatusNumeric var1) {
      this.set(maxLevel, var1, (Context)null);
   }

   public final BStatusNumeric getPowerOnLevel() {
      return (BStatusNumeric)this.get(powerOnLevel);
   }

   public final void setPowerOnLevel(BStatusNumeric var1) {
      this.set(powerOnLevel, var1, (Context)null);
   }

   public final BStatusNumeric getSystemFailureLevel() {
      return (BStatusNumeric)this.get(systemFailureLevel);
   }

   public final void setSystemFailureLevel(BStatusNumeric var1) {
      this.set(systemFailureLevel, var1, (Context)null);
   }

   public final int getProgramRepeatCount() {
      return this.getInt(programRepeatCount);
   }

   public final void setProgramRepeatCount(int var1) {
      this.setInt(programRepeatCount, var1, (Context)null);
   }

   public final void programDevices() {
      this.invoke(programDevices, (BValue)null, (Context)null);
   }

   public final void clearSensorGroups() {
      this.invoke(clearSensorGroups, (BValue)null, (Context)null);
   }

   public final void disableAllOccupancyIndicators() {
      this.invoke(disableAllOccupancyIndicators, (BValue)null, (Context)null);
   }

   public final void changeAllOccupancyTimeoutValues(BDouble var1) {
      this.invoke(changeAllOccupancyTimeoutValues, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final Type[] getServiceTypes() {
      return null;
   }

   public final void started() {
      this.setStatusMessage("Initialising ...");
      this.resolveNetworkAccess();
   }

   public final void doProgramDevices() {
      if (this.daliNetwork == null) {
         log.error("Cannot start DALI device programming job: Invalid DALI network");
      } else if (this.deviceProgrammer != null && this.deviceProgrammer.isAlive()) {
         log.error("DALI programming job already running");
      } else {
         log.trace("Starting DALI device programming job");
         this.deviceProgrammer = new BDaliDeviceProgramJob(this, true, false);
         if (this.deviceProgrammer == null) {
            log.error("Cannot start DALI device programming job: Job failed to start");
         }

         this.deviceProgrammer.submit((Context)null);
      }
   }

   public final void doClearSensorGroups() {
      if (this.daliNetwork == null) {
         log.error("Cannot start DALI sensor group programming job: Invalid DALI network");
      } else if (this.deviceProgrammer != null && this.deviceProgrammer.isAlive()) {
         log.error("DALI programming job already running");
      } else {
         log.trace("Starting DALI sensor group programming job");
         this.deviceProgrammer = new BDaliDeviceProgramJob(this, false, true);
         if (this.deviceProgrammer == null) {
            log.error("Cannot start DALI sensor group programming job: Job failed to start");
         }

         this.deviceProgrammer.submit((Context)null);
      }
   }

   public final void doDisableAllOccupancyIndicators() {
      if (this.daliNetwork == null) {
         log.error("Cannot Disable All Occupancy Indicators: Invalid DALI network");
      } else {
         try {
            log.trace("Disabling All Occupancy Indicators (MSensor2)");
            DaliWrite var1 = new DaliWrite(this.getDaliNetwork(), true, 63, 254, 2);
            var1.setDaliDeviceType(128);
            var1.setSendRepeat(4);
            var1.setStoreRepeat(4);
            var1.execute();
         } catch (Exception var3) {
            log.error("Disable All Occupancy Indicators failed: " + var3);
         }

      }
   }

   public final void doChangeAllOccupancyTimeoutValues(BDouble var1) {
      if (this.daliNetwork == null) {
         log.error("Cannot Change All Occupancy Timeout Values: Invalid DALI network");
      } else {
         try {
            log.trace("Change All Occupancy Timeout Values (MSensor2)");
            DaliWrite var2 = new DaliWrite(this.getDaliNetwork(), true, 63, 252, (int)(var1.getDouble() * 10.0));
            var2.setDaliDeviceType(128);
            var2.setSendRepeat(4);
            var2.setStoreRepeat(4);
            var2.execute();
         } catch (Exception var4) {
            log.error("Change All Occupancy Timeout Values failed: " + var4);
         }

      }
   }

   public final BDaliNetwork getDaliNetwork() {
      return this.daliNetwork;
   }

   public final Log getLog() {
      return log;
   }

   private final void resolveNetworkAccess() {
      BComponent var1 = (BComponent)this.getParent();
      if (var1.getType() == BDaliNetwork.TYPE) {
         this.daliNetwork = (BDaliNetwork)var1;
         this.setStatusMessage("OK");
      } else if (var1.getType() == BDaliGroupFolder.TYPE) {
         BDaliGroupFolder var2 = (BDaliGroupFolder)var1;
         this.daliNetwork = var2.getDaliNetwork();
         this.setNetworkBroadcast(false);
         this.getGroupAddress().setValue(var2.getGroupAddress().getValue());
         this.getGroupAddress().setStatusNull(false);
         this.setStatusMessage("OK");
      } else {
         log.error("DALI programmer parent is not a DALI network");
         this.setStatusMessage("ERROR: DALI programmer parent is not a DALI network");
      }

   }

   public BDaliProgrammer() {
     this.daliNetwork = null;
     this.deviceProgrammer = null;
   }

   static {
      groupAddress = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), (BFacets)null);
      scene1 = newProperty(256, new BStatusNumeric(100.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene2 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene3 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene4 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene5 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene6 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene7 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene8 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene9 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene10 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene11 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene12 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene13 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene14 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene15 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene16 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      fadeRate = newProperty(256, new BStatusEnum(BFadeRate.Steps_360, BStatus.nullStatus), (BFacets)null);
      fadeTime = newProperty(256, new BStatusEnum(BFadeTime.NoFade, BStatus.nullStatus), (BFacets)null);
      minLevel = newProperty(256, new BStatusNumeric(1.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      maxLevel = newProperty(256, new BStatusNumeric(100.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      powerOnLevel = newProperty(256, new BStatusNumeric(80.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      systemFailureLevel = newProperty(256, new BStatusNumeric(80.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      programRepeatCount = newProperty(260, 5, (BFacets)null);
      programDevices = newAction(128, (BFacets)null);
      clearSensorGroups = newAction(0, (BFacets)null);
      disableAllOccupancyIndicators = newAction(0, (BFacets)null);
      changeAllOccupancyTimeoutValues = newAction(0, BDouble.make((double)2), BFacets.makeNumeric(UnitDatabase.getUnit("second"), BInteger.make(1), BDouble.make(1.0), BDouble.make(25.0)));
      TYPE = Sys.loadType(BDaliProgrammer.class);
      log = Log.getLog("elitedali2.programmer");
   }
}
