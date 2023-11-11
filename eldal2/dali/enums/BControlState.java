package uk.co.controlnetworksolutions.elitedali2.dali.enums;

import javax.baja.sys.BFrozenEnum;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BControlState extends BFrozenEnum {
   public static final int OFF = 0;
   public static final int ON = 1;
   public static final int AUTO = 255;
   public static final int AUTO_NO_CLC = 10;
   public static final int AUTO_NO_OCC = 20;
   public static final int DISABLE = 50;
   public static final int MANUAL = 100;
   public static final int NULL_STATE = -1;
   public static final BControlState off = new BControlState(0);
   public static final BControlState on = new BControlState(1);
   public static final BControlState auto = new BControlState(255);
   public static final BControlState auto_no_clc = new BControlState(10);
   public static final BControlState auto_no_occ = new BControlState(20);
   public static final BControlState disable = new BControlState(50);
   public static final BControlState manual = new BControlState(100);
   public static final BControlState nullState = new BControlState(-1);
   public static final Type TYPE;
   public static final BControlState DEFAULT;

   public final Type getType() {
      return TYPE;
   }

   public static final BControlState make(int var0) {
      return (BControlState)off.getRange().get(var0, false);
   }

   public static final BControlState make(String var0) {
      return (BControlState)off.getRange().get(var0);
   }

   private BControlState(int var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BControlState.class);
      DEFAULT = off;
   }
}
