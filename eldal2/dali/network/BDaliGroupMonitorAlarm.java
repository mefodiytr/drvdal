package uk.co.controlnetworksolutions.elitedali2.dali.network;

import javax.baja.alarm.BAckState;
import javax.baja.alarm.BAlarmClass;
import javax.baja.alarm.BAlarmRecord;
import javax.baja.alarm.BAlarmService;
import javax.baja.alarm.BIAlarmSource;
import javax.baja.alarm.BSourceState;
import javax.baja.driver.BDevice;
import javax.baja.log.Log;
import javax.baja.naming.BOrdList;
import javax.baja.status.BStatusBoolean;
import javax.baja.sys.Action;
import javax.baja.sys.BBoolean;
import javax.baja.sys.BComponent;
import javax.baja.sys.BFacets;
import javax.baja.sys.BString;
import javax.baja.sys.Clock;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.util.BFormat;

public final class BDaliGroupMonitorAlarm extends BComponent implements BIAlarmSource {
   public static final Property alarmEnable = newProperty(256, new BStatusBoolean(false), (BFacets)null);
   public static final Property alarmClass = newProperty(256, "defaultAlarmClass", BFacets.make("fieldEditor", BString.make("alarm:AlarmClassFE")));
   public static final Property sourceName = newProperty(0, BFormat.make("%parent.parent.parent.parent.displayName%/%parent.parent.parent.displayName%/%parent.displayName%/%displayName%"), (BFacets)null);
   public static final Property logAlarms = newProperty(4, false, (BFacets)null);
   public static final Action ackAlarm = newAction(5, new BAlarmRecord(), (BFacets)null);
   public static final Type TYPE;
   public static final BSourceState NORMAL;
   public static final BSourceState FAULT;

   public final BStatusBoolean getAlarmEnable() {
      return (BStatusBoolean)this.get(alarmEnable);
   }

   public final void setAlarmEnable(BStatusBoolean var1) {
      this.set(alarmEnable, var1, (Context)null);
   }

   public final String getAlarmClass() {
      return this.getString(alarmClass);
   }

   public final void setAlarmClass(String var1) {
      this.setString(alarmClass, var1, (Context)null);
   }

   public final BFormat getSourceName() {
      return (BFormat)this.get(sourceName);
   }

   public final void setSourceName(BFormat var1) {
      this.set(sourceName, var1, (Context)null);
   }

   public final boolean getLogAlarms() {
      return this.getBoolean(logAlarms);
   }

   public final void setLogAlarms(boolean var1) {
      this.setBoolean(logAlarms, var1, (Context)null);
   }

   public final BBoolean ackAlarm(BAlarmRecord var1) {
      return (BBoolean)this.invoke(ackAlarm, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void dispatch(BSourceState var1, BDevice var2, String var3) {
      if (this.getAlarmEnable().getValue()) {
         BAlarmService var4 = (BAlarmService)Sys.getService(BAlarmService.TYPE);
         if (var4 != null) {
            String var8 = this.getSourceName().format(var2);
            BFacets var7 = BFacets.make("msgText", BString.make(var3), "sourceName", BString.make(var8));
            BAlarmRecord var6 = new BAlarmRecord();
            var6.setSource(BOrdList.make(this.getNavOrd()));
            var6.setAlarmClass(this.getAlarmClass());
            var6.setAlarmTransition(var1);
            var6.setSourceState(var1);
            BAlarmClass var5 = var4.lookupAlarmClass(this.getAlarmClass());
            if (var5 != null) {
               var6.setAckRequired(var5.getAckRequired().includes(var1));
            } else {
               var6.setAckRequired(false);
            }

            var6.setAlarmData(var7);
            var4.routeAlarm(var6);
            if (this.getLogAlarms()) {
               Log.getLog("elitedali2.alarm").trace(var8 + " = " + var1 + " / " + var3);
            }
         }
      }

   }

   public final BBoolean doAckAlarm(BAlarmRecord var1) {
      BAlarmService var2 = (BAlarmService)Sys.getService(BAlarmService.TYPE);
      if (var2 != null) {
         var1.setAckTime(Clock.time());
         var1.setAckState(BAckState.acked);

         try {
            var2.routeAlarm(var1);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return BBoolean.TRUE;
   }

   static {
      TYPE = Sys.loadType(BDaliGroupMonitorAlarm.class);
      NORMAL = BSourceState.normal;
      FAULT = BSourceState.offnormal;
   }
}
