package com;

import javafx.stage.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.*;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.geometry.*;
import javafx.beans.Observable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.beans.*;
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
	Label ls = new Label();
   
	final DoubleProperty zoomProperty = new SimpleDoubleProperty(250);

	
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
		    clock.setTranslateX(85);
		    clock.setTranslateY(-150);
		    gp.add(clock, 0, 0);
		    
		    TabPane tp = new TabPane();
		    tp.setVisible(true);
		    tp.setPrefWidth(412);		   
		    tp.setTranslateX(0);
		    tp.setTranslateY(47);		    
		    gp.add(tp, 0, 0);	    	
		    
		    Tab tripPlanner = new Tab();
		    tripPlanner.setText("Trip Planner");
		    tripPlanner.setClosable(false);	

		    Tab systemMap = new Tab();
		    systemMap.setText("System Map");
		    systemMap.setClosable(false);	
		    
		    Tab realTime = new Tab();
		    realTime.setText("Real Time Updates");
		    realTime.setClosable(false);	
		    
		    Tab stationInfoTab = new Tab();
		    stationInfoTab.setText("Station Info");
		    stationInfoTab.setClosable(false);	
		   
		    
		    Label origin = new Label("Origin"), destination = new Label("Destination");
			origin.setFont(new Font("Arial", 20));
			destination.setFont(new Font("Arial", 20));
			
			origin.setTranslateX(87);
			origin.setTranslateY(0);
					
			destination.setTranslateX(40);
			destination.setTranslateY(80);
			
			gp.add(origin,0,0);
			gp.add(destination, 0, 0);
			
		    BARTAPICall bac = new BARTAPICall();
			Map<String, String> mapOfStations = bac.getStationList();
		    
			MenuButton menuListOfStartStations = new MenuButton("Select Start Station");
			menuListOfStartStations.setTranslateX(150);
			menuListOfStartStations.setTranslateY(0);
			menuListOfStartStations.setMinWidth(200);
			menuListOfStartStations.setMaxWidth(150);
			
			MenuButton menuListOfEndStations = new MenuButton("Select Dest. Station");
			menuListOfEndStations.setTranslateX(150);
			menuListOfEndStations.setTranslateY(80);
			menuListOfEndStations.setMinWidth(200);
			menuListOfEndStations.setMaxWidth(100);
			
			MenuButton menuListOfStationsInfo = new MenuButton("Select Station for Info");
			menuListOfStationsInfo.setTranslateX(150);
			menuListOfStationsInfo.setTranslateY(0);
			menuListOfStationsInfo.setMinWidth(200);
			menuListOfStationsInfo.setMaxWidth(100);
			
			Label stationInformation = new Label();
			stationInformation.setTranslateX(50);
			stationInformation.setTranslateY(100);
			stationInformation.setFont(new Font("Arial", 15));
			stationInformation.setVisible(true);
			gp.getChildren().add(stationInformation);
			
			try
			{
				for(Map.Entry<String, String> m1: mapOfStations.entrySet())
				{
					
					MenuItem station = new MenuItem();
					String stationInfo = m1.getValue();
					String key = m1.getKey();
					//System.out.println(key);
					String[] item = stationInfo.split("\\| ");
					
					for(int i = 0; i < item.length; i++)
					{		
						
						String st = item[1].replaceAll("\"", "");
						String st1 = item[i++].replaceAll("\"", "").trim();
						station.setText(st1);
						station.setOnAction(new EventHandler<ActionEvent>()
						{
							public void handle(ActionEvent ae)
							{
								String line = "";
								menuListOfStationsInfo.setText(station.getText());
								stationInformation.setText(st);
								try
								{			
									String abbr = bac.getStationAbbr(station.getText(), mapOfStations);
									String routeStr = bac.getRouteNumber(abbr).trim().replace("[", "").replaceAll("]", "").replace("\"", "").replaceAll(", ", ",");
									String[] route = routeStr.split(",");
											
									for(int i = 0; i < route.length; i++)
									{	
										Label ls = new Label();
										line = bac.getLineName(route[i]);
										ls.setVisible(true);
										ls.setText(line + "\n");
										ls.setTranslateX(50);
										ls.setTranslateY(100 + (15 * i));
										gp.add(ls, 0, 0);
										
									}
									//gp.add(ls, 0, 0);
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}							
							}
						});	
					}
					menuListOfStationsInfo.getItems().add(station);
				}
				gp.getChildren().addAll(menuListOfStationsInfo);
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			// Start Stations
			try
			{
				
				for(Map.Entry<String, String> m1: mapOfStations.entrySet())
				{
					MenuItem station = new MenuItem();
					String stationInfo = m1.getValue();
					String[] item = stationInfo.split("\\| ");
					
					for(int i = 0; i < item.length; i+=2)
					{						
						String st = item[i].replaceAll("\"", "");
						station.setText(st);
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
				gp.getChildren().addAll(menuListOfStartStations);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			//////
			
			
			/* Button to perform the search for the trip Planner*/
			Button search = new Button("Search");
		    search.setTranslateX(100);
		    search.setTranslateY(125);
		    gp.add(search, 0, 0);
		    
		    /* Button to perform the search for the real time updates*/
			Button searchForRealTime = new Button("Search");
			searchForRealTime.setTranslateX(90);
			searchForRealTime.setTranslateY(50);
		    gp.add(searchForRealTime, 0, 0);
		    
		    MenuButton menuListOfStationsRealTime = new MenuButton("Select Station");
		    menuListOfStationsRealTime.setTranslateX(150);
		    menuListOfStationsRealTime.setTranslateY(0);
		    menuListOfStationsRealTime.setMinWidth(200);
		    menuListOfStationsRealTime.setMaxWidth(100);
		    //-----------------------------------------------------------//
		    // List of Stations
			try
			{
				for(Map.Entry<String, String> m1: mapOfStations.entrySet())
				{
					MenuItem station = new MenuItem();
					String stationInfo = m1.getValue();
					String[] item = stationInfo.split("\\| ");
					for(int i = 0; i < item.length; i+=2)
					{						
						String st = item[i].replaceAll("\"", "");
						station.setText(st);
						station.setOnAction(new EventHandler<ActionEvent>()
						{
							public void handle(ActionEvent ae)
							{
								menuListOfStationsRealTime.setText(station.getText());
							}
						});	
					}
					menuListOfStationsRealTime.getItems().add(station);
				}
				gp.getChildren().addAll(menuListOfStationsRealTime);				
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
	    					String str = menuListOfStationsRealTime.getText().trim();
		    				String abbr = bac.getStationAbbr(str, mapOfStations);
		    				bac.getRealTime(abbr);
	    				}
	    				catch(Exception e)
	    				{
	    					e.printStackTrace();
	    				}	    				
	    			}
	    		});
			
			//-----------------------------------------------------------//
			// List of End Stations
			try
			{	
				for(Map.Entry<String, String> m1: mapOfStations.entrySet())
				{
					MenuItem station = new MenuItem();
					String stationInfo = m1.getValue();
					String[] item = stationInfo.split("\\| ");
					for(int i = 0; i < item.length; i+=2)
					{						
						String st = item[i].replaceAll("\"", "");
						station.setText(st);	
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
				gp.getChildren().addAll(menuListOfEndStations);
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			////////////////////

			
			/////////////////
			GridPane layout = new GridPane();
	        //GridPane layout = createScrollLayout();
	        Image img = new Image("file:./BART_cc_map.png");
	          	    
	        // wrap the scene contents in a pannable scroll pane.
	        ScrollPane scroll = new ScrollPane(layout);
	        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); 
	        scroll.setPannable(true);

	        ImageView imageView = new ImageView();
	        imageView.setImage(img);
	        imageView.setFitWidth(350);
	        imageView.setFitHeight(350);
	        
	        scroll.setContent(imageView);
	        
	       
	        scroll.setMinWidth(350);
	        scroll.setMaxWidth(350);
	        scroll.setMinHeight(350);
	        scroll.setMaxHeight(350);
	        
	       // scroll.setPrefHeight(500);
	        
	        scroll.setTranslateX(50);	        
	        scroll.setTranslateY(83);
	        
	       
	        layout.getChildren().addAll(imageView);
	        System.out.println();

	        zoomProperty.addListener(new InvalidationListener() 
	        {
	            public void invalidated(Observable arg0) 
	            {
	                
	                //System.out.println(imageView.getFitWidth());
	                //System.out.println(imageView.getFitHeight());
	                
	                imageView.setFitWidth(zoomProperty.get());// * 3);
	                imageView.setFitHeight(zoomProperty.get());// * 3);
	                	scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	    	        		scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); 
	                
	                if(imageView.getFitHeight() < 350)
	                {
	                    imageView.setFitHeight(350);
	                }
	                
	                if(imageView.getFitWidth() < 350)
	                {
	                    imageView.setFitWidth(350);
	                }
	                
	                if(imageView.getFitWidth() > 2000)
	                {
	                    imageView.setFitWidth(2000);
	                }
	                
	                if(imageView.getFitHeight() > 2000)
	                {
	                    imageView.setFitHeight(2000);
	                }
	            }
	         }
	        );
	        
	        scroll.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() 
	        {
	            
	            public void handle(ScrollEvent event) 
	            {
	                if (event.getDeltaY() > 0) {
	                    zoomProperty.set(zoomProperty.get() * 1.1);
	                } else if (event.getDeltaY() < 0) {
	                    zoomProperty.set(zoomProperty.get() / 1.1);
	                }
	            }
	        }
	        );
	        // center the scroll contents.
	        scroll.setHvalue(scroll.getHmin() + (scroll.getHmax() - scroll.getHmin()) / 2);
	        scroll.setVvalue(scroll.getVmin() + (scroll.getVmax() - scroll.getVmin()) / 2);
			//gp.add(layout, 0, 0);
			//gp.getChildren().addAll(scroll);
			gp.add(scroll, 0, 0);
			
			
			//-----------------------------------------------------------//
			// Trip Planner tab
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
						menuListOfStationsRealTime.setVisible(false);
						stationInformation.setVisible(false);
						menuListOfStationsInfo.setVisible(false);
						scroll.setVisible(false);
						ls.setVisible(false);
						
						
					}
					else if(tripPlanner.isSelected())
					{
						menuListOfStartStations.setVisible(true);
						menuListOfEndStations.setVisible(true);
						origin.setVisible(true);
						destination.setVisible(true);
						search.setVisible(true);
						searchForRealTime.setVisible(false);
						menuListOfStationsRealTime.setVisible(false);
						stationInformation.setVisible(false);
						menuListOfStationsInfo.setVisible(false);
						//iv.setVisible(false);
						scroll.setVisible(false);
						
						ls.setVisible(false);
						
					}
				}
			}
			);

			//-----------------------------------------------------------//
			// system map
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
						menuListOfStationsRealTime.setVisible(false);
						stationInformation.setVisible(false);
						//mp.setVisible(false);
						//iv.setVisible(false);
						scroll.setVisible(false);
						ls.setVisible(false);
						
	    				}
	    				
	    				else if(systemMap.isSelected()) 
	    				{
						menuListOfStartStations.setVisible(false);
						menuListOfEndStations.setVisible(false);
						origin.setVisible(false);
						destination.setVisible(false);
						search.setVisible(false);
						searchForRealTime.setVisible(false);
						menuListOfStationsRealTime.setVisible(false);
						stationInformation.setVisible(false);
						//mp.setVisible(true);
						scroll.setVisible(true);
						ls.setVisible(false);
						
						
						
	    				}	
	    			}
    			});
		    
		    //-----------------------------------------------------------//
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
						menuListOfStationsRealTime.setVisible(false);
						menuListOfStationsInfo.setVisible(false);
						stationInformation.setVisible(false);
						//iv.setVisible(false);
						ls.setVisible(false);
						scroll.setVisible(false);
						
						
	    					
	    				}
	    				else if(realTime.isSelected()) 
	    				{
	    					menuListOfStartStations.setVisible(false);
						menuListOfEndStations.setVisible(false);
						origin.setVisible(false);
						destination.setVisible(false);
						search.setVisible(false);
						searchForRealTime.setVisible(true);
						menuListOfStationsRealTime.setVisible(true);
						menuListOfStationsInfo.setVisible(false);
						stationInformation.setVisible(false);
						//iv.setVisible(false);
						ls.setVisible(false);
						scroll.setVisible(false);
						
	    				}	    				
	    			}
	    		});
		    
		    //-----------------------------------------------------------//		    
		    stationInfoTab.setOnSelectionChanged(new EventHandler<Event>()
	    		{
	    			public void handle(Event e)
	    			{
	    				if(!stationInfoTab.isSelected())
	    				{
	    					menuListOfStationsInfo.setVisible(false);
	    					menuListOfStartStations.setVisible(false);
						menuListOfEndStations.setVisible(false);
						origin.setVisible(false);
						destination.setVisible(false);
						search.setVisible(false);
						searchForRealTime.setVisible(false);
						menuListOfStationsRealTime.setVisible(false);
						menuListOfStationsInfo.setVisible(false);
						stationInformation.setVisible(false);
						//iv.setVisible(false);
						ls.setVisible(false);
						scroll.setVisible(false);
	    				}
	    				else if(stationInfoTab.isSelected())
	    				{
	    					menuListOfStationsInfo.setVisible(true);
	    					menuListOfStartStations.setVisible(false);
						menuListOfEndStations.setVisible(false);
						origin.setVisible(false);
						destination.setVisible(false);
						search.setVisible(false);
						searchForRealTime.setVisible(false);
						menuListOfStationsRealTime.setVisible(false);
						stationInformation.setVisible(true);
						//iv.setVisible(false);
						ls.setVisible(true);
						scroll.setVisible(false);
						
	    				}
	    			}
	    		});

		    tp.getTabs().addAll(tripPlanner, systemMap, realTime, stationInfoTab);
		    //-------------------------------------------------------------------//
		    // Search Button
		    search.setOnAction(new EventHandler<ActionEvent>()
	    		{
	    			public void handle(ActionEvent ae)
	    			{
	    				try
	    				{
	    					for(Map.Entry<String, String> m1: mapOfStations.entrySet())
		    				{
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
			    			        gp.setHgap(5);
			    			        gp.setVgap(5);
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
			    			        gp.setHgap(5);
			    			        gp.setVgap(5);
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
		    						
		    						//s.close();
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
		    
		    VBox root = new VBox(gp);
		    //root.getChildren().addAll(iv);
		    root.setTranslateY(-10);
		    s.setWidth(450);
		    s.setHeight(450);
		    s.show();
		    Scene scene = new Scene(root);
		   
		    //root.getChildren().addAll(gp);
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
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		System.out.println("Working dir:  " + System.getProperty("user.dir"));
		
        launch(args);
		// TODO Auto-generated method stub
		
	}

}
