package a4;

import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import inputport.InputPort;
import inputport.datacomm.duplex.object.explicitreceive.ExplicitSourceReceive;
import inputport.rpc.ASerializableCall;
import inputport.rpc.duplex.ADuplexSentCallCompleter;
import inputport.rpc.duplex.AnRPCReturnValue;
import inputport.rpc.duplex.DuplexRPCInputPort;
import inputport.rpc.duplex.DuplexSentCallCompleter;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;
import inputport.rpc.duplex.RPCReturnValue;
import port.trace.rpc.ReceivedObjectTransformed;
import port.trace.rpc.RemoteCallReceivedReturnValue;
import port.trace.rpc.RemoteCallWaitingForReturnValue;

public class ACustomCallCompleter extends ADuplexSentCallCompleter	{
	DuplexRPCInputPort inputPort;
	public ACustomCallCompleter(DuplexRPCInputPort aPort, LocalRemoteReferenceTranslator aRemoteHandler) {
		super(aPort, aRemoteHandler);	
		inputPort = aPort;
	}	
	@Override
	protected void returnValueReceived(String aRemoteEndPoint, Object message) {
		//does nothing but override supermethod which fills bounded buffer
	}
	
	@Override
	public Object waitForReturnValue(String aRemoteEndPoint) {
		System.out.println("waitForReturnValue() -> receive()");
		ReceiveReturnMessage aReceivedMessage = inputPort.receive(aRemoteEndPoint);
		System.out.println(aRemoteEndPoint +  "-->"+((RPCReturnValue) aReceivedMessage.getMessage()).getReturnValue());
		while(!(aReceivedMessage.getMessage() instanceof RPCReturnValue)){
			aReceivedMessage = inputPort.receive(aRemoteEndPoint);
		}
		return ((RPCReturnValue) aReceivedMessage.getMessage()).getReturnValue();
	}
	
	public Object getReturnValueOfRemoteProcedureCall(String aRemoteEndPoint, Object aMessage) {
		RemoteCallWaitingForReturnValue.newCase(this);
		Object possiblyRemoteRetVal = waitForReturnValue(aRemoteEndPoint);
		Object returnValue = localRemoteReferenceTranslator.transformReceivedReference(possiblyRemoteRetVal);
		RemoteCallReceivedReturnValue.newCase(this, returnValue);
		ReceivedObjectTransformed.newCase(this, possiblyRemoteRetVal, returnValue);
		return  returnValue;
	}	
	
	
}
