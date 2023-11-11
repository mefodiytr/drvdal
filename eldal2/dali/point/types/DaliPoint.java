package uk.co.controlnetworksolutions.elitedali2.dali.point.types;

import javax.baja.control.BControlPoint;
import javax.baja.driver.util.BPollFrequency;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BFacets;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliStatus;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPoint;

public abstract class DaliPoint {
   protected String daliPointIdentifier;
   protected int daliQueryCode;
   private int daliStatus;
   private int daliQueryResult;

   public abstract Type getPointType();

   public abstract BControlPoint getProxyPoint();

   public abstract BVirtualPoint getVirtualPoint();

   public abstract boolean processDaliQuery(int var1, int var2);

   public abstract BStatusValue getStatusValue();

   public String getIdentifier() {
      return this.daliPointIdentifier;
   }

   public int getQueryCode() {
      return this.daliQueryCode;
   }

   public boolean queryValue(BGenericDaliDevice var1) {
      boolean var2 = false;
      this.daliQueryResult = -1;
      DaliQuery var3 = new DaliQuery(var1, this.getQueryCode());
      var3.confirmResult();
      this.daliStatus = var3.getDaliStatus();
      if (this.daliStatus == 53) {
         var1.pingOk();
      }

      if (this.daliStatus != 53 && this.daliStatus != 1) {
         var2 = false;
      } else {
         this.daliQueryResult = var3.getResult();
         if (this.processDaliQuery(this.daliStatus, this.daliQueryResult)) {
            if (this.isSuccessReal()) {
               var1.configOk();
            }

            var2 = true;
         } else {
            var1.putDeviceInFault(DaliStatus.toString(this.daliStatus));
            var2 = false;
         }
      }

      return var2;
   }

   public int getDaliStatus() {
      return this.daliStatus;
   }

   public int getQueryResult() {
      return this.daliQueryResult;
   }

   public boolean isSuccessReal() {
      return true;
   }

   public boolean writeValue(BGenericDaliDevice var1, boolean var2) {
      return false;
   }

   public boolean writeValue(BGenericDaliDevice var1, int var2) {
      return false;
   }

   public boolean writeValue(BGenericDaliDevice var1, double var2) {
      return false;
   }

   public boolean writeNull(BGenericDaliDevice var1) {
      return false;
   }

   public BFacets getFacets() {
      return BFacets.NULL;
   }

   public BPollFrequency getPollFrequency() {
      return BPollFrequency.normal;
   }

   public DaliPoint(String var1, int var2) {
     this.daliPointIdentifier = null;
     this.daliQueryCode = -1;
     this.daliStatus = 69;
     this.daliQueryResult = -1;
     
      this.daliPointIdentifier = var1;
      this.daliQueryCode = var2;
   }
}
