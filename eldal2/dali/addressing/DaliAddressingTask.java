package uk.co.controlnetworksolutions.elitedali2.dali.addressing;

import javax.baja.naming.BOrd;

public interface DaliAddressingTask {
   BOrd addressAll();

   BOrd addressNew();

   void setTridonicClass(int var1);

   void setTenacityLevel(int var1);

   void cancel();

   boolean isRunning();

   boolean isSuccessful();

   int getDuration();

   int getDeviceCount();

   int getWarningCount();
}
