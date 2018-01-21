package ch.bfh.eadj.integration;

public class AmazonSecurityCredentials {

    private String accessKey;
    private String timestamp;
    private String signature;

    public AmazonSecurityCredentials(String accessKey, String timestamp, String signature) {
        this.accessKey = accessKey;
        this.timestamp = timestamp;
        this.signature = signature;
    }

    protected String getAccessKey() {
        return accessKey;
    }

    protected String getTimestamp() {
        return timestamp;
    }

    protected String getSignature() {
        return signature;
    }
}
