/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package demo.hw.server;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.hello_world_soap12_http.Greeter;
import org.apache.hello_world_soap12_http.PingMeFault;
import org.apache.hello_world_soap12_http.types.FaultDetail;

import javax.activation.DataHandler;

@javax.jws.WebService(portName = "SoapPort", 
serviceName = "SOAPService", 
targetNamespace = "http://apache.org/hello_world_soap12_http", 
endpointInterface = "org.apache.hello_world_soap12_http.Greeter")
@javax.xml.ws.BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")     
@org.apache.cxf.annotations.SchemaValidation
public class GreeterImpl implements Greeter {

	@Resource
	WebServiceContext wsContext;

	private static final Logger LOG = 
			Logger.getLogger(GreeterImpl.class.getPackage().getName());

	public String greetMe(String me) {
		LOG.info("Executing operation greetMe");
		System.out.println("Executing operation greetMe");
		System.out.println("Message received: " + me + "\n");
		
		System.out.println("wsContext: " + wsContext + "\n");
		
		MessageContext msContext = wsContext.getMessageContext();
		Map<String,DataHandler> attachments = (Map<String,DataHandler>) msContext.get(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS);
		
		byte[] bytes = new byte[] {'a', 'b', 'c'}; 
		attachments.put("attachment-1", new DataHandler(new ByteArrayDataSource(bytes, "application/octet-stream")));
				
		System.out.println("attached attachment-1");

		
		return "Hello " + me;
	}

	public void greetMeOneWay(String me) {
		LOG.info("Executing operation greetMeOneWay");
		System.out.println("Executing operation greetMeOneWay\n");
		System.out.println("Hello there " + me);
	}

	public String sayHi() {
		LOG.info("Executing operation sayHi");
		System.out.println("Executing operation sayHi\n");
		return "Bonjour";
	}

	public void pingMe() throws PingMeFault {
		FaultDetail faultDetail = new FaultDetail();
		faultDetail.setMajor((short)2);
		faultDetail.setMinor((short)1);
		LOG.info("Executing operation pingMe, throwing PingMeFault exception");
		System.out.println("Executing operation pingMe, throwing PingMeFault exception\n");
		throw new PingMeFault("PingMeFault raised by server", faultDetail);
	}

}
