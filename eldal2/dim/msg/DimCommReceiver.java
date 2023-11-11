package uk.co.controlnetworksolutions.elitedali2.dim.msg;

import com.tridium.basicdriver.comm.CommReceiver;
import com.tridium.basicdriver.message.Message;
import com.tridium.basicdriver.message.ReceivedMessage;

public class DimCommReceiver extends CommReceiver {
   private DimMessageStateMachine stateMachine;

   public void initReceiveState(Message var1) {
      DimMessageRequest var2 = (DimMessageRequest)var1;
      this.stateMachine.reset();
      this.stateMachine.setAddress(var2.getDimAddress());
      this.stateMachine.setOpCode(var2.getDimOpcode());
      this.stateMachine.setCrcEnable(var2.getCrcEnable());
   }

   protected ReceivedMessage receive() throws Exception {
      return this.stateMachine.processInput(this.getInputStream().read());
   }

   public synchronized void resetReceiveBuffer() {
      this.stateMachine.reset();
   }

   public DimCommReceiver() {
     this.stateMachine = new DimMessageStateMachine();
   }
}
