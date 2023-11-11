package uk.co.controlnetworksolutions.elitedali2.dim.ui;

import javax.baja.driver.BDevice;
import javax.baja.driver.ui.device.BDeviceManager;
import javax.baja.driver.ui.device.DeviceController;
import javax.baja.driver.ui.device.DeviceExtsColumn;
import javax.baja.driver.ui.device.DeviceModel;
import javax.baja.gx.BImage;
import javax.baja.job.BJob;
import javax.baja.naming.BOrd;
import javax.baja.sys.BComponent;
import javax.baja.sys.BObject;
import javax.baja.sys.BString;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.ui.CommandArtifact;
import javax.baja.util.BFolder;
import javax.baja.workbench.mgr.MgrColumn;
import javax.baja.workbench.mgr.MgrController;
import javax.baja.workbench.mgr.MgrEditRow;
import javax.baja.workbench.mgr.MgrLearn;
import javax.baja.workbench.mgr.MgrModel;
import javax.baja.workbench.mgr.MgrTypeInfo;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDeviceFolder;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimLearnDeviceEntry;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimLearnDevicesJob;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimNetwork;
import uk.co.controlnetworksolutions.elitedali2.utils.Lex;

public class BDimDeviceManager extends BDeviceManager {
   public static final Type TYPE;
   private static BImage stationIcon;
   MgrColumn colName;
   MgrColumn colType;
   MgrColumn colDeviceExts;
   MgrColumn colAddress;
   MgrColumn colStatus;
   MgrColumn colEnabled;
   MgrColumn colHealth;
   MgrColumn[] cols;

   public Type getType() {
      return TYPE;
   }

   public BDimNetwork getNetwork() {
      BObject var1 = this.getCurrentValue();
      if (var1 instanceof BDimDeviceFolder) {
         return (BDimNetwork)((BDimDeviceFolder)var1).getNetwork();
      } else {
         return var1 instanceof BDimNetwork ? (BDimNetwork)var1 : null;
      }
   }

   public void doLoadValue(BObject var1, Context var2) {
      super.doLoadValue(var1, var2);
      this.updateLearnData();
   }

   void updateLearnData() {
      BDimLearnDevicesJob var1 = (BDimLearnDevicesJob)this.getLearn().getJob();
      if (var1 != null) {
         MgrLearn var10000 = this.getLearn();
         BFolder var10001 = var1.getLearnedDevices();
         var10000.updateRoots(var10001.getChildren(BDimLearnDeviceEntry.class));
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

   public BDimDeviceManager() {
     this.colName = new MgrColumn.Name();
     this.colType = new MgrColumn.Type();
     this.colDeviceExts = new DeviceExtsColumn(new BDimDevice());
     this.colAddress = new MgrColumn.Prop(BDimDevice.address, 1);
     this.colStatus = new MgrColumn.Prop(BDevice.status);
     this.colEnabled = new MgrColumn.Prop(BDevice.enabled, 3);
     this.colHealth = new MgrColumn.Prop(BDevice.health, 0);
     this.cols = new MgrColumn[]{this.colName, this.colType, this.colDeviceExts, this.colAddress, this.colStatus, this.colEnabled, this.colHealth};
   }

   static {
      TYPE = Sys.loadType(BDimDeviceManager.class);
      stationIcon = BImage.make("module://icons/x16/device.png");
   }

   class Model extends DeviceModel {
      protected MgrColumn[] makeColumns() {
         return BDimDeviceManager.this.cols;
      }

      Model(BDeviceManager var2) {
         super(var2);
      }
   }

   class Controller extends DeviceController {
      private BDimDeviceManager manager;
      MgrController.MgrCommand upload;

      public CommandArtifact doDiscover(Context var1) throws Exception {
         super.doDiscover(var1);
         BOrd var2 = BDimDeviceManager.this.getNetwork().submitDimDiscoveryJob();
         BDimDeviceManager.this.getLearn().setJob(var2);
         return null;
      }

      Controller(BDeviceManager var2) {
         super(var2);
         this.manager = null;
         this.upload = null;
         
         this.manager = (BDimDeviceManager)var2;
      }
   }

   class Learn extends MgrLearn {
      protected MgrColumn[] makeColumns() {
         return new MgrColumn[]{new MgrColumn.Prop(BDimLearnDeviceEntry.address)};
      }

      public BImage getIcon(Object var1) {
         return BDimDeviceManager.stationIcon;
      }

      public MgrTypeInfo[] toTypes(Object var1) {
         return MgrTypeInfo.makeArray(BDimDeviceManager.this.getNetwork().getDeviceType());
      }

      public void toRow(Object var1, MgrEditRow var2) {
         BDimLearnDeviceEntry var3 = (BDimLearnDeviceEntry)var1;
         var2.setCell(BDimDeviceManager.this.colAddress, BString.make(Integer.toString(var3.getAddress())));
         var2.setDefaultName(Lex.getText("newDimDeviceName") + '1');
      }

      public boolean isExisting(Object var1, BComponent var2) {
         BDimLearnDeviceEntry var3 = (BDimLearnDeviceEntry)var1;
         BDimDevice var4 = (BDimDevice)var2;
         return var4.getAddress().equals(String.valueOf(var3.getAddress()));
      }

      public void jobComplete(BJob var1) {
         super.jobComplete(var1);
         if (var1 instanceof BDimLearnDevicesJob) {
            BDimDeviceManager.this.updateLearnData();
         }

      }

      Learn(BDeviceManager var2) {
         super(var2);
      }
   }
}
