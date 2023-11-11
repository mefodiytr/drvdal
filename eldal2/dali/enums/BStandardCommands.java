package uk.co.controlnetworksolutions.elitedali2.dali.enums;

import javax.baja.sys.BFrozenEnum;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BStandardCommands extends BFrozenEnum {
   public static final int OFF = 0;
   public static final int UP = 1;
   public static final int DOWN = 2;
   public static final int STEP_UP = 3;
   public static final int STEP_DOWN = 4;
   public static final int RECALL_MAX = 5;
   public static final int RECALL_MIN = 6;
   public static final int STEP_DOWN_OFF = 7;
   public static final int ON_STEP_UP = 8;
   public static final int SCENE_1 = 16;
   public static final int SCENE_2 = 17;
   public static final int SCENE_3 = 18;
   public static final int SCENE_4 = 19;
   public static final int SCENE_5 = 20;
   public static final int SCENE_6 = 21;
   public static final int SCENE_7 = 22;
   public static final int SCENE_8 = 23;
   public static final int SCENE_9 = 24;
   public static final int SCENE_10 = 25;
   public static final int SCENE_11 = 26;
   public static final int SCENE_12 = 27;
   public static final int SCENE_13 = 28;
   public static final int SCENE_14 = 29;
   public static final int SCENE_15 = 30;
   public static final int SCENE_16 = 31;
   public static final int IDLE = 255;
   public static final BStandardCommands off = new BStandardCommands(0);
   public static final BStandardCommands up = new BStandardCommands(1);
   public static final BStandardCommands down = new BStandardCommands(2);
   public static final BStandardCommands stepUp = new BStandardCommands(3);
   public static final BStandardCommands stepDown = new BStandardCommands(4);
   public static final BStandardCommands recallMax = new BStandardCommands(5);
   public static final BStandardCommands recallMin = new BStandardCommands(6);
   public static final BStandardCommands stepDownOff = new BStandardCommands(7);
   public static final BStandardCommands onStepUp = new BStandardCommands(8);
   public static final BStandardCommands scene1 = new BStandardCommands(16);
   public static final BStandardCommands scene2 = new BStandardCommands(17);
   public static final BStandardCommands scene3 = new BStandardCommands(18);
   public static final BStandardCommands scene4 = new BStandardCommands(19);
   public static final BStandardCommands scene5 = new BStandardCommands(20);
   public static final BStandardCommands scene6 = new BStandardCommands(21);
   public static final BStandardCommands scene7 = new BStandardCommands(22);
   public static final BStandardCommands scene8 = new BStandardCommands(23);
   public static final BStandardCommands scene9 = new BStandardCommands(24);
   public static final BStandardCommands scene10 = new BStandardCommands(25);
   public static final BStandardCommands scene11 = new BStandardCommands(26);
   public static final BStandardCommands scene12 = new BStandardCommands(27);
   public static final BStandardCommands scene13 = new BStandardCommands(28);
   public static final BStandardCommands scene14 = new BStandardCommands(29);
   public static final BStandardCommands scene15 = new BStandardCommands(30);
   public static final BStandardCommands scene16 = new BStandardCommands(31);
   public static final BStandardCommands idle = new BStandardCommands(255);
   public static final Type TYPE;
   public static final BStandardCommands DEFAULT;

   public final Type getType() {
      return TYPE;
   }

   public static final BStandardCommands make(int var0) {
      return (BStandardCommands)off.getRange().get(var0, false);
   }

   public static final BStandardCommands make(String var0) {
      return (BStandardCommands)off.getRange().get(var0);
   }

   private BStandardCommands(int var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BStandardCommands.class);
      DEFAULT = off;
   }
}
