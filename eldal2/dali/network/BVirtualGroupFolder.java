package uk.co.controlnetworksolutions.elitedali2.dali.network;

import javax.baja.driver.BDeviceFolder;
import javax.baja.log.Log;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusString;
import javax.baja.sys.Action;
import javax.baja.sys.BComponent;
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
import uk.co.controlnetworksolutions.elitedali2.dali.config.BDaliDeviceConfigurationJob;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BFadeRate;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BFadeTime;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BScenes;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BStandardCommands;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliLevel;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliWrite;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.DaliArcPowerUtils;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.IdentifyTask;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public final class BVirtualGroupFolder extends BDeviceFolder implements BallastGroupFolder {
   public static final Property groupName = newProperty(256, new BStatusString(), (BFacets)null);
   public static final Property groupBallastCount = newProperty(257, new BStatusNumeric(), (BFacets)null);
   public static final Property scene1;
   public static final Property scene2;
   public static final Property scene3;
   public static final Property scene4;
   public static final Property scene5;
   public static final Property scene6;
   public static final Property scene7;
   public static final Property scene8;
   public static final Property scene9;
   public static final Property scene10;
   public static final Property scene11;
   public static final Property scene12;
   public static final Property scene13;
   public static final Property scene14;
   public static final Property scene15;
   public static final Property scene16;
   public static final Property fadeRate;
   public static final Property fadeTime;
   public static final Property minLevel;
   public static final Property maxLevel;
   public static final Property powerOnLevel;
   public static final Property systemFailureLevel;
   public static final Property command;
   public static final Property directLevel;
   public static final Action sendCommand;
   public static final Action recallScene;
   public static final Action setLevel;
   public static final Action allDevicesOff;
   public static final Action allDevicesOn;
   public static final Action startGroupIdentify;
   public static final Action stopGroupIdentify;
   public static final Action reprogram;
   public static final Type TYPE;
   private BDaliDeviceConfigurationJob devCfgJob;
   private IdentifyTask identifyTask;
   private Log log;

   public final BStatusString getGroupName() {
      return (BStatusString)this.get(groupName);
   }

   public final void setGroupName(BStatusString var1) {
      this.set(groupName, var1, (Context)null);
   }

   public final BStatusNumeric getGroupBallastCount() {
      return (BStatusNumeric)this.get(groupBallastCount);
   }

   public final void setGroupBallastCount(BStatusNumeric var1) {
      this.set(groupBallastCount, var1, (Context)null);
   }

   public final BStatusNumeric getScene1() {
      return (BStatusNumeric)this.get(scene1);
   }

   public final void setScene1(BStatusNumeric var1) {
      this.set(scene1, var1, (Context)null);
   }

   public final BStatusNumeric getScene2() {
      return (BStatusNumeric)this.get(scene2);
   }

   public final void setScene2(BStatusNumeric var1) {
      this.set(scene2, var1, (Context)null);
   }

   public final BStatusNumeric getScene3() {
      return (BStatusNumeric)this.get(scene3);
   }

   public final void setScene3(BStatusNumeric var1) {
      this.set(scene3, var1, (Context)null);
   }

   public final BStatusNumeric getScene4() {
      return (BStatusNumeric)this.get(scene4);
   }

   public final void setScene4(BStatusNumeric var1) {
      this.set(scene4, var1, (Context)null);
   }

   public final BStatusNumeric getScene5() {
      return (BStatusNumeric)this.get(scene5);
   }

   public final void setScene5(BStatusNumeric var1) {
      this.set(scene5, var1, (Context)null);
   }

   public final BStatusNumeric getScene6() {
      return (BStatusNumeric)this.get(scene6);
   }

   public final void setScene6(BStatusNumeric var1) {
      this.set(scene6, var1, (Context)null);
   }

   public final BStatusNumeric getScene7() {
      return (BStatusNumeric)this.get(scene7);
   }

   public final void setScene7(BStatusNumeric var1) {
      this.set(scene7, var1, (Context)null);
   }

   public final BStatusNumeric getScene8() {
      return (BStatusNumeric)this.get(scene8);
   }

   public final void setScene8(BStatusNumeric var1) {
      this.set(scene8, var1, (Context)null);
   }

   public final BStatusNumeric getScene9() {
      return (BStatusNumeric)this.get(scene9);
   }

   public final void setScene9(BStatusNumeric var1) {
      this.set(scene9, var1, (Context)null);
   }

   public final BStatusNumeric getScene10() {
      return (BStatusNumeric)this.get(scene10);
   }

   public final void setScene10(BStatusNumeric var1) {
      this.set(scene10, var1, (Context)null);
   }

   public final BStatusNumeric getScene11() {
      return (BStatusNumeric)this.get(scene11);
   }

   public final void setScene11(BStatusNumeric var1) {
      this.set(scene11, var1, (Context)null);
   }

   public final BStatusNumeric getScene12() {
      return (BStatusNumeric)this.get(scene12);
   }

   public final void setScene12(BStatusNumeric var1) {
      this.set(scene12, var1, (Context)null);
   }

   public final BStatusNumeric getScene13() {
      return (BStatusNumeric)this.get(scene13);
   }

   public final void setScene13(BStatusNumeric var1) {
      this.set(scene13, var1, (Context)null);
   }

   public final BStatusNumeric getScene14() {
      return (BStatusNumeric)this.get(scene14);
   }

   public final void setScene14(BStatusNumeric var1) {
      this.set(scene14, var1, (Context)null);
   }

   public final BStatusNumeric getScene15() {
      return (BStatusNumeric)this.get(scene15);
   }

   public final void setScene15(BStatusNumeric var1) {
      this.set(scene15, var1, (Context)null);
   }

   public final BStatusNumeric getScene16() {
      return (BStatusNumeric)this.get(scene16);
   }

   public final void setScene16(BStatusNumeric var1) {
      this.set(scene16, var1, (Context)null);
   }

   public final BStatusEnum getFadeRate() {
      return (BStatusEnum)this.get(fadeRate);
   }

   public final void setFadeRate(BStatusEnum var1) {
      this.set(fadeRate, var1, (Context)null);
   }

   public final BStatusEnum getFadeTime() {
      return (BStatusEnum)this.get(fadeTime);
   }

   public final void setFadeTime(BStatusEnum var1) {
      this.set(fadeTime, var1, (Context)null);
   }

   public final BStatusNumeric getMinLevel() {
      return (BStatusNumeric)this.get(minLevel);
   }

   public final void setMinLevel(BStatusNumeric var1) {
      this.set(minLevel, var1, (Context)null);
   }

   public final BStatusNumeric getMaxLevel() {
      return (BStatusNumeric)this.get(maxLevel);
   }

   public final void setMaxLevel(BStatusNumeric var1) {
      this.set(maxLevel, var1, (Context)null);
   }

   public final BStatusNumeric getPowerOnLevel() {
      return (BStatusNumeric)this.get(powerOnLevel);
   }

   public final void setPowerOnLevel(BStatusNumeric var1) {
      this.set(powerOnLevel, var1, (Context)null);
   }

   public final BStatusNumeric getSystemFailureLevel() {
      return (BStatusNumeric)this.get(systemFailureLevel);
   }

   public final void setSystemFailureLevel(BStatusNumeric var1) {
      this.set(systemFailureLevel, var1, (Context)null);
   }

   public final BStatusEnum getCommand() {
      return (BStatusEnum)this.get(command);
   }

   public final void setCommand(BStatusEnum var1) {
      this.set(command, var1, (Context)null);
   }

   public final BStatusNumeric getDirectLevel() {
      return (BStatusNumeric)this.get(directLevel);
   }

   public final void setDirectLevel(BStatusNumeric var1) {
      this.set(directLevel, var1, (Context)null);
   }

   public final void sendCommand(BStandardCommands var1) {
      this.invoke(sendCommand, var1, (Context)null);
   }

   public final void recallScene(BScenes var1) {
      this.invoke(recallScene, var1, (Context)null);
   }

   public final void setLevel(BInteger var1) {
      this.invoke(setLevel, var1, (Context)null);
   }

   public final void allDevicesOff() {
      this.invoke(allDevicesOff, (BValue)null, (Context)null);
   }

   public final void allDevicesOn() {
      this.invoke(allDevicesOn, (BValue)null, (Context)null);
   }

   public final void startGroupIdentify() {
      this.invoke(startGroupIdentify, (BValue)null, (Context)null);
   }

   public final void stopGroupIdentify() {
      this.invoke(stopGroupIdentify, (BValue)null, (Context)null);
   }

   public final void reprogram() {
      this.invoke(reprogram, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void started() throws Exception {
      super.started();
      this.setGroupBallastCount(new BStatusNumeric(0.0, BStatus.nullStatus));
      BComponent[] var1 = this.getChildComponents();
      int var3 = 0;

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] instanceof BBallast) {
            ++var3;
         }
      }

      this.setGroupBallastCount(new BStatusNumeric((double)var3));
      this.log.trace("DALI group " + this.getName() + " has " + var3 + " ballasts");
   }

   public final void stopped() throws Exception {
      super.stopped();
      this.doStopGroupIdentify();
   }

   public final void added(Property var1, Context var2) {
      if (this.isGroupOk() && BDaliDeviceType.isSupportedDaliDevice(var1.getType())) {
         BGenericDaliDevice var3 = (BGenericDaliDevice)this.get(var1);
         if (this.devCfgJob == null) {
            this.devCfgJob = new BDaliDeviceConfigurationJob(this);
         } else if (!this.devCfgJob.isAlive()) {
            this.devCfgJob = new BDaliDeviceConfigurationJob(this);
         }

         this.devCfgJob.setConfigureProperties(true);
         this.devCfgJob.add(var3);
         if (var3 instanceof BBallast) {
            this.getGroupBallastCount().setValue(this.getGroupBallastCount().getValue() + 1.0);
         }
      }

   }

   public final void removed(Property var1, BValue var2, Context var3) {
      if (this.isGroupOk()) {
         BGenericDaliDevice var4 = (BGenericDaliDevice)this.get(var1);
         if (var4 instanceof BBallast) {
            this.getGroupBallastCount().setValue(this.getGroupBallastCount().getValue() - 1.0);
         }
      }

   }

   public final void changed(Property var1, Context var2) {
      if (this.isGroupOk()) {
         if (var1 == command) {
            this.groupCommand(this.getCommand().getEnum().getOrdinal());
         } else if (var1 == directLevel) {
            this.groupDirectLevel(this.getDirectLevel().getValue());
         } else if (var1 == scene1) {
            this.programLevel(64, this.getScene1());
         } else if (var1 == scene2) {
            this.programLevel(65, this.getScene2());
         } else if (var1 == scene3) {
            this.programLevel(66, this.getScene3());
         } else if (var1 == scene4) {
            this.programLevel(67, this.getScene4());
         } else if (var1 == scene5) {
            this.programLevel(68, this.getScene5());
         } else if (var1 == scene6) {
            this.programLevel(69, this.getScene6());
         } else if (var1 == scene7) {
            this.programLevel(70, this.getScene7());
         } else if (var1 == scene8) {
            this.programLevel(71, this.getScene8());
         } else if (var1 == scene9) {
            this.programLevel(72, this.getScene9());
         } else if (var1 == scene10) {
            this.programLevel(73, this.getScene10());
         } else if (var1 == scene11) {
            this.programLevel(74, this.getScene11());
         } else if (var1 == scene12) {
            this.programLevel(75, this.getScene12());
         } else if (var1 == scene13) {
            this.programLevel(76, this.getScene13());
         } else if (var1 == scene14) {
            this.programLevel(77, this.getScene14());
         } else if (var1 == scene15) {
            this.programLevel(78, this.getScene15());
         } else if (var1 == scene16) {
            this.programLevel(79, this.getScene16());
         } else if (var1 == fadeRate) {
            this.programEnum(47, this.getFadeRate());
         } else if (var1 == fadeTime) {
            this.programEnum(46, this.getFadeTime());
         } else if (var1 == minLevel) {
            this.programLevel(43, this.getMinLevel());
         } else if (var1 == maxLevel) {
            this.programLevel(42, this.getMaxLevel());
         } else if (var1 == powerOnLevel) {
            this.programLevel(45, this.getPowerOnLevel());
         } else if (var1 == systemFailureLevel) {
            this.programLevel(44, this.getSystemFailureLevel());
         }
      }

   }

   public final void doSendCommand(BStandardCommands var1) {
      this.groupCommand(var1.getOrdinal());
   }

   public final void doRecallScene(BScenes var1) {
      this.groupCommand(var1.getOrdinal());
   }

   public final void doSetLevel(BInteger var1) {
      this.groupDirectLevel(var1.getDouble());
   }

   public final void doAllDevicesOff() {
      this.groupCommand(0);
   }

   public final void doAllDevicesOn() {
      this.groupCommand(5);
   }

   public final synchronized void doStartGroupIdentify() {
      this.doStopGroupIdentify();
      if (this.getDaliNetwork().isNetworkOk()) {
         if (this.identifyTask != null && !this.identifyTask.isAlive()) {
            this.identifyTask = null;
         }

         if (this.identifyTask == null) {
            this.identifyTask = new IdentifyTask(this);
            this.identifyTask.start();
         }
      }

   }

   public final synchronized void doStopGroupIdentify() {
      if (this.identifyTask != null) {
         this.identifyTask.stop();
         this.identifyTask = null;
      }

   }

   public final void doReprogram() {
      this.reconfigureGroup(true);
   }

   public final void reconfigureGroup(boolean var1) {
      if (this.isGroupOk()) {
         if (this.devCfgJob != null && this.devCfgJob.isAlive()) {
            this.log.warning("Could not reprogram group '" + this.getName() + "' a configuration job is already in progress");
            return;
         }

         this.devCfgJob = new BDaliDeviceConfigurationJob(this);
         this.devCfgJob.setConfigureProperties(var1);
         this.log.message("Reprogramming DALI group '" + this.getName() + '\'');
         BComponent[] var2 = this.getChildComponents();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] instanceof BBallast) {
               this.devCfgJob.add((BGenericDaliDevice)var2[var3]);
            }
         }
      }

   }

   private final void programLevel(int var1, BStatusNumeric var2) {
      if (this.getDaliNetwork().isNetworkOk() && !var2.getStatus().isNull()) {
         int var3 = DaliArcPowerUtils.percentToDirectLevel(var2.getValue());
         BComponent[] var4 = this.getChildComponents();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5] instanceof BBallast) {
               BBallast var6 = (BBallast)var4[var5];
               DaliWrite var7 = new DaliWrite(this.getDaliNetwork(), false, var6.getDaliAddress(), var1, var3);
               var7.setSendRepeat(2);
               var7.setStoreRepeat(2);
               var7.execute();
            }
         }
      }

   }

   private final void programEnum(int var1, BStatusEnum var2) {
      if (this.getDaliNetwork().isNetworkOk() && !var2.getStatus().isNull()) {
         BComponent[] var3 = this.getChildComponents();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] instanceof BBallast) {
               BBallast var5 = (BBallast)var3[var4];
               DaliWrite var6 = new DaliWrite(this.getDaliNetwork(), false, var5.getDaliAddress(), var1, var2.getValue().getOrdinal());
               var6.setSendRepeat(2);
               var6.setStoreRepeat(2);
               var6.execute();
            }
         }
      }

   }

   public final void groupCommand(int var1) {
      if (this.getDaliNetwork().isNetworkOk()) {
         BComponent[] var2 = this.getChildComponents();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] instanceof BBallast) {
               BBallast var4 = (BBallast)var2[var3];
               DaliCommand var5 = new DaliCommand(var4, var1);
               this.getDaliNetwork().setOpRetransmitCount(var5, var1);
               var5.execute();
            }
         }
      }

   }

   public final void groupDirectLevel(double var1) {
      if (this.getDaliNetwork().isNetworkOk()) {
         BComponent[] var3 = this.getChildComponents();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] instanceof BBallast) {
               BBallast var5 = (BBallast)var3[var4];
               DaliLevel var6 = new DaliLevel(var5, var1);
               var6.setTransmitRepeatCount(this.getDaliNetwork().getTransmitCount() - 1);
               var6.execute();
            }
         }
      }

   }

   public final int getDaliGroupAddress() {
      return -1;
   }

   public final BDaliNetwork getDaliNetwork() {
      try {
         if (this.isRunning()) {
            if (this.getNetwork() == null) {
               throw new RuntimeException("DALI group network null [" + this.getName() + ']');
            } else if (this.getNetwork() instanceof BDaliNetwork) {
               return (BDaliNetwork)this.getNetwork();
            } else {
               throw new RuntimeException("DALI group network invalid [" + this.getName() + ']');
            }
         } else {
            return null;
         }
      } catch (Exception var2) {
         throw new RuntimeException("DALI group network invalid [" + this.getName() + ']');
      }
   }

   public final boolean isVirtualGroup() {
      return true;
   }

   public final boolean isGroupOk() {
      boolean var10000;
      if (this.getDaliNetwork() == null) {
         var10000 = false;
         if (this.isRunning() && Sys.atSteadyState()) {
            var10000 = true;
         }

         return var10000;
      } else {
         var10000 = false;
         if (this.isRunning() && this.getDaliNetwork().isNetworkOk()) {
            var10000 = true;
         }

         return var10000;
      }
   }

   public BVirtualGroupFolder() {
     this.devCfgJob = null;
     this.identifyTask = null;
     this.log = Report.daliNetwork;
   }

   static {
      scene1 = newProperty(256, new BStatusNumeric(100.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene2 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene3 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene4 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene5 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene6 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene7 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene8 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene9 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene10 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene11 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene12 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene13 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene14 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene15 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      scene16 = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      fadeRate = newProperty(256, new BStatusEnum(BFadeRate.Steps_360, BStatus.nullStatus), (BFacets)null);
      fadeTime = newProperty(256, new BStatusEnum(BFadeTime.NoFade, BStatus.nullStatus), (BFacets)null);
      minLevel = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      maxLevel = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      powerOnLevel = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      systemFailureLevel = newProperty(256, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      command = newProperty(264, new BStatusEnum(BStandardCommands.off), BFacets.makeEnum(BEnumRange.make(BStandardCommands.TYPE)));
      directLevel = newProperty(264, new BStatusNumeric(), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(0), BDouble.make(0.0), BDouble.make(100.0)));
      sendCommand = newAction(0, BStandardCommands.off, (BFacets)null);
      recallScene = newAction(0, BScenes.scene1, (BFacets)null);
      setLevel = newAction(0, BInteger.make(0), (BFacets)null);
      allDevicesOff = newAction(0, (BFacets)null);
      allDevicesOn = newAction(0, (BFacets)null);
      startGroupIdentify = newAction(0, (BFacets)null);
      stopGroupIdentify = newAction(0, (BFacets)null);
      reprogram = newAction(128, (BFacets)null);
      TYPE = Sys.loadType(BVirtualGroupFolder.class);
   }
}
