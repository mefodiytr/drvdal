package uk.co.controlnetworksolutions.elitedali2.dim.msg;

import com.tridium.basicdriver.message.ReceivedMessage;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public class DimMessageStateMachine {
   private static final int MSG_STATE_AWAITING_ADDRESS = 11;
   private static final int MSG_STATE_AWAITING_FUNCTION_CODE = 22;
   private static final int MSG_STATE_AWAITING_OPCODE = 33;
   private static final int MSG_STATE_AWAITING_SIZE = 44;
   private static final int MSG_STATE_AWAITING_STATUS = 55;
   private static final int MSG_STATE_READING_BODY_DATA = 66;
   private static final int MSG_STATE_AWAITING_CRC_LOW = 77;
   private static final int MSG_STATE_AWAITING_CRC_HIGH = 88;
   private static final int MSG_STATE_INVALID_MESSAGE = 99;
   private static final int MSG_DATA_BUFFER_SIZE = 264;
   private int msgState;
   private int msgAddress;
   private int msgOpcode;
   private boolean msgCrcEnable;
   private int msgResponseStatus;
   private int msgBodySize;
   private int msgBodyIndex;
   private int msgCrcLo;
   private int msgCrcHi;
   private int msgDataIndex;
   private byte[] msgDataBuffer;

   public void reset() {
      this.msgState = 11;
   }

   public void setAddress(int var1) {
      this.msgAddress = var1;
   }

   public void setOpCode(int var1) {
      this.msgOpcode = var1;
   }

   public void setCrcEnable(boolean var1) {
      this.msgCrcEnable = var1;
   }

   public ReceivedMessage processInput(int var1) {
      boolean var2 = false;
      boolean var3 = false;
      if (var1 >= 0) {
         switch (this.msgState) {
            case 11:
               if (this.msgAddress != 0 && var1 != this.msgAddress) {
                  var3 = true;
               } else {
                  this.msgState = 22;
               }

               this.msgDataIndex = 0;
               break;
            case 22:
               if (var1 == 102) {
                  this.msgState = 33;
               } else {
                  var3 = true;
               }
               break;
            case 33:
               if (var1 == this.msgOpcode) {
                  this.msgState = 44;
               } else {
                  var3 = true;
               }
               break;
            case 44:
               this.msgBodySize = var1;
               if (this.msgBodySize < 255) {
                  this.msgState = 55;
               } else {
                  var3 = true;
               }
               break;
            case 55:
               if (var1 != 172 && var1 != 166 && var1 != 165) {
                  var3 = true;
               } else {
                  this.msgResponseStatus = var1;
                  this.msgBodyIndex = 0;
                  if (this.msgBodySize > 0) {
                     this.msgState = 66;
                  } else {
                     this.msgState = 77;
                  }
               }
               break;
            case 66:
               ++this.msgBodyIndex;
               if (this.msgBodyIndex >= this.msgBodySize) {
                  this.msgState = 77;
               }
               break;
            case 77:
               this.msgCrcLo = var1;
               this.msgState = 88;
               break;
            case 88:
               this.msgCrcHi = var1;
               this.msgState = 11;
               var2 = true;
               break;
            default:
               this.msgState = 11;
         }

         if (this.msgDataIndex < 264) {
            this.msgDataBuffer[this.msgDataIndex] = (byte)var1;
            ++this.msgDataIndex;
         }
      }

      if (var3) {
         this.msgState = 11;
         this.msgDataIndex = 0;
      }

      if (var2) {
         if (!this.msgCrcEnable) {
            return this.GetResponse();
         }

         byte[] var4 = DimMessageCrc.calculate(this.msgDataBuffer, this.msgDataIndex - 2);
         if (this.msgDataBuffer[this.msgDataIndex - 2] == var4[0] && this.msgDataBuffer[this.msgDataIndex - 1] == var4[1]) {
            return this.GetResponse();
         }

         Report.dim.trace("ERROR: CRC failed");
      }

      return null;
   }

   private final DimMessageResponse GetResponse() {
      byte[] var1 = new byte[this.msgDataIndex];
      System.arraycopy(this.msgDataBuffer, 0, var1, 0, this.msgDataIndex);
      this.msgState = 11;
      this.msgDataIndex = 0;
      return new DimMessageResponse(var1);
   }

   public DimMessageStateMachine() {
     this.msgState = 11;
     this.msgAddress = 0;
     this.msgOpcode = 0;
     this.msgCrcEnable = true;
     this.msgResponseStatus = 175;
     this.msgBodySize = 0;
     this.msgBodyIndex = 0;
     this.msgCrcLo = 0;
     this.msgCrcHi = 0;
     this.msgDataIndex = 0;
     this.msgDataBuffer = new byte[264];
   }
}
