package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import javax.baja.status.BIStatus;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.sys.BAbsTime;
import javax.baja.sys.BComponent;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BDaliDeviceStatus extends BComponent implements BIStatus {
   public static final Property status;
   public static final Property faultCause;
   public static final Property auditMessage;
   public static final Property out;
   public static final Property lastUpdateTime;
   public static final Property lastFaultTime;
   public static final Type TYPE;

   public BStatus getStatus() {
      return (BStatus)this.get(status);
   }

   public void setStatus(BStatus var1) {
      this.set(status, var1, (Context)null);
   }

   public String getFaultCause() {
      return this.getString(faultCause);
   }

   public void setFaultCause(String var1) {
      this.setString(faultCause, var1, (Context)null);
   }

   public String getAuditMessage() {
      return this.getString(auditMessage);
   }

   public void setAuditMessage(String var1) {
      this.setString(auditMessage, var1, (Context)null);
   }

   public BStatusBoolean getOut() {
      return (BStatusBoolean)this.get(out);
   }

   public void setOut(BStatusBoolean var1) {
      this.set(out, var1, (Context)null);
   }

   public BAbsTime getLastUpdateTime() {
      return (BAbsTime)this.get(lastUpdateTime);
   }

   public void setLastUpdateTime(BAbsTime var1) {
      this.set(lastUpdateTime, var1, (Context)null);
   }

   public BAbsTime getLastFaultTime() {
      return (BAbsTime)this.get(lastFaultTime);
   }

   public void setLastFaultTime(BAbsTime var1) {
      this.set(lastFaultTime, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   static {
      status = newProperty(329, BStatus.ok, (BFacets)null);
      faultCause = newProperty(325, "", (BFacets)null);
      auditMessage = newProperty(321, "", (BFacets)null);
      out = newProperty(321, new BStatusBoolean(false), (BFacets)null);
      lastUpdateTime = newProperty(321, BAbsTime.DEFAULT, (BFacets)null);
      lastFaultTime = newProperty(321, BAbsTime.DEFAULT, (BFacets)null);
      TYPE = Sys.loadType(BDaliDeviceStatus.class);
   }
}
