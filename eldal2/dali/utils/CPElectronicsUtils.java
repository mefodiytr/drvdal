package uk.co.controlnetworksolutions.elitedali2.dali.utils;

public class CPElectronicsUtils {
   public static boolean getSensorOccupancy(int var0) {
      boolean var10000 = false;
      if ((var0 & 128) != 0) {
         var10000 = true;
      }

      return var10000;
   }

   public static double getSensorLux(int var0) {
      double var1 = 0.0;
      if (var0 >= 0) {
         var1 = (double)(var0 & 127) * 7.8;
      }

      return var1;
   }
}
