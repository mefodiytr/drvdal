package uk.co.controlnetworksolutions.elitedali2.dim.msg;

import com.tridium.basicdriver.message.ReceivedMessage;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;
import uk.co.controlnetworksolutions.elitedali2.utils.StrUtil;

public class DimMessageResponse extends ReceivedMessage {
   private byte[] messageData;

   public byte[] getBodyData() {
      byte[] var2 = null;
      if (this.messageData != null) {
         if (this.messageData.length >= 7) {
            int var1 = 255 & this.messageData[3];
            if (var1 > 0) {
               if (var1 <= this.messageData.length - 7) {
                  var2 = new byte[var1];
                  System.arraycopy(this.messageData, 5, var2, 0, var2.length);
               } else {
                  Report.dim.trace("ERROR: Received invalid message body");
               }
            }
         } else {
            Report.dim.trace("ERROR: Received message too small");
         }
      } else {
         Report.dim.trace("ERROR: Received null message");
      }

      return var2;
   }

   public int getStatusCode() {
      return this.messageData != null && this.messageData.length > 5 ? 255 & this.messageData[4] : -1;
   }

   public String toDebugString() {
      return this.messageData == null ? null : StrUtil.dataToHex(this.messageData, "");
   }

   public DimMessageResponse() {
     this.messageData = null;
     
      this.messageData = null;
   }

   public DimMessageResponse(byte[] var1) {
     this.messageData = null;
     
      this.messageData = var1;
   }
}
