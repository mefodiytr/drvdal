package uk.co.controlnetworksolutions.elitedali2.dali.point.virtual;

import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusValue;
import javax.baja.sys.Action;
import javax.baja.sys.BBoolean;
import javax.baja.sys.BEnum;
import javax.baja.sys.BFacets;
import javax.baja.sys.BIBoolean;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;

public final class BVirtualBooleanPoint extends BVirtualPoint implements BIBoolean {
   public static final Property readValue = newProperty(265, new BStatusBoolean(), (BFacets)null);
   public static final Property writeValue = newProperty(264, new BStatusBoolean(), (BFacets)null);
   public static final Action setValue = newAction(0, BBoolean.make(false), (BFacets)null);
   public static final Action setTrue = newAction(0, (BFacets)null);
   public static final Action setFalse = newAction(0, (BFacets)null);
   public static final Type TYPE;

   public final BStatusBoolean getReadValue() {
      return (BStatusBoolean)this.get(readValue);
   }

   public final void setReadValue(BStatusBoolean var1) {
      this.set(readValue, var1, (Context)null);
   }

   public final BStatusBoolean getWriteValue() {
      return (BStatusBoolean)this.get(writeValue);
   }

   public final void setWriteValue(BStatusBoolean var1) {
      this.set(writeValue, var1, (Context)null);
   }

   public final void setValue(BBoolean var1) {
      this.invoke(setValue, var1, (Context)null);
   }

   public final void setTrue() {
      this.invoke(setTrue, (BValue)null, (Context)null);
   }

   public final void setFalse() {
      this.invoke(setFalse, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void started() throws Exception {
      super.started();
      this.setWriteValue(new BStatusBoolean(false, BStatus.nullStatus));
   }

   public final void updatePointValue() {
      this.setReadValue((BStatusBoolean)this.daliPoint.getStatusValue());
   }

   public final boolean getBoolean() {
      return this.getReadValue().getBoolean();
   }

   public final BFacets getBooleanFacets() {
      return BFacets.NULL;
   }

   public final BEnum getEnum() {
      return this.getReadValue().getEnum();
   }

   public final BFacets getEnumFacets() {
      return BFacets.NULL;
   }

   public final BStatusValue getStatusValue() {
      return this.getReadValue();
   }

   public final void doSetValue(BBoolean var1) {
      this.setWriteValue(new BStatusBoolean(var1.getBoolean()));
   }

   public final void doSetTrue() {
      this.setWriteValue(new BStatusBoolean(true));
   }

   public final void doSetFalse() {
      this.setWriteValue(new BStatusBoolean(false));
   }

   public final void changed(Property var1, Context var2) {
      if (this.isRunning() && Sys.atSteadyState() && var1 == writeValue && !this.getWriteValue().getStatus().isNull()) {
         this.daliPoint.writeValue(this.device, this.getWriteValue().getBoolean());
      }

   }

   public BVirtualBooleanPoint() {
      super((DaliPoint)null);
   }

   public BVirtualBooleanPoint(DaliPoint var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BVirtualBooleanPoint.class);
   }
}
