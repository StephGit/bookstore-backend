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

    public String getAccessKey() {
        return accessKey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSignature() {
        return signature;
    }
}
