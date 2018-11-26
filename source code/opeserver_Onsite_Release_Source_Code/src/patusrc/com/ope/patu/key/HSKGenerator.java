package com.ope.patu.key;

import java.util.Random;

import javax.crypto.SecretKey;

import com.ope.patu.exception.HSKGeneratorException;
import com.ope.patu.util.CommonUtil;
import com.ope.patu.util.DESUtil;
import com.ope.patu.util.ParityUtil;

public class HSKGenerator 
{
	private byte[] hskBytes;
	private String hskString;
	
	public HSKGenerator()
	{
		super();
	}
	
	public void generateHSK( Object ... objects ) throws HSKGeneratorException
	{
		/*
		 * Generate the time stamp and it should be 64 bits or 8 bytes
		 */
		long DT = System.nanoTime();
		byte[] dtbytes = ParityUtil.getLongBytes(DT);//64 bits or 8 byte
		SecretKey kSecretKey = DESUtil.getsecretKey();
		/*
		 * Encrypt DT using DES algorithm by using K as key to DES Input for
		 * encryption of DT. Generate the Intermediate value I.
		 * 1. time stamp as bytes 
		 * 2. k as DES secret key
		 */
		byte[] I = DESUtil.getEncryptedBytes(dtbytes, kSecretKey);
		/*
		 * Generate a Random value for V as the seed value.
		 */
		Random vrand = new Random();
		long V = vrand.nextLong();
		byte[] Vbytes = ParityUtil.getLongBytes(V);
		byte[] xorBytes = ParityUtil.xorArray( I, Vbytes );
		/*
		 * Encrypt the XOR value using DES by using the k as Key for DES
		 */
		byte[] R = DESUtil.getEncryptedBytes(xorBytes, kSecretKey);
		/*
		 * Now set the parity of the bytes of R to odd.
		 */
		byte[] RoddBytes = ParityUtil.getOddPariyByte( R );
		hskString = CommonUtil.bytesToHex( RoddBytes );
		hskBytes = CommonUtil.hexToBytes( hskString );
		/*
		 * Calculate V = e(K)(V+R)
		 * Store this V for the next generation.
		 */
		Vbytes = DESUtil.getEncryptedBytes( ParityUtil.xorArray( Vbytes, R ) , kSecretKey );
	}

	public byte[] getHskBytes() throws HSKGeneratorException
	{
		if( hskBytes == null )
			throw new HSKGeneratorException();
		return hskBytes;
	}

	public String getHskString() throws HSKGeneratorException
	{
		if( hskString == null )
			throw new HSKGeneratorException();
		return hskString;
	}
}
