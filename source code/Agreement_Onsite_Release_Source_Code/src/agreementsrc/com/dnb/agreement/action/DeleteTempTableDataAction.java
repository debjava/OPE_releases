package com.dnb.agreement.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dnb.agreement.DAO.DAOOpe;
import com.dnb.agreement.DAO.ServiceSpecificDAO;
import com.dnb.agreement.bean.ServiceSpecificCommonBean;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;
import com.dnb.agreement.exception.IErrorCodes;


public class DeleteTempTableDataAction extends Action {
	
	private Logger logger=Logger.getLogger(DeleteTempTableDataAction.class);
	
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws AgreementSystemException,AgreementBusinessException
	{
		String message="failed";
		//obtain the user id from session
		
		logger.debug("Ajax call for DeleteTempTableDataAction");

		List opeErrorList = new ArrayList();
		ServiceSpecificCommonBean sscb=null;
		int status = 0;
		sscb=(ServiceSpecificCommonBean)form;
		DAOOpe daoOPE=DAOOpe.getDAOOpe(1);
		ServiceSpecificDAO ssDAO=daoOPE.getServiceSpecificDAO();
		try {
			status = ssDAO.clearTemporaryTable(sscb);
			
			request.setAttribute("result", status);
		}catch(AgreementSystemException se) {
			opeErrorList.add(se.getErrorCode());
			logger.debug("AgreementSystemException : "+se.getErrorCode());
		}catch(RuntimeException re) {
			opeErrorList.add(IErrorCodes.DEFAULT_ERROR);
			logger.debug("RuntimeException :: "+re);
		}
		
		return mapping.findForward(message);
		
		}
	}


