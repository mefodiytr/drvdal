package uk.co.controlnetworksolutions.elitedali2.dali.network;

import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusString;
import javax.baja.sys.BComponent;
import javax.baja.sys.BInteger;
import javax.baja.sys.BValue;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BScenes;
import uk.co.controlnetworksolutions.elitedali2.dali.enums.BStandardCommands;

public interface BallastGroupFolder {
   String getName();

   BValue get(String var1);

   BComponent[] getChildComponents();

   BStatusString getGroupName();

   BStatusNumeric getGroupBallastCount();

   BStatusNumeric getScene1();

   BStatusNumeric getScene2();

   BStatusNumeric getScene3();

   BStatusNumeric getScene4();

   BStatusNumeric getScene5();

   BStatusNumeric getScene6();

   BStatusNumeric getScene7();

   BStatusNumeric getScene8();

   BStatusNumeric getScene9();

   BStatusNumeric getScene10();

   BStatusNumeric getScene11();

   BStatusNumeric getScene12();

   BStatusNumeric getScene13();

   BStatusNumeric getScene14();

   BStatusNumeric getScene15();

   BStatusNumeric getScene16();

   BStatusEnum getFadeRate();

   BStatusEnum getFadeTime();

   BStatusNumeric getMinLevel();

   BStatusNumeric getMaxLevel();

   BStatusNumeric getPowerOnLevel();

   BStatusNumeric getSystemFailureLevel();

   void doSendCommand(BStandardCommands var1);

   void doRecallScene(BScenes var1);

   void doSetLevel(BInteger var1);

   void doAllDevicesOff();

   void doAllDevicesOn();

   void doStartGroupIdentify();

   void doStopGroupIdentify();

   void doReprogram();

   void reconfigureGroup(boolean var1);

   void groupCommand(int var1);

   void groupDirectLevel(double var1);

   int getDaliGroupAddress();

   BDaliNetwork getDaliNetwork();

   boolean isVirtualGroup();

   boolean isGroupOk();
}
