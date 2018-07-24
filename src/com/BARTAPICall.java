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
import org.json.*;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javafx.stage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BARTAPICall {
	
	
	private static OkHttpClient client = new OkHttpClient();
	//private static enum options {stationInfo, tripPlanner};
	private static Scanner scanner;
	
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
	
	public static String getStationAbbr(String station, Map<String, String> stationAbbr)
	{
		//for(int i = 0; i < stationAbbr.size(); i++)
		//{
			for(Map.Entry<String, String> m1: stationAbbr.entrySet())
			{
				String value = m1.getValue().replaceAll("\"", "");
				String[] stationName = value.split(" \\| ");
				String key = m1.getKey();
				//System.out.println(stationName[0]);
				if(station.equals(stationName[0]))
				{
					return key;
				}
			}
			return null;
		//}
		//return null;	
	}
	
	public static void getRealTimeEstimates(String stationAbbr) throws Exception
	{
		String url = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=" + stationAbbr + "&key=ZPBV-5VQB-98QT-DWE9&json=y";
		Response resp = null;
		Request request = new Request.Builder().url(url).get().build();
		try
		{
			resp = client.newCall(request).execute();
		}
		catch (Exception e)
		{
			throw new Exception("No connection");
		}
		String s = resp.body().string();
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(s);
			
			/*Set<JsonNode> station = (HashSet<JsonNode>)jsonNode.findValues("station");
			Set<JsonNode> destination = (HashSet<JsonNode>)jsonNode.findValues("destination");
			Set<JsonNode> minutes = (HashSet<JsonNode>)jsonNode.findValues("minutes");
			Set<JsonNode> length = (HashSet<JsonNode>)jsonNode.findValues("length");
			Set<JsonNode> platform = (HashSet<JsonNode>)jsonNode.findValues("platform");
			Set<JsonNode> direction = (HashSet<JsonNode>)jsonNode.findValues("direction");*/
			
			List<JsonNode> station = (ArrayList<JsonNode>)jsonNode.findValues("station");
			List<JsonNode> destination = (ArrayList<JsonNode>)jsonNode.findValues("destination");
			List<JsonNode> minutes = (ArrayList<JsonNode>)jsonNode.findValues("minutes");
			List<JsonNode> length = (ArrayList<JsonNode>)jsonNode.findValues("length");
			List<JsonNode> platform = (ArrayList<JsonNode>)jsonNode.findValues("platform");
			List<JsonNode> direction = (ArrayList<JsonNode>)jsonNode.findValues("direction");
			
			System.out.println("Station: " + station.size());
			System.out.println("destination: " + destination.size());
			System.out.println("minutes: " + minutes.size());
			System.out.println("length: " + length.size());
			System.out.println("platform: " + platform.size());
			System.out.println("---------------------------------------------");
			
			
			
			for(int i = 0; i < destination.size(); i++)
			{
				//System.out.println("Direction: " + direction.get(i+3).toString().replaceAll("\"", ""));
				
				System.out.println(length.get(i).toString().replaceAll("\"", "") + " Car Train");
				System.out.println("Platform " + platform.get(i).toString().replaceAll("\"", ""));
				System.out.println(destination.get(i).toString().replaceAll("\"", "") + " \t " + minutes.get(i).toString().replaceAll("\"", "") + " min");
				System.out.println();
			}
			
			/*for(int i = 0; i < destination.size(); i++)
			{
				System.out.println("Platform " + platform.get(i).toString().replaceAll("\"", ""));
				System.out.println(destination.get(i).toString().replaceAll("\"", "") + " \t " + minutes.get(i).toString().replaceAll("\"", "") + " min");
				System.out.println();
			}*/
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void getRealTime(String abbr) throws Exception
	{
		String url = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=" + abbr + "&key=ZPBV-5VQB-98QT-DWE9&json=y";
		Response resp = null;
		Request request = new Request.Builder().url(url).get().build();
		try
		{
			resp = client.newCall(request).execute();
		}
		catch (Exception e)
		{
			throw new Exception("No connection");
		}
		String s = resp.body().string();
		JSONObject jo = new JSONObject(s);
        JSONObject jo1 = jo.getJSONObject("root");
        JSONArray ja1 = jo1.getJSONArray("station");
        
        for(int i = 0; i < ja1.length(); i++)
        {
        		System.out.println(ja1.length());
        		JSONObject jo2 = ja1.getJSONObject(i);
        		//for(int j = 0; j < jo2.length(); j++) 
        		//{
    			JSONArray ja2 = jo2.getJSONArray("etd");
    			for(int j = 0; j < ja2.length(); j++)
    			{
    				JSONObject jo3 = ja2.getJSONObject(j);
        			String dest = jo3.getString("destination");
    				System.out.println(dest);
    				
    				JSONArray estimate = jo3.getJSONArray("estimate");
    				JSONObject jo4 = estimate.getJSONObject(i);
    				String minutes = jo4.getString("minutes");
    				String platform = jo4.getString("platform");
    				String length = jo4.getString("length");
    				System.out.println(length + " Car Train" + "\t " + minutes + " Minutes");
    				System.out.println("Platform " + platform);
    				System.out.println();
    			}
        			/*JSONObject jo3 = ja2.getJSONObject(j);
        			String dest = jo3.getString("destination");
    				System.out.println(dest);
    				JSONArray estimate = jo3.getJSONArray("estimate");
    				JSONObject jo4 = estimate.getJSONObject(j);
				String minutes = jo4.getString("minutes");
				String platform = jo4.getString("platform");
				String length = jo4.getString("length");
				System.out.println(length + " Car Train" + "\t " + minutes + " Minutes");
				System.out.println("Platform " + platform);
				System.out.println();*/
        		//}
        		
        }
        
		
		
	}
	
	public static void getTripInfo(String start, String dest, Map<String, String> stationAbbr) throws Exception
	{
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("h:mm");
		String dateStr = df.format(date);
		String[] timeArray = dateStr.split(":");
		
		int curHour = Integer.parseInt(timeArray[0]);
		int curMinute = Integer.parseInt(timeArray[1]);
		//System.out.println(curHour); // hour		
		//System.out.println(curMinute); // min
		//Map<String, String> tripLeg = new HashMap<String, String>();
		Response resp = null;
		String url = "https://api.bart.gov/api/sched.aspx?cmd=depart&orig=" + start + "&dest=" + dest + "&time=now&b=0&key=ZPBV-5VQB-98QT-DWE9&json=y";
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
		
		if(start.equals(dest) || dest.equals(start))
		{
			throw new Exception("The destination must be different from the starting station");
		}
		
		try
		{
			GridPane gp = new GridPane();
    			gp.setAlignment(Pos.CENTER);
			
    			Stage s11 = new Stage();
			s11.setResizable(false);
    			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(s);
			
			//List<JsonNode> trip = (ArrayList<JsonNode>)jsonNode.findValues("leg");
			List<JsonNode> order = (ArrayList<JsonNode>)jsonNode.findValues("@order");
			List<JsonNode> origin = (ArrayList<JsonNode>)jsonNode.findValues("@origin");
			List<JsonNode> destination = (ArrayList<JsonNode>)jsonNode.findValues("@destination");
			List<JsonNode> trainDestination = (ArrayList<JsonNode>)jsonNode.findValues("@trainHeadStation");

			List<JsonNode> originTime = (ArrayList<JsonNode>)jsonNode.findValues("@origTimeMin");
			List<JsonNode> destTime = (ArrayList<JsonNode>)jsonNode.findValues("@destTimeMin");
			List<JsonNode> line = (ArrayList<JsonNode>)jsonNode.findValues("@line");
			
			for(int i = 0; i < order.size(); i++) 
			{
				
				int orderNum = Integer.parseInt(order.get(i).toString().replaceAll("\"", ""));
				if(i >= orderNum && orderNum >= 1)
				{
					break;
				}
				
		        Text t = new Text(String.format("Step %d: ", orderNum));  
		        t.setTranslateX(10);
		        //t.setTranslateY(-400 + (150 * i));// + i);//(100 * i));
		        t.setTranslateY(-230 + (150 * i));// + i);//(100 * i));
		        t.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		        gp.add(t,0,0);
		        
		        Text t1 = new Text(String.format("Your Start Station: " + getStationName(origin.get(i+1).toString().replaceAll("\"", ""), stationAbbr)));  
		        t1.setTranslateX(10);
		        //t1.setTranslateY(-380 + (150 * i));// + i);
		        t1.setTranslateY(-210 + (150 * i));// + i);
		        t1.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		        gp.add(t1,0,0);
		        
		        Text t2 = new Text(String.format("Next Train Leaves At: " + originTime.get(i+1).toString().replaceAll("\"", "")));  
		        t2.setTranslateX(10);
		        //t2.setTranslateY(-360 + (150 * i));// + i);
		        t2.setTranslateY(-190 + (150 * i));// + i);
		        t2.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		        gp.add(t2,0,0);
		        
		        Text t3 = new Text(String.format("Train Destination: " + getStationName(trainDestination.get(i).toString().replaceAll("\"", ""), stationAbbr)));  
		        t3.setTranslateX(10);
		        //t3.setTranslateY(-340 + (150 * i));// + i);
		        t3.setTranslateY(-170 + (150 * i));// + i);
		        t3.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		        gp.add(t3,0,0);
		        
		        Text t4 = new Text(String.format("Your Destination Station: " + getStationName(destination.get(i+1).toString().replaceAll("\"", ""), stationAbbr)));  
		        t4.setTranslateX(10);
		        //t4.setTranslateY(-320 + (150 * i));// * + i);
		        t4.setTranslateY(-150 + (150 * i));// * + i);
		        t4.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		        gp.add(t4,0,0);
		         
		        Text t5 = new Text(String.format("Estimated Time of Arrival: " + destTime.get(i+1).toString().replaceAll("\"", "")));  
		        t5.setTranslateX(10);
		        //t5.setTranslateY(-300 + (150 * i));//  + i);
		        t5.setTranslateY(-130 + (150 * i));//  + i);
		        t5.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		        gp.add(t5,0,0);
		        
		        Text t6 = new Text(String.format("Line To Take: " + getLineName(line.get(i).toString().replaceAll("\"", ""))));  
		        t6.setTranslateX(10);
		        //t6.setTranslateY(-280 + (150 * i));// + i);
		        t6.setTranslateY(-110 + (150 * i));// + i);
		        t6.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		        gp.add(t6,0,0);
		        
		        /*Text t7 = new Text("\n");  
		        t7.setTranslateX(10);
		        t7.setTranslateY(-330 + (50 * i));// + i);
		        t7.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		        gp.add(t7,0,0);*/
		        
		        Text t8 = new Text("-----------------------------------------------------------------------------------------------------------------");  
		        t8.setTranslateX(10);
		        //t8.setTranslateY(-270 + (150 * i));// + i);//(50 * i));
		        t8.setTranslateY(-90 + (150 * i));// + i);//(50 * i));
		        t8.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		        gp.add(t8,0,0);

				System.out.println(String.format("Step %d: ", orderNum));
				System.out.println();
				System.out.println("Your Start Station: " + getStationName(origin.get(i+1).toString().replaceAll("\"", ""), stationAbbr));
				String[] startHour = originTime.get(i+1).toString().replaceAll("\"", "").split(":");
				
				System.out.println("Next Train Leaves At: " + originTime.get(i+1).toString().replaceAll("\"", ""));	

				System.out.println("Train Destination: " + getStationName(trainDestination.get(i).toString().replaceAll("\"", ""), stationAbbr));

				System.out.println("Your Destination Station: " + getStationName(destination.get(i+1).toString().replaceAll("\"", ""), stationAbbr));
				System.out.println("Estimated Time of Arrival: " + destTime.get(i+1).toString().replaceAll("\"", ""));
				System.out.println("Line To Take: " + getLineName(line.get(i).toString().replaceAll("\"", "")));

				System.out.println();
				System.out.println("-----------------------------------------------------------------------------------------------------------------");
			
				
				
			}
			
			s11.setWidth(600);
	        s11.setHeight(500);
	        
	        Scene scene = new Scene(gp);
	        s11.setScene(scene);
	        s11.show(); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static String getLineName(String line)
	{
		switch(line)
		{
		case "ROUTE 1":
			return "Pittsburg/Bay Point - San Francisco International Airport/Millbrae";
			
		
		case "ROUTE 2":
			return "Millbrae/San Francisco International Airport - Pittsburg/Bay Point";
		
		case "ROUTE 3":
			return "Warm Springs/South Fremont - Richmond";
		
		case "ROUTE 4":
			return "Richmond - Warm Springs/South Fremont";
		
		case "ROUTE 5":
			return "Warm Springs/South Fremont - Daly City";
		
		case "ROUTE 6":
			return "Daly City - Warm Springs/South Fremont";
		
		case "ROUTE 7":
			return "Richmond - Daly City/Millbrae";
			
		case "ROUTE 8":
			return "Millbrae/Daly City - Richmond";
			
		case "ROUTE 11":
			return "Dublin/Pleasanton - Daly City";
			
		case "ROUTE 12":
			return "Daly City - Dublin/Pleasanton";
		
		case "ROUTE 19":
			return "Coliseum - Oakland Int'l Airport";
			
		case "ROUTE 20":
			return "Oakland Int'l Airport - Coliseum";	
			
		default:
			return "";
		}
	}
	
	
	
	public static void main(String[] args) throws Exception 
	{
		
		getRealTime("CIVC");
		/*Map<String, String> mapOfStations = getStationList();
		
		for(Map.Entry<String, String> m1: mapOfStations.entrySet())
		{
			//String key = m1.getKey();
			String value = m1.getValue();
			System.out.println(value);
					
		}
		
		while(true)
		{
			scanner = new Scanner(System.in);
			
			System.out.println("Options: ");
			
			System.out.println("1. Entire List of Stations");
			System.out.println("2. Specific Station Information");
			System.out.println("3. Trip Planner ");
			System.out.println("4. Exit ");
			System.out.println("____________________________________");
			System.out.println("Enter an option: ");
			
			int number = scanner.nextInt();
			
			switch(number)
			{
				case 1:
					
					for(Map.Entry<String, String> m1: mapOfStations.entrySet())
					{
						String key = m1.getKey().replaceAll("\"", "");
						String value = m1.getValue().replaceAll("\"", "");
						System.out.println(key + " " + value);
					}
					break;
					
				case 2:
					scanner = new Scanner(System.in);
					System.out.println("Enter a station name or abbr to get information: ");
					String station = scanner.nextLine();
					for(Map.Entry<String, String> m1: mapOfStations.entrySet())
					{
						
						String key = m1.getKey().replaceAll("\"", "");
						String value = m1.getValue().replaceAll("\"", "");
						String[] stationName = value.split(" \\| ");
						
						if(stationName[0].contains(station) || key.equals(station.toUpperCase()))
						{
							System.out.println(key + " " + value);
							getRealTimeEstimates(key);
							break;
						}
					}
					break;
					
				case 3:
					scanner = new Scanner(System.in);
					System.out.println("Enter your start station: ");
					String start = scanner.nextLine();
					
					System.out.println("Enter your destination: ");
					String dest = scanner.nextLine();
					
					
					for(Map.Entry<String, String> m1: mapOfStations.entrySet())
					{
						String key = m1.getKey().replaceAll("\"", "");
						String value = m1.getValue().replaceAll("\"", "");
						if(start.length() > 4 && dest.length() > 4)
						{
							String startAbbr = getStationAbbr(start, mapOfStations);
							String endAbbr = getStationAbbr(dest, mapOfStations);
							//System.out.println(startAbbr);
							getTripInfo(startAbbr, endAbbr, mapOfStations);
							break;
						}
						else
						{
							start = start.toUpperCase();
							
							if(key.contains(start.toUpperCase()))
							{
								for(Map.Entry<String, String> m2: mapOfStations.entrySet())
								{
									key = m2.getKey().replaceAll("\"", "");
									value = m2.getValue().replaceAll("\"", "");
									dest = dest.toUpperCase();
									if(key.contains(dest.toUpperCase()))
									{
										getTripInfo(start, dest, mapOfStations);		
									}	
								}
							}
						}	
					}
					break;
					
				case 4:
					System.out.println("Exiting");
					System.exit(0);
					break;
					
				default:
					System.out.println("Invalid input. You must enter a number");
			}
		}
			
	}*/

	}
}
