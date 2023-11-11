package uk.co.controlnetworksolutions.elitedali2.dali.network;

import javax.baja.log.Log;
import javax.baja.sys.Action;
import javax.baja.sys.BComponent;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.config.DaliDeviceConfig;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public final class BDaliNetworkUtils extends BComponent {
   public static final Property statusMessage = newProperty(265, "", (BFacets)null);
   public static final Action abortAllIdentifyTasks = newAction(0, (BFacets)null);
   public static final Action clearSensorGroups = newAction(128, (BFacets)null);
   public static final Action clearAllAddresses = newAction(128, (BFacets)null);
   public static final Action resetSingleDevice = newAction(0, BInteger.make(1), (BFacets)null);
   public static final Action resetAllDevices = newAction(128, (BFacets)null);
   public static final Type TYPE;
   private BDaliNetwork daliNetwork;
   private Log log;

   public final String getStatusMessage() {
      return this.getString(statusMessage);
   }

   public final void setStatusMessage(String var1) {
      this.setString(statusMessage, var1, (Context)null);
   }

   public final void abortAllIdentifyTasks() {
      this.invoke(abortAllIdentifyTasks, (BValue)null, (Context)null);
   }

   public final void clearSensorGroups() {
      this.invoke(clearSensorGroups, (BValue)null, (Context)null);
   }

   public final void clearAllAddresses() {
      this.invoke(clearAllAddresses, (BValue)null, (Context)null);
   }

   public final void resetSingleDevice(BInteger var1) {
      this.invoke(resetSingleDevice, var1, (Context)null);
   }

   public final void resetAllDevices() {
      this.invoke(resetAllDevices, (BValue)null, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final Type[] getServiceTypes() {
      return null;
   }

   public final void started() {
      this.setStatusMessage("");
      this.resolveNetworkAccess();
   }

   public final void doAbortAllIdentifyTasks() {
      if (this.daliNetwork != null) {
         this.daliNetwork.setIdentifyAbortFlag(true);
      }

   }

   public final void doClearSensorGroups() {
      this.log.message("Clearing All Sensor Groups");
      if (this.daliNetwork != null) {
         for(int var1 = 0; var1 < 64; ++var1) {
            DaliQuery var2 = new DaliQuery(this.daliNetwork, var1, 153);
            if (var2.getResult() == 254) {
               this.log.message("Found sensor with address " + (var1 + 1));
               DaliDeviceConfig var3 = new DaliDeviceConfig(this.daliNetwork, var1);
               var3.programGroups(0);
            }
         }

         this.log.message("Sensor Group Clear Complete");
      } else {
         this.setStatusMessage("ERROR: Invalid network");
      }

   }

   public final void doClearAllAddresses() {
      if (this.daliNetwork != null) {
         this.updateStatus("Clearing all DALI device short addresses");
         this.daliNetwork.specialCommand(163, 255);
         this.daliNetwork.broadcastCommand(128);
         this.daliNetwork.broadcastCommand(128);
         this.daliNetwork.specialCommand(163, 255);
         this.daliNetwork.specialCommand(163, 255);
         this.daliNetwork.broadcastCommand(128);
         this.daliNetwork.broadcastCommand(128);
         this.daliNetwork.broadcastCommand(128);
         this.daliNetwork.broadcastCommand(128);
         this.updateStatus("All DALI device short addresses cleared");
      } else {
         this.setStatusMessage("ERROR: Invalid network");
      }

   }

   public final void doResetSingleDevice(BInteger var1) {
      if (this.daliNetwork != null) {
         this.updateStatus("Resetting DALI device with short address " + var1.getInt());
         DaliCommand var2 = new DaliCommand(this.daliNetwork, false, var1.getInt(), 32);
         var2.setTransmitRepeatCount(1);
         var2.execute();

         try {
            Thread.sleep(350L);
         } catch (Exception var4) {
         }

         var2 = new DaliCommand(this.daliNetwork, false, var1.getInt() - 1, 32);
         var2.setTransmitRepeatCount(1);
         var2.execute();
         this.updateStatus("DALI device reset");
      } else {
         this.setStatusMessage("ERROR: Invalid network");
      }

   }

   public final void doResetAllDevices() {
      if (this.daliNetwork != null) {
         this.updateStatus("Resetting all DALI devices");
         DaliCommand var1 = new DaliCommand(this.daliNetwork, true, 63, 32);
         var1.setTransmitRepeatCount(1);
         var1.execute();

         try {
            Thread.sleep(350L);
         } catch (Exception var3) {
         }

         var1 = new DaliCommand(this.daliNetwork, true, 63, 32);
         var1.setTransmitRepeatCount(1);
         var1.execute();
         this.updateStatus("DALI devices reset");
      } else {
         this.setStatusMessage("ERROR: Invalid network");
      }

   }

   private final void updateStatus(String var1) {
      if (this.daliNetwork != null) {
         this.log.message("[" + this.daliNetwork.getName() + "] " + var1);
      }

      this.setStatusMessage(var1);
   }

   private final void resolveNetworkAccess() {
      this.daliNetwork = null;
      BComponent var1 = (BComponent)this.getParent();
      if (var1.getType() == BDaliNetwork.TYPE) {
         this.daliNetwork = (BDaliNetwork)var1;
         this.setStatusMessage("OK");
      } else {
         this.log.error("Network Utils parent is not a DALI network");
         this.setStatusMessage("ERROR: Network Utils parent is not a DALI network");
      }

   }

   public BDaliNetworkUtils() {
     this.daliNetwork = null;
     this.log = Report.daliNetwork;
   }

   static {
      TYPE = Sys.loadType(BDaliNetworkUtils.class);
   }
}
