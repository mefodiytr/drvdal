package uk.co.controlnetworksolutions.elitedali2.dali.point.database;

import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliArcPowerLevelPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliBitFieldPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliBooleanPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliDurationPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliEmergencyFailurePoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliEmergencyFeaturesPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliEmergencyModePoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliEmergencyStatusPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliGroupSelectPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliLinearPercentPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliPoint;
import uk.co.controlnetworksolutions.elitedali2.dali.point.types.DaliStatusPoint;

public class EmergencyLightingPoints {
   public static DaliPoint[] proxyPointsList = new DaliPoint[]{new DaliStatusPoint("status"), new DaliBooleanPoint("lampFailure", 146), new DaliBooleanPoint("lampPowerOn", 147), new DaliLinearPercentPoint("batteryCharge", 241), new DaliDurationPoint("durationTestResult", 243, 120), new DaliDurationPoint("lampEmergencyTime", 244, 3600), new DaliDurationPoint("lampTotalTime", 245, 14400), new DaliArcPowerLevelPoint("emergencyLevel", 246, 233), new DaliArcPowerLevelPoint("emergencyMinLevel", 247), new DaliArcPowerLevelPoint("emergencyMaxLevel", 248), new DaliDurationPoint("ratedDuration", 249, 120), new DaliEmergencyModePoint("mode"), new DaliEmergencyFeaturesPoint("features"), new DaliEmergencyFailurePoint("failureStatus"), new DaliEmergencyStatusPoint("emergencyStatus")};
   public static DaliPoint[] legacyProxyPointsList = new DaliPoint[]{new DaliBitFieldPoint("restModeActive", 250, 0), new DaliBitFieldPoint("normalModeActive", 250, 1), new DaliBitFieldPoint("emergencyModeActive", 250, 2), new DaliBitFieldPoint("extendedEmergencyModeActive", 250, 3), new DaliBitFieldPoint("functionTestInProgress", 250, 4), new DaliBitFieldPoint("durationTestInProgress", 250, 5), new DaliBitFieldPoint("hardwiredInhibitIsActive", 250, 6), new DaliBitFieldPoint("hardwiredSwitchIsOn", 250, 7), new DaliBitFieldPoint("integralEmergencyControlGear", 251, 0), new DaliBitFieldPoint("maintainedControlGear", 251, 1), new DaliBitFieldPoint("switchedMaintainedControlGear", 251, 2), new DaliBitFieldPoint("autoTestCapability", 251, 3), new DaliBitFieldPoint("adjustableEmergencyLevel", 251, 4), new DaliBitFieldPoint("hardwiredInhibitSupported", 251, 5), new DaliBitFieldPoint("physicalSelectionSupported", 251, 6), new DaliBitFieldPoint("relightInRestModeSupported", 251, 7), new DaliBitFieldPoint("circuitFailure", 252, 0), new DaliBitFieldPoint("batteryDurationFailure", 252, 1), new DaliBitFieldPoint("batteryFailure", 252, 2), new DaliBitFieldPoint("emergencyLampFailure", 252, 3), new DaliBitFieldPoint("functionTestMaxDelayExceeded", 252, 4), new DaliBitFieldPoint("durationTestMaxDelayExceeded", 252, 5), new DaliBitFieldPoint("functionTestFailed", 252, 6), new DaliBitFieldPoint("durationTestFailed", 252, 7), new DaliBitFieldPoint("inhibitMode", 253, 0), new DaliBitFieldPoint("functionTestDoneAndResultValid", 253, 1), new DaliBitFieldPoint("durationTestDoneAndResultValid", 253, 2), new DaliBitFieldPoint("batteryFullyCharged", 253, 3), new DaliBitFieldPoint("functionTestRequestPending", 253, 4), new DaliBitFieldPoint("durationTestRequestPending", 253, 5), new DaliBitFieldPoint("identificationActive", 253, 6), new DaliBitFieldPoint("physicallySelected", 253, 7)};
   public static DaliPoint[] virtualPointsList = new DaliPoint[]{new DaliGroupSelectPoint("group1", 0), new DaliGroupSelectPoint("group2", 1), new DaliGroupSelectPoint("group3", 2), new DaliGroupSelectPoint("group4", 3), new DaliGroupSelectPoint("group5", 4), new DaliGroupSelectPoint("group6", 5), new DaliGroupSelectPoint("group7", 6), new DaliGroupSelectPoint("group8", 7), new DaliGroupSelectPoint("group9", 8), new DaliGroupSelectPoint("group10", 9), new DaliGroupSelectPoint("group11", 10), new DaliGroupSelectPoint("group12", 11), new DaliGroupSelectPoint("group13", 12), new DaliGroupSelectPoint("group14", 13), new DaliGroupSelectPoint("group15", 14), new DaliGroupSelectPoint("group16", 15)};
}
