package a4.p1;

import inputport.datacomm.duplex.object.DuplexObjectInputPortSelector;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import inputport.rpc.duplex.DuplexReceivedCallInvokerSelector;
import inputport.rpc.duplex.DuplexSentCallCompleterSelector;
import port.trace.objects.ObjectTraceUtility;
import port.trace.rpc.RPCTraceUtility;
import examples.gipc.counter.layers.AMultiLayerCounterClient;
import examples.gipc.counter.layers.AMultiLayerCounterServer;

public class ACustomCounterServer extends AMultiLayerCounterServer{
	public static void setFactories() {
		ACustomCounterClient.setFactories();

	}
	public static void main (String[] args) {		
//		BufferTraceUtility.setTracing();
//		RPCTraceUtility.setTracing();
		ObjectTraceUtility.setTracing();
		setFactories();
		init();
		setPort();
		addListeners();
		while (true) {
			ReceiveReturnMessage aReceivedMessage = gipcRegistry.getRPCServerPort().receive();
			if (aReceivedMessage == null) {
				break;
			}
			System.out.println("Source:" + aReceivedMessage.getSource()+" Message:"+ aReceivedMessage.getMessage());
		}
	}
	

}
