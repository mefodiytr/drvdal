package uk.co.controlnetworksolutions.elitedali2.dim.point;

import com.tridium.basicdriver.point.BBasicProxyExt;
import com.tridium.basicdriver.util.BIBasicPollable;
import java.text.NumberFormat;
import javax.baja.driver.point.BReadWriteMode;
import javax.baja.driver.util.BPollFrequency;
import javax.baja.status.BStatusString;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import uk.co.controlnetworksolutions.elitedali2.dim.network.BDimDevice;
import uk.co.controlnetworksolutions.elitedali2.dim.ops.DimParamRead;
import uk.co.controlnetworksolutions.elitedali2.utils.StrUtil;

public class BDimProxyExt extends BBasicProxyExt implements BIBasicPollable {
   public static final Property pollFrequency;
   public static final Property identifier;
   public static final Type TYPE;

   public BPollFrequency getPollFrequency() {
      return (BPollFrequency)this.get(pollFrequency);
   }

   public void setPollFrequency(BPollFrequency var1) {
      this.set(pollFrequency, var1, (Context)null);
   }

   public int getIdentifier() {
      return this.getInt(identifier);
   }

   public void setIdentifier(int var1) {
      this.setInt(identifier, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   public Type getDeviceExtType() {
      return BDimPointDeviceExt.TYPE;
   }

   public BReadWriteMode getMode() {
      return BReadWriteMode.readonly;
   }

   public void poll() {
      if (!this.isUnoperational() && this.getDevice().isRunning()) {
         DimParamRead var1 = new DimParamRead((BDimDevice)this.getDevice(), this.getIdentifier());
         byte[] var2 = var1.getParamData();
         if (var2 != null) {
            String var3;
            switch (this.getIdentifier()) {
               case 1:
                  if (var2.length < 2) {
                     this.readFail("Invalid data");
                  } else {
                     NumberFormat var4 = NumberFormat.getInstance();
                     var4.setMinimumIntegerDigits(2);
                     var3 = "" + (255 & var2[0]) + '.' + var4.format((long)(255 & var2[1]));
                     this.readOk(new BStatusString(var3));
                  }
                  break;
               case 16:
                  if (var2.length < 6) {
                     this.readFail("Invalid data");
                  } else {
                     var3 = StrUtil.paddedStr(StrUtil.dataToHex(var2, ""), 12);
                     this.readOk(new BStatusString(var3));
                  }
                  break;
               case 17:
                  if (var2.length < 4) {
                     this.readFail("Invalid data");
                  } else {
                     var3 = "" + (255 & var2[3]) + '/' + (255 & var2[2]) + '/' + ((255 & var2[1]) + (255 & var2[0]) * 100);
                     this.readOk(new BStatusString(var3));
                  }
                  break;
               default:
                  this.readFail("Invalid DIM point identifier");
            }
         } else {
            this.readFail(var1.getFaultText());
         }

      }
   }

   static {
      pollFrequency = newProperty(0, BPollFrequency.slow, (BFacets)null);
      identifier = newProperty(0, 0, (BFacets)null);
      TYPE = Sys.loadType(BDimProxyExt.class);
   }
}
