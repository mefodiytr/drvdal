package uk.co.controlnetworksolutions.elitedali2.dim.network;

import javax.baja.job.BSimpleJob;
import javax.baja.log.Log;
import javax.baja.sys.BFacets;
import javax.baja.sys.Clock;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.util.BFolder;
import uk.co.controlnetworksolutions.elitedali2.dim.ops.DimPing;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public class BDimLearnDevicesJob extends BSimpleJob {
   public static final Property learnedDevices = newProperty(7, new BFolder(), (BFacets)null);
   public static final Type TYPE;
   private static final int DIM_DISOVERY_TIMEOUT = 30;
   private static final int DIM_DISOVERY_RETRY_COUNT = 3;
   private static final Log discoveryLog;
   private BDimNetwork dimNetwork;
   private boolean cancelled;

   public BFolder getLearnedDevices() {
      return (BFolder)this.get(learnedDevices);
   }

   public void setLearnedDevices(BFolder var1) {
      this.set(learnedDevices, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   private final void addLearnedDevice(int var1) {
      String var2 = "device" + var1;
      if (this.getLearnedDevices().get(var2) == null) {
         this.getLearnedDevices().add(var2, new BDimLearnDeviceEntry(var1));
      }

   }

   public void run(Context var1) throws Exception {
      this.logMessage("DIM device discovery started ...");
      this.setProgress(0);
      if (this.dimNetwork.getSerialPortConfig().getPortName().equalsIgnoreCase("none")) {
         this.logError("Serial port has not been defined");
         throw new Error();
      } else {
         double var6 = 0.41841004184100417;
         int var8 = 0;
         long var2 = Clock.millis();
         DimPing var10 = new DimPing(this.dimNetwork, 244);
         var10.setTimeout(30);
         var10.setRetryCount(3);
         var10.execute();

         for(int var9 = 1; var9 <= 240; ++var9) {
            var10 = new DimPing(this.dimNetwork, var9);
            var10.setTimeout(30);
            var10.setRetryCount(3);
            var10.execute();
            if (var10.dimResponded()) {
               this.logMessage("DIM found with address " + var9);
               this.addLearnedDevice(var9);
               ++var8;
            } else {
               discoveryLog.trace("No DIM found with address " + var9);
            }

            this.progress((int)(var6 * (double)(var9 - 1)));
            if (this.cancelled) {
               break;
            }
         }

         long var4 = Clock.millis();
         this.logMessage(var8 + " DIM devices found");
         this.logMessage("DIM device discovery time = " + (var4 - var2) + " milliseconds");
         if (this.cancelled) {
            this.logMessage("DIM device discovery cancelled");
            throw new Error();
         } else {
            this.progress(100);
            this.logMessage("DIM device discovery succeeded");
         }
      }
   }

   public void doCancel(Context var1) {
      this.cancelled = true;
   }

   public void cancel() {
      super.cancel();
      this.cancelled = true;
   }

   public void logMessage(String var1) {
      this.log().message(var1);
      discoveryLog.message(var1);
   }

   public void logError(String var1) {
      this.log().message("ERROR: " + var1);
      discoveryLog.error(var1);
   }

   public BDimLearnDevicesJob() {
     this.dimNetwork = null;
     this.cancelled = false;
     
      this.dimNetwork = null;
   }

   public BDimLearnDevicesJob(BDimNetwork var1) {
     this.dimNetwork = null;
     this.cancelled = false;
     
      this.dimNetwork = var1;
   }

   static {
      TYPE = Sys.loadType(BDimLearnDevicesJob.class);
      discoveryLog = Report.dim;
   }
}
