package uk.co.controlnetworksolutions.elitedali2.dali.ops;

import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;

public class DaliCommand extends DaliOperation {
   public boolean execute() {
      if (this.daliDeviceType >= 0 && this.daliDataByte >= 224 && this.daliDataByte <= 255) {
         if (this.transmitRepeatCount > 0) {
            int var2 = this.transmitRepeatCount + 1;
            this.transmitRepeatCount = 0;

            for(int var3 = 0; var3 < var2; ++var3) {
               DaliSpecialCommand var1 = new DaliSpecialCommand(this.daliNetwork, 193, this.daliDeviceType);
               if (!var1.execute()) {
                  return false;
               }

               if (!super.execute()) {
                  return false;
               }

               if (!super.execute()) {
                  return false;
               }
            }
         }
      } else if (!super.execute()) {
         return false;
      }

      return true;
   }

   public DaliCommand(BDaliNetwork var1, boolean var2, int var3, int var4) {
      super(var1, var2, true, var3, var4, 0);
   }

   public DaliCommand(BGenericDaliDevice var1, int var2) {
      super(var1, true, var2, 0);
   }
}
