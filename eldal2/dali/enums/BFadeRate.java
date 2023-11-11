package uk.co.controlnetworksolutions.elitedali2.dali.enums;

import javax.baja.sys.BFrozenEnum;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BFadeRate extends BFrozenEnum {
   public static final int NOT_APPLICABLE = 0;
   public static final int STEPS_360 = 1;
   public static final int STEPS_250 = 2;
   public static final int STEPS_180 = 3;
   public static final int STEPS_120 = 4;
   public static final int STEPS_90 = 5;
   public static final int STEPS_60 = 6;
   public static final int STEPS_45 = 7;
   public static final int STEPS_30 = 8;
   public static final int STEPS_20 = 9;
   public static final int STEPS_15 = 10;
   public static final int STEPS_10 = 11;
   public static final int STEPS_8 = 12;
   public static final int STEPS_5 = 13;
   public static final int STEPS_4 = 14;
   public static final int STEPS_3 = 15;
   public static final BFadeRate NotApplicable = new BFadeRate(0);
   public static final BFadeRate Steps_360 = new BFadeRate(1);
   public static final BFadeRate Steps_250 = new BFadeRate(2);
   public static final BFadeRate Steps_180 = new BFadeRate(3);
   public static final BFadeRate Steps_120 = new BFadeRate(4);
   public static final BFadeRate Steps_90 = new BFadeRate(5);
   public static final BFadeRate Steps_60 = new BFadeRate(6);
   public static final BFadeRate Steps_45 = new BFadeRate(7);
   public static final BFadeRate Steps_30 = new BFadeRate(8);
   public static final BFadeRate Steps_20 = new BFadeRate(9);
   public static final BFadeRate Steps_15 = new BFadeRate(10);
   public static final BFadeRate Steps_10 = new BFadeRate(11);
   public static final BFadeRate Steps_8 = new BFadeRate(12);
   public static final BFadeRate Steps_5 = new BFadeRate(13);
   public static final BFadeRate Steps_4 = new BFadeRate(14);
   public static final BFadeRate Steps_3 = new BFadeRate(15);
   public static final Type TYPE;
   public static final BFadeRate DEFAULT;

   public final Type getType() {
      return TYPE;
   }

   public static final BFadeRate make(int var0) {
      return (BFadeRate)NotApplicable.getRange().get(var0, false);
   }

   public static final BFadeRate make(String var0) {
      return (BFadeRate)NotApplicable.getRange().get(var0);
   }

   private BFadeRate(int var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BFadeRate.class);
      DEFAULT = NotApplicable;
   }
}
