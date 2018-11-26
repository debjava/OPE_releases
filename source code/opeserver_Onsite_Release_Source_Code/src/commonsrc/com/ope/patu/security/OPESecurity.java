package com.ope.patu.security;

import com.ope.patu.exception.OPESecurityException;

public interface OPESecurity
{
	public String getEncryptedString( String originalValue ) throws OPESecurityException;
	public String getDecryptedString( String ecryptedString ) throws OPESecurityException;
}
