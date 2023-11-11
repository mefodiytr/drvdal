package uk.co.controlnetworksolutions.elitedali2.dali.enums;

import javax.baja.sys.BFrozenEnum;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BFadeTime extends BFrozenEnum {
   public static final int NO_FADE = 0;
   public static final int SECONDS_0_7 = 1;
   public static final int SECONDS_1 = 2;
   public static final int SECONDS_1_4 = 3;
   public static final int SECONDS_2 = 4;
   public static final int SECONDS_3 = 5;
   public static final int SECONDS_4 = 6;
   public static final int SECONDS_6 = 7;
   public static final int SECONDS_8 = 8;
   public static final int SECONDS_11 = 9;
   public static final int SECONDS_16 = 10;
   public static final int SECONDS_23 = 11;
   public static final int SECONDS_32 = 12;
   public static final int SECONDS_45 = 13;
   public static final int SECONDS_64 = 14;
   public static final int SECONDS_90 = 15;
   public static final BFadeTime NoFade = new BFadeTime(0);
   public static final BFadeTime Seconds0_7 = new BFadeTime(1);
   public static final BFadeTime Seconds1 = new BFadeTime(2);
   public static final BFadeTime Seconds1_4 = new BFadeTime(3);
   public static final BFadeTime Seconds2 = new BFadeTime(4);
   public static final BFadeTime Seconds3 = new BFadeTime(5);
   public static final BFadeTime Seconds4 = new BFadeTime(6);
   public static final BFadeTime Seconds6 = new BFadeTime(7);
   public static final BFadeTime Seconds8 = new BFadeTime(8);
   public static final BFadeTime Seconds11 = new BFadeTime(9);
   public static final BFadeTime Seconds16 = new BFadeTime(10);
   public static final BFadeTime Seconds23 = new BFadeTime(11);
   public static final BFadeTime Seconds32 = new BFadeTime(12);
   public static final BFadeTime Seconds45 = new BFadeTime(13);
   public static final BFadeTime Seconds64 = new BFadeTime(14);
   public static final BFadeTime Seconds90 = new BFadeTime(15);
   public static final Type TYPE;
   public static final BFadeTime DEFAULT;

   public final Type getType() {
      return TYPE;
   }

   public static final BFadeTime make(int var0) {
      return (BFadeTime)NoFade.getRange().get(var0, false);
   }

   public static final BFadeTime make(String var0) {
      return (BFadeTime)NoFade.getRange().get(var0);
   }

   private BFadeTime(int var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BFadeTime.class);
      DEFAULT = NoFade;
   }
}
