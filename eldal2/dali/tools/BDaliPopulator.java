package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import javax.baja.driver.util.BPollFrequency;
import javax.baja.log.Log;
import javax.baja.naming.BOrd;
import javax.baja.status.BStatus;
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

public final class BDaliPopulator extends BComponent {
   public static final Property statusMessage = newProperty(265, "", (BFacets)null);
   public static final Property networkCount = newProperty(257, 0, (BFacets)null);
   public static final Property ballastCount = newProperty(257, 0, (BFacets)null);
   public static final Property sensorCount = newProperty(257, 0, (BFacets)null);
   public static final Property populateStation = newProperty(256, false, (BFacets)null);
   public static final Property ballastGroupSkip = newProperty(256, false, (BFacets)null);
   public static final Property ballastStatusPointEnable = newProperty(256, false, (BFacets)null);
   public static final Property ballastStatusPointPollPeriod = newProperty(256, BPollFrequency.make(2), (BFacets)null);
   public static final Property ballastActualLevelPointEnable = newProperty(256, false, (BFacets)null);
   public static final Property ballastActualLevelPointPollPeriod = newProperty(256, BPollFrequency.make(2), (BFacets)null);
   public static final Property ballastLampFailurePointEnable = newProperty(256, false, (BFacets)null);
   public static final Property ballastLampFailurePointPollPeriod = newProperty(256, BPollFrequency.make(2), (BFacets)null);
   public static final Property ballastLampPowerOnPointEnable = newProperty(256, false, (BFacets)null);
   public static final Property ballastLampPowerOnPointPollPeriod = newProperty(256, BPollFrequency.make(2), (BFacets)null);
   public static final Property occupancySensorEnable = newProperty(256, false, (BFacets)null);
   public static final Property occupancySensorPollPeriod;
   public static final Property luxSensorEnable;
   public static final Property luxSensorPollPeriod;
   public static final Property temperatureSensorEnable;
   public static final Property temperatureSensorPollPeriod;
   public static final Action populateDevices;
   public static final Type TYPE;
   private static String STATION_ROOT;
   private static final Log log;
   private BDaliPopulatorJob daliPopulatorTask;

   public final String getStatusMessage() {
      return this.getString(statusMessage);
   }

   public final void setStatusMessage(String var1) {
      this.setString(statusMessage, var1, (Context)null);
   }

   public final int getNetworkCount() {
      return this.getInt(networkCount);
   }

   public final void setNetworkCount(int var1) {
      this.setInt(networkCount, var1, (Context)null);
   }

   public final int getBallastCount() {
      return this.getInt(ballastCount);
   }

   public final void setBallastCount(int var1) {
      this.setInt(ballastCount, var1, (Context)null);
   }

   public final int getSensorCount() {
      return this.getInt(sensorCount);
   }

   public final void setSensorCount(int var1) {
      this.setInt(sensorCount, var1, (Context)null);
   }

   public final boolean getPopulateStation() {
      return this.getBoolean(populateStation);
   }

   public final void setPopulateStation(boolean var1) {
      this.setBoolean(populateStation, var1, (Context)null);
   }

   public final boolean getBallastGroupSkip() {
      return this.getBoolean(ballastGroupSkip);
   }

   public final void setBallastGroupSkip(boolean var1) {
      this.setBoolean(ballastGroupSkip, var1, (Context)null);
   }

   public final boolean getBallastStatusPointEnable() {
      return this.getBoolean(ballastStatusPointEnable);
   }

   public final void setBallastStatusPointEnable(boolean var1) {
      this.setBoolean(ballastStatusPointEnable, var1, (Context)null);
   }

   public final BPollFrequency getBallastStatusPointPollPeriod() {
      return (BPollFrequency)this.get(ballastStatusPointPollPeriod);
   }

   public final void setBallastStatusPointPollPeriod(BPollFrequency var1) {
      this.set(ballastStatusPointPollPeriod, var1, (Context)null);
   }

   public final boolean getBallastActualLevelPointEnable() {
      return this.getBoolean(ballastActualLevelPointEnable);
   }

   public final void setBallastActualLevelPointEnable(boolean var1) {
      this.setBoolean(ballastActualLevelPointEnable, var1, (Context)null);
   }

   public final BPollFrequency getBallastActualLevelPointPollPeriod() {
      return (BPollFrequency)this.get(ballastActualLevelPointPollPeriod);
   }

   public final void setBallastActualLevelPointPollPeriod(BPollFrequency var1) {
      this.set(ballastActualLevelPointPollPeriod, var1, (Context)null);
   }

   public final boolean getBallastLampFailurePointEnable() {
      return this.getBoolean(ballastLampFailurePointEnable);
   }

   public final void setBallastLampFailurePointEnable(boolean var1) {
      this.setBoolean(ballastLampFailurePointEnable, var1, (Context)null);
   }

   public final BPollFrequency getBallastLampFailurePointPollPeriod() {
      return (BPollFrequency)this.get(ballastLampFailurePointPollPeriod);
   }

   public final void setBallastLampFailurePointPollPeriod(BPollFrequency var1) {
      this.set(ballastLampFailurePointPollPeriod, var1, (Context)null);
   }

   public final boolean getBallastLampPowerOnPointEnable() {
      return this.getBoolean(ballastLampPowerOnPointEnable);
   }

   public final void setBallastLampPowerOnPointEnable(boolean var1) {
      this.setBoolean(ballastLampPowerOnPointEnable, var1, (Context)null);
   }

   public final BPollFrequency getBallastLampPowerOnPointPollPeriod() {
      return (BPollFrequency)this.get(ballastLampPowerOnPointPollPeriod);
   }

   public final void setBallastLampPowerOnPointPollPeriod(BPollFrequency var1) {
      this.set(ballastLampPowerOnPointPollPeriod, var1, (Context)null);
   }

   public final boolean getOccupancySensorEnable() {
      return this.getBoolean(occupancySensorEnable);
   }

   public final void setOccupancySensorEnable(boolean var1) {
      this.setBoolean(occupancySensorEnable, var1, (Context)null);
   }

   public final BStatusNumeric getOccupancySensorPollPeriod() {
      return (BStatusNumeric)this.get(occupancySensorPollPeriod);
   }

   public final void setOccupancySensorPollPeriod(BStatusNumeric var1) {
      this.set(occupancySensorPollPeriod, var1, (Context)null);
   }

   public final boolean getLuxSensorEnable() {
      return this.getBoolean(luxSensorEnable);
   }

   public final void setLuxSensorEnable(boolean var1) {
      this.setBoolean(luxSensorEnable, var1, (Context)null);
   }

   public final BStatusNumeric getLuxSensorPollPeriod() {
      return (BStatusNumeric)this.get(luxSensorPollPeriod);
   }

   public final void setLuxSensorPollPeriod(BStatusNumeric var1) {
      this.set(luxSensorPollPeriod, var1, (Context)null);
   }

   public final boolean getTemperatureSensorEnable() {
      return this.getBoolean(temperatureSensorEnable);
   }

   public final void setTemperatureSensorEnable(boolean var1) {
      this.setBoolean(temperatureSensorEnable, var1, (Context)null);
   }

   public final BStatusNumeric getTemperatureSensorPollPeriod() {
      return (BStatusNumeric)this.get(temperatureSensorPollPeriod);
   }

   public final void setTemperatureSensorPollPeriod(BStatusNumeric var1) {
      this.set(temperatureSensorPollPeriod, var1, (Context)null);
   }

   public final void populateDevices() {
      this.invoke(populateDevices, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final Type[] getServiceTypes() {
      return null;
   }

   public final void started() {
      this.setNetworkCount(0);
      this.setBallastCount(0);
      this.setSensorCount(0);
      this.setStatusMessage("OK");
   }

   public final void doPopulateDevices() {
      BComponent var1;
      if (this.getPopulateStation()) {
         var1 = (BComponent)BOrd.make(STATION_ROOT).get();
      } else {
         var1 = (BComponent)this.getParent();
      }

      if (var1 != null) {
         this.daliPopulatorTask = new BDaliPopulatorJob(var1, this);
         if (this.daliPopulatorTask == null) {
            log.error("Cannot start DALI device populator: Could not create job");
         }

         this.daliPopulatorTask.submit((Context)null);
      } else {
         log.error("Cannot start DALI device populator: Invalid parent node");
      }

   }

   public final Log getLog() {
      return log;
   }

   public BDaliPopulator() {
     this.daliPopulatorTask = null;
   }

   static {
      occupancySensorPollPeriod = newProperty(256, new BStatusNumeric(1000.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(500.0), BDouble.make(1.0E9)));
      luxSensorEnable = newProperty(4, false, (BFacets)null);
      luxSensorPollPeriod = newProperty(4, new BStatusNumeric(10000.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(500.0), BDouble.make(1.0E9)));
      temperatureSensorEnable = newProperty(4, false, (BFacets)null);
      temperatureSensorPollPeriod = newProperty(4, new BStatusNumeric(30000.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(500.0), BDouble.make(1.0E9)));
      populateDevices = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BDaliPopulator.class);
      STATION_ROOT = "station:|slot:/Drivers";
      log = Log.getLog("elitedali2.populator");
   }
}
