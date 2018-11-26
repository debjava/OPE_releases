package com.ope.patu.unittest.keys;

import com.ope.patu.key.KeyGenerator;

public class TestKeyGenerator {

	public static void main(String[] args) 
	{
//		String kekPart1 = KeyGenerator.getKEKPart1();
//		String kekPart2 = KeyGenerator.getKEKPart2();
		
		String kekPart1 = "C24AD2B042CD01AC";
		String kekPart2 = "BAA4347E91BB776B";
//		
//		System.out.println("KEK Part1-------"+kekPart1);
//		System.out.println("KEK Part1-------"+kekPart2);
		String kekString = KeyGenerator.generateKEK( kekPart1 , kekPart2 );
//		System.out.println("KEK-----"+kekString);
		String kvvString = "C3D5EE";//KeyGenerator.getKVV(kekString);
//		System.out.println("KVV-----"+kvvString);
//		/*
//		 * For the first time, pass new kek as null
//		 * For the second time, pass the new kek
//		 */
		String newKekString = null;
//		String kekString = "989173A43E9DB352";
		String aukString = KeyGenerator.getAUK( kekString, newKekString );
		System.out.println("AUK-------"+aukString);
		//Calculate AUK-AEBAE983D6406D07
		
		
//		String kek = "379723239789FD9D";
//		String newKey = "2DC962135AE1515F";
//		String aukString = KeyGenerator.getAUK( kek, newKey );
//		System.out.println("nEW AUK-------"+aukString);
		
		
		
	}
}
