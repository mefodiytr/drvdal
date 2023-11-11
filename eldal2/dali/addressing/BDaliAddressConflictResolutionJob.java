package uk.co.controlnetworksolutions.elitedali2.dali.addressing;

import javax.baja.job.BJobState;
import javax.baja.job.BSimpleJob;
import javax.baja.log.Log;
import javax.baja.sys.Clock;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;

public final class BDaliAddressConflictResolutionJob extends BSimpleJob {
   public static final Type TYPE;
   private BDaliNetwork daliNetwork;
   private boolean jobInProgress;
   private boolean fault;
   private boolean abort;
   private int conflictCount;
   private boolean passiveMode;
   private int responseCheckMax;
   private Log log;

   public final Type getType() {
      return TYPE;
   }

   public final void initialise(BDaliNetwork var1) {
      this.daliNetwork = var1;
      this.jobInProgress = false;
      this.fault = false;
      this.abort = false;
      this.conflictCount = 0;
      this.passiveMode = false;
   }

   public final void run(Context var1) throws Exception {
      this.logMessage("Searching for DALI address conflicts");
      if (this.daliNetwork == null) {
         this.logError("Address conflict resolution failed: invalid network");
         throw new Error();
      } else {
         this.jobInProgress = true;
         this.fault = false;
         this.progress(0);
         long var5 = Clock.millis();

         try {
            if (!this.daliNetwork.isNetworkOk()) {
               this.logError("Address conflict resolution failed: invalid network state");
               throw new Error();
            }

            if (this.daliNetwork.isAddressingJobInProgress()) {
               this.logError("Address conflict resolution failed: a DALI addressing job is in progress");
               throw new Error();
            }

            this.conflictCount = 0;

            for(int var2 = 0; var2 < 64; ++var2) {
               int var4 = 0;

               for(int var3 = 0; var3 < this.responseCheckMax; ++var3) {
                  if (this.conflictQuery(var2, 153)) {
                     ++var4;
                  }

                  if (this.conflictQuery(var2, 160)) {
                     ++var4;
                  }

                  if (this.conflictQuery(var2, 152)) {
                     ++var4;
                  }

                  if (this.conflictQuery(var2, 194)) {
                     ++var4;
                  }

                  if (this.conflictQuery(var2, 195)) {
                     ++var4;
                  }

                  if (this.conflictQuery(var2, 196)) {
                     ++var4;
                  }

                  if (this.abort || this.fault) {
                     break;
                  }
               }

               this.log.trace("Conflict count for address " + (var2 + 1) + " = " + var4);
               if (this.abort || this.fault) {
                  break;
               }

               if (!this.passiveMode && var4 > 0) {
                  this.logMessage("Conflict found at DALI address " + (var2 + 1) + ", clearing ...");
                  if (!BGenericDaliDevice.changeDaliShortAddress(this.daliNetwork, var2, 0, true, false)) {
                     this.logError("Failed to clear address " + (var2 + 1));
                  }

                  ++this.conflictCount;
               }

               this.progress((var2 + 1) * 97 / 64);
            }

            this.logMessage("Number of DALI address conflicts found = " + this.conflictCount);
            if (this.abort) {
               this.logMessage("DALI address conflict resolution ABORTED");
            } else {
               if (!this.passiveMode && !this.fault) {
                  if (this.daliNetwork.isAddressingJobInProgress()) {
                     this.fault = true;
                     this.logError("Address conflict resolution failed: a DALI addressing job is in progress");
                  } else {
                     this.logMessage("Addressing unaddressed DALI devices");
                     this.daliNetwork.submitAddressNewDevicesJob();
                  }
               }

               long var7 = Clock.millis();
               if (!this.fault) {
                  this.logMessage("DALI address conflict resolution complete");
               } else {
                  this.logError("DALI address conflict resolution FAILED");
               }

               this.logMessage("DALI address conflict resolution time = " + (var7 - var5) + " milliseconds");
            }
         } catch (Exception var10) {
            this.logError("Address conflict resolution failed: " + var10);
            this.fault = true;
         }

         this.progress(100);
         this.jobInProgress = false;
         if (!this.fault && !this.abort) {
            this.success();
         } else {
            this.setJobState(BJobState.failed);
            throw new Error();
         }
      }
   }

   public final boolean conflictQuery(int var1, int var2) {
      DaliQuery var3 = new DaliQuery(this.daliNetwork, var1, var2);
      var3.confirmResult();
      int var4 = var3.getDaliStatus();
      if (var4 == 3) {
         return true;
      } else {
         if (var4 != 53 && var4 != 1) {
            this.fault = true;
         }

         return false;
      }
   }

   public final void doCancel(Context var1) {
      this.abort = true;
   }

   public final void logMessage(String var1) {
      this.log.message(var1);
      this.log().message(var1);
   }

   public final void logError(String var1) {
      this.log.error(var1);
      this.log().message("ERROR: " + var1);
   }

   public final void setPassiveMode(boolean var1) {
      this.passiveMode = var1;
   }

   public final int getConflictCount() {
      return this.conflictCount;
   }

   public final void setResponseCheckMax(int var1) {
      this.responseCheckMax = var1;
   }

   public final boolean isJobRunning() {
      return this.jobInProgress;
   }

   public BDaliAddressConflictResolutionJob() {
     this.daliNetwork = null;
     this.jobInProgress = false;
     this.fault = false;
     this.abort = false;
     this.conflictCount = 0;
     this.passiveMode = false;
     this.responseCheckMax = 3;
     this.log = Log.getLog("elitedali2.resolveConflicts");
     
      this.initialise((BDaliNetwork)null);
   }

   public BDaliAddressConflictResolutionJob(BDaliNetwork var1) {
     this.daliNetwork = null;
     this.jobInProgress = false;
     this.fault = false;
     this.abort = false;
     this.conflictCount = 0;
     this.passiveMode = false;
     this.responseCheckMax = 3;
     this.log = Log.getLog("elitedali2.resolveConflicts");
     
      this.initialise(var1);
   }

   public BDaliAddressConflictResolutionJob(BDaliNetwork var1, int var2) {
     this.daliNetwork = null;
     this.jobInProgress = false;
     this.fault = false;
     this.abort = false;
     this.conflictCount = 0;
     this.passiveMode = false;
     this.responseCheckMax = 3;
     this.log = Log.getLog("elitedali2.resolveConflicts");
     
      this.initialise(var1);
      this.responseCheckMax = var2;
   }

   static {
      TYPE = Sys.loadType(BDaliAddressConflictResolutionJob.class);
   }
}
