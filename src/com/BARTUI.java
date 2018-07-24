package com;

import javafx.stage.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.util.*;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.stage.*;
import java.time.LocalTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.animation.*;

@SuppressWarnings("unchecked")

public class BARTUI extends Application {

	Label clock = new Clock();
	
	
	@Override
	public void start(Stage s)
	{
		try
		{
			// TODO Auto-generated method stub
			GridPane gp = new GridPane();
			gp.setAlignment(Pos.CENTER);
			clock.setVisible(true);
		    clock.setTextFill(Color.BLACK);
		    clock.setFont(new Font("Arial", 20));
		    clock.setTranslateX(50);
		    clock.setTranslateY(0);
		    gp.add(clock, 0, 0);
		    
		    TabPane tp = new TabPane();
		    tp.setVisible(true);
		    
		    tp.setPrefWidth(412);
		   
		    tp.setTranslateX(10);
		    tp.setTranslateY(50);
		    
		    gp.add(tp, 0, 0);
		    
		     
		    Tab tripPlanner = new Tab();
		    tripPlanner.setText("Trip Planner");
		    tripPlanner.setClosable(false);	

		    Label origin = new Label("Origin"), destination = new Label("Destination");
			origin.setFont(new Font("Arial", 20));
			destination.setFont(new Font("Arial", 20));
			
			origin.setTranslateX(50);
			origin.setTranslateY(100);
					
			destination.setTranslateX(40);
			destination.setTranslateY(250);
			
			gp.add(origin,0,0);
			gp.add(destination, 0, 0);
			
		    BARTAPICall bac = new BARTAPICall();
			Map<String, String> mapOfStations = bac.getStationList();
		    
			MenuButton menuListOfStartStations = new MenuButton("Select Start Station");
			menuListOfStartStations.setTranslateX(150);
			menuListOfStartStations.setTranslateY(100);
			menuListOfStartStations.setMinWidth(200);
			menuListOfStartStations.setMaxWidth(100);
			
			try
			{
				
				for(Map.Entry<String, String> m1: mapOfStations.entrySet())
				{
					MenuItem station = new MenuItem();
					String stationInfo = m1.getValue();
					//String stationAbbr = m1.getKey();
					//System.out.println(stationAbbr);
					//System.out.println(stationInfo);
					String[] item = stationInfo.split("\\| ");
					//System.out.println(item[1]);
					for(int i = 0; i < item.length; i+=2)
					{
						
						String st = item[i].replaceAll("\"", "");
						station.setText(st);
						//menuListOfStartStations.getItems().add(station);
						station.setOnAction(new EventHandler<ActionEvent>()
						{
							public void handle(ActionEvent ae)
							{
								menuListOfStartStations.setText(station.getText());
							}
						});
						
					}
					menuListOfStartStations.getItems().add(station);
				}
				//menuListOfStartStations.getItems().add(station);
				gp.getChildren().addAll(menuListOfStartStations);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			//////
			MenuButton menuListOfEndStations = new MenuButton("Select Dest. Station");
			menuListOfEndStations.setTranslateX(150);
			menuListOfEndStations.setTranslateY(250);
			menuListOfEndStations.setMinWidth(200);
			menuListOfEndStations.setMaxWidth(100);

			
			/* Button to perform the search for the trip Planner*/
			Button search = new Button("Search");
		    search.setTranslateX(100);
		    search.setTranslateY(300);
		    gp.add(search, 0, 0);
		    
		    /* Button to perform the search for the real time updates*/
			Button searchForRealTime = new Button("Search");
			searchForRealTime.setTranslateX(90);
			searchForRealTime.setTranslateY(150);
		    gp.add(searchForRealTime, 0, 0);
			
		    
		    
		    MenuButton menuListOfStations = new MenuButton("Select Station");
		    menuListOfStations.setTranslateX(150);
		    menuListOfStations.setTranslateY(100);
		    menuListOfStations.setMinWidth(200);
		    menuListOfStations.setMaxWidth(100);
		    
			try
			{
				for(Map.Entry<String, String> m1: mapOfStations.entrySet())
				{
					MenuItem station = new MenuItem();
					String stationInfo = m1.getValue();
					//String stationAbbr = m1.getKey();
					//System.out.println(stationAbbr);
					String[] item = stationInfo.split("\\| ");
					for(int i = 0; i < item.length; i+=2)
					{						
						String st = item[i].replaceAll("\"", "");
						station.setText(st);
						//menuListOfStartStations.getItems().add(station);
						station.setOnAction(new EventHandler<ActionEvent>()
						{
							public void handle(ActionEvent ae)
							{
								menuListOfStations.setText(station.getText());
							}
						});	
					}
					menuListOfStations.getItems().add(station);
				}
				//menuListOfStartStations.getItems().add(station);
				gp.getChildren().addAll(menuListOfStations);
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			searchForRealTime.setOnAction(new EventHandler<ActionEvent>()
    		{
    			public void handle(ActionEvent ae)
    			{
    				try
    				{
    					String str = menuListOfStations.getText().trim();
	    				String abbr = bac.getStationAbbr(str, mapOfStations);
	    				bac.getRealTimeEstimates(abbr);
    				}
    				catch(Exception e)
    				{
    					e.printStackTrace();
    				}
    				
    			}
    		});
			
			
			try
			{
				
				for(Map.Entry<String, String> m1: mapOfStations.entrySet())
				{
					MenuItem station = new MenuItem();
					String stationInfo = m1.getValue();
					//String stationAbbr = m1.getKey();
					//System.out.println(stationAbbr);
					String[] item = stationInfo.split("\\| ");
					for(int i = 0; i < item.length; i+=2)
					{						
						String st = item[i].replaceAll("\"", "");
						station.setText(st);
						//menuListOfStartStations.getItems().add(station);
						station.setOnAction(new EventHandler<ActionEvent>()
						{
							public void handle(ActionEvent ae)
							{
								menuListOfEndStations.setText(station.getText());
							}
						});	
					}
					menuListOfEndStations.getItems().add(station);
				}
				//menuListOfStartStations.getItems().add(station);
				gp.getChildren().addAll(menuListOfEndStations);
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			tripPlanner.setOnSelectionChanged(new EventHandler<Event>() 
			{
				public void handle(Event e)
				{
					if(!tripPlanner.isSelected())
					{
						menuListOfStartStations.setVisible(false);
						menuListOfEndStations.setVisible(false);
						origin.setVisible(false);
						destination.setVisible(false);
						search.setVisible(false);
						searchForRealTime.setVisible(false);
						menuListOfStations.setVisible(false);
						
					}
					else if(tripPlanner.isSelected())
					{
						menuListOfStartStations.setVisible(true);
						menuListOfEndStations.setVisible(true);
						origin.setVisible(true);
						destination.setVisible(true);
						search.setVisible(true);
						searchForRealTime.setVisible(false);
						menuListOfStations.setVisible(false);
						
					}
				}
			}
			);

		    Tab systemMap = new Tab();
		    systemMap.setText("System Map");
		    systemMap.setClosable(false);	
		    
		    systemMap.setOnSelectionChanged(new EventHandler<Event>()
    			{
	    			public void handle(Event e)
	    			{
	    				if(!systemMap.isSelected()) 
	    				{
	    					menuListOfStartStations.setVisible(false);
						menuListOfEndStations.setVisible(false);
						origin.setVisible(false);
						destination.setVisible(false);
						search.setVisible(false);
						searchForRealTime.setVisible(false);
						menuListOfStations.setVisible(false);
	    				}
	    				
	    				else if(systemMap.isSelected()) 
	    				{
						menuListOfStartStations.setVisible(false);
						menuListOfEndStations.setVisible(false);
						origin.setVisible(false);
						destination.setVisible(false);
						search.setVisible(false);
						searchForRealTime.setVisible(false);
						menuListOfStations.setVisible(false);
	    				}
	    			}
    			});
		    
		    
		    Tab realTime = new Tab();
		    realTime.setText("Real Time Updates");
		    realTime.setClosable(false);	
		    
		    realTime.setOnSelectionChanged(new EventHandler<Event>()
	    		{
	    			public void handle(Event e)
	    			{
	    				if(!realTime.isSelected()) 
	    				{
	    					menuListOfStartStations.setVisible(false);
						menuListOfEndStations.setVisible(false);
						origin.setVisible(false);
						destination.setVisible(false);
						search.setVisible(false);
						searchForRealTime.setVisible(false);
						menuListOfStations.setVisible(false);
	    					
	    				}
	    				else if(realTime.isSelected()) 
	    				{
	    					menuListOfStartStations.setVisible(false);
						menuListOfEndStations.setVisible(false);
						origin.setVisible(false);
						destination.setVisible(false);
						search.setVisible(false);
						searchForRealTime.setVisible(true);
						menuListOfStations.setVisible(true);
	    				}	    				
	    			}
	    		});
		    

		    Tab stationInfo = new Tab();
		    stationInfo.setText("Station Info");
		    stationInfo.setClosable(false);	
		    
		    tp.getTabs().addAll(tripPlanner, systemMap, realTime, stationInfo);

		    
		    
		    
		    search.setOnAction(new EventHandler<ActionEvent>()
	    		{
	    			public void handle(ActionEvent ae)
	    			{
	    				try
	    				{
	    					for(Map.Entry<String, String> m1: mapOfStations.entrySet())
		    				{
		    					/*String startKey = m1.getKey().replaceAll("\"", "");
		    					String startValue = m1.getValue().replaceAll("\"", "");
		    					
		    					String endKey = m1.getKey().replaceAll("\"", "");
		    					String endValue = m1.getValue().replaceAll("\"", "");*/
		    					
		    					if(menuListOfStartStations.getText().equals("Select Start Station"))
		    					{
		    						Stage s11 = new Stage();
			    			    		s11.setResizable(false);
			    			    		GridPane gp = new GridPane();
			    			    		gp.setAlignment(Pos.CENTER);
			    			        Text t = new Text("You must select the origin");
			    			        t.setTranslateX(10);
			    			        t.setTranslateY(10);
			    			        t.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
			    			        gp.add(t,0,0);
			    			         
			    			        s11.setWidth(500);
			    			        s11.setHeight(100);
			    			        Scene scene = new Scene(gp);
			    			        s11.setScene(scene);
			    			        s11.show();
			    			        break;
			    			        //s11.close();
		    					}
		    					
		    					else if(menuListOfEndStations.getText().equals("Select Dest. Station"))
		    					{
		    						Stage s11 = new Stage();
			    			    		s11.setResizable(false);
			    			    		GridPane gp = new GridPane();
			    			    		gp.setAlignment(Pos.CENTER);
			    			        gp.setHgap(20);
			    			        gp.setVgap(20);
			    			        gp.setPadding(new Insets(25,25,25,25));   
			    			        Text t = new Text("You must select the destination");
			    			        t.setTranslateX(10);
			    			        t.setTranslateY(10);
			    			        t.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
			    			        gp.add(t,0,0);
			    			         
			    			        s11.setWidth(500);
			    			        s11.setHeight(100);
			    			        Scene scene = new Scene(gp);
			    			        s11.setScene(scene);
			    			        s11.show(); 
			    			        //s11.close();
			    			        break;
		    					}
		    					
		    					else if(menuListOfStartStations.getText().equals("Select Start Station") && menuListOfEndStations.getText().equals("Select Dest. Station"))
		    					{
		    						Stage s11 = new Stage();
			    			    		s11.setResizable(false);
			    			    		GridPane gp = new GridPane();
			    			    		gp.setAlignment(Pos.CENTER);
			    			        gp.setHgap(20);
			    			        gp.setVgap(20);
			    			        gp.setPadding(new Insets(25,25,25,25));   
			    			        Text t = new Text("You must select the origin and destination");
			    			        t.setTranslateX(10);
			    			        t.setTranslateY(10);
			    			        t.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
			    			        gp.add(t,0,0);
			    			         
			    			        s11.setWidth(500);
			    			        s11.setHeight(100);
			    			        Scene scene = new Scene(gp);
			    			        s11.setScene(scene);
			    			        s11.show(); 
			    			        //s11.close();
			    			        break;
		    					}
		    					else
		    					{
		    						//System.out.println(menuListOfStartStations.getText());
		    						String stationNameStart = menuListOfStartStations.getText().trim();		
		    						String stationNameEnd = menuListOfEndStations.getText().trim();
		    						//System.out.println(stationNameStart + stationNameEnd);
		    						String start = bac.getStationAbbr(stationNameStart, mapOfStations);
		    						String end = bac.getStationAbbr(stationNameEnd, mapOfStations);
		    						
		    						s.close();
		    						bac.getTripInfo(start, end, mapOfStations);
		    						break;
		    					}
		    				}
	    				}
	    				catch(Exception e)
	    				{
	    					e.printStackTrace();
	    				}
	    				
	    			}
	    		});
		    
		    
		    VBox root = new VBox(10);
		    
		    s.setWidth(500);
		    s.setHeight(400);
		    s.show();
		    Scene scene = new Scene(root);
		   
		    root.getChildren().addAll(gp);
		    //root.setId("root");
		    
		    s.setScene(scene);
		    //s.setMaximized(true);
		   
		    // This piece of code will kill any running threads after the application is terminated
		        s.setOnCloseRequest(new EventHandler<WindowEvent>()
		        {
		            public void handle(WindowEvent we)
		            {
		                Platform.exit();
		                System.exit(0);
		            }
		        }
		        );

		}
		catch(Exception e)
		{
			
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
