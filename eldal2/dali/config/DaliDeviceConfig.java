package uk.co.controlnetworksolutions.elitedali2.dali.config;

import javax.baja.log.Log;
import javax.baja.status.BStatus;
import javax.baja.status.BStatusNumeric;
import uk.co.controlnetworksolutions.elitedali2.dali.devices.BGenericDaliDevice;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BallastGroupFolder;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliWrite;
import uk.co.controlnetworksolutions.elitedali2.dali.utils.DaliArcPowerUtils;
import uk.co.controlnetworksolutions.elitedali2.utils.Report;

public class DaliDeviceConfig {
   private int configRetryMax;
   private int queryRetryMax;
   private int daliAddress;
   private int daliDeviceType;
   private BDaliNetwork daliNetwork;
   private BGenericDaliDevice daliDevice;
   private String deviceId;
   private Log log;

   public void setDaliDeviceType(int var1) {
      this.daliDeviceType = var1;
   }

   public void programGroups(int var1) {
      if (this.daliAddress >= 0 && this.daliAddress < 65) {
         this.assignGroupBlock(192, 96, 112, (byte)(var1 & 255));
         this.assignGroupBlock(193, 104, 120, (byte)(var1 >> 8 & 255));
      }

   }

   public void programScenes(BallastGroupFolder var1) {
      for(int var2 = 0; var2 < 16; ++var2) {
         BStatusNumeric var5 = (BStatusNumeric)var1.get("scene" + (var2 + 1));
         if (!var5.getStatus().isNull()) {
            double var3 = var5.getValue();
            this.programLevel(64 + var2, 176 + var2, var3);
         }
      }

   }

   public void programProperties(BallastGroupFolder var1) {
      this.programStatusLevel(43, 162, var1.getMinLevel());
      this.programStatusLevel(42, 161, var1.getMaxLevel());
      this.programStatusLevel(45, 163, var1.getPowerOnLevel());
      this.programStatusLevel(44, 164, var1.getSystemFailureLevel());
      this.programFadeEnums(var1);
   }

   public void programStatusLevel(int var1, int var2, BStatusNumeric var3) {
      if (!var3.getStatus().isNull()) {
         this.programLevel(var1, var2, var3.getValue());
      }

   }

   public boolean programLevel(int var1, int var2, double var3) {
      int var5 = 0;
      int var8 = -999;

      try {
         var5 = 255 & DaliArcPowerUtils.percentToDirectLevel(var3);

         for(int var6 = 0; var6 < this.configRetryMax; ++var6) {
            var8 = -9;

            for(int var7 = 0; var7 < this.queryRetryMax; ++var7) {
               var8 = this.cfgQuery(var2);
               if (var8 >= 0) {
                  break;
               }
            }

            if (var8 < 0) {
               this.devCfgFault("Config level query " + var2 + " failed for device '" + this.deviceId + '\'');
               return false;
            }

            if (var5 == var8) {
               break;
            }

            this.cfgStore(var1, var5);
         }
      } catch (Exception var10) {
         this.devCfgFault("Config level " + var1 + " exception for device '" + this.deviceId + '\'');
         var10.printStackTrace();
      }

      if (var5 != var8) {
         this.devCfgFault("Config level " + var1 + " failed for device '" + this.deviceId + '\'');
         return false;
      } else {
         return true;
      }
   }

   public boolean programFadeEnums(BallastGroupFolder var1) {
      boolean var2 = false;
      boolean var3 = false;
      if (var1.getFadeRate().getStatus().isNull() && var1.getFadeTime().getStatus().isNull()) {
         return true;
      } else {
         try {
            int var4 = 0;

            label67:
            while(var4 < this.configRetryMax) {
               int var6 = -9;
               int var5 = 0;

               while(true) {
                  if (var5 < this.queryRetryMax) {
                     var6 = this.cfgQuery(165);
                     if (var6 < 0) {
                        ++var5;
                        continue;
                     }
                  }

                  if (var6 < 0) {
                     this.devCfgFault("Config fade rate/time query failed for device '" + this.deviceId + '\'');
                     return false;
                  }

                  int var7;
                  if (!var1.getFadeRate().getStatus().isNull()) {
                     var7 = var1.getFadeRate().getValue().getOrdinal();
                     if (var7 != (var6 & 15)) {
                        this.cfgStore(47, var7);
                     } else {
                        var2 = true;
                     }
                  } else {
                     var2 = true;
                  }

                  if (!var1.getFadeTime().getStatus().isNull()) {
                     var7 = var1.getFadeTime().getValue().getOrdinal();
                     if (var7 != (var6 >> 4 & 15)) {
                        this.cfgStore(46, var7);
                     } else {
                        var3 = true;
                     }
                  } else {
                     var3 = true;
                  }

                  if (var2 && var3) {
                     break label67;
                  }

                  ++var4;
                  break;
               }
            }
         } catch (Exception var9) {
            this.devCfgFault("Config fade rate/time exception for device '" + this.deviceId + '\'');
            var9.printStackTrace();
         }

         if (var2 && var3) {
            return true;
         } else {
            this.devCfgFault("Config fade rate/time failed for device '" + this.deviceId + '\'');
            return false;
         }
      }
   }

   private final boolean assignGroupBlock(int var1, int var2, int var3, byte var4) {
      boolean var5 = false;

      try {
         for(int var6 = 0; var6 < this.configRetryMax; ++var6) {
            int var8 = -9;

            for(int var7 = 0; var7 < this.queryRetryMax; ++var7) {
               var8 = this.cfgQuery(var1);
               if (var8 >= 0) {
                  break;
               }
            }

            if (var8 < 0) {
               this.devCfgFault("Config group query failed for device '" + this.deviceId + '\'');
               return false;
            }

            if (var8 == var4) {
               var5 = true;
               break;
            }

            int var9 = 1;

            for(int var10 = 0; var10 < 8; ++var10) {
               if ((var4 & var9) == 0) {
                  if ((var8 & var9) != 0) {
                     this.cfgCommand(var3 + var10);
                  }
               } else if ((var8 & var9) == 0) {
                  this.cfgCommand(var2 + var10);
               }

               var9 <<= 1;
            }
         }
      } catch (Exception var12) {
         this.devCfgFault("Config group exception for device '" + this.deviceId + '\'');
         var12.printStackTrace();
      }

      if (!var5) {
         this.devCfgFault("Config group failed for device '" + this.deviceId + '\'');
         return false;
      } else {
         return true;
      }
   }

   public boolean configureValue(int var1, int var2, int var3) {
      int var8 = -1;

      try {
         for(int var6 = 0; var6 < this.configRetryMax; ++var6) {
            for(int var7 = 0; var7 < this.queryRetryMax; ++var7) {
               DaliQuery var4 = new DaliQuery(this.daliNetwork, this.daliAddress, var1);
               if (this.daliDeviceType >= 0) {
                  var4.setDaliDeviceType(this.daliDeviceType);
               }

               var4.confirmResult();
               var8 = var4.getResult();
               if (var8 >= 0) {
                  break;
               }
            }

            if (var8 < 0) {
               this.log.trace("Config value query " + var1 + " failed for device '" + this.deviceId + '\'');
               return false;
            }

            if (var3 == var8) {
               break;
            }

            DaliWrite var5 = new DaliWrite(this.daliNetwork, this.daliAddress, var2, var3);
            if (this.daliDeviceType >= 0) {
               var5.setDaliDeviceType(this.daliDeviceType);
            }

            var5.setSendRepeat(4);
            var5.setStoreRepeat(4);
            var5.execute();
         }
      } catch (Exception var10) {
         this.log.trace("Config value " + var2 + " exception for device '" + this.deviceId + '\'');
         var10.printStackTrace();
      }

      if (var3 != var8) {
         this.log.trace("Config value " + var2 + " failed for device '" + this.deviceId + '\'');
         return false;
      } else {
         return true;
      }
   }

   private final boolean cfgCommand(int var1) {
      DaliCommand var2;
      if (this.daliDevice == null) {
         var2 = new DaliCommand(this.daliNetwork, false, this.daliAddress, var1);
      } else {
         var2 = new DaliCommand(this.daliDevice, var1);
      }

      var2.setTransmitRepeatCount(1);
      var2.execute();
      return var2.success();
   }

   private final int cfgQuery(int var1) {
      DaliQuery var2;
      if (this.daliDevice == null) {
         var2 = new DaliQuery(this.daliNetwork, this.daliAddress, var1);
      } else {
         var2 = new DaliQuery(this.daliDevice, var1);
      }

      var2.confirmResult();
      return var2.getResult();
   }

   private final boolean cfgStore(int var1, int var2) {
      DaliWrite var3 = new DaliWrite(this.daliNetwork, this.daliAddress, var1, var2);
      var3.setSendRepeat(2);
      var3.setStoreRepeat(2);
      var3.execute();
      return var3.success();
   }

   public void devCfgFault(String var1) {
      this.log.error(var1);
      if (this.daliDevice != null) {
         this.daliDevice.setStatus(BStatus.fault);
      }

   }

   public void setConfigRetryMax(int var1) {
      this.configRetryMax = var1;
   }

   public int getConfigRetryMax() {
      return this.configRetryMax;
   }

   public void setQueryRetryMax(int var1) {
      this.queryRetryMax = var1;
   }

   public int getQueryRetryMax() {
      return this.queryRetryMax;
   }

   public DaliDeviceConfig(BDaliNetwork var1, int var2) {
     this.configRetryMax = 5;
     this.queryRetryMax = 5;
     this.daliAddress = -1;
     this.daliDeviceType = -1;
     this.daliNetwork = null;
     this.daliDevice = null;
     this.deviceId = null;
     this.log = Report.daliDevice;
     
      this.daliNetwork = var1;
      this.daliAddress = var2;
      this.deviceId = "address " + (this.daliAddress + 1);
      this.daliDevice = null;
   }

   public DaliDeviceConfig(BGenericDaliDevice var1) {
     this.configRetryMax = 5;
     this.queryRetryMax = 5;
     this.daliAddress = -1;
     this.daliDeviceType = -1;
     this.daliNetwork = null;
     this.daliDevice = null;
     this.deviceId = null;
     this.log = Report.daliDevice;
     
      this.daliDevice = var1;
      this.daliNetwork = this.daliDevice.getDaliNetwork();
      this.daliAddress = this.daliDevice.getDaliAddress();
      this.daliDeviceType = this.daliDevice.getDaliDeviceType().getOrdinal();
      this.deviceId = this.daliDevice.getName();
   }
}
