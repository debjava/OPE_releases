package com.dnb.agreement.key;


public class TestKeyGenerator 
{
	public static void main(String[] args) 
	{
		String kekPart1 = KeyGenerator.getKEKPart1();
		String kekPart2 = KeyGenerator.getKEKPart2();

		
		System.out.println("KEK Part1-------"+kekPart1);
		System.out.println("KEK Part1-------"+kekPart2);
		String kekString = KeyGenerator.generateKEK( kekPart1 , kekPart2 );
		System.out.println("KEK-----"+kekString);
		String kvvString = KeyGenerator.getKVV(kekString);
		System.out.println("KVV-----"+kvvString);
		/*
		 * For the first time, pass new kek as null
		 * For the second time, pass the new kek
		 */
		String newKekString = null;
		String aukString = KeyGenerator.getAUK( kekString, newKekString );
		System.out.println("AUK-------"+aukString);
		//Calculate AUK-AEBAE983D6406D07
	}
}
