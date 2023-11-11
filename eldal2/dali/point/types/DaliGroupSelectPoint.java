package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BBooleanPoint;
import javax.baja.control.BControlPoint;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusValue;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualBooleanPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;

public class DaliGroupSelectPoint extends DaliPoint {
   private int daliGroupIndex;
   private int daliQueryBitMask;
   private int daliGroupAddCommand;
   private int daliGroupRemoveCommand;
   private boolean daliPointValue;

   private final void createPoint(int var1, boolean var2) {
      this.daliGroupIndex = var1;
      if (this.daliGroupIndex < 8) {
         this.daliQueryCode = 192;
         this.daliQueryBitMask = 1 << var1;
      } else {
         this.daliQueryCode = 193;
         this.daliQueryBitMask = 1 << var1 - 8;
      }

      if (!var2) {
         this.daliGroupAddCommand = 96 + var1;
      } else {
         this.daliGroupAddCommand = -1;
      }

      this.daliGroupRemoveCommand = 112 + var1;
   }

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
         if ((var2 & this.daliQueryBitMask) != 0) {
            this.daliPointValue = true;
         }

         var3 = true;
      }

      return var3;
   }

   public BStatusValue getStatusValue() {
      return new BStatusBoolean(this.daliPointValue);
   }

   public boolean writeValue(BGenericDaliDevice var1, boolean var2) {
      boolean var3 = false;
      DaliCommand var4 = null;
      if (var2) {
         if (this.daliGroupAddCommand >= 0) {
            var4 = new DaliCommand(var1, this.daliGroupAddCommand);
         }
      } else if (this.daliGroupRemoveCommand >= 0) {
         var4 = new DaliCommand(var1, this.daliGroupRemoveCommand);
      }

      if (var4 != null) {
         var4.setTransmitRepeatCount(3);
         var4.execute();
         var3 = var4.success();
      }

      return var3;
   }

   public DaliGroupSelectPoint(String var1, int var2) {
      super(var1, 192);
      this.daliGroupIndex = -1;
      this.daliQueryBitMask = 0;
      this.daliGroupAddCommand = -1;
      this.daliGroupRemoveCommand = -1;
      this.daliPointValue = false;
      
      this.createPoint(var2, false);
   }

   public DaliGroupSelectPoint(String var1, int var2, boolean var3) {
      super(var1, 192);
      this.daliGroupIndex = -1;
      this.daliQueryBitMask = 0;
      this.daliGroupAddCommand = -1;
      this.daliGroupRemoveCommand = -1;
      this.daliPointValue = false;
      
      this.createPoint(var2, var3);
   }
}
