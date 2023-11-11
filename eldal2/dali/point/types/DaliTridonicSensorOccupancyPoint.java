package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BControlPoint;
import javax.baja.control.BEnumPoint;
import javax.baja.driver.util.BPollFrequency;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BOccupancy;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualEnumPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.TridonicUtils;

public class DaliTridonicSensorOccupancyPoint extends DaliPoint {
   private int daliPointValue;

   public Type getPointType() {
      return BEnumPoint.TYPE;
   }

   public BControlPoint getProxyPoint() {
      return new BEnumPoint();
   }

   public BVirtualPoint getVirtualPoint() {
      return new BVirtualEnumPoint(this);
   }

   public boolean processDaliQuery(int var1, int var2) {
      boolean var3 = false;
      this.daliPointValue = -1;
      if (var1 == 53) {
         if (TridonicUtils.getSensorOccupancy(var2)) {
            this.daliPointValue = 0;
         } else {
            this.daliPointValue = 1;
         }

         var3 = true;
      }

      return var3;
   }

   public BStatusValue getStatusValue() {
      return new BStatusEnum(BDynamicEnum.make(this.daliPointValue));
   }

   public BFacets getFacets() {
      return BFacets.makeEnum(BEnumRange.make(BOccupancy.TYPE));
   }

   public BPollFrequency getPollFrequency() {
      return BPollFrequency.fast;
   }

   public DaliTridonicSensorOccupancyPoint(String var1) {
      super(var1, 160);
      this.daliPointValue = -1;
   }
}
