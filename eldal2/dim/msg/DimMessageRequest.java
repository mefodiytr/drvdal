package uk.co.controlnetworksolutions.elitedali2.dim.msg;

import com.tridium.basicdriver.message.Message;
import com.tridium.basicdriver.message.ReceivedMessage;
import java.io.OutputStream;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;
import uk.co.controlnetworksolutions.elitedali2.utils.StrUtil;

public class DimMessageRequest extends Message {
   private boolean dimAddressSetFlag;
   private int dimAddress;
   private boolean dimOpcodeSetFlag;
   private int dimOpcode;
   private boolean dimCrcEnable;
   private byte[] messageData;
   private byte[] bodyData;
   private int interByteDelay;

   public void setDimAddress(int var1) {
      this.dimAddress = var1;
      this.dimAddressSetFlag = true;
   }

   public int getDimAddress() {
      return this.dimAddress;
   }

   public void setDimOpcode(int var1) {
      this.dimOpcode = var1;
      this.dimOpcodeSetFlag = true;
   }

   public int getDimOpcode() {
      return this.dimOpcode;
   }

   public void setCrcEnable(boolean var1) {
      this.dimCrcEnable = var1;
   }

   public boolean getCrcEnable() {
      return this.dimCrcEnable;
   }

   public void setInterByteDelay(int var1) {
      this.interByteDelay = var1;
   }

   public void setBodyData(byte[] var1) {
      this.bodyData = var1;
   }

   public byte[] getBodyData() {
      return null;
   }

   public boolean construct() {
      this.messageData = null;
      if (!this.dimAddressSetFlag) {
         Report.dim.trace("ERROR: DIM address not set");
         return false;
      } else if (!this.dimOpcodeSetFlag) {
         Report.dim.trace("ERROR: DIM operation not defined");
         return false;
      } else {
         int var1;
         if (this.bodyData == null) {
            var1 = 0;
         } else {
            var1 = this.bodyData.length;
         }

         int var2 = 7 + var1;
         this.messageData = new byte[var2];
         this.messageData[0] = (byte)this.dimAddress;
         this.messageData[1] = 102;
         this.messageData[2] = (byte)this.dimOpcode;
         this.messageData[3] = (byte)var1;
         this.messageData[4] = -84;
         if (var1 > 0) {
            System.arraycopy(this.bodyData, 0, this.messageData, 5, this.bodyData.length);
         }

         byte[] var3 = DimMessageCrc.calculate(this.messageData, var2 - 2);
         this.messageData[var2 - 2] = var3[0];
         this.messageData[var2 - 1] = var3[1];
         return true;
      }
   }

   public void write(OutputStream var1) {
      if (this.messageData == null) {
         this.construct();
      }

      if (this.messageData != null) {
         try {
            if (this.interByteDelay == 0) {
               var1.write(this.messageData);
            } else {
               int var2 = this.interByteDelay / 1000000;
               int var3 = this.interByteDelay % 1000000;

               for(int var4 = 0; var4 < this.messageData.length; ++var4) {
                  var1.write(this.messageData[var4]);
                  Thread.sleep((long)var2, var3);
               }
            }
         } catch (Exception var5) {
            Report.dim.trace("ERROR: Message write exception");
            var5.printStackTrace();
         }
      } else {
         Report.dim.trace("ERROR: Attempt to write null message");
      }

   }

   public Message toResponse(ReceivedMessage var1) {
      return var1;
   }

   public String toDebugString() {
      return this.messageData == null ? null : StrUtil.dataToHex(this.messageData, "");
   }

   public DimMessageRequest() {
     this.dimAddressSetFlag = false;
     this.dimAddress = -1;
     this.dimOpcodeSetFlag = false;
     this.dimOpcode = -1;
     this.dimCrcEnable = true;
     this.messageData = null;
     this.bodyData = null;
     this.interByteDelay = 0;
     
      this.setResponseExpected(true);
      this.dimAddressSetFlag = false;
      this.dimOpcodeSetFlag = false;
   }

   public DimMessageRequest(int var1) {
     this.dimAddressSetFlag = false;
     this.dimAddress = -1;
     this.dimOpcodeSetFlag = false;
     this.dimOpcode = -1;
     this.dimCrcEnable = true;
     this.messageData = null;
     this.bodyData = null;
     this.interByteDelay = 0;
     
      this.setResponseExpected(true);
      this.dimAddress = var1;
      this.dimAddressSetFlag = true;
      this.dimOpcodeSetFlag = false;
   }

   public DimMessageRequest(int var1, int var2) {
     this.dimAddressSetFlag = false;
     this.dimAddress = -1;
     this.dimOpcodeSetFlag = false;
     this.dimOpcode = -1;
     this.dimCrcEnable = true;
     this.messageData = null;
     this.bodyData = null;
     this.interByteDelay = 0;
     
      this.setResponseExpected(true);
      this.dimAddress = var1;
      this.dimAddressSetFlag = true;
      this.dimOpcode = var2;
      this.dimOpcodeSetFlag = true;
   }

   public DimMessageRequest(int var1, int var2, byte[] var3) {
     this.dimAddressSetFlag = false;
     this.dimAddress = -1;
     this.dimOpcodeSetFlag = false;
     this.dimOpcode = -1;
     this.dimCrcEnable = true;
     this.messageData = null;
     this.bodyData = null;
     this.interByteDelay = 0;
     
      this.setResponseExpected(true);
      this.dimAddress = var1;
      this.dimAddressSetFlag = true;
      this.dimOpcode = var2;
      this.dimOpcodeSetFlag = true;
      this.bodyData = var3;
   }
}
