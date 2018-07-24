package com;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BARTAPICaller {
	
	private static OkHttpClient client = new OkHttpClient();
	
	
	public static Map<String, String> getStationList() throws Exception
	{
		
		Response resp = null;
		String url = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=ZPBV-5VQB-98QT-DWE9&json=y";
		Request request = new Request.Builder().url(url).get().build();
		
		try
		{
			resp = client.newCall(request).execute();
		}
		catch(Exception e)
		{
			System.out.println("No connection");
		}
		
		String s = resp.body().string();
		
		try
		{
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(s);
			String info = "";
			Map<String, String> stationInfo = new TreeMap<String, String>();
			Map<String, String> mapOfStations = new HashMap<String, String>();
			List<JsonNode> station = (ArrayList<JsonNode>) jsonNode.findValues("station");
			List<JsonNode> stationName = (ArrayList<JsonNode>) jsonNode.findValues("name");
			List<JsonNode> stationAbbr = (ArrayList<JsonNode>) jsonNode.findValues("abbr");
			for(int i = 0; i < station.size(); i++)
			{	
				for(int j = 0; j < stationName.size(); j++)
				{
					mapOfStations.put(stationName.get(j).toString(), stationAbbr.get(j).toString());	
				}
			}
			
			for(Map.Entry<String, String> m1: mapOfStations.entrySet())
			{	
				String value = m1.getValue().replaceAll("\"", "");
				
				info = getStationInfo(value);
				stationInfo.put(value, info);
			}
			
			return stationInfo;
		}
		
		catch(Exception e)
		{
			return null;
		}	
	}
	
	public static String getStationInfo(String abbr) throws Exception
	{
		Response resp = null;
		String url = "https://api.bart.gov/api/stn.aspx?cmd=stninfo&orig="+abbr+"&key=ZPBV-5VQB-98QT-DWE9&json=y";
		Request request = new Request.Builder().url(url).get().build();
		
		try
		{
			resp = client.newCall(request).execute();
		}
		catch(Exception e)
		{
			System.out.println("No connection");
		}
		
		String s = resp.body().string();
		
		try
		{
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(s);
			
			String info = "";
			List<JsonNode> root = (ArrayList<JsonNode>) jsonNode.findValues("root");
			List<JsonNode> stationName = (ArrayList<JsonNode>) jsonNode.findValues("name");
			List<JsonNode> address = (ArrayList<JsonNode>) jsonNode.findValues("address");
			List<JsonNode> city = (ArrayList<JsonNode>) jsonNode.findValues("city");
			List<JsonNode> state = (ArrayList<JsonNode>) jsonNode.findValues("state");
			List<JsonNode> zipcode = (ArrayList<JsonNode>) jsonNode.findValues("zipcode");
			List<JsonNode> south = (ArrayList<JsonNode>) jsonNode.findValues("north_platforms");
			List<JsonNode> north = (ArrayList<JsonNode>) jsonNode.findValues("south_platforms");
			List<JsonNode> platform = (ArrayList<JsonNode>) jsonNode.findValues("platform");
			
			for(int i = 0; i < root.size();)
			{	
				info = stationName.get(i).toString() + " | " +  address.get(i).toString() + " " + city.get(i).toString() + " " + state.get(i).toString() + " " + zipcode.get(i).toString();

				return info;
			}
			return "";			
		}
		
		catch(Exception e)
		{
			return null;
		}	
	}
	
	public static String getStationName(String abbr, Map<String, String> stationAbbr)
	{
		for(Map.Entry<String, String> m1: stationAbbr.entrySet())
		{
			
			String key = m1.getKey();
			if(key.contains(abbr.toUpperCase()))
			{
				String value = m1.getValue().replaceAll("\"", "");
				String[] stationName = value.split(" \\| ");
				//System.out.println(value);
				//System.out.println(stationName[0]);
				return stationName[0];
			}	
		}
		return "";
	}

}
