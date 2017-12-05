package de.vaplus.api.controller;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import de.vaplus.api.controller.stub.FaqStub;

@WebService
public interface FaqService {

	@WebMethod
	public List<FaqStub> getFaqList() throws Exception;
}
