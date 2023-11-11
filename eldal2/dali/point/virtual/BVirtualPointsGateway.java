package uk.co.controlnetworksolutions.elitedali2.dali.point.virtual;

import javax.baja.sys.Property;
import javax.baja.sys.Slot;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.virtual.BVirtualComponent;
import javax.baja.virtual.BVirtualGateway;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BAnalogConverter;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BEmergencyLighting;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BHID;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BHalogen;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BIncandescent;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BLED;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BRelay;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BTridonicSensor;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.point.database.BallastPoints;
import uk.co.controlnetworksolutions.elitedali2.dali.point.database.EmergencyLightingPoints;
import uk.co.controlnetworksolutions.elitedali2.dali.point.database.GenericPoints;
import uk.co.controlnetworksolutions.elitedali2.dali.point.database.TridonicSensorPoints;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;

public final class BVirtualPointsGateway extends BVirtualGateway {
   public static final Type TYPE;
   private Type daliDeviceType;

   public final Type getType() {
      return TYPE;
   }

   protected final Property addVirtualSlot(BVirtualComponent var1, String var2) {
      return null;
   }

   public final void addPointsList(BVirtualComponent var1, DaliPoint[] var2) {
      if (var1 != null && var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            DaliPoint var4 = var2[var3];
            String var5 = var4.getIdentifier();
            BVirtualPoint var6 = var4.getVirtualPoint();
            if (var5 != null) {
               Slot var7 = var1.getSlot(var5);
               if (var7 == null) {
                  var1.add(var5, var6);
               }
            }
         }

      }
   }

   public final void loadVirtualSlots(BVirtualComponent var1) {
      if (var1.getType() == BVirtualComponent.TYPE) {
         this.addPointsList(var1, GenericPoints.virtualPointsList);
         if (this.daliDeviceType != BBallast.TYPE && this.daliDeviceType != BHID.TYPE && this.daliDeviceType != BHalogen.TYPE && this.daliDeviceType != BIncandescent.TYPE && this.daliDeviceType != BAnalogConverter.TYPE && this.daliDeviceType != BLED.TYPE && this.daliDeviceType != BRelay.TYPE) {
            if (this.daliDeviceType == BTridonicSensor.TYPE) {
               this.addPointsList(var1, TridonicSensorPoints.virtualPointsList);
            } else if (this.daliDeviceType == BEmergencyLighting.TYPE) {
               this.addPointsList(var1, EmergencyLightingPoints.virtualPointsList);
            }
         } else {
            this.addPointsList(var1, BallastPoints.virtualPointsList);
         }
      }

   }

   public final BDaliNetwork getDaliNetwork() {
      return ((BGenericDaliDevice)this.getParent()).getDaliNetwork();
   }

   public final void setDaliDeviceType(Type var1) {
      this.daliDeviceType = var1;
   }

   public BVirtualPointsGateway() {
     this.daliDeviceType = null;
   }

   static {
      TYPE = Sys.loadType(BVirtualPointsGateway.class);
   }
}
