package a4.p1;

import util.trace.Tracer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import inputport.datacomm.AReceiveRegistrarAndNotifier;
import inputport.datacomm.ReceiveListener;
import port.trace.objects.ReceivedMessageQueued;

public class ACustomClientReceiveNotifier extends AReceiveRegistrarAndNotifier{
	@Override
	public void notifyPortReceive (String aSource, Object aMessage) {	
		super.notifyPortReceive(aSource, aMessage);
		ACustomDuplexObjectClientInputPort.queue.offer(aMessage);
		ReceivedMessageQueued.newCase(this, ACustomDuplexObjectClientInputPort.queue, aMessage);
	}
}
