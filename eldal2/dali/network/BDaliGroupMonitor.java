package uk.co.controlnetworksolutions.elitedali2.dali.network;

import java.util.Arrays;
import javax.baja.log.Log;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BComponent;
import javax.baja.sys.BDouble;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BRelTime;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import javax.baja.util.BFolder;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.DaliArcPowerUtils;
import uk.co.controlnetworksolutions.elitedali2.utils.StrUtil;

public final class BDaliGroupMonitor extends BComponent {
   public static final Property enable = newProperty(256, new BStatusBoolean(true), (BFacets)null);
   public static final Property alarm = newProperty(257, new BDaliGroupMonitorAlarm(), (BFacets)null);
   public static final Property totalBallasts = newProperty(265, new BStatusNumeric(0.0), (BFacets)null);
   public static final Property groupFault = newProperty(257, new BStatusBoolean(false), (BFacets)null);
   public static final Property ballastFault = newProperty(257, new BStatusBoolean(false), (BFacets)null);
   public static final Property ballastFaultCount = newProperty(257, new BStatusNumeric(0.0), (BFacets)null);
   public static final Property lampFault = newProperty(257, new BStatusBoolean(false), (BFacets)null);
   public static final Property lampFaultCount = newProperty(257, new BStatusNumeric(0.0), (BFacets)null);
   public static final Property minimumLevel = newProperty(265, new BStatusNumeric(0.0), BFacets.make("units", UnitDatabase.getUnit("percent")));
   public static final Property maximumLevel = newProperty(265, new BStatusNumeric(0.0), BFacets.make("units", UnitDatabase.getUnit("percent")));
   public static final Property averageLevel = newProperty(265, new BStatusNumeric(0.0), BFacets.make("units", UnitDatabase.getUnit("percent")));
   public static final Property lampsOn = newProperty(257, new BStatusBoolean(false), (BFacets)null);
   public static final Property lampOnTime = newProperty(265, BRelTime.make(0L), (BFacets)null);
   public static final Property lampOnHours = newProperty(265, new BStatusNumeric(0.0), BFacets.makeNumeric(UnitDatabase.getUnit("hour"), 3));
   public static final Property checkStatus = newProperty(256, new BStatusBoolean(true), (BFacets)null);
   public static final Property checkActualLevel = newProperty(256, new BStatusBoolean(true), (BFacets)null);
   public static final Property pollPeriod = newProperty(256, new BStatusNumeric(2000.0), BFacets.makeNumeric(UnitDatabase.getUnit("millisecond"), BInteger.make(0), BDouble.make(500.0), BDouble.make(1.0E9)));
   public static final Property queryRepeatCount = newProperty(4, 3, (BFacets)null);
   public static final Property debug = newProperty(4, new BDaliGroupMonitorDebug(), (BFacets)null);
   public static final Action startMonitor = newAction(0, (BFacets)null);
   public static final Action stopMonitor = newAction(0, (BFacets)null);
   public static final Action resetLampOnTime = newAction(0, (BFacets)null);
   public static final Type TYPE;
   private static final int MIN_POLL_PERIOD = 500;
   private static final int MAX_POLL_PERIOD = 1000000000;
   private BallastMonitorLog ballastLog;
   private MonitorTask monitorProcess;
   private Thread monitorThread;
   private BComponent ballastFolder;

   public final BStatusBoolean getEnable() {
      return (BStatusBoolean)this.get(enable);
   }

   public final void setEnable(BStatusBoolean var1) {
      this.set(enable, var1, (Context)null);
   }

   public final BDaliGroupMonitorAlarm getAlarm() {
      return (BDaliGroupMonitorAlarm)this.get(alarm);
   }

   public final void setAlarm(BDaliGroupMonitorAlarm var1) {
      this.set(alarm, var1, (Context)null);
   }

   public final BStatusNumeric getTotalBallasts() {
      return (BStatusNumeric)this.get(totalBallasts);
   }

   public final void setTotalBallasts(BStatusNumeric var1) {
      this.set(totalBallasts, var1, (Context)null);
   }

   public final BStatusBoolean getGroupFault() {
      return (BStatusBoolean)this.get(groupFault);
   }

   public final void setGroupFault(BStatusBoolean var1) {
      this.set(groupFault, var1, (Context)null);
   }

   public final BStatusBoolean getBallastFault() {
      return (BStatusBoolean)this.get(ballastFault);
   }

   public final void setBallastFault(BStatusBoolean var1) {
      this.set(ballastFault, var1, (Context)null);
   }

   public final BStatusNumeric getBallastFaultCount() {
      return (BStatusNumeric)this.get(ballastFaultCount);
   }

   public final void setBallastFaultCount(BStatusNumeric var1) {
      this.set(ballastFaultCount, var1, (Context)null);
   }

   public final BStatusBoolean getLampFault() {
      return (BStatusBoolean)this.get(lampFault);
   }

   public final void setLampFault(BStatusBoolean var1) {
      this.set(lampFault, var1, (Context)null);
   }

   public final BStatusNumeric getLampFaultCount() {
      return (BStatusNumeric)this.get(lampFaultCount);
   }

   public final void setLampFaultCount(BStatusNumeric var1) {
      this.set(lampFaultCount, var1, (Context)null);
   }

   public final BStatusNumeric getMinimumLevel() {
      return (BStatusNumeric)this.get(minimumLevel);
   }

   public final void setMinimumLevel(BStatusNumeric var1) {
      this.set(minimumLevel, var1, (Context)null);
   }

   public final BStatusNumeric getMaximumLevel() {
      return (BStatusNumeric)this.get(maximumLevel);
   }

   public final void setMaximumLevel(BStatusNumeric var1) {
      this.set(maximumLevel, var1, (Context)null);
   }

   public final BStatusNumeric getAverageLevel() {
      return (BStatusNumeric)this.get(averageLevel);
   }

   public final void setAverageLevel(BStatusNumeric var1) {
      this.set(averageLevel, var1, (Context)null);
   }

   public final BStatusBoolean getLampsOn() {
      return (BStatusBoolean)this.get(lampsOn);
   }

   public final void setLampsOn(BStatusBoolean var1) {
      this.set(lampsOn, var1, (Context)null);
   }

   public final BRelTime getLampOnTime() {
      return (BRelTime)this.get(lampOnTime);
   }

   public final void setLampOnTime(BRelTime var1) {
      this.set(lampOnTime, var1, (Context)null);
   }

   public final BStatusNumeric getLampOnHours() {
      return (BStatusNumeric)this.get(lampOnHours);
   }

   public final void setLampOnHours(BStatusNumeric var1) {
      this.set(lampOnHours, var1, (Context)null);
   }

   public final BStatusBoolean getCheckStatus() {
      return (BStatusBoolean)this.get(checkStatus);
   }

   public final void setCheckStatus(BStatusBoolean var1) {
      this.set(checkStatus, var1, (Context)null);
   }

   public final BStatusBoolean getCheckActualLevel() {
      return (BStatusBoolean)this.get(checkActualLevel);
   }

   public final void setCheckActualLevel(BStatusBoolean var1) {
      this.set(checkActualLevel, var1, (Context)null);
   }

   public final BStatusNumeric getPollPeriod() {
      return (BStatusNumeric)this.get(pollPeriod);
   }

   public final void setPollPeriod(BStatusNumeric var1) {
      this.set(pollPeriod, var1, (Context)null);
   }

   public final int getQueryRepeatCount() {
      return this.getInt(queryRepeatCount);
   }

   public final void setQueryRepeatCount(int var1) {
      this.setInt(queryRepeatCount, var1, (Context)null);
   }

   public final BDaliGroupMonitorDebug getDebug() {
      return (BDaliGroupMonitorDebug)this.get(debug);
   }

   public final void setDebug(BDaliGroupMonitorDebug var1) {
      this.set(debug, var1, (Context)null);
   }

   public final void startMonitor() {
      this.invoke(startMonitor, (BValue)null, (Context)null);
   }

   public final void stopMonitor() {
      this.invoke(stopMonitor, (BValue)null, (Context)null);
   }

   public final void resetLampOnTime() {
      this.invoke(resetLampOnTime, (BValue)null, (Context)null);
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
      if (Sys.atSteadyState()) {
         this.doStartMonitor();
      }

   }

   public final void stopped() {
      this.doStopMonitor();
   }

   private final void resolveGroupAccess() {
      BComponent var1 = (BComponent)this.getParent();
      if (var1.getType() != BDaliGroupFolder.TYPE && var1.getType() != BVirtualGroupFolder.TYPE && var1.getType() != BFolder.TYPE) {
         this.ballastFolder = null;
         this.logError("DALI Group Monitor parent is invalid");
      } else {
         this.ballastFolder = var1;
      }

   }

   public final void atSteadyState() throws Exception {
      this.doStartMonitor();
   }

   public final void changed(Property var1, Context var2) {
      if (this.ballastFolder != null && var1 == enable) {
         if (this.getEnable().getValue()) {
            this.doStartMonitor();
         } else {
            this.doStopMonitor();
         }
      }

   }

   public final void doStartMonitor() {
      if (this.ballastFolder == null) {
         this.doStopMonitor();
         this.logError("DALI Group Monitor parent is invalid");
      } else {
         if (this.monitorThread != null) {
            if (this.monitorThread.isAlive()) {
               if (this.getDebug().getShowTaskMessages()) {
                  this.logMsg("DALI Group Monitor already running");
               }

               return;
            }

            this.doStopMonitor();
         }

         this.clearValues();
         if (this.getEnable().getValue()) {
            this.monitorProcess = new MonitorTask(this);
            this.monitorThread = new Thread(this.monitorProcess);
            if (this.getDebug().getShowTaskMessages()) {
               this.logMsg("Starting DALI Group Monitor");
            }

            this.monitorThread.start();
         }

      }
   }

   public final void doStopMonitor() {
      if (this.monitorProcess != null) {
         if (this.getDebug().getShowTaskMessages()) {
            this.logMsg("Stopping DALI Group Monitor");
         }

         this.monitorProcess.stop();
      }

      this.monitorProcess = null;
      this.monitorThread = null;
   }

   public final void doResetLampOnTime() {
      this.clearTimeValues();
   }

   public final void clearTimeValues() {
      this.setLampOnTime(BRelTime.make(0L));
      this.getLampOnHours().setValue(0.0);
   }

   public final void clearValues() {
      this.getTotalBallasts().setValue(0.0);
      this.getGroupFault().setValue(false);
      this.getBallastFault().setValue(false);
      this.getBallastFaultCount().setValue(0.0);
      this.getLampFault().setValue(false);
      this.getLampFaultCount().setValue(0.0);
      this.getMinimumLevel().setValue(0.0);
      this.getMaximumLevel().setValue(0.0);
      this.getAverageLevel().setValue(0.0);
      this.getLampsOn().setValue(false);
   }

   private final void logMsg(String var1) {
      if (!this.getDebug().getHideAllDebugMessages()) {
         if (this.ballastFolder != null && !this.getDebug().getUseGroupMonitorName()) {
            Log.getLog("elitedali2").message("[" + this.ballastFolder.getName() + "] " + var1);
         } else {
            Log.getLog("elitedali2").message("[" + this.getName() + "] " + var1);
         }
      }

   }

   private final void logError(String var1) {
      Log.getLog("elitedali2").error("[" + this.getName() + "] " + var1);
   }

   public BDaliGroupMonitor() {
     this.ballastLog = null;
     this.monitorProcess = null;
     this.monitorThread = null;
     this.ballastFolder = null;
   }

   static {
      TYPE = Sys.loadType(BDaliGroupMonitor.class);
   }

   private class MonitorTask implements Runnable {
      private boolean stopThread;
      private BDaliGroupMonitor monitor;

      public void run() {
         if (BDaliGroupMonitor.this.getDebug().getShowTaskMessages()) {
            BDaliGroupMonitor.this.logMsg("DALI Group Monitor started");
         }

         if (BDaliGroupMonitor.this.ballastFolder == null) {
            BDaliGroupMonitor.this.monitorProcess = null;
            BDaliGroupMonitor.this.monitorThread = null;
            BDaliGroupMonitor.this.logError("DALI Group Monitor parent is invalid");
         } else {
            BDaliGroupMonitor.this.ballastLog = BDaliGroupMonitor.this.new BallastMonitorLog();

            while(!this.stopThread) {
               this.scanGroupBallasts();
            }

            if (BDaliGroupMonitor.this.getDebug().getShowTaskMessages()) {
               BDaliGroupMonitor.this.logMsg("DALI Group Monitor terminated");
            }

            BDaliGroupMonitor.this.monitorProcess = null;
            BDaliGroupMonitor.this.monitorThread = null;
         }
      }

      private final void scanGroupBallasts() {
         BBallast[] var1 = this.getBallastList();
         if (var1 != null && var1.length > 0) {
            BDaliGroupMonitor.this.getTotalBallasts().setValue((double)var1.length);
            if (BDaliGroupMonitor.this.getDebug().getShowScanDetails()) {
               BDaliGroupMonitor.this.logMsg("Group " + BDaliGroupMonitor.this.ballastFolder.getName() + " contains " + var1.length + " ballasts");
            }

            BDaliGroupMonitor.this.ballastLog.reset(var1.length);

            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (this.stopThread) {
                  return;
               }

               if (BDaliGroupMonitor.this.getCheckActualLevel().getValue()) {
                  this.queryBallastLevel(var2, var1[var2]);
               }

               if (BDaliGroupMonitor.this.getCheckStatus().getValue()) {
                  this.queryBallastStatus(var2, var1[var2]);
               }

               BDaliGroupMonitor.this.ballastLog.calculateStatistics(this.monitor);
               if (this.stopThread) {
                  return;
               }

               this.delay();
            }
         } else {
            BDaliGroupMonitor.this.clearValues();
            if (BDaliGroupMonitor.this.getDebug().getShowScanDetails()) {
               BDaliGroupMonitor.this.logMsg("Group " + BDaliGroupMonitor.this.ballastFolder.getName() + " contains NO ballasts");
            }

            this.delay();
         }

      }

      private final BBallast[] getBallastList() {
         BBallast[] var1 = new BBallast[64];
         int var2 = 0;
         BComponent[] var3 = BDaliGroupMonitor.this.ballastFolder.getChildComponents();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (this.stopThread) {
               return null;
            }

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

      private final void queryBallastStatus(int var1, BBallast var2) {
         if (var2 != null) {
            DaliQuery var3 = new DaliQuery(var2, 144);
            int var4 = var3.getResult();
            BDaliGroupMonitor.this.ballastLog.newStatus(var1, var4);
            this.processAlarm(var4, var2.getBallastStatus(), var2);
         }

      }

      private final int getStatusCode(int var1) {
         byte var2;
         if (var1 <= -99) {
            var2 = 3;
         } else if (var1 < 0) {
            var2 = 2;
         } else if ((var1 & 2) != 0) {
            var2 = 1;
         } else {
            var2 = 0;
         }

         return var2;
      }

      private final void processAlarm(int var1, int var2, BBallast var3) {
         int var4 = this.getStatusCode(var1);
         int var5 = this.getStatusCode(var2);
         BDaliDeviceType var6 = var3.getDaliDeviceType();
         if (var4 != var5) {
            if (var4 == 0 && var5 != 3) {
               if (BDaliGroupMonitor.this.getDebug().getShowAllDeviceOk()) {
                  BDaliGroupMonitor.this.logMsg("[" + var3.getName() + "] Device OK");
               }

               BDaliGroupMonitor.this.getAlarm().dispatch(BDaliGroupMonitorAlarm.NORMAL, var3, var6.getText() + " OK");
            } else if (var4 == 1) {
               if (BDaliGroupMonitor.this.getDebug().getShowAllLampFaults()) {
                  BDaliGroupMonitor.this.logMsg("[" + var3.getName() + "] Lamp fault");
               }

               BDaliGroupMonitor.this.getAlarm().dispatch(BDaliGroupMonitorAlarm.FAULT, var3, var6.getText() + " lamp fault");
            } else if (var4 == 2) {
               if (BDaliGroupMonitor.this.getDebug().getShowAllDeviceFaults()) {
                  BDaliGroupMonitor.this.logMsg("[" + var3.getName() + "] Device fault");
               }

               BDaliGroupMonitor.this.getAlarm().dispatch(BDaliGroupMonitorAlarm.FAULT, var3, var6.getText() + " fault");
            }

            var3.setBallastStatus(var1);
         }

      }

      private final void queryBallastLevel(int var1, BBallast var2) {
         if (var2 != null) {
            DaliQuery var3 = new DaliQuery(var2, 160);
            BDaliGroupMonitor.this.ballastLog.newLevel(var1, var3.getResult());
            if (BDaliGroupMonitor.this.ballastFolder instanceof BDaliGroupFolder && (BDaliGroupMonitor.this.getDebug().getShowLevelCompareFaults() || BDaliGroupMonitor.this.getDebug().getShowAllLevelCompares())) {
               this.compareDeviceLevel(var2, var3.getResult());
            }
         }

      }

      private final void compareDeviceLevel(BBallast var1, int var2) {
         if (var2 >= 0 && var2 < 255) {
            double var3 = DaliArcPowerUtils.directLevelToPercent(var2);
            double var5 = ((BDaliGroupFolder)BDaliGroupMonitor.this.ballastFolder).getDirectLevel().getValue();
            int var7 = DaliArcPowerUtils.percentToDirectLevel(var5);
            if (var2 != var7) {
               BDaliGroupMonitor.this.logMsg("[" + var1.getName() + "] Level fault: " + StrUtil.numberFormat2(var5) + " / " + StrUtil.numberFormat2(var3));
            } else if (BDaliGroupMonitor.this.getDebug().getShowAllLevelCompares()) {
               BDaliGroupMonitor.this.logMsg("[" + var1.getName() + "] Device level: " + StrUtil.numberFormat2(var5) + " / " + StrUtil.numberFormat2(var3));
            }
         }

      }

      private final void delay() {
         if (BDaliGroupMonitor.this.getPollPeriod().getValue() < 500.0) {
            BDaliGroupMonitor.this.getPollPeriod().setValue(500.0);
         }

         this.delay((int)BDaliGroupMonitor.this.getPollPeriod().getValue());
      }

      private final void delay(int var1) {
         try {
            Thread.sleep((long)var1);
         } catch (Exception var3) {
            BDaliGroupMonitor.this.logError("Delay failed with exception: " + var3);
            var3.printStackTrace();
         }

      }

      public void stop() {
         this.stopThread = true;
      }

      MonitorTask(BDaliGroupMonitor var2) {
        this.stopThread = false;
        this.monitor = null;
        
         this.monitor = var2;
      }
   }

   private class BallastMonitorLog {
      private int numberOfBallasts;
      private int[] statusList;
      private int[] levelList;
      private boolean lastLampOnState;
      private long lastLampOnTime;

      public void reset(int var1) {
         this.numberOfBallasts = var1;
         if (this.numberOfBallasts <= 0) {
            this.levelList = null;
            this.statusList = null;
         } else {
            if (this.statusList == null) {
               this.statusList = new int[this.numberOfBallasts];
               Arrays.fill(this.statusList, -1);
            } else if (this.statusList.length != this.numberOfBallasts) {
               int[] var3 = new int[this.numberOfBallasts];
               if (this.statusList.length > this.numberOfBallasts) {
                  System.arraycopy(this.statusList, this.statusList.length - this.numberOfBallasts, var3, 0, this.numberOfBallasts);
               } else {
                  Arrays.fill(var3, -1);
                  System.arraycopy(this.statusList, 0, var3, this.numberOfBallasts - this.statusList.length, this.statusList.length);
               }

               this.statusList = var3;
            }

            if (this.levelList == null) {
               this.levelList = new int[this.numberOfBallasts];
               Arrays.fill(this.levelList, -1);
            } else if (this.levelList.length != this.numberOfBallasts) {
               int[] var2 = new int[this.numberOfBallasts];
               if (this.levelList.length > this.numberOfBallasts) {
                  System.arraycopy(this.levelList, this.levelList.length - this.numberOfBallasts, var2, 0, this.numberOfBallasts);
               } else {
                  Arrays.fill(var2, -1);
                  System.arraycopy(this.levelList, 0, var2, this.numberOfBallasts - this.levelList.length, this.levelList.length);
               }

               this.levelList = var2;
            }

         }
      }

      public void newStatus(int var1, int var2) {
         if (var1 >= 0 && var1 < this.levelList.length) {
            this.statusList[var1] = var2;
         }

      }

      public void newLevel(int var1, int var2) {
         if (var1 >= 0 && var1 < this.levelList.length) {
            this.levelList[var1] = var2;
         }

      }

      public void calculateStatistics(BDaliGroupMonitor var1) {
         if (this.numberOfBallasts < 1) {
            var1.clearValues();
         } else {
            int var5 = 1000;
            int var6 = 0;
            double var7 = 0.0;
            int var9 = 0;
            int var2 = 0;
            int var3 = 0;
            int var4 = 0;

            for(int var12 = 0; var12 < this.numberOfBallasts; ++var12) {
               if (this.statusList != null) {
                  if (this.statusList[var12] >= 0) {
                     if ((this.statusList[var12] & 2) != 0) {
                        ++var3;
                     }

                     if ((this.statusList[var12] & 4) != 0) {
                        ++var2;
                     }
                  } else {
                     ++var4;
                  }
               }

               if (this.levelList != null && this.levelList[var12] >= 0 && this.levelList[var12] < 255) {
                  if (this.levelList[var12] < var5) {
                     var5 = this.levelList[var12];
                  }

                  if (this.levelList[var12] > var6) {
                     var6 = this.levelList[var12];
                  }

                  var7 += DaliArcPowerUtils.directLevelToPercent(this.levelList[var12]);
                  ++var9;
               }
            }

            double var10;
            if (var9 > 0) {
               var10 = var7 / (double)var9;
            } else {
               var5 = 0;
               var6 = 0;
               var10 = 0.0;
            }

            if (var2 > 0) {
               BDaliGroupMonitor.this.getLampsOn().setValue(true);
               if (!this.lastLampOnState) {
                  this.lastLampOnState = true;
                  this.lastLampOnTime = System.currentTimeMillis();
               } else {
                  long var14 = System.currentTimeMillis();
                  long var16 = var14 - this.lastLampOnTime;
                  long var18 = var1.getLampOnTime().getMillis() + var16;
                  var1.setLampOnTime(BRelTime.make(var18));
                  var1.getLampOnHours().setValue((double)var18 / 3600000.0);
                  this.lastLampOnTime = var14;
               }
            } else {
               BDaliGroupMonitor.this.getLampsOn().setValue(false);
               this.lastLampOnState = false;
               this.lastLampOnTime = (long)-1;
            }

            if (BDaliGroupMonitor.this.getDebug().getShowScanDetails()) {
               BDaliGroupMonitor.this.logMsg("Group " + BDaliGroupMonitor.this.ballastFolder.getName() + " ballast faults = " + var4);
               BDaliGroupMonitor.this.logMsg("Group " + BDaliGroupMonitor.this.ballastFolder.getName() + " lamp faults = " + var3);
               BDaliGroupMonitor.this.logMsg("Group " + BDaliGroupMonitor.this.ballastFolder.getName() + " lamps on = " + var2);
            }

            BStatusBoolean var10000 = var1.getBallastFault();
            boolean var10001 = false;
            if (var4 != 0) {
               var10001 = true;
            }

            var10000.setValue(var10001);
            var1.getBallastFaultCount().setValue((double)var4);
            var10000 = var1.getLampFault();
            var10001 = false;
            if (var3 != 0) {
               var10001 = true;
            }

            var10000.setValue(var10001);
            var1.getLampFaultCount().setValue((double)var3);
            var10000 = var1.getGroupFault();
            var10001 = false;
            if (var4 > 0 || var3 > 0) {
               var10001 = true;
            }

            var10000.setValue(var10001);
            var1.getMinimumLevel().setValue(DaliArcPowerUtils.directLevelToPercent(var5));
            var1.getMaximumLevel().setValue(DaliArcPowerUtils.directLevelToPercent(var6));
            var1.getAverageLevel().setValue(var10);
            if (BDaliGroupMonitor.this.getDebug().getShowLevelAverage()) {
               BDaliGroupMonitor.this.logMsg("Group " + BDaliGroupMonitor.this.ballastFolder.getName() + " level average = " + var1.getAverageLevel().getValue());
            }

         }
      }

      public BallastMonitorLog() {
        this.numberOfBallasts = 0;
        this.statusList = null;
        this.levelList = null;
        this.lastLampOnState = false;
        this.lastLampOnTime = (long)-1;
      }
   }
}
