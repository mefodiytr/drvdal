package uk.co.controlnetworksolutions.elitedali2.dali.point.custom;

import javax.baja.log.Log;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BAbsTime;
import javax.baja.sys.BComplex;
import javax.baja.sys.BComponent;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.BRelTime;
import javax.baja.sys.BValue;
import javax.baja.sys.Clock;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BTridonicSensor;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BOccupancy;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliStatus;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.TridonicUtils;

public class BTridonicSensorExt extends BComponent {
   public static final Property enabled = newProperty(0, true, (BFacets)null);
   public static final Property luxLevel = newProperty(265, new BStatusNumeric(), BFacets.make("units", UnitDatabase.getUnit("lux")));
   public static final Property occupancy;
   public static final Property history;
   public static final Property pollPeriod;
   public static final Action update;
   public static final Type TYPE;
   private static final Log log;
   private double oldLuxLevel;
   private boolean oldOccupancyState;
   private BTridonicSensor daliDevice;
   private Clock.Ticket pollTimer;

   public boolean getEnabled() {
      return this.getBoolean(enabled);
   }

   public void setEnabled(boolean var1) {
      this.setBoolean(enabled, var1, (Context)null);
   }

   public BStatusNumeric getLuxLevel() {
      return (BStatusNumeric)this.get(luxLevel);
   }

   public void setLuxLevel(BStatusNumeric var1) {
      this.set(luxLevel, var1, (Context)null);
   }

   public BStatusEnum getOccupancy() {
      return (BStatusEnum)this.get(occupancy);
   }

   public void setOccupancy(BStatusEnum var1) {
      this.set(occupancy, var1, (Context)null);
   }

   public BTridonicSensorHistory getHistory() {
      return (BTridonicSensorHistory)this.get(history);
   }

   public void setHistory(BTridonicSensorHistory var1) {
      this.set(history, var1, (Context)null);
   }

   public int getPollPeriod() {
      return this.getInt(pollPeriod);
   }

   public void setPollPeriod(int var1) {
      this.setInt(pollPeriod, var1, (Context)null);
   }

   public void update() {
      this.invoke(update, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void started() throws Exception {
      super.started();
      BComplex var1 = this.getParent();
      if (var1 instanceof BTridonicSensor) {
         this.daliDevice = (BTridonicSensor)var1;
      } else {
         this.setEnabled(false);
         this.getLuxLevel().setStatusFault(true);
         this.getOccupancy().setStatusFault(true);
         throw new RuntimeException("TridonicSensorExt can only be added to a Tridonic Sensor device");
      }
   }

   public void stopped() throws Exception {
      super.stopped();
      this.unsubscribed();
   }

   public void changed(Property var1, Context var2) {
      if (this.isRunning()) {
         if (var1 == enabled) {
            if (this.getEnabled() && this.isSubscribed()) {
               this.startPollTimer();
            } else {
               this.stopPollTimer();
            }
         } else if (var1 == pollPeriod) {
            this.startPollTimer();
         }
      }

   }

   public void subscribed() {
      this.startPollTimer();
      log.trace("Sensor '" + this.getName() + "' subscribed");
   }

   public void unsubscribed() {
      this.stopPollTimer();
      log.trace("Sensor '" + this.getName() + "' unsubscribed");
   }

   public void startPollTimer() {
      this.stopPollTimer();
      if (this.getPollPeriod() < 500) {
         this.setPollPeriod(500);
      }

      if (this.getEnabled() && this.daliDevice != null) {
         this.pollTimer = Clock.schedulePeriodically(this, BRelTime.make((long)this.getPollPeriod()), update, (BValue)null);
      }

   }

   public void stopPollTimer() {
      if (this.pollTimer != null) {
         this.pollTimer.cancel();
         this.pollTimer = null;
      }

   }

   public void doUpdate() {
      try {
         if (!this.getEnabled() || !this.isRunning()) {
            return;
         }

         boolean var1 = false;
         double var4 = 0.0;
         boolean var6 = false;
         if (this.daliDevice != null) {
            DaliQuery var2 = new DaliQuery(this.daliDevice, 160);
            var2.confirmResult();
            if (var2.getDaliStatus() == 53) {
               int var3 = var2.getResult();
               if (var3 >= 0) {
                  var4 = TridonicUtils.getSensorLux(var3);
                  this.getLuxLevel().setValue(var4);
                  if (var4 != this.oldLuxLevel) {
                     this.getHistory().setLastLuxChangeTime(BAbsTime.make());
                     this.oldLuxLevel = var4;
                  }

                  var6 = TridonicUtils.getSensorOccupancy(var3);
                  if (var6) {
                     this.getOccupancy().setValue(BOccupancy.occupied);
                     this.getHistory().setLastOccupiedTime(BAbsTime.make());
                  } else {
                     this.getOccupancy().setValue(BOccupancy.unoccupied);
                     this.getHistory().setLastUnoccupiedTime(BAbsTime.make());
                  }

                  if (var6 != this.oldOccupancyState) {
                     this.getHistory().setLastOccupancyChangeTime(BAbsTime.make());
                     this.oldOccupancyState = var6;
                  }

                  this.daliDevice.pingOk();
                  var1 = true;
               }
            } else {
               log.trace("TridonicSensorExt update query failed: " + DaliStatus.toString(var2.getDaliStatus()) + " [" + var2.getDaliStatus() + ']');
            }
         } else {
            log.trace("TridonicSensorExt update query device null");
         }

         if (var1) {
            this.getLuxLevel().setStatus(BStatus.ok);
            this.getOccupancy().setStatus(BStatus.ok);
            log.trace("TridonicSensorExt update success [" + var4 + '/' + var6 + ']');
         } else {
            this.getLuxLevel().setStatusFault(true);
            this.getOccupancy().setStatusFault(true);
            log.trace("TridonicSensorExt fault");
         }
      } catch (Exception var8) {
         log.trace("TridonicSensorExt update exception: " + var8);
      }

   }

   public BTridonicSensorExt() {
     this.oldLuxLevel = -1.0;
     this.oldOccupancyState = false;
     this.daliDevice = null;
     this.pollTimer = null;
   }

   static {
      occupancy = newProperty(265, new BStatusEnum(), BFacets.makeEnum(BEnumRange.make(BOccupancy.TYPE)));
      history = newProperty(257, new BTridonicSensorHistory(), (BFacets)null);
      pollPeriod = newProperty(256, 800, BFacets.make("units", UnitDatabase.getUnit("millisecond")));
      update = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BTridonicSensorExt.class);
      log = Log.getLog("elitedali2.sensor");
   }
}
