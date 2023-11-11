package uk.co.controlnetworksolutions.elitedali2.dali.ops;

public class DaliStatus {
   public static final int OK = 53;
   public static final int NO_RESPONSE = 1;
   public static final int CORRUPT_TX = 2;
   public static final int CORRUPT_RX = 3;
   public static final int NO_DALI_POWER = 4;
   public static final int RX_FAILURE = 14;
   public static final int REQUEST_INVALID = 15;
   public static final int DIM_DOWN = 34;
   public static final int SYSTEM_INVALID = 69;

   public static String toString(int var0) {
      String var1;
      switch (var0) {
         case 1:
            var1 = "No response from DALI device";
            break;
         case 2:
            var1 = "DALI transmission corrupted";
            break;
         case 3:
            var1 = "Multiple DALI response";
            break;
         case 4:
            var1 = "No DALI power";
            break;
         case 14:
            var1 = "DALI reception failure";
            break;
         case 15:
            var1 = "DALI request invalid";
            break;
         case 34:
            var1 = "No response from DIM";
            break;
         case 53:
            var1 = "OK";
            break;
         default:
            var1 = "Unknown DALI status [" + var0 + ']';
      }

      return var1;
   }
}
