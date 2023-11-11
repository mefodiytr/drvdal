package uk.co.controlnetworksolutions.elitedali2.dali.addressing;

import javax.baja.naming.BOrd;
import javax.baja.sys.Context;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;

public final class DaliAddressingTaskModeA implements DaliAddressingTask {
   private BDaliNetwork network;
   private AddressingSearchTask searchTask;
   private DaliAddressingSearch search;
   private BDaliDeviceAddressingJob addressingJob;
   private DaliTaskReport report;
   private int tridonicClass;
   private int tenacityLevel;

   public final BOrd addressAll() {
      this.search = null;
      this.addressingJob = null;
      this.report = null;
      this.searchTask = new AddressingSearchTask();
      if (this.searchTask != null) {
         this.searchTask.setTypeAll();
         this.searchTask.setTridonicClass(this.tridonicClass);
         this.searchTask.setTenacityLevel(this.tenacityLevel);
         this.addressingJob = new BDaliDeviceAddressingJob(this.searchTask);
         this.report = this.addressingJob.getReporter();
         if (this.addressingJob != null) {
            return this.addressingJob.submit((Context)null);
         }
      }

      return null;
   }

   public final BOrd addressNew() {
      this.search = null;
      this.addressingJob = null;
      this.report = null;
      this.searchTask = new AddressingSearchTask();
      if (this.searchTask != null) {
         this.searchTask.setTypeNew();
         this.searchTask.setTridonicClass(this.tridonicClass);
         this.searchTask.setTenacityLevel(this.tenacityLevel);
         this.addressingJob = new BDaliDeviceAddressingJob(this.searchTask);
         this.report = this.addressingJob.getReporter();
         if (this.addressingJob != null) {
            return this.addressingJob.submit((Context)null);
         }
      }

      return null;
   }

   public final void setTridonicClass(int var1) {
      this.tridonicClass = var1;
   }

   public final void setTenacityLevel(int var1) {
      this.tenacityLevel = var1;
   }

   public final boolean isRunning() {
      return this.addressingJob != null ? this.addressingJob.isJobRunning() : false;
   }

   public final boolean isSuccessful() {
      return this.search != null ? this.search.isSuccessful() : false;
   }

   public final int getDuration() {
      return this.search != null ? this.search.getDuration() : 0;
   }

   public final int getDeviceCount() {
      return this.search != null ? this.search.getDeviceCount() : 0;
   }

   public final int getWarningCount() {
      return this.search != null ? this.search.getWarningCount() : 0;
   }

   public final void cancel() {
      if (this.addressingJob != null) {
         this.addressingJob.doCancel((Context)null);
      }

   }

   public DaliAddressingTaskModeA(BDaliNetwork var1) {
     this.network = null;
     this.searchTask = null;
     this.search = null;
     this.addressingJob = null;
     this.report = null;
     this.tridonicClass = -1;
     this.tenacityLevel = 1;
     
      this.network = var1;
      this.addressingJob = null;
      this.report = null;
      this.search = null;
   }

   public class AddressingSearchTask implements BDaliDeviceAddressingJob.JobTask {
      public void setTypeAll() {
         DaliAddressingTaskModeA.this.search.setTypeAll();
      }

      public void setTypeNew() {
         DaliAddressingTaskModeA.this.search.setTypeNew();
      }

      public void setTridonicClass(int var1) {
         if (var1 >= 0) {
            DaliAddressingTaskModeA.this.search.setTridonicClass(var1);
         }

      }

      public void setTenacityLevel(int var1) {
         if (var1 >= 0) {
            DaliAddressingTaskModeA.this.search.setTenacityLevel(var1);
         }

      }

      public void execute() {
         DaliAddressingTaskModeA.this.search.setReporter(DaliAddressingTaskModeA.this.report);
         DaliAddressingTaskModeA.this.search.start();
      }

      public void cancel() {
         DaliAddressingTaskModeA.this.search.cancel();
      }

      public AddressingSearchTask() {
         DaliAddressingTaskModeA.this.search = new DaliAddressingSearch(DaliAddressingTaskModeA.this.network);
      }
   }
}
