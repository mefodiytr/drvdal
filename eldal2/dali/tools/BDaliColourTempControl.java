package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusString;
import javax.baja.sys.Action;
import javax.baja.sys.BComponent;
import javax.baja.sys.BDouble;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliGroupFolder;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliSpecialCommand;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public final class BDaliColourTempControl extends BComponent {
   public static final Property statusMessage;
   public static final Property enabled;
   public static final Property inputColourTemp;
   public static final Property minColourTemp;
   public static final Property maxColourTemp;
   public static final Property outputColourTemp;
   public static final Property outputMirek;
   public static final Property onlyOutputOnTrigger;
   public static final Property triggerInput;
   public static final Property triggerState;
   public static final Action setColourTemp;
   public static final Action trigger;
   public static final Action queryDeviceLimits;
   public static final Type TYPE;
   public static final int MIN_MIREK_VALUE = 1;
   public static final int MAX_MIREK_VALUE = 65535;
   private BDaliNetwork daliNetwork;
   private boolean daliGroupValue;
   private int daliAddressValue;
   private BBallast localDaliBallast;
   private boolean lastTriggerStateValue;

   public final BStatusString getStatusMessage() {
      return (BStatusString)this.get(statusMessage);
   }

   public final void setStatusMessage(BStatusString var1) {
      this.set(statusMessage, var1, (Context)null);
   }

   public final boolean getEnabled() {
      return this.getBoolean(enabled);
   }

   public final void setEnabled(boolean var1) {
      this.setBoolean(enabled, var1, (Context)null);
   }

   public final BStatusNumeric getInputColourTemp() {
      return (BStatusNumeric)this.get(inputColourTemp);
   }

   public final void setInputColourTemp(BStatusNumeric var1) {
      this.set(inputColourTemp, var1, (Context)null);
   }

   public final BStatusNumeric getMinColourTemp() {
      return (BStatusNumeric)this.get(minColourTemp);
   }

   public final void setMinColourTemp(BStatusNumeric var1) {
      this.set(minColourTemp, var1, (Context)null);
   }

   public final BStatusNumeric getMaxColourTemp() {
      return (BStatusNumeric)this.get(maxColourTemp);
   }

   public final void setMaxColourTemp(BStatusNumeric var1) {
      this.set(maxColourTemp, var1, (Context)null);
   }

   public final BStatusNumeric getOutputColourTemp() {
      return (BStatusNumeric)this.get(outputColourTemp);
   }

   public final void setOutputColourTemp(BStatusNumeric var1) {
      this.set(outputColourTemp, var1, (Context)null);
   }

   public final BStatusNumeric getOutputMirek() {
      return (BStatusNumeric)this.get(outputMirek);
   }

   public final void setOutputMirek(BStatusNumeric var1) {
      this.set(outputMirek, var1, (Context)null);
   }

   public final BStatusBoolean getOnlyOutputOnTrigger() {
      return (BStatusBoolean)this.get(onlyOutputOnTrigger);
   }

   public final void setOnlyOutputOnTrigger(BStatusBoolean var1) {
      this.set(onlyOutputOnTrigger, var1, (Context)null);
   }

   public final BStatusBoolean getTriggerInput() {
      return (BStatusBoolean)this.get(triggerInput);
   }

   public final void setTriggerInput(BStatusBoolean var1) {
      this.set(triggerInput, var1, (Context)null);
   }

   public final boolean getTriggerState() {
      return this.getBoolean(triggerState);
   }

   public final void setTriggerState(boolean var1) {
      this.setBoolean(triggerState, var1, (Context)null);
   }

   public final void setColourTemp(BInteger var1) {
      this.invoke(setColourTemp, var1, (Context)null);
   }

   public final void trigger() {
      this.invoke(trigger, (BValue)null, (Context)null);
   }

   public final void queryDeviceLimits() {
      this.invoke(queryDeviceLimits, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void started() throws Exception {
      super.started();
      this.resolveNetworkAccess();
   }

   public final void changed(Property var1, Context var2) {
      super.changed(var1, var2);
      if (this.isRunning() && this.getEnabled()) {
         if (var1 == inputColourTemp) {
            if (!this.getOnlyOutputOnTrigger().getValue()) {
               this.setColourValue();
            }
         } else if (var1 == triggerInput) {
            boolean var3 = this.getTriggerInput().getValue();
            if (var3 != this.lastTriggerStateValue && var3 == this.getTriggerState()) {
               this.setColourValue();
            }

            this.lastTriggerStateValue = var3;
         }
      }

   }

   public final void doSetColourTemp(BInteger var1) {
      this.sendColourTemp(this.daliNetwork, this.daliGroupValue, this.daliAddressValue, var1.getDouble());
   }

   public final void doTrigger() {
      this.setColourValue();
   }

   public final void doQueryDeviceLimits() {
      if (this.isRunning() && this.getEnabled()) {
         if (this.localDaliBallast == null) {
            Report.daliDevice.error("Can only read Colour Control limits from a DALI device");
         } else {
            int var1 = this.queryColourValue(128);
            if (var1 >= 0) {
               Report.daliDevice.message("Colour Control limit [" + this.localDaliBallast.getName() + "] Coolest = " + (int)(1000000.0 / (double)var1));
            } else {
               Report.daliDevice.error("Could not read Colour Control limit (coolest) from DALI device");
            }

            var1 = this.queryColourValue(130);
            if (var1 >= 0) {
               Report.daliDevice.message("Colour Control limit [" + this.localDaliBallast.getName() + "] Warmest = " + (int)(1000000.0 / (double)var1));
            } else {
               Report.daliDevice.error("Could not read Colour Control limit (warmest) from DALI device");
            }

         }
      }
   }

   public final int queryColourValue(int var1) {
      if (this.localDaliBallast == null) {
         Report.daliDevice.error("Can only read Colour Control limits from a DALI device");
         return -1;
      } else {
         DaliSpecialCommand var2 = new DaliSpecialCommand(this.localDaliBallast.getDaliNetwork(), 163, var1);
         var2.execute();
         var2 = new DaliSpecialCommand(this.localDaliBallast.getDaliNetwork(), 193, 8);
         var2.execute();
         DaliQuery var6 = new DaliQuery(this.localDaliBallast.getDaliNetwork(), this.localDaliBallast.getDaliAddress(), 250);
         var6.execute();
         int var3 = var6.getResult();
         if (var3 < 0) {
            return -1;
         } else {
            var6 = new DaliQuery(this.localDaliBallast.getDaliNetwork(), this.localDaliBallast.getDaliAddress(), 152);
            var6.execute();
            int var4 = var6.getResult();
            if (var4 < 0) {
               return -1;
            } else {
               var6 = new DaliQuery(this.localDaliBallast.getDaliNetwork(), this.localDaliBallast.getDaliAddress(), 156);
               var6.execute();
               int var5 = var6.getResult();
               return var5 < 0 ? -1 : var5 << 8 | var4;
            }
         }
      }
   }

   public final void setColourValue() {
      this.sendColourTemp(this.daliNetwork, this.daliGroupValue, this.daliAddressValue, this.getInputColourTemp().getValue());
   }

   public final void sendColourTemp(BDaliNetwork var1, boolean var2, int var3, double var4) {
      if (this.isRunning() && this.getEnabled()) {
         if (this.daliNetwork == null) {
            this.parentFault();
         } else {
            if (var4 < this.getMinColourTemp().getValue()) {
               var4 = this.getMinColourTemp().getValue();
            }

            if (var4 > this.getMaxColourTemp().getValue()) {
               var4 = this.getMaxColourTemp().getValue();
            }

            int var6 = (int)(1000000.0 / var4);
            if (var6 < 1) {
               var6 = 1;
            }

            if (var6 > (char)-1) {
               var6 = (char)-1;
            }

            DaliSpecialCommand var7 = new DaliSpecialCommand(var1, 195, var6 >> 8 & 255);
            var7.execute();
            var7 = new DaliSpecialCommand(var1, 163, var6 & 255);
            var7.execute();
            var7 = new DaliSpecialCommand(var1, 193, 8);
            var7.execute();
            DaliCommand var8 = new DaliCommand(var1, var2, var3, 231);
            var8.execute();
            var7 = new DaliSpecialCommand(var1, 193, 8);
            var7.execute();
            var8 = new DaliCommand(var1, var2, var3, 226);
            var8.execute();
            this.getOutputMirek().setValue((double)var6);
            this.getOutputMirek().setStatusNull(false);
            this.getOutputColourTemp().setValue(var4);
            this.getOutputColourTemp().setStatusNull(false);
         }
      }
   }

   private final void resolveNetworkAccess() {
      this.daliNetwork = null;
      this.daliGroupValue = false;
      this.daliAddressValue = 0;
      this.localDaliBallast = null;
      BComponent var1 = (BComponent)this.getParent();
      if (var1 instanceof BDaliNetwork) {
         this.daliNetwork = (BDaliNetwork)var1;
         this.daliGroupValue = true;
         this.daliAddressValue = 63;
         this.localDaliBallast = null;
      } else if (var1 instanceof BDaliGroupFolder) {
         this.daliNetwork = ((BDaliGroupFolder)var1).getDaliNetwork();
         this.daliGroupValue = true;
         this.daliAddressValue = ((BDaliGroupFolder)var1).getDaliGroupAddress();
         this.localDaliBallast = null;
      } else if (var1 instanceof BBallast) {
         this.daliNetwork = ((BBallast)var1).getDaliNetwork();
         this.daliGroupValue = false;
         this.daliAddressValue = ((BBallast)var1).getDaliAddress();
         this.localDaliBallast = (BBallast)var1;
      }

      if (this.daliNetwork != null) {
         this.getStatusMessage().setValue("OK");
         this.getStatusMessage().setStatus(BStatus.ok);
      } else {
         this.parentFault();
      }

   }

   private final void parentFault() {
      this.setStatusMessage(new BStatusString("ERROR: Parent is not a DALI network, group or device", BStatus.fault));
   }

   public BDaliColourTempControl() {
     this.daliNetwork = null;
     this.daliGroupValue = false;
     this.daliAddressValue = 0;
     this.localDaliBallast = null;
     this.lastTriggerStateValue = false;
   }

   static {
      statusMessage = newProperty(265, new BStatusString("undefined", BStatus.nullStatus), (BFacets)null);
      enabled = newProperty(256, true, (BFacets)null);
      inputColourTemp = newProperty(264, new BStatusNumeric(2500.0), BFacets.makeNumeric(UnitDatabase.getUnit("kelvin"), BInteger.make(1), BDouble.make(15.0), BDouble.make(1000000.0)));
      minColourTemp = newProperty(256, new BStatusNumeric(15.0), BFacets.makeNumeric(UnitDatabase.getUnit("kelvin"), BInteger.make(1), BDouble.make(15.0), BDouble.make(1000000.0)));
      maxColourTemp = newProperty(256, new BStatusNumeric(1000000.0), BFacets.makeNumeric(UnitDatabase.getUnit("kelvin"), BInteger.make(1), BDouble.make(15.0), BDouble.make(1000000.0)));
      outputColourTemp = newProperty(257, new BStatusNumeric(2500.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("kelvin"), 1));
      outputMirek = newProperty(257, new BStatusNumeric(400.0, BStatus.nullStatus), BFacets.makeNumeric(0));
      onlyOutputOnTrigger = newProperty(256, new BStatusBoolean(false), (BFacets)null);
      triggerInput = newProperty(256, new BStatusBoolean(false, BStatus.nullStatus), (BFacets)null);
      triggerState = newProperty(256, true, (BFacets)null);
      setColourTemp = newAction(0, BInteger.make(2000), (BFacets)null);
      trigger = newAction(0, (BFacets)null);
      queryDeviceLimits = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BDaliColourTempControl.class);
   }
}
