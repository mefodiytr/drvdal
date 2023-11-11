package uk.co.controlnetworksolutions.elitedali2.dali.network;

import javax.baja.log.Log;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BComponent;
import javax.baja.sys.BDouble;
import javax.baja.sys.BEnum;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BStandardCommands;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliLevel;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public final class BDaliNetworkBroadcast extends BComponent {
   public static final Property enable = newProperty(256, new BStatusBoolean(true), (BFacets)null);
   public static final Property inputCommand;
   public static final Property lastOutputCommand;
   public static final Property inputLevel;
   public static final Action allDevicesOff;
   public static final Action allDevicesOn;
   public static final Action sendCommand;
   public static final Action setLevel;
   public static final Type TYPE;
   private BDaliNetwork daliNetwork;
   private Log log;

   public final BStatusBoolean getEnable() {
      return (BStatusBoolean)this.get(enable);
   }

   public final void setEnable(BStatusBoolean var1) {
      this.set(enable, var1, (Context)null);
   }

   public final BStatusEnum getInputCommand() {
      return (BStatusEnum)this.get(inputCommand);
   }

   public final void setInputCommand(BStatusEnum var1) {
      this.set(inputCommand, var1, (Context)null);
   }

   public final BStatusEnum getLastOutputCommand() {
      return (BStatusEnum)this.get(lastOutputCommand);
   }

   public final void setLastOutputCommand(BStatusEnum var1) {
      this.set(lastOutputCommand, var1, (Context)null);
   }

   public final BStatusNumeric getInputLevel() {
      return (BStatusNumeric)this.get(inputLevel);
   }

   public final void setInputLevel(BStatusNumeric var1) {
      this.set(inputLevel, var1, (Context)null);
   }

   public final void allDevicesOff() {
      this.invoke(allDevicesOff, (BValue)null, (Context)null);
   }

   public final void allDevicesOn() {
      this.invoke(allDevicesOn, (BValue)null, (Context)null);
   }

   public final void sendCommand(BStandardCommands var1) {
      this.invoke(sendCommand, var1, (Context)null);
   }

   public final void setLevel(BInteger var1) {
      this.invoke(setLevel, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void started() throws Exception {
      super.started();
      this.resolveNetworkAccess();
   }

   public final void changed(Property var1, Context var2) {
      if (this.daliNetwork != null && this.getEnable().getValue() && this.daliNetwork.isNetworkOk()) {
         if (var1 == inputCommand) {
            this.broadcastCommand(this.getInputCommand().getValue());
         } else if (var1 == inputLevel) {
            this.broadcastDirectLevel(this.getInputLevel().getValue());
         }
      }

   }

   public final void doAllDevicesOff() {
      this.broadcastCommand(BStandardCommands.off);
   }

   public final void doAllDevicesOn() {
      this.broadcastDirectLevel(100.0);
   }

   public final void doSendCommand(BStandardCommands var1) {
      this.broadcastCommand(var1);
   }

   public final void doSetLevel(BInteger var1) {
      this.broadcastDirectLevel(var1.getDouble());
   }

   public final void broadcastCommand(BEnum var1) {
      if (this.daliNetwork != null && this.getEnable().getValue() && var1.getOrdinal() != 255 && this.daliNetwork.isNetworkOk()) {
         this.daliNetwork.broadcastCommand(var1.getOrdinal());
         this.getLastOutputCommand().setValue(var1);
      }

   }

   public final void broadcastDirectLevel(double var1) {
      if (this.daliNetwork != null && this.getEnable().getValue() && this.daliNetwork.isNetworkOk()) {
         if (var1 < 0.0) {
            var1 = 0.0;
         }

         if (var1 > 100.0) {
            var1 = 100.0;
         }

         DaliLevel var3 = new DaliLevel(this.daliNetwork, true, 63, var1);
         var3.setTransmitRepeatCount(this.daliNetwork.getTransmitCount() - 1);
         var3.execute();
      }

   }

   private final void resolveNetworkAccess() {
      BComponent var1 = (BComponent)this.getParent();
      if (var1.getType() == BDaliNetwork.TYPE) {
         this.daliNetwork = (BDaliNetwork)var1;
      } else {
         this.log.error("BDaliNetworkBroadcast parent is not a DALI network");
      }

   }

   public BDaliNetworkBroadcast() {
     this.daliNetwork = null;
     this.log = Report.daliNetwork;
   }

   static {
      inputCommand = newProperty(1288, new BStatusEnum(BStandardCommands.off), BFacets.makeEnum(BEnumRange.make(BStandardCommands.TYPE)));
      lastOutputCommand = newProperty(5, new BStatusEnum(BStandardCommands.off), BFacets.makeEnum(BEnumRange.make(BStandardCommands.TYPE)));
      inputLevel = newProperty(1288, new BStatusNumeric(0.0), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      allDevicesOff = newAction(0, (BFacets)null);
      allDevicesOn = newAction(0, (BFacets)null);
      sendCommand = newAction(0, BStandardCommands.off, (BFacets)null);
      setLevel = newAction(0, BInteger.make(0), (BFacets)null);
      TYPE = Sys.loadType(BDaliNetworkBroadcast.class);
   }
}
