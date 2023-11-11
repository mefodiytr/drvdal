package uk.co.controlnetworksolutions.elitedali2.dim.network;

import com.tridium.basicdriver.comm.Comm;
import com.tridium.basicdriver.serial.BSerialNetwork;
import javax.baja.driver.loadable.BDownloadParameters;
import javax.baja.driver.loadable.BUploadParameters;
import javax.baja.driver.point.BTuningPolicyMap;
import javax.baja.license.LicenseException;
import javax.baja.naming.BOrd;
import javax.baja.serial.BSerialBaudRate;
import javax.baja.serial.BSerialDataBits;
import javax.baja.serial.BSerialFlowControlMode;
import javax.baja.serial.BSerialParity;
import javax.baja.serial.BSerialStopBits;
import javax.baja.sys.Action;
import javax.baja.sys.BComponent;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BEnum;
import javax.baja.sys.BEnumRange;
import javax.baja.sys.BFacets;
import javax.baja.sys.BRelTime;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.NotRunningException;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dim.msg.DimComm;
import uk.co.controlnetworksolutions.elitedali2.dim.msg.DimCommReceiver;
import uk.co.controlnetworksolutions.elitedali2.dim.ops.DimAction;
import uk.co.controlnetworksolutions.elitedali2.dim.ops.DimBoot;
import uk.co.controlnetworksolutions.elitedali2.utils.CnsLicense;
import uk.co.controlnetworksolutions.elitedali2.utils.CnsVersionUtil;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public class BDimNetwork extends BSerialNetwork {
   public static final Property driverVersion = newProperty(1, "", (BFacets)null);
   public static final Property tuningPolicies = newProperty(0, new BTuningPolicyMap(), (BFacets)null);
   public static final Action setComPort = newAction(0, BDynamicEnum.make(2, BEnumRange.make(new int[]{1, 2, 3, 4, 5, 6}, new String[]{"COM1", "COM2", "COM3", "COM4", "COM5", "COM6"})), (BFacets)null);
   public static final Action identifyAll = newAction(256, (BFacets)null);
   public static final Action submitDimDiscoveryJob = newAction(4, (BFacets)null);
   public static final Action ping = newAction(4, (BFacets)null);
   public static final Action downloadAbort = newAction(4, (BFacets)null);
   public static final Type TYPE;
   private static int dimDeviceCount;
   private static int dimDeviceLimit;
   private static final int SERIAL_BAUD_RATE = 57600;
   private static final int RETRY_COUNT = 2;
   private static final int RESPONSE_TIMEOUT = 250;
   private static final int INTER_MESSAGE_DELAY = 0;
   private static final String DIM_LIMIT_ERROR = "Too many DIM devices in network, could not add new device";

   public String getDriverVersion() {
      return this.getString(driverVersion);
   }

   public void setDriverVersion(String var1) {
      this.setString(driverVersion, var1, (Context)null);
   }

   public BTuningPolicyMap getTuningPolicies() {
      return (BTuningPolicyMap)this.get(tuningPolicies);
   }

   public void setTuningPolicies(BTuningPolicyMap var1) {
      this.set(tuningPolicies, var1, (Context)null);
   }

   public void setComPort(BEnum var1) {
      this.invoke(setComPort, var1, (Context)null);
   }

   public void identifyAll() {
      this.invoke(identifyAll, (BValue)null, (Context)null);
   }

   public BOrd submitDimDiscoveryJob() {
      return (BOrd)this.invoke(submitDimDiscoveryJob, (BValue)null, (Context)null);
   }

   public void ping() {
      this.invoke(ping, (BValue)null, (Context)null);
   }

   public void downloadAbort() {
      this.invoke(downloadAbort, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public Type getDeviceType() {
      return BDimDevice.TYPE;
   }

   public Type getDeviceFolderType() {
      return BDimDeviceFolder.TYPE;
   }

   public void doUpload(BUploadParameters var1, Context var2) throws Exception {
   }

   public void doDownload(BDownloadParameters var1, Context var2) throws Exception {
   }

   public void started() throws Exception {
      super.started();
      this.setDriverVersion(CnsVersionUtil.getModuleVersion(this));
      dimDeviceLimit = CnsLicense.checkDimLicense(this);
      this.checkDimLimit();
      this.pingOk();
   }

   public void added(Property var1, Context var2) {
      if (Sys.atSteadyState() && this.isRunning()) {
         if (var1.getType() == BDimDevice.TYPE) {
            Report.dim.trace("Adding DIM device " + var1.getName() + " to " + this.getName());
            synchronized(this) {
               ++dimDeviceCount;
            }

            if (dimDeviceCount > dimDeviceLimit) {
               Report.dim.error("Too many DIM devices in network, could not add new device");
               this.configFatal("Too many DIM devices in network, could not add new device");
               this.pingFail("Too many DIM devices in network, could not add new device");
               throw new LicenseException("Too many DIM devices in network, could not add new device");
            }

            super.added(var1, var2);
         }

      }
   }

   public void removed(Property var1, BValue var2, Context var3) {
      if (var1.getType() == BDimDevice.TYPE) {
         synchronized(this) {
            --dimDeviceCount;
         }
      }

      super.removed(var1, var2, var3);
   }

   public void doSetComPort(BEnum var1) {
      this.getSerialPortConfig().setPortName(var1.getTag());
   }

   public void doIdentifyAll() {
      DimAction var1 = new DimAction(this, 0, 1, new byte[]{1, 15});
      var1.execute();
   }

   public BOrd doSubmitDimDiscoveryJob() {
      if (this.isCommActive()) {
         BDimLearnDevicesJob var1 = new BDimLearnDevicesJob(this);
         return var1.submit((Context)null);
      } else {
         throw new NotRunningException("DIM Network is not running");
      }
   }

   public void doDownloadAbort() {
      DimBoot var1 = new DimBoot(this, 0, 168, new byte[]{1, 15});
      var1.execute();
   }

   protected Comm makeComm() {
      return new DimComm(this, new DimCommReceiver());
   }

   public void checkDimLimit() {
      BComponent[] var1 = this.getChildComponents();
      dimDeviceCount = 0;

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] instanceof BDimDevice) {
            synchronized(this) {
               ++dimDeviceCount;
            }
         }
      }

      if (dimDeviceCount > dimDeviceLimit) {
         Report.dim.error("Too many DIM devices in network, could not add new device");
         this.configFatal("Too many DIM devices in network, could not add new device");
         this.pingFail("Too many DIM devices in network, could not add new device");
         throw new LicenseException("Too many DIM devices in network, could not add new device");
      }
   }

   public BDimNetwork() {
      this.setFlags(this.getSlot("upload"), 4);
      this.setFlags(this.getSlot("download"), 4);
      this.setRetryCount(2);
      this.setResponseTimeout(BRelTime.make(250L));
      this.setInterMessageDelay(BRelTime.make(0L));
      this.getSerialPortConfig().setDataBits(BSerialDataBits.dataBits8);
      this.getSerialPortConfig().setStopBits(BSerialStopBits.stopBit1);
      this.getSerialPortConfig().setParity(BSerialParity.none);
      this.getSerialPortConfig().setFlowControlMode(BSerialFlowControlMode.none);
      this.getSerialPortConfig().setBaudRate(BSerialBaudRate.make(57600));
      this.getSerialPortConfig().setFlags(this.getSerialPortConfig().getSlot("baudRate"), 1);
      this.getSerialPortConfig().setFlags(this.getSerialPortConfig().getSlot("dataBits"), 1);
      this.getSerialPortConfig().setFlags(this.getSerialPortConfig().getSlot("stopBits"), 1);
      this.getSerialPortConfig().setFlags(this.getSerialPortConfig().getSlot("parity"), 1);
      this.getSerialPortConfig().setFlags(this.getSerialPortConfig().getSlot("flowControlMode"), 1);
   }

   static {
      TYPE = Sys.loadType(BDimNetwork.class);
      dimDeviceCount = 0;
      dimDeviceLimit = 1;
   }
}
