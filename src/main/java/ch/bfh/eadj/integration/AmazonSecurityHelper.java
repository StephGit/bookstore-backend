package ch.bfh.eadj.integration;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The programm AmazonSecurityHelper computes the security parameters needed in requests of
 * Amazon's Product Advertising API.
 */
public class AmazonSecurityHelper {
    private static final String ASSOCIATE_TAG = "test";
	private static final String ACCESS_KEY = "AKIAJ3GAM4266O4LXRIA";
	private static final String SECRET_KEY = "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";
	private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String MAC_ALGORITHM = "HmacSHA256";

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
		if (args.length < 1) {
			System.out.println("Usage: java AmazonSecurityHelper <operation>");
			return;
		}

		DateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
		String timestamp = dateFormat.format(Calendar.getInstance().getTime());

		Mac mac = Mac.getInstance(MAC_ALGORITHM);
		SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), MAC_ALGORITHM);
		mac.init(key);
		byte[] data = mac.doFinal((args[0] + timestamp).getBytes());
		String signature = DatatypeConverter.printBase64Binary(data);

		System.out.println("AssociateTag:   " + ASSOCIATE_TAG);
		System.out.println("AWSAccessKeyId: " + ACCESS_KEY);
		System.out.println("Timestamp:      " + timestamp);
		System.out.println("Signature:      " + signature);
	}
}