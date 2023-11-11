package uk.co.controlnetworksolutions.elitedali2.dali.devices;

import javax.baja.driver.BDevice;
import javax.baja.log.Log;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusString;
import javax.baja.sys.Action;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.util.IFuture;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliLevel;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliPing;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliSpecialCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliStatus;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliDevicePointExt;
import uk.co.controlnetworksolutions.elitedali2.dali.point.virtual.BVirtualPointsGateway;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public class BGenericDaliDevice extends BDevice {
   public static final Property address = newProperty(257, "", (BFacets)null);
   public static final Property points = newProperty(0, new BDaliDevicePointExt(), (BFacets)null);
   public static final Property configPoints = newProperty(0, new BVirtualPointsGateway(), (BFacets)null);
   public static final Property duplicateDetected = newProperty(4, false, (BFacets)null);
   public static final Property location;
   public static final Property globalTradeItemNumber;
   public static final Property vendorIdentifier;
   public static final Property serialNumber;
   public static final Property firmwareVersion;
   public static final Property commissioningComplete;
   public static final Property configurationComplete;
   public static final Property retryMax;
   public static final Action changeAddress;
   public static final Action resolveConflicts;
   public static final Type TYPE;
   protected int daliAddress;
   private Log log;

   public String getAddress() {
      return this.getString(address);
   }

   public void setAddress(String var1) {
      this.setString(address, var1, (Context)null);
   }

   public BDaliDevicePointExt getPoints() {
      return (BDaliDevicePointExt)this.get(points);
   }

   public void setPoints(BDaliDevicePointExt var1) {
      this.set(points, var1, (Context)null);
   }

   public BVirtualPointsGateway getConfigPoints() {
      return (BVirtualPointsGateway)this.get(configPoints);
   }

   public void setConfigPoints(BVirtualPointsGateway var1) {
      this.set(configPoints, var1, (Context)null);
   }

   public boolean getDuplicateDetected() {
      return this.getBoolean(duplicateDetected);
   }

   public void setDuplicateDetected(boolean var1) {
      this.setBoolean(duplicateDetected, var1, (Context)null);
   }

   public BStatusString getLocation() {
      return (BStatusString)this.get(location);
   }

   public void setLocation(BStatusString var1) {
      this.set(location, var1, (Context)null);
   }

   public BStatusString getGlobalTradeItemNumber() {
      return (BStatusString)this.get(globalTradeItemNumber);
   }

   public void setGlobalTradeItemNumber(BStatusString var1) {
      this.set(globalTradeItemNumber, var1, (Context)null);
   }

   public BStatusString getVendorIdentifier() {
      return (BStatusString)this.get(vendorIdentifier);
   }

   public void setVendorIdentifier(BStatusString var1) {
      this.set(vendorIdentifier, var1, (Context)null);
   }

   public BStatusString getSerialNumber() {
      return (BStatusString)this.get(serialNumber);
   }

   public void setSerialNumber(BStatusString var1) {
      this.set(serialNumber, var1, (Context)null);
   }

   public BStatusString getFirmwareVersion() {
      return (BStatusString)this.get(firmwareVersion);
   }

   public void setFirmwareVersion(BStatusString var1) {
      this.set(firmwareVersion, var1, (Context)null);
   }

   public boolean getCommissioningComplete() {
      return this.getBoolean(commissioningComplete);
   }

   public void setCommissioningComplete(boolean var1) {
      this.setBoolean(commissioningComplete, var1, (Context)null);
   }

   public boolean getConfigurationComplete() {
      return this.getBoolean(configurationComplete);
   }

   public void setConfigurationComplete(boolean var1) {
      this.setBoolean(configurationComplete, var1, (Context)null);
   }

   public int getRetryMax() {
      return this.getInt(retryMax);
   }

   public void setRetryMax(int var1) {
      this.setInt(retryMax, var1, (Context)null);
   }

   public void changeAddress(BInteger var1) {
      this.invoke(changeAddress, var1, (Context)null);
   }

   public void resolveConflicts() {
      this.invoke(resolveConflicts, (BValue)null, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void started() throws Exception {
      this.assignDaliAddress();
      if (Sys.atSteadyState()) {
         this.doPing();
      }

   }

   public Type getNetworkType() {
      return BDaliNetwork.TYPE;
   }

   public void setDaliAddress(int var1) {
      if (var1 >= 0 && var1 <= 63) {
         this.daliAddress = var1;
         this.setAddress(new String(Integer.toString(this.daliAddress + 1)));
      } else {
         this.log.error("DALI address out of range: " + var1);
         this.configFail("DALI address out of range");
      }

   }

   public void setDaliAddress(String var1) {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (Exception var4) {
         this.log.error("Invalid DALI address: '" + this.getAddress() + '\'');
         this.configFail("Invalid DALI address");
         return;
      }

      this.setDaliAddress(var2 - 1);
   }

   public void assignDaliAddress() {
      this.setDaliAddress(this.getAddress());
   }

   public int getDaliAddress() {
      return this.daliAddress;
   }

   public BDaliNetwork getDaliNetwork() {
      try {
         if (this.isRunning()) {
            if (this.getNetwork() instanceof BDaliNetwork) {
               return (BDaliNetwork)this.getNetwork();
            }

            throw new RuntimeException("DALI device network invalid [" + this.toPathString() + ']');
         }
      } catch (Exception var2) {
         this.log.trace("DALI device network exception [" + this.toPathString() + "]: " + var2);
      }

      return null;
   }

   public boolean daliCommand(int var1) {
      if (var1 != 255 && this.isDeviceOk()) {
         DaliCommand var2 = new DaliCommand(this, var1);
         this.getDaliNetwork().setOpRetransmitCount(var2, var1);
         var2.execute();
         return var2.success();
      } else {
         return false;
      }
   }

   public int daliQuery(int var1) {
      if (this.isDeviceOk()) {
         DaliQuery var2 = new DaliQuery(this, var1);
         return var2.getResult();
      } else {
         return -1;
      }
   }

   public boolean daliLevel(double var1) {
      if (this.isDeviceOk()) {
         DaliLevel var3 = new DaliLevel(this, var1);
         var3.setTransmitRepeatCount(this.getDaliNetwork().getTransmitCount() - 1);
         var3.execute();
         return var3.success();
      } else {
         return false;
      }
   }

   public int getDeviceRetryMax() {
      return this.getRetryMax() < 0 ? this.getDaliNetwork().getRetryMax() : this.getRetryMax();
   }

   protected IFuture postPing() {
      this.doPing();
      return null;
   }

   public void doPing() {
      if (this.isDeviceReady()) {
         if (this.getDaliAddress() < 0 || this.getDaliAddress() > 64) {
            this.putDeviceInFault("DALI address out of range");
            throw new IllegalArgumentException("DALI address out of range");
         }

         int var4 = 69;
         int var2 = this.getDeviceRetryMax();
         DaliPing var1 = new DaliPing(this);

         for(int var3 = 0; var3 < var2 + 1; ++var3) {
            var1.execute();
            var4 = var1.getDaliStatus();
            if (var4 == 53) {
               break;
            }

            try {
               Thread.sleep(250L);
            } catch (Exception var6) {
            }
         }

         if (var4 == 1) {
            this.putDeviceInFault(DaliStatus.toString(var4));
         }

         this.updateStatus();
      }

   }

   public void handleDaliStatus(int var1) {
      switch (var1) {
         case 1:
         default:
            break;
         case 3:
         case 4:
         case 34:
            this.putDeviceInFault(DaliStatus.toString(var1));
            break;
         case 53:
            this.setStatus(BStatus.ok);
            this.configOk();
            if (this.isRunning()) {
               this.pingOk();
            }
      }

      this.updateStatus();
   }

   public void changed(Property var1, Context var2) {
      if (this.isRunning() && var1 == address) {
         this.assignDaliAddress();
      }

   }

   public void doChangeAddress(BInteger var1) {
      if (this.isDeviceOk()) {
         this.log.message("Changing DALI device address " + (this.getDaliAddress() + 1) + " to " + var1.getInt());
         if (var1.getInt() > 0 && var1.getInt() <= 64) {
            if (var1.getInt() != this.getDaliAddress() + 1) {
               if (this.changeDaliShortAddress(var1.getInt() - 1, false, true)) {
                  this.setDaliAddress(var1.getInt() - 1);
                  DaliPing var2 = new DaliPing(this);
                  var2.execute();
                  if (var2.success()) {
                     this.log.message("DALI device address successfully changed to " + (this.getDaliAddress() + 1));
                  } else {
                     this.log.error("Ping failure after changing DALI device address");
                  }
               } else {
                  this.log.error("Failed to change DALI device address");
               }
            } else {
               this.log.warning("No change to DALI address");
            }
         } else {
            this.log.error("Invalid DALI address " + var1.getInt());
         }

      }
   }

   public static boolean changeDaliShortAddress(BDaliNetwork var0, int var1, int var2, boolean var3, boolean var4) {
      int var5;
      if (var3) {
         var5 = 255;
      } else {
         var5 = (var2 & 63) << 1 | 1;
      }

      DaliSpecialCommand var6 = new DaliSpecialCommand(var0, 163, var5);
      var6.setTransmitRepeatCount(3);
      var6.execute();
      if (!var6.success()) {
         return false;
      } else {
         if (var4) {
            DaliQuery var7 = new DaliQuery(var0, var1, 152);
            var7.confirmResult();
            if (!var7.success() || var7.getResult() != var5) {
               return false;
            }
         }

         DaliCommand var8 = new DaliCommand(var0, false, var1, 128);
         var8.setTransmitRepeatCount(3);
         var8.execute();
         return var8.success();
      }
   }

   public boolean changeDaliShortAddress(int var1, boolean var2, boolean var3) {
      if (!this.isDeviceOk()) {
         return false;
      } else {
         int var4;
         if (var2) {
            var4 = 255;
         } else {
            var4 = (var1 & 63) << 1 | 1;
         }

         DaliSpecialCommand var5 = new DaliSpecialCommand(this.getDaliNetwork(), 163, var4);
         var5.setTransmitRepeatCount(3);
         var5.execute();
         if (!var5.success()) {
            return false;
         } else {
            if (var3) {
               DaliQuery var6 = new DaliQuery(this, 152);
               var6.confirmResult();
               if (!var6.success() || var6.getResult() != var4) {
                  return false;
               }
            }

            DaliCommand var7 = new DaliCommand(this, 128);
            var7.setTransmitRepeatCount(3);
            var7.execute();
            return var7.success();
         }
      }
   }

   public void doResolveConflicts() {
      try {
         if (!this.isRunning() || this.isDisabled() || !Sys.atSteadyState() || !this.getDaliNetwork().isNetworkOk()) {
            return;
         }
      } catch (Exception var2) {
         this.log.trace("DALI device check exception [" + this.toPathString() + "]: " + var2);
      }

      if (this.getDaliNetwork().isAddressingJobInProgress()) {
         this.log.warning("Cannot resolve DALI address conflicts, a DALI addressing job is already in progress");
      } else if (!this.changeDaliShortAddress(255, true, false)) {
         this.log.error("Resolving DALI address conflicts failed");
      } else {
         this.getDaliNetwork().submitAddressNewDevicesJob();
      }
   }

   public boolean isDeviceOk() {
      try {
         boolean var10000 = false;
         if (this.isRunning() && !this.isDisabled() && !this.isFault() && this.getStatus().isOk() && Sys.atSteadyState() && this.getDaliNetwork().isNetworkOk()) {
            var10000 = true;
         }

         return var10000;
      } catch (Exception var2) {
         this.log.trace("DALI device OK exception [" + this.toPathString() + "]: " + var2);
         return false;
      }
   }

   public boolean isDeviceReady() {
      try {
         boolean var10000 = false;
         if (this.isRunning() && !this.isDisabled() && Sys.atSteadyState() && this.getDaliNetwork().isNetworkOk()) {
            var10000 = true;
         }

         return var10000;
      } catch (Exception var2) {
         this.log.trace("DALI device ready exception [" + this.toPathString() + "]: " + var2);
         return false;
      }
   }

   public void identifyStopped() {
   }

   public BDaliDeviceType getDaliDeviceType() {
      return BDaliDeviceType.unknownDevice;
   }

   public boolean isSensor() {
      return false;
   }

   public boolean isBallast() {
      return false;
   }

   public void putDeviceInFault(String var1) {
      this.pingFail(var1);
      this.configFail(var1);
   }

   public BGenericDaliDevice() {
     this.daliAddress = 99;
     this.log = Report.daliDevice;
   }

   static {
      location = newProperty(256, new BStatusString("", BStatus.nullStatus), (BFacets)null);
      globalTradeItemNumber = newProperty(4, new BStatusString("undefined", BStatus.nullStatus), (BFacets)null);
      vendorIdentifier = newProperty(4, new BStatusString("undefined", BStatus.nullStatus), (BFacets)null);
      serialNumber = newProperty(4, new BStatusString("undefined", BStatus.nullStatus), (BFacets)null);
      firmwareVersion = newProperty(4, new BStatusString("undefined", BStatus.nullStatus), (BFacets)null);
      commissioningComplete = newProperty(4, false, (BFacets)null);
      configurationComplete = newProperty(5, false, (BFacets)null);
      retryMax = newProperty(4, -1, (BFacets)null);
      changeAddress = newAction(0, BInteger.make(1), (BFacets)null);
      resolveConflicts = newAction(128, (BFacets)null);
      TYPE = Sys.loadType(BGenericDaliDevice.class);
   }
}
