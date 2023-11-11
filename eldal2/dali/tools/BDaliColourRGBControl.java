package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
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
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliGroupFolder;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliSpecialCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.DaliArcPowerUtils;

public final class BDaliColourRGBControl extends BComponent {
   public static final Property statusMessage;
   public static final Property enabled;
   public static final Property inputColourRed;
   public static final Property inputColourGreen;
   public static final Property inputColourBlue;
   public static final Property inputColourWhite;
   public static final Property inputColourAmber;
   public static final Property inputColourFreecolour;
   public static final Property outputSelection;
   public static final Property useLogarithmicDimmingCurve;
   public static final Property onlyOutputOnTrigger;
   public static final Property triggerInput;
   public static final Property triggerState;
   public static final Action trigger;
   public static final Type TYPE;
   private static final int OUTPUT_SELECTION_NONE = 0;
   private static final int OUTPUT_SELECTION_RGBWAF = 1;
   private static final int OUTPUT_SELECTION_RGB = 2;
   private static final int OUTPUT_SELECTION_WAF = 3;
   private BDaliNetwork daliNetwork;
   private boolean daliGroupValue;
   private int daliAddressValue;
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

   public final BStatusNumeric getInputColourRed() {
      return (BStatusNumeric)this.get(inputColourRed);
   }

   public final void setInputColourRed(BStatusNumeric var1) {
      this.set(inputColourRed, var1, (Context)null);
   }

   public final BStatusNumeric getInputColourGreen() {
      return (BStatusNumeric)this.get(inputColourGreen);
   }

   public final void setInputColourGreen(BStatusNumeric var1) {
      this.set(inputColourGreen, var1, (Context)null);
   }

   public final BStatusNumeric getInputColourBlue() {
      return (BStatusNumeric)this.get(inputColourBlue);
   }

   public final void setInputColourBlue(BStatusNumeric var1) {
      this.set(inputColourBlue, var1, (Context)null);
   }

   public final BStatusNumeric getInputColourWhite() {
      return (BStatusNumeric)this.get(inputColourWhite);
   }

   public final void setInputColourWhite(BStatusNumeric var1) {
      this.set(inputColourWhite, var1, (Context)null);
   }

   public final BStatusNumeric getInputColourAmber() {
      return (BStatusNumeric)this.get(inputColourAmber);
   }

   public final void setInputColourAmber(BStatusNumeric var1) {
      this.set(inputColourAmber, var1, (Context)null);
   }

   public final BStatusNumeric getInputColourFreecolour() {
      return (BStatusNumeric)this.get(inputColourFreecolour);
   }

   public final void setInputColourFreecolour(BStatusNumeric var1) {
      this.set(inputColourFreecolour, var1, (Context)null);
   }

   public final BStatusEnum getOutputSelection() {
      return (BStatusEnum)this.get(outputSelection);
   }

   public final void setOutputSelection(BStatusEnum var1) {
      this.set(outputSelection, var1, (Context)null);
   }

   public final BStatusBoolean getUseLogarithmicDimmingCurve() {
      return (BStatusBoolean)this.get(useLogarithmicDimmingCurve);
   }

   public final void setUseLogarithmicDimmingCurve(BStatusBoolean var1) {
      this.set(useLogarithmicDimmingCurve, var1, (Context)null);
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

   public final void trigger() {
      this.invoke(trigger, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final Type[] getServiceTypes() {
      return null;
   }

   public final BFacets getSlotFacets(Slot var1) {
      if (var1 == outputSelection) {
         int[] var2 = new int[]{0, 1, 2, 3};
         String[] var3 = new String[]{"Output_None", "Output_RGBWAF", "Output_RGB", "Output_WAF"};
         return BFacets.makeEnum(BEnumRange.make(var2, var3));
      } else {
         return super.getSlotFacets(var1);
      }
   }

   public final void started() throws Exception {
      super.started();
      this.resolveNetworkAccess();
   }

   public final void changed(Property var1, Context var2) {
      super.changed(var1, var2);
      if (this.isRunning() && this.getEnabled()) {
         if (var1 != inputColourRed && var1 != inputColourGreen && var1 != inputColourBlue) {
            if (var1 == triggerInput) {
               boolean var3 = this.getTriggerInput().getValue();
               if (var3 != this.lastTriggerStateValue && var3 == this.getTriggerState()) {
                  this.setColourComponents();
               }

               this.lastTriggerStateValue = var3;
            }
         } else if (!this.getOnlyOutputOnTrigger().getValue()) {
            this.setColourComponents();
         }
      }

   }

   public final void doTrigger() {
      this.setColourComponents();
   }

   public final void setColourComponents() {
      if (this.isRunning() && this.getEnabled()) {
         if (this.daliNetwork == null) {
            this.parentFault();
         } else {
            sendColourRGBWAF(this.daliNetwork, this.daliGroupValue, this.daliAddressValue, this.getOutputSelection().getValue().getOrdinal(), this.convertInput(this.getInputColourRed().getValue()), this.convertInput(this.getInputColourGreen().getValue()), this.convertInput(this.getInputColourBlue().getValue()), this.convertInput(this.getInputColourWhite().getValue()), this.convertInput(this.getInputColourAmber().getValue()), this.convertInput(this.getInputColourFreecolour().getValue()));
         }
      }
   }

   public static final synchronized void sendColourRGBWAF(BDaliNetwork var0, boolean var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      if (var3 != 0) {
         DaliSpecialCommand var10;
         DaliCommand var11;
         if (var3 == 1 || var3 == 2) {
            var10 = new DaliSpecialCommand(var0, 163, var4 & 255);
            var10.execute();
            var10 = new DaliSpecialCommand(var0, 195, var5 & 255);
            var10.execute();
            var10 = new DaliSpecialCommand(var0, 197, var6 & 255);
            var10.execute();
            var10 = new DaliSpecialCommand(var0, 193, 8);
            var10.execute();
            var11 = new DaliCommand(var0, var1, var2, 235);
            var11.execute();
         }

         if (var3 == 1 || var3 == 3) {
            var10 = new DaliSpecialCommand(var0, 163, var7 & 255);
            var10.execute();
            var10 = new DaliSpecialCommand(var0, 195, var8 & 255);
            var10.execute();
            var10 = new DaliSpecialCommand(var0, 197, var9 & 255);
            var10.execute();
            var10 = new DaliSpecialCommand(var0, 193, 8);
            var10.execute();
            var11 = new DaliCommand(var0, var1, var2, 236);
            var11.execute();
         }

         var10 = new DaliSpecialCommand(var0, 193, 8);
         var10.execute();
         var11 = new DaliCommand(var0, var1, var2, 226);
         var11.execute();
      }
   }

   private final int convertInput(double var1) {
      return this.getUseLogarithmicDimmingCurve().getValue() ? DaliArcPowerUtils.percentToDirectLevel(var1) : (int)(254.0 * var1 / 100.0);
   }

   private final void resolveNetworkAccess() {
      this.daliNetwork = null;
      this.daliGroupValue = false;
      this.daliAddressValue = 0;
      BComponent var1 = (BComponent)this.getParent();
      if (var1 instanceof BDaliNetwork) {
         this.daliNetwork = (BDaliNetwork)var1;
         this.daliGroupValue = true;
         this.daliAddressValue = 63;
      } else if (var1 instanceof BDaliGroupFolder) {
         this.daliNetwork = ((BDaliGroupFolder)var1).getDaliNetwork();
         this.daliGroupValue = true;
         this.daliAddressValue = ((BDaliGroupFolder)var1).getDaliGroupAddress();
      } else if (var1 instanceof BBallast) {
         this.daliNetwork = ((BBallast)var1).getDaliNetwork();
         this.daliGroupValue = false;
         this.daliAddressValue = ((BBallast)var1).getDaliAddress();
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

   public BDaliColourRGBControl() {
     this.daliNetwork = null;
     this.daliGroupValue = false;
     this.daliAddressValue = 0;
     this.lastTriggerStateValue = false;
   }

   static {
      statusMessage = newProperty(265, new BStatusString("undefined", BStatus.nullStatus), (BFacets)null);
      enabled = newProperty(256, true, (BFacets)null);
      inputColourRed = newProperty(264, new BStatusNumeric(0.0), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(2), BDouble.make(0.0), BDouble.make(100.0)));
      inputColourGreen = newProperty(264, new BStatusNumeric(0.0), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(2), BDouble.make(0.0), BDouble.make(100.0)));
      inputColourBlue = newProperty(264, new BStatusNumeric(0.0), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(2), BDouble.make(0.0), BDouble.make(100.0)));
      inputColourWhite = newProperty(256, new BStatusNumeric(0.0), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(2), BDouble.make(0.0), BDouble.make(100.0)));
      inputColourAmber = newProperty(256, new BStatusNumeric(0.0), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(2), BDouble.make(0.0), BDouble.make(100.0)));
      inputColourFreecolour = newProperty(256, new BStatusNumeric(0.0), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(2), BDouble.make(0.0), BDouble.make(100.0)));
      outputSelection = newProperty(256, new BStatusEnum(BDynamicEnum.make(1)), (BFacets)null);
      useLogarithmicDimmingCurve = newProperty(256, new BStatusBoolean(true), (BFacets)null);
      onlyOutputOnTrigger = newProperty(256, new BStatusBoolean(false), (BFacets)null);
      triggerInput = newProperty(256, new BStatusBoolean(false, BStatus.nullStatus), (BFacets)null);
      triggerState = newProperty(256, true, (BFacets)null);
      trigger = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BDaliColourRGBControl.class);
   }
}
