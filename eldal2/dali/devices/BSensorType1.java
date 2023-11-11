package uk.co.controlnetworksolutions.elitedali2.dali.devices;

import javax.baja.log.Log;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BDouble;
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
import uk.co.controlnetworksolutions.elitedali2.dali.utils.PollQueryTask;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.TridonicUtils;

public class BSensorType1 extends BDaliSensor {
   public static final Property occupancy;
   public static final Property luxLevel;
   public static final Property pollPeriod;
   public static final Property identifyActive;
   public static final Action startSensorIdentify;
   public static final Action stopSensorIdentify;
   public static final Action update;
   public static final Type TYPE;
   private static final Log log;
   private SensorPollTask pollTask;

   public BStatusEnum getOccupancy() {
      return (BStatusEnum)this.get(occupancy);
   }

   public void setOccupancy(BStatusEnum var1) {
      this.set(occupancy, var1, (Context)null);
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

   public void startSensorIdentify() {
      this.invoke(startSensorIdentify, (BValue)null, (Context)null);
   }

   public void stopSensorIdentify() {
      this.invoke(stopSensorIdentify, (BValue)null, (Context)null);
   }

   public void update() {
      this.invoke(update, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void started() throws Exception {
      super.started();
      this.setFlags(this.getSlot("points"), 4);
      this.setFlags(this.getSlot("configPoints"), 4);
   }

   public void stopped() throws Exception {
      super.stopped();
      this.unsubscribed();
      this.doStopSensorIdentify();
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
      log.trace("'" + this.getName() + "' subscribed");
   }

   public void unsubscribed() {
      this.stopPollTask();
      log.trace("'" + this.getName() + "' unsubscribed");
   }

   public void doStartSensorIdentify() {
      this.daliCommand(5);
      this.getIdentifyActive().setValue(true);
   }

   public void doStopSensorIdentify() {
      this.daliCommand(6);
      this.getIdentifyActive().setValue(false);
   }

   public synchronized void startPollTask() {
      if (this.getPollPeriod().getValue() < (double)MIN_POLL_PERIOD) {
         this.getPollPeriod().setValue((double)MIN_POLL_PERIOD);
      }

      this.stopPollTask();
      if (this.getEnabled() && this.isSubscribed() && !this.getPollPeriod().getStatus().isNull()) {
         this.pollTask = new SensorPollTask(this, 160, this.getPollPeriod().getValue());
         this.pollTask.start();
      }

   }

   public synchronized void stopPollTask() {
      if (this.pollTask != null) {
         this.pollTask.stop();
         this.pollTask = null;
      }

   }

   public boolean configure(DaliDeviceConfig var1) {
      return true;
   }

   public BDaliDeviceType getDaliDeviceType() {
      return BDaliDeviceType.sensorType1;
   }

   public void doUpdate() {
   }

   public BSensorType1() {
     this.pollTask = null;
   }

   static {
      occupancy = newProperty(265, new BStatusEnum(), BFacets.makeEnum(BEnumRange.make(BOccupancy.TYPE)));
      luxLevel = newProperty(265, new BStatusNumeric(), BFacets.make("units", UnitDatabase.getUnit("lux")));
      pollPeriod = newProperty(256, new BStatusNumeric(1000.0), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(500.0), BDouble.make(1.0E9)));
      identifyActive = newProperty(257, new BStatusBoolean(false), (BFacets)null);
      startSensorIdentify = newAction(0, (BFacets)null);
      stopSensorIdentify = newAction(0, (BFacets)null);
      update = newAction(4, (BFacets)null);
      TYPE = Sys.loadType(BSensorType1.class);
      log = Log.getLog("elitedali2.sensor1");
   }

   class SensorPollTask extends PollQueryTask {
      public void processQueryResult(int var1) {
         double var2 = TridonicUtils.getSensorLux(var1);
         BSensorType1.this.getLuxLevel().setValue(var2);
         boolean var4 = TridonicUtils.getSensorOccupancy(var1);
         if (var4) {
            BSensorType1.this.getOccupancy().setValue(BOccupancy.occupied);
         } else {
            BSensorType1.this.getOccupancy().setValue(BOccupancy.unoccupied);
         }

         BSensorType1.this.getLuxLevel().setStatus(BStatus.ok);
         BSensorType1.this.getOccupancy().setStatus(BStatus.ok);
         BSensorType1.this.pingOk();
         BSensorType1.log.trace("'" + BSensorType1.this.getName() + "' update = " + var2 + '/' + var4);
      }

      public void processQueryFailure(int var1) {
         BSensorType1.this.getLuxLevel().setStatusFault(true);
         BSensorType1.this.getOccupancy().setStatusFault(true);
         BSensorType1.log.trace("'" + BSensorType1.this.getName() + "' read fault");
      }

      public SensorPollTask(BGenericDaliDevice var2, int var3, double var4) {
         super(var2, var3, (int)var4);
      }
   }
}
