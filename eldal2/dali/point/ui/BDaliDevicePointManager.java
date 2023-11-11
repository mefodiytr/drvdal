package uk.co.controlnetworksolutions.elitedali2.dali.point.ui;

import javax.baja.control.BBooleanPoint;
import javax.baja.control.BControlPoint;
import javax.baja.control.BEnumPoint;
import javax.baja.control.BNumericPoint;
import javax.baja.driver.point.BProxyExt;
import javax.baja.driver.ui.point.BPointManager;
import javax.baja.driver.ui.point.PointController;
import javax.baja.driver.ui.point.PointModel;
import javax.baja.job.BJob;
import javax.baja.naming.BOrd;
import javax.baja.sys.BObject;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.ui.CommandArtifact;
import javax.baja.util.BFolder;
import javax.baja.util.BTypeSpec;
import javax.baja.workbench.mgr.MgrColumn;
import javax.baja.workbench.mgr.MgrController;
import javax.baja.workbench.mgr.MgrEditRow;
import javax.baja.workbench.mgr.MgrLearn;
import javax.baja.workbench.mgr.MgrModel;
import javax.baja.workbench.mgr.MgrTypeInfo;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliDevicePointExt;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliDiscoverPointEntry;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliEmergencyFailureProxyPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliEmergencyFeaturesProxyPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliEmergencyModeProxyPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliEmergencyStatusProxyPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliPointsFolder;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDaliStatusProxyPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BDiscoverPointsJob;
import uk.co.controlnetworksolutions.elitedali2.dali.point.proxy.BPointProxyExt;

public class BDaliDevicePointManager extends BPointManager {
   public static final Type TYPE;
   MgrColumn colPath;
   MgrColumn colName;
   MgrColumn colType;
   MgrColumn colToString;
   MgrColumn colEnabled;
   MgrColumn colFacets;
   MgrColumn colTuning;
   MgrColumn colDeviceFacets;
   MgrColumn colConversion;
   MgrColumn colFaultCause;
   MgrColumn colReadValue;
   MgrColumn colWriteValue;
   MgrColumn colAddress;
   MgrColumn[] cols;

   public Type getType() {
      return TYPE;
   }

   public BGenericDaliDevice getDevice() {
      BObject var1 = this.getCurrentValue();
      if (var1 instanceof BDaliDevicePointExt) {
         return (BGenericDaliDevice)((BDaliDevicePointExt)var1).getParent();
      } else {
         return var1 instanceof BDaliPointsFolder ? (BGenericDaliDevice)((BDaliPointsFolder)var1).getParent().getParent() : null;
      }
   }

   public void doLoadValue(BObject var1, Context var2) {
      super.doLoadValue(var1, var2);
      this.updateLearnData();
   }

   public void updateLearnData() {
      BDiscoverPointsJob var1 = (BDiscoverPointsJob)this.getLearn().getJob();
      if (var1 != null) {
         MgrLearn var10000 = this.getLearn();
         BFolder var10001 = var1.getLearnedDevices();
         var10000.updateRoots(var10001.getChildren(BDaliDiscoverPointEntry.class));
      }

   }

   protected MgrModel makeModel() {
      return new Model(this);
   }

   protected MgrController makeController() {
      return new Controller(this);
   }

   protected MgrLearn makeLearn() {
      return new Learn(this);
   }

   public BDaliDevicePointManager() {
     this.colPath = new MgrColumn.Path(2);
     this.colName = new MgrColumn.Name();
     this.colType = new MgrColumn.Type();
     this.colToString = new MgrColumn.ToString("Out", 0);
     this.colEnabled = new MgrColumn.PropPath(new Property[]{BControlPoint.proxyExt, BProxyExt.enabled}, 3);
     this.colFacets = new MgrColumn.PropPath(new Property[]{BControlPoint.facets}, 3);
     this.colTuning = new MgrColumn.PropPath(new Property[]{BControlPoint.proxyExt, BProxyExt.tuningPolicyName}, 1);
     this.colDeviceFacets = new MgrColumn.PropPath(new Property[]{BControlPoint.proxyExt, BProxyExt.deviceFacets}, 3);
     this.colConversion = new MgrColumn.PropPath(new Property[]{BControlPoint.proxyExt, BProxyExt.conversion}, 3);
     this.colFaultCause = new MgrColumn.PropPath(new Property[]{BControlPoint.proxyExt, BProxyExt.faultCause}, 2);
     this.colReadValue = new MgrColumn.PropPath(new Property[]{BControlPoint.proxyExt, BProxyExt.readValue}, 2);
     this.colWriteValue = new MgrColumn.PropPath(new Property[]{BControlPoint.proxyExt, BProxyExt.writeValue}, 2);
     this.colAddress = new MgrColumn.PropPath(new Property[]{BControlPoint.proxyExt}, 1);
     this.cols = new MgrColumn[]{this.colPath, this.colName, this.colType, this.colToString, this.colAddress, this.colEnabled, this.colFacets, this.colTuning, this.colDeviceFacets, this.colConversion, this.colFaultCause, this.colReadValue, this.colWriteValue};
   }

   static {
      TYPE = Sys.loadType(BDaliDevicePointManager.class);
   }

   class Model extends PointModel {
      protected MgrColumn[] makeColumns() {
         return BDaliDevicePointManager.this.cols;
      }

      Model(BPointManager var2) {
         super(var2);
      }
   }

   class Controller extends PointController {
      public CommandArtifact doDiscover(Context var1) {
         try {
            super.doDiscover(var1);
            BGenericDaliDevice var2 = BDaliDevicePointManager.this.getDevice();
            BOrd var3 = var2.getPoints().discoverPoints();
            BDaliDevicePointManager.this.getLearn().setJob(var3);
         } catch (Exception var5) {
            var5.printStackTrace();
         }

         return null;
      }

      public CommandArtifact doAdd(Context var1) throws Exception {
         if (BDaliDevicePointManager.this.getLearn() == null) {
            return null;
         } else {
            Object[] var2 = this.getLearnTable().getSelectedObjects();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               BDaliDiscoverPointEntry var4 = (BDaliDiscoverPointEntry)var2[var3];
               if (!this.isExisting(var4.getPointIdentifier())) {
                  this.CreateControlPoint(var4);
               }

               this.getManager().loadValue(BDaliDevicePointManager.this.getDevice().getPoints());
            }

            return null;
         }
      }

      public void CreateControlPoint(BDaliDiscoverPointEntry var1) {
         BTypeSpec var2 = BTypeSpec.make(var1.getPointType());
         if (var2.equals(BNumericPoint.TYPE.getTypeSpec())) {
            BNumericPoint var3 = new BNumericPoint();
            var3.setProxyExt(new BPointProxyExt(var1.getPointIdentifier()));
            BDaliDevicePointManager.this.getDevice().getPoints().add(var1.getPointIdentifier(), var3);
         } else if (var2.equals(BBooleanPoint.TYPE.getTypeSpec())) {
            BBooleanPoint var4 = new BBooleanPoint();
            var4.setProxyExt(new BPointProxyExt(var1.getPointIdentifier()));
            BDaliDevicePointManager.this.getDevice().getPoints().add(var1.getPointIdentifier(), var4);
         } else if (var2.equals(BEnumPoint.TYPE.getTypeSpec())) {
            BEnumPoint var5 = new BEnumPoint();
            var5.setProxyExt(new BPointProxyExt(var1.getPointIdentifier()));
            BDaliDevicePointManager.this.getDevice().getPoints().add(var1.getPointIdentifier(), var5);
         } else if (var2.equals(BDaliStatusProxyPoint.TYPE.getTypeSpec())) {
            BDaliStatusProxyPoint var6 = new BDaliStatusProxyPoint();
            var6.setProxyExt(new BPointProxyExt(var1.getPointIdentifier()));
            BDaliDevicePointManager.this.getDevice().getPoints().add(var1.getPointIdentifier(), var6);
         } else if (var2.equals(BDaliEmergencyModeProxyPoint.TYPE.getTypeSpec())) {
            BDaliEmergencyModeProxyPoint var7 = new BDaliEmergencyModeProxyPoint();
            var7.setProxyExt(new BPointProxyExt(var1.getPointIdentifier()));
            BDaliDevicePointManager.this.getDevice().getPoints().add(var1.getPointIdentifier(), var7);
         } else if (var2.equals(BDaliEmergencyFeaturesProxyPoint.TYPE.getTypeSpec())) {
            BDaliEmergencyFeaturesProxyPoint var8 = new BDaliEmergencyFeaturesProxyPoint();
            var8.setProxyExt(new BPointProxyExt(var1.getPointIdentifier()));
            BDaliDevicePointManager.this.getDevice().getPoints().add(var1.getPointIdentifier(), var8);
         } else if (var2.equals(BDaliEmergencyFailureProxyPoint.TYPE.getTypeSpec())) {
            BDaliEmergencyFailureProxyPoint var9 = new BDaliEmergencyFailureProxyPoint();
            var9.setProxyExt(new BPointProxyExt(var1.getPointIdentifier()));
            BDaliDevicePointManager.this.getDevice().getPoints().add(var1.getPointIdentifier(), var9);
         } else if (var2.equals(BDaliEmergencyStatusProxyPoint.TYPE.getTypeSpec())) {
            BDaliEmergencyStatusProxyPoint var10 = new BDaliEmergencyStatusProxyPoint();
            var10.setProxyExt(new BPointProxyExt(var1.getPointIdentifier()));
            BDaliDevicePointManager.this.getDevice().getPoints().add(var1.getPointIdentifier(), var10);
         }

      }

      public boolean isExisting(String var1) {
         return BDaliDevicePointManager.this.getDevice().getPoints().get(var1) != null;
      }

      Controller(BPointManager var2) {
         super(var2);
      }
   }

   class Learn extends MgrLearn {
      protected MgrColumn[] makeColumns() {
         return new MgrColumn[]{new MgrColumn.Prop(BDaliDiscoverPointEntry.pointIdentifier), new MgrColumn.Prop(BDaliDiscoverPointEntry.pointType)};
      }

      public void toRow(Object var1, MgrEditRow var2) {
      }

      public MgrTypeInfo[] toTypes(Object var1) {
         return null;
      }

      public void jobComplete(BJob var1) {
         super.jobComplete(var1);
         if (var1 instanceof BDiscoverPointsJob) {
            BDaliDevicePointManager.this.updateLearnData();
         }

      }

      Learn(BPointManager var2) {
         super(var2);
      }
   }
}
