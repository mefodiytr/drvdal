package uk.co.controlnetworksolutions.elitedali2.dali.devices;

import javax.baja.sys.Action;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BEmergencyLightingCommands;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliSpecialCommand;

public final class BEmergencyLighting extends BGenericDaliDevice {
   public static final Property command;
   public static final Action startFunctionTest;
   public static final Action startDurationTest;
   public static final Action stopTest;
   public static final Action startIdentify;
   public static final Action rest;
   public static final Action inhibit;
   public static final Action resetInhibit;
   public static final Action resetFunctionTestFlag;
   public static final Action resetDurationTestFlag;
   public static final Action resetLampTime;
   public static final Action setEmergencyLevel;
   public static final Action setTestDelay;
   public static final Action setFunctionTestInterval;
   public static final Action setDurationTestInterval;
   public static final Action setTestTimeout;
   public static final Action setProlongTime;
   public static final Action setLevel;
   public static final Type TYPE;

   public final BEmergencyLightingCommands getCommand() {
      return (BEmergencyLightingCommands)this.get(command);
   }

   public final void setCommand(BEmergencyLightingCommands var1) {
      this.set(command, var1, (Context)null);
   }

   public final void startFunctionTest() {
      this.invoke(startFunctionTest, (BValue)null, (Context)null);
   }

   public final void startDurationTest() {
      this.invoke(startDurationTest, (BValue)null, (Context)null);
   }

   public final void stopTest() {
      this.invoke(stopTest, (BValue)null, (Context)null);
   }

   public final void startIdentify() {
      this.invoke(startIdentify, (BValue)null, (Context)null);
   }

   public final void rest() {
      this.invoke(rest, (BValue)null, (Context)null);
   }

   public final void inhibit() {
      this.invoke(inhibit, (BValue)null, (Context)null);
   }

   public final void resetInhibit() {
      this.invoke(resetInhibit, (BValue)null, (Context)null);
   }

   public final void resetFunctionTestFlag() {
      this.invoke(resetFunctionTestFlag, (BValue)null, (Context)null);
   }

   public final void resetDurationTestFlag() {
      this.invoke(resetDurationTestFlag, (BValue)null, (Context)null);
   }

   public final void resetLampTime() {
      this.invoke(resetLampTime, (BValue)null, (Context)null);
   }

   public final void setEmergencyLevel(BInteger var1) {
      this.invoke(setEmergencyLevel, var1, (Context)null);
   }

   public final void setTestDelay(BInteger var1) {
      this.invoke(setTestDelay, var1, (Context)null);
   }

   public final void setFunctionTestInterval(BInteger var1) {
      this.invoke(setFunctionTestInterval, var1, (Context)null);
   }

   public final void setDurationTestInterval(BInteger var1) {
      this.invoke(setDurationTestInterval, var1, (Context)null);
   }

   public final void setTestTimeout(BInteger var1) {
      this.invoke(setTestTimeout, var1, (Context)null);
   }

   public final void setProlongTime(BInteger var1) {
      this.invoke(setProlongTime, var1, (Context)null);
   }

   public final void setLevel(BInteger var1) {
      this.invoke(setLevel, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void started() throws Exception {
      super.started();
      this.getConfigPoints().setDaliDeviceType(TYPE);
   }

   public final void doStartFunctionTest() {
      this.daliCommand(227);
   }

   public final void doStartDurationTest() {
      this.daliCommand(228);
   }

   public final void doStopTest() {
      this.daliCommand(229);
   }

   public final void doStartIdentify() {
      this.daliCommand(240);
   }

   public final void doRest() {
      this.daliCommand(224);
   }

   public final void doInhibit() {
      this.daliCommand(225);
   }

   public final void doResetInhibit() {
      this.daliCommand(226);
   }

   public final void doResetFunctionTestFlag() {
      this.daliCommand(230);
   }

   public final void doResetDurationTestFlag() {
      this.daliCommand(231);
   }

   public final void doResetLampTime() {
      this.daliCommand(232);
   }

   public final void doSetEmergencyLevel(BInteger var1) {
      this.storeValue(233, var1.getInt());
   }

   public final void doSetTestDelay(BInteger var1) {
      this.storeValue(234, var1.getInt() >> 8 & 255);
      this.storeValue(235, var1.getInt() & 255);
   }

   public final void doSetFunctionTestInterval(BInteger var1) {
      this.storeValue(236, var1.getInt());
   }

   public final void doSetDurationTestInterval(BInteger var1) {
      this.storeValue(237, var1.getInt());
   }

   public final void doSetTestTimeout(BInteger var1) {
      this.storeValue(238, var1.getInt());
   }

   public final void doSetProlongTime(BInteger var1) {
      this.storeValue(239, var1.getInt());
   }

   public final void doSetLevel(BInteger var1) {
      this.daliLevel(var1.getDouble());
   }

   private final boolean storeValue(int var1, int var2) {
      if (this.isDeviceOk()) {
         DaliSpecialCommand var3 = new DaliSpecialCommand(this.getDaliNetwork(), 163, var2);
         var3.setTransmitRepeatCount(1);
         var3.execute();
         if (var3.getDaliStatus() != 4 && var3.getDaliStatus() != 34) {
            DaliCommand var4 = new DaliCommand(this, var1);
            var4.setTransmitRepeatCount(1);
            var4.execute();
            return var3.getDaliStatus() != 4 && var3.getDaliStatus() != 34;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public final BDaliDeviceType getDaliDeviceType() {
      return BDaliDeviceType.emergencyLighting;
   }

   static {
      command = newProperty(264, BEmergencyLightingCommands.idle, (BFacets)null);
      startFunctionTest = newAction(0, (BFacets)null);
      startDurationTest = newAction(0, (BFacets)null);
      stopTest = newAction(0, (BFacets)null);
      startIdentify = newAction(0, (BFacets)null);
      rest = newAction(0, (BFacets)null);
      inhibit = newAction(0, (BFacets)null);
      resetInhibit = newAction(0, (BFacets)null);
      resetFunctionTestFlag = newAction(0, (BFacets)null);
      resetDurationTestFlag = newAction(0, (BFacets)null);
      resetLampTime = newAction(0, (BFacets)null);
      setEmergencyLevel = newAction(0, BInteger.make(100), (BFacets)null);
      setTestDelay = newAction(0, BInteger.make(0), (BFacets)null);
      setFunctionTestInterval = newAction(0, BInteger.make(0), (BFacets)null);
      setDurationTestInterval = newAction(0, BInteger.make(0), (BFacets)null);
      setTestTimeout = newAction(0, BInteger.make(0), (BFacets)null);
      setProlongTime = newAction(0, BInteger.make(0), (BFacets)null);
      setLevel = newAction(0, BInteger.make(0), (BFacets)null);
      TYPE = Sys.loadType(BEmergencyLighting.class);
   }
}
