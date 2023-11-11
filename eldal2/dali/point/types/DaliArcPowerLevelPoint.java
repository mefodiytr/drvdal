package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BControlPoint;
import javax.baja.control.BNumericPoint;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliWrite;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualNumericPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.DaliArcPowerUtils;

public class DaliArcPowerLevelPoint extends DaliPoint {
   private int daliCommandCode;
   private boolean daliPointWritableFlag;
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
            this.daliPointValue = DaliArcPowerUtils.directLevelToPercent(var2);
            this.daliPointNullFlag = false;
         }

         var3 = true;
      }

      return var3;
   }

   public BStatusValue getStatusValue() {
      return this.daliPointNullFlag ? new BStatusNumeric(this.daliPointValue, BStatus.nullStatus) : new BStatusNumeric(this.daliPointValue);
   }

   public boolean writeValue(BGenericDaliDevice var1, double var2) {
      if (this.daliCommandCode < 0) {
         return false;
      } else {
         int var4 = DaliArcPowerUtils.percentToDirectLevel(var2);
         DaliWrite var5 = new DaliWrite(var1.getDaliNetwork(), var1.getDaliAddress(), this.daliCommandCode, var4);
         var5.execute();
         return var5.success();
      }
   }

   public boolean writeNull(BGenericDaliDevice var1) {
      if (this.daliCommandCode < 0) {
         return false;
      } else {
         DaliWrite var2 = new DaliWrite(var1.getDaliNetwork(), var1.getDaliAddress(), this.daliCommandCode, 255);
         var2.execute();
         return var2.success();
      }
   }

   public BFacets getFacets() {
      return BFacets.make("units", UnitDatabase.getUnit("percent"));
   }

   public DaliArcPowerLevelPoint(String var1, int var2) {
      super(var1, var2);
      this.daliCommandCode = -1;
      this.daliPointWritableFlag = false;
      this.daliPointValue = 0.0;
      this.daliPointNullFlag = false;
      
      this.daliCommandCode = -1;
      this.daliPointWritableFlag = false;
   }

   public DaliArcPowerLevelPoint(String var1, int var2, int var3) {
      super(var1, var2);
      this.daliCommandCode = -1;
      this.daliPointWritableFlag = false;
      this.daliPointValue = 0.0;
      this.daliPointNullFlag = false;
      
      this.daliCommandCode = var3;
      this.daliPointWritableFlag = true;
   }
}
