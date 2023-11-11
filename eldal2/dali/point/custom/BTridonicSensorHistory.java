package uk.co.controlnetworksolutions.elitedali2.dali.point.custom;

import javax.baja.sys.BAbsTime;
import javax.baja.sys.BComponent;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BTridonicSensorHistory extends BComponent {
   public static final Property lastLuxChangeTime;
   public static final Property lastOccupancyChangeTime;
   public static final Property lastOccupiedTime;
   public static final Property lastUnoccupiedTime;
   public static final Type TYPE;

   public BAbsTime getLastLuxChangeTime() {
      return (BAbsTime)this.get(lastLuxChangeTime);
   }

   public void setLastLuxChangeTime(BAbsTime var1) {
      this.set(lastLuxChangeTime, var1, (Context)null);
   }

   public BAbsTime getLastOccupancyChangeTime() {
      return (BAbsTime)this.get(lastOccupancyChangeTime);
   }

   public void setLastOccupancyChangeTime(BAbsTime var1) {
      this.set(lastOccupancyChangeTime, var1, (Context)null);
   }

   public BAbsTime getLastOccupiedTime() {
      return (BAbsTime)this.get(lastOccupiedTime);
   }

   public void setLastOccupiedTime(BAbsTime var1) {
      this.set(lastOccupiedTime, var1, (Context)null);
   }

   public BAbsTime getLastUnoccupiedTime() {
      return (BAbsTime)this.get(lastUnoccupiedTime);
   }

   public void setLastUnoccupiedTime(BAbsTime var1) {
      this.set(lastUnoccupiedTime, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   static {
      lastLuxChangeTime = newProperty(257, BAbsTime.DEFAULT, (BFacets)null);
      lastOccupancyChangeTime = newProperty(257, BAbsTime.DEFAULT, (BFacets)null);
      lastOccupiedTime = newProperty(257, BAbsTime.DEFAULT, (BFacets)null);
      lastUnoccupiedTime = newProperty(257, BAbsTime.DEFAULT, (BFacets)null);
      TYPE = Sys.loadType(BTridonicSensorHistory.class);
   }
}
