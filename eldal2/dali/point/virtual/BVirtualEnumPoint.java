package uk.co.controlnetworksolutions.elitedali2.dali.point.virtual;

import javax.baja.status.BStatus;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusValue;
import javax.baja.sys.Action;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BEnum;
import javax.baja.sys.BFacets;
import javax.baja.sys.BIEnum;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Slot;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;

public final class BVirtualEnumPoint extends BVirtualPoint implements BIEnum {
   public static final Property readValue = newProperty(265, new BStatusEnum(), (BFacets)null);
   public static final Property writeValue = newProperty(264, new BStatusEnum(), (BFacets)null);
   public static final Property facets = newProperty(0, BFacets.makeEnum(), (BFacets)null);
   public static final Action setValue = newAction(0, BDynamicEnum.make(0), (BFacets)null);
   public static final Type TYPE;

   public final BStatusEnum getReadValue() {
      return (BStatusEnum)this.get(readValue);
   }

   public final void setReadValue(BStatusEnum var1) {
      this.set(readValue, var1, (Context)null);
   }

   public final BStatusEnum getWriteValue() {
      return (BStatusEnum)this.get(writeValue);
   }

   public final void setWriteValue(BStatusEnum var1) {
      this.set(writeValue, var1, (Context)null);
   }

   public final BFacets getFacets() {
      return (BFacets)this.get(facets);
   }

   public final void setFacets(BFacets var1) {
      this.set(facets, var1, (Context)null);
   }

   public final void setValue(BEnum var1) {
      this.invoke(setValue, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void started() throws Exception {
      super.started();
      if (this.daliPoint != null) {
         this.setFacets(this.daliPoint.getFacets());
      }

      this.setWriteValue(new BStatusEnum(BDynamicEnum.make(0), BStatus.nullStatus));
   }

   public final BFacets getSlotFacets(Slot var1) {
      if (var1 == readValue) {
         return this.getFacets();
      } else if (var1 == writeValue) {
         return this.getFacets();
      } else {
         return var1 == setValue ? this.getFacets() : super.getSlotFacets(var1);
      }
   }

   public final void updatePointValue() {
      this.setReadValue((BStatusEnum)this.daliPoint.getStatusValue());
   }

   public final BEnum getEnum() {
      return this.getReadValue().getEnum();
   }

   public final BFacets getEnumFacets() {
      return this.getReadValue().getEnumFacets();
   }

   public final BStatusValue getStatusValue() {
      return this.getReadValue();
   }

   public final void doSetValue(BEnum var1) {
      this.setWriteValue(new BStatusEnum(var1));
   }

   public final void changed(Property var1, Context var2) {
      if (this.isRunning() && Sys.atSteadyState() && var1 == writeValue && !this.getWriteValue().getStatus().isNull()) {
         this.daliPoint.writeValue(this.device, this.getWriteValue().getEnum().getOrdinal());
      }

   }

   public BVirtualEnumPoint() {
      super((DaliPoint)null);
   }

   public BVirtualEnumPoint(DaliPoint var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BVirtualEnumPoint.class);
   }
}
