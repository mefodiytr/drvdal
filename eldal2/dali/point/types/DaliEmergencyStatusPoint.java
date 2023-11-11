package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BControlPoint;
import javax.baja.driver.util.BPollFrequency;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusValue;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliEmergencyStatusProxyPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualNumericPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;

public class DaliEmergencyStatusPoint extends DaliPoint {
   private int daliPointValue;

   public Type getPointType() {
      return BDaliEmergencyStatusProxyPoint.TYPE;
   }

   public BControlPoint getProxyPoint() {
      return new BDaliEmergencyStatusProxyPoint();
   }

   public BVirtualPoint getVirtualPoint() {
      return new BVirtualNumericPoint(this);
   }

   public boolean processDaliQuery(int var1, int var2) {
      boolean var3 = false;
      this.daliPointValue = 0;
      if (var1 == 53) {
         this.daliPointValue = var2;
         var3 = true;
      }

      return var3;
   }

   public BStatusValue getStatusValue() {
      return new BStatusNumeric((double)this.daliPointValue);
   }

   public BPollFrequency getPollFrequency() {
      return BPollFrequency.slow;
   }

   public DaliEmergencyStatusPoint(String var1) {
      super(var1, 253);
      this.daliPointValue = 0;
   }
}
