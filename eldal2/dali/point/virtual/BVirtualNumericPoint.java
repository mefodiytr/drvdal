package uk.co.controlnetworksolutions.elitedali2.dali.point.virtual;

import javax.baja.status.BStatus;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusValue;
import javax.baja.sys.Action;
import javax.baja.sys.BFacets;
import javax.baja.sys.BINumeric;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;

public final class BVirtualNumericPoint extends BVirtualPoint implements BINumeric {
   public static final Property readValue = newProperty(265, new BStatusNumeric(), (BFacets)null);
   public static final Property writeValue = newProperty(264, new BStatusNumeric(), (BFacets)null);
   public static final Property facets = newProperty(0, BFacets.makeEnum(), (BFacets)null);
   public static final Action setValue = newAction(0, BInteger.make(0), (BFacets)null);
   public static final Action setNull = newAction(0, (BFacets)null);
   public static final Type TYPE;

   public final BStatusNumeric getReadValue() {
      return (BStatusNumeric)this.get(readValue);
   }

   public final void setReadValue(BStatusNumeric var1) {
      this.set(readValue, var1, (Context)null);
   }

   public final BStatusNumeric getWriteValue() {
      return (BStatusNumeric)this.get(writeValue);
   }

   public final void setWriteValue(BStatusNumeric var1) {
      this.set(writeValue, var1, (Context)null);
   }

   public final BFacets getFacets() {
      return (BFacets)this.get(facets);
   }

   public final void setFacets(BFacets var1) {
      this.set(facets, var1, (Context)null);
   }

   public final void setValue(BInteger var1) {
      this.invoke(setValue, var1, (Context)null);
   }

   public final void setNull() {
      this.invoke(setNull, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void started() throws Exception {
      super.started();
      if (this.daliPoint != null) {
         this.setFacets(this.daliPoint.getFacets());
      }

      this.setWriteValue(new BStatusNumeric(0.0, BStatus.nullStatus));
   }

   public final void updatePointValue() {
      this.setReadValue((BStatusNumeric)this.daliPoint.getStatusValue());
   }

   public final double getNumeric() {
      return this.getReadValue().getValue();
   }

   public final BFacets getNumericFacets() {
      return this.daliPoint.getFacets();
   }

   public final BFacets getStatusValueFacets() {
      return this.daliPoint.getFacets();
   }

   public final BStatusValue getStatusValue() {
      return this.getReadValue();
   }

   public final void doSetValue(BInteger var1) {
      this.setWriteValue(new BStatusNumeric(var1.getDouble()));
   }

   public final void doSetNull() {
      this.setWriteValue(new BStatusNumeric(0.0, BStatus.nullStatus));
      this.daliPoint.writeNull(this.device);
   }

   public final void changed(Property var1, Context var2) {
      if (this.isRunning() && Sys.atSteadyState() && var1 == writeValue && !this.getWriteValue().getStatus().isNull()) {
         this.daliPoint.writeValue(this.device, this.getWriteValue().getValue());
      }

   }

   public BVirtualNumericPoint() {
      super((DaliPoint)null);
   }

   public BVirtualNumericPoint(DaliPoint var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BVirtualNumericPoint.class);
   }
}
