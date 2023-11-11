package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BControlPoint;
import javax.baja.control.BNumericPoint;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualNumericPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;

public class DaliDurationPoint extends DaliPoint {
   private int secondsPerStep;
   private int daliPointValue;

   public Type getPointType() {
      return BNumericPoint.TYPE;
   }

   public BControlPoint getProxyPoint() {
      return new BNumericPoint();
   }

   public BVirtualPoint getVirtualPoint() {
      return new BVirtualNumericPoint(this);
   }

   public boolean processDaliQuery(int var1, int var2) {
      boolean var3 = false;
      this.daliPointValue = 0;
      if (var1 == 53) {
         this.daliPointValue = var2 * this.secondsPerStep;
         var3 = true;
      }

      return var3;
   }

   public BStatusValue getStatusValue() {
      return new BStatusNumeric((double)this.daliPointValue);
   }

   public BFacets getFacets() {
      return BFacets.make("units", UnitDatabase.getUnit("second"));
   }

   public DaliDurationPoint(String var1, int var2, int var3) {
      super(var1, var2);
      this.secondsPerStep = -1;
      this.daliPointValue = 0;
      
      this.secondsPerStep = var3;
   }
}
