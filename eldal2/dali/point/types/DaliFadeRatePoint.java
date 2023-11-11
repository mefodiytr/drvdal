package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BControlPoint;
import javax.baja.control.BEnumPoint;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BFadeRate;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliWrite;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualEnumPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;

public class DaliFadeRatePoint extends DaliPoint {
   private static int daliCommandCode = 47;
   private static boolean daliPointWritableFlag = false;
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
      this.daliPointValue = 0;
      if (var1 == 53) {
         this.daliPointValue = var2 & 15;
         var3 = true;
      }

      return var3;
   }

   public BStatusValue getStatusValue() {
      return new BStatusEnum(BDynamicEnum.make(this.daliPointValue));
   }

   public boolean writeValue(BGenericDaliDevice var1, int var2) {
      DaliWrite var3 = new DaliWrite(var1.getDaliNetwork(), var1.getDaliAddress(), daliCommandCode, var2);
      var3.execute();
      return var3.success();
   }

   public BFacets getFacets() {
      return BFacets.makeEnum(BEnumRange.make(BFadeRate.TYPE));
   }

   public DaliFadeRatePoint(String var1) {
      super(var1, 165);
      this.daliPointValue = 0;
   }
}
