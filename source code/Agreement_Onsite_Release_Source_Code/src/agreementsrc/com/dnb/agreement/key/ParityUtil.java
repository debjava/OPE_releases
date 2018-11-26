package com.dnb.agreement.key;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;

public class ParityUtil 
{
	/**
	 * Checks parity of a byte.
	 *
	 * The right most bit used as placeholder to change the parity.
	 *
	 * 00000010 -> odd  parity
	 * 00000110 -> even parity
	 * 00001110 -> odd  parity
	 *   .
	 *   .
	 * 10001110 -> even parity
	 *
	 * @return true if the parity of byte b is odd, or false if the parity is even.
	 * @param b the byte to check.
	 */
	public static boolean isParrityOdd(byte b)
	{
		if (((
				(b >>> 0) ^
				(b >>> 1) ^ 
				(b >>> 2) ^ 
				(b >>> 3) ^ 
				(b >>> 4) ^ 
				(b >>> 5) ^ 
				(b >>> 6) ^ 
				(b >>> 7)
		) & 0x00000001 ) == 0x00000001)

		{
			return true;  //1
		} else {
			return false; //0    
		}
	}

	/**
	 * Sets a byte b to odd parity, if it has even parity.
	 * If the input byte b has odd parity the return value will be
	 * the same as the input value b.
	 * 
	 * <pre>
	 * 00000010 -> odd  parity
	 * 00000110 -> even parity 
	 * 00001110 -> odd  parity
	 *    .
	 *    .
	 * 10001110 -> even parity
	 * </pre>
	 *
	 * Even parity means that the number of all "1" bits are even.
	 * Odd parity means that the number of all "1" bits are odd.
	 * 
	 * The algorithm works in the following way:
	 * The right most bit (bit 8) is the parity bit.
	 * Every byte in the DES key must have always odd parity!
	 * If one byte in the key has not odd parity, the key is corrupted.
	 *
	 * That means:
	 * Is the sum of all "1" bits from bit 1 to bit 7 odd, set bit 8 to "0".
	 * IS the sum of all "1" bits from bit 1 to bit 7 even, set bit 8 to "1".
	 *
	 * @param b the byte where will be set to odd parity.
	 * @return the parity adjusted byte.
	 */   
	public static byte oddParity(byte b) {    
		return (byte)(
				(b & 0x000000fe) |     (           //set bit 8 to 0
						(
								(
										//xor bit 1 to 7
										(b >>> 1) ^ 
										(b >>> 2) ^ 
										(b >>> 3) ^ 
										(b >>> 4) ^ 
										(b >>> 5) ^ 
										(b >>> 6) ^ 
										(b >>> 7)

										//delete bit 1 to bit 7    
								) & 0x00000001

								//if the xor result "0" means even
								//set party bit to "1" means odd
								//
								//if the xor result "1" means odd
								//set parity bit to "0" means odd                                                
						) ^ 0x00000001 
				)
		);
	}
	
	public static boolean isParityOdd( byte[] bytes )
	{
		boolean flag = false;
		for( int i = 0 ; i < bytes.length ; i++ )
		{
			flag = isParrityOdd(bytes[i]);
			if( flag )
				break;
		}
		return flag;
	}

	public static byte[] getOddPariyByte( byte[] bytes )
	{
		byte[] oddBytes = new byte[8];
		for( int i = 0 ; i < bytes.length ; i++ )
		{
			boolean flag1= isParrityOdd(bytes[i]);

			if( flag1 )
			{
				oddBytes[i] = bytes[i];
			}
			else
			{
				oddBytes[i] = oddParity( bytes[i] );
			}

		}
		return oddBytes;
	}
	
	/**It does not provide the actual result.
	 * @param firstValue
	 * @param secondValue
	 * @return
	 * @deprecated
	 */
	public static byte[] getXOR( byte[] firstValue , byte[] secondValue )
	{
		byte[] xorBytes = null;
		BigInteger bi1 = new BigInteger(firstValue);
		BigInteger bi2 = new BigInteger(secondValue);
		BigInteger bi3 = bi1.xor(bi2);
		xorBytes = bi3.toByteArray();
		return xorBytes;
	}
	
	public static byte[] getLongBytes(long val) 
	{
		byte[] longByte = null;
		try 
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeLong(val);
			longByte = bos.toByteArray();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return longByte;
	}
	
	public static byte [] xorArray(byte [] a, byte [] b)
	{
		byte [] c = new byte[8];
		try
		{
			if( a.length != b.length )
				throw new Exception();
			else
			{
				for(int i =0;i< a.length;i++)
				   {
				      c[i] = (byte)(a[i] ^ b[i]);
				   }
			}
		}
		catch( Exception e )
		{
			System.out.println("---------------bytes are not of same length-----------");
			e.printStackTrace();
		}
	   if(b.length<a.length) throw new IllegalArgumentException("length of byte [] b must be >= byte [] a");
	   return c;
	}



}
