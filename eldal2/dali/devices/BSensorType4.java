package uk.co.controlnetworksolutions.elitedali2.dali.devices;

import javax.baja.log.Log;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.config.DaliDeviceConfig;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BOccupancy;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.AurexIndustriesUtils;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.IdentifyTask;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.PollQueryTask;
import uk.co.controlnetworksolutions.elitedali2.utils.CnsLicense;

public class BSensorType4 extends BDaliSensor {
   public static final Property occupancy;
   public static final Property occupancyBoolean;
   public static final Property luxLevel;
   public static final Property pollPeriod;
   public static final Property identifyActive;
   public static final Property logState;
   public static final Property logFaults;
   public static final Property luxNormalisation;
   public static final Action startSensorIdentify;
   public static final Action stopSensorIdentify;
   public static final Type TYPE;
   private static int sensor4DeviceCount;
   private static final String SENSOR_TYPE_4_LICENSE_FEATURE = "elitedaliSensor4";
   private static final String SENSOR_TYPE_4_LICENSE_LIMIT = "sensorType4.limit";
   private static final String SENSOR_TYPE_4_NAME = "SensorType4";
   private static final Log log;
   private boolean licenceValid;
   private SensorPollTask pollTask;
   private IdentifyTask identifyTask;

   public BStatusEnum getOccupancy() {
      return (BStatusEnum)this.get(occupancy);
   }

   public void setOccupancy(BStatusEnum var1) {
      this.set(occupancy, var1, (Context)null);
   }

   public BStatusBoolean getOccupancyBoolean() {
      return (BStatusBoolean)this.get(occupancyBoolean);
   }

   public void setOccupancyBoolean(BStatusBoolean var1) {
      this.set(occupancyBoolean, var1, (Context)null);
   }

   public BStatusNumeric getLuxLevel() {
      return (BStatusNumeric)this.get(luxLevel);
   }

   public void setLuxLevel(BStatusNumeric var1) {
      this.set(luxLevel, var1, (Context)null);
   }

   public BStatusNumeric getPollPeriod() {
      return (BStatusNumeric)this.get(pollPeriod);
   }

   public void setPollPeriod(BStatusNumeric var1) {
      this.set(pollPeriod, var1, (Context)null);
   }

   public BStatusBoolean getIdentifyActive() {
      return (BStatusBoolean)this.get(identifyActive);
   }

   public void setIdentifyActive(BStatusBoolean var1) {
      this.set(identifyActive, var1, (Context)null);
   }

   public boolean getLogState() {
      return this.getBoolean(logState);
   }

   public void setLogState(boolean var1) {
      this.setBoolean(logState, var1, (Context)null);
   }

   public boolean getLogFaults() {
      return this.getBoolean(logFaults);
   }

   public void setLogFaults(boolean var1) {
      this.setBoolean(logFaults, var1, (Context)null);
   }

   public BStatusEnum getLuxNormalisation() {
      return (BStatusEnum)this.get(luxNormalisation);
   }

   public void setLuxNormalisation(BStatusEnum var1) {
      this.set(luxNormalisation, var1, (Context)null);
   }

   public void startSensorIdentify() {
      this.invoke(startSensorIdentify, (BValue)null, (Context)null);
   }

   public void stopSensorIdentify() {
      this.invoke(stopSensorIdentify, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void started() throws Exception {
      super.started();
      this.setFlags(this.getSlot("points"), 4);
      this.setFlags(this.getSlot("configPoints"), 4);
      this.licenceValid = false;
      ++sensor4DeviceCount;
      this.licenceValid = CnsLicense.checkDeviceLicense("elitedaliSensor4", "sensorType4.limit", "SensorType4", sensor4DeviceCount, this);
   }

   public void stopped() throws Exception {
      super.stopped();
      this.unsubscribed();
      this.doStopSensorIdentify();
      --sensor4DeviceCount;
   }

   public void changed(Property var1, Context var2) {
      super.changed(var1, var2);
      if (this.isRunning() && (var1 == enabled || var1 == pollPeriod)) {
         if (this.getEnabled() && this.isSubscribed()) {
            this.startPollTask();
         } else {
            this.stopPollTask();
         }
      }

   }

   public void subscribed() {
      this.startPollTask();
   }

   public void unsubscribed() {
      this.stopPollTask();
   }

   public synchronized void doStartSensorIdentify() {
      if (this.getEnabled() && this.isRunning()) {
         if (this.identifyTask != null && !this.identifyTask.isAlive()) {
            this.identifyTask = null;
         }

         if (this.identifyTask == null) {
            this.identifyTask = new IdentifyTask(this);
            this.identifyTask.setMode(3);
            this.getIdentifyActive().setValue(true);
            this.identifyTask.start();
         }

      }
   }

   public synchronized void doStopSensorIdentify() {
      if (this.identifyTask != null) {
         this.identifyTask.stop();
         this.identifyTask = null;
      }

      this.getIdentifyActive().setValue(false);
   }

   public synchronized void startPollTask() {
      if (this.getPollPeriod().getValue() < (double)MIN_POLL_PERIOD) {
         this.getPollPeriod().setValue((double)MIN_POLL_PERIOD);
      }

      this.stopPollTask();
      if (this.licenceValid && this.getEnabled() && this.isSubscribed() && !this.getPollPeriod().getStatus().isNull()) {
         this.pollTask = new SensorPollTask(this, 160, this.getPollPeriod().getValue());
         this.pollTask.setRetryMax(this.getDeviceRetryMax());
         this.pollTask.start();
      }

   }

   public void stopPollTask() {
      if (this.pollTask != null) {
         this.pollTask.stop();
         this.pollTask = null;
      }

   }

   public synchronized void identifyStopped() {
      this.getIdentifyActive().setValue(false);
   }

   public boolean configure(DaliDeviceConfig var1) {
      return true;
   }

   public BDaliDeviceType getDaliDeviceType() {
      return BDaliDeviceType.sensorType4;
   }

   public BSensorType4() {
     this.licenceValid = false;
     this.pollTask = null;
     this.identifyTask = null;
     
      this.licenceValid = false;
   }

   static {
      occupancy = newProperty(265, new BStatusEnum(), BFacets.makeEnum(BEnumRange.make(BOccupancy.TYPE)));
      occupancyBoolean = newProperty(257, new BStatusBoolean(true), (BFacets)null);
      luxLevel = newProperty(265, new BStatusNumeric(), BFacets.make("units", UnitDatabase.getUnit("lux")));
      pollPeriod = newProperty(256, new BStatusNumeric(1000.0), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), 0, 500.0, 1.0E9));
      identifyActive = newProperty(257, new BStatusBoolean(false), (BFacets)null);
      logState = newProperty(260, false, (BFacets)null);
      logFaults = newProperty(260, false, (BFacets)null);
      luxNormalisation = newProperty(260, new BStatusEnum(BDynamicEnum.make(0), BStatus.nullStatus), (BFacets)null);
      startSensorIdentify = newAction(0, (BFacets)null);
      stopSensorIdentify = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BSensorType4.class);
      sensor4DeviceCount = 0;
      log = Log.getLog("elitedali2.sensor4");
   }

   class SensorPollTask extends PollQueryTask {
      public void processQueryResult(int var1) {
         double var2 = AurexIndustriesUtils.getSensorLux(var1);
         BSensorType4.this.getLuxLevel().setValue(var2);
         boolean var4 = AurexIndustriesUtils.getSensorOccupancy(var1);
         if (var4) {
            BSensorType4.this.getOccupancy().setValue(BOccupancy.occupied);
            BSensorType4.this.getOccupancyBoolean().setValue(true);
         } else {
            BSensorType4.this.getOccupancy().setValue(BOccupancy.unoccupied);
            BSensorType4.this.getOccupancyBoolean().setValue(false);
            BSensorType4.this.pollTask.setPollPeriod((int)BSensorType4.this.getPollPeriod().getValue());
         }

         BSensorType4.this.getLuxLevel().setStatus(BStatus.ok);
         BSensorType4.this.getOccupancy().setStatus(BStatus.ok);
         BSensorType4.this.getOccupancyBoolean().setStatus(BStatus.ok);
         BSensorType4.this.pingOk();
         if (BSensorType4.this.getLogState()) {
            BSensorType4.log.trace("'" + BSensorType4.this.getName() + "': State = " + var2 + '/' + var4);
         }

      }

      public void processQueryFailure(int var1) {
         BSensorType4.this.getLuxLevel().setStatusFault(true);
         BSensorType4.this.getOccupancy().setStatusFault(true);
         BSensorType4.this.getOccupancyBoolean().setStatusFault(true);
         BSensorType4.this.pollTask.setPollPeriod((int)BSensorType4.this.getPollPeriod().getValue());
         if (BSensorType4.this.getLogFaults()) {
            BSensorType4.log.trace("'" + BSensorType4.this.getName() + "': read fault");
         }

      }

      public SensorPollTask(BGenericDaliDevice var2, int var3, double var4) {
         super(var2, var3, (int)var4);
      }
   }
}
