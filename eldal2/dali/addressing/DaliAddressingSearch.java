package uk.co.controlnetworksolutions.elitedali2.dali.addressing;

import javax.baja.sys.Clock;
import uk.co.controlnetworksolutions.elitedali2.dali.network.BDaliNetwork;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliOperation;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliSpecialCommand;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliSpecialQuery;
import uk.co.controlnetworksolutions.elitedali2.dali.ops.DaliTridonicCommand;

public class DaliAddressingSearch {
   private static final int SEARCH_RESULT_NO_RESPONSE = 0;
   private static final int SEARCH_RESULT_YES = 1;
   private static final int SEARCH_RESULT_MULTIPLE_RESPONSE = 2;
   private static final int SEARCH_RESULT_NO_DALI_POWER = 3;
   private static final int SEARCH_RESULT_DIM_DOWN = 4;
   private static final int SEARCH_RESULT_INVALID = 99;
   private BDaliNetwork network;
   private DaliTaskReport report;
   private DaliDeviceMap deviceMap;
   private boolean addressAllFlag;
   private boolean doTridonicAddressing;
   private int tridonicClass;
   private boolean cancelSearchFlag;
   private boolean addressingFaultFlag;
   private String addressingFaultText;
   private int addressingWarningCount;
   private boolean addressingSucceededFlag;
   private int totalSearchSteps;
   private int totalDevicesFound;
   private int searchDuration;
   private int lastSearchAddress_H;
   private int lastSearchAddress_M;
   private int lastSearchAddress_L;
   private int deviceSearchStepsMax;
   private int deviceCheckRepeatMax;
   private int searchLoopRetryMax;
   private int setSearchAddressRepeatCount;
   private int assignRetryMax;
   private int programAddressRepeatCount;
   private int searchStandardQueryRetryMax;
   private int searchQueryAddressRetryMax;
   private int deviceSearchStepMax;
   private int queryDelayValue;

   public void setTypeAll() {
      this.addressAllFlag = true;
   }

   public void setTypeNew() {
      this.addressAllFlag = false;
   }

   public void setTridonicClass(int var1) {
      this.tridonicClass = var1;
      this.doTridonicAddressing = true;
   }

   public void start() {
      this.cancelSearchFlag = false;
      this.addressingFaultFlag = false;
      this.addressingSucceededFlag = false;
      this.totalSearchSteps = 0;
      this.totalDevicesFound = 0;
      this.addressingWarningCount = 0;
      this.logMessage("Begin addressing search for " + this.network.getParent().getParent().getName() + '/' + this.network.getParent().getName());
      this.network.addressingJobStarted();
      this.reportProgress(1);
      long var3 = Clock.millis();
      short var1;
      if (this.addressAllFlag) {
         this.logMessage("Address all devices");
         this.resetAllDeviceAddresses();
         var1 = 0;
      } else {
         this.logMessage("Address new devices");
         var1 = 255;
      }

      this.reportProgress(2);
      if (this.checkForNoDevices(var1)) {
         this.network.addressingJobFinished();
         if (!this.addressingFaultFlag) {
            if (this.addressAllFlag) {
               this.addressingSucceededFlag = false;
               this.logError("Addressing search found no devices");
               this.reportFailure("Addressing search found no devices");
            } else {
               this.addressingSucceededFlag = true;
               this.logWarning("Addressing search found no devices");
               this.reportSuccess();
            }
         } else {
            this.addressingSucceededFlag = false;
            this.logError("Addressing search failed: " + this.addressingFaultText);
            this.reportFailure(this.addressingFaultText);
         }

      } else {
         this.reportProgress(3);
         this.searchInitialisation(var1);
         DaliOperation.setIgnorePowerState(true);
         this.reportProgress(4);
         this.deviceMap = new DaliDeviceMap();
         if (!this.addressAllFlag) {
            this.buildDaliDeviceMap();
         }

         this.reportProgress(5);

         for(int var2 = 0; var2 < this.searchLoopRetryMax; ++var2) {
            this.searchLoop();
            this.searchTermination();
            if (this.addressingFaultFlag || this.cancelSearchFlag || this.checkForNoDevices(255)) {
               break;
            }

            this.searchInitialisation(255);
         }

         this.searchTermination();
         if (!this.cancelSearchFlag && !this.doTridonicAddressing) {
            this.checkDaliDeviceMap();
         }

         DaliOperation.setIgnorePowerState(false);
         this.reportProgress(100);
         long var5 = Clock.millis();
         this.logMessage("Addressing search devices found = " + this.totalDevicesFound);
         this.logMessage("Addressing total search steps = " + this.totalSearchSteps);
         this.logMessage("Addressing device search steps max = " + this.deviceSearchStepsMax);
         this.logMessage("Addressing search time = " + (var5 - var3) + " milliseconds");
         this.searchDuration = (int)(var5 - var3);
         this.network.addressingJobFinished();
         if (this.cancelSearchFlag) {
            this.addressingSucceededFlag = false;
            this.logWarning("Addressing search cancelled");
            this.reportFailure("Addressing search cancelled");
         } else if (this.addressingFaultFlag) {
            this.addressingSucceededFlag = false;
            this.logError("Addressing search failed: " + this.addressingFaultText);
            this.reportFailure(this.addressingFaultText);
         } else {
            this.addressingSucceededFlag = true;
            this.logMessage("End addressing search");
            this.reportSuccess();
         }

      }
   }

   private final void searchLoop() {
      int var2 = 0;
      DaliSearchAddress var1 = new DaliSearchAddress();
      this.logTrace("Begin search loop");

      while(!this.addressingFaultFlag && !this.cancelSearchFlag) {
         this.logTrace("Search Address = " + var1.getAddress());
         this.setSearchAddress(var1.getAddress(), false);
         int var3 = this.searchCompare();
         if (var3 == 0) {
            var1.stepUp();
            if (var1.getLastStep() == 0) {
               this.logTrace("Addressing search stalled");
               break;
            }
         } else if (var3 != 1 && var3 != 2) {
            if (var3 == 3) {
               this.addressingFaultFlag = true;
               this.addressingFaultText = "No DALI power";
               this.logError("No DALI power");
               break;
            }

            if (var3 == 4) {
               this.addressingFaultFlag = true;
               this.addressingFaultText = "No response from DIM";
               this.logError("No response from DIM");
               break;
            }
         } else if (this.checkDeviceFound(var1)) {
            if (this.assignDevice(var1)) {
               ++this.totalDevicesFound;
               this.logTrace("DALI device found after " + var2 + " steps");
            }

            if (this.addressingFaultFlag) {
               break;
            }

            this.setSearchAddress(16777215, true);
            var3 = this.searchCompare();
            if (var3 == 0) {
               break;
            }

            var1.restartSearch();
            var2 = 0;
         } else {
            var1.stepDown();
            if (var1.getLastStep() == 0) {
               this.logTrace("Addressing search stalled");
               break;
            }
         }

         ++var2;
         ++this.totalSearchSteps;
         if (var2 >= this.deviceSearchStepMax) {
            this.logTrace("Addressing search timed-out after " + var2 + " steps");
            break;
         }
      }

      if (var2 > this.deviceSearchStepsMax) {
         this.deviceSearchStepsMax = var2;
      }

      this.logTrace("End search loop");
   }

   private final boolean assignDevice(DaliSearchAddress var1) {
      boolean var6 = false;
      int var2 = this.deviceMap.getFreeAddress();
      if (var2 >= 0) {
         byte var3 = (byte)((byte)var2 << 1 | 1);

         for(int var4 = 0; var4 < this.assignRetryMax; ++var4) {
            this.setSearchAddress(var1.getAddress(), true);
            this.logTrace("Assigning short address " + (var2 + 1) + " to device at search address " + var1.getAddress());
            this.daliSpecialCommand(183, var3, this.programAddressRepeatCount);
            int var7 = this.searchQueryAddress(var2);
            if (var7 == 1) {
               this.logMessage("Found DALI device, assigned address " + (var2 + 1));
               this.deviceMap.addDevice(new DaliDeviceConfig(var2, var1.getAddress()));
               var6 = true;
               this.reportProgress((var2 + 1) * 100 / 70 + 5);
               break;
            }

            if (var7 == 2) {
               ++this.addressingWarningCount;
               this.logWarning("Duplicate search address found [" + var1.getAddress() + ']');
               break;
            }

            if (var7 == 0) {
               ++this.addressingWarningCount;
               this.logWarning("No response when assigning device address");
            } else {
               if (var7 == 3) {
                  this.addressingFaultFlag = true;
                  this.addressingFaultText = "No DALI power";
                  this.logError("No DALI power");
                  break;
               }

               if (var7 == 4) {
                  this.addressingFaultFlag = true;
                  this.addressingFaultText = "No response from DIM";
                  this.logError("No response from DIM");
                  break;
               }

               ++this.addressingWarningCount;
               this.logWarning("Error occurred when assigning device address");
            }
         }
      } else {
         var6 = true;
         this.addressingFaultFlag = true;
         this.addressingFaultText = "More than 64 DALI devices found";
         this.logError("More than 64 DALI devices found");
      }

      if (!var6) {
         this.logTrace("Clearing DALI device short address");
         this.daliSpecialCommand(183, 255, this.programAddressRepeatCount);
      }

      this.daliSpecialCommand(171, 0, this.programAddressRepeatCount);
      return var6;
   }

   public void resetAllDeviceAddresses() {
      if (!this.doTridonicAddressing) {
         this.daliSpecialCommand(163, 255, 1);
         this.daliBroadcastCommand(128, 2);
         this.daliSpecialCommand(163, 255, 2);
         this.daliBroadcastCommand(128, 4);
      } else {
         this.daliSpecialCommand(163, 255, 4);
         DaliTridonicCommand var1 = new DaliTridonicCommand(this.network, true, this.tridonicClass, 63, 33);
         var1.setTransmitRepeatCount(3);
         var1.execute();
      }

   }

   private final void searchInitialisation(int var1) {
      if (!this.doTridonicAddressing) {
         this.daliSpecialCommand(165, var1, 4);
         this.daliSpecialCommand(167, 0, 4);
         this.daliSpecialCommand(161, 0, 2);
         this.daliSpecialCommand(165, var1, 2);
         this.daliSpecialCommand(167, 0, 2);
         this.daliSpecialCommand(161, 0, 2);
         this.daliSpecialCommand(165, var1, 2);
      } else {
         DaliTridonicCommand var2 = new DaliTridonicCommand(this.network, this.tridonicClass, 165, var1);
         var2.setTransmitRepeatCount(3);
         var2.execute();
      }

      this.daliSpecialCommand(167, 0, 2);
   }

   private final void searchTermination() {
      this.daliSpecialCommand(161, 0, 4);
   }

   private final int searchCompare() {
      return this.daliSpecialQueryVerify(169, 0, 255);
   }

   private final int searchQueryAddress(int var1) {
      byte var2 = (byte)((byte)var1 << 1 | 1);
      int var3 = this.daliSpecialQueryVerify(187, 0, var2);
      return var3;
   }

   private final boolean checkForNoDevices(int var1) {
      this.logTrace("Check if there are any devices left to address");

      for(int var2 = 0; var2 < this.deviceCheckRepeatMax; ++var2) {
         this.searchInitialisation(var1);
         this.setSearchAddress(16777215, true);
         int var3 = this.searchCompare();
         this.searchTermination();
         if (var3 == 3) {
            this.addressingFaultFlag = true;
            this.addressingFaultText = "No DALI power";
            this.logError("No DALI power");
            return true;
         }

         if (var3 == 4) {
            this.addressingFaultFlag = true;
            this.addressingFaultText = "No response from DIM";
            this.logError("No response from DIM");
            return true;
         }

         if (var3 != 0 || this.cancelSearchFlag) {
            return false;
         }
      }

      this.logTrace("No more devices left to address");
      return true;
   }

   private final void buildDaliDeviceMap() {
      if (this.deviceMap == null) {
         this.deviceMap = new DaliDeviceMap();
      }

      this.deviceMap.clearAll();

      for(int var1 = 0; var1 < 64; ++var1) {
         int var3 = this.daliQueryVerify(var1, 145, 255);
         boolean var2 = true;
         if (var3 == 0) {
            this.deviceMap.clearDevice(var1);
            var2 = false;
         } else if (var3 == 1) {
            int var4 = this.getDeviceRandomAddress(var1);
            if (var4 >= 0) {
               this.deviceMap.addDevice(new DaliDeviceConfig(var1, var4));
               var2 = false;
            } else {
               this.logError("Could not read random address from device " + (var1 + 1));
            }
         } else if (var3 == 2) {
            this.logTrace("Clearing duplicate addressed device [" + (var1 + 1) + ']');
         } else {
            this.logTrace("Clearing device address [" + (var1 + 1) + ']');
         }

         if (var2) {
            this.daliSpecialCommand(163, 255, 1);
            this.daliSpecialCommand(163, 255, 1);
            this.network.deviceCommand(var1, 128);
            this.network.deviceCommand(var1, 128);
            this.network.deviceCommand(var1, 128);
            this.network.deviceCommand(var1, 128);
         }

         if (this.cancelSearchFlag) {
            break;
         }
      }

   }

   private final boolean checkDaliDeviceMap() {
      this.logMessage("Checking DALI devices");
      boolean var2 = false;
      if (this.deviceMap == null) {
         this.logError("Missing device map");
         return false;
      } else {
         for(int var3 = 0; var3 < 64; ++var3) {
            DaliDeviceConfig var1 = this.deviceMap.getDevice(var3);
            if (var1 != null) {
               int var5 = this.daliQueryVerify(var3, 145, 255);
               if (var5 == 1) {
                  int var4 = this.getDeviceRandomAddress(var3);
                  if (var3 != var1.getShortAddress()) {
                     var2 = true;
                     ++this.addressingWarningCount;
                     this.logError("Device " + (var3 + 1) + " has incorrect short address (" + (var1.getShortAddress() + 1) + ')');
                  }

                  if (var4 != var1.getRandomAddress()) {
                     var2 = true;
                     ++this.addressingWarningCount;
                     this.logWarning("Device " + (var3 + 1) + " has incorrect random address");
                  }
               } else {
                  var2 = true;
                  ++this.addressingWarningCount;
                  this.logError("No response from device " + (var3 + 1));
               }
            }

            if (this.cancelSearchFlag) {
               return false;
            }
         }

         return var2;
      }
   }

   private final int getDeviceRandomAddress(int var1) {
      int var3 = 0;
      int var4 = -9;

      int var2;
      for(var2 = 0; var2 < this.searchQueryAddressRetryMax; ++var2) {
         var4 = this.daliQueryConfirm(var1, 194);
         if (var4 >= 0) {
            break;
         }
      }

      if (var4 < 0) {
         return -1;
      } else {
         var3 |= (255 & var4) << 16;
         var4 = -9;

         for(var2 = 0; var2 < this.searchQueryAddressRetryMax; ++var2) {
            var4 = this.daliQueryConfirm(var1, 195);
            if (var4 >= 0) {
               break;
            }
         }

         if (var4 < 0) {
            return -1;
         } else {
            var3 |= (255 & var4) << 8;
            var4 = -9;

            for(var2 = 0; var2 < this.searchQueryAddressRetryMax; ++var2) {
               var4 = this.daliQueryConfirm(var1, 196);
               if (var4 >= 0) {
                  break;
               }
            }

            if (var4 < 0) {
               return -1;
            } else {
               var3 |= 255 & var4;
               return var3;
            }
         }
      }
   }

   private final boolean checkDeviceFound(DaliSearchAddress var1) {
      boolean var2;
      if (var1.getLastStepAbs() <= 1) {
         var2 = true;
         int var3;
         if (var1.getAddress() > 0) {
            this.setSearchAddress(var1.getAddress() - 1, false);
            var3 = this.searchCompare();
            if (var3 == 0) {
               var2 = true;
            } else {
               var2 = false;
            }
         }

         if (var2) {
            this.setSearchAddress(var1.getAddress(), false);
            var3 = this.searchCompare();
            if (var3 != 1 && var3 != 2) {
               var2 = false;
            } else {
               var2 = true;
            }
         }
      } else {
         var2 = false;
      }

      return var2;
   }

   private final void setSearchAddress(int var1, boolean var2) {
      byte var4 = (byte)(var1 >> 16 & 255);
      byte var5 = (byte)(var1 >> 8 & 255);
      byte var6 = (byte)(var1 & 255);
      if (var4 != this.lastSearchAddress_H || var2) {
         this.daliSpecialCommand(177, var4, this.setSearchAddressRepeatCount);
         this.lastSearchAddress_H = var4;
      }

      if (var5 != this.lastSearchAddress_M || var2) {
         this.daliSpecialCommand(179, var5, this.setSearchAddressRepeatCount);
         this.lastSearchAddress_M = var5;
      }

      if (var6 != this.lastSearchAddress_L || var2) {
         this.daliSpecialCommand(181, var6, this.setSearchAddressRepeatCount);
         this.lastSearchAddress_L = var6;
      }

   }

   private final int daliQueryVerify(int var1, int var2, int var3) {
      byte var5 = 99;
      DaliQuery var4 = new DaliQuery(this.network, var1, var2);
      var4.setQueryDelay(this.queryDelayValue);
      var4.setRetryMax(this.searchStandardQueryRetryMax);
      var4.execute();
      if (!var4.dimResponded()) {
         var5 = 4;
      } else if (var4.verifyResult(var3)) {
         var5 = 1;
      } else if (var4.getDaliStatus() == 1) {
         var5 = 0;
      } else if (var4.getDaliStatus() == 3) {
         var5 = 2;
      } else if (var4.getDaliStatus() == 4) {
         var5 = 3;
      }

      return var5;
   }

   private final int daliQueryConfirm(int var1, int var2) {
      int var4 = -1;
      DaliQuery var3 = new DaliQuery(this.network, var1, var2);
      var3.setQueryDelay(this.queryDelayValue);
      var3.setRetryMax(this.searchStandardQueryRetryMax);
      if (!var3.confirmResult()) {
         if (var3.dimResponded()) {
            this.logTrace("Query retry timed-out [" + Integer.toHexString(var1) + '/' + Integer.toHexString(var2) + ']');
         }
      } else {
         var4 = var3.getResult();
      }

      return var4;
   }

   private final void daliBroadcastCommand(int var1, int var2) {
      if (var2 > 0) {
         DaliCommand var3;
         if (var2 < 5) {
            var3 = new DaliCommand(this.network, true, 63, var1);
            var3.setTransmitRepeatCount(var2 - 1);
            var3.execute();
         } else {
            for(int var4 = 0; var4 < var2; ++var4) {
               var3 = new DaliCommand(this.network, true, 63, var1);
               var3.execute();
            }
         }
      }

   }

   private final void daliSpecialCommand(int var1, int var2, int var3) {
      if (var3 > 0) {
         DaliSpecialCommand var4;
         if (var3 < 5) {
            var4 = new DaliSpecialCommand(this.network, var1, var2);
            var4.setTransmitRepeatCount(var3 - 1);
            var4.execute();
         } else {
            for(int var5 = 0; var5 < var3; ++var5) {
               var4 = new DaliSpecialCommand(this.network, var1, var2);
               var4.execute();
            }
         }
      }

   }

   private final int daliSpecialQueryVerify(int var1, int var2, int var3) {
      byte var5 = 99;
      DaliSpecialQuery var4 = new DaliSpecialQuery(this.network, var1, var2);
      var4.setQueryDelay(this.queryDelayValue);
      var4.setRetryMax(this.searchStandardQueryRetryMax);
      var4.execute();
      if (!var4.dimResponded()) {
         var5 = 4;
      } else if (var4.verifyResult(var3)) {
         var5 = 1;
      } else if (var4.getDaliStatus() == 1) {
         var5 = 0;
      } else if (var4.getDaliStatus() == 3) {
         var5 = 2;
      } else if (var4.getDaliStatus() == 4) {
         var5 = 3;
      }

      return var5;
   }

   public void cancel() {
      this.cancelSearchFlag = true;
   }

   public boolean isSuccessful() {
      return this.addressingSucceededFlag;
   }

   public int getDuration() {
      return this.searchDuration;
   }

   public int getDeviceCount() {
      return this.totalDevicesFound;
   }

   public int getWarningCount() {
      return this.addressingWarningCount;
   }

   public void setReporter(DaliTaskReport var1) {
      this.report = var1;
   }

   public void setQueryDelay(int var1) {
      this.queryDelayValue = var1;
   }

   public void setTenacityLevel(int var1) {
      if (var1 < 2) {
         this.deviceCheckRepeatMax = 4;
         this.searchLoopRetryMax = 10;
         this.setSearchAddressRepeatCount = 3;
         this.assignRetryMax = 5;
         this.programAddressRepeatCount = 3;
         this.searchQueryAddressRetryMax = 9;
         this.searchStandardQueryRetryMax = 9;
         this.deviceSearchStepMax = 40;
      } else if (var1 == 2) {
         this.deviceCheckRepeatMax = 7;
         this.searchLoopRetryMax = 15;
         this.setSearchAddressRepeatCount = 5;
         this.assignRetryMax = 10;
         this.programAddressRepeatCount = 5;
         this.searchQueryAddressRetryMax = 15;
         this.searchStandardQueryRetryMax = 15;
         this.deviceSearchStepMax = 70;
      } else if (var1 > 2) {
         this.deviceCheckRepeatMax = 15;
         this.searchLoopRetryMax = 30;
         this.assignRetryMax = 20;
         this.setSearchAddressRepeatCount = 9;
         this.programAddressRepeatCount = 8;
         this.searchQueryAddressRetryMax = 20;
         this.searchStandardQueryRetryMax = 20;
         this.deviceSearchStepMax = 100;
      }

   }

   private final void logTrace(String var1) {
      if (this.report != null) {
         this.report.logTrace(var1);
      }

   }

   private final void logMessage(String var1) {
      if (this.report != null) {
         this.report.logMessage(var1);
      }

   }

   private final void logWarning(String var1) {
      if (this.report != null) {
         this.report.logWarning(var1);
      }

   }

   private final void logError(String var1) {
      if (this.report != null) {
         this.report.logError(var1);
      }

   }

   private final void reportProgress(int var1) {
      if (this.report != null) {
         this.report.reportProgress(var1);
      }

   }

   private final void reportFailure(String var1) {
      if (this.report != null) {
         this.report.reportFailure(var1);
      }

   }

   private final void reportSuccess() {
      if (this.report != null) {
         this.report.reportSuccess();
      }

   }

   public DaliAddressingSearch(BDaliNetwork var1) {
     this.network = null;
     this.report = null;
     this.deviceMap = null;
     this.addressAllFlag = false;
     this.doTridonicAddressing = false;
     this.tridonicClass = -1;
     this.cancelSearchFlag = false;
     this.addressingFaultFlag = false;
     this.addressingFaultText = "Addressing search fault";
     this.addressingWarningCount = 0;
     this.addressingSucceededFlag = false;
     this.totalSearchSteps = 0;
     this.totalDevicesFound = 0;
     this.searchDuration = 0;
     this.lastSearchAddress_H = -1;
     this.lastSearchAddress_M = -1;
     this.lastSearchAddress_L = -1;
     this.deviceSearchStepsMax = 0;
     this.deviceCheckRepeatMax = 4;
     this.searchLoopRetryMax = 10;
     this.setSearchAddressRepeatCount = 3;
     this.assignRetryMax = 5;
     this.programAddressRepeatCount = 3;
     this.searchStandardQueryRetryMax = 9;
     this.searchQueryAddressRetryMax = 9;
     this.deviceSearchStepMax = 40;
     this.queryDelayValue = 30;
     
      this.network = var1;
   }

   private class DaliDeviceMap {
      private DaliDeviceConfig[] deviceArray = new DaliDeviceConfig[64];

      public void clearAll() {
         for(int var1 = 0; var1 < 64; ++var1) {
            this.deviceArray[var1] = null;
         }

      }

      public void clearDevice(int var1) {
         if (var1 >= 0 && var1 < 64) {
            this.deviceArray[var1] = null;
         }

      }

      public void addDevice(DaliDeviceConfig var1) {
         if (var1 != null && var1.getShortAddress() >= 0 && var1.getShortAddress() < 64) {
            this.deviceArray[var1.getShortAddress()] = var1;
         }

      }

      public int getFreeAddress() {
         int var2 = -1;
         int var1;
         if (!DaliAddressingSearch.this.network.getReverseAddressingMode()) {
            for(var1 = 0; var1 < 64; ++var1) {
               if (this.deviceArray[var1] == null) {
                  var2 = var1;
                  break;
               }
            }
         } else {
            for(var1 = 63; var1 >= 0; --var1) {
               if (this.deviceArray[var1] == null) {
                  var2 = var1;
                  break;
               }
            }
         }

         return var2;
      }

      public DaliDeviceConfig getDevice(int var1) {
         return this.deviceArray[var1];
      }

      public void dumpMap() {
         DaliAddressingSearch.this.logTrace("DALI Device Addressing Map:");

         for(int var1 = 0; var1 < 64; ++var1) {
            if (this.deviceArray[var1] == null) {
               DaliAddressingSearch.this.logTrace("Index = " + (var1 + 1) + " - NULL");
            } else {
               DaliAddressingSearch.this.logTrace("Index = " + (var1 + 1) + " - Short address = " + (this.deviceArray[var1].getShortAddress() + 1) + " - Random address = " + this.deviceArray[var1].getRandomAddress());
            }
         }

      }

      public DaliDeviceMap() {
         this.clearAll();
      }
   }

   private class DaliSearchAddress {
      private static final int SEARCH_ADDRESS_MIN = 0;
      private static final int SEARCH_ADDRESS_MAX = 16777215;
      private static final int SEARCH_ADDRESS_START = 8388608;
      int currentAddress;
      int lastAddress;
      int searchMax;
      int searchMin;

      public void newSearch() {
         this.currentAddress = 8388608;
         this.lastAddress = this.currentAddress;
         this.searchMax = 16777215;
         this.searchMin = 0;
      }

      public void restartSearch() {
         this.lastAddress = this.currentAddress;
         this.searchMax = 16777215;
         this.searchMin = this.currentAddress + 1;
         this.currentAddress = (this.searchMax - this.searchMin) / 2 + this.searchMin;
         this.lastAddress = this.currentAddress;
      }

      public int getAddress() {
         return this.currentAddress;
      }

      public int getLastStep() {
         return this.currentAddress - this.lastAddress;
      }

      public int getLastStepAbs() {
         return Math.abs(this.currentAddress - this.lastAddress);
      }

      public void stepUp() {
         this.searchMin = this.currentAddress;
         int var1 = (this.searchMax - this.currentAddress) / 2;
         if (var1 < 1) {
            var1 = 1;
         }

         this.lastAddress = this.currentAddress;
         this.currentAddress += var1;
         if (this.currentAddress > 16777215) {
            this.currentAddress = 16777215;
         }

      }

      public void stepDown() {
         this.searchMax = this.currentAddress;
         int var1 = (this.currentAddress - this.searchMin) / 2;
         this.lastAddress = this.currentAddress;
         this.currentAddress = var1 + this.searchMin;
         if (this.currentAddress > 16777215) {
            this.currentAddress = 16777215;
         }

      }

      public int getAddressMin() {
         return 0;
      }

      public int getAddressMax() {
         return 16777215;
      }

      public DaliSearchAddress() {
         this.newSearch();
      }
   }

   private class DaliDeviceConfig {
      private int shortAddress;
      private int randomAddress;

      public void setShortAddress(int var1) {
         this.shortAddress = var1;
      }

      public int getShortAddress() {
         return this.shortAddress;
      }

      public void setDeviceRandomAddress(int var1) {
         this.randomAddress = var1;
      }

      public int getRandomAddress() {
         return this.randomAddress;
      }

      public DaliDeviceConfig() {
        this.shortAddress = -1;
        this.randomAddress = -1;
        
         this.shortAddress = -1;
         this.randomAddress = -1;
      }

      public DaliDeviceConfig(int var2, int var3) {
        this.shortAddress = -1;
        this.randomAddress = -1;
        
         this.shortAddress = var2;
         this.randomAddress = var3;
      }
   }
}
