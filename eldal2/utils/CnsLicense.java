package uk.co.controlnetworksolutions.elitedali2.utils;

import javax.baja.driver.BDevice;
import javax.baja.driver.BDeviceNetwork;
import javax.baja.license.Feature;
import javax.baja.license.FeatureNotLicensedException;
import javax.baja.license.LicenseException;
import javax.baja.status.BStatus;
import javax.baja.sys.Sys;

public class CnsLicense {
   private static final String CNS_LICENSE_VENDOR = "cns";
   private static final String CNS_LICENSE_DIM_FEATURE = "dim2";
   private static final String CNS_LICENSE_DIM_PER_PORT_LIMIT = "dimperport.limit";
   private static final String CNS_LICENSE_DIM_ERROR = "This elitedali software is not licensed to run on this host";
   private static final int DEFAULT_DIM_DEVICE_LIMIT = 2;

   public static synchronized int checkDimLicense(BDeviceNetwork var0) {
      Feature var1;
      try {
         var1 = Sys.getLicenseManager().checkFeature("cns", "dim2");
      } catch (FeatureNotLicensedException var3) {
         var1 = null;
      }

      if (var1 != null && var1.isExpired()) {
         var1 = null;
      }

      if (var1 == null) {
         Report.dim.error("This elitedali software is not licensed to run on this host");
         var0.configFatal("This elitedali software is not licensed to run on this host");
         var0.pingFail("This elitedali software is not licensed to run on this host");
         throw new LicenseException("This elitedali software is not licensed to run on this host");
      } else {
         return var1.geti("dimperport.limit", 2);
      }
   }

   public static synchronized boolean checkDeviceLicense(String var0, String var1, String var2, int var3, BDevice var4) {
      boolean var9 = false;

      Feature var5;
      try {
         var5 = Sys.getLicenseManager().getFeature("cns", var0);
      } catch (FeatureNotLicensedException var11) {
         var5 = null;
      }

      String var6 = "This CNS elitedali system is not licensed to use the " + var2 + " device";
      if (var5 == null) {
         var4.setStatus(BStatus.fault);
         var4.setFaultCause(var6);
         var4.configFatal(var2 + " device not licensed");
         var4.pingFail(var6);
         throw new LicenseException(var6);
      } else {
         String var7 = var5.get(var1);
         if (var7 == null) {
            var4.setStatus(BStatus.fault);
            var4.setFaultCause(var6);
            var4.configFatal(var2 + " device not licensed");
            var4.pingFail(var6);
            throw new LicenseException(var6);
         } else {
            if (var7.equalsIgnoreCase("none")) {
               var9 = true;
            } else {
               int var8 = var5.geti(var1, 0);
               if (var3 > var8) {
                  var6 = "CNS elitedali " + var2 + " device count (" + var3 + ") exceeds license limit (" + var8 + ')';
                  var4.setStatus(BStatus.fault);
                  var4.setFaultCause(var6);
                  var4.configFatal(var2 + " device license exceeded");
                  var4.pingFail(var6);
                  throw new LicenseException(var6);
               }

               var9 = true;
            }

            return var9;
         }
      }
   }
}
