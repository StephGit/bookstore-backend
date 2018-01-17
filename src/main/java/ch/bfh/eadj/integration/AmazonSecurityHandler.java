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

    public static final String AWS_ACCESS_KEY_ID = "AWSAccessKeyId";
    public static final String SIGNATURE = "Signature";
    public static final String TIMESTAMP = "Timestamp";
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
            Iterator it = soapBody.getChildElements();
            SOAPElement searchType = (SOAPElement) it.next();
            AmazonSecurityCredentials amazonSecurityCredentials;
            try {
                amazonSecurityCredentials = amazonSecurityHelper.createCredentials(searchType.getNodeName());
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new SOAPException(e.toString());
            }

            setElementValue(AWS_ACCESS_KEY_ID, searchType, amazonSecurityCredentials.getAccessKey());
            setElementValue(SIGNATURE, searchType, amazonSecurityCredentials.getSignature());
            setElementValue(TIMESTAMP, searchType, amazonSecurityCredentials.getTimestamp());

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

    private void setElementValue(String elementName, SOAPElement searchType, String value) {
        QName qName = new QName(elementName);
        Iterator it = searchType.getChildElements(qName);
        SOAPElement soapElement = (SOAPElement) it.next();
        soapElement.setValue(value);
    }
}
