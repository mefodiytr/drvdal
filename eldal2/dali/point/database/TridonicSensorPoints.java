package uk.co.controlnetworksolutions.elitedali2.dali.point.database;

import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliGroupSelectPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliTridonicSensorLuxPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliTridonicSensorOccupancyPoint;

public class TridonicSensorPoints {
   public static DaliPoint[] proxyPointsList = new DaliPoint[]{new DaliTridonicSensorLuxPoint("luxLevel"), new DaliTridonicSensorOccupancyPoint("occupancy")};
   public static DaliPoint[] virtualPointsList = new DaliPoint[]{new DaliGroupSelectPoint("group1", 0, true), new DaliGroupSelectPoint("group2", 1, true), new DaliGroupSelectPoint("group3", 2, true), new DaliGroupSelectPoint("group4", 3, true), new DaliGroupSelectPoint("group5", 4, true), new DaliGroupSelectPoint("group6", 5, true), new DaliGroupSelectPoint("group7", 6, true), new DaliGroupSelectPoint("group8", 7, true), new DaliGroupSelectPoint("group9", 8, true), new DaliGroupSelectPoint("group10", 9, true), new DaliGroupSelectPoint("group11", 10, true), new DaliGroupSelectPoint("group12", 11, true), new DaliGroupSelectPoint("group13", 12, true), new DaliGroupSelectPoint("group14", 13, true), new DaliGroupSelectPoint("group15", 14, true), new DaliGroupSelectPoint("group16", 15, true)};
}
