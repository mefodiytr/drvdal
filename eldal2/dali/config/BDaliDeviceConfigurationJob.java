package uk.co.controlnetworksolutions.elitedali2.dali.config;

import javax.baja.job.BJobService;
import javax.baja.job.BJobState;
import javax.baja.job.BSimpleJob;
import javax.baja.sys.Clock;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.util.Queue;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BDaliSensor;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BallastGroupFolder;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public final class BDaliDeviceConfigurationJob extends BSimpleJob {
   public static final Type TYPE;
   private static final int DALI_DEVICE_QUEUE_SIZE = 200;
   private static final int DALI_DEVICE_QUEUE_TIMEOUT = 800;
   private int totalDevices;
   private int processedDevices;
   private boolean configurePropertiesFlag;
   private Queue deviceQueue;
   private BallastGroupFolder ballastGroup;
   private BDaliNetwork daliNetwork;
   private int groupAddressByte;

   public final Type getType() {
      return TYPE;
   }

   public final void initialise() {
      this.daliNetwork = null;
      this.ballastGroup = null;
      this.totalDevices = 0;
      this.processedDevices = 0;
      this.configurePropertiesFlag = true;
      this.groupAddressByte = 0;
      this.deviceQueue = new Queue(200);
   }

   public final void setConfigureProperties(boolean var1) {
      this.configurePropertiesFlag = var1;
   }

   public final void add(BGenericDaliDevice var1) {
      this.deviceQueue.enqueue(var1);
      ++this.totalDevices;
      if (!this.isAlive()) {
         BJobService.getService().submit(this, (Context)null);
      }

   }

   public final void run(Context var1) throws Exception {
      if (this.daliNetwork == null) {
         Report.daliDevice.error("Invalid DALI network for device configuration");
         this.log().message("Invalid DALI network for device configuration");
      } else {
         if (this.ballastGroup == null) {
            this.cfgLog("Starting DALI device configuration job");
         } else if (this.ballastGroup.isVirtualGroup()) {
            this.cfgLog("Starting DALI device configuration job for virtual group " + this.ballastGroup.getName());
         } else {
            this.cfgLog("Starting DALI device configuration job for DALI group " + this.ballastGroup.getName());
         }

         this.progress(0);
         long var4 = Clock.millis();

         while(true) {
            BGenericDaliDevice var2 = (BGenericDaliDevice)this.deviceQueue.dequeue(800);
            if (var2 == null) {
               this.processedDevices = 0;
               this.totalDevices = 0;
               break;
            }

            if (!this.getJobState().isRunning()) {
               break;
            }

            this.progress(this.processedDevices * 100 / this.totalDevices);
            var2.assignDaliAddress();
            DaliDeviceConfig var3 = new DaliDeviceConfig(this.daliNetwork, var2.getDaliAddress());
            long var8 = Clock.millis();
            if (var2.isSensor()) {
               this.cfgLog("Configuring sensor device '" + ((BallastGroupFolder) var2).getName() + '\'');
               ((BDaliSensor)var2).configure(var3);
            } else if (this.ballastGroup == null) {
               this.cfgLog("Configuring groupless device '" + ((BallastGroupFolder) var2).getName() + '\'');
               var3.programGroups(0);
            } else {
               this.cfgLog("Configuring device '" + ((BallastGroupFolder) var2).getName() + "' for group " + this.ballastGroup.getName());
               var3.programGroups(this.groupAddressByte);
               this.progress((this.processedDevices * 100 + 40) / this.totalDevices);
               if (!this.getJobState().isRunning()) {
                  break;
               }

               if (this.configurePropertiesFlag && var2.isBallast()) {
                  var3.programScenes(this.ballastGroup);
                  var3.programProperties(this.ballastGroup);
               }
            }

            long var10 = Clock.millis();
            if (!this.getJobState().isRunning()) {
               break;
            }

            ++this.processedDevices;
            this.cfgLog("Device '" + ((BallastGroupFolder) var2).getName() + "' configured OK in " + (var10 - var8) + " ms");
         }

         if (this.getJobState().isRunning()) {
            long var6 = Clock.millis();
            this.progress(100);
            this.cfgLog("DALI device configuration job completed in " + (var6 - var4) + " ms");
            this.complete(BJobState.make(4));
         } else {
            this.cfgLog("DALI device configuration job cancelled");
            this.complete(BJobState.make(3));
         }

      }
   }

   public final void cfgLog(String var1) {
      Report.daliDevice.trace(var1);
      this.log().message(var1);
   }

   public BDaliDeviceConfigurationJob() {
     this.totalDevices = 0;
     this.processedDevices = 0;
     this.configurePropertiesFlag = true;
     this.deviceQueue = null;
     this.ballastGroup = null;
     this.daliNetwork = null;
     this.groupAddressByte = 0;
     
      this.initialise();
   }

   public BDaliDeviceConfigurationJob(BDaliNetwork var1) {
     this.totalDevices = 0;
     this.processedDevices = 0;
     this.configurePropertiesFlag = true;
     this.deviceQueue = null;
     this.ballastGroup = null;
     this.daliNetwork = null;
     this.groupAddressByte = 0;
     
      this.initialise();
      this.daliNetwork = var1;
   }

   public BDaliDeviceConfigurationJob(BallastGroupFolder var1) {
     this.totalDevices = 0;
     this.processedDevices = 0;
     this.configurePropertiesFlag = true;
     this.deviceQueue = null;
     this.ballastGroup = null;
     this.daliNetwork = null;
     this.groupAddressByte = 0;
     
      this.initialise();
      if (var1 != null) {
         this.ballastGroup = var1;
         this.daliNetwork = this.ballastGroup.getDaliNetwork();
         if (!this.ballastGroup.isVirtualGroup()) {
            this.groupAddressByte = 1 << this.ballastGroup.getDaliGroupAddress();
         }
      }

   }

   static {
      TYPE = Sys.loadType(BDaliDeviceConfigurationJob.class);
   }
}
