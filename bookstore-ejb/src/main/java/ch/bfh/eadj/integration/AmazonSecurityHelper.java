package ch.bfh.eadj.integration;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
@Stateless
public class AmazonSecurityHelper {

	@Resource(name="AmazonAccessKey")
	private String ACCESS_KEY;

	@Resource(name="AmazonSecretKey")
	private String SECRET_KEY;

	@Inject
	@ConfigProperty(name = "amazon.security.access.key")
	private String TEST;

	private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String MAC_ALGORITHM = "HmacSHA256";

	public AmazonSecurityCredentials createCredentials(String searchType) throws NoSuchAlgorithmException, InvalidKeyException {

		DateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
		String timestamp = dateFormat.format(Calendar.getInstance().getTime());

		Mac mac = Mac.getInstance(MAC_ALGORITHM);
		SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), MAC_ALGORITHM);
		mac.init(key);
		byte[] data = mac.doFinal((searchType + timestamp).getBytes());
		String signature = DatatypeConverter.printBase64Binary(data);

		return new AmazonSecurityCredentials(ACCESS_KEY, timestamp, signature);
	}
}