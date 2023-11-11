package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import javax.baja.control.BBooleanPoint;
import javax.baja.control.BControlPoint;
import javax.baja.control.BNumericPoint;
import javax.baja.job.BJobState;
import javax.baja.job.BSimpleJob;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.BComponent;
import javax.baja.sys.BLink;
import javax.baja.sys.Clock;
import javax.baja.sys.Context;
import javax.baja.sys.Knob;
import javax.baja.sys.Slot;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BEmergencyLighting;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType1;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType2;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType3;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliGroupFolder;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliStatusProxyPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BPointProxyExt;

public final class BDaliPopulatorJob extends BSimpleJob {
   public static final Type TYPE;
   private BComponent rootNode;
   private BDaliPopulator daliPopulator;
   private int totalDeviceCount;
   private boolean cancelOperation;

   public final Type getType() {
      return TYPE;
   }

   public final void run(Context var1) throws Exception {
      if (this.daliPopulator == null) {
         this.log().message("FAILED: Invalid populator invocation");
         throw new RuntimeException("Invalid populator invocation");
      } else if (this.rootNode == null) {
         this.daliPopulator.setStatusMessage("FAILED: Invalid root node");
         this.daliPopulator.getLog().error("FAILED: Invalid root node");
         this.log().message("FAILED: Invalid root node");
         throw new RuntimeException("Invalid root node");
      } else {
         this.cancelOperation = false;
         long var2 = Clock.millis();
         this.progress(0);
         this.taskLog("Searching for devices ...", true);
         this.daliPopulator.setNetworkCount(0);
         this.daliPopulator.setBallastCount(0);
         this.daliPopulator.setSensorCount(0);
         this.totalDeviceCount = 0;
         this.findDevices(this.rootNode);
         long var4 = Clock.millis();
         this.taskLog("Device search complete in " + (var4 - var2) + " milliseconds", true);
         this.taskLog(this.totalDeviceCount + " devices found", true);
         this.progress(100);
         if (!this.cancelOperation) {
            this.complete(BJobState.make(4));
         } else {
            this.taskLog("Job CANCELLED", true);
            this.complete(BJobState.make(3));
            throw new RuntimeException("Populator job cancelled");
         }
      }
   }

   public final void doCancel(Context var1) {
      this.cancelOperation = true;
   }

   private final void findDevices(BComponent var1) {
      if (var1 != null && !this.cancelOperation) {
         this.traceLog("Traversing node '" + var1.toPathString() + '\'');
         if (var1.getType() == BDaliNetwork.TYPE) {
            this.daliPopulator.setNetworkCount(this.daliPopulator.getNetworkCount() + 1);
            this.taskLog("Found DALI network " + this.daliPopulator.getNetworkCount() + ": " + var1.toPathString(), false);
         }

         if (var1.getType() == BDaliGroupFolder.TYPE) {
            if (this.daliPopulator.getBallastGroupSkip()) {
               this.traceLog("Skipping DALI group: " + var1.toPathString());
               return;
            }

            this.traceLog("Found DALI group: " + var1.toPathString());
         }

         BComponent[] var2 = var1.getChildComponents();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] instanceof BBallast) {
               this.PopulateDaliBallast((BBallast)var2[var3]);
            } else if (var2[var3] instanceof BEmergencyLighting) {
               this.PopulateDaliInverter((BEmergencyLighting)var2[var3]);
            } else if (var2[var3] instanceof BSensorType1) {
               this.PopulateSensorType1((BSensorType1)var2[var3]);
            } else if (var2[var3] instanceof BSensorType2) {
               this.PopulateSensorType2((BSensorType2)var2[var3]);
            } else if (var2[var3] instanceof BSensorType3) {
               this.PopulateSensorType3((BSensorType3)var2[var3]);
            } else {
               this.findDevices(var2[var3]);
            }

            if (this.cancelOperation) {
               break;
            }
         }
      }

   }

   private final void PopulateDaliBallast(BBallast var1) {
      if (var1 != null) {
         ++this.totalDeviceCount;
         this.daliPopulator.setBallastCount(this.daliPopulator.getBallastCount() + 1);
         this.taskLog("Found ballast " + this.daliPopulator.getBallastCount() + " with address " + var1.getAddress(), true);
         BControlPoint var2 = null;
         BControlPoint var3 = null;
         BControlPoint var4 = null;
         BControlPoint var5 = null;
         BControlPoint[] var6 = var1.getPoints().getPoints();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (this.isProxyIdentifier(var6[var7], "status")) {
               var2 = var6[var7];
               this.traceLog("Existing 'status' point found");
            }

            if (this.isProxyIdentifier(var6[var7], "actualLevel")) {
               var3 = var6[var7];
               this.traceLog("Existing 'actualLevel' point found");
            }

            if (this.isProxyIdentifier(var6[var7], "lampFailure")) {
               var4 = var6[var7];
               this.traceLog("Existing 'lampFailure' point found");
            }

            if (this.isProxyIdentifier(var6[var7], "lampPowerOn")) {
               var5 = var6[var7];
               this.traceLog("Existing 'lampPowerOn' point found");
            }
         }

         BPointProxyExt var9;
         if (this.daliPopulator.getBallastStatusPointEnable()) {
            if (var2 == null) {
               this.traceLog("Creating 'status' point");
               BDaliStatusProxyPoint var10 = new BDaliStatusProxyPoint();
               var9 = new BPointProxyExt("status");
               var10.setProxyExt(var9);
               var1.getPoints().add("status", var10);
            } else {
               var9 = (BPointProxyExt)var2.getProxyExt();
            }

            var9.setPollFrequency(this.daliPopulator.getBallastStatusPointPollPeriod());
         }

         if (this.daliPopulator.getBallastActualLevelPointEnable()) {
            if (var3 == null) {
               this.traceLog("Creating 'actualLevel' point");
               BNumericPoint var11 = new BNumericPoint();
               var9 = new BPointProxyExt("actualLevel");
               var11.setProxyExt(var9);
               var1.getPoints().add("actualLevel", var11);
            } else {
               var9 = (BPointProxyExt)var3.getProxyExt();
            }

            var9.setPollFrequency(this.daliPopulator.getBallastActualLevelPointPollPeriod());
         }

         if (this.daliPopulator.getBallastLampFailurePointEnable()) {
            if (var4 == null) {
               this.traceLog("Creating 'lampFailure' point");
               BBooleanPoint var12 = new BBooleanPoint();
               var9 = new BPointProxyExt("lampFailure");
               var12.setProxyExt(var9);
               var1.getPoints().add("lampFailure", var12);
            } else {
               var9 = (BPointProxyExt)var4.getProxyExt();
            }

            var9.setPollFrequency(this.daliPopulator.getBallastLampFailurePointPollPeriod());
         }

         if (this.daliPopulator.getBallastLampPowerOnPointEnable()) {
            if (var5 == null) {
               this.traceLog("Creating 'lampPowerOn' point");
               BBooleanPoint var13 = new BBooleanPoint();
               var9 = new BPointProxyExt("lampPowerOn");
               var13.setProxyExt(var9);
               var1.getPoints().add("lampPowerOn", var13);
            } else {
               var9 = (BPointProxyExt)var5.getProxyExt();
            }

            var9.setPollFrequency(this.daliPopulator.getBallastLampPowerOnPointPollPeriod());
         }

      }
   }

   private final void PopulateDaliInverter(BEmergencyLighting var1) {
      this.traceLog("DALI emergency lighting populator not supported yet");
   }

   private final void PopulateSensorType1(BSensorType1 var1) {
      if (var1 != null && this.daliPopulator.getOccupancySensorEnable()) {
         ++this.totalDeviceCount;
         this.daliPopulator.setSensorCount(this.daliPopulator.getSensorCount() + 1);
         this.taskLog("Found sensor " + this.daliPopulator.getSensorCount() + " with address " + var1.getAddress(), true);
         BStatusNumeric var2 = this.daliPopulator.getOccupancySensorPollPeriod();
         BStatusNumeric var3 = new BStatusNumeric(var2.getValue(), var2.getStatus());
         var1.setPollPeriod(var3);
      }
   }

   private final void PopulateSensorType2(BSensorType2 var1) {
      if (var1 != null && this.daliPopulator.getOccupancySensorEnable()) {
         ++this.totalDeviceCount;
         this.daliPopulator.setSensorCount(this.daliPopulator.getSensorCount() + 1);
         this.taskLog("Found sensor " + this.daliPopulator.getSensorCount() + " with address " + var1.getAddress(), true);
         BStatusNumeric var2 = this.daliPopulator.getOccupancySensorPollPeriod();
         BStatusNumeric var3 = new BStatusNumeric(var2.getValue(), var2.getStatus());
         var1.setPollPeriod(var3);
      }
   }

   private final void PopulateSensorType3(BSensorType3 var1) {
      if (var1 != null) {
         boolean var2 = false;
         BStatusNumeric var3;
         BStatusNumeric var4;
         if (this.daliPopulator.getOccupancySensorEnable()) {
            var3 = this.daliPopulator.getOccupancySensorPollPeriod();
            var4 = new BStatusNumeric(var3.getValue(), var3.getStatus());
            var1.setOccupancyPollPeriod(var4);
            var2 = true;
         }

         if (this.daliPopulator.getLuxSensorEnable()) {
            var3 = this.daliPopulator.getLuxSensorPollPeriod();
            var4 = new BStatusNumeric(var3.getValue(), var3.getStatus());
            var1.setLuxPollPeriod(var4);
            var2 = true;
         }

         if (this.daliPopulator.getTemperatureSensorEnable()) {
            var3 = this.daliPopulator.getTemperatureSensorPollPeriod();
            var4 = new BStatusNumeric(var3.getValue(), var3.getStatus());
            var1.setTemperaturePollPeriod(var4);
            var2 = true;
         }

         if (var2) {
            ++this.totalDeviceCount;
            this.daliPopulator.setSensorCount(this.daliPopulator.getSensorCount() + 1);
            this.taskLog("Found sensor " + this.daliPopulator.getSensorCount() + " with address " + var1.getAddress(), true);
         }

      }
   }

   private final boolean isProxyIdentifier(BControlPoint var1, String var2) {
      if (var1.getProxyExt() instanceof BPointProxyExt) {
         BPointProxyExt var3 = (BPointProxyExt)var1.getProxyExt();
         return var3.getPointIdentifier().equalsIgnoreCase(var2);
      } else {
         return false;
      }
   }

   private final void taskLog(String var1, boolean var2) {
      if (this.daliPopulator != null) {
         this.daliPopulator.getLog().trace(var1);
         if (var2) {
            this.daliPopulator.setStatusMessage(var1);
         }
      }

      this.log().message(var1);
   }

   private final void traceLog(String var1) {
      if (this.daliPopulator != null) {
         this.daliPopulator.getLog().trace(var1);
      }

   }

   public BDaliPopulatorJob() {
     this.rootNode = null;
     this.daliPopulator = null;
     this.totalDeviceCount = 0;
     this.cancelOperation = false;
     
      this.rootNode = null;
      this.daliPopulator = null;
      this.totalDeviceCount = 0;
      this.cancelOperation = false;
   }

   public BDaliPopulatorJob(BDaliPopulator var1) {
     this.rootNode = null;
     this.daliPopulator = null;
     this.totalDeviceCount = 0;
     this.cancelOperation = false;
     
      this.rootNode = null;
      this.daliPopulator = var1;
      this.totalDeviceCount = 0;
      this.cancelOperation = false;
   }

   public BDaliPopulatorJob(BComponent var1, BDaliPopulator var2) {
     this.rootNode = null;
     this.daliPopulator = null;
     this.totalDeviceCount = 0;
     this.cancelOperation = false;
     
      this.rootNode = var1;
      this.daliPopulator = var2;
      this.totalDeviceCount = 0;
      this.cancelOperation = false;
   }

   static {
      TYPE = Sys.loadType(BDaliPopulatorJob.class);
   }

   private class PointLink {
      private String pointName;
      private BComponent targetComponent;
      private Slot targetSlot;

      public void setProxyTarget(BComponent var1) {
         this.targetComponent = null;
         this.targetSlot = null;
         if (var1 != null) {
            Knob[] var2 = var1.getKnobs();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (var2[var3].getSourceSlotName().equalsIgnoreCase("out")) {
                  this.targetComponent = var2[var3].getTargetComponent();
                  this.targetSlot = var2[var3].getTargetSlot();
               }
            }
         }

      }

      public boolean targetValid() {
         boolean var10000 = false;
         if (this.targetComponent != null && this.targetSlot != null) {
            var10000 = true;
         }

         return var10000;
      }

      public void makeLink(BComponent var1) {
         if (var1 != null && this.targetComponent != null && this.targetSlot != null) {
            BLink var2 = new BLink(var1.getHandleOrd(), this.pointName, this.targetSlot.getName(), true);
            this.targetComponent.add(this.pointName, var2);
         }

      }

      public PointLink(String var2) {
        this.pointName = null;
        this.targetComponent = null;
        this.targetSlot = null;
        
         this.pointName = var2;
         this.targetComponent = null;
         this.targetSlot = null;
      }
   }
}
