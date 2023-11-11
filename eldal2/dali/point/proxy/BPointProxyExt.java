package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import com.tridium.basicdriver.util.BIBasicPollable;
import javax.baja.control.BControlPoint;
import javax.baja.driver.point.BProxyExt;
import javax.baja.driver.point.BReadWriteMode;
import javax.baja.driver.util.BPollFrequency;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliStatus;
import uk.co.controlnetworksolutions.elitedali2.dali.point.database.PointSearch;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;

public class BPointProxyExt extends BProxyExt implements BIBasicPollable {
   public static final Property pointIdentifier = newProperty(1, "unknown", (BFacets)null);
   public static final Property pollFrequency;
   public static final Property pollAssigned;
   public static final Type TYPE;
   protected DaliPoint daliPoint;

   public String getPointIdentifier() {
      return this.getString(pointIdentifier);
   }

   public void setPointIdentifier(String var1) {
      this.setString(pointIdentifier, var1, (Context)null);
   }

   public BPollFrequency getPollFrequency() {
      return (BPollFrequency)this.get(pollFrequency);
   }

   public void setPollFrequency(BPollFrequency var1) {
      this.set(pollFrequency, var1, (Context)null);
   }

   public boolean getPollAssigned() {
      return this.getBoolean(pollAssigned);
   }

   public void setPollAssigned(boolean var1) {
      this.setBoolean(pollAssigned, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void started() throws Exception {
      super.started();
      this.setFlags(this.getSlot("deviceFacets"), 4);
      this.setFlags(this.getSlot("conversion"), 4);
      this.setFlags(this.getSlot("readValue"), 4);
      this.setFlags(this.getSlot("writeValue"), 4);
      if (this.daliPoint == null) {
         this.daliPoint = PointSearch.get(this.getPointIdentifier());
         if (this.daliPoint == null) {
            throw new NullPointerException("Could not find definition for point: '" + this.getPointIdentifier() + '\'');
         }
      }

      this.setDeviceFacets(this.daliPoint.getFacets());
      ((BControlPoint)this.getParent()).setFacets(this.daliPoint.getFacets());
      if (!this.getPollAssigned()) {
         this.setPollFrequency(this.daliPoint.getPollFrequency());
         this.setPollAssigned(true);
      }

   }

   public final BGenericDaliDevice getDaliDevice() {
      return (BGenericDaliDevice)this.getDevice();
   }

   public Type getDeviceExtType() {
      return BDaliDevicePointExt.TYPE;
   }

   public BReadWriteMode getMode() {
      return BReadWriteMode.readonly;
   }

   public void poll() {
      if (!this.isUnoperational() && this.daliPoint != null) {
         if (this.daliPoint.queryValue(this.getDaliDevice())) {
            if (this.daliPoint.isSuccessReal()) {
               this.getDaliDevice().configOk();
            }

            this.readOk(this.daliPoint.getStatusValue());
         } else {
            this.readFail(DaliStatus.toString(this.daliPoint.getDaliStatus()));
         }

         this.updateStatus();
      }
   }

   public void readSubscribed(Context var1) throws Exception {
      BDaliNetwork var2 = (BDaliNetwork)this.getNetwork();
      if (var2 != null) {
         var2.getPollScheduler().subscribe(this);
      }

   }

   public void readUnsubscribed(Context var1) throws Exception {
      BDaliNetwork var2 = (BDaliNetwork)this.getNetwork();
      if (var2 != null) {
         var2.getPollScheduler().unsubscribe(this);
      }

   }

   public boolean write(Context var1) {
      return false;
   }

   public BPointProxyExt() {
     this.daliPoint = null;
   }

   public BPointProxyExt(String var1) {
     this.daliPoint = null;
     
      this.setPointIdentifier(var1);
   }

   static {
      pollFrequency = newProperty(0, BPollFrequency.normal, (BFacets)null);
      pollAssigned = newProperty(5, false, (BFacets)null);
      TYPE = Sys.loadType(BPointProxyExt.class);
   }
}
