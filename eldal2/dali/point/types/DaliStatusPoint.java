package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BControlPoint;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusValue;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliStatusProxyPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualNumericPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;

public class DaliStatusPoint extends DaliPoint {
   private int daliPointValue;

   public Type getPointType() {
      return BDaliStatusProxyPoint.TYPE;
   }

   public BControlPoint getProxyPoint() {
      return new BDaliStatusProxyPoint();
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

   public DaliStatusPoint(String var1) {
      super(var1, 144);
      this.daliPointValue = 0;
   }
}
