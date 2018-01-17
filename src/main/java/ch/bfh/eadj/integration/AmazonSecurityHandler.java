package ch.bfh.eadj.integration;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;

public class AmazonSecurityHandler implements SOAPHandler<SOAPMessageContext> {

    @Resource
    AmazonSecurityHelper amazonSecurityHelper;

    // change this to redirect output if desired
    private static PrintStream out = System.out;

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        try {
            logToSystemOut(smc);
        } catch (SOAPException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        try {
            logToSystemOut(smc);
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return true;
    }

    // nothing to clean up
    public void close(MessageContext messageContext) {
    }

    /*
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context
     * to see if this is an outgoing or incoming message.
     * Write a brief message to the print stream and
     * output the message. The writeTo() method can throw
     * SOAPException or IOException
     */
    private void logToSystemOut(SOAPMessageContext smc) throws SOAPException {
        Boolean outboundProperty = (Boolean)
                smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            out.println("\nOutbound message:");
            SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
            SOAPBody soapBody = envelope.getBody();
            String searchType = soapBody.getFirstChild().getNodeName();
            try {
                AmazonSecurityCredentials amazonSecurityCredentials = amazonSecurityHelper.createCredentials(searchType);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new SOAPException(e.toString());
            }

            Iterator it = soapBody.getChildElements();
            it.next();

            SOAPHeader header = envelope.addHeader();
            SOAPElement security =
                    header.addChildElement("AssociateTag", "ns", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
            header.addChildElement("AWSAccessKeyId", "ns", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");


        } else {
            out.println("\nInbound message:");
        }

        SOAPMessage message = smc.getMessage();
        try {
            message.writeTo(out);
            out.println("");   // just to add a newline
        } catch (Exception e) {
            out.println("Exception in handler: " + e);
        }
    }
}
