package uk.co.controlnetworksolutions.elitedali2.dali.utils;

public class AurexIndustriesUtils {
   public static boolean getSensorOccupancy(int var0) {
      boolean var10000 = false;
      if ((var0 & 128) != 0) {
         var10000 = true;
      }

      return var10000;
   }

   public static double getSensorLux(int var0) {
      return (double)((var0 & 127) << 4);
   }
}
