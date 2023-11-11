package uk.co.controlnetworksolutions.elitedali2.dali.ui;

import com.tridium.workbench.transfer.TransferUtil;
import java.text.NumberFormat;
import javax.baja.driver.BDevice;
import javax.baja.driver.ui.device.BDeviceManager;
import javax.baja.driver.ui.device.DeviceController;
import javax.baja.driver.ui.device.DeviceExtsColumn;
import javax.baja.driver.ui.device.DeviceModel;
import javax.baja.gx.BImage;
import javax.baja.job.BJob;
import javax.baja.naming.BOrd;
import javax.baja.space.Mark;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusString;
import javax.baja.sys.BComponent;
import javax.baja.sys.BIcon;
import javax.baja.sys.BObject;
import javax.baja.sys.BString;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.ui.BAbstractButton;
import javax.baja.ui.BDialog;
import javax.baja.ui.BWidget;
import javax.baja.ui.CommandArtifact;
import javax.baja.ui.pane.BEdgePane;
import javax.baja.util.BFolder;
import javax.baja.workbench.mgr.MgrColumn;
import javax.baja.workbench.mgr.MgrController;
import javax.baja.workbench.mgr.MgrEditRow;
import javax.baja.workbench.mgr.MgrLearn;
import javax.baja.workbench.mgr.MgrModel;
import javax.baja.workbench.mgr.MgrTypeInfo;
import javax.baja.workbench.mgr.folder.FolderModel;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BAnalogConverter;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BBallast;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BEmergencyLighting;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BHID;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BHalogen;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BIncandescent;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BLED;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BRelay;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType1;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType2;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType3;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BSensorType4;
import uk.co.controlnetworksolutions.elitedali2.dali.discovery.BDaliDiscoverDeviceEntry;
import uk.co.controlnetworksolutions.elitedali2.dali.discovery.BDiscoverDaliDevicesJob;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BDaliDeviceType;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliGroupFolder;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BVirtualGroupFolder;
import uk.co.controlnetworksolutions.elitedali2.utils.Lex;

public class BDaliNetworkManager extends BDeviceManager {
   public static final Type TYPE;
   private static BImage stationIcon;
   MgrColumn colName;
   MgrColumn colType;
   MgrColumn colDeviceExts;
   MgrColumn colAddress;
   MgrColumn colStatus;
   MgrColumn colEnabled;
   MgrColumn colHealth;
   MgrColumn colLocation;
   MgrColumn[] cols;

   public Type getType() {
      return TYPE;
   }

   public void doLoadValue(BObject var1, Context var2) {
      super.doLoadValue(var1, var2);
      BEdgePane var3 = (BEdgePane)this.getContent();
      BWidget[] var4 = var3.getBottom().getChildWidgets();
      BAbstractButton var5 = (BAbstractButton)var4[1].get("cmd0");
      var5.setText(Lex.getText("addNewDaliGroup.label"));
   }

   public BDaliNetwork getNetwork() {
      BObject var1 = this.getCurrentValue();
      if (var1 instanceof BDaliGroupFolder) {
         return (BDaliNetwork)((BDaliGroupFolder)var1).getNetwork();
      } else if (var1 instanceof BVirtualGroupFolder) {
         return (BDaliNetwork)((BVirtualGroupFolder)var1).getNetwork();
      } else {
         return var1 instanceof BDaliNetwork ? (BDaliNetwork)var1 : null;
      }
   }

   void updateLearnData() {
      BDiscoverDaliDevicesJob var1 = (BDiscoverDaliDevicesJob)this.getLearn().getJob();
      if (var1 != null) {
         MgrLearn var10000 = this.getLearn();
         BFolder var10001 = var1.getLearnedDevices();
         var10000.updateRoots(var10001.getChildren(BDaliDiscoverDeviceEntry.class));
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

   public BDaliNetworkManager() {
     this.colName = new MgrColumn.Name();
     this.colType = new MgrColumn.Type();
     this.colDeviceExts = new DeviceExtsColumn(new BGenericDaliDevice());
     this.colAddress = new CustomColumn(BGenericDaliDevice.address, 1);
     this.colStatus = new CustomColumn(BDevice.status);
     this.colEnabled = new CustomColumn(BDevice.enabled, 1);
     this.colHealth = new CustomColumn(BDevice.health);
     this.colLocation = new CustomColumn(BGenericDaliDevice.location, 1);
     this.cols = new MgrColumn[]{this.colName, this.colType, this.colDeviceExts, this.colAddress, this.colStatus, this.colEnabled, this.colHealth, this.colLocation};
   }

   static {
      TYPE = Sys.loadType(BDaliNetworkManager.class);
      stationIcon = BImage.make("module://icons/x16/device.png");
   }

   class Model extends DeviceModel {
      protected MgrColumn[] makeColumns() {
         return BDaliNetworkManager.this.cols;
      }

      public Type getFolderType() {
         return BDaliGroupFolder.TYPE;
      }

      Model(BDeviceManager var2) {
         super(var2);
      }
   }

   class Controller extends DeviceController {
      BDaliNetworkManager manager;
      MgrController.MgrCommand discover;
      MgrController.MgrCommand address;
      MgrController.MgrCommand sendcmd;

      protected MgrController.IMgrCommand[] makeCommands() {
         return append(super.makeCommands(), new MgrController.IMgrCommand[]{this.address});
      }

      public CommandArtifact doDiscover(Context var1) throws Exception {
         super.doDiscover(var1);
         BDaliNetwork var2 = BDaliNetworkManager.this.getNetwork();
         BOrd var3 = var2.submitDeviceDiscoverJob();

         try {
            BDaliNetworkManager.this.getLearn().setJob(var3);
            return null;
         } catch (NullPointerException var5) {
            throw new RuntimeException("DALI network discovery FAILED.");
         }
      }

      public CommandArtifact doNewFolder() throws Exception {
         boolean var1 = true;
         BDaliNetworkManager var2 = (BDaliNetworkManager)this.getManager();
         FolderModel var3 = (FolderModel)var2.getModel();
         BComponent var4 = (BComponent)var2.getCurrentValue();
         BComponent var5 = (BComponent)var3.getFolderType().getInstance();
         BDaliGroupFolder var6 = (BDaliGroupFolder)var5;

         while(var1) {
            String var7 = BDialog.prompt(this.manager.getParentWidget(), Lex.getText("daliGroupAddressDialog.title"), "1 - 16", 3);

            try {
               if (var7 == null) {
                  return null;
               }

               int var8 = Integer.parseInt(var7);
               if (var8 > 0 && var8 < 17) {
                  var1 = true;
                  Property[] var9 = var2.getNetwork().getDynamicPropertiesArray();
                  if (var9.length < 1) {
                     var1 = false;
                  }

                  for(int var10 = 0; var10 < var9.length; ++var10) {
                     if (var9[var10].getType() == BDaliGroupFolder.TYPE) {
                        BDaliGroupFolder var11 = (BDaliGroupFolder)var2.getNetwork().get(var9[var10]);
                        if (var11.getGroupAddress().getValue() == (double)var8) {
                           var1 = true;
                           BDialog.error(this.manager.getParentWidget(), Lex.getText("daliGroupAddressError.title"), Lex.getText("daliGroupAddressErrorExists.text"));
                           break;
                        }

                        var1 = false;
                     } else {
                        var1 = false;
                     }
                  }

                  if (!var1) {
                     var6.setGroupAddress(new BStatusNumeric((double)var8));
                     String var12 = "Group" + var8;
                     return TransferUtil.insert(var2, 16, new Mark(var5, var12), var4, (BComponent)null, (Context)null);
                  }
               } else {
                  BDialog.error(this.manager.getParentWidget(), Lex.getText("daliGroupAddressError.title"), Lex.getText("daliGroupAddressErrorInvalid.text"));
                  var1 = true;
               }
            } catch (NumberFormatException var14) {
               BDialog.error(this.manager.getParentWidget(), Lex.getText("daliGroupAddressError.title"), Lex.getText("daliGroupAddressErrorInvalid.text"));
            }
         }

         return null;
      }

      Controller(BDeviceManager var2) {
         super(var2);
         this.manager = (BDaliNetworkManager)var2;
         this.address = new Address(this.manager);
         this.address.setFlags(7);
         this.address.setEnabled(true);
         this.match.setEnabled(false);
         this.match.setFlags(0);
      }

      class Address extends MgrController.MgrCommand {
         public CommandArtifact doInvoke() throws Exception {
            BOrd var1 = null;
            BAddressingDialog var2 = new BAddressingDialog();
            var2.setReverseOption(Controller.this.manager.getNetwork().getReverseAddressingMode());
            int var3 = BDialog.open(Controller.this.manager, Lex.getText("addressingOptionsDialog.title"), var2.getPane(), 3);
            if (var3 == 1) {
               Controller.this.manager.getNetwork().setReverseAddressingMode(var2.getReverseOption());
               var3 = var2.getAddressingMode();
               switch (var3) {
                  case 1:
                     var1 = Controller.this.manager.getNetwork().submitAddressAllDevicesJob();
                     break;
                  case 2:
                     var1 = Controller.this.manager.getNetwork().submitAddressNewDevicesJob();
               }

               try {
                  BDaliNetworkManager.this.getLearn().setJob(var1);
               } catch (NullPointerException var5) {
                  throw new RuntimeException("DALI network addressing FAILED.");
               }
            }

            return null;
         }

         public BImage getIcon() {
            return BImage.make(BIcon.make(BOrd.make("module://icons/x16/deviceData.png")));
         }

         public Address(BWidget var2) {
            super(var2, Lex.getLexicon(), "addressDaliNetwork");
         }
      }
   }

   class Learn extends MgrLearn {
      protected MgrColumn[] makeColumns() {
         return new MgrColumn[]{new MgrColumn.Prop(BDaliDiscoverDeviceEntry.address), new MgrColumn.Prop(BDaliDiscoverDeviceEntry.deviceType), new MgrColumn.Prop(BDaliDiscoverDeviceEntry.status)};
      }

      public BImage getIcon(Object var1) {
         return BDaliNetworkManager.stationIcon;
      }

      public MgrTypeInfo[] toTypes(Object var1) {
         BDaliDiscoverDeviceEntry var2 = (BDaliDiscoverDeviceEntry)var1;
         if (var2.getDeviceType().equals(BDaliDeviceType.unknownDevice)) {
            return MgrTypeInfo.makeArray(BGenericDaliDevice.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.ballast)) {
            return new MgrTypeInfo[]{MgrTypeInfo.make(BBallast.TYPE), MgrTypeInfo.make(BLED.TYPE), MgrTypeInfo.make(BRelay.TYPE)};
         } else if (var2.getDeviceType().equals(BDaliDeviceType.emergencyLighting)) {
            return MgrTypeInfo.makeArray(BEmergencyLighting.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.hid)) {
            return MgrTypeInfo.makeArray(BHID.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.halogen)) {
            return MgrTypeInfo.makeArray(BHalogen.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.incandescent)) {
            return MgrTypeInfo.makeArray(BIncandescent.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.analogConverter)) {
            return MgrTypeInfo.makeArray(BAnalogConverter.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.led)) {
            return MgrTypeInfo.makeArray(BLED.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.relay)) {
            return MgrTypeInfo.makeArray(BRelay.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.colourControl)) {
            return new MgrTypeInfo[]{MgrTypeInfo.make(BLED.TYPE), MgrTypeInfo.make(BBallast.TYPE)};
         } else if (var2.getDeviceType().equals(BDaliDeviceType.sensorType1)) {
            return MgrTypeInfo.makeArray(BSensorType1.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.sensorType2)) {
            return MgrTypeInfo.makeArray(BSensorType2.TYPE);
         } else if (var2.getDeviceType().equals(BDaliDeviceType.sensorType3)) {
            return MgrTypeInfo.makeArray(BSensorType3.TYPE);
         } else {
            return var2.getDeviceType().equals(BDaliDeviceType.sensorType4) ? MgrTypeInfo.makeArray(BSensorType4.TYPE) : MgrTypeInfo.makeArray(BDaliNetworkManager.this.getNetwork().getDeviceType());
         }
      }

      public void toRow(Object var1, MgrEditRow var2) {
         BDaliDiscoverDeviceEntry var3 = (BDaliDiscoverDeviceEntry)var1;
         String var4 = var3.getAddress();
         var2.setCell(BDaliNetworkManager.this.colAddress, BString.make(var4));
         NumberFormat var6 = NumberFormat.getInstance();
         var6.setMinimumIntegerDigits(2);
         String var5 = "";

         try {
            var5 = var6.format((long)var6.parse(var4).intValue());
         } catch (Exception var8) {
         }

         if (var3.getDeviceType() == BDaliDeviceType.ballast) {
            var2.setDefaultName(Lex.getText("newDaliBallastName") + var5);
         } else if (var3.getDeviceType() == BDaliDeviceType.led) {
            var2.setDefaultName(Lex.getText("newDaliLedName") + var5);
         } else if (var3.getDeviceType() == BDaliDeviceType.relay) {
            var2.setDefaultName(Lex.getText("newDaliRelayName") + var5);
         } else if (var3.getDeviceType() == BDaliDeviceType.sensorType1) {
            var2.setDefaultName(Lex.getText("newDaliSensorName") + var5);
         } else if (var3.getDeviceType() == BDaliDeviceType.sensorType2) {
            var2.setDefaultName(Lex.getText("newDaliSensorName") + var5);
         } else if (var3.getDeviceType() == BDaliDeviceType.sensorType3) {
            var2.setDefaultName(Lex.getText("newDaliSensorName") + var5);
         } else if (var3.getDeviceType() == BDaliDeviceType.sensorType4) {
            var2.setDefaultName(Lex.getText("newDaliSensorName") + var5);
         } else if (var3.getDeviceType() == BDaliDeviceType.emergencyLighting) {
            var2.setDefaultName(Lex.getText("newDaliEmergencyLightingName") + var5);
         } else {
            var2.setDefaultName(Lex.getText("newDaliDeviceName") + var5);
         }

      }

      public boolean isExisting(Object var1, BComponent var2) {
         BDevice[] var3 = BDaliNetworkManager.this.getNetwork().getDevices();
         BDaliDiscoverDeviceEntry var4 = (BDaliDiscoverDeviceEntry)var1;
         boolean var6 = false;

         for(int var7 = 0; var7 < var3.length; ++var7) {
            BGenericDaliDevice var5 = (BGenericDaliDevice)var3[var7];
            if (var5.getAddress().equals(var4.getAddress())) {
               var6 = true;
               break;
            }
         }

         return var6;
      }

      public void jobComplete(BJob var1) {
         super.jobComplete(var1);
         if (var1 instanceof BDiscoverDaliDevicesJob) {
            BDaliNetworkManager.this.updateLearnData();
         }

      }

      Learn(BDeviceManager var2) {
         super(var2);
      }
   }

   class CustomColumn extends MgrColumn.Prop {
      private NumberFormat displayFormat;

      public String toDisplayString(Object var1, Object var2, Context var3) {
         if (var1 instanceof BGenericDaliDevice) {
            return this.getDisplayName().equals("Location") ? ((BStatusString)var2).getValue() : super.toDisplayString(var1, var2, var3);
         } else if (var1 instanceof BDaliGroupFolder) {
            BDaliGroupFolder var4 = (BDaliGroupFolder)var1;
            this.displayFormat.setMaximumFractionDigits(0);
            if (this.getDisplayName().equals("Address")) {
               return "G " + this.displayFormat.format(var4.getGroupAddress().getValue());
            } else if (this.getDisplayName().equals("Location")) {
               return var4.getLocation().getValue();
            } else {
               return this.getDisplayName().equals("Status") ? this.displayFormat.format(var4.getGroupBallastCount().getValue()) + " devices" : "";
            }
         } else {
            return super.toDisplayString(var1, var2, var3);
         }
      }

      CustomColumn(Property var2) {
         super(var2, 0);
         this.displayFormat = NumberFormat.getInstance();
      }

      CustomColumn(Property var2, int var3) {
         super(var2, var3);
         this.displayFormat = NumberFormat.getInstance();
      }
   }
}
