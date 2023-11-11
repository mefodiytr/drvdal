package uk.co.controlnetworksolutions.elitedali2.dali.ui;

import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.ui.BBorder;
import javax.baja.ui.BCheckBox;
import javax.baja.ui.BLabel;
import javax.baja.ui.BRadioButton;
import javax.baja.ui.BSeparator;
import javax.baja.ui.BWidget;
import javax.baja.ui.ToggleCommand;
import javax.baja.ui.ToggleCommandGroup;
import javax.baja.ui.enums.BHalign;
import javax.baja.ui.enums.BOrientation;
import javax.baja.ui.pane.BBorderPane;
import javax.baja.ui.pane.BGridPane;
import javax.baja.ui.pane.BPane;
import uk.co.controlnetworksolutions.elitedali2.utils.Lex;

public final class BAddressingDialog extends BWidget {
   public static final Type TYPE;
   public static final int MODE_ADDRESS_ALL = 1;
   public static final int MODE_ADDRESS_NEW = 2;
   private ToggleCommandGroup addressingOptionGroup;
   protected BGridPane dialogPane;
   private ToggleCommand optAddressAll;
   private ToggleCommand optAddressNew;
   private BCheckBox reverseOption;

   public final Type getType() {
      return TYPE;
   }

   public final int getAddressingMode() {
      if (this.optAddressAll.isSelected()) {
         return 1;
      } else {
         return this.optAddressNew.isSelected() ? 2 : -1;
      }
   }

   public final void setReverseOption(boolean var1) {
      this.reverseOption.setSelected(var1);
   }

   public final boolean getReverseOption() {
      return this.reverseOption.isSelected();
   }

   public final BPane getPane() {
      return this.dialogPane;
   }

   public final void setOptionGroup() {
      this.addressingOptionGroup.add(this.optAddressNew);
      this.addressingOptionGroup.add(this.optAddressAll);
   }

   public BAddressingDialog() {
      BGridPane var1 = new BGridPane(3);
      this.addressingOptionGroup = new ToggleCommandGroup();
      this.optAddressAll = new ToggleCommand(this, Lex.getLexicon(), "daliAddressAll");
      this.optAddressNew = new ToggleCommand(this, Lex.getLexicon(), "daliAddressNew");
      this.setOptionGroup();
      this.reverseOption = new BCheckBox(Lex.getText("daliAddressReverse.label"));
      var1.add((String)null, new BRadioButton(this.optAddressNew));
      var1.add((String)null, new BSeparator(BOrientation.horizontal));
      BLabel var3 = new BLabel(Lex.getText("daliAddressNew.text"));
      var3.setHalign(BHalign.left);
      var1.add((String)null, var3);
      var1.add((String)null, new BRadioButton(this.optAddressAll));
      var1.add((String)null, new BSeparator(BOrientation.horizontal));
      var3 = new BLabel(Lex.getText("daliAddressAll.text"));
      var3.setHalign(BHalign.left);
      var1.add((String)null, var3);
      BGridPane var2 = new BGridPane(3);
      var2.add((String)null, this.reverseOption);
      var2.add((String)null, new BSeparator(BOrientation.horizontal));
      var3 = new BLabel(Lex.getText("daliAddressReverse.text"));
      var3.setHalign(BHalign.left);
      var2.add((String)null, var3);
      this.dialogPane = new BGridPane(1);
      this.dialogPane.setColumnAlign(BHalign.center);
      this.dialogPane.add((String)null, new BBorderPane(var1, BBorder.solid));
      this.dialogPane.add((String)null, new BBorderPane(var2, BBorder.solid));
   }

   static {
      TYPE = Sys.loadType(BAddressingDialog.class);
   }
}
