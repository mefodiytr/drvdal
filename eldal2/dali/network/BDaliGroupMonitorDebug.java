package uk.co.controlnetworksolutions.elitedali2.dali.network;

import javax.baja.sys.BComponent;
import javax.baja.sys.BFacets;
import javax.baja.sys.Context;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

public class BDaliGroupMonitorDebug extends BComponent {
   public static final Property showAllDeviceOk = newProperty(0, false, (BFacets)null);
   public static final Property showAllDeviceFaults = newProperty(0, false, (BFacets)null);
   public static final Property showAllLampFaults = newProperty(0, false, (BFacets)null);
   public static final Property showLevelCompareFaults = newProperty(0, false, (BFacets)null);
   public static final Property showAllLevelCompares = newProperty(0, false, (BFacets)null);
   public static final Property showLevelAverage = newProperty(0, false, (BFacets)null);
   public static final Property showScanDetails = newProperty(0, false, (BFacets)null);
   public static final Property showTaskMessages = newProperty(0, false, (BFacets)null);
   public static final Property hideAllDebugMessages = newProperty(0, false, (BFacets)null);
   public static final Property useGroupMonitorName = newProperty(0, false, (BFacets)null);
   public static final Type TYPE;

   public boolean getShowAllDeviceOk() {
      return this.getBoolean(showAllDeviceOk);
   }

   public void setShowAllDeviceOk(boolean var1) {
      this.setBoolean(showAllDeviceOk, var1, (Context)null);
   }

   public boolean getShowAllDeviceFaults() {
      return this.getBoolean(showAllDeviceFaults);
   }

   public void setShowAllDeviceFaults(boolean var1) {
      this.setBoolean(showAllDeviceFaults, var1, (Context)null);
   }

   public boolean getShowAllLampFaults() {
      return this.getBoolean(showAllLampFaults);
   }

   public void setShowAllLampFaults(boolean var1) {
      this.setBoolean(showAllLampFaults, var1, (Context)null);
   }

   public boolean getShowLevelCompareFaults() {
      return this.getBoolean(showLevelCompareFaults);
   }

   public void setShowLevelCompareFaults(boolean var1) {
      this.setBoolean(showLevelCompareFaults, var1, (Context)null);
   }

   public boolean getShowAllLevelCompares() {
      return this.getBoolean(showAllLevelCompares);
   }

   public void setShowAllLevelCompares(boolean var1) {
      this.setBoolean(showAllLevelCompares, var1, (Context)null);
   }

   public boolean getShowLevelAverage() {
      return this.getBoolean(showLevelAverage);
   }

   public void setShowLevelAverage(boolean var1) {
      this.setBoolean(showLevelAverage, var1, (Context)null);
   }

   public boolean getShowScanDetails() {
      return this.getBoolean(showScanDetails);
   }

   public void setShowScanDetails(boolean var1) {
      this.setBoolean(showScanDetails, var1, (Context)null);
   }

   public boolean getShowTaskMessages() {
      return this.getBoolean(showTaskMessages);
   }

   public void setShowTaskMessages(boolean var1) {
      this.setBoolean(showTaskMessages, var1, (Context)null);
   }

   public boolean getHideAllDebugMessages() {
      return this.getBoolean(hideAllDebugMessages);
   }

   public void setHideAllDebugMessages(boolean var1) {
      this.setBoolean(hideAllDebugMessages, var1, (Context)null);
   }

   public boolean getUseGroupMonitorName() {
      return this.getBoolean(useGroupMonitorName);
   }

   public void setUseGroupMonitorName(boolean var1) {
      this.setBoolean(useGroupMonitorName, var1, (Context)null);
   }

   public Type getType() {
      return TYPE;
   }

   static {
      TYPE = Sys.loadType(BDaliGroupMonitorDebug.class);
   }
}
