package a4;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import inputport.datacomm.ReceiveRegistrarAndNotifier;
import inputport.datacomm.duplex.DuplexClientInputPort;
import inputport.datacomm.duplex.object.ADuplexObjectClientInputPort;
import inputport.datacomm.duplex.object.DuplexObjectInputPortSelector;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import port.trace.objects.ReceivedMessageDequeued;

public class ACustomDuplexObjectClientInputPort extends ADuplexObjectClientInputPort {

	public static BlockingQueue<Object> queue;
	
	public ACustomDuplexObjectClientInputPort(
			DuplexClientInputPort<ByteBuffer> aBBClientInputPort) {
		super(aBBClientInputPort);
		queue = new ArrayBlockingQueue<>(1024);
	}
	
	protected ReceiveRegistrarAndNotifier<Object> createReceiveRegistrarAndNotifier() {
		return new ACustomClientReceiveNotifier();
	}
	
	@Override
	public void send(String aDestination, Object aMessage) {
		//System.out.println (aDestination + "<-" + aMessage);
		super.send(aDestination, aMessage);	
	}
	
	public ReceiveReturnMessage<Object> receive() {
		return receive(getSender());		

	}
	
	@Override
	public ReceiveReturnMessage<Object> receive(String aSource) {
		Object message=null;
		try {
			message = queue.take();
			ReceivedMessageDequeued.newCase(this, queue, message);
		} catch (InterruptedException e) {e.printStackTrace();}
		ReceiveReturnMessage<Object> retVal = new AReceiveReturnMessage(aSource, message);
		return retVal;
	}
}
