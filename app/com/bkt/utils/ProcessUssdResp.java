package com.bkt.utils;

public class ProcessUssdResp {


	private String requestXml = "";

	public ProcessUssdResp()
	{
	}

	public ProcessUssdResp(String requestStr)
	{
		requestXml = requestStr;
	}

	public String getRequestXml()
	{
		return requestXml;
	}

	public void setRequestXml(String xml)
	{
		requestXml = xml;
	}

	public String getStatusCode()
	{
	
		if(requestXml.contains("<name>StatusCode</name><value>"))
		{
			String temp = requestXml.split("<name>StatusCode</name><value>")[1].split("</value>")[0];
			return temp;
		
		}
		else
		{
			return "0";
		}
	}public String getStatusDesc()
	{
		if(requestXml.contains("<name>StatusDesc</name><value>"))
		{
			String temp = requestXml.split("<name>StatusDesc</name><value>")[1].split("</value>")[0];
			return temp;
		}
		else
		{
			return "";
		}
	}public String getProcessingNumber()
	{
		if(requestXml.contains("<name>ProcessingNumber</name><value>"))
		{
			String temp = requestXml.split("<name>ProcessingNumber</name><value>")[1].split("</value>")[0];
			return temp;
		}
		else
		{
			return "";
		}
	}public String getMOMTransactionID()
	{
		if(requestXml.contains("<name>MOMTransactionID</name><value>"))
		{
			String temp = requestXml.split("<name>MOMTransactionID</name><value>")[1].split("</value>")[0];
			return temp;
		
		}
		else
		{
			return "";
		}
	}
	



}
