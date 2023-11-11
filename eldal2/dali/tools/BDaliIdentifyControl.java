package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import javax.baja.status.BIStatus;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusString;
import javax.baja.sys.Action;
import javax.baja.sys.BComponent;
import javax.baja.sys.BDouble;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Slot;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;

public final class BDaliIdentifyControl extends BComponent implements BIStatus {
   public static final Property statusMessage;
   public static final Property identifyOnPeriod;
   public static final Property identifyOffPeriod;
   public static final Property identifyMaxTime;
   public static final Property identifyMode;
   public static final Action abortAllIdentifyTasks;
   public static final Type TYPE;
   private BDaliNetwork localDaliNetwork;

   public final BStatusString getStatusMessage() {
      return (BStatusString)this.get(statusMessage);
   }

   public final void setStatusMessage(BStatusString var1) {
      this.set(statusMessage, var1, (Context)null);
   }

   public final BStatusNumeric getIdentifyOnPeriod() {
      return (BStatusNumeric)this.get(identifyOnPeriod);
   }

   public final void setIdentifyOnPeriod(BStatusNumeric var1) {
      this.set(identifyOnPeriod, var1, (Context)null);
   }

   public final BStatusNumeric getIdentifyOffPeriod() {
      return (BStatusNumeric)this.get(identifyOffPeriod);
   }

   public final void setIdentifyOffPeriod(BStatusNumeric var1) {
      this.set(identifyOffPeriod, var1, (Context)null);
   }

   public final BStatusNumeric getIdentifyMaxTime() {
      return (BStatusNumeric)this.get(identifyMaxTime);
   }

   public final void setIdentifyMaxTime(BStatusNumeric var1) {
      this.set(identifyMaxTime, var1, (Context)null);
   }

   public final BStatusEnum getIdentifyMode() {
      return (BStatusEnum)this.get(identifyMode);
   }

   public final void setIdentifyMode(BStatusEnum var1) {
      this.set(identifyMode, var1, (Context)null);
   }

   public final void abortAllIdentifyTasks() {
      this.invoke(abortAllIdentifyTasks, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final BFacets getSlotFacets(Slot var1) {
      if (var1 == identifyMode) {
         int[] var2 = new int[]{1, 2, 3};
         String[] var3 = new String[]{"Identify_Mode_Level", "Identify_Mode_Max_Off", "Identify_Mode_Max_Min"};
         return BFacets.makeEnum(BEnumRange.make(var2, var3));
      } else {
         return super.getSlotFacets(var1);
      }
   }

   public final void started() {
      this.localDaliNetwork = null;
      BComponent var1 = (BComponent)this.getParent();
      if (var1 instanceof BDaliNetwork) {
         this.localDaliNetwork = (BDaliNetwork)var1;
         this.getStatusMessage().setValue("OK");
         this.getStatusMessage().setStatus(BStatus.ok);
      } else if (var1 instanceof BGenericDaliDevice) {
         this.localDaliNetwork = ((BGenericDaliDevice)var1).getDaliNetwork();
         this.getStatusMessage().setValue("OK");
         this.getStatusMessage().setStatus(BStatus.ok);
      } else {
         this.setStatusMessage(new BStatusString("ERROR: Parent is not a DALI network or DALI device", BStatus.fault));
      }

   }

   public final BStatus getStatus() {
      return this.localDaliNetwork == null ? BStatus.fault : BStatus.ok;
   }

   public final void doAbortAllIdentifyTasks() {
      if (this.localDaliNetwork != null) {
         this.localDaliNetwork.setIdentifyAbortFlag(true);
      }

   }

   private final void setLocalDaliNetwork(BDaliNetwork var1) {
      this.localDaliNetwork = var1;
   }

   private final BDaliNetwork getLocalDaliNetwork() {
      return this.localDaliNetwork;
   }

   public static final BDaliIdentifyControl findInstance(BComponent var0) {
      if (var0 == null) {
         return null;
      } else {
         BComponent[] var1 = var0.getChildComponents();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] instanceof BDaliIdentifyControl) {
               return (BDaliIdentifyControl)var1[var2];
            }
         }

         return null;
      }
   }

   public BDaliIdentifyControl() {
     this.localDaliNetwork = null;
   }

   static {
      statusMessage = newProperty(265, new BStatusString("undefined", BStatus.nullStatus), (BFacets)null);
      identifyOnPeriod = newProperty(256, new BStatusNumeric(1.0), BFacets.makeNumeric(UnitDatabase.getUnit("second"), BInteger.make(1), BDouble.make(1.0), BDouble.make(1000.0)));
      identifyOffPeriod = newProperty(256, new BStatusNumeric(1.0), BFacets.makeNumeric(UnitDatabase.getUnit("second"), BInteger.make(1), BDouble.make(1.0), BDouble.make(1000.0)));
      identifyMaxTime = newProperty(256, new BStatusNumeric(600.0), BFacets.makeNumeric(UnitDatabase.getUnit("second"), BInteger.make(0), BDouble.make(1.0), BDouble.make(100000.0)));
      identifyMode = newProperty(256, new BStatusEnum(BDynamicEnum.make(1), BStatus.nullStatus), (BFacets)null);
      abortAllIdentifyTasks = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BDaliIdentifyControl.class);
   }
}
