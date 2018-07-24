package com;

import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.*;
public class TripPlannerHandler implements EventHandler<Event> {


	@Override
	public void handle(Event event) {
		// TODO Auto-generated method stub
		
		BorderPane bp = new BorderPane();
		
		MenuButton menuListOfStartStations = new MenuButton("Select Starting Station");
		menuListOfStartStations.setTranslateX(10);
		menuListOfStartStations.setTranslateY(10);
		//bp.getChildren().add(menuListOfStartStations);
		BARTAPICall bac = new BARTAPICall();
		
		try
		{
			Map<String, String> mapOfStations = bac.getStationList();
			for(Map.Entry<String, String> m1: mapOfStations.entrySet())
			{
				String stationInfo = m1.getValue();
				System.out.println(stationInfo);
				String[] item = stationInfo.split("|");
				for(int i = 0; i < item.length; i++)
				{
					System.out.println(item[i]);
					menuListOfStartStations.setText(item[i]);
				}
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
