package a4;

import util.trace.Tracer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import inputport.datacomm.AReceiveRegistrarAndNotifier;
import inputport.datacomm.ReceiveListener;
import port.trace.objects.ReceivedMessageQueueCreated;
import port.trace.objects.ReceivedMessageQueued;

public class ACustomReceiveNotifier extends AReceiveRegistrarAndNotifier{
	@Override
	public void notifyPortReceive (String aSource, Object aMessage) {	
		super.notifyPortReceive(aSource, aMessage);
		WrapperBlockingQueue<Object> queue = ACustomDuplexObjectServerInputPort.queues.get(aSource);
		if(queue==null){
			queue= new WrapperBlockingQueue<>(1024);
			ReceivedMessageQueueCreated.newCase(this, queue, "queue has been created for"+aSource);
			ACustomDuplexObjectServerInputPort.queues.put(aSource, queue);
			ACustomDuplexObjectServerInputPort.generalQueue.offer(aMessage);
		}
		else if(queue.getCount()<1){
			ACustomDuplexObjectServerInputPort.generalQueue.offer(aMessage);
			ReceivedMessageQueued.newCase(this, ACustomDuplexObjectServerInputPort.generalQueue, aMessage);
		}else{
			queue.offer(aMessage);
			ReceivedMessageQueued.newCase(this, queue, aMessage);
			ACustomDuplexObjectServerInputPort.queues.put(aSource, queue);
		}
	}
}
