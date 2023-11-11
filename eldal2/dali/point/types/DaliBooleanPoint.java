package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BBooleanPoint;
import javax.baja.control.BControlPoint;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualBooleanPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;

public class DaliBooleanPoint extends DaliPoint {
   private boolean daliPointValue;
   private BFacets daliPointFacet;

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
         this.daliPointValue = true;
         var3 = true;
      } else if (var1 == 1) {
         this.daliPointValue = false;
         var3 = true;
      }

      return var3;
   }

   public boolean isSuccessReal() {
      return false;
   }

   public BStatusValue getStatusValue() {
      return new BStatusBoolean(this.daliPointValue);
   }

   public BFacets getFacets() {
      return this.daliPointFacet;
   }

   public DaliBooleanPoint(String var1, int var2) {
      super(var1, var2);
      this.daliPointValue = false;
      this.daliPointFacet = BFacets.NULL;
   }

   public DaliBooleanPoint(String var1, int var2, BFacets var3) {
      super(var1, var2);
      this.daliPointValue = false;
      this.daliPointFacet = BFacets.NULL;
      
      this.daliPointFacet = var3;
   }
}
