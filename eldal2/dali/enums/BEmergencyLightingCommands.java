package uk.co.controlnetworksolutions.elitedali2.dali.enums;

import javax.baja.sys.BFrozenEnum;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BEmergencyLightingCommands extends BFrozenEnum {
   public static final int IDLE = 0;
   public static final int REST = 224;
   public static final int INHIBIT = 225;
   public static final int RELIGHT_RESET_INHIBIT = 226;
   public static final int START_FUNCTION_TEST = 227;
   public static final int START_DURATION_TEST = 228;
   public static final int STOP_TEST = 229;
   public static final int RESET_FUNCTION_TEST_FLAG = 230;
   public static final int RESET_DURATION_TEST_FLAG = 231;
   public static final int RESET_LAMP_TIME = 232;
   public static final BEmergencyLightingCommands idle = new BEmergencyLightingCommands(0);
   public static final BEmergencyLightingCommands rest = new BEmergencyLightingCommands(224);
   public static final BEmergencyLightingCommands inhibit = new BEmergencyLightingCommands(225);
   public static final BEmergencyLightingCommands relightResetInhibit = new BEmergencyLightingCommands(226);
   public static final BEmergencyLightingCommands startFunctionTest = new BEmergencyLightingCommands(227);
   public static final BEmergencyLightingCommands startDurationTest = new BEmergencyLightingCommands(228);
   public static final BEmergencyLightingCommands stopTest = new BEmergencyLightingCommands(229);
   public static final BEmergencyLightingCommands resetFunctionTestFlag = new BEmergencyLightingCommands(230);
   public static final BEmergencyLightingCommands resetDurationTestFlag = new BEmergencyLightingCommands(231);
   public static final BEmergencyLightingCommands resetLampTime = new BEmergencyLightingCommands(232);
   public static final Type TYPE;
   public static final BEmergencyLightingCommands DEFAULT;

   public final Type getType() {
      return TYPE;
   }

   public static final BEmergencyLightingCommands make(int var0) {
      return (BEmergencyLightingCommands)idle.getRange().get(var0, false);
   }

   public static final BEmergencyLightingCommands make(String var0) {
      return (BEmergencyLightingCommands)idle.getRange().get(var0);
   }

   private BEmergencyLightingCommands(int var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BEmergencyLightingCommands.class);
      DEFAULT = idle;
   }
}
