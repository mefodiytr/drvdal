package uk.co.controlnetworksolutions.elitedali2.dali.enums;

import javax.baja.sys.BFrozenEnum;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public final class BOccupancy extends BFrozenEnum {
   public static final int OCCUPIED = 0;
   public static final int UNOCCUPIED = 1;
   public static final int BYPASS = 2;
   public static final int STANDBY = 3;
   public static final int OCC_NULL = -1;
   public static final BOccupancy occupied = new BOccupancy(0);
   public static final BOccupancy unoccupied = new BOccupancy(1);
   public static final BOccupancy bypass = new BOccupancy(2);
   public static final BOccupancy standby = new BOccupancy(3);
   public static final BOccupancy occNull = new BOccupancy(-1);
   public static final Type TYPE;
   public static final BOccupancy DEFAULT;

   public final Type getType() {
      return TYPE;
   }

   public static final BOccupancy make(int var0) {
      return (BOccupancy)occupied.getRange().get(var0, false);
   }

   public static final BOccupancy make(String var0) {
      return (BOccupancy)occupied.getRange().get(var0);
   }

   private BOccupancy(int var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BOccupancy.class);
      DEFAULT = occupied;
   }
}
