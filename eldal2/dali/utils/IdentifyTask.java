package uk.co.controlnetworksolutions.elitedali2.dali.utils;

import javax.baja.sys.BComponent;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliLevel;
import uk.co.controlnetworksolutions.elitedali2.dali.tools.BDaliIdentifyControl;

public class IdentifyTask {
   public static final int IDENTIFY_MODE_LEVEL = 1;
   public static final int IDENTIFY_MODE_MAX_OFF = 2;
   public static final int IDENTIFY_MODE_MAX_MIN = 3;
   private volatile Thread identifyThread;
   private volatile IdentifyProcess localIdentifyProcess;
   private volatile BDaliIdentifyControl identifyControl;
   private volatile int identifyMode;
   private volatile int identifyOnPeriod;
   private volatile int identifyOffPeriod;
   private volatile int maxIdentifyTime;

   public void setMode(int var1) {
      this.identifyMode = var1;
   }

   public void setPeriods(int var1, int var2) {
      this.identifyOnPeriod = var1;
      this.identifyOffPeriod = var2;
   }

   public void setMaxTime(int var1) {
      this.maxIdentifyTime = var1;
   }

   public synchronized void start() {
      if (this.localIdentifyProcess != null) {
         this.identifyThread = new Thread(this.localIdentifyProcess);
         this.identifyThread.start();
      }

   }

   public synchronized void stop() {
      if (this.localIdentifyProcess != null) {
         this.localIdentifyProcess.stop();
      }

   }

   public synchronized boolean isAlive() {
      return this.identifyThread != null ? this.identifyThread.isAlive() : false;
   }

   public IdentifyTask(BDaliNetwork var1, boolean var2, int var3) {
     this.identifyThread = null;
     this.localIdentifyProcess = null;
     this.identifyControl = null;
     this.identifyMode = 1;
     this.identifyOnPeriod = 4000;
     this.identifyOffPeriod = 3000;
     this.maxIdentifyTime = 600;
     
      this.localIdentifyProcess = new IdentifyProcess(var1, var2, var3);
   }

   public IdentifyTask(BComponent var1) {
     this.identifyThread = null;
     this.localIdentifyProcess = null;
     this.identifyControl = null;
     this.identifyMode = 1;
     this.identifyOnPeriod = 4000;
     this.identifyOffPeriod = 3000;
     this.maxIdentifyTime = 600;
     
      this.localIdentifyProcess = new IdentifyProcess(var1);
   }

   public IdentifyTask(BGenericDaliDevice var1) {
     this.identifyThread = null;
     this.localIdentifyProcess = null;
     this.identifyControl = null;
     this.identifyMode = 1;
     this.identifyOnPeriod = 4000;
     this.identifyOffPeriod = 3000;
     this.maxIdentifyTime = 600;
     
      this.localIdentifyProcess = new IdentifyProcess(var1);
   }

   public class IdentifyProcess implements Runnable {
      private BDaliNetwork daliNetwork;
      private boolean daliGroupFlag;
      private int daliAddress;
      private BGenericDaliDevice daliDevice;
      private BComponent ballastFolder;
      private boolean cancelTask;

      public void run() {
         this.resolveDaliNetwork();
         this.clearNetworkAbortFlag();
         if (this.daliDevice != null) {
            IdentifyTask.this.identifyControl = BDaliIdentifyControl.findInstance(this.daliDevice);
         }

         if (IdentifyTask.this.identifyControl == null && this.daliNetwork != null) {
            IdentifyTask.this.identifyControl = BDaliIdentifyControl.findInstance(this.daliNetwork);
         }

         if (IdentifyTask.this.identifyControl != null) {
            IdentifyTask.this.maxIdentifyTime = (int)IdentifyTask.this.identifyControl.getIdentifyMaxTime().getValue();
         }

         long var1 = System.currentTimeMillis() + (long)(IdentifyTask.this.maxIdentifyTime * 1000);
         boolean var3 = false;

         try {
            while(!this.cancelTask && !this.getNetworkAbortFlag() && System.currentTimeMillis() < var1) {
               if (IdentifyTask.this.identifyControl != null) {
                  IdentifyTask.this.identifyOnPeriod = (int)(IdentifyTask.this.identifyControl.getIdentifyOnPeriod().getValue() * 1000.0);
                  IdentifyTask.this.identifyOffPeriod = (int)(IdentifyTask.this.identifyControl.getIdentifyOffPeriod().getValue() * 1000.0);
                  if (!IdentifyTask.this.identifyControl.getIdentifyMode().getStatus().isNull()) {
                     IdentifyTask.this.identifyMode = IdentifyTask.this.identifyControl.getIdentifyMode().getValue().getOrdinal();
                  }
               }

               if (!var3) {
                  this.turnOn();
                  Thread.sleep((long)IdentifyTask.this.identifyOnPeriod);
                  var3 = true;
               } else {
                  this.turnOff();
                  Thread.sleep((long)IdentifyTask.this.identifyOffPeriod);
                  var3 = false;
               }
            }
         } catch (Exception var7) {
         }

         try {
            if (this.daliDevice != null) {
               this.daliDevice.identifyStopped();
            }
         } catch (Exception var6) {
         }

         try {
            this.turnOff();
         } catch (Exception var5) {
         }

      }

      private final void turnOn() {
         if (IdentifyTask.this.identifyMode == 1) {
            this.setLevel(100);
         } else if (IdentifyTask.this.identifyMode == 2) {
            this.sendCommand(5);
         } else if (IdentifyTask.this.identifyMode == 3) {
            this.sendCommand(5);
         }

      }

      private final void turnOff() {
         if (IdentifyTask.this.identifyMode == 1) {
            this.setLevel(0);
         } else if (IdentifyTask.this.identifyMode == 2) {
            this.sendCommand(0);
         } else if (IdentifyTask.this.identifyMode == 3) {
            this.sendCommand(6);
         }

      }

      private final void setLevel(int var1) {
         if (this.ballastFolder == null) {
            if (this.daliNetwork != null) {
               DaliLevel var5 = new DaliLevel(this.daliNetwork, this.daliGroupFlag, this.daliAddress, (double)var1);
               var5.setTransmitRepeatCount(this.daliNetwork.getTransmitCount() - 1);
               var5.execute();
            }
         } else {
            BComponent[] var2 = this.ballastFolder.getChildComponents();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (var2[var3] instanceof BBallast) {
                  BBallast var4 = (BBallast)var2[var3];
                  var4.daliLevel((double)var1);
               }
            }
         }

      }

      private final void sendCommand(int var1) {
         if (this.ballastFolder == null) {
            if (this.daliNetwork != null) {
               DaliCommand var5 = new DaliCommand(this.daliNetwork, this.daliGroupFlag, this.daliAddress, var1);
               var5.setTransmitRepeatCount(this.daliNetwork.getTransmitCount() - 1);
               var5.execute();
            }
         } else {
            BComponent[] var2 = this.ballastFolder.getChildComponents();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (var2[var3] instanceof BBallast) {
                  BBallast var4 = (BBallast)var2[var3];
                  var4.daliCommand(var1);
               }
            }
         }

      }

      private final void clearNetworkAbortFlag() {
         if (this.daliNetwork != null) {
            this.daliNetwork.setIdentifyAbortFlag(false);
         }

      }

      private final boolean getNetworkAbortFlag() {
         return this.daliNetwork != null ? this.daliNetwork.getIdentifyAbortFlag() : false;
      }

      private final void resolveDaliNetwork() {
         if (this.daliNetwork == null) {
            if (this.daliDevice != null) {
               this.daliNetwork = this.daliDevice.getDaliNetwork();
            } else if (this.ballastFolder != null) {
               BComponent[] var1 = this.ballastFolder.getChildComponents();

               for(int var2 = 0; var2 < var1.length; ++var2) {
                  if (var1[var2] instanceof BBallast) {
                     BBallast var3 = (BBallast)var1[var2];
                     this.daliNetwork = var3.getDaliNetwork();
                     break;
                  }
               }
            }
         }

      }

      private final synchronized void stop() {
         this.cancelTask = true;
      }

      public IdentifyProcess(BDaliNetwork var2, boolean var3, int var4) {
        this.daliNetwork = null;
        this.daliGroupFlag = false;
        this.daliAddress = 63;
        this.daliDevice = null;
        this.ballastFolder = null;
        this.cancelTask = false;
        
         this.daliNetwork = var2;
         this.daliGroupFlag = var3;
         this.daliAddress = var4;
         this.ballastFolder = null;
      }

      public IdentifyProcess(BGenericDaliDevice var2) {
        this.daliNetwork = null;
        this.daliGroupFlag = false;
        this.daliAddress = 63;
        this.daliDevice = null;
        this.ballastFolder = null;
        this.cancelTask = false;
        
         this.daliDevice = var2;
         this.daliNetwork = this.daliDevice.getDaliNetwork();
         this.daliGroupFlag = false;
         this.daliAddress = this.daliDevice.getDaliAddress();
         this.ballastFolder = null;
      }

      public IdentifyProcess(BComponent var2) {
        this.daliNetwork = null;
        this.daliGroupFlag = false;
        this.daliAddress = 63;
        this.daliDevice = null;
        this.ballastFolder = null;
        this.cancelTask = false;
        
         this.ballastFolder = var2;
         this.daliNetwork = null;
      }
   }
}
