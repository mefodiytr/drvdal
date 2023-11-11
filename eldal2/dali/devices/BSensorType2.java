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
import javax.baja.units.BUnit;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.config.DaliDeviceConfig;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BOccupancy;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliStatus;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.CPElectronicsUtils;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.IdentifyTask;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.PollQueryTask;
import uk.co.controlnetworksolutions.elitedali2.utils.CnsLicense;

public class BSensorType2 extends BDaliSensor {
   public static final Property occupancy;
   public static final Property occupancyBoolean;
   public static final Property luxLevel;
   public static final Property pollPeriod;
   public static final Property occupiedPollPeriod;
   public static final Property identifyActive;
   public static final Property timeout;
   public static final Property sensitivity;
   public static final Property occupancyIndicator;
   public static final Property powerUpDelay;
   public static final Property configFlags;
   public static final Property luxNormalisation;
   public static final Action startSensorIdentify;
   public static final Action stopSensorIdentify;
   public static final Action reprogram;
   public static final Action readConfig;
   public static final Type TYPE;
   private static int sensor2DeviceCount;
   private static final String SENSOR_TYPE_2_LICENSE_FEATURE = "elitedali2Sensor2";
   private static final String SENSOR_TYPE_2_LICENSE_LIMIT = "sensorType2.limit";
   private static final String SENSOR_TYPE_2_NAME = "SensorType2";
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

   public BStatusNumeric getOccupiedPollPeriod() {
      return (BStatusNumeric)this.get(occupiedPollPeriod);
   }

   public void setOccupiedPollPeriod(BStatusNumeric var1) {
      this.set(occupiedPollPeriod, var1, (Context)null);
   }

   public BStatusBoolean getIdentifyActive() {
      return (BStatusBoolean)this.get(identifyActive);
   }

   public void setIdentifyActive(BStatusBoolean var1) {
      this.set(identifyActive, var1, (Context)null);
   }

   public BStatusNumeric getTimeout() {
      return (BStatusNumeric)this.get(timeout);
   }

   public void setTimeout(BStatusNumeric var1) {
      this.set(timeout, var1, (Context)null);
   }

   public BStatusNumeric getSensitivity() {
      return (BStatusNumeric)this.get(sensitivity);
   }

   public void setSensitivity(BStatusNumeric var1) {
      this.set(sensitivity, var1, (Context)null);
   }

   public BStatusBoolean getOccupancyIndicator() {
      return (BStatusBoolean)this.get(occupancyIndicator);
   }

   public void setOccupancyIndicator(BStatusBoolean var1) {
      this.set(occupancyIndicator, var1, (Context)null);
   }

   public BStatusBoolean getPowerUpDelay() {
      return (BStatusBoolean)this.get(powerUpDelay);
   }

   public void setPowerUpDelay(BStatusBoolean var1) {
      this.set(powerUpDelay, var1, (Context)null);
   }

   public BStatusNumeric getConfigFlags() {
      return (BStatusNumeric)this.get(configFlags);
   }

   public void setConfigFlags(BStatusNumeric var1) {
      this.set(configFlags, var1, (Context)null);
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

   public void reprogram() {
      this.invoke(reprogram, (BValue)null, (Context)null);
   }

   public void readConfig() {
      this.invoke(readConfig, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void started() throws Exception {
      super.started();
      this.setFlags(this.getSlot("points"), 4);
      this.setFlags(this.getSlot("configPoints"), 4);
      this.licenceValid = false;
      ++sensor2DeviceCount;
      this.licenceValid = CnsLicense.checkDeviceLicense("elitedali2Sensor2", "sensorType2.limit", "SensorType2", sensor2DeviceCount, this);
   }

   public void stopped() throws Exception {
      super.stopped();
      this.unsubscribed();
      this.doStopSensorIdentify();
      --sensor2DeviceCount;
   }

   public void changed(Property var1, Context var2) {
      super.changed(var1, var2);
      if (this.isRunning()) {
         if (var1 != enabled && var1 != pollPeriod) {
            if (var1 == timeout) {
               if (!this.getTimeout().getStatus().isNull()) {
                  this.configureTimeout((int)(this.getTimeout().getValue() * 10.0));
               }
            } else if (var1 == sensitivity) {
               if (!this.getSensitivity().getStatus().isNull()) {
                  this.configureSensitivity((int)this.getSensitivity().getValue());
               }
            } else if (var1 == occupancyIndicator) {
               if (!this.getOccupancyIndicator().getStatus().isNull()) {
                  this.configureFlags(this.getOccupancyIndicator().getValue(), this.getPowerUpDelay().getValue());
               }
            } else if (var1 == powerUpDelay && !this.getPowerUpDelay().getStatus().isNull()) {
               this.configureFlags(this.getOccupancyIndicator().getValue(), this.getPowerUpDelay().getValue());
            }
         } else if (this.getEnabled() && this.isSubscribed()) {
            this.startPollTask();
         } else {
            this.stopPollTask();
         }
      }

   }

   public void subscribed() {
      this.startPollTask();
      log.trace("'" + this.getName() + "' subscribed");
   }

   public void unsubscribed() {
      this.stopPollTask();
      log.trace("'" + this.getName() + "' unsubscribed");
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

   public void doReprogram() {
      if (this.isDeviceOk()) {
         DaliDeviceConfig var1 = new DaliDeviceConfig(this);
         this.configure(var1);
      }

   }

   public void doReadConfig() {
      try {
         if (!this.getEnabled() || !this.isRunning()) {
            return;
         }

         DaliQuery var1 = new DaliQuery(this, 241);
         var1.confirmResult();
         int var2 = var1.getResult();
         if (var1.getDaliStatus() == 53 && var2 >= 0) {
            log.trace("'" + this.getName() + "' config timeout query = " + var2);
            this.getTimeout().setValue((double)var2 / 10.0);
            this.getTimeout().setStatus(BStatus.ok);
         } else {
            this.getTimeout().setStatusFault(true);
            log.trace("'" + this.getName() + "' config timeout query failed: " + DaliStatus.toString(var1.getDaliStatus()) + " [" + var1.getDaliStatus() + ']');
         }

         var1 = new DaliQuery(this, 242);
         var1.confirmResult();
         var2 = var1.getResult();
         if (var1.getDaliStatus() == 53 && var2 >= 0) {
            log.trace("'" + this.getName() + "' config sensitivity query = " + var2);
            this.getSensitivity().setValue((double)var2);
            this.getSensitivity().setStatus(BStatus.ok);
         } else {
            this.getSensitivity().setStatusFault(true);
            log.trace("'" + this.getName() + "' config sensitivity query failed: " + DaliStatus.toString(var1.getDaliStatus()) + " [" + var1.getDaliStatus() + ']');
         }

         var1 = new DaliQuery(this, 243);
         var1.confirmResult();
         var2 = var1.getResult();
         if (var1.getDaliStatus() == 53 && var2 >= 0) {
            log.trace("'" + this.getName() + "' config flags query = " + var2);
            BStatusBoolean var10000 = this.getOccupancyIndicator();
            boolean var10001 = false;
            if ((var2 & 1) != 0) {
               var10001 = true;
            }

            var10000.setValue(var10001);
            this.getOccupancyIndicator().setStatus(BStatus.ok);
            var10000 = this.getPowerUpDelay();
            var10001 = false;
            if ((var2 & 2) == 0) {
               var10001 = true;
            }

            var10000.setValue(var10001);
            this.getPowerUpDelay().setStatus(BStatus.ok);
            this.getConfigFlags().setValue((double)var2);
            this.getConfigFlags().setStatus(BStatus.ok);
         } else {
            this.getConfigFlags().setStatusFault(true);
            log.trace("'" + this.getName() + "' config flags query failed: " + DaliStatus.toString(var1.getDaliStatus()) + " [" + var1.getDaliStatus() + ']');
         }
      } catch (Exception var4) {
         log.warning("Config exception: " + var4);
      }

   }

   public synchronized void startPollTask() {
      if (this.getPollPeriod().getValue() < (double)MIN_POLL_PERIOD) {
         this.getPollPeriod().setValue((double)MIN_POLL_PERIOD);
      }

      if (this.getOccupiedPollPeriod().getValue() < (double)MIN_POLL_PERIOD) {
         this.getOccupiedPollPeriod().setValue((double)MIN_POLL_PERIOD);
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

   public boolean configure(DaliDeviceConfig var1) {
      int var2 = 0;
      if (this.getOccupancyIndicator().getValue()) {
         var2 |= 1;
      }

      if (!this.getPowerUpDelay().getValue()) {
         var2 |= 2;
      }

      var1.setDaliDeviceType(128);
      var1.configureValue(243, 254, var2);
      if (!this.getTimeout().getStatus().isNull()) {
         var1.configureValue(241, 252, (int)(this.getTimeout().getValue() * 10.0));
      }

      if (!this.getSensitivity().getStatus().isNull() && this.getSensitivity().getValue() >= 1.0 && this.getSensitivity().getValue() <= 9.0) {
         var1.configureValue(242, 253, (int)this.getSensitivity().getValue());
      }

      return true;
   }

   public void configureTimeout(int var1) {
      if (this.getEnabled() && this.isRunning()) {
         log.trace("'" + this.getName() + "' set config timeout = " + var1);
         DaliDeviceConfig var2 = new DaliDeviceConfig(this);
         var2.configureValue(241, 252, var1);
      }
   }

   public void configureSensitivity(int var1) {
      if (this.getEnabled() && this.isRunning() && var1 >= 1 && var1 <= 9) {
         log.trace("'" + this.getName() + "' set config sensitivity = " + var1);
         DaliDeviceConfig var2 = new DaliDeviceConfig(this);
         var2.configureValue(242, 253, var1);
      }
   }

   public void configureFlags(boolean var1, boolean var2) {
      if (this.getEnabled() && this.isRunning()) {
         int var3 = 0;
         if (var1) {
            var3 |= 1;
         }

         if (!var2) {
            var3 |= 2;
         }

         DaliDeviceConfig var4 = new DaliDeviceConfig(this);
         var4.configureValue(243, 254, var3);
      }
   }

   public synchronized void identifyStopped() {
      this.getIdentifyActive().setValue(false);
   }

   public BDaliDeviceType getDaliDeviceType() {
      return BDaliDeviceType.sensorType2;
   }

   public BSensorType2() {
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
      occupiedPollPeriod = newProperty(256, new BStatusNumeric(60000.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), 0, 500.0, 1.0E9));
      identifyActive = newProperty(257, new BStatusBoolean(false), (BFacets)null);
      timeout = newProperty(256, new BStatusNumeric(1.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("second"), 1, 0.0, 25.0));
      sensitivity = newProperty(256, new BStatusNumeric(8.0, BStatus.nullStatus), BFacets.makeNumeric((BUnit)null, 0, 1.0, 9.0));
      occupancyIndicator = newProperty(256, new BStatusBoolean(false, BStatus.nullStatus), (BFacets)null);
      powerUpDelay = newProperty(256, new BStatusBoolean(false, BStatus.nullStatus), (BFacets)null);
      configFlags = newProperty(261, new BStatusNumeric((double)2, BStatus.nullStatus), (BFacets)null);
      luxNormalisation = newProperty(260, new BStatusEnum(BDynamicEnum.make(0), BStatus.nullStatus), (BFacets)null);
      startSensorIdentify = newAction(0, (BFacets)null);
      stopSensorIdentify = newAction(0, (BFacets)null);
      reprogram = newAction(128, (BFacets)null);
      readConfig = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BSensorType2.class);
      sensor2DeviceCount = 0;
      log = Log.getLog("elitedali2.sensor2");
   }

   class SensorPollTask extends PollQueryTask {
      public void processQueryResult(int var1) {
         double var2 = CPElectronicsUtils.getSensorLux(var1);
         BSensorType2.this.getLuxLevel().setValue(var2);
         boolean var4 = CPElectronicsUtils.getSensorOccupancy(var1);
         if (var4) {
            BSensorType2.this.getOccupancy().setValue(BOccupancy.occupied);
            BSensorType2.this.getOccupancyBoolean().setValue(true);
            if (!BSensorType2.this.getOccupiedPollPeriod().getStatus().isNull()) {
               BSensorType2.this.pollTask.setPollPeriod((int)BSensorType2.this.getOccupiedPollPeriod().getValue());
            }
         } else {
            BSensorType2.this.getOccupancy().setValue(BOccupancy.unoccupied);
            BSensorType2.this.getOccupancyBoolean().setValue(false);
            BSensorType2.this.pollTask.setPollPeriod((int)BSensorType2.this.getPollPeriod().getValue());
         }

         BSensorType2.this.getLuxLevel().setStatus(BStatus.ok);
         BSensorType2.this.getOccupancy().setStatus(BStatus.ok);
         BSensorType2.this.getOccupancyBoolean().setStatus(BStatus.ok);
         BSensorType2.this.pingOk();
         BSensorType2.log.trace("'" + BSensorType2.this.getName() + "' update = " + var2 + '/' + var4);
      }

      public void processQueryFailure(int var1) {
         BSensorType2.this.getLuxLevel().setStatusFault(true);
         BSensorType2.this.getOccupancy().setStatusFault(true);
         BSensorType2.this.getOccupancyBoolean().setStatusFault(true);
         BSensorType2.this.pollTask.setPollPeriod((int)BSensorType2.this.getPollPeriod().getValue());
         BSensorType2.log.trace("'" + BSensorType2.this.getName() + "' read fault");
      }

      public SensorPollTask(BGenericDaliDevice var2, int var3, double var4) {
         super(var2, var3, (int)var4);
      }
   }
}
