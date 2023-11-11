package uk.co.controlnetworksolutions.elitedali2.dali.enums;

import javax.baja.sys.BFrozenEnum;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BAnalogConverter;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BEmergencyLighting;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BHID;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BHalogen;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BIncandescent;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BLED;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BRelay;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType1;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType2;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType3;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType4;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BTridonicSensor;

public final class BDaliDeviceType extends BFrozenEnum {
   public static final int BALLAST = 0;
   public static final int EMERGENCY_LIGHTING = 1;
   public static final int HID = 2;
   public static final int HALOGEN = 3;
   public static final int INCANDESCENT = 4;
   public static final int ANALOG_CONVERTER = 5;
   public static final int LED = 6;
   public static final int RELAY = 7;
   public static final int COLOUR_CONTROL = 8;
   public static final int SENSOR_TYPE_3 = 100;
   public static final int SENSOR_TYPE_4 = 120;
   public static final int SENSOR_TYPE_2 = 128;
   public static final int SENSOR_TYPE_1 = 254;
   public static final int MULTIPLE_DEVICE_TYPES = 255;
   public static final int UNKNOWN_DEVICE = 1000;
   public static final int INVALID = -1;
   public static final BDaliDeviceType ballast = new BDaliDeviceType(0);
   public static final BDaliDeviceType emergencyLighting = new BDaliDeviceType(1);
   public static final BDaliDeviceType hid = new BDaliDeviceType(2);
   public static final BDaliDeviceType halogen = new BDaliDeviceType(3);
   public static final BDaliDeviceType incandescent = new BDaliDeviceType(4);
   public static final BDaliDeviceType analogConverter = new BDaliDeviceType(5);
   public static final BDaliDeviceType led = new BDaliDeviceType(6);
   public static final BDaliDeviceType relay = new BDaliDeviceType(7);
   public static final BDaliDeviceType colourControl = new BDaliDeviceType(8);
   public static final BDaliDeviceType sensorType3 = new BDaliDeviceType(100);
   public static final BDaliDeviceType sensorType4 = new BDaliDeviceType(120);
   public static final BDaliDeviceType sensorType2 = new BDaliDeviceType(128);
   public static final BDaliDeviceType sensorType1 = new BDaliDeviceType(254);
   public static final BDaliDeviceType multipleDeviceTypes = new BDaliDeviceType(255);
   public static final BDaliDeviceType unknownDevice = new BDaliDeviceType(1000);
   public static final BDaliDeviceType invalid = new BDaliDeviceType(-1);
   public static final Type TYPE;
   public static final BDaliDeviceType DEFAULT;
   public static final int MAX_STANDARD_DEVICE_TYPE = 8;

   public final Type getType() {
      return TYPE;
   }

   public static final BDaliDeviceType make(int var0) {
      return (BDaliDeviceType)ballast.getRange().get(var0, false);
   }

   public static final BDaliDeviceType make(String var0) {
      return (BDaliDeviceType)ballast.getRange().get(var0);
   }

   public static final boolean isSupportedDaliDevice(Type var0) {
      boolean var10000 = false;
      if (var0 == BBallast.TYPE || var0 == BEmergencyLighting.TYPE || var0 == BHID.TYPE || var0 == BHalogen.TYPE || var0 == BIncandescent.TYPE || var0 == BAnalogConverter.TYPE || var0 == BLED.TYPE || var0 == BRelay.TYPE || var0 == BSensorType1.TYPE || var0 == BSensorType2.TYPE || var0 == BSensorType3.TYPE || var0 == BSensorType4.TYPE || var0 == BTridonicSensor.TYPE) {
         var10000 = true;
      }

      return var10000;
   }

   public static final boolean isSupportedDaliDevice(BDaliDeviceType var0) {
      boolean var10000 = false;
      if (var0.getEnum().compareTo(ballast) == 0 || var0.getEnum().compareTo(emergencyLighting) == 0 || var0.getEnum().compareTo(hid) == 0 || var0.getEnum().compareTo(halogen) == 0 || var0.getEnum().compareTo(incandescent) == 0 || var0.getEnum().compareTo(analogConverter) == 0 || var0.getEnum().compareTo(led) == 0 || var0.getEnum().compareTo(relay) == 0 || var0.getEnum().compareTo(sensorType1) == 0 || var0.getEnum().compareTo(sensorType2) == 0 || var0.getEnum().compareTo(sensorType3) == 0 || var0.getEnum().compareTo(sensorType4) == 0) {
         var10000 = true;
      }

      return var10000;
   }

   public static final BDaliDeviceType getDaliDeviceType(int var0) {
      BDaliDeviceType var1 = null;
      switch (var0) {
         case 0:
            var1 = ballast;
            break;
         case 1:
            var1 = emergencyLighting;
            break;
         case 2:
            var1 = hid;
            break;
         case 3:
            var1 = halogen;
            break;
         case 4:
            var1 = incandescent;
            break;
         case 5:
            var1 = analogConverter;
            break;
         case 6:
            var1 = led;
            break;
         case 7:
            var1 = relay;
            break;
         case 8:
            var1 = colourControl;
            break;
         case 100:
            var1 = sensorType3;
            break;
         case 120:
            var1 = sensorType4;
            break;
         case 128:
            var1 = sensorType2;
            break;
         case 254:
            var1 = sensorType1;
            break;
         default:
            var1 = null;
      }

      return var1;
   }

   public final String getText() {
      return getText(this.getOrdinal());
   }

   public static final String getText(int var0) {
      String var1;
      switch (var0) {
         case 0:
            var1 = "ballast";
            break;
         case 1:
            var1 = "emergency lighting";
            break;
         case 2:
            var1 = "hid";
            break;
         case 3:
            var1 = "halogen";
            break;
         case 4:
            var1 = "incandescent";
            break;
         case 5:
            var1 = "analog converter";
            break;
         case 6:
            var1 = "LED";
            break;
         case 7:
            var1 = "relay";
            break;
         case 8:
            var1 = "colour control";
            break;
         case 100:
            var1 = "sensor";
            break;
         case 120:
            var1 = "sensor";
            break;
         case 128:
            var1 = "sensor";
            break;
         case 254:
            var1 = "sensor";
            break;
         default:
            var1 = "unknown";
      }

      return var1;
   }

   private BDaliDeviceType(int var1) {
      super(var1);
   }

   static {
      TYPE = Sys.loadType(BDaliDeviceType.class);
      DEFAULT = ballast;
   }
}
