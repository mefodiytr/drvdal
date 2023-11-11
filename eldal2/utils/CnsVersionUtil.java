package uk.co.controlnetworksolutions.elitedali2.utils;

import javax.baja.sys.BModule;
import javax.baja.sys.BObject;
import javax.baja.sys.Sys;
import javax.baja.util.Version;

public class CnsVersionUtil {
   public static String getModuleVersion(BObject var0) {
      BModule var1 = Sys.getModuleForClass(var0.getType().getTypeClass());
      if (var1 != null) {
         Version var2 = var1.getVendorVersion();
         if (var2 != null) {
            return var2.toString();
         }
      }

      return "unknown";
   }
}
