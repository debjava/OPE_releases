package com.ope.patu.key;

import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import org.bouncycastle.util.encoders.Hex;

import com.ope.patu.exception.MACException;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;
import com.ope.patu.util.ParityUtil;

public class MACGenerator 
{
	/**
	 * MAC Generator method
	 * 
	 * @param stringForAuthentication
	 *            String for which the MAC has to be calculated
	 * @param encryptionKey
	 *            Key which has to be used for encryption during MAC
	 * @return MAC as a String
	 * @throws MACException
	 */
	public String generateMAC(String stringForAuthentication,
			String encryptionKey) throws MACException {

		String returnMAC = null;
		try {
			returnMAC = generateMAC(stringForAuthentication, CommonUtil
					.hexToBytes(encryptionKey));
		} catch (NoSuchAlgorithmException nsae) {
			new MACException("No Such Algorithm Found");
			nsae.printStackTrace();
		}
		return returnMAC;
	}

	/**
	 * MAC Generation Implementation. For Hash Value computation, pass the
	 * actual data string without the line feeds and pass in the HSK for
	 * encryption. For Authentication value computation, pass the data as a
	 * String and pass the AUK key for encryption
	 * 
	 * @param stringForAuthentication
	 *            String for Authentication
	 * @param kSecretKey
	 *            SecretKey object
	 * @return MAC String
	 */
	public String generateMAC(String stringForAuthentication,
			SecretKey kSecretKey) {

		// Get the array of the partitioned string of each 8 characters.
		String[] strArray = CommonUtil.getPartitionString(
				stringForAuthentication, 8);

		// MAC Implementation logic
		return doMAC(strArray, kSecretKey);
	}

	/**
	 * This is the MAC calculation. The specification is given below.
	 * 
	 * T0 = DES( data 0 ) S0 = T0 XOR ( DATA 1 )
	 * 
	 * T1 = DES( S0 ) S1 = T1 XOR ( DATA 2 ) So on, Final Tn is the MAC values.
	 * 
	 * @param strs
	 *            indicating the array of data
	 * @param skey
	 *            of type {@link SecretKey}
	 */
	private static String doMAC(String[] strs, SecretKey skey) {
		byte[] macBytes = null;
		byte[] Ti = null;
		byte[] Si = null;
		for (int i = 0; i < strs.length; i++) {
			try {
				String firstdata = strs[i];
				String nextData = strs[i + 1];
				if (i == 0) {
					Si = Hex.decode(firstdata);
				}
				Ti = DESUtil.getEncryptedBytesWithIV(Si, skey);
				// System.out.println("T[" + i + "]::"+ new
				// String(Hex.encode(Ti)));
				Si = ParityUtil.xorArray(Ti, Hex.decode(nextData));
				// System.out.println("S[" + i + "]::"
				// + new String(Hex.encode(Si)));
			} catch (IndexOutOfBoundsException ibe) {
				macBytes = DESUtil.getEncryptedBytesWithIV(Si, skey);
				break;
			}
		}
		return CommonUtil.bytesToHex(macBytes);
	}

	/**
	 * MAC Generation Implementation. For Hash Value computation, pass the
	 * actual data string without the line feeds and pass in the HSK for
	 * encryption. For Authentication value computation, pass the data as a
	 * String and pass the AUK key for encryption
	 * 
	 * @param stringForAuthentication
	 *            Authentication String
	 * @param keyByteArray
	 *            Byte Array of the key
	 * @return MAC String
	 * @throws NoSuchAlgorithmException
	 */
	public String generateMAC(String stringForAuthentication,
			byte[] keyByteArray) throws NoSuchAlgorithmException 
			{
		SecretKey kSecretKey = DESUtil.getSecretKey(keyByteArray,"DES");
		return generateMAC(stringForAuthentication, kSecretKey);
			}

}
