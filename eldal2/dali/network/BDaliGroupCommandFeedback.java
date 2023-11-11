package uk.co.controlnetworksolutions.elitedali2.dali.network;

import javax.baja.log.Log;
import javax.baja.status.BIStatus;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
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
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BStandardCommands;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;

public final class BDaliGroupCommandFeedback extends BComponent implements BIStatus {
   public static final Property status;
   public static final Property enable;
   public static final Property command;
   public static final Property ballastPollInterval;
   public static final Property groupScanInterval;
   public static final Property maxScanCount;
   public static final Property mismatchCount;
   public static final Property debugLevel;
   public static final Action runGroupScan;
   public static final Action resetCounters;
   public static final Type TYPE;
   private static final int MIN_POLL_PERIOD = 500;
   private static final int MAX_POLL_PERIOD = 1000000000;
   private static final Log log;
   private volatile DeviceMonitorTask monitorTask;
   private volatile Thread monitorThread;
   private BDaliGroupFolder ballastFolder;

   public final BStatus getStatus() {
      return (BStatus)this.get(status);
   }

   public final void setStatus(BStatus var1) {
      this.set(status, var1, (Context)null);
   }

   public final BStatusBoolean getEnable() {
      return (BStatusBoolean)this.get(enable);
   }

   public final void setEnable(BStatusBoolean var1) {
      this.set(enable, var1, (Context)null);
   }

   public final BStatusEnum getCommand() {
      return (BStatusEnum)this.get(command);
   }

   public final void setCommand(BStatusEnum var1) {
      this.set(command, var1, (Context)null);
   }

   public final BStatusNumeric getBallastPollInterval() {
      return (BStatusNumeric)this.get(ballastPollInterval);
   }

   public final void setBallastPollInterval(BStatusNumeric var1) {
      this.set(ballastPollInterval, var1, (Context)null);
   }

   public final BStatusNumeric getGroupScanInterval() {
      return (BStatusNumeric)this.get(groupScanInterval);
   }

   public final void setGroupScanInterval(BStatusNumeric var1) {
      this.set(groupScanInterval, var1, (Context)null);
   }

   public final BStatusNumeric getMaxScanCount() {
      return (BStatusNumeric)this.get(maxScanCount);
   }

   public final void setMaxScanCount(BStatusNumeric var1) {
      this.set(maxScanCount, var1, (Context)null);
   }

   public final BStatusNumeric getMismatchCount() {
      return (BStatusNumeric)this.get(mismatchCount);
   }

   public final void setMismatchCount(BStatusNumeric var1) {
      this.set(mismatchCount, var1, (Context)null);
   }

   public final int getDebugLevel() {
      return this.getInt(debugLevel);
   }

   public final void setDebugLevel(int var1) {
      this.setInt(debugLevel, var1, (Context)null);
   }

   public final void runGroupScan() {
      this.invoke(runGroupScan, (BValue)null, (Context)null);
   }

   public final void resetCounters() {
      this.invoke(resetCounters, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final Type[] getServiceTypes() {
      return null;
   }

   public final void started() throws Exception {
      super.started();
      this.resolveGroupAccess();
      if (this.ballastFolder != null && this.get("groupCommandLink") == null) {
         this.add("groupCommandLink", this.makeLink(this.ballastFolder, this.ballastFolder.getSlot("command"), this.getSlot("command"), (Context)null), 5);
      }

      this.startMonitorTask();
   }

   public final void stopped() {
      this.stopMonitorTask();
   }

   private final void resolveGroupAccess() {
      BComponent var1 = (BComponent)this.getParent();
      if (var1.getType() == BDaliGroupFolder.TYPE) {
         this.ballastFolder = (BDaliGroupFolder)var1;
         this.setStatus(BStatus.makeFault(this.getStatus(), false));
      } else {
         this.ballastFolder = null;
         this.setStatus(BStatus.makeFault(this.getStatus(), true));
         log.error("DALI Group Feedback parent is invalid");
      }

   }

   public final void changed(Property var1, Context var2) {
      if (this.ballastFolder != null && this.getStatus().isOk()) {
         if (var1 == enable) {
            if (this.getEnable().getValue()) {
               this.startMonitorTask();
            } else {
               this.stopMonitorTask();
            }
         } else if (var1 == command) {
            this.processInputCommand();
         } else if (var1 == groupScanInterval && !this.getGroupScanInterval().getStatus().isNull()) {
            this.processInputCommand();
         }

      }
   }

   public final void doRunGroupScan() {
      this.processInputCommand();
   }

   public final void doResetCounters() {
      this.getMismatchCount().setValue(0.0);
   }

   public final void processInputCommand() {
      if (this.ballastFolder != null) {
         boolean var3 = false;
         boolean var2 = true;
         int var1 = this.getCommand().getEnum().getOrdinal();
         if (var1 == 0) {
            var2 = false;
            var3 = true;
         } else if (var1 == 5) {
            var2 = true;
            var3 = true;
         } else if (var1 == 6) {
            var2 = true;
            var3 = true;
         } else if (var1 == 16) {
            if (this.ballastFolder.getScene1().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 17) {
            if (this.ballastFolder.getScene2().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 18) {
            if (this.ballastFolder.getScene3().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 19) {
            if (this.ballastFolder.getScene4().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 20) {
            if (this.ballastFolder.getScene4().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 21) {
            if (this.ballastFolder.getScene6().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 22) {
            if (this.ballastFolder.getScene7().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 23) {
            if (this.ballastFolder.getScene8().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 24) {
            if (this.ballastFolder.getScene9().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 25) {
            if (this.ballastFolder.getScene10().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 26) {
            if (this.ballastFolder.getScene11().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 27) {
            if (this.ballastFolder.getScene12().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 28) {
            if (this.ballastFolder.getScene13().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 29) {
            if (this.ballastFolder.getScene14().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 30) {
            if (this.ballastFolder.getScene15().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         } else if (var1 == 31) {
            if (this.ballastFolder.getScene16().getValue() == 0.0) {
               var2 = false;
            } else {
               var2 = true;
            }

            var3 = true;
         }

         if (var3 && this.monitorTask != null) {
            this.monitorTask.initiateScan(var2);
         }

      }
   }

   private final synchronized void startMonitorTask() {
      if (this.monitorTask != null) {
         this.monitorTask.stop();
         this.dbgMsg("stopTask");
      }

      this.monitorTask = null;
      this.monitorThread = null;
      if (this.getEnable().getValue() && this.getStatus().isOk()) {
         this.monitorTask = new DeviceMonitorTask();
         this.monitorThread = new Thread(this.monitorTask);
         this.dbgMsg("startTask");
         this.monitorThread.start();
      }
   }

   private final synchronized void stopMonitorTask() {
      if (this.monitorTask != null) {
         this.monitorTask.stop();
         this.dbgMsg("stopTask");
      }

      this.monitorTask = null;
      this.monitorThread = null;
   }

   private final synchronized void dbgMsg(String var1) {
      if (this.getDebugLevel() > 0) {
         log.trace("[" + this.getName() + "] " + var1);
      }

   }

   public BDaliGroupCommandFeedback() {
     this.monitorTask = null;
     this.monitorThread = null;
     this.ballastFolder = null;
   }

   static {
      status = newProperty(257, BStatus.ok, (BFacets)null);
      enable = newProperty(256, new BStatusBoolean(true), (BFacets)null);
      command = newProperty(264, new BStatusEnum(BStandardCommands.off), BFacets.makeEnum(BEnumRange.make(BStandardCommands.TYPE)));
      ballastPollInterval = newProperty(256, new BStatusNumeric(2000.0), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(1000.0), BDouble.make(600000.0)));
      groupScanInterval = newProperty(256, new BStatusNumeric(300000.0), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(5000.0), BDouble.make(6000000.0)));
      maxScanCount = newProperty(256, new BStatusNumeric((double)3), (BFacets)null);
      mismatchCount = newProperty(1, new BStatusNumeric(0.0), (BFacets)null);
      debugLevel = newProperty(4, 0, (BFacets)null);
      runGroupScan = newAction(0, (BFacets)null);
      resetCounters = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BDaliGroupCommandFeedback.class);
      log = Log.getLog("elitedali2.groupCmdFeedback");
   }

   private class DeviceMonitorTask implements Runnable {
      private boolean taskAlive;
      private boolean expectedStateOn;
      private boolean mismatch;

      public synchronized void initiateScan(boolean var1) {
         this.expectedStateOn = var1;
         this.notifyAll();
      }

      public void run() {
         BDaliGroupCommandFeedback.this.dbgMsg("DALI Group Feedback task started");
         if (BDaliGroupCommandFeedback.this.ballastFolder == null) {
            BDaliGroupCommandFeedback.this.monitorThread = null;
            BDaliGroupCommandFeedback.this.monitorTask = null;
            BDaliGroupCommandFeedback.log.error("DALI Group Feedback task aborted");
         } else {
            this.taskAlive = true;

            try {
               BDaliGroupCommandFeedback.this.processInputCommand();

               while(this.taskAlive) {
                  this.scanGroupBallasts();
                  if (!this.taskAlive) {
                     return;
                  }

                  synchronized(this) {
                     if (BDaliGroupCommandFeedback.this.getGroupScanInterval().getStatus().isNull()) {
                        this.wait();
                     } else {
                        this.wait((long)BDaliGroupCommandFeedback.this.getGroupScanInterval().getValue());
                     }
                  }
               }
            } catch (Exception var4) {
            }

            BDaliGroupCommandFeedback.log.trace("DALI Group Feedback task terminated");
            BDaliGroupCommandFeedback.this.monitorTask = null;
            BDaliGroupCommandFeedback.this.monitorThread = null;
         }
      }

      private final void scanGroupBallasts() throws Exception {
         BBallast[] var1 = this.getBallastList();
         if (var1 != null && var1.length > 0) {
            for(int var2 = 0; (double)var2 < BDaliGroupCommandFeedback.this.getMaxScanCount().getValue(); ++var2) {
               if (!this.taskAlive) {
                  return;
               }

               this.mismatch = false;
               boolean var4 = false;

               for(int var3 = 0; var3 < var1.length; ++var3) {
                  if (!this.taskAlive) {
                     return;
                  }

                  if (var1[var3].getEnabled()) {
                     synchronized(this) {
                        this.wait((long)BDaliGroupCommandFeedback.this.getBallastPollInterval().getValue());
                     }

                     if (!this.taskAlive) {
                        return;
                     }

                     this.ballastLevelCheck(var1[var3]);
                     if (this.mismatch) {
                        this.retransmitCommand();
                        this.mismatch = false;
                        var4 = true;
                        BDaliGroupCommandFeedback.this.dbgMsg("Mismatch on device with address " + var1[var3].getDaliAddress());
                     }
                  }
               }

               if (!var4) {
                  return;
               }

               BDaliGroupCommandFeedback.this.getMismatchCount().setValue(BDaliGroupCommandFeedback.this.getMismatchCount().getValue() + 1.0);
            }
         }

      }

      private final BBallast[] getBallastList() {
         int var2 = 0;
         BComponent[] var3 = BDaliGroupCommandFeedback.this.ballastFolder.getChildComponents();
         BBallast[] var1 = new BBallast[64];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] instanceof BBallast) {
               if (var2 < 64) {
                  var1[var2] = (BBallast)var3[var4];
               }

               ++var2;
            }
         }

         if (var2 < 1) {
            return null;
         } else {
            BBallast[] var5 = new BBallast[var2];
            System.arraycopy(var1, 0, var5, 0, var2);
            return var5;
         }
      }

      private final void ballastLevelCheck(BBallast var1) {
         if (var1 != null) {
            DaliQuery var2 = new DaliQuery(var1, 160);
            if (var2.getResult() >= 0 && var2.getResult() < 255) {
               if (this.expectedStateOn) {
                  if (var2.getResult() == 0) {
                     this.mismatch = true;
                  }
               } else if (var2.getResult() != 0) {
                  this.mismatch = true;
               }
            }
         }

      }

      public synchronized void retransmitCommand() {
         if (BDaliGroupCommandFeedback.this.ballastFolder != null) {
            BDaliGroupCommandFeedback.this.ballastFolder.groupCommand(BDaliGroupCommandFeedback.this.getCommand().getEnum().getOrdinal());
         }

      }

      public synchronized void stop() {
         this.taskAlive = false;
         this.notifyAll();
      }
      
      DeviceMonitorTask() {
        this.taskAlive = true;
        this.expectedStateOn = true;
        this.mismatch = false;
      }
   }
}
