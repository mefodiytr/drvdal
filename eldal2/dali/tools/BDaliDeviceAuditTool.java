package uk.co.controlnetworksolutions.elitedali2.dali.tools;

import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.baja.alarm.BAlarmRecord;
import javax.baja.alarm.BAlarmService;
import javax.baja.alarm.BSourceState;
import javax.baja.driver.BDevice;
import javax.baja.log.Log;
import javax.baja.naming.BOrd;
import javax.baja.naming.BOrdList;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.Action;
import javax.baja.sys.BAbsTime;
import javax.baja.sys.BComponent;
import javax.baja.sys.BDouble;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BRelTime;
import javax.baja.sys.BString;
import javax.baja.sys.BTime;
import javax.baja.sys.BValue;
import javax.baja.sys.BWeekday;
import javax.baja.sys.Clock;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Slot;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.units.UnitDatabase;
import javax.baja.util.BFormat;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType1;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;

public class BDaliDeviceAuditTool extends BComponent {
   public static final Property statusMessage = newProperty(1, "", (BFacets)null);
   public static final Property auditAlarmClass = newProperty(256, "defaultAlarmClass", BFacets.make("fieldEditor", BString.make("alarm:AlarmClassFE")));
   public static final Property deviceAlarmSourceName = newProperty(0, BFormat.make("%parent.parent.parent.parent.displayName%/%parent.parent.parent.displayName%/%parent.displayName%/%displayName%"), (BFacets)null);
   public static final Property auditInProgress = newProperty(257, new BStatusBoolean(false), (BFacets)null);
   public static final Property nextAuditTime;
   public static final Property lastAuditStart;
   public static final Property lastAuditDuration;
   public static final Property lastAuditDeviceCount;
   public static final Property lastAuditFaultCount;
   public static final Property enable;
   public static final Property auditFrequency;
   public static final Property auditTime;
   public static final Property auditWeekday;
   public static final Property interDeviceDelay;
   public static final Property useDeviceAlarmClass;
   public static final Property auditAllDevicesOnStation;
   public static final Property logFaults;
   public static final Property retryCount;
   public static final Property debugLevel;
   public static final Action runAudit;
   public static final Action abortRunningAudit;
   public static final Type TYPE;
   private static final int AUDIT_FREQUENCY_DISABLED = 0;
   private static final int AUDIT_FREQUENCY_MANUAL = 1;
   private static final int AUDIT_FREQUENCY_WEEKLY = 2;
   private static final int AUDIT_FREQUENCY_DAILY = 3;
   private static final int DEVICE_STATE_NORMAL = 0;
   private static final int DEVICE_STATE_LAMP_FAIL = 1;
   private static final int DEVICE_STATE_FAULT = 2;
   private static final int DEVICE_STATE_DOWN = 3;
   private static String STATION_ROOT;
   private volatile Timer auditTimer;
   private volatile DeviceAuditTask auditTask;

   public String getStatusMessage() {
      return this.getString(statusMessage);
   }

   public void setStatusMessage(String var1) {
      this.setString(statusMessage, var1, (Context)null);
   }

   public String getAuditAlarmClass() {
      return this.getString(auditAlarmClass);
   }

   public void setAuditAlarmClass(String var1) {
      this.setString(auditAlarmClass, var1, (Context)null);
   }

   public BFormat getDeviceAlarmSourceName() {
      return (BFormat)this.get(deviceAlarmSourceName);
   }

   public void setDeviceAlarmSourceName(BFormat var1) {
      this.set(deviceAlarmSourceName, var1, (Context)null);
   }

   public BStatusBoolean getAuditInProgress() {
      return (BStatusBoolean)this.get(auditInProgress);
   }

   public void setAuditInProgress(BStatusBoolean var1) {
      this.set(auditInProgress, var1, (Context)null);
   }

   public BAbsTime getNextAuditTime() {
      return (BAbsTime)this.get(nextAuditTime);
   }

   public void setNextAuditTime(BAbsTime var1) {
      this.set(nextAuditTime, var1, (Context)null);
   }

   public BAbsTime getLastAuditStart() {
      return (BAbsTime)this.get(lastAuditStart);
   }

   public void setLastAuditStart(BAbsTime var1) {
      this.set(lastAuditStart, var1, (Context)null);
   }

   public BRelTime getLastAuditDuration() {
      return (BRelTime)this.get(lastAuditDuration);
   }

   public void setLastAuditDuration(BRelTime var1) {
      this.set(lastAuditDuration, var1, (Context)null);
   }

   public BStatusNumeric getLastAuditDeviceCount() {
      return (BStatusNumeric)this.get(lastAuditDeviceCount);
   }

   public void setLastAuditDeviceCount(BStatusNumeric var1) {
      this.set(lastAuditDeviceCount, var1, (Context)null);
   }

   public BStatusNumeric getLastAuditFaultCount() {
      return (BStatusNumeric)this.get(lastAuditFaultCount);
   }

   public void setLastAuditFaultCount(BStatusNumeric var1) {
      this.set(lastAuditFaultCount, var1, (Context)null);
   }

   public BStatusBoolean getEnable() {
      return (BStatusBoolean)this.get(enable);
   }

   public void setEnable(BStatusBoolean var1) {
      this.set(enable, var1, (Context)null);
   }

   public BStatusEnum getAuditFrequency() {
      return (BStatusEnum)this.get(auditFrequency);
   }

   public void setAuditFrequency(BStatusEnum var1) {
      this.set(auditFrequency, var1, (Context)null);
   }

   public BTime getAuditTime() {
      return (BTime)this.get(auditTime);
   }

   public void setAuditTime(BTime var1) {
      this.set(auditTime, var1, (Context)null);
   }

   public BStatusEnum getAuditWeekday() {
      return (BStatusEnum)this.get(auditWeekday);
   }

   public void setAuditWeekday(BStatusEnum var1) {
      this.set(auditWeekday, var1, (Context)null);
   }

   public BStatusNumeric getInterDeviceDelay() {
      return (BStatusNumeric)this.get(interDeviceDelay);
   }

   public void setInterDeviceDelay(BStatusNumeric var1) {
      this.set(interDeviceDelay, var1, (Context)null);
   }

   public boolean getUseDeviceAlarmClass() {
      return this.getBoolean(useDeviceAlarmClass);
   }

   public void setUseDeviceAlarmClass(boolean var1) {
      this.setBoolean(useDeviceAlarmClass, var1, (Context)null);
   }

   public boolean getAuditAllDevicesOnStation() {
      return this.getBoolean(auditAllDevicesOnStation);
   }

   public void setAuditAllDevicesOnStation(boolean var1) {
      this.setBoolean(auditAllDevicesOnStation, var1, (Context)null);
   }

   public boolean getLogFaults() {
      return this.getBoolean(logFaults);
   }

   public void setLogFaults(boolean var1) {
      this.setBoolean(logFaults, var1, (Context)null);
   }

   public int getRetryCount() {
      return this.getInt(retryCount);
   }

   public void setRetryCount(int var1) {
      this.setInt(retryCount, var1, (Context)null);
   }

   public int getDebugLevel() {
      return this.getInt(debugLevel);
   }

   public void setDebugLevel(int var1) {
      this.setInt(debugLevel, var1, (Context)null);
   }

   public void runAudit() {
      this.invoke(runAudit, (BValue)null, (Context)null);
   }

   public void abortRunningAudit() {
      this.invoke(abortRunningAudit, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public BFacets getSlotFacets(Slot var1) {
      if (var1 == auditFrequency) {
         int[] var2 = new int[]{0, 1, 2, 3};
         String[] var3 = new String[]{"Audit_Disabled", "Audit_Manual", "Audit_Weekly", "Audit_Daily"};
         return BFacets.makeEnum(BEnumRange.make(var2, var3));
      } else {
         return super.getSlotFacets(var1);
      }
   }

   public void started() throws Exception {
      super.started();
      this.scheduleAuditTask(false);
   }

   public void stopped() throws Exception {
      super.stopped();
      this.abortAuditTask();
   }

   public void changed(Property var1, Context var2) {
      if (this.isRunning()) {
         if (var1 == enable) {
            if (this.getEnable().getValue()) {
               this.scheduleAuditTask(false);
            } else {
               this.abortAuditTask();
               this.setNextAuditTime(BAbsTime.DEFAULT);
            }
         } else if (var1 == auditFrequency) {
            if (this.getAuditFrequency().getValue().getOrdinal() == 2) {
               this.getAuditWeekday().setStatusNull(false);
            } else {
               this.getAuditWeekday().setStatusNull(true);
            }

            this.scheduleAuditTask(false);
         } else if (var1 == auditTime) {
            this.scheduleAuditTask(false);
         } else if (var1 == auditWeekday) {
            if (this.getAuditFrequency().getValue().getOrdinal() == 2) {
               this.scheduleAuditTask(false);
            } else {
               this.getAuditWeekday().setStatusNull(true);
            }
         }

      }
   }

   public void doRunAudit() {
      this.scheduleAuditTask(true);
   }

   public void doAbortRunningAudit() {
      this.abortAuditTask();
   }

   public final void dispatchAlarm(BSourceState var1, BDevice var2, String var3) {
      if (this.getEnable().getValue()) {
         BAlarmService var4 = (BAlarmService)Sys.getService(BAlarmService.TYPE);
         if (var4 != null) {
            String var8 = this.getDeviceAlarmSourceName().format(var2);
            BFacets var6 = BFacets.make("msgText", BString.make(var3), "sourceName", BString.make(var8));
            BAlarmRecord var5 = new BAlarmRecord();
            var5.setSource(BOrdList.make(var2.getNavOrd()));
            if (this.getUseDeviceAlarmClass()) {
               var5.setAlarmClass(var2.getAlarmSourceInfo().getAlarmClass());
            } else {
               var5.setAlarmClass(this.getAuditAlarmClass());
            }

            var5.setAlarmTransition(var1);
            var5.setSourceState(var1);
            var5.setAckRequired(true);
            var5.setAlarmData(var6);
            var4.routeAlarm(var5);
            if (this.getLogFaults() && var1.getOrdinal() != 0) {
               Log.getLog(this.getName()).message("Alarm = " + var8 + " = " + var1 + " / " + var3);
            } else {
               this.dbgMsg(18, "Alarm = " + var8 + " = " + var1 + " / " + var3);
            }
         }
      }

   }

   public void scheduleAuditTask(boolean var1) {
      try {
         BAbsTime var2;
         if (var1) {
            if (this.getEnable().getValue() && this.getAuditFrequency().getValue().getOrdinal() != 0) {
               var2 = BAbsTime.make();
            } else {
               this.dbgMsg("DALI Audit Task not scheduled - DISABLED");
               var2 = null;
            }
         } else {
            var2 = this.determineNextAuditTime();
         }

         if (var2 != null) {
            BComponent var3;
            if (this.getAuditAllDevicesOnStation()) {
               var3 = (BComponent)BOrd.make(STATION_ROOT).get();
            } else {
               var3 = (BComponent)this.getParent();
            }

            if (this.auditTask != null) {
               this.auditTask.cancel();
               this.auditTask = null;
            }

            this.auditTask = new DeviceAuditTask(var3);
            GregorianCalendar var4 = new GregorianCalendar();
            var4.set(var2.getYear(), var2.getMonth().getMonthOfYear() - 1, var2.getDay(), var2.getHour(), var2.getMinute(), var2.getSecond());
            this.dbgMsg("Scheduling DALI Audit Task for " + var4.getTime());
            if (this.auditTimer == null) {
               this.auditTimer = new Timer();
            }

            this.auditTimer.schedule(this.auditTask, var4.getTime());
            this.setNextAuditTime(var2);
            this.setStatusMessage("OK");
         } else {
            this.setNextAuditTime(BAbsTime.DEFAULT);
         }
      } catch (Exception var6) {
         this.reportError("Audit scheduling failed (" + var6 + ')');
      }

   }

   public BAbsTime determineNextAuditTime() {
      if (!this.getEnable().getValue()) {
         this.dbgMsg("DALI Audit Task not scheduled - DISABLED");
         return null;
      } else {
         BAbsTime var1 = BAbsTime.make();
         BAbsTime var2 = null;
         if (this.getAuditFrequency().getValue().getOrdinal() == 3) {
            var2 = BAbsTime.make(var1, this.getAuditTime());
            if (var2.isBefore(var1)) {
               var2 = var2.nextDay();
            }
         } else if (this.getAuditFrequency().getValue().getOrdinal() == 2) {
            int var3 = this.getAuditWeekday().getValue().getOrdinal();
            var2 = BAbsTime.make(var1, this.getAuditTime());
            if (var1.getWeekday().getOrdinal() != var3 || var2.isBefore(var1)) {
               var2 = var2.next(BWeekday.make(var3));
            }
         }

         if (this.getAuditFrequency().getValue().getOrdinal() == 1) {
            this.dbgMsg("DALI Audit Task not scheduled - MANUAL");
         }

         if (this.getAuditFrequency().getValue().getOrdinal() == 0) {
            this.dbgMsg("DALI Audit Task not scheduled - DISABLED");
         }

         return var2;
      }
   }

   private final synchronized void abortAuditTask() {
      if (this.auditTask != null) {
         this.auditTask.stop();
      }

   }

   private final String getNavPathStr(BOrd var1) {
      String var3 = "unknown";

      try {
         String var2 = var1.toString();
         int var4 = var2.indexOf("/Drivers/");
         if (var4 >= 0) {
            var3 = var2.substring(var4 + 9);
         } else {
            var3 = var2;
         }
      } catch (Exception var6) {
         var3 = "unknown";
      }

      return var3;
   }

   private final synchronized void dbgMsg(int var1, String var2) {
      if (this.getDebugLevel() >= var1) {
         Log.getLog(this.getName()).trace(var2);
      }

   }

   private final synchronized void dbgMsg(String var1) {
      if (this.getDebugLevel() > 0) {
         Log.getLog(this.getName()).trace(var1);
      }

   }

   private final synchronized void reportError(String var1) {
      this.setStatusMessage(var1);
      Log.getLog(this.getName()).error(var1);
   }

   public BDaliDeviceAuditTool() {
     this.auditTimer = new Timer();
     this.auditTask = null;
   }

   static {
      nextAuditTime = newProperty(257, BAbsTime.DEFAULT, (BFacets)null);
      lastAuditStart = newProperty(257, BAbsTime.DEFAULT, (BFacets)null);
      lastAuditDuration = newProperty(257, BRelTime.DEFAULT, (BFacets)null);
      lastAuditDeviceCount = newProperty(257, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(0));
      lastAuditFaultCount = newProperty(257, new BStatusNumeric(0.0, BStatus.nullStatus), BFacets.makeNumeric(0));
      enable = newProperty(256, new BStatusBoolean(true), (BFacets)null);
      auditFrequency = newProperty(256, new BStatusEnum(BDynamicEnum.make(3)), (BFacets)null);
      auditTime = newProperty(256, BTime.make(1, 0, 0), (BFacets)null);
      auditWeekday = newProperty(256, new BStatusEnum(BWeekday.saturday, BStatus.nullStatus), (BFacets)null);
      interDeviceDelay = newProperty(256, new BStatusNumeric(30.0), BFacets.makeNumeric(UnitDatabase.getUnit("second"), BInteger.make(0), BDouble.make(1.0), BDouble.make(1000000.0)));
      useDeviceAlarmClass = newProperty(256, false, (BFacets)null);
      auditAllDevicesOnStation = newProperty(256, false, (BFacets)null);
      logFaults = newProperty(256, false, (BFacets)null);
      retryCount = newProperty(4, 3, (BFacets)null);
      debugLevel = newProperty(4, 0, (BFacets)null);
      runAudit = newAction(0, (BFacets)null);
      abortRunningAudit = newAction(0, (BFacets)null);
      TYPE = Sys.loadType(BDaliDeviceAuditTool.class);
      STATION_ROOT = "station:|slot:/Drivers";
   }

   private class DeviceAuditTask extends TimerTask {
      private volatile BComponent auditRoot;
      private int deviceCount;
      private int faultCount;
      private volatile boolean auditTaskAbort;

      public void run() {
         BDaliDeviceAuditTool.this.dbgMsg("DALI Audit Task started");
         this.auditTaskAbort = false;
         BDaliDeviceAuditTool.this.getAuditInProgress().setValue(true);
         BDaliDeviceAuditTool.this.setLastAuditStart(BAbsTime.make());
         if (this.auditRoot == null) {
            BDaliDeviceAuditTool.this.reportError("Audit root not defined");
         } else {
            try {
               this.deviceCount = 0;
               this.faultCount = 0;
               long var1 = Clock.millis();
               this.TraverseComponents(this.auditRoot);
               long var3 = Clock.millis();
               long var5 = var3 - var1;
               BDaliDeviceAuditTool.this.dbgMsg("DALI Audit Task device count = " + this.deviceCount);
               BDaliDeviceAuditTool.this.dbgMsg("DALI Audit Task fault count = " + this.faultCount);
               BDaliDeviceAuditTool.this.dbgMsg("DALI Audit Task duration = " + BRelTime.make(var5));
               BDaliDeviceAuditTool.this.dbgMsg("DALI Audit Task completed");
               BDaliDeviceAuditTool.this.getLastAuditDeviceCount().setValue((double)this.deviceCount);
               BDaliDeviceAuditTool.this.getLastAuditDeviceCount().setStatusNull(false);
               BDaliDeviceAuditTool.this.getLastAuditFaultCount().setValue((double)this.faultCount);
               BDaliDeviceAuditTool.this.getLastAuditFaultCount().setStatusNull(false);
               BDaliDeviceAuditTool.this.setLastAuditDuration(BRelTime.make(var5));
               BDaliDeviceAuditTool.this.setStatusMessage("OK");
            } catch (InterruptedException var8) {
            } catch (Exception var9) {
               BDaliDeviceAuditTool.this.reportError("Audit task failed (" + var9 + ')');
               var9.printStackTrace();
               BDaliDeviceAuditTool.this.getLastAuditDeviceCount().setValue(0.0);
               BDaliDeviceAuditTool.this.setLastAuditDuration(BRelTime.DEFAULT);
            }
         }

         if (BDaliDeviceAuditTool.this.auditTask != null) {
            BDaliDeviceAuditTool.this.auditTask.cancel();
         }

         BDaliDeviceAuditTool.this.auditTask = null;
         BDaliDeviceAuditTool.this.getAuditInProgress().setValue(false);
         BDaliDeviceAuditTool.this.scheduleAuditTask(false);
      }

      public void TraverseComponents(BComponent var1) throws Exception {
         BDaliDeviceAuditTool.this.dbgMsg(47, "Nav = " + var1.getNavOrd());
         if (!this.auditTaskAbort) {
            if (var1 instanceof BGenericDaliDevice) {
               this.ProcessDaliDevice((BGenericDaliDevice)var1);
            } else {
               BComponent[] var2 = var1.getChildComponents();
               if (var2 != null) {
                  for(int var3 = 0; var3 < var2.length; ++var3) {
                     if (var2[var3] instanceof BComponent) {
                        this.TraverseComponents(var2[var3]);
                     }

                     if (this.auditTaskAbort) {
                        return;
                     }
                  }
               }
            }

         }
      }

      public void ProcessDaliDevice(BGenericDaliDevice var1) throws Exception {
         if (var1.getEnabled()) {
            ++this.deviceCount;
            short var3;
            if (var1 instanceof BSensorType1) {
               var3 = 153;
            } else {
               var3 = 144;
            }

            DaliQuery var2 = new DaliQuery(var1, var3);
            int var5 = 0;
            byte var6 = 3;

            int var4;
            for(var4 = -999; var5 < BDaliDeviceAuditTool.this.getRetryCount() && var6 != 0; ++var5) {
               var2.execute();
               var4 = var2.getResult();
               if (var4 < 0) {
                  var6 = 3;
               } else if (var1 instanceof BSensorType1) {
                  if (var4 == 254) {
                     var6 = 0;
                  } else {
                     var6 = 2;
                  }
               } else if ((var4 & 3) == 0) {
                  var6 = 0;
               } else if ((var4 & 3) == 2) {
                  var6 = 1;
               } else {
                  var6 = 2;
               }

               Thread.sleep(500L);
            }

            BDaliDeviceAuditTool.this.dbgMsg(27, BDaliDeviceAuditTool.this.getNavPathStr(var1.getNavOrd()) + " [ " + var4 + " / " + var6 + " ]");
            BDaliDeviceType var7 = var1.getDaliDeviceType();
            String var8 = "DALI " + var7.getText();
            if (var6 == 0) {
               var8 = var8 + " OK";
            } else if (var6 == 1) {
               var8 = var8 + " lamp failure";
            } else if (var6 == 2) {
               var8 = var8 + " device fault";
            } else if (var6 == 3) {
               var8 = var8 + " device down";
            }

            if (var6 == 0) {
               BDaliDeviceAuditTool.this.dispatchAlarm(BSourceState.normal, var1, var8);
            } else {
               BDaliDeviceAuditTool.this.dispatchAlarm(BSourceState.offnormal, var1, var8);
               ++this.faultCount;
            }

            BComponent[] var10 = var1.getChildComponents();
            if (var10 != null) {
               for(int var11 = 0; var11 < var10.length; ++var11) {
                  if (var10[var11] instanceof BDaliDeviceStatus) {
                     BDaliDeviceStatus var9 = (BDaliDeviceStatus)var10[var11];
                     if (var6 == 0) {
                        var9.setStatus(BStatus.ok);
                        var9.getOut().setValue(false);
                        var9.getOut().setStatus(BStatus.ok);
                        var9.setFaultCause("");
                     } else if (var6 == 3) {
                        var9.setStatus(BStatus.down);
                        var9.getOut().setValue(true);
                        var9.getOut().setStatus(BStatus.down);
                        var9.setFaultCause(var8);
                        var9.setLastFaultTime(BAbsTime.make());
                     } else {
                        var9.setStatus(BStatus.fault);
                        var9.getOut().setValue(true);
                        var9.getOut().setStatus(BStatus.fault);
                        var9.setFaultCause(var8);
                        var9.setLastFaultTime(BAbsTime.make());
                     }

                     var9.setLastUpdateTime(BAbsTime.make());
                     var9.setAuditMessage(var8);
                     break;
                  }
               }
            }

            Thread.sleep((long)(BDaliDeviceAuditTool.this.getInterDeviceDelay().getValue() * 1000.0));
         }
      }

      public synchronized void stop() {
         this.auditTaskAbort = true;
         BDaliDeviceAuditTool.this.dbgMsg("Aborting DALI Audit Task");
      }

      public DeviceAuditTask(BComponent var2) {
        this.auditRoot = null;
        this.deviceCount = 0;
        this.faultCount = 0;
        this.auditTaskAbort = false;
        
         this.auditRoot = var2;
      }
   }
}
