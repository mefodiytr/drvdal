package uk.co.controlnetworksolutions.elitedali2.dali.addressing;

import javax.baja.job.BSimpleJob;
import javax.baja.log.Log;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public final class BDaliDeviceAddressingJob extends BSimpleJob {
   public static final Type TYPE;
   private static final Log addressingLog;
   private JobTask taskJob;
   private boolean jobInProgress;
   private boolean jobSucceededFlag;

   public final Type getType() {
      return TYPE;
   }

   public final void run(Context var1) throws Exception {
      this.jobInProgress = true;
      this.jobSucceededFlag = false;

      try {
         if (this.taskJob != null) {
            this.taskJob.execute();
         }
      } catch (Exception var3) {
         this.log().message("ERROR: DALI device addressing job failed");
         addressingLog.error("DALI device addressing job failed");
         var3.printStackTrace();
      }

      this.jobInProgress = false;
      if (!this.jobSucceededFlag) {
         throw new Error();
      }
   }

   public final void doCancel(Context var1) {
      if (this.taskJob != null) {
         this.taskJob.cancel();
      }

   }

   public final boolean isJobRunning() {
      return this.jobInProgress;
   }

   public final DaliTaskReport getReporter() {
      return new AddressJobReporter();
   }

   public BDaliDeviceAddressingJob() {
     this.taskJob = null;
     this.jobInProgress = false;
     this.jobSucceededFlag = false;
     
      this.taskJob = null;
      this.jobInProgress = false;
      this.jobSucceededFlag = false;
   }

   public BDaliDeviceAddressingJob(JobTask var1) {
     this.taskJob = null;
     this.jobInProgress = false;
     this.jobSucceededFlag = false;
     
      this.taskJob = var1;
      this.jobInProgress = false;
      this.jobSucceededFlag = false;
   }

   static {
      TYPE = Sys.loadType(BDaliDeviceAddressingJob.class);
      addressingLog = Report.daliAddressing;
   }

   public interface JobTask {
      void execute();

      void cancel();
   }

   private class AddressJobReporter implements DaliTaskReport {
      public void logTrace(String var1) {
         BDaliDeviceAddressingJob.addressingLog.trace(var1);
      }

      public void logMessage(String var1) {
         BDaliDeviceAddressingJob.this.log().message(var1);
         BDaliDeviceAddressingJob.addressingLog.message(var1);
      }

      public void logWarning(String var1) {
         BDaliDeviceAddressingJob.this.log().message("WARNING: " + var1);
         BDaliDeviceAddressingJob.addressingLog.warning(var1);
      }

      public void logError(String var1) {
         BDaliDeviceAddressingJob.this.log().message("ERROR: " + var1);
         BDaliDeviceAddressingJob.addressingLog.error(var1);
      }

      public void reportProgress(int var1) {
         BDaliDeviceAddressingJob.this.progress(var1);
      }

      public void reportFailure(String var1) {
         BDaliDeviceAddressingJob.this.log().endFailed(var1);
      }

      public void reportSuccess() {
         BDaliDeviceAddressingJob.this.log().endSuccess();
         BDaliDeviceAddressingJob.this.jobSucceededFlag = true;
      }

      private AddressJobReporter() {
      }
   }
}
