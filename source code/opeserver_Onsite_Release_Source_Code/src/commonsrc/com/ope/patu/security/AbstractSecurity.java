package com.ope.patu.security;

import com.ope.patu.server.constant.ServerConstants;

public class AbstractSecurity 
{
	public static OPESecurity getSecurity( String securityType )
	{
		OPESecurity opesecurity = null;
		if( securityType.equals(ServerConstants.NORMAL_SECURITY ) )
		{
			opesecurity = new PBESecurityImpl();
		}
		else if( securityType.equals(ServerConstants.ASYMMETRIC_SECURITY ) )
		{
			opesecurity = new AsymSecurityImpl();
		}
		return opesecurity;
	}
}
