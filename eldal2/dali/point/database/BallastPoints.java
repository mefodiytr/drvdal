package uk.co.controlnetworksolutions.elitedali2.dali.point.database;

import javax.baja.sys.BFacets;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliArcPowerLevelPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliBooleanPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliFadeRatePoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliFadeTimePoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliGroupSelectPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliStatusPoint;

public class BallastPoints {
   public static DaliPoint[] proxyPointsList = new DaliPoint[]{new DaliStatusPoint("status"), new DaliArcPowerLevelPoint("actualLevel", 160), new DaliBooleanPoint("lampFailure", 146, BFacets.makeBoolean("Fault", "Good")), new DaliBooleanPoint("lampPowerOn", 147, BFacets.makeBoolean("On", "Off"))};
   public static DaliPoint[] virtualPointsList = new DaliPoint[]{new DaliGroupSelectPoint("group1", 0), new DaliGroupSelectPoint("group2", 1), new DaliGroupSelectPoint("group3", 2), new DaliGroupSelectPoint("group4", 3), new DaliGroupSelectPoint("group5", 4), new DaliGroupSelectPoint("group6", 5), new DaliGroupSelectPoint("group7", 6), new DaliGroupSelectPoint("group8", 7), new DaliGroupSelectPoint("group9", 8), new DaliGroupSelectPoint("group10", 9), new DaliGroupSelectPoint("group11", 10), new DaliGroupSelectPoint("group12", 11), new DaliGroupSelectPoint("group13", 12), new DaliGroupSelectPoint("group14", 13), new DaliGroupSelectPoint("group15", 14), new DaliGroupSelectPoint("group16", 15), new DaliArcPowerLevelPoint("scene1", 176, 64), new DaliArcPowerLevelPoint("scene2", 177, 65), new DaliArcPowerLevelPoint("scene3", 178, 66), new DaliArcPowerLevelPoint("scene4", 179, 67), new DaliArcPowerLevelPoint("scene5", 180, 68), new DaliArcPowerLevelPoint("scene6", 181, 69), new DaliArcPowerLevelPoint("scene7", 182, 70), new DaliArcPowerLevelPoint("scene8", 183, 71), new DaliArcPowerLevelPoint("scene9", 184, 72), new DaliArcPowerLevelPoint("scene10", 185, 73), new DaliArcPowerLevelPoint("scene11", 186, 74), new DaliArcPowerLevelPoint("scene12", 187, 75), new DaliArcPowerLevelPoint("scene13", 188, 76), new DaliArcPowerLevelPoint("scene14", 189, 77), new DaliArcPowerLevelPoint("scene15", 190, 78), new DaliArcPowerLevelPoint("scene16", 191, 79), new DaliFadeRatePoint("fadeRate"), new DaliFadeTimePoint("fadeTime"), new DaliArcPowerLevelPoint("minLevel", 162, 43), new DaliArcPowerLevelPoint("maxLevel", 161, 42), new DaliArcPowerLevelPoint("powerOnLevel", 163, 45), new DaliArcPowerLevelPoint("systemFailureLevel", 164, 44), new DaliArcPowerLevelPoint("physicalMinLevel", 154)};
}
