package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BBooleanPoint;
import javax.baja.control.BControlPoint;
import javax.baja.driver.util.BPollFrequency;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusValue;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualBooleanPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;

public class DaliBitFieldPoint extends DaliPoint {
   private int dataBitMask;
   private boolean daliPointValue;

   public Type getPointType() {
      return BBooleanPoint.TYPE;
   }

   public BControlPoint getProxyPoint() {
      return new BBooleanPoint();
   }

   public BVirtualPoint getVirtualPoint() {
      return new BVirtualBooleanPoint(this);
   }

   public boolean processDaliQuery(int var1, int var2) {
      boolean var3 = false;
      this.daliPointValue = false;
      if (var1 == 53) {
         boolean var10001 = false;
         if ((var2 & this.dataBitMask) != 0) {
            var10001 = true;
         }

         this.daliPointValue = var10001;
         var3 = true;
      }

      return var3;
   }

   public BStatusValue getStatusValue() {
      return new BStatusBoolean(this.daliPointValue);
   }

   public BPollFrequency getPollFrequency() {
      return BPollFrequency.slow;
   }

   public DaliBitFieldPoint(String var1, int var2, int var3) {
      super(var1, var2);
      this.dataBitMask = 0;
      this.daliPointValue = false;
      
      this.dataBitMask = 1 << var3;
   }
}
