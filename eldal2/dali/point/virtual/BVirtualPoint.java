package uk.co.controlnetworksolutions.elitedali2.dali.point.virtual;

import com.tridium.basicdriver.util.BIBasicPollable;
import javax.baja.driver.util.BPollFrequency;
import javax.baja.status.BIStatus;
import javax.baja.status.BIStatusValue;
import javax.baja.status.BStatus;
import javax.baja.sys.Action;
import javax.baja.sys.BFacets;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.virtual.BVirtualComponent;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;

public abstract class BVirtualPoint extends BVirtualComponent implements BIBasicPollable, BIStatus, BIStatusValue {
   public static final Property status = newProperty(1, BStatus.make(0), (BFacets)null);
   public static final Property pollFrequency;
   public static final Action subscribe;
   public static final Type TYPE;
   protected DaliPoint daliPoint;
   protected BDaliNetwork network;
   protected BGenericDaliDevice device;
   protected boolean isPollSubscribed;

   public BStatus getStatus() {
      return (BStatus)this.get(status);
   }

   public void setStatus(BStatus var1) {
      this.set(status, var1, (Context)null);
   }

   public BPollFrequency getPollFrequency() {
      return (BPollFrequency)this.get(pollFrequency);
   }

   public void setPollFrequency(BPollFrequency var1) {
      this.set(pollFrequency, var1, (Context)null);
   }

   public void subscribe() {
      this.invoke(subscribe, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void started() throws Exception {
      super.started();
      this.network = ((BVirtualPointsGateway)this.getVirtualGateway()).getDaliNetwork();
      this.device = (BGenericDaliDevice)this.getVirtualGateway().getParent();
   }

   public void stopped() throws Exception {
      super.stopped();
      if (this.isPollSubscribed) {
         this.network.getPollScheduler().unsubscribe(this);
         this.isPollSubscribed = false;
      }

   }

   public String getIdentifier() {
      return this.daliPoint != null ? this.daliPoint.getIdentifier() : null;
   }

   public void poll() {
      if (!this.device.isDisabled() && !this.device.isFault()) {
         if (this.daliPoint == null) {
            this.setStatus(BStatus.fault);
         } else if (this.daliPoint.queryValue(this.device)) {
            if (this.daliPoint.isSuccessReal()) {
               this.setStatus(BStatus.ok);
            }

            this.updatePointValue();
         } else {
            this.setStatus(BStatus.fault);
         }

      }
   }

   public abstract void updatePointValue();

   public BFacets getStatusValueFacets() {
      return BFacets.NULL;
   }

   public void subscribed() {
      this.subscribe();
   }

   public void doSubscribe() {
      this.network.getPollScheduler().subscribe(this);
      this.isPollSubscribed = true;
   }

   public BVirtualPoint(DaliPoint var1) {
     this.daliPoint = null;
     this.network = null;
     this.device = null;
     this.isPollSubscribed = false;
     
      this.daliPoint = var1;
   }

   static {
      pollFrequency = newProperty(0, BPollFrequency.slow, (BFacets)null);
      subscribe = newAction(20, (BFacets)null);
      TYPE = Sys.loadType(BVirtualPoint.class);
   }
}
