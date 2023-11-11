package uk.co.controlnetworksolutions.elitedali2.dali.addressing;

public interface DaliTaskReport {
   void logTrace(String var1);

   void logMessage(String var1);

   void logWarning(String var1);

   void logError(String var1);

   void reportProgress(int var1);

   void reportFailure(String var1);

   void reportSuccess();
}
