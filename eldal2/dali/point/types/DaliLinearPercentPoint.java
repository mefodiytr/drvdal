package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BControlPoint;
import javax.baja.control.BNumericPoint;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualNumericPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;

public class DaliLinearPercentPoint extends DaliPoint {
   private double daliPointValue;
   private boolean daliPointNullFlag;

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
      this.daliPointValue = 0.0;
      this.daliPointNullFlag = true;
      if (var1 == 53) {
         if (var2 != 255) {
            this.daliPointValue = (double)var2 / 2.54;
            this.daliPointNullFlag = false;
         }

         var3 = true;
      }

      return var3;
   }

   public BStatusValue getStatusValue() {
      return this.daliPointNullFlag ? new BStatusNumeric(this.daliPointValue, BStatus.nullStatus) : new BStatusNumeric(this.daliPointValue);
   }

   public BFacets getFacets() {
      return BFacets.make("units", UnitDatabase.getUnit("percent"));
   }

   public DaliLinearPercentPoint(String var1, int var2) {
      super(var1, var2);
      this.daliPointValue = 0.0;
      this.daliPointNullFlag = false;
   }
}
