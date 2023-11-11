package uk.co.controlnetworksolutions.elitedali2.dali.utils;

public class FifthLightUtils {
   public static final double LIGHT_LEVEL_LOW_DIVISOR = 2.55;
   public static final double LIGHT_LEVEL_HIGH_DIVISOR = 0.6375;
   public static final double TEMPERATURE_OFFSET = 77.0;
   public static final double TEMPERATURE_DIVISOR = 4.0;
   public static final int HOLDING_TIME_MULTIPLIER = 5;

   public static boolean getSensorOccupancy(int var0) {
      return var0 != 0;
   }

   public static double getSensorLuxLow(int var0) {
      return (double)var0 / 2.55;
   }

   public static double getSensorLuxHigh(int var0) {
      return (double)var0 / 0.6375;
   }

   public static double getSensorTemperature(int var0) {
      return ((double)var0 - 77.0) / (double)4;
   }

   public static int convertHoldingTime(double var0) {
      return (int)(var0 / (double)5);
   }
}
