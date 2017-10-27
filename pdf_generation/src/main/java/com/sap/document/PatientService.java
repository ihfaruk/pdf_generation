package com.sap.document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PatientService {
	private String serviceEndpoint = "https://sap-rest-hana.cfapps.sap.hana.ondemand.com/rest/%s/Patient/%s";
	public Map<String, Object> getPatientDataById(String tenantId, String patientId) {
		Map<String, Object> fieldValueMap = new HashMap<String, Object>();
		try {
			HttpURLConnection conn = (HttpURLConnection) getCompleteEnpointURL(tenantId, patientId).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			if (conn.getResponseCode() != 201) {
				System.out.println("Response Code:"+conn.getResponseCode() +": Response Message" + conn.getResponseMessage());
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String response = new String();
			for (String line; (line = br.readLine()) != null; response += line);
			ObjectMapper mapper = new ObjectMapper();
			fieldValueMap = mapper.readValue(response, Map.class);
			conn.disconnect();
		} catch(Exception ex) {
			ex.getMessage();
		}

		return fieldValueMap;
	}

	private URL getCompleteEnpointURL(String tenantId, String patientId) throws Exception {
		if(tenantId != null && tenantId.trim().length()>0 && patientId!= null && patientId.trim().length() >0) {
			return new URL(String.format(serviceEndpoint, tenantId, patientId));
		}
		throw new Exception("End point is not in proper format");
	}

}
