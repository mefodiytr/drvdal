package uk.co.controlnetworksolutions.elitedali2.dali.point.database;

import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;

public class PointSearch {
   private static DaliPoint[][] allPoints;

   public static DaliPoint get(String var0) {
      if (allPoints != null) {
         for(int var1 = 0; var1 < allPoints.length; ++var1) {
            if (allPoints[var1] != null) {
               for(int var2 = 0; var2 < allPoints[var1].length; ++var2) {
                  if (var0.equals(allPoints[var1][var2].getIdentifier())) {
                     return allPoints[var1][var2];
                  }
               }
            }
         }
      }

      return null;
   }

   static {
      allPoints = new DaliPoint[][]{GenericPoints.proxyPointsList, GenericPoints.virtualPointsList, BallastPoints.proxyPointsList, BallastPoints.virtualPointsList, EmergencyLightingPoints.proxyPointsList, EmergencyLightingPoints.legacyProxyPointsList, EmergencyLightingPoints.virtualPointsList, TridonicSensorPoints.proxyPointsList, TridonicSensorPoints.virtualPointsList};
   }
}
