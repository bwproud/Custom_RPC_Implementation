package a4;


import inputport.datacomm.NamingSender;
import inputport.datacomm.duplex.DuplexInputPort;
import inputport.rpc.RPCRegistry;
import inputport.rpc.duplex.ADuplexReceivedCallInvoker;
import inputport.rpc.duplex.AnRPCReturnValue;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;
import inputport.rpc.duplex.RPCReturnValue;

import java.io.Serializable;

import port.trace.rpc.ReturnMessageCreated;
import util.trace.Tracer;


public class ACustomReceivedCallInvoker extends ADuplexReceivedCallInvoker {
	
	public ACustomReceivedCallInvoker(LocalRemoteReferenceTranslator aRemoteHandler, DuplexInputPort<Object> aReplier, RPCRegistry theRPCRegistry) {
		super(aRemoteHandler, aReplier, theRPCRegistry);
	}
	protected void handleProcedureReturn(String aSender, Exception e) {
		replyPossiblyTransformedMethodReturnValue(aSender, null, null, e);
	}
	
}
