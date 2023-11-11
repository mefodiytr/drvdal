package uk.co.controlnetworksolutions.elitedali2.dali.devices;

import javax.baja.log.Log;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BDouble;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.config.DaliDeviceConfig;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BOccupancy;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliSpecialCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.FifthLightUtils;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.IdentifyTask;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.PollQueryTask;
import uk.co.controlnetworksolutions.elitedali2.utils.CnsLicense;

public class BSensorType3 extends BDaliSensor {
   public static final Property occupancyBoolean = newProperty(257, new BStatusBoolean(true), (BFacets)null);
   public static final Property occupancyEnum;
   public static final Property luxLevel;
   public static final Property temperature;
   public static final Property occupancyPollPeriod;
   public static final Property luxPollPeriod;
   public static final Property temperaturePollPeriod;
   public static final Property identifyActive;
   public static final Property timeout;
   public static final Property sensitivity;
   public static final Property luxNormalisation;
   public static final Action startSensorIdentify;
   public static final Action stopSensorIdentify;
   public static final Action reprogram;
   public static final Type TYPE;
   private static int sensor3DeviceCount;
   private static final String SENSOR_TYPE_3_LICENSE_FEATURE = "elitedaliSensor3";
   private static final String SENSOR_TYPE_3_LICENSE_LIMIT = "sensorType3.limit";
   private static final String SENSOR_TYPE_3_NAME = "SensorType3";
   private static final Log log;
   private boolean licenceValid;
   private SensorOccupancyPollTask occupancyPollTask;
   private SensorLuxPollTask luxPollTask;
   private SensorTemperaturePollTask temperaturePollTask;
   private IdentifyTask identifyTask;

   public BStatusBoolean getOccupancyBoolean() {
      return (BStatusBoolean)this.get(occupancyBoolean);
   }

   public void setOccupancyBoolean(BStatusBoolean var1) {
      this.set(occupancyBoolean, var1, (Context)null);
   }

   public BStatusEnum getOccupancyEnum() {
      return (BStatusEnum)this.get(occupancyEnum);
   }

   public void setOccupancyEnum(BStatusEnum var1) {
      this.set(occupancyEnum, var1, (Context)null);
   }

   public BStatusNumeric getLuxLevel() {
      return (BStatusNumeric)this.get(luxLevel);
   }

   public void setLuxLevel(BStatusNumeric var1) {
      this.set(luxLevel, var1, (Context)null);
   }

   public BStatusNumeric getTemperature() {
      return (BStatusNumeric)this.get(temperature);
   }

   public void setTemperature(BStatusNumeric var1) {
      this.set(temperature, var1, (Context)null);
   }

   public BStatusNumeric getOccupancyPollPeriod() {
      return (BStatusNumeric)this.get(occupancyPollPeriod);
   }

   public void setOccupancyPollPeriod(BStatusNumeric var1) {
      this.set(occupancyPollPeriod, var1, (Context)null);
   }

   public BStatusNumeric getLuxPollPeriod() {
      return (BStatusNumeric)this.get(luxPollPeriod);
   }

   public void setLuxPollPeriod(BStatusNumeric var1) {
      this.set(luxPollPeriod, var1, (Context)null);
   }

   public BStatusNumeric getTemperaturePollPeriod() {
      return (BStatusNumeric)this.get(temperaturePollPeriod);
   }

   public void setTemperaturePollPeriod(BStatusNumeric var1) {
      this.set(temperaturePollPeriod, var1, (Context)null);
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

   public Type getType() {
      return TYPE;
   }

   public void started() throws Exception {
      super.started();
      this.setFlags(this.getSlot("points"), 4);
      this.setFlags(this.getSlot("configPoints"), 4);
      this.licenceValid = false;
      ++sensor3DeviceCount;
      this.licenceValid = CnsLicense.checkDeviceLicense("elitedaliSensor3", "sensorType3.limit", "SensorType3", sensor3DeviceCount, this);
   }

   public void stopped() throws Exception {
      super.stopped();
      this.unsubscribed();
      this.doStopSensorIdentify();
      --sensor3DeviceCount;
   }

   public void changed(Property var1, Context var2) {
      super.changed(var1, var2);
      if (this.isRunning()) {
         if (var1 == enabled) {
            if (this.getEnabled() && this.isSubscribed()) {
               this.startOccupancyPollTask();
               this.startLuxPollTask();
               this.startTemperaturePollTask();
            } else {
               this.stopOccupancyPollTask();
               this.stopLuxPollTask();
               this.stopTemperaturePollTask();
            }
         } else if (var1 == occupancyPollPeriod) {
            if (this.getEnabled() && this.isSubscribed()) {
               this.startOccupancyPollTask();
            } else {
               this.stopOccupancyPollTask();
            }
         } else if (var1 == luxPollPeriod) {
            if (this.getEnabled() && this.isSubscribed()) {
               this.startLuxPollTask();
            } else {
               this.stopLuxPollTask();
            }
         } else if (var1 == temperaturePollPeriod) {
            if (this.getEnabled() && this.isSubscribed()) {
               this.startTemperaturePollTask();
            } else {
               this.stopTemperaturePollTask();
            }
         } else if (var1 == timeout) {
            if (!this.getTimeout().getStatus().isNull()) {
               this.configureTimeout(FifthLightUtils.convertHoldingTime(this.getTimeout().getValue()));
            }
         } else if (var1 == sensitivity && !this.getSensitivity().getStatus().isNull()) {
            this.configureSensitivity((int)this.getSensitivity().getValue());
         }
      }

   }

   public void subscribed() {
      this.startOccupancyPollTask();
      this.startLuxPollTask();
      this.startTemperaturePollTask();
      log.trace("'" + this.getName() + "' subscribed");
   }

   public void unsubscribed() {
      this.stopOccupancyPollTask();
      this.stopLuxPollTask();
      this.stopTemperaturePollTask();
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

   public synchronized void startOccupancyPollTask() {
      if (this.getOccupancyPollPeriod().getValue() < (double)MIN_POLL_PERIOD) {
         this.getOccupancyPollPeriod().setValue((double)MIN_POLL_PERIOD);
      }

      this.stopOccupancyPollTask();
      if (this.licenceValid && this.getEnabled() && this.isSubscribed() && !this.getOccupancyPollPeriod().getStatus().isNull()) {
         this.occupancyPollTask = new SensorOccupancyPollTask(this, 191, this.getOccupancyPollPeriod().getValue());
         this.occupancyPollTask.start();
      }

   }

   public synchronized void stopOccupancyPollTask() {
      if (this.occupancyPollTask != null) {
         this.occupancyPollTask.stop();
         this.occupancyPollTask = null;
      }

   }

   public synchronized void startLuxPollTask() {
      if (this.getLuxPollPeriod().getValue() < (double)MIN_POLL_PERIOD) {
         this.getLuxPollPeriod().setValue((double)MIN_POLL_PERIOD);
      }

      this.stopLuxPollTask();
      if (this.licenceValid && this.getEnabled() && this.isSubscribed() && !this.getLuxPollPeriod().getStatus().isNull()) {
         this.luxPollTask = new SensorLuxPollTask(this, 189, this.getLuxPollPeriod().getValue());
         this.luxPollTask.start();
      }

   }

   public synchronized void stopLuxPollTask() {
      if (this.luxPollTask != null) {
         this.luxPollTask.stop();
         this.luxPollTask = null;
      }

   }

   public synchronized void startTemperaturePollTask() {
      if (this.getTemperaturePollPeriod().getValue() < (double)MIN_POLL_PERIOD) {
         this.getTemperaturePollPeriod().setValue((double)MIN_POLL_PERIOD);
      }

      this.stopTemperaturePollTask();
      if (this.licenceValid && this.getEnabled() && this.isSubscribed() && !this.getTemperaturePollPeriod().getStatus().isNull()) {
         this.temperaturePollTask = new SensorTemperaturePollTask(this, 184, this.getTemperaturePollPeriod().getValue());
         this.temperaturePollTask.start();
      }

   }

   public synchronized void stopTemperaturePollTask() {
      if (this.temperaturePollTask != null) {
         this.temperaturePollTask.stop();
         this.temperaturePollTask = null;
      }

   }

   public boolean configure(DaliDeviceConfig var1) {
      var1.setDaliDeviceType(100);
      int var2;
      if (!this.getTimeout().getStatus().isNull()) {
         var2 = FifthLightUtils.convertHoldingTime(this.getTimeout().getValue());
         if (var2 < 0) {
            var2 = 0;
         }

         if (var2 > 255) {
            var2 = 255;
         }

         this.sendConfigEnableSequence();
         var1.configureValue(181, 69, var2);
      }

      if (!this.getSensitivity().getStatus().isNull()) {
         var2 = (int)this.getSensitivity().getValue();
         if (var2 < 0) {
            var2 = 0;
         }

         if (var2 > 255) {
            var2 = 255;
         }

         this.sendConfigEnableSequence();
         var1.configureValue(187, 75, var2);
      }

      return true;
   }

   public void configureTimeout(int var1) {
      if (this.getEnabled() && this.isRunning()) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var1 > 255) {
            var1 = 255;
         }

         log.trace("'" + this.getName() + "' set config timeout = " + var1);
         DaliDeviceConfig var2 = new DaliDeviceConfig(this);
         this.sendConfigEnableSequence();
         var2.configureValue(181, 69, var1);
      }
   }

   public void configureSensitivity(int var1) {
      if (this.getEnabled() && this.isRunning()) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var1 > 255) {
            var1 = 255;
         }

         log.trace("'" + this.getName() + "' set config sensitivity = " + var1);
         DaliDeviceConfig var2 = new DaliDeviceConfig(this);
         this.sendConfigEnableSequence();
         var2.configureValue(187, 75, var1);
      }
   }

   public void sendConfigEnableSequence() {
      DaliSpecialCommand var1 = new DaliSpecialCommand(this.getDaliNetwork(), 163, 170);
      var1.setTransmitRepeatCount(1);
      var1.execute();
      DaliCommand var2 = new DaliCommand(this.getDaliNetwork(), false, this.getDaliAddress(), 74);
      var2.setTransmitRepeatCount(1);
      var2.execute();
      var1 = new DaliSpecialCommand(this.getDaliNetwork(), 163, 0);
      var1.setTransmitRepeatCount(1);
      var1.execute();
      var2 = new DaliCommand(this.getDaliNetwork(), false, this.getDaliAddress(), 74);
      var2.setTransmitRepeatCount(1);
      var2.execute();
      var1 = new DaliSpecialCommand(this.getDaliNetwork(), 163, 100);
      var1.setTransmitRepeatCount(1);
      var1.execute();
      var2 = new DaliCommand(this.getDaliNetwork(), false, this.getDaliAddress(), 74);
      var2.setTransmitRepeatCount(1);
      var2.execute();
      var1 = new DaliSpecialCommand(this.getDaliNetwork(), 163, 50);
      var1.setTransmitRepeatCount(1);
      var1.execute();
      var2 = new DaliCommand(this.getDaliNetwork(), false, this.getDaliAddress(), 74);
      var2.setTransmitRepeatCount(1);
      var2.execute();
   }

   public synchronized void identifyStopped() {
      this.getIdentifyActive().setValue(false);
   }

   public BDaliDeviceType getDaliDeviceType() {
      return BDaliDeviceType.sensorType3;
   }

   public BSensorType3() {
     this.licenceValid = false;
     this.occupancyPollTask = null;
     this.luxPollTask = null;
     this.temperaturePollTask = null;
     this.identifyTask = null;
     
      this.licenceValid = false;
   }

   static {
      occupancyEnum = newProperty(265, new BStatusEnum(BOccupancy.occupied), BFacets.makeEnum(BEnumRange.make(BOccupancy.TYPE)));
      luxLevel = newProperty(265, new BStatusNumeric(), BFacets.make("units", UnitDatabase.getUnit("lux")));
      temperature = newProperty(257, new BStatusNumeric(), BFacets.make("units", UnitDatabase.getUnit("celsius")));
      occupancyPollPeriod = newProperty(256, new BStatusNumeric(1000.0), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(500.0), BDouble.make(1.0E9)));
      luxPollPeriod = newProperty(256, new BStatusNumeric(30000.0), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(500.0), BDouble.make(1.0E9)));
      temperaturePollPeriod = newProperty(256, new BStatusNumeric(60000.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(500.0), BDouble.make(1.0E9)));
      identifyActive = newProperty(257, new BStatusBoolean(false), (BFacets)null);
      timeout = newProperty(256, new BStatusNumeric(60.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("second"), BInteger.make(1), BDouble.make(0.0), BDouble.make(1200.0)));
      sensitivity = newProperty(256, new BStatusNumeric(30.0, BStatus.nullStatus), BFacets.makeInt(0, 255));
      luxNormalisation = newProperty(260, new BStatusEnum(BDynamicEnum.make(0), BStatus.nullStatus), (BFacets)null);
      startSensorIdentify = newAction(0, (BFacets)null);
      stopSensorIdentify = newAction(0, (BFacets)null);
      reprogram = newAction(128, (BFacets)null);
      TYPE = Sys.loadType(BSensorType3.class);
      sensor3DeviceCount = 0;
      log = Log.getLog("elitedali2.sensor3");
   }

   class SensorOccupancyPollTask extends PollQueryTask {
      public void processQueryResult(int var1) {
         boolean var2 = FifthLightUtils.getSensorOccupancy(var1);
         if (var2) {
            BSensorType3.this.getOccupancyBoolean().setValue(true);
            BSensorType3.this.getOccupancyEnum().setValue(BOccupancy.occupied);
         } else {
            BSensorType3.this.getOccupancyBoolean().setValue(false);
            BSensorType3.this.getOccupancyEnum().setValue(BOccupancy.unoccupied);
         }

         BSensorType3.this.getOccupancyBoolean().setStatus(BStatus.ok);
         BSensorType3.this.getOccupancyEnum().setStatus(BStatus.ok);
         BSensorType3.this.pingOk();
         BSensorType3.log.trace("'" + BSensorType3.this.getName() + "' occupancy = " + var2);
      }

      public void processQueryFailure(int var1) {
         BSensorType3.this.getOccupancyBoolean().setStatusFault(true);
         BSensorType3.this.getOccupancyEnum().setStatusFault(true);
         BSensorType3.log.trace("'" + BSensorType3.this.getName() + "' occupancy read fault");
      }

      public SensorOccupancyPollTask(BGenericDaliDevice var2, int var3, double var4) {
         super(var2, var3, (int)var4);
      }
   }

   class SensorLuxPollTask extends PollQueryTask {
      public void processQueryResult(int var1) {
         double var2 = FifthLightUtils.getSensorLuxHigh(var1);
         BSensorType3.this.getLuxLevel().setValue(var2);
         BSensorType3.this.getLuxLevel().setStatus(BStatus.ok);
         BSensorType3.this.pingOk();
         BSensorType3.log.trace("'" + BSensorType3.this.getName() + "' Lux = " + var2);
      }

      public void processQueryFailure(int var1) {
         BSensorType3.this.getLuxLevel().setStatusFault(true);
         BSensorType3.log.trace("'" + BSensorType3.this.getName() + "' Lux read fault");
      }

      public SensorLuxPollTask(BGenericDaliDevice var2, int var3, double var4) {
         super(var2, var3, (int)var4);
      }
   }

   class SensorTemperaturePollTask extends PollQueryTask {
      public void processQueryResult(int var1) {
         double var2 = FifthLightUtils.getSensorTemperature(var1);
         BSensorType3.this.getTemperature().setValue(var2);
         BSensorType3.this.getTemperature().setStatus(BStatus.ok);
         BSensorType3.this.pingOk();
         BSensorType3.log.trace("'" + BSensorType3.this.getName() + "' Temperature = " + var2);
      }

      public void processQueryFailure(int var1) {
         BSensorType3.this.getTemperature().setStatusFault(true);
         BSensorType3.log.trace("'" + BSensorType3.this.getName() + "' Temperature read fault");
      }

      public SensorTemperaturePollTask(BGenericDaliDevice var2, int var3, double var4) {
         super(var2, var3, (int)var4);
      }
   }
}
