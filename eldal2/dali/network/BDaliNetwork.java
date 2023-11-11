package uk.co.controlnetworksolutions.elitedali2.dali.network;

import com.tridium.basicdriver.util.BBasicPollScheduler;
import javax.baja.driver.BDevice;
import javax.baja.driver.BDeviceNetwork;
import javax.baja.driver.point.BTuningPolicyMap;
import javax.baja.driver.util.BPollScheduler;
import javax.baja.log.Log;
import javax.baja.naming.BOrd;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusString;
import javax.baja.sys.Action;
import javax.baja.sys.BBoolean;
import javax.baja.sys.BFacets;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.addressing.BDaliAddressConflictResolutionJob;
import uk.co.controlnetworksolutions.elitedali2.dali.addressing.DaliAddressingTask;
import uk.co.controlnetworksolutions.elitedali2.dali.addressing.DaliAddressingTaskModeA;
import uk.co.controlnetworksolutions.elitedali2.dali.config.BDaliDeviceConfigurationJob;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.discovery.BDiscoverDaliDevicesJob;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliLevel;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliOperation;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliSpecialCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.DaliBusPowerUtils;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.IdentifyTask;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public final class BDaliNetwork extends BDeviceNetwork {
   public static final Property monitor = newProperty(0, new BDaliPingMonitor(), (BFacets)null);
   public static final Property daliPower = newProperty(257, new BStatusBoolean(true), (BFacets)null);
   public static final Property pollScheduler = newProperty(0, new BBasicPollScheduler(), (BFacets)null);
   public static final Property tuningPolicies = newProperty(0, new BTuningPolicyMap(), (BFacets)null);
   public static final Property location;
   public static final Property transmitCount;
   public static final Property retryMax;
   public static final Property reverseAddressingMode;
   public static final Action allDevicesOff;
   public static final Action allDevicesOn;
   public static final Action startNetworkIdentify;
   public static final Action stopNetworkIdentify;
   public static final Action resolveAllAddressConflicts;
   public static final Action submitDeviceDiscoverJob;
   public static final Action submitAddressAllDevicesJob;
   public static final Action submitAddressNewDevicesJob;
   public static final Action setDaliPowerIgnore;
   public static final Action ping;
   public static final Action checkDaliPower;
   public static final Type TYPE;
   private BDaliDeviceConfigurationJob devCfgJob;
   private boolean addressingJobInProgress;
   private boolean identifyAbortFlag;
   private IdentifyTask identifyTask;
   private boolean networkInactive;
   private Log log;

   public final BDaliPingMonitor getMonitor() {
      return (BDaliPingMonitor)this.get(monitor);
   }

   public final void setMonitor(BDaliPingMonitor var1) {
      this.set(monitor, var1, (Context)null);
   }

   public final BStatusBoolean getDaliPower() {
      return (BStatusBoolean)this.get(daliPower);
   }

   public final void setDaliPower(BStatusBoolean var1) {
      this.set(daliPower, var1, (Context)null);
   }

   public final BPollScheduler getPollScheduler() {
      return (BPollScheduler)this.get(pollScheduler);
   }

   public final void setPollScheduler(BPollScheduler var1) {
      this.set(pollScheduler, var1, (Context)null);
   }

   public final BTuningPolicyMap getTuningPolicies() {
      return (BTuningPolicyMap)this.get(tuningPolicies);
   }

   public final void setTuningPolicies(BTuningPolicyMap var1) {
      this.set(tuningPolicies, var1, (Context)null);
   }

   public final BStatusString getLocation() {
      return (BStatusString)this.get(location);
   }

   public final void setLocation(BStatusString var1) {
      this.set(location, var1, (Context)null);
   }

   public final int getTransmitCount() {
      return this.getInt(transmitCount);
   }

   public final void setTransmitCount(int var1) {
      this.setInt(transmitCount, var1, (Context)null);
   }

   public final int getRetryMax() {
      return this.getInt(retryMax);
   }

   public final void setRetryMax(int var1) {
      this.setInt(retryMax, var1, (Context)null);
   }

   public final boolean getReverseAddressingMode() {
      return this.getBoolean(reverseAddressingMode);
   }

   public final void setReverseAddressingMode(boolean var1) {
      this.setBoolean(reverseAddressingMode, var1, (Context)null);
   }

   public final void allDevicesOff() {
      this.invoke(allDevicesOff, (BValue)null, (Context)null);
   }

   public final void allDevicesOn() {
      this.invoke(allDevicesOn, (BValue)null, (Context)null);
   }

   public final void startNetworkIdentify() {
      this.invoke(startNetworkIdentify, (BValue)null, (Context)null);
   }

   public final void stopNetworkIdentify() {
      this.invoke(stopNetworkIdentify, (BValue)null, (Context)null);
   }

   public final void resolveAllAddressConflicts() {
      this.invoke(resolveAllAddressConflicts, (BValue)null, (Context)null);
   }

   public final BOrd submitDeviceDiscoverJob() {
      return (BOrd)this.invoke(submitDeviceDiscoverJob, (BValue)null, (Context)null);
   }

   public final BOrd submitAddressAllDevicesJob() {
      return (BOrd)this.invoke(submitAddressAllDevicesJob, (BValue)null, (Context)null);
   }

   public final BOrd submitAddressNewDevicesJob() {
      return (BOrd)this.invoke(submitAddressNewDevicesJob, (BValue)null, (Context)null);
   }

   public final void setDaliPowerIgnore(BBoolean var1) {
      this.invoke(setDaliPowerIgnore, var1, (Context)null);
   }

   public final void ping() {
      this.invoke(ping, (BValue)null, (Context)null);
   }

   public final void checkDaliPower() {
      this.invoke(checkDaliPower, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final Type getDeviceType() {
      return BGenericDaliDevice.TYPE;
   }

   public final BDaliNetwork getDaliNetwork() {
      return this;
   }

   public final Type getDeviceFolderType() {
      return BDaliGroupFolder.TYPE;
   }

   public final void started() throws Exception {
      super.started();
      this.getTuningPolicies().getDefaultPolicy().setWriteOnEnabled(false);
      this.getTuningPolicies().getDefaultPolicy().setWriteOnStart(false);
      this.getTuningPolicies().getDefaultPolicy().setWriteOnUp(false);
      this.addressingJobInProgress = false;
      this.networkInactive = false;
      this.pingOk();
      this.doCheckDaliPower();
   }

   public final void stopped() throws Exception {
      super.stopped();
      this.doStopNetworkIdentify();
   }

   public final void added(Property var1, Context var2) {
      if (this.isNetworkOk()) {
         if (BDaliDeviceType.isSupportedDaliDevice(var1.getType())) {
            BGenericDaliDevice var3 = (BGenericDaliDevice)this.get(var1);
            if (this.devCfgJob == null) {
               this.devCfgJob = new BDaliDeviceConfigurationJob(this);
            } else if (!this.devCfgJob.isAlive()) {
               this.devCfgJob = new BDaliDeviceConfigurationJob(this);
            }

            this.devCfgJob.add((BGenericDaliDevice)this.get(var1));
         } else if (var1.getType() == BDaliGroupFolder.TYPE) {
            BDaliGroupFolder var4 = (BDaliGroupFolder)this.get(var1);
            if ((!(var4.getGroupAddress().getValue() > 0.0) || !(var4.getGroupAddress().getValue() < 17.0)) && !var4.getGroupAddress().getStatus().isNull()) {
               this.log.error("Adding group " + var4.getName() + " to database failed");
            } else {
               this.log.trace("Added DALI group '" + var4.getName() + '\'');
            }
         }
      }

   }

   public final boolean broadcastCommand(int var1) {
      if (var1 != 255 && this.isNetworkOk()) {
         DaliCommand var2 = new DaliCommand(this, true, 63, var1);
         this.setOpRetransmitCount(var2, var1);
         var2.execute();
         return var2.success();
      } else {
         return false;
      }
   }

   public final boolean groupCommand(int var1, int var2) {
      if (var2 != 255 && this.isNetworkOk()) {
         DaliCommand var3 = new DaliCommand(this, true, var1, var2);
         this.setOpRetransmitCount(var3, var2);
         var3.execute();
         return var3.success();
      } else {
         return false;
      }
   }

   public final boolean deviceCommand(int var1, int var2) {
      if (var2 != 255 && this.isNetworkOk()) {
         DaliCommand var3 = new DaliCommand(this, false, var1, var2);
         this.setOpRetransmitCount(var3, var2);
         var3.execute();
         return var3.success();
      } else {
         return false;
      }
   }

   public final boolean specialCommand(int var1, int var2) {
      if (this.isNetworkOk()) {
         DaliSpecialCommand var3 = new DaliSpecialCommand(this, var1, var2);
         var3.execute();
         return var3.success();
      } else {
         return false;
      }
   }

   public final void setOpRetransmitCount(DaliOperation var1, int var2) {
      if (var2 != 1 && var2 != 2 && var2 != 3 && var2 != 4 && var2 != 7 && var2 != 8) {
         var1.setTransmitRepeatCount(this.getTransmitCount() - 1);
      }

   }

   public final void handleDaliStatus(int var1) {
      switch (var1) {
         case 4:
            this.getDaliPower().setValue(false);
            break;
         case 53:
            this.getDaliPower().setValue(true);
            this.setStatus(BStatus.ok);
            this.configOk();
      }

   }

   public final void doAllDevicesOff() {
      this.broadcastCommand(0);
   }

   public final void doAllDevicesOn() {
      if (this.isNetworkOk()) {
         DaliLevel var1 = new DaliLevel(this, true, 63, 100.0);
         var1.setTransmitRepeatCount(this.getTransmitCount() - 1);
         var1.execute();
      }

   }

   public final synchronized void doStartNetworkIdentify() {
      this.doStopNetworkIdentify();
      if (this.isNetworkOk()) {
         if (this.identifyTask != null && !this.identifyTask.isAlive()) {
            this.identifyTask = null;
         }

         if (this.identifyTask == null) {
            this.identifyTask = new IdentifyTask(this, true, 63);
            this.identifyTask.start();
         }
      }

   }

   public final synchronized void doStopNetworkIdentify() {
      if (this.identifyTask != null) {
         this.identifyTask.stop();
         this.identifyTask = null;
      }

   }

   public final void doResolveAllAddressConflicts() {
      if (this.isNetworkOk()) {
         BDaliAddressConflictResolutionJob var1 = new BDaliAddressConflictResolutionJob(this);
         var1.submit((Context)null);
      }
   }

   public final BOrd doSubmitDeviceDiscoverJob() {
      if (!this.isNetworkOk()) {
         return null;
      } else {
         BDiscoverDaliDevicesJob var1 = new BDiscoverDaliDevicesJob(this);
         return var1.submit((Context)null);
      }
   }

   public final BOrd doSubmitAddressAllDevicesJob() {
      if (!this.isNetworkOk()) {
         return null;
      } else if (this.isAddressingJobInProgress()) {
         this.log.warning("Cannot start DALI device addressing, an addressing job is already in progress");
         return null;
      } else {
         DaliAddressingTask var1 = this.getAddressingTask();
         return var1 != null ? var1.addressAll() : null;
      }
   }

   public final BOrd doSubmitAddressNewDevicesJob() {
      if (!this.isNetworkOk()) {
         return null;
      } else if (this.isAddressingJobInProgress()) {
         this.log.warning("Cannot start DALI device addressing, an addressing job is already in progress");
         return null;
      } else {
         DaliAddressingTask var1 = this.getAddressingTask();
         return var1 != null ? var1.addressNew() : null;
      }
   }

   public final void doSetDaliPowerIgnore(BBoolean var1) {
      DaliOperation.setIgnorePowerState(var1.getBoolean());
   }

   public final void doCheckDaliPower() {
      this.getDaliPowerState();
   }

   public final boolean getDaliPowerState() {
      boolean var1 = DaliBusPowerUtils.getPowerState(this);
      this.getDaliPower().setValue(var1);
      return var1;
   }

   public final DaliAddressingTask getAddressingTask() {
      return new DaliAddressingTaskModeA(this);
   }

   public final void addressingJobStarted() {
      BDevice[] var1 = this.getDevices();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].setEnabled(false);
      }

      this.addressingJobInProgress = true;
   }

   public final void addressingJobFinished() {
      BDevice[] var1 = this.getDevices();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].setEnabled(true);
      }

      this.addressingJobInProgress = false;
   }

   public final boolean isAddressingJobInProgress() {
      return this.addressingJobInProgress;
   }

   public final void setIdentifyAbortFlag(boolean var1) {
      this.identifyAbortFlag = var1;
   }

   public final boolean getIdentifyAbortFlag() {
      return this.identifyAbortFlag;
   }

   public final boolean isNetworkOk() {
      boolean var10000 = false;
      if (!this.isDisabled() && !this.isFault() && this.getStatus().isOk() && this.isRunning() && Sys.atSteadyState()) {
         var10000 = true;
      }

      boolean var1 = var10000;
      if (!var1) {
         if (!this.networkInactive && this.isRunning() && Sys.atSteadyState()) {
            if (this.isFault()) {
               this.log.error("DALI Network " + this.toPathString() + " is in fault");
            } else if (this.isDisabled()) {
               this.log.error("DALI Network " + this.toPathString() + " is disabled");
            } else {
               this.log.error("DALI Network " + this.toPathString() + " has a problem [" + this.getStatus().getBits() + ']');
            }
         }

         this.networkInactive = true;
      } else {
         this.networkInactive = false;
      }

      return var1;
   }

   public BDaliNetwork() {
     this.devCfgJob = null;
     this.addressingJobInProgress = false;
     this.identifyAbortFlag = false;
     this.identifyTask = null;
     this.networkInactive = false;
     this.log = Report.daliNetwork;
   }

   static {
      location = newProperty(256, new BStatusString("undefined", BStatus.nullStatus), (BFacets)null);
      transmitCount = newProperty(4, 2, (BFacets)null);
      retryMax = newProperty(4, 2, (BFacets)null);
      reverseAddressingMode = newProperty(4, false, (BFacets)null);
      allDevicesOff = newAction(0, (BFacets)null);
      allDevicesOn = newAction(0, (BFacets)null);
      startNetworkIdentify = newAction(0, (BFacets)null);
      stopNetworkIdentify = newAction(0, (BFacets)null);
      resolveAllAddressConflicts = newAction(128, (BFacets)null);
      submitDeviceDiscoverJob = newAction(4, (BFacets)null);
      submitAddressAllDevicesJob = newAction(4, (BFacets)null);
      submitAddressNewDevicesJob = newAction(4, (BFacets)null);
      setDaliPowerIgnore = newAction(4, BBoolean.make(false), (BFacets)null);
      ping = newAction(4, (BFacets)null);
      checkDaliPower = newAction(4, (BFacets)null);
      TYPE = Sys.loadType(BDaliNetwork.class);
   }
}
