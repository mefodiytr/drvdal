package uk.co.controlnetworksolutions.elitedali2.utils;

import java.text.NumberFormat;

public final class StrUtil {
   public static final String byteToBinary(byte var0) {
      return byteToBinary(255 & var0);
   }

   public static final String byteToBinary(int var0) {
      String var1 = "0000000000" + Integer.toBinaryString(var0);
      return var1.substring(var1.length() - 8, var1.length());
   }

   public static final String dataToHex(byte[] var0) {
      return dataToHex(var0, "/");
   }

   public static final String dataToHex(byte[] var0, String var1) {
      String var2 = "";
      if (var0 != null) {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            if (var3 != 0) {
               var2 = var2 + var1;
            }

            var2 = var2 + paddedHex(255 & var0[var3], 2);
         }
      } else {
         var2 = "null";
      }

      return var2;
   }

   public static final String paddedHex(int var0) {
      return paddedHex((byte)var0, 2);
   }

   public static final String paddedHex(byte var0) {
      return paddedHex(var0, 2);
   }

   public static final String paddedHex(long var0, int var2) {
      return paddedStr(Long.toHexString(var0).toUpperCase(), var2);
   }

   public static final String paddedHex(int var0, int var1) {
      return paddedStr(Integer.toHexString(var0).toUpperCase(), var1);
   }

   public static final String paddedStr(String var0, int var1) {
      String var2 = "0000000000" + var0;
      return var2.substring(var2.length() - var1, var2.length());
   }

   public static final String numberFormat2(double var0) {
      NumberFormat var2 = NumberFormat.getInstance();
      var2.setMaximumFractionDigits(2);
      return var2.format(var0);
   }
}
