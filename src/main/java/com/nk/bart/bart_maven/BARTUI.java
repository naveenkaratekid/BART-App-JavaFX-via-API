package com.nk.bart.bart_maven;

import static org.junit.Assert.*;

import javafx.stage.*;
/*import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;*/
import javafx.application.*;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/*import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;*/
import javafx.event.*;
import javafx.geometry.*;
import javafx.beans.Observable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.text.*;
import javafx.scene.image.*;

import javafx.scene.input.ScrollEvent;

import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import org.testfx.framework.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.BARTAPICall;
//import com.Clock;

/*import javafx.util.*;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.stage.*;
import java.time.LocalTime;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.animation.*;*/

import com.nk.bart.bart_maven.BARTAPICall;

@SuppressWarnings("unchecked")

public class BARTUI extends Application {

	Label clock = new Clock();
	Label ls = new Label();
	String line = "";
	final DoubleProperty zoomProperty = new SimpleDoubleProperty(250);
	Text txt = new Text();
	Text color = new Text();
	
	public void start(Stage s)
	{
		try
		{
			// TODO Auto-generated method stub
			GridPane gp = new GridPane();
			
			ImageView iv = new ImageView();
			iv.setImage(new Image(getClass().getResourceAsStream("Untitled19.png")));
			iv.setScaleX(0.75);
			iv.setScaleY(0.75);
			iv.setTranslateX(300);
			iv.setTranslateY(-145);
			iv.setVisible(true);
			gp.add(iv, 0, 0);
			gp.setAlignment(Pos.CENTER);
			clock.setVisible(true);
		    clock.setTextFill(Color.BLACK);
		    clock.setFont(new Font("Arial", 20));
		    clock.setTranslateX(0);
		    clock.setTranslateY(-145);
		    gp.add(clock, 0, 0);
		    
		    TabPane tp = new TabPane();
		    tp.setVisible(true);
		    tp.setPrefWidth(412);		   
		    tp.setTranslateX(0);
		    //tp.setTranslateY(47);	
		    tp.setTranslateY(52);
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
			//origin.setTranslateY(-50);
			origin.setTranslateY(-48);		
			
			destination.setTranslateX(40);
			//destination.setTranslateY(0);
			destination.setTranslateY(5);
			
			gp.add(origin,0,0);
			gp.add(destination, 0, 0);
			
		    BARTAPICall bac = new BARTAPICall();
			Map<String, String> mapOfStations = bac.getStationList();
		    
			MenuButton menuListOfStartStations = new MenuButton("Select Start Station");
			menuListOfStartStations.setTranslateX(150);
			menuListOfStartStations.setTranslateY(-50);
			menuListOfStartStations.setMinWidth(200);
			menuListOfStartStations.setMaxWidth(150);
			
			MenuButton menuListOfEndStations = new MenuButton("Select Dest. Station");
			menuListOfEndStations.setTranslateX(150);
			//menuListOfEndStations.setTranslateY(0);
			menuListOfEndStations.setTranslateY(5);
			menuListOfEndStations.setMinWidth(200);
			menuListOfEndStations.setMaxWidth(100);
			
			MenuButton menuListOfStationsInfo = new MenuButton("Select Station for Info");
			//menuListOfStationsInfo.setTranslateX(100);
			menuListOfStationsInfo.setTranslateX(150);
			menuListOfStationsInfo.setTranslateY(-50);
			menuListOfStationsInfo.setMinWidth(200);
			menuListOfStationsInfo.setMaxWidth(100);
			
			Text stationInformation = new Text();
			stationInformation.setTranslateX(40);
			//stationInformation.setTranslateY(80);
			stationInformation.setTranslateY(85);
			stationInformation.setFont(new Font("Arial", 15));
			stationInformation.setVisible(true);
			gp.getChildren().add(stationInformation);

			//gp.add(txt, 0, 0);
			
			//Text color = new Text();
			color.setTranslateY(120);
			color.setTranslateX(40);
			color.setVisible(true);
			//gp.add(color, 0, 0);
			
			/*Text address = new Text("Address: ");
			address.setTranslateX(40);
			address.setTranslateY(-50);
			//gp.add(address, 0, 0);
			
			Text linesServed = new Text("Lines served at this station: ");
			linesServed.setTranslateX(40);
			linesServed.setTranslateY(0);*/
			
			//gp.add(linesServed, 0, 0);
			
			for(Map.Entry<String, String> m1: mapOfStations.entrySet())
			{
				MenuItem listOfStations = new MenuItem();
				
				String value = m1.getValue().replaceAll("\"", "");
				
				String[] stationName = value.trim().split("\\| ");
				listOfStations.setText(stationName[0]);
				menuListOfStationsInfo.getItems().add(listOfStations);
				
				Set<String> list = new HashSet<String>();
				String abbr = bac.getStationAbbr(listOfStations.getText(), mapOfStations);
				//String routeStr = bac.getRouteNumber(abbr).trim().replace("[", "").replaceAll("]", "").replace("\"", "").replaceAll(", ", ",");
				String routeStr = bac.getRouteNumber(abbr);
				//System.out.println(abbr);
				routeStr = routeStr.trim().replace("[", "").replaceAll("]", "").replaceAll("\"", "").replace(", ", ",");
				//System.out.println(routeStr);
				String[] route = routeStr.split(",");
				
				
				listOfStations.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent event) 
					{
						
						
						menuListOfStationsInfo.setText(listOfStations.getText());
						for(int i = 0; i < route.length; i++)
						{	
							txt = bac.getLineNames(route[i]);
							list.add(txt.getText().toString());														
							String lines = list.toString().trim().replace("[", "").replace("]", "").replaceAll(",", "\n").trim();
							stationInformation.setText("Address: " + (stationName[1].contains("Level 3") ? "International Terminal,\n\t\tSan Francisco Int'l Airport CA 94128" : stationName[1]) + "\n\n------------------------------------------\n\nLines served at this station: \n\n" + lines);// + lines);
							//color.setText("------------------------------------------\n" + lines);				
						}	
					}					
				});				
			}
			gp.getChildren().addAll(menuListOfStationsInfo);
			
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
			Button search = new Button("Plan this Trip");
		    search.setTranslateX(175);
		    search.setTranslateY(50);
		    gp.add(search, 0, 0);
		    
		    /* Button to perform the search for the real time updates*/
			Button searchForRealTime = new Button("Get Real Time");
			searchForRealTime.setTranslateX(175);
			searchForRealTime.setTranslateY(50);
		    gp.add(searchForRealTime, 0, 0);
		    
		    MenuButton menuListOfStationsRealTime = new MenuButton("Select Station");
		    menuListOfStationsRealTime.setTranslateX(150);
		    menuListOfStationsRealTime.setTranslateY(-50);
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
			
			
			// Real time updates
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

			GridPane layout = new GridPane();
	        //GridPane layout = createScrollLayout();
			InputStream is = getClass().getResourceAsStream("BART_cc_map_ weekday_saturday.png");
	        //Image img = new Image("file:./BART_cc_map.png");
	        Image img = new Image(is);  	    
	        
	        Calendar calendar = Calendar.getInstance();
	        
	        LocalDate date = LocalDate.now();
	        
	        DayOfWeek dow = date.getDayOfWeek();
	        System.out.println(dow.toString());
	        if(dow.equals(DayOfWeek.SUNDAY))
	        {
	        		is = getClass().getResourceAsStream("BART_cc_map_sunday.png");
	        }
	        
	        
	        
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
	        
	        //scroll.setTranslateX(25);	
	        scroll.setTranslateX(35);	 
	        //scroll.setTranslateY(83);
	        scroll.setTranslateY(90);
	        
	        layout.getChildren().addAll(imageView);
	       
	        System.out.println();

	        zoomProperty.addListener(new InvalidationListener() 
	        {
	            public void invalidated(Observable arg0) 
	            {
       
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
		    //root.getChildren().addAll(vbox);
		    //root.getChildren().addAll(iv);
		    root.setTranslateY(-10);
		    s.setWidth(450);
		    s.setHeight(455);
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
