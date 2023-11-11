package uk.co.controlnetworksolutions.elitedali2.dim.ops;

import javax.baja.sys.BRelTime;
import uk.co.controlnetworksolutions.elitedali2.dim.msg.DimMessageRequest;
import uk.co.controlnetworksolutions.elitedali2.dim.msg.DimMessageResponse;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimNetwork;

public abstract class DimOperation {
   private static int messageFaultThreshold = 1;
   private static int sequentialFaultCounter = 0;
   protected BDimNetwork dimNetwork;
   protected BDimDevice dimDevice;
   protected int dimAddress;
   protected int dimOpcode;
   protected boolean dimCrcEnable;
   protected int interByteDelay;
   protected long messageTimeout;
   protected int messageRetryCount;
   protected DimMessageRequest messageRequest;
   protected DimMessageResponse messageReply;
   protected int replyStatusCode;
   protected byte[] bodyData;
   protected byte[] replyData;
   protected boolean executedFlag;
   protected boolean dimOperationSuccess;
   protected boolean dimResponseFlag;
   protected String faultText;

   protected boolean sendMessage() {
      this.dimOperationSuccess = false;
      this.dimResponseFlag = false;
      if (this.dimOpcode < 0) {
         this.setFaultText("System fault (null DIM operation)");
         return false;
      } else if (this.dimAddress < 0) {
         this.setFaultText("System fault (null DIM device)");
         return false;
      } else if (this.dimNetwork == null) {
         this.setFaultText("System fault (null DIM network)");
         return false;
      } else {
         this.messageRequest = new DimMessageRequest(this.dimAddress, this.dimOpcode, this.bodyData);
         if (this.messageRequest == null) {
            this.setFaultText("System fault (null DIM request)");
            return false;
         } else {
            this.messageRequest.setInterByteDelay(this.interByteDelay);
            this.messageRequest.setCrcEnable(this.dimCrcEnable);
            if (!this.messageRequest.construct()) {
               this.setFaultText("System fault (message construction failed)");
               return false;
            } else {
               this.messageReply = null;

               try {
                  if (this.messageTimeout == 0L) {
                     if (this.dimNetwork != null) {
                        this.messageReply = (DimMessageResponse)this.dimNetwork.sendSync(this.messageRequest);
                     }
                  } else if (this.dimNetwork != null) {
                     this.messageReply = (DimMessageResponse)this.dimNetwork.sendSync(this.messageRequest, BRelTime.make(this.messageTimeout), this.messageRetryCount);
                  }
               } catch (Exception var2) {
                  this.dimDeviceFault(1, "DIM transaction exception", false);
                  return false;
               }

               this.executedFlag = true;
               if (this.messageReply == null) {
                  if (this.dimNetwork == null) {
                     this.dimDeviceFault(2, "DIM Network null", true);
                  } else if (this.dimNetwork.isDisabled()) {
                     this.dimDeviceFault(3, "DIM Network disabled", true);
                  } else {
                     this.setFaultText("No response from DIM [" + this.dimAddress + ']');
                     ++sequentialFaultCounter;
                     if (sequentialFaultCounter > messageFaultThreshold) {
                        this.dimDeviceFault(4, "No response from DIM [" + this.dimAddress + ']', true);
                     }
                  }

                  return false;
               } else {
                  this.replyStatusCode = 255 & this.messageReply.getStatusCode();
                  if (this.dimDevice != null) {
                     this.dimDevice.setDeviceOk();
                  }

                  sequentialFaultCounter = 0;
                  this.dimResponseFlag = true;
                  this.replyData = this.messageReply.getBodyData();
                  return true;
               }
            }
         }
      }
   }

   public void setTimeout(int var1) {
      this.messageTimeout = (long)var1;
   }

   public void setRetryCount(int var1) {
      this.messageRetryCount = var1;
   }

   public static void setFaultThreshold(int var0) {
      messageFaultThreshold = var0;
   }

   public boolean success() {
      if (!this.executedFlag) {
         this.execute();
      }

      return this.dimOperationSuccess;
   }

   public boolean dimResponded() {
      if (!this.executedFlag) {
         this.execute();
      }

      return this.dimResponseFlag;
   }

   public byte[] getReply() {
      if (!this.executedFlag) {
         this.execute();
      }

      return this.replyData;
   }

   private final void dimDeviceFault(int var1, String var2, boolean var3) {
      this.setFaultText(var2);
      if (this.dimDevice != null) {
         this.dimDevice.setDeviceFault(var1, var2, var3);
      }

   }

   public void setFaultText(String var1) {
      this.faultText = var1;
   }

   public String getFaultText() {
      return this.faultText == null ? "None" : this.faultText;
   }

   protected abstract boolean execute();

   public DimOperation() {
     this.dimNetwork = null;
     this.dimDevice = null;
     this.dimAddress = -1;
     this.dimOpcode = -1;
     this.dimCrcEnable = true;
     this.interByteDelay = 0;
     this.messageTimeout = 0L;
     this.messageRetryCount = 2;
     this.messageRequest = null;
     this.messageReply = null;
     this.replyStatusCode = -9;
     this.bodyData = null;
     this.replyData = null;
     this.executedFlag = false;
     this.dimOperationSuccess = false;
     this.dimResponseFlag = false;
     this.faultText = "Unknown";
   }

   public DimOperation(BDimNetwork var1, BDimDevice var2, int var3, int var4) {
     this.dimNetwork = null;
     this.dimDevice = null;
     this.dimAddress = -1;
     this.dimOpcode = -1;
     this.dimCrcEnable = true;
     this.interByteDelay = 0;
     this.messageTimeout = 0L;
     this.messageRetryCount = 2;
     this.messageRequest = null;
     this.messageReply = null;
     this.replyStatusCode = -9;
     this.bodyData = null;
     this.replyData = null;
     this.executedFlag = false;
     this.dimOperationSuccess = false;
     this.dimResponseFlag = false;
     this.faultText = "Unknown";
     
      this.dimNetwork = var1;
      this.dimDevice = var2;
      this.dimAddress = var3;
      this.dimOpcode = var4;
   }
}
