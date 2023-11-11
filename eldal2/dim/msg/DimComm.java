package uk.co.controlnetworksolutions.elitedali2.dim.msg;

import com.tridium.basicdriver.serial.SerialComm;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimNetwork;

public class DimComm extends SerialComm {
   public DimComm(BDimNetwork var1, DimCommReceiver var2) {
      super(var1, var2);
   }
}
