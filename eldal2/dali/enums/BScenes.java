package uk.co.controlnetworksolutions.elitedali2.dali.enums;

import javax.baja.sys.BFrozenEnum;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BScenes extends BFrozenEnum {
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
   public static final BScenes scene1 = new BScenes(16);
   public static final BScenes scene2 = new BScenes(17);
   public static final BScenes scene3 = new BScenes(18);
   public static final BScenes scene4 = new BScenes(19);
   public static final BScenes scene5 = new BScenes(20);
   public static final BScenes scene6 = new BScenes(21);
   public static final BScenes scene7 = new BScenes(22);
   public static final BScenes scene8 = new BScenes(23);
   public static final BScenes scene9 = new BScenes(24);
   public static final BScenes scene10 = new BScenes(25);
   public static final BScenes scene11 = new BScenes(26);
   public static final BScenes scene12 = new BScenes(27);
   public static final BScenes scene13 = new BScenes(28);
   public static final BScenes scene14 = new BScenes(29);
   public static final BScenes scene15 = new BScenes(30);
   public static final BScenes scene16 = new BScenes(31);
   public static final Type TYPE;
   public static final BScenes DEFAULT;

   public final Type getType() {
      return TYPE;
   }

   public static final BScenes make(int var0) {
      return (BScenes)scene1.getRange().get(var0, false);
   }

   public static final BScenes make(String var0) {
      return (BScenes)scene1.getRange().get(var0);
   }

   private BScenes(int var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BScenes.class);
      DEFAULT = scene1;
   }
}
