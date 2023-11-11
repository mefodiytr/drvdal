package uk.co.controlnetworksolutions.elitedali2.dali.ops;

import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;

public class DaliTridonicQuery extends DaliQuery {
   protected void create() {
      this.prepare();
      this.bodyData = new byte[7];
      this.bodyData[0] = -128;
      this.bodyData[1] = 25;
      this.bodyData[2] = 8;
      this.bodyData[3] = 15;
      this.bodyData[4] = (byte)this.daliAddressByte;
      this.bodyData[5] = (byte)(128 | this.daliDataByte >> 1 & 255);
      this.bodyData[6] = (byte)(this.daliDataByte << 7 & 255);
      byte[] var10000;
      if (ignorePowerState) {
         var10000 = this.bodyData;
         var10000[0] = (byte)(var10000[0] | 64);
      }

      if (this.daliGroupAddressFlag) {
         var10000 = this.bodyData;
         var10000[4] = (byte)(var10000[4] | 128);
      }

      if (this.daliCommandFlag) {
         var10000 = this.bodyData;
         var10000[4] = (byte)(var10000[4] | 1);
      }

   }

   public DaliTridonicQuery(BDaliNetwork var1, int var2, int var3, int var4) {
      super(var1, var3, var4);
      this.bodyData[3] = (byte)var2;
   }
}
