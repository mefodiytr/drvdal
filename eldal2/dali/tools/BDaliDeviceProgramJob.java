package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import javax.baja.job.BSimpleJob;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Clock;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.config.DaliDeviceConfig;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliWrite;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.DaliArcPowerUtils;

public final class BDaliDeviceProgramJob extends BSimpleJob {
   public static final Type TYPE;
   private BDaliProgrammer daliProgrammer;
   private BDaliNetwork daliNetwork;
   private boolean programBallastsFlag;
   private boolean programSensorsFlag;
   private int programRepeatCount;

   public final Type getType() {
      return TYPE;
   }

   public final void run(Context var1) throws Exception {
      if (this.daliProgrammer == null) {
         this.cfgError("DALI device programming failed (invalid programmer)");
      }

      this.daliNetwork = this.daliProgrammer.getDaliNetwork();
      if (this.daliNetwork == null) {
         this.cfgError("DALI device programming failed (invalid DALI network)");
      }

      this.programRepeatCount = this.daliProgrammer.getProgramRepeatCount();
      boolean var7 = true;
      this.cfgLog("DALI device programming starting ...");
      this.daliProgrammer.setStatusMessage("DALI device programming started");
      long var2 = Clock.millis();
      this.progress(0);
      if (this.programBallastsFlag) {
         int var8;
         if (this.daliProgrammer.getNetworkBroadcast()) {
            var8 = 63;
            this.cfgLog("Programming all devices on DALI network");
         } else {
            if (this.daliProgrammer.getGroupAddress().getStatus().isNull()) {
               this.cfgError("DALI device programming failed (null DALI group address)");
            }

            int var6 = (int)this.daliProgrammer.getGroupAddress().getValue();
            this.cfgLog("Programming DALI devices in group " + var6);
            --var6;
            if (var6 < 0 || var6 > 15) {
               this.cfgError("DALI device programming failed (DALI group address out of range)");
            }

            var8 = var6;
         }

         this.cfgLog("DALI ballast programming started ...");
         this.daliProgrammer.getLog().trace("DALI address = 0x" + Integer.toHexString(var8));
         this.progLevelValue(var8, 64, this.daliProgrammer.getScene1());
         this.progress(4);
         this.progLevelValue(var8, 65, this.daliProgrammer.getScene2());
         this.progress(8);
         this.progLevelValue(var8, 66, this.daliProgrammer.getScene3());
         this.progress(12);
         this.progLevelValue(var8, 67, this.daliProgrammer.getScene4());
         this.progress(16);
         this.progLevelValue(var8, 68, this.daliProgrammer.getScene5());
         this.progress(20);
         this.progLevelValue(var8, 69, this.daliProgrammer.getScene6());
         this.progress(24);
         this.progLevelValue(var8, 70, this.daliProgrammer.getScene7());
         this.progress(28);
         this.progLevelValue(var8, 71, this.daliProgrammer.getScene8());
         this.progress(32);
         this.progLevelValue(var8, 72, this.daliProgrammer.getScene9());
         this.progress(36);
         this.progLevelValue(var8, 73, this.daliProgrammer.getScene10());
         this.progress(40);
         this.progLevelValue(var8, 74, this.daliProgrammer.getScene11());
         this.progress(44);
         this.progLevelValue(var8, 75, this.daliProgrammer.getScene12());
         this.progress(48);
         this.progLevelValue(var8, 76, this.daliProgrammer.getScene13());
         this.progress(52);
         this.progLevelValue(var8, 77, this.daliProgrammer.getScene14());
         this.progress(56);
         this.progLevelValue(var8, 78, this.daliProgrammer.getScene15());
         this.progress(60);
         this.progLevelValue(var8, 79, this.daliProgrammer.getScene16());
         this.progress(64);
         this.progLevelValue(var8, 43, this.daliProgrammer.getMinLevel());
         this.progress(68);
         this.progLevelValue(var8, 42, this.daliProgrammer.getMaxLevel());
         this.progress(72);
         this.progLevelValue(var8, 45, this.daliProgrammer.getPowerOnLevel());
         this.progress(76);
         this.progLevelValue(var8, 44, this.daliProgrammer.getSystemFailureLevel());
         this.progress(80);
         this.progEnumValue(var8, 47, this.daliProgrammer.getFadeRate());
         this.progress(90);
         this.progEnumValue(var8, 46, this.daliProgrammer.getFadeTime());
         this.cfgLog("DALI ballast programming complete");
      }

      if (this.programSensorsFlag) {
         this.clearSensorGroups();
      }

      this.progress(100);
      long var4 = Clock.millis();
      this.cfgLog("DALI device programming completed in " + (var4 - var2) + " ms");
      this.daliProgrammer.setStatusMessage("DALI device programming complete");
   }

   public final void clearSensorGroups() {
      this.cfgLog("DALI sensor programming started ...");

      for(int var1 = 0; var1 < 64; ++var1) {
         if (this.daliQuery(var1, 153) == 254) {
            this.cfgLog("Clearing groups for sensor with address " + (var1 + 1));
            DaliDeviceConfig var2 = new DaliDeviceConfig(this.daliNetwork, var1);
            var2.programGroups(0);
         }
      }

      this.cfgLog("DALI sensor programming complete");
   }

   private final void progLevelValue(int var1, int var2, BStatusNumeric var3) {
      if (!var3.getStatus().isNull()) {
         this.progRawValue(var1, var2, DaliArcPowerUtils.percentToDirectLevel(var3.getValue()));
      }

   }

   private final void progEnumValue(int var1, int var2, BStatusEnum var3) {
      if (!var3.getStatus().isNull()) {
         this.progRawValue(var1, var2, var3.getValue().getOrdinal());
      }

   }

   private final void progRawValue(int var1, int var2, int var3) {
      DaliWrite var4 = new DaliWrite(this.daliNetwork, true, var1, var2, var3);
      var4.setSendRepeat(this.programRepeatCount);
      var4.setStoreRepeat(this.programRepeatCount);
      var4.execute();
      this.handleDaliStatus(var4.getDaliStatus());
   }

   private final int daliQuery(int var1, int var2) {
      DaliQuery var3 = new DaliQuery(this.daliNetwork, var1, var2);
      var3.setQueryDelay(100);
      var3.setRetryMax(this.programRepeatCount);
      var3.execute();
      this.handleDaliStatus(var3.getDaliStatus());
      return var3.getResult();
   }

   private final void handleDaliStatus(int var1) {
      if (var1 == 34) {
         this.cfgError("DALI device programming failed (DIM fault)");
      }

      if (var1 == 4) {
         this.cfgError("DALI device programming failed (no DALI power)");
      }

   }

   private final void cfgLog(String var1) {
      if (this.daliProgrammer != null) {
         this.daliProgrammer.getLog().trace(var1);
      }

      this.log().message(var1);
   }

   private final void cfgError(String var1) {
      if (this.daliProgrammer != null) {
         this.daliProgrammer.getLog().error(var1);
         this.daliProgrammer.setStatusMessage("ERROR: " + var1);
      }

      this.log().message("ERROR: " + var1);
      throw new RuntimeException("ERROR: " + var1);
   }

   public BDaliDeviceProgramJob() {
     this.daliProgrammer = null;
     this.daliNetwork = null;
     this.programBallastsFlag = false;
     this.programSensorsFlag = false;
     this.programRepeatCount = 5;
     
      this.daliProgrammer = null;
   }

   public BDaliDeviceProgramJob(BDaliProgrammer var1, boolean var2, boolean var3) {
     this.daliProgrammer = null;
     this.daliNetwork = null;
     this.programBallastsFlag = false;
     this.programSensorsFlag = false;
     this.programRepeatCount = 5;
     
      this.daliProgrammer = var1;
      this.programBallastsFlag = var2;
      this.programSensorsFlag = var3;
   }

   static {
      TYPE = Sys.loadType(BDaliDeviceProgramJob.class);
   }
}
