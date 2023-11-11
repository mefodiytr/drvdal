package uk.co.controlnetworksolutions.elitedali2.dali.point.proxy;

import javax.baja.job.BSimpleJob;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.util.BFolder;
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
import uk.co.controlnetworksolutions.elitedali2.dali.point.database.BallastPoints;
import uk.co.controlnetworksolutions.elitedali2.dali.point.database.EmergencyLightingPoints;
import uk.co.controlnetworksolutions.elitedali2.dali.point.database.TridonicSensorPoints;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;

public final class BDiscoverPointsJob extends BSimpleJob {
   public static final Property learnedDevices = newProperty(7, new BFolder(), (BFacets)null);
   public static final Type TYPE;
   private BGenericDaliDevice daliDevice;

   public final BFolder getLearnedDevices() {
      return (BFolder)this.get(learnedDevices);
   }

   public final void setLearnedDevices(BFolder var1) {
      this.set(learnedDevices, var1, (Context)null);
   }

   public final Type getType() {
      return TYPE;
   }

   public final void run(Context var1) throws Exception {
      this.progress(0);
      this.discoveryDevicePoints(this.daliDevice.getType());
      this.progress(100);
   }

   private final void discoveryDevicePoints(Type var1) {
      DaliPoint[] var2;
      if (var1 != BBallast.TYPE && var1 != BHID.TYPE && var1 != BHalogen.TYPE && var1 != BIncandescent.TYPE && var1 != BAnalogConverter.TYPE && var1 != BLED.TYPE && var1 != BRelay.TYPE) {
         if (var1 == BTridonicSensor.TYPE) {
            var2 = TridonicSensorPoints.proxyPointsList;
         } else if (var1 == BEmergencyLighting.TYPE) {
            var2 = EmergencyLightingPoints.proxyPointsList;
         } else {
            var2 = null;
         }
      } else {
         var2 = BallastPoints.proxyPointsList;
      }

      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            DaliPoint var4 = var2[var3];
            this.getLearnedDevices().add(var4.getIdentifier(), new BDaliDiscoverPointEntry(var4.getIdentifier(), var4.getPointType()));
         }
      }

   }

   public BDiscoverPointsJob() {
     this.daliDevice = null;
   }

   public BDiscoverPointsJob(BGenericDaliDevice var1) {
     this.daliDevice = null;
     
      this.daliDevice = var1;
   }

   static {
      TYPE = Sys.loadType(BDiscoverPointsJob.class);
   }
}
