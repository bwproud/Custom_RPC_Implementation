package a4.p1;

import inputport.datacomm.duplex.object.DuplexObjectInputPortSelector;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import inputport.rpc.duplex.DuplexReceivedCallInvokerSelector;
import inputport.rpc.duplex.DuplexSentCallCompleterSelector;
import inputport.rpc.duplex.SynchronousDuplexReceivedCallInvokerSelector;
import port.trace.objects.ObjectTraceUtility;
import port.trace.rpc.RPCTraceUtility;
import serialization.SerializerSelector;

import java.util.Random;

import examples.gipc.counter.layers.AMultiLayerCounterClient;

public class ACustomCounterClient extends AMultiLayerCounterClient{
	public static void setFactories() {
		DuplexReceivedCallInvokerSelector.setReceivedCallInvokerFactory(
				new ACustomDuplexReceivedCallInvokerFactory());
//		DuplexReceivedCallInvokerSelector.setReceivedCallInvokerFactory(
//				new AnAsynchronousCustomDuplexReceivedCallInvokerFactory());
		DuplexSentCallCompleterSelector.setDuplexSentCallCompleterFactory(
				new ACustomSentCallCompleterFactory());
		DuplexObjectInputPortSelector.setDuplexInputPortFactory(
				new ACustomDuplexObjectInputPortFactory());
		SerializerSelector.setSerializerFactory(new ACustomSerializerFactory());	
	}
	public static void main (String[] args) {
//		BufferTraceUtility.setTracing();
//		RPCTraceUtility.setTracing();
		ObjectTraceUtility.setTracing();
		setFactories();
		Random rand = new Random();
		init("Client "+rand.nextInt(5));
		setPort();
		sendByteBuffers();
		sendObjects();
		doOperations();	
		while (true) {
			ReceiveReturnMessage aReceivedMessage = gipcRegistry.getRPCClientPort().receive();
			if (aReceivedMessage == null) {
				break;
			}
			System.out.println("Received message:" + aReceivedMessage.getMessage() );
		}
	}
	

}
