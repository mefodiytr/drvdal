package uk.co.controlnetworksolutions.elitedali2.dali.utils;

import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;

public abstract class PollQueryTask {
   public static final int DEFAULT_POLL_PERIOD = 1000;
   public static final int DEFAULT_MINIMUM_POLL_PERIOD = 500;
   public static final int DEFAULT_MAXIMUM_POLL_PERIOD = 1000000000;
   protected BDaliNetwork daliNetwork;
   protected int daliAddress;
   protected BGenericDaliDevice daliDevice;
   protected volatile Thread pollThread;
   protected volatile boolean stopFlag;
   protected volatile int daliQueryCode;
   protected volatile int pollPeriod;
   protected volatile int minimumPollPeriod;
   private int retryCount;
   private int retryMax;
   private int faultCount;

   public void setDaliQueryCode(int var1) {
      this.daliQueryCode = var1;
   }

   public void setPollPeriod(int var1) {
      if (var1 < 500) {
         this.pollPeriod = 500;
      } else {
         this.pollPeriod = var1;
      }

   }

   public void setMinimumPollPeriod(int var1) {
      this.minimumPollPeriod = var1;
   }

   public void setRetryMax(int var1) {
      this.retryMax = var1;
   }

   public synchronized void start() {
      this.pollThread = new Thread(new PollingThread());
      this.pollThread.start();
   }

   public synchronized void stop() {
      if (this.pollThread != null) {
         this.stopFlag = true;
         this.pollThread = null;
      }

   }

   public abstract void processQueryResult(int var1);

   public abstract void processQueryFailure(int var1);

   public PollQueryTask(BDaliNetwork var1, int var2, int var3, int var4) {
     this.daliNetwork = null;
     this.daliAddress = 0;
     this.daliDevice = null;
     this.pollThread = null;
     this.stopFlag = false;
     this.daliQueryCode = 0;
     this.pollPeriod = 1000;
     this.minimumPollPeriod = 500;
     this.retryCount = 0;
     this.retryMax = 2;
     this.faultCount = 0;
     
      this.daliNetwork = var1;
      this.daliAddress = var2;
      this.daliQueryCode = var3;
      this.setPollPeriod(var4);
   }

   public PollQueryTask(BGenericDaliDevice var1, int var2, int var3) {
     this.daliNetwork = null;
     this.daliAddress = 0;
     this.daliDevice = null;
     this.pollThread = null;
     this.stopFlag = false;
     this.daliQueryCode = 0;
     this.pollPeriod = 1000;
     this.minimumPollPeriod = 500;
     this.retryCount = 0;
     this.retryMax = 2;
     this.faultCount = 0;
     
      this.daliDevice = var1;
      this.daliQueryCode = var2;
      this.setPollPeriod(var3);
   }

   public class PollingThread implements Runnable {
      public void run() {
         PollQueryTask.this.stopFlag = false;

         while(!PollQueryTask.this.stopFlag) {
            try {
               this.updateSensorData();
            } catch (InterruptedException var4) {
               PollQueryTask.this.stopFlag = true;
               break;
            } catch (Exception var5) {
            }

            try {
               Thread.sleep((long)PollQueryTask.this.pollPeriod);
            } catch (InterruptedException var2) {
               PollQueryTask.this.stopFlag = true;
               break;
            } catch (Exception var3) {
            }
         }

      }

      public void updateSensorData() throws Exception {
         DaliQuery var1 = null;
         if (PollQueryTask.this.daliDevice != null) {
            var1 = new DaliQuery(PollQueryTask.this.daliDevice, PollQueryTask.this.daliQueryCode);
         } else if (PollQueryTask.this.daliNetwork != null) {
            var1 = new DaliQuery(PollQueryTask.this.daliNetwork, PollQueryTask.this.daliAddress, PollQueryTask.this.daliQueryCode);
         }

         if (var1 != null) {
            var1.execute();
            PollQueryTask.this.retryCount = 0;

            PollQueryTask var10000;
            while((var1.getDaliStatus() == 1 || var1.getDaliStatus() == 3) && PollQueryTask.this.retryCount < PollQueryTask.this.retryMax) {
               Thread.sleep(133L);
               var1.execute();
               var10000 = PollQueryTask.this;
               var10000.retryCount = var10000.retryCount + 1;
            }

            if (var1.getDaliStatus() == 53 && var1.getResult() >= 0) {
               PollQueryTask.this.processQueryResult(var1.getResult());
            } else {
               var10000 = PollQueryTask.this;
               var10000.faultCount = var10000.faultCount + 1;
               PollQueryTask.this.processQueryFailure(var1.getDaliStatus());
            }
         }

      }
   }
}
