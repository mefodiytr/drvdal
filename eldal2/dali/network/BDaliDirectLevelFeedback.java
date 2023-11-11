package uk.co.controlnetworksolutions.elitedali2.dali.network;

import javax.baja.log.Log;
import javax.baja.status.BIStatus;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BAbsTime;
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
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.DaliArcPowerUtils;

public final class BDaliDirectLevelFeedback extends BComponent implements BIStatus {
   public static final Property status;
   public static final Property lastScanTime;
   public static final Property enable;
   public static final Property directLevel;
   public static final Property inhibit;
   public static final Property ballastPollInterval;
   public static final Property deviceScanInterval;
   public static final Property maxScanCount;
   public static final Property mismatchCount;
   public static final Property mismatchMargin;
   public static final Property debugLevel;
   public static final Action runDeviceScan;
   public static final Action resetCounters;
   public static final Type TYPE;
   private static final int MIN_POLL_PERIOD = 500;
   private static final int MAX_POLL_PERIOD = 1000000000;
   private static final Log log;
   private volatile DeviceMonitorTask monitorTask;
   private volatile Thread monitorThread;
   private BDaliGroupFolder groupParent;
   private BBallast deviceParent;

   public final BStatus getStatus() {
      return (BStatus)this.get(status);
   }

   public final void setStatus(BStatus var1) {
      this.set(status, var1, (Context)null);
   }

   public final BAbsTime getLastScanTime() {
      return (BAbsTime)this.get(lastScanTime);
   }

   public final void setLastScanTime(BAbsTime var1) {
      this.set(lastScanTime, var1, (Context)null);
   }

   public final BStatusBoolean getEnable() {
      return (BStatusBoolean)this.get(enable);
   }

   public final void setEnable(BStatusBoolean var1) {
      this.set(enable, var1, (Context)null);
   }

   public final BStatusNumeric getDirectLevel() {
      return (BStatusNumeric)this.get(directLevel);
   }

   public final void setDirectLevel(BStatusNumeric var1) {
      this.set(directLevel, var1, (Context)null);
   }

   public final BStatusBoolean getInhibit() {
      return (BStatusBoolean)this.get(inhibit);
   }

   public final void setInhibit(BStatusBoolean var1) {
      this.set(inhibit, var1, (Context)null);
   }

   public final BStatusNumeric getBallastPollInterval() {
      return (BStatusNumeric)this.get(ballastPollInterval);
   }

   public final void setBallastPollInterval(BStatusNumeric var1) {
      this.set(ballastPollInterval, var1, (Context)null);
   }

   public final BStatusNumeric getDeviceScanInterval() {
      return (BStatusNumeric)this.get(deviceScanInterval);
   }

   public final void setDeviceScanInterval(BStatusNumeric var1) {
      this.set(deviceScanInterval, var1, (Context)null);
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

   public final BStatusNumeric getMismatchMargin() {
      return (BStatusNumeric)this.get(mismatchMargin);
   }

   public final void setMismatchMargin(BStatusNumeric var1) {
      this.set(mismatchMargin, var1, (Context)null);
   }

   public final int getDebugLevel() {
      return this.getInt(debugLevel);
   }

   public final void setDebugLevel(int var1) {
      this.setInt(debugLevel, var1, (Context)null);
   }

   public final void runDeviceScan() {
      this.invoke(runDeviceScan, (BValue)null, (Context)null);
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
      this.resolveDeviceAccess();
      this.startMonitorTask();
      if (this.groupParent != null && this.get("groupLevelLink") == null) {
         this.add("groupLevelLink", this.makeLink(this.groupParent, this.groupParent.getSlot("directLevel"), this.getSlot("directLevel"), (Context)null), 5);
      }

      if (this.deviceParent != null && this.get("deviceLevelLink") == null) {
         this.add("deviceLevelLink", this.makeLink(this.deviceParent, this.deviceParent.getSlot("directLevel"), this.getSlot("directLevel"), (Context)null), 5);
      }

   }

   public final void stopped() {
      this.stopMonitorTask();
   }

   private final void resolveDeviceAccess() {
      BComponent var1 = (BComponent)this.getParent();
      if (var1 instanceof BDaliGroupFolder) {
         this.groupParent = (BDaliGroupFolder)var1;
         this.setStatus(BStatus.makeFault(this.getStatus(), false));
      } else if (var1 instanceof BBallast) {
         this.deviceParent = (BBallast)var1;
         this.setStatus(BStatus.makeFault(this.getStatus(), false));
      } else {
         this.groupParent = null;
         this.setStatus(BStatus.makeFault(this.getStatus(), true));
         log.error("[" + this.getName() + "] DALI Direct Level Feedback parent is invalid");
      }

   }

   public final void changed(Property var1, Context var2) {
      if ((this.groupParent != null || this.deviceParent != null) && this.getStatus().isOk()) {
         if (var1 == enable) {
            if (this.getEnable().getValue()) {
               this.startMonitorTask();
            } else {
               this.stopMonitorTask();
            }
         } else if (var1 == inhibit) {
            if (this.getInhibit().getValue()) {
               this.dbgMsg("Inhibit");
               if (this.monitorTask != null) {
                  this.monitorTask.cancelScan();
               }
            } else {
               this.dbgMsg("Uninhibit");
               this.processInputDirectLevel();
            }
         } else if (var1 == directLevel) {
            this.processInputDirectLevel();
         } else if (var1 == deviceScanInterval && !this.getDeviceScanInterval().getStatus().isNull()) {
            this.processInputDirectLevel();
         }

      }
   }

   public final void doRunDeviceScan() {
      this.processInputDirectLevel();
   }

   public final void doResetCounters() {
      this.getMismatchCount().setValue(0.0);
   }

   public final void processInputDirectLevel() {
      if (this.getEnable().getValue() && !this.getInhibit().getValue() && (this.groupParent != null || this.deviceParent != null) && this.isRunning() && Sys.atSteadyState()) {
         if (this.monitorTask == null) {
            log.error("[" + this.getName() + "] DALI Direct Level Feedback monitor task failed");
         } else {
            this.monitorTask.initiateScan();
         }
      }
   }

   private final synchronized void startMonitorTask() {
      this.dbgMsg("Stopping DALI Direct Level Feedback task");
      if (this.monitorTask != null) {
         this.monitorTask.stop();
      }

      this.monitorTask = null;
      this.monitorThread = null;
      if (this.getEnable().getValue() && this.getStatus().isOk()) {
         this.monitorTask = new DeviceMonitorTask();
         this.monitorThread = new Thread(this.monitorTask);
         this.dbgMsg("Starting DALI Direct Level Feedback task");
         this.monitorThread.start();
      }
   }

   private final synchronized void stopMonitorTask() {
      if (this.monitorTask != null) {
         this.dbgMsg("Stopping DALI Direct Level Feedback task");
         this.monitorTask.stop();
      }

      this.monitorTask = null;
      this.monitorThread = null;
   }

   private final synchronized void dbgMsg(String var1) {
      if (this.getDebugLevel() > 0) {
         log.trace("[" + this.getName() + "] " + var1);
      }

   }

   public BDaliDirectLevelFeedback() {
     this.monitorTask = null;
     this.monitorThread = null;
     this.groupParent = null;
     this.deviceParent = null;
   }

   static {
      status = newProperty(257, BStatus.ok, (BFacets)null);
      lastScanTime = newProperty(257, BAbsTime.DEFAULT, (BFacets)null);
      enable = newProperty(256, new BStatusBoolean(true), (BFacets)null);
      directLevel = newProperty(264, new BStatusNumeric(), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(1), BDouble.make(0.0), BDouble.make(100.0)));
      inhibit = newProperty(256, new BStatusBoolean(false), (BFacets)null);
      ballastPollInterval = newProperty(256, new BStatusNumeric(2000.0), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(1000.0), BDouble.make(600000.0)));
      deviceScanInterval = newProperty(256, new BStatusNumeric(300000.0), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(5000.0), BDouble.make(6000000.0)));
      maxScanCount = newProperty(256, new BStatusNumeric((double)3), (BFacets)null);
      mismatchCount = newProperty(5, new BStatusNumeric(0.0), (BFacets)null);
      mismatchMargin = newProperty(4, new BStatusNumeric((double)4), BFacets.makeNumeric(UnitDatabase.getUnit("percent"), BInteger.make(1), BDouble.make(0.0), BDouble.make(100.0)));
      debugLevel = newProperty(4, 0, (BFacets)null);
      runDeviceScan = newAction(0, (BFacets)null);
      resetCounters = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BDaliDirectLevelFeedback.class);
      log = Log.getLog("elitedali2.levelFeedback");
   }

   private class DeviceMonitorTask implements Runnable {
      private boolean taskAlive;
      private boolean cancelScan;
      private boolean mismatch;

      public synchronized void initiateScan() {
         this.notifyAll();
         if (BDaliDirectLevelFeedback.this.getDebugLevel() > 60) {
            BDaliDirectLevelFeedback.this.dbgMsg("Initiate Scan");
         }

      }

      public synchronized void cancelScan() {
         this.cancelScan = true;
         this.notifyAll();
         if (BDaliDirectLevelFeedback.this.getDebugLevel() > 60) {
            BDaliDirectLevelFeedback.this.dbgMsg("Cancel Scan");
         }

      }

      public void run() {
         BDaliDirectLevelFeedback.this.dbgMsg("DALI Direct Level Feedback task started");
         if (BDaliDirectLevelFeedback.this.groupParent == null && BDaliDirectLevelFeedback.this.deviceParent == null) {
            BDaliDirectLevelFeedback.this.monitorThread = null;
            BDaliDirectLevelFeedback.this.monitorTask = null;
            BDaliDirectLevelFeedback.log.error("[" + BDaliDirectLevelFeedback.this.getName() + "] DALI Direct Level Feedback task aborted");
         } else {
            this.taskAlive = true;
            this.cancelScan = false;

            try {
               while(this.taskAlive) {
                  if (!this.cancelScan) {
                     if (BDaliDirectLevelFeedback.this.getDebugLevel() > 10) {
                        BDaliDirectLevelFeedback.this.dbgMsg("Level = " + BDaliDirectLevelFeedback.this.getDirectLevel().getValue());
                     }

                     BDaliDirectLevelFeedback.this.setLastScanTime(BAbsTime.make());
                     this.scanBallasts();
                  }

                  if (!this.taskAlive) {
                     return;
                  }

                  synchronized(this) {
                     this.cancelScan = false;
                     if (BDaliDirectLevelFeedback.this.getDeviceScanInterval().getStatus().isNull()) {
                        if (BDaliDirectLevelFeedback.this.getDebugLevel() > 70) {
                           BDaliDirectLevelFeedback.this.dbgMsg("Wait");
                        }

                        this.wait();
                     } else {
                        if (BDaliDirectLevelFeedback.this.getDebugLevel() > 70) {
                           BDaliDirectLevelFeedback.this.dbgMsg("Wait (" + (long)BDaliDirectLevelFeedback.this.getDeviceScanInterval().getValue() + ')');
                        }

                        this.wait((long)BDaliDirectLevelFeedback.this.getDeviceScanInterval().getValue());
                     }
                  }
               }
            } catch (Exception var4) {
            }

            BDaliDirectLevelFeedback.log.trace("DALI Direct Level Feedback task terminated");
            BDaliDirectLevelFeedback.this.monitorTask = null;
            BDaliDirectLevelFeedback.this.monitorThread = null;
         }
      }

      private final void scanBallasts() throws Exception {
         BBallast[] var1 = this.getBallastList();
         if (var1 != null && var1.length > 0) {
            if (BDaliDirectLevelFeedback.this.getDebugLevel() > 20) {
               BDaliDirectLevelFeedback.this.dbgMsg("Scan " + var1.length + " devices");
            }

            for(int var2 = 0; (double)var2 < BDaliDirectLevelFeedback.this.getMaxScanCount().getValue(); ++var2) {
               this.mismatch = false;
               boolean var4 = false;

               for(int var3 = 0; var3 < var1.length; ++var3) {
                  if (!this.taskAlive || this.cancelScan) {
                     return;
                  }

                  if (var1[var3].getEnabled()) {
                     synchronized(this) {
                        this.wait((long)BDaliDirectLevelFeedback.this.getBallastPollInterval().getValue());
                     }

                     if (!this.taskAlive || this.cancelScan) {
                        return;
                     }

                     if (BDaliDirectLevelFeedback.this.getDebugLevel() > 50) {
                        BDaliDirectLevelFeedback.this.dbgMsg("Check device " + var3 + " - " + var1[var3].getName());
                     }

                     this.ballastLevelCheck(var1[var3]);
                     if (this.mismatch) {
                        this.retransmitDirectLevel();
                        this.mismatch = false;
                        var4 = true;
                        BDaliDirectLevelFeedback.this.dbgMsg("Mismatch on device with address " + (var1[var3].getDaliAddress() + 1));
                     }
                  }
               }

               if (!var4) {
                  return;
               }

               BDaliDirectLevelFeedback.this.getMismatchCount().setValue(BDaliDirectLevelFeedback.this.getMismatchCount().getValue() + 1.0);
            }
         }

      }

      private final BBallast[] getBallastList() {
         int var2 = 0;
         if (BDaliDirectLevelFeedback.this.deviceParent != null) {
            return new BBallast[]{BDaliDirectLevelFeedback.this.deviceParent};
         } else {
            BComponent[] var3 = BDaliDirectLevelFeedback.this.groupParent.getChildComponents();
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
      }

      private final void ballastLevelCheck(BBallast var1) {
         if (var1 != null) {
            DaliQuery var2 = new DaliQuery(var1, 160);
            int var3 = var2.getResult();
            if (var3 >= 0 && var3 < 255) {
               double var4 = BDaliDirectLevelFeedback.this.getDirectLevel().getValue();
               if (var4 == 0.0) {
                  if (var3 != 0) {
                     this.mismatch = true;
                  }
               } else if (var3 == 0 && var4 != 0.0) {
                  this.mismatch = true;
               } else {
                  double var6 = DaliArcPowerUtils.directLevelToPercent(var3);
                  double var8 = var4 - BDaliDirectLevelFeedback.this.getMismatchMargin().getValue();
                  double var10 = var4 + BDaliDirectLevelFeedback.this.getMismatchMargin().getValue();
                  if (var6 < var8 || var6 > var10) {
                     this.mismatch = true;
                  }
               }
            } else {
               if (BDaliDirectLevelFeedback.this.getDebugLevel() > 5) {
                  BDaliDirectLevelFeedback.this.dbgMsg("Device fault, address = " + (var1.getDaliAddress() + 1) + " [ " + var3 + " ]");
               }

               this.mismatch = true;
            }
         }

      }

      public synchronized void retransmitDirectLevel() {
         if (BDaliDirectLevelFeedback.this.getDebugLevel() > 10) {
            BDaliDirectLevelFeedback.this.dbgMsg("Retransmit level = " + BDaliDirectLevelFeedback.this.getDirectLevel().getValue());
         }

         if (BDaliDirectLevelFeedback.this.groupParent != null) {
            BDaliDirectLevelFeedback.this.groupParent.groupDirectLevel(BDaliDirectLevelFeedback.this.getDirectLevel().getValue());
         }

         if (BDaliDirectLevelFeedback.this.deviceParent != null) {
            BDaliDirectLevelFeedback.this.deviceParent.daliLevel(BDaliDirectLevelFeedback.this.getDirectLevel().getValue());
         }

      }

      public synchronized void stop() {
         this.taskAlive = false;
         this.notifyAll();
      }

      private DeviceMonitorTask() {
        this.taskAlive = true;
        this.cancelScan = false;
        this.mismatch = false;
      }
   }
}
