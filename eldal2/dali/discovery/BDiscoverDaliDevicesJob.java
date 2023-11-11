package uk.co.controlnetworksolutions.elitedali2.dali.discovery;

import javax.baja.job.BSimpleJob;
import javax.baja.log.Log;
import javax.baja.sys.BFacets;
import javax.baja.sys.Clock;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.util.BFolder;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public final class BDiscoverDaliDevicesJob extends BSimpleJob {
   public static final Property learnedDevices = newProperty(7, new BFolder(), (BFacets)null);
   public static final Type TYPE;
   private BDaliNetwork network;
   private boolean canceledFlag;
   private int discoveryDelay;
   private Log appLog;

   public final BFolder getLearnedDevices() {
      return (BFolder)this.get(learnedDevices);
   }

   public final void setLearnedDevices(BFolder var1) {
      this.set(learnedDevices, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void run(Context var1) {
      this.appLog.message("Starting DALI device discovery job ...");
      this.setProgress(0);
      long var3 = Clock.millis();
      boolean var7 = false;
      int var8 = 0;

      for(int var9 = 0; var9 < 64; ++var9) {
         DaliQuery var2 = new DaliQuery(this.network, var9, 153);
         var2.execute();
         if (!var2.dimResponded()) {
            this.appLog.error("No response from DIM during DALI discovery");
            this.log().message("ERROR: No response from DIM during discovery");
            var7 = true;
            break;
         }

         if (!var2.daliPowerPresent()) {
            this.appLog.error("No DALI power during DALI discovery");
            this.log().message("ERROR: No DALI power during discovery");
            var7 = true;
            break;
         }

         if (var2.daliReceiveCorrupted()) {
            this.appLog.error("Multiple response recevied for DALI address " + (var9 + 1));
            this.log().message("ERROR: Multiple response recevied for DALI address " + (var9 + 1));
            this.addDaliDevice(var9, -1, "Duplicate address");
            ++var8;
         } else if (!var2.daliDeviceResponded()) {
            this.appLog.trace("No response recevied for DALI address " + (var9 + 1));
         } else if (var2.daliDeviceResponded()) {
            int var10 = var2.getResult();
            if (var10 == 255) {
               var10 = this.getMultiDeviceType(var9);
            }

            this.addDaliDevice(var9, var10, "OK");
            ++var8;
         } else {
            this.appLog.trace("Query failed for DALI address " + (var9 + 1) + ": " + var2.getFaultText());
         }

         this.progress((var9 + 1) * 100 / 64);
         if (this.canceledFlag) {
            break;
         }

         try {
            Thread.sleep((long)this.discoveryDelay);
         } catch (InterruptedException var12) {
         }
      }

      long var5 = Clock.millis();
      if (!this.canceledFlag && !var7) {
         this.progress(100);
         this.logMessage("Discovery time = " + (var5 - var3) + " milliseconds");
         if (var8 == 0) {
            this.appLog.warning("No DALI devices discovered");
            this.log().message("WARNING: No DALI devices discovered");
         } else {
            this.logMessage(var8 + " DALI device" + (var8 == 1 ? "" : "s") + " discovered");
         }

         this.logMessage("DALI device discovery job completed");
      } else {
         if (!var7) {
            this.appLog.warning("DALI device discovery job was cancelled");
            this.log().message("DALI device discovery job was cancelled");
         }

         throw new Error();
      }
   }

   private final void addDaliDevice(int var1, int var2, String var3) {
      BDaliDeviceType var4 = BDaliDeviceType.getDaliDeviceType(var2);
      if (var4 != null) {
         this.addLearnedDevice(var1, var4, var3);
         this.logMessage("Discovered DALI device with address " + (var1 + 1) + " of type '" + var4.toString() + '\'');
      } else {
         this.addLearnedDevice(var1, BDaliDeviceType.unknownDevice, var3);
         this.logMessage("Discovered DALI device with address " + (var1 + 1) + " of unknown type");
      }

   }

   public final void addLearnedDevice(int var1, BDaliDeviceType var2, String var3) {
      String var4 = Integer.toString(var1 + 1);
      String var5 = "device" + var4;
      if (this.getLearnedDevices().get(var5) == null) {
         this.getLearnedDevices().add(var5, new BDaliDiscoverDeviceEntry(var4, var2, var3));
      }

   }

   public final int getMultiDeviceType(int var1) {
      int var3 = 1000;
      int var4 = 1000;

      for(int var5 = 0; var5 <= 8; ++var5) {
         DaliQuery var2 = new DaliQuery(this.network, var1, 255);
         var2.setDaliDeviceType(var5);
         var2.execute();
         if (var2.daliDeviceResponded()) {
            if (var3 == 1000) {
               var3 = var5;
            }

            var4 = var5;
         }
      }

      if (var4 != 8) {
         var4 = var3;
      }

      return var4;
   }

   public final void doCancel(Context var1) {
      this.canceledFlag = true;
   }

   public final void setDiscoveryDelay(int var1) {
      this.discoveryDelay = var1;
   }

   private final void logMessage(String var1) {
      if (this.appLog != null) {
         this.appLog.message(var1);
      }

      this.log().message(var1);
   }

   public BDiscoverDaliDevicesJob() {
     this.network = null;
     this.canceledFlag = false;
     this.discoveryDelay = 10;
     this.appLog = Report.daliNetwork;
   }

   public BDiscoverDaliDevicesJob(BDaliNetwork var1) {
     this.network = null;
     this.canceledFlag = false;
     this.discoveryDelay = 10;
     this.appLog = Report.daliNetwork;
     
      this.network = var1;
   }

   static {
      TYPE = Sys.loadType(BDiscoverDaliDevicesJob.class);
   }
}
