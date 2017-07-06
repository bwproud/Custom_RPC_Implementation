package a4;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import inputport.datacomm.AReceiveRegistrarAndNotifier;
import inputport.datacomm.ReceiveRegistrarAndNotifier;
import inputport.datacomm.duplex.DuplexServerInputPort;
import inputport.datacomm.duplex.object.ADuplexObjectServerInputPort;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import port.trace.objects.ReceivedMessageDequeued;

public class ACustomDuplexObjectServerInputPort extends ADuplexObjectServerInputPort{
	public static Map<String, WrapperBlockingQueue<Object>> queues;
	public static BlockingQueue<Object> generalQueue;
	
	public ACustomDuplexObjectServerInputPort(
			DuplexServerInputPort<ByteBuffer> aBBDuplexServerInputPort) {
		super(aBBDuplexServerInputPort);
		queues= new HashMap<>();
		generalQueue = new ArrayBlockingQueue<>(1024);
	}
	
	protected ReceiveRegistrarAndNotifier<Object> createReceiveRegistrarAndNotifier() {
		return new ACustomReceiveNotifier();
	}

	public ReceiveReturnMessage<Object> receive() {
		String lastSender = getSender();
		if(lastSender == null){
			lastSender="*";
		}
		return receive(lastSender);		

	}
	
	public ReceiveReturnMessage<Object> getMessageFromQueue(BlockingQueue<Object> q, String aSource){
		Object message=null;
		try {
			message = q.take();
			ReceivedMessageDequeued.newCase(this, q, message);
		} catch (InterruptedException e) {e.printStackTrace();}
		ReceiveReturnMessage<Object> retVal = new AReceiveReturnMessage(aSource, message);
		return retVal;
	}
	
	public ReceiveReturnMessage<Object> receive(String aSource) {
		if(aSource == null || aSource.trim().equals("*")){
			return getMessageFromQueue(generalQueue, aSource);
		}
		
		WrapperBlockingQueue<Object> queue = queues.get(aSource);
		if(queue==null){
			queue = new WrapperBlockingQueue<>(1024);
			queues.put(aSource, queue);
		}
		
		return getMessageFromQueue(queue, aSource);
	}
}
