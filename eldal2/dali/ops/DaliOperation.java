package uk.co.controlnetworksolutions.elitedali2.dali.ops;

import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.ops.DimOperation;

public class DaliOperation extends DimOperation {
   protected static boolean ignorePowerState = false;
   protected static final int DALI_REQUEST_FLAG_IGNORE_POWER = 64;
   protected static final int DALI_REQUEST_FLAG_TRANSMIT_MSG = 128;
   protected BDaliNetwork daliNetwork;
   protected BGenericDaliDevice daliDevice;
   protected boolean daliGroupAddressFlag;
   protected boolean daliCommandFlag;
   protected int daliAddressByte;
   protected int daliDataByte;
   protected int daliDeviceType;
   protected int receiveBitCount;
   protected int transmitRepeatCount;
   protected byte[] daliRxData;
   private int daliReplyStatus;

   public void setDaliDeviceType(int var1) {
      this.daliDeviceType = var1;
   }

   protected void prepare() {
      this.bodyData = null;
      if (this.daliNetwork != null) {
         if (!this.daliNetwork.isDisabled()) {
            this.dimDevice = (BDimDevice)this.daliNetwork.getParent();
            if (this.dimDevice != null) {
               if (!this.dimDevice.isDisabled() && !this.dimDevice.isDown() && !this.dimDevice.isFault()) {
                  this.dimNetwork = this.dimDevice.getDimNetwork();
                  this.dimAddress = this.dimDevice.getDimAddress();
                  this.dimOpcode = 88;
               }
            }
         }
      }
   }

   protected void create() {
      this.prepare();
      this.bodyData = new byte[5];
      this.bodyData[0] = -128;
      this.bodyData[1] = 16;
      this.bodyData[2] = (byte)this.receiveBitCount;
      this.bodyData[3] = (byte)this.daliAddressByte;
      this.bodyData[4] = (byte)this.daliDataByte;
      byte[] var10000;
      if (ignorePowerState) {
         var10000 = this.bodyData;
         var10000[0] = (byte)(var10000[0] | 64);
      }

      if (this.daliGroupAddressFlag) {
         var10000 = this.bodyData;
         var10000[3] = (byte)(var10000[3] | 128);
      }

      if (this.daliCommandFlag) {
         var10000 = this.bodyData;
         var10000[3] = (byte)(var10000[3] | 1);
      }

   }

   public boolean execute() {
      this.daliReplyStatus = 15;
      this.daliRxData = null;
      if (this.bodyData == null) {
         this.setFaultText("System fault (null DALI data)");
         return this.dimOperationSuccess;
      } else {
         this.dimOpcode = 88;
         byte[] var10000 = this.bodyData;
         var10000[0] |= (byte)(this.transmitRepeatCount & 3);
         this.sendMessage();
         if (this.dimResponseFlag) {
            if (this.replyData == null) {
               this.setFaultText("System fault (null reply)");
            } else if (this.replyData.length < 2) {
               this.setFaultText("System fault (reply invalid)");
            } else {
               this.daliReplyStatus = 255 & this.replyData[0];
               this.faultText = DaliStatus.toString(this.daliReplyStatus);
               if (this.daliReplyStatus == 53) {
                  this.daliRxData = new byte[this.replyData.length - 2];
                  System.arraycopy(this.replyData, 2, this.daliRxData, 0, this.daliRxData.length);
                  this.dimOperationSuccess = true;
               }
            }
         } else {
            this.daliReplyStatus = 34;
         }

         if (this.daliNetwork != null) {
            this.daliNetwork.handleDaliStatus(this.daliReplyStatus);
         }

         if (this.daliDevice != null) {
            this.daliDevice.handleDaliStatus(this.daliReplyStatus);
         }

         return this.dimOperationSuccess;
      }
   }

   public boolean daliDeviceResponded() {
      if (!this.executedFlag) {
         this.execute();
      }

      boolean var10000 = false;
      if (this.daliReplyStatus == 53) {
         var10000 = true;
      }

      return var10000;
   }

   public boolean daliPowerPresent() {
      if (!this.executedFlag) {
         this.execute();
      }

      boolean var10000 = false;
      if (this.daliReplyStatus != 4) {
         var10000 = true;
      }

      return var10000;
   }

   public boolean daliReceiveCorrupted() {
      if (!this.executedFlag) {
         this.execute();
      }

      boolean var10000 = false;
      if (this.daliReplyStatus == 3) {
         var10000 = true;
      }

      return var10000;
   }

   public int getDaliStatus() {
      if (!this.executedFlag) {
         this.execute();
      }

      return this.daliReplyStatus;
   }

   public int getResult() {
      if (!this.executedFlag) {
         this.execute();
      }

      if (!this.success()) {
         return -1;
      } else if (this.daliRxData == null) {
         return -1;
      } else {
         return this.daliRxData.length < 1 ? -1 : 255 & this.daliRxData[0];
      }
   }

   public void setTransmitRepeatCount(int var1) {
      this.transmitRepeatCount = var1;
      if (this.transmitRepeatCount > 3) {
         this.transmitRepeatCount = 3;
      }

   }

   public static void setIgnorePowerState(boolean var0) {
      ignorePowerState = var0;
   }

   public DaliOperation(BDaliNetwork var1, int var2, int var3, int var4) {
     this.daliNetwork = null;
     this.daliDevice = null;
     this.daliGroupAddressFlag = false;
     this.daliCommandFlag = false;
     this.daliAddressByte = 0;
     this.daliDataByte = 0;
     this.daliDeviceType = -1;
     this.receiveBitCount = 0;
     this.transmitRepeatCount = 0;
     this.daliRxData = null;
     this.daliReplyStatus = 15;
     
      this.daliNetwork = var1;
      this.daliGroupAddressFlag = false;
      this.daliCommandFlag = false;
      this.daliAddressByte = var2;
      this.daliDataByte = var3;
      this.receiveBitCount = var4;
      this.create();
   }

   public DaliOperation(BDaliNetwork var1, boolean var2, boolean var3, int var4, int var5, int var6) {
     this.daliNetwork = null;
     this.daliDevice = null;
     this.daliGroupAddressFlag = false;
     this.daliCommandFlag = false;
     this.daliAddressByte = 0;
     this.daliDataByte = 0;
     this.daliDeviceType = -1;
     this.receiveBitCount = 0;
     this.transmitRepeatCount = 0;
     this.daliRxData = null;
     this.daliReplyStatus = 15;
     
      this.daliNetwork = var1;
      this.daliDevice = null;
      this.daliGroupAddressFlag = var2;
      this.daliCommandFlag = var3;
      this.daliAddressByte = var4 << 1 & 255;
      this.daliDataByte = var5;
      this.receiveBitCount = var6;
      this.create();
   }

   public DaliOperation(BGenericDaliDevice var1, boolean var2, int var3, int var4) {
     this.daliNetwork = null;
     this.daliDevice = null;
     this.daliGroupAddressFlag = false;
     this.daliCommandFlag = false;
     this.daliAddressByte = 0;
     this.daliDataByte = 0;
     this.daliDeviceType = -1;
     this.receiveBitCount = 0;
     this.transmitRepeatCount = 0;
     this.daliRxData = null;
     this.daliReplyStatus = 15;
     
      if (var1 != null && var1.getDaliAddress() >= 0 && var1.getDaliAddress() <= 64) {
         this.daliNetwork = var1.getDaliNetwork();
         this.daliDevice = var1;
         this.daliGroupAddressFlag = false;
         this.daliCommandFlag = var2;
         this.daliAddressByte = var1.getDaliAddress() << 1 & 255;
         this.daliDataByte = var3;
         this.daliDeviceType = var1.getDaliDeviceType().getOrdinal();
         this.receiveBitCount = var4;
         this.create();
      } else {
         throw new IllegalArgumentException("Invalid DALI operation");
      }
   }
}
