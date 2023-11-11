package uk.co.controlnetworksolutions.elitedali2.dali.devices;

import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BDouble;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BScenes;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BStandardCommands;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.IdentifyTask;

public class BBallast extends BGenericDaliDevice {
   public static final Property command;
   public static final Property directLevel;
   public static final Property identifyActive;
   public static final Action sendCommand;
   public static final Action recallScene;
   public static final Action setLevel;
   public static final Action startBallastIdentify;
   public static final Action stopBallastIdentify;
   public static final Type TYPE;
   public static final int STATUS_INVALID = -99;
   private int ballastStatus;
   private IdentifyTask identifyTask;

   public BStatusEnum getCommand() {
      return (BStatusEnum)this.get(command);
   }

   public void setCommand(BStatusEnum var1) {
      this.set(command, var1, (Context)null);
   }

   public BStatusNumeric getDirectLevel() {
      return (BStatusNumeric)this.get(directLevel);
   }

   public void setDirectLevel(BStatusNumeric var1) {
      this.set(directLevel, var1, (Context)null);
   }

   public BStatusBoolean getIdentifyActive() {
      return (BStatusBoolean)this.get(identifyActive);
   }

   public void setIdentifyActive(BStatusBoolean var1) {
      this.set(identifyActive, var1, (Context)null);
   }

   public void sendCommand(BStandardCommands var1) {
      this.invoke(sendCommand, var1, (Context)null);
   }

   public void recallScene(BScenes var1) {
      this.invoke(recallScene, var1, (Context)null);
   }

   public void setLevel(BInteger var1) {
      this.invoke(setLevel, var1, (Context)null);
   }

   public void startBallastIdentify() {
      this.invoke(startBallastIdentify, (BValue)null, (Context)null);
   }

   public void stopBallastIdentify() {
      this.invoke(stopBallastIdentify, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void started() throws Exception {
      super.started();
      this.getConfigPoints().setDaliDeviceType(TYPE);
   }

   public void stopped() throws Exception {
      super.stopped();
      this.doStopBallastIdentify();
   }

   public void changed(Property var1, Context var2) {
      super.changed(var1, var2);
      if (this.isDeviceReady()) {
         if (var1 == command) {
            this.daliCommand(this.getCommand().getEnum().getOrdinal());
         } else if (var1 == directLevel) {
            this.daliLevel(this.getDirectLevel().getValue());
         }
      }

   }

   public void doSendCommand(BStandardCommands var1) {
      this.daliCommand(var1.getOrdinal());
   }

   public void doRecallScene(BScenes var1) {
      this.daliCommand(var1.getOrdinal());
   }

   public void doSetLevel(BInteger var1) {
      this.daliLevel(var1.getDouble());
   }

   public synchronized void doStartBallastIdentify() {
      this.doStopBallastIdentify();
      if (this.isDeviceOk()) {
         if (this.identifyTask != null && !this.identifyTask.isAlive()) {
            this.identifyTask = null;
         }

         if (this.identifyTask == null) {
            this.identifyTask = new IdentifyTask(this);
            this.getIdentifyActive().setValue(true);
            this.identifyTask.start();
         }
      }

   }

   public synchronized void doStopBallastIdentify() {
      if (this.identifyTask != null) {
         this.identifyTask.stop();
         this.identifyTask = null;
      }

      this.getIdentifyActive().setValue(false);
   }

   public void identifyStopped() {
      this.getIdentifyActive().setValue(false);
   }

   public BDaliDeviceType getDaliDeviceType() {
      return BDaliDeviceType.ballast;
   }

   public boolean isBallast() {
      return true;
   }

   public void setBallastStatus(int var1) {
      this.ballastStatus = var1;
   }

   public int getBallastStatus() {
      return this.ballastStatus;
   }

   public BBallast() {
     this.ballastStatus = -99;
     this.identifyTask = null;
   }

   static {
      command = newProperty(264, new BStatusEnum(BStandardCommands.off), BFacets.makeEnum(BEnumRange.make(BStandardCommands.TYPE)));
      directLevel = newProperty(264, new BStatusNumeric(), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(1), BDouble.make(0.0), BDouble.make(100.0)));
      identifyActive = newProperty(257, new BStatusBoolean(false), (BFacets)null);
      sendCommand = newAction(0, BStandardCommands.off, (BFacets)null);
      recallScene = newAction(0, BScenes.scene1, (BFacets)null);
      setLevel = newAction(0, BInteger.make(0), (BFacets)null);
      startBallastIdentify = newAction(0, (BFacets)null);
      stopBallastIdentify = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BBallast.class);
   }
}
