package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import javax.baja.status.BIStatus;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusString;
import javax.baja.sys.Action;
import javax.baja.sys.BComponent;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Slot;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliGroupFolder;

public final class BDaliTrigger extends BComponent implements BIStatus {
   public static final Property statusMessage;
   public static final Property triggerInput;
   public static final Property triggerMode;
   public static final Property triggerState;
   public static final Action trigger;
   public static final Type TYPE;
   public static final int TRIGGER_MODE_COMMAND = 11;
   public static final int TRIGGER_MODE_LEVEL = 22;
   private BDaliGroupFolder localDaliGroup;
   private BBallast localDaliBallast;
   private boolean lastStateValue;

   public final BStatusString getStatusMessage() {
      return (BStatusString)this.get(statusMessage);
   }

   public final void setStatusMessage(BStatusString var1) {
      this.set(statusMessage, var1, (Context)null);
   }

   public final BStatusBoolean getTriggerInput() {
      return (BStatusBoolean)this.get(triggerInput);
   }

   public final void setTriggerInput(BStatusBoolean var1) {
      this.set(triggerInput, var1, (Context)null);
   }

   public final BStatusEnum getTriggerMode() {
      return (BStatusEnum)this.get(triggerMode);
   }

   public final void setTriggerMode(BStatusEnum var1) {
      this.set(triggerMode, var1, (Context)null);
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

   public final BFacets getSlotFacets(Slot var1) {
      if (var1 == triggerMode) {
         int[] var2 = new int[]{11, 22};
         String[] var3 = new String[]{"Trigger_Mode_Command", "Trigger_Mode_Direct_Level"};
         return BFacets.makeEnum(BEnumRange.make(var2, var3));
      } else {
         return super.getSlotFacets(var1);
      }
   }

   public final void started() {
      this.localDaliGroup = null;
      this.localDaliBallast = null;
      BComponent var1 = (BComponent)this.getParent();
      if (var1 instanceof BDaliGroupFolder) {
         this.localDaliGroup = (BDaliGroupFolder)var1;
         this.getStatusMessage().setValue("OK");
         this.getStatusMessage().setStatus(BStatus.ok);
      } else if (var1 instanceof BBallast) {
         this.localDaliBallast = (BBallast)var1;
         this.getStatusMessage().setValue("OK");
         this.getStatusMessage().setStatus(BStatus.ok);
      } else {
         this.parentFault();
      }

   }

   public final void changed(Property var1, Context var2) {
      if (var1 == triggerInput) {
         boolean var3 = this.getTriggerInput().getValue();
         if (var3 != this.lastStateValue && var3 == this.getTriggerState()) {
            this.doTrigger();
         }

         this.lastStateValue = var3;
      }

   }

   public final void doTrigger() {
      if (this.localDaliGroup != null) {
         if (this.getTriggerMode().getValue().getOrdinal() == 11) {
            this.localDaliGroup.groupCommand(this.localDaliGroup.getCommand().getEnum().getOrdinal());
         }

         if (this.getTriggerMode().getValue().getOrdinal() == 22) {
            this.localDaliGroup.groupDirectLevel(this.localDaliGroup.getDirectLevel().getValue());
         }
      } else if (this.localDaliBallast != null) {
         if (this.getTriggerMode().getValue().getOrdinal() == 11) {
            this.localDaliBallast.daliCommand(this.localDaliBallast.getCommand().getEnum().getOrdinal());
         }

         if (this.getTriggerMode().getValue().getOrdinal() == 22) {
            this.localDaliBallast.daliLevel(this.localDaliBallast.getDirectLevel().getValue());
         }
      } else {
         this.parentFault();
      }

   }

   public final BStatus getStatus() {
      return this.localDaliGroup == null && this.localDaliBallast == null ? BStatus.fault : BStatus.ok;
   }

   private final void parentFault() {
      this.setStatusMessage(new BStatusString("ERROR: Parent is not a DALI group or DALI ballast", BStatus.fault));
   }

   public BDaliTrigger() {
     this.localDaliGroup = null;
     this.localDaliBallast = null;
     this.lastStateValue = false;
   }

   static {
      statusMessage = newProperty(265, new BStatusString("undefined", BStatus.nullStatus), (BFacets)null);
      triggerInput = newProperty(1288, new BStatusBoolean(false, BStatus.nullStatus), (BFacets)null);
      triggerMode = newProperty(256, new BStatusEnum(BDynamicEnum.make(22)), (BFacets)null);
      triggerState = newProperty(256, false, (BFacets)null);
      trigger = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BDaliTrigger.class);
   }
}
