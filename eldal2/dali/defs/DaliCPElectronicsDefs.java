package uk.co.controlnetworksolutions.elitedali2.dali.defs;

public class DaliCPElectronicsDefs {
   public static final int QUERY_COMPOSITE_SENSOR_DATA = 160;
   public static final int QUERY_INSTANTANEOUS_LUX = 161;
   public static final int START_IDENTIFICATION = 240;
   public static final int QUERY_TIMEOUT = 241;
   public static final int QUERY_SENSITIVITY = 242;
   public static final int QUERY_FLAGS = 243;
   public static final int PHYSICAL_INDICATOR_OFF = 250;
   public static final int PHYSICAL_INDICATOR_ON = 251;
   public static final int STORE_DTR_AS_TIMEOUT = 252;
   public static final int STORE_DTR_AS_SENSITIVITY = 253;
   public static final int STORE_DTR_AS_FLAGS = 254;
   public static final int DEFAULT_TIMEOUT = 1;
   public static final int MIN_SENSITIVITY = 1;
   public static final int MAX_SENSITIVITY = 9;
   public static final int DEFAULT_SENSITIVITY = 8;

   public class Flags {
      public static final int WALK_TEST_ON = 1;
      public static final int POWER_UP_ON = 2;
      public static final int DISABLE_DETECTION = 4;
      public static final int ENABLE_IR = 128;
   }
}
