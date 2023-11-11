package uk.co.controlnetworksolutions.elitedali2.dali.utils;

public class DaliArcPowerUtils {
   public static int percentToDirectLevel(double var0) {
      double var4 = 0.0;
      if (var0 <= 0.0) {
         var4 = 0.0;
      } else if (var0 >= 100.0) {
         var4 = 254.0;
      } else {
         try {
            double var2 = Math.log(var0) / Math.log(10.0);
            var4 = (var2 + 1.0) * 84.33333333333333 + 1.0;
            var4 = Math.ceil(var4);
         } catch (NumberFormatException var7) {
            var4 = 0.0;
         }

         if (var4 < 0.0) {
            var4 = 0.0;
         }

         if (var4 > 254.0) {
            var4 = 254.0;
         }
      }

      return (int)var4;
   }

   public static double directLevelToPercent(int var0) {
      double var1 = 0.0;
      if (var0 <= 0) {
         var1 = 0.0;
      } else if (var0 >= 254) {
         var1 = 100.0;
      } else {
         double var3 = ((double)var0 - 1.0) / 84.33333333333333 - 1.0;
         var1 = Math.pow(10.0, var3);
         if (var1 < 0.0) {
            var1 = 0.0;
         }

         if (var1 > 100.0) {
            var1 = 100.0;
         }
      }

      return var1;
   }
}
