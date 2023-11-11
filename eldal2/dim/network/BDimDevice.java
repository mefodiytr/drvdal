package uk.co.controlnetworksolutions.elitedali2.dim.network;

import javax.baja.driver.BDevice;
import javax.baja.log.Log;
import javax.baja.status.BStatus;
import javax.baja.sys.Action;
import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.util.IFuture;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dim.ops.DimAction;
import uk.co.controlnetworksolutions.elitedali2.dim.ops.DimParamWrite;
import uk.co.controlnetworksolutions.elitedali2.dim.ops.DimPing;
import uk.co.controlnetworksolutions.elitedali2.dim.point.BDimPointDeviceExt;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public class BDimDevice extends BDevice {
   public static final Property address = newProperty(257, "", (BFacets)null);
   public static final Property points = newProperty(0, new BDimPointDeviceExt(), (BFacets)null);
   public static final Property daliNetwork = newProperty(0, new BDaliNetwork(), (BFacets)null);
   public static final Property debugLevel = newProperty(4, 0, (BFacets)null);
   public static final Action identify = newAction(0, (BFacets)null);
   public static final Action changeAddress = newAction(0, BInteger.make(1), (BFacets)null);
   public static final Action resetAddress = newAction(132, (BFacets)null);
   public static final Action replaceAddress = newAction(0, BInteger.make(1), (BFacets)null);
   public static final Action resetDevice = newAction(0, (BFacets)null);
   public static final Action setDaliPowerTimeout = newAction(4, BInteger.make(150), (BFacets)null);
   public static final Type TYPE;
   public static final int DIM_ADDRESS_BROADCAST = 0;
   public static final int DIM_ADDRESS_MIN = 1;
   public static final int DIM_ADDRESS_MAX = 240;
   public static final int DIM_ADDRESS_UNCONFIGURED = 244;
   public static final int DEFAULT_DIM_IDENTIFY_PERIOD = 15;
   public static final int DIM_FAULT_NONE = 0;
   public static final int DIM_FAULT_TRANSACTION_EXCEPTION = 1;
   public static final int DIM_FAULT_NETWORK_NULL = 2;
   public static final int DIM_FAULT_NETWORK_DISABLED = 3;
   public static final int DIM_FAULT_NO_RESPONSE = 4;
   private Type parentNetworkType;
   private int dimAddress;
   private int lastFaultCode;
   private Log log;

   public String getAddress() {
      return this.getString(address);
   }

   public void setAddress(String var1) {
      this.setString(address, var1, (Context)null);
   }

   public BDimPointDeviceExt getPoints() {
      return (BDimPointDeviceExt)this.get(points);
   }

   public void setPoints(BDimPointDeviceExt var1) {
      this.set(points, var1, (Context)null);
   }

   public BDaliNetwork getDaliNetwork() {
      return (BDaliNetwork)this.get(daliNetwork);
   }

   public void setDaliNetwork(BDaliNetwork var1) {
      this.set(daliNetwork, var1, (Context)null);
   }

   public int getDebugLevel() {
      return this.getInt(debugLevel);
   }

   public void setDebugLevel(int var1) {
      this.setInt(debugLevel, var1, (Context)null);
   }

   public void identify() {
      this.invoke(identify, (BValue)null, (Context)null);
   }

   public void changeAddress(BInteger var1) {
      this.invoke(changeAddress, var1, (Context)null);
   }

   public void resetAddress() {
      this.invoke(resetAddress, (BValue)null, (Context)null);
   }

   public void replaceAddress(BInteger var1) {
      this.invoke(replaceAddress, var1, (Context)null);
   }

   public void resetDevice() {
      this.invoke(resetDevice, (BValue)null, (Context)null);
   }

   public void setDaliPowerTimeout(BInteger var1) {
      this.invoke(setDaliPowerTimeout, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public void setDeviceNetworkType(Type var1) {
      this.parentNetworkType = var1;
   }

   public void started() throws Exception {
      super.started();
      this.setDimAddress(this.getAddress());
   }

   public void atSteadyState() throws Exception {
      super.atSteadyState();
      this.doPing();
   }

   public int getDimAddress() {
      return this.dimAddress;
   }

   public void setDimAddress(int var1) {
      if (var1 >= 1 && var1 <= 240) {
         this.dimAddress = var1;
         this.setAddress(new String(Integer.toString(this.dimAddress)));
      } else {
         this.log.error("DIM address out of range: " + var1);
      }

   }

   public void setDimAddress(String var1) {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (Exception var4) {
         this.log.error("Invalid DIM address: '" + this.getAddress() + '\'');
         this.configFail("Invalid DIM address");
         return;
      }

      this.setDimAddress(var2);
   }

   public Type getNetworkType() {
      return this.parentNetworkType;
   }

   public final BDimNetwork getDimNetwork() {
      if (this.isRunning()) {
         return (BDimNetwork)this.getNetwork();
      } else {
         this.log.error("Attempted to get DIM network before the device is running");
         return null;
      }
   }

   public void changed(Property var1, Context var2) {
      if (var1 == address) {
         this.setDimAddress(this.getAddress());
         this.doPing();
      }

   }

   public void setDeviceOk() {
      this.setStatus(BStatus.ok);
      this.configOk();
      this.pingOk();
      this.lastFaultCode = 0;
   }

   public void setDeviceFault(int var1, String var2, boolean var3) {
      this.setFaultCause(var2);
      if (var3) {
         this.pingFail(var2);
      }

      this.updateStatus();
      if (this.getDebugLevel() > 0 && this.lastFaultCode != var1) {
         this.log.trace("DIM " + this.getDimAddress() + " fault [" + var1 + "]: " + var2);
      }

      this.lastFaultCode = var1;
   }

   protected IFuture postPing() {
      this.doPing();
      return null;
   }

   public void doPing() {
      if (this.isRunning() && !this.isDisabled()) {
         DimPing var1 = new DimPing(this);
         var1.execute();
      }

   }

   public void doChangeAddress(BInteger var1) {
      this.assignNewDimAddress(var1.getInt());
   }

   public void doIdentify() {
      DimAction var1 = new DimAction(this.getDimNetwork(), this.dimAddress, 1, new byte[]{1, 15});
      var1.execute();
   }

   public void doResetAddress() {
      DimAction var1 = new DimAction(this.getDimNetwork(), this.dimAddress, 65);
      var1.execute();
      byte[] var2 = var1.getReturnData();
      if (var2 != null && var2.length > 0) {
         this.setDimAddress(255 & var2[0]);
         this.doPing();
      } else {
         this.log.error("Failed to obtain new DIM address after address reset");
      }

   }

   public void doReplaceAddress(BInteger var1) {
      this.log.message("Replacing DIM address " + this.dimAddress + " with " + var1.getInt());
      this.setDimAddress(var1.getInt());
   }

   public void doResetDevice() {
      DimAction var1 = new DimAction(this.getDimNetwork(), this.dimAddress, 117);
      var1.execute();
   }

   public void doSetDaliPowerTimeout(BInteger var1) {
      byte[] var3 = new byte[]{(byte)var1.getInt()};
      DimParamWrite var2 = new DimParamWrite(this, 81, var3);
      var2.execute();
      if (var2.success()) {
         this.log.message("Changed DIM DALI power timeout to " + var1.getInt());
      } else {
         this.log.error("Failed to set DIM DALI power timeout");
      }

   }

   public void assignNewDimAddress(int var1) {
      if (var1 >= 1 && var1 <= 240) {
         byte[] var3 = new byte[]{(byte)var1};
         DimParamWrite var2 = new DimParamWrite(this.getDimNetwork(), this.dimAddress, 32, var3);
         var2.execute();
         if (var2.success()) {
            this.log.message("Changed DIM address from " + this.dimAddress + " to " + var1);
            this.setDimAddress(var1);
            this.doPing();
         } else {
            this.log.error("Failed to assign new DIM address (" + var1 + ')');
         }

      } else {
         this.log.error("DIM address out of range");
      }
   }

   public BDimDevice() {
     this.parentNetworkType = BDimNetwork.TYPE;
     this.dimAddress = 999;
     this.lastFaultCode = 0;
     this.log = Report.dim;
     
      this.parentNetworkType = BDimNetwork.TYPE;
   }

   static {
      TYPE = Sys.loadType(BDimDevice.class);
   }
}
