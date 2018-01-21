package ch.bfh.eadj.integration;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;

public class AmazonSecurityHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String AWS_ACCESS_KEY_ID = "AWSAccessKeyId";
    public static final String SIGNATURE = "Signature";
    public static final String TIMESTAMP = "Timestamp";

    @Inject
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        try {
            logToSystemOut(smc);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    private void logToSystemOut(SOAPMessageContext smc) throws SOAPException, IOException {
        Boolean outboundProperty = (Boolean)
                smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
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
            SOAPHeader header = envelope.getHeader();
            addHeaderElement(header, AWS_ACCESS_KEY_ID, amazonSecurityCredentials.getAccessKey());
            addHeaderElement(header, SIGNATURE, amazonSecurityCredentials.getSignature());
            addHeaderElement(header, TIMESTAMP, amazonSecurityCredentials.getTimestamp());
            out.println("\nOutbound message:");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            smc.getMessage().writeTo(stream);
            System.out.println(stream.toString());

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

    private void addHeaderElement(SOAPHeader header, String awsAccessKeyId, String accessKey) throws SOAPException {
        SOAPElement element = header.addChildElement(awsAccessKeyId, "aws", "http://security.amazonaws.com/doc/2007-01-01/");
        element.addTextNode(accessKey);
    }
}
