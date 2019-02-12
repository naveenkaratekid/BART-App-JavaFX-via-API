package com.nk.bart.bart_maven;

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
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BARTAPICall {

	private static OkHttpClient client = new OkHttpClient();
	//private static enum options {stationInfo, tripPlanner};
	private static Scanner scanner;
	private static Logger logger = LoggerFactory.getLogger(BARTAPICall.class);
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
			logger.error("No connection");
			//System.out.println("No connection");
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
				//System.out.println(info);
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
			logger.error("No connection");
			//System.out.println("No connection");
		}
		
		String s = resp.body().string();
		
		try
		{
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(s);
			
			String info = "";
			List<JsonNode> stationName = (ArrayList<JsonNode>) jsonNode.findValues("name");
			List<JsonNode> address = (ArrayList<JsonNode>) jsonNode.findValues("address");
			List<JsonNode> city = (ArrayList<JsonNode>) jsonNode.findValues("city");
			List<JsonNode> state = (ArrayList<JsonNode>) jsonNode.findValues("state");
			List<JsonNode> zipcode = (ArrayList<JsonNode>) jsonNode.findValues("zipcode");
			List<JsonNode> route = (ArrayList<JsonNode>) jsonNode.findValues("route");
			//String[] rte = route.toString().replace("[", "").replace("]", "").replace("\"", "").split(",");

			for(int i = 0; i < route.size();)
			{
				//System.out.println();
				info = stationName.get(i).toString() + " | " +  address.get(i).toString() + " " + city.get(i).toString() + " " + state.get(i).toString() + " " + zipcode.get(i).toString();
					
				/*for(String s2: rte)
				{
					info += " | " + getLineName(s2);
					System.out.println(info);
					return info;
				}*/
				
				return info;
			}
			return "";			
		}
		
		catch(Exception e)
		{
			return null;
		}	
	}
	
	public static String getRouteNumber(String abbr) throws Exception
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
			logger.error("No connection");
			//System.out.println("No connection");
		}
		
		String s = resp.body().string();
		
		try
		{
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(s);
			
			String info = "";

			List<JsonNode> route = (ArrayList<JsonNode>) jsonNode.findValues("route");
			//List<JsonNode> platform = (ArrayList<JsonNode>) jsonNode.findValues("platform");
			ArrayList<String> listOfRoutes = new ArrayList<String>();
			//System.out.println("Route: "  + route.size());
			for(int i = 0; i < route.size();i++)
			{
				
				info = route.get(i).toString().trim();
				listOfRoutes.add(info);
				
			}

			return listOfRoutes.toString();
			
			/*for(int i = 0; i < root.size();)
			{	
				
				info = stationName.get(i).toString() + " | " +  address.get(i).toString() + " " + city.get(i).toString() + " " + state.get(i).toString() + " " + zipcode.get(i).toString() + " | " + getLineName(route.get(i).toString());
				return info;
			}*/		
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
				String[] stationName = value.split("\\| ");
				String key = m1.getKey();
				String name = stationName[0].trim();
				//System.out.println(name);
				//System.out.println(station);
				//if(station.contains(stationName[0]))
				if(station.contains(name))
				{
					return key;
				}
			}
			return null;
		//}
		//return null;	
	}

	/*public static void getRealTimeEstimates(String stationAbbr) throws Exception
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
				System.out.println(length.get(i).toString().replaceAll("\"", "") + " Car Train");
				System.out.println("Platform " + platform.get(i).toString().replaceAll("\"", ""));
				System.out.println(destination.get(i).toString().replaceAll("\"", "") + " \t " + minutes.get(i).toString().replaceAll("\"", "") + " min");
				System.out.println();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}*/

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
		
		GridPane gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
	
		Stage s11 = new Stage();
		s11.setResizable(true);
		
		String s = resp.body().string();
		JSONObject jo = new JSONObject(s);
        JSONObject jo1 = jo.getJSONObject("root");
        JSONArray ja1 = jo1.getJSONArray("station");
        
        
        for(int i = 0; i < ja1.length(); i++)
        {
        		//System.out.println(ja1.length());
        		JSONObject jo2 = ja1.getJSONObject(i);
        		//for(int j = 0; j < jo2.length(); j++) 
        		//{
        		try
        		{
        			JSONArray ja2 = jo2.getJSONArray("etd");
        			//List<String> list = new ArrayList<String>();
	    			for(int j = 0; j < ja2.length(); j++)
	    			{
	    				Text destName = new Text();
	    				destName.setTranslateX(0);
	    				//destName.setTranslateY(-180 + (60 * j));
	    				destName.setTranslateY(-180 + (55 * j));
	    				gp.add(destName, 0, 0);
	    				
	    				JSONObject jo3 = ja2.getJSONObject(j);
	        			String dest = jo3.getString("destination");
	        			
	    				logger.info(dest);
	        			//System.out.println(dest);
	    				Text trainDest = new Text();
	    				trainDest.setTranslateX(0);
	    				trainDest.setTranslateY(-180 + (55 * j));
	    				//trainDest.setTranslateY(-180 + (60 * j));
	        			trainDest.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	        			gp.add(trainDest, 0, 0);
	    				
	    				JSONArray estimate = jo3.getJSONArray("estimate");
	    				JSONObject jo4 = estimate.getJSONObject(i);
	    				
	    				String hex = jo4.getString("hexcolor");
	    				destName.setFill(Color.web(hex)); 				
	    				
	    				if(destName.getFill().toString().contains("ffff33"))
	    				{
	    					destName.setFill(Color.GOLD);
	    				}
	    				
	    				String minutes = jo4.getString("minutes");
	    				String platform = jo4.getString("platform");
	    				String length = jo4.getString("length");
	    				Text minLength = new Text();
	    				
	    				minLength.setTranslateX(0);
	    				//minLength.setTranslateY(-160 + (60 * j));
	    				minLength.setTranslateY(-160 + (55 * j));
	    				minLength.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	        			gp.add(minLength, 0, 0);
	        			
	        			logger.info((length + " Car Train") + "\t" + (minutes.contains("Leaving") ? "Leaving": minutes + " Minutes"));
	    				//System.out.println((length + " Car Train") + "\t" + (minutes.contains("Leaving") ? "Leaving": minutes + " Minutes"));
	    				
	        			logger.info("Platform " + platform);
	        			//System.out.println("Platform " + platform);
	    				Text plat = new Text((platform.contains("1") || platform.contains("3") ? "\t\t\t\t\t\t\t\tPlatform " : "Platform ") + platform);
	    				minLength.setText((platform.contains("1") || platform.contains("3") ? "\t\t\t\t\t\t\t\t" : "") + length + " Car Train\t\t" + (minutes.contains("Leaving") ? "Leaving": minutes + " Minutes"));
	    				
	    				if(minutes.contains("2") || minutes.contains("1"))
	    				{
	    					
	    				}
	    				trainDest.setText((platform.contains("1") || platform.contains("3") ? "\t\t\t\t\t\t\t\t" : "") /*+ dest*/);
	    				
	    				destName.setText((platform.contains("1") || platform.contains("3") ? "\t\t\t\t\t\t\t\t    " : "") + dest);
	    				
	    				logger.info(hex);
	    				//System.out.println(hex);
	    				plat.setTranslateX(0);
	    				//plat.setTranslateY(-140 + (60 * j));
	    				plat.setTranslateY(-140 + (55 * j));
	    				plat.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	        			gp.add(plat, 0, 0);
	    				System.out.println();
	    				
	    				//minLength.setFill(Color.WHITE);
	    				//trainDest.setFill(Color.WHITE);
	    				//plat.setFill(Color.WHITE);
	    				
	    			}
        		}
        		catch(Exception e)
        		{
        			Label l = new Label("No trains leaving at this time");
        			//l.setTextFill(Color.WHITE);
        			l.setFont(new Font("Arial", 14));
        			l.setTranslateX(10);
        			l.setTranslateY(-180);
        			gp.add(l, 0, 0);
        			logger.error("No trains leaving");
        			//System.out.println("No trains leaving");
        		}
    			
        }
        //s11.setWidth(500);
        //s11.setHeight(400);
        s11.setWidth(450);
	    s11.setHeight(450);
        
        Scene scene = new Scene(gp);
        //gp.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        s11.setScene(scene);
        s11.show(); 
	}
	
	public static void getTripInfo(String start, String dest, Map<String, String> stationAbbr) throws Exception
	{
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("h:mm");
		GridPane gp = new GridPane();
		Response resp = null;
		String url = "https://api.bart.gov/api/sched.aspx?cmd=depart&orig=" + start + "&dest=" + dest + "&time=now&key=ZPBV-5VQB-98QT-DWE9&json=y";
		Request request = new Request.Builder().url(url).get().build();
		try
		{
			resp = client.newCall(request).execute();
		}
		catch(Exception e)
		{
			logger.error("No connection");
			//System.out.println("No connection");
		}
		String s = resp.body().string();
		System.out.println(s);
		
		if(start.equals(dest) || dest.equals(start))
		{
			//GridPane gp = new GridPane();
			Text l = new Text("The destination must be different\nfrom the starting station");
			l.setFill(Color.RED);
			l.setFont(new Font("Arial", 18));
			l.setTranslateX(0);
			l.setTranslateY(-10);
			l.setVisible(true);
			gp.add(l, 0, 0);
			
			PauseTransition delay = new PauseTransition(Duration.seconds(3));
						
			Scene scene = new Scene(gp);
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setWidth(300);
	        stage.setHeight(100);
			stage.setScene(scene);
			stage.setTitle("Error");
			stage.show();
			delay.setOnFinished(event -> stage.close());
			delay.play();
			//throw new Exception("The destination must be different from the starting station");
		}
		
		try
		{
			
    			gp.setAlignment(Pos.CENTER);
			
    			Stage s11 = new Stage();
    			s11.setResizable(true);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(s);
			
			List<JsonNode> order = (ArrayList<JsonNode>)jsonNode.findValues("@order");
			List<JsonNode> origin = (ArrayList<JsonNode>)jsonNode.findValues("@origin");
			List<JsonNode> destination = (ArrayList<JsonNode>)jsonNode.findValues("@destination");
			List<JsonNode> trainDestination = (ArrayList<JsonNode>)jsonNode.findValues("@trainHeadStation");
			List<JsonNode> originTime = (ArrayList<JsonNode>)jsonNode.findValues("@origTimeMin");
			List<JsonNode> destTime = (ArrayList<JsonNode>)jsonNode.findValues("@destTimeMin");
			List<JsonNode> line = (ArrayList<JsonNode>)jsonNode.findValues("@line");
			List<JsonNode> fares = (ArrayList<JsonNode>)jsonNode.findValues("@fare");
			List<JsonNode> tripTime = (ArrayList<JsonNode>)jsonNode.findValues("@tripTime");
			
			Text fare = new Text(String.format("Fare: $%s", fares.get(0).toString().replaceAll("\"", "")));
			fare.setTranslateX(300);
			fare.setTranslateY(-200);
			fare.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
			gp.add(fare, 0, 0);
			
			int totalTime = Integer.parseInt(tripTime.get(0).toString().replaceAll("\"", ""));
			
			int totalHours = totalTime / 60;
			int totalMinutes = totalTime % 60;
			
			Text time = new Text("Total trip time: \n" +  (totalHours == 1 ? totalHours + " hour" : totalHours + " hours") + " " + (totalMinutes == 1 ? totalMinutes + " minute" : totalMinutes + " mins"));
			time.setTranslateX(300);
			time.setTranslateY(-150);
			time.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
			gp.add(time, 0, 0);
			
			for(int i = 0; i < order.size(); i++) 
			{
				int orderNum = Integer.parseInt(order.get(i).toString().replaceAll("\"", ""));
				if(i >= orderNum && orderNum >= 1)
				{
					break;
				}
			
		        Text t = new Text(String.format("Step %d: ", orderNum));  
		        //t.setTranslateX(10);
		        t.setTranslateX(-10);
		        //t.setTranslateY(-400 + (150 * i));// + i);//(100 * i));
		        //t.setTranslateY(-230 + (150 * i));// + i);//(100 * i));
		        t.setTranslateY(-207 + (140 * i));// + i);//(100 * i));
		        t.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		        gp.add(t,0,0);
		        
		        Text t1 = new Text(String.format("Your Start Station: " + getStationName(origin.get(i+1).toString().replaceAll("\"", ""), stationAbbr)));  
		        //t1.setTranslateX(10);
		        t1.setTranslateX(-10);
		        //t1.setTranslateY(-380 + (150 * i));// + i);
		        //t1.setTranslateY(-210 + (150 * i));// + i);
		        t1.setTranslateY(-195 + (140 * i));// + i);
		        t1.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
		        gp.add(t1,0,0);
		        
		        Text t2 = new Text(String.format("Next Train Leaves At: " + originTime.get(i+1).toString().replaceAll("\"", "")));  
		        //t2.setTranslateX(10);
		        t2.setTranslateX(-10);
		        //t2.setTranslateY(-360 + (150 * i));// + i);
		        //t2.setTranslateY(-190 + (150 * i));// + i);
		        t2.setTranslateY(-175 + (140 * i));// + i);
		        t2.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
		        gp.add(t2,0,0);
		        
		        Text t3 = new Text(String.format("Train Destination: " + trainDestination.get(i).toString().replaceAll("\"", ""))); //getStationName(trainDestination.get(i).toString().replaceAll("\"", ""), stationAbbr)));  
		        System.out.println((String.format("Train Destination: " + trainDestination.get(i).toString().replaceAll("\"", "")))); //, stationAbbr))));  
		        //t3.setTranslateX(10);
		        t3.setTranslateX(-10);
		        //t3.setTranslateY(-340 + (150 * i));// + i);
		        //t3.setTranslateY(-170 + (150 * i));// + i);
		        t3.setTranslateY(-155 + (140 * i));// + i);
		        t3.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
		        gp.add(t3,0,0);
		        
		        Text t4 = new Text(String.format("Your Destination Station: " + getStationName(destination.get(i+1).toString().replaceAll("\"", ""), stationAbbr)));  
		        //t4.setTranslateX(10);
		        t4.setTranslateX(-10);
		        //t4.setTranslateY(-320 + (150 * i));// * + i);
		        //t4.setTranslateY(-150 + (150 * i));// * + i);
		        t4.setTranslateY(-135 + (140 * i));// * + i);
		        t4.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
		        gp.add(t4,0,0);
		         
		        Text t5 = new Text(String.format("Estimated Time of Arrival: " + destTime.get(i+1).toString().replaceAll("\"", "")));  
		        //t5.setTranslateX(10);
		        t5.setTranslateX(-10);
		        //t5.setTranslateY(-300 + (150 * i));//  + i);
		        //t5.setTranslateY(-130 + (150 * i));//  + i);
		        t5.setTranslateY(-115 + (140 * i));//  + i);
		        t5.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
		        gp.add(t5,0,0);	        
		        
		        Text lineText = (getLineNames(line.get(i).toString().replaceAll("\"", ""))); 
		        Text t6 = new Text("Line To Take: " );//
		        //t6.setTranslateX(10);
		        t6.setTranslateX(-10);
		        //lineText.setTranslateX(100);
		        lineText.setTranslateX(70);
		        lineText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 13));
		        //t6.setTranslateY(-280 + (150 * i));// + i);
		        //t6.setTranslateY(-110 + (150 * i));// + i);
		        t6.setTranslateY(-95 + (140 * i));// + i);
		        //lineText.setTranslateY(-110 + (150 * i));// + i);
		        lineText.setTranslateY(-95 + (140 * i));// + i);
		        t6.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
		        gp.add(t6,0,0);
		        gp.add(lineText, 0, 0);
		        
		        Text t8 = new Text("-----------------------------------------------------------------------------------------------------------------");  
		        //t8.setTranslateX(10);
		        t8.setTranslateX(-10);
		        //t8.setTranslateY(-270 + (150 * i));// + i);//(50 * i));
		        //t8.setTranslateY(-90 + (150 * i));// + i);//(50 * i));
		        t8.setTranslateY(-78 + (140 * i));
		        
		        t8.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		        gp.add(t8,0,0);
			}
			
			s11.setWidth(450);
	        s11.setHeight(450);
	        
	        Scene scene = new Scene(gp);
	        s11.setScene(scene);
	        s11.show(); 
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
	}
	
	public static Text getLineNames(String line)
	{
		Text lineName = new Text();
		switch(line)
		{
		case "ROUTE 1":
			lineName.setText("Antioch - San Francisco International Airport/Millbrae");
			lineName.setFill(Color.GOLD);
			return lineName;
			//return "Antioch - San Francisco International Airport/Millbrae";		
		
		case "ROUTE 2":
			lineName.setText("Millbrae/San Francisco International Airport - Antioch");
			lineName.setFill(Color.GOLD);
			return lineName;
			//return "Millbrae/San Francisco International Airport - Antioch";
		
		case "ROUTE 3":
			lineName.setText("Warm Springs/South Fremont - Richmond");
			lineName.setFill(Color.ORANGE);
			return lineName;
			//return "Warm Springs/South Fremont - Richmond";
		
		case "ROUTE 4":
			lineName.setText("Richmond - Warm Springs/South Fremont");
			lineName.setFill(Color.ORANGE);
			return lineName;
			//return "Richmond - Warm Springs/South Fremont";
		
		case "ROUTE 5":
			lineName.setText("Warm Springs/South Fremont - Daly City");
			lineName.setFill(Color.GREEN);
			return lineName;
			//return "Warm Springs/South Fremont - Daly City";
		
		case "ROUTE 6":
			lineName.setText("Daly City - Warm Springs/South Fremont");
			lineName.setFill(Color.GREEN);
			return lineName;
			//return "Daly City - Warm Springs/South Fremont";
		
		case "ROUTE 7":
			lineName.setText("Richmond - Daly City/Millbrae");
			lineName.setFill(Color.RED);
			return lineName;
			//return "Richmond - Daly City/Millbrae";
			
		case "ROUTE 8":
			lineName.setText("Millbrae/Daly City - Richmond");
			lineName.setFill(Color.RED);
			return lineName;
			//return "Millbrae/Daly City - Richmond";
		
		case "ROUTE 9":
			lineName.setText("Dublin/Pleasanton - MacArthur");
			lineName.setFill(Color.LIGHTSKYBLUE);
			return lineName;
		
		case "ROUTE 10":
			lineName.setText("MacArthur - Dublin/Pleasanton");
			lineName.setFill(Color.LIGHTSKYBLUE);
			return lineName;
		
		case "ROUTE 11":
			lineName.setText("Dublin/Pleasanton - Daly City");
			lineName.setFill(Color.LIGHTSKYBLUE);
			return lineName;
			//return "Dublin/Pleasanton - Daly City";
			
		case "ROUTE 12":
			lineName.setText("Daly City - Dublin/Pleasanton");
			lineName.setFill(Color.LIGHTSKYBLUE);
			return lineName;
			//return "Daly City - Dublin/Pleasanton";
		
		case "ROUTE 13":
			lineName.setText("Millbrae - SFO");
			lineName.setFill(Color.PURPLE);
			return lineName;
			//return "Daly City - Dublin/Pleasanton";
			
		case "ROUTE 14":
			lineName.setText("SFO - Millbrae");
			lineName.setFill(Color.PURPLE);
			return lineName;
			
		case "ROUTE 19":
			lineName.setText("Coliseum - Oakland Int'l Airport");
			lineName.setFill(Color.GOLDENROD);
			return lineName;
			//return "Coliseum - Oakland Int'l Airport";
			
		case "ROUTE 20":
			lineName.setText("Oakland Int'l Airport - Coliseum");
			lineName.setFill(Color.GOLDENROD);
			return lineName;
			//return "Oakland Int'l Airport - Coliseum";	
		default:
			return null;
		}
	}
	
	public static String getLineName(String line)
	{
		switch(line)
		{
		case "ROUTE 1":
			return "Antioch - San Francisco International Airport/Millbrae";		
		
		case "ROUTE 2":
			return "Millbrae/San Francisco International Airport - Antioch";
		
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
		
		case "ROUTE 9":
			return "Dublin/Pleasanton - MacArthur";
		
		case "ROUTE 10":
			return "MacArthur - Dublin/Pleasanton";
			
		case "ROUTE 11":
			return "Dublin/Pleasanton - Daly City";
			
		case "ROUTE 12":
			return "Daly City - Dublin/Pleasanton";
		
		case "ROUTE 13":
			return "Millbrae - SFO";
		
		case "ROUTE 14":
			return "SFO - Millbrae";
		
		case "ROUTE 19":
			return "Coliseum - Oakland Int'l Airport";
			
		case "ROUTE 20":
			return "Oakland Int'l Airport - Coliseum";	
			
		default:
			return "";
		}
	}

}
