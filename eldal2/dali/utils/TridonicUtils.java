package uk.co.controlnetworksolutions.elitedali2.dali.utils;

public class TridonicUtils {
   public static boolean getSensorOccupancy(int var0) {
      boolean var10000 = false;
      if ((var0 & 128) != 0) {
         var10000 = true;
      }

      return var10000;
   }

   public static double getSensorLux(int var0) {
      int var1 = 0;
      if (var0 >= 0) {
         int var2 = var0 >> 5 & 3;
         int var3 = var0 & 31;
         if (var2 == 0) {
            var1 = var3 << 2;
         } else if (var2 == 1) {
            var1 = var3 << 2 | 128;
         } else if (var2 == 2) {
            var1 = var3 << 3 | 256;
         } else if (var2 == 3) {
            var1 = var3 << 4 | 512;
         }
      }

      return (double)var1;
   }
}
