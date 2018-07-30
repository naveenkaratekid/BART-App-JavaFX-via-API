package com;

import javafx.scene.layout.GridPane;
import javafx.application.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
public class BartMap extends GridPane {

	final DoubleProperty zoomProperty = new SimpleDoubleProperty(250);
    /*@Override public void init() {
      backgroundImage = new Image("file:/Users/Naveen/Downloads/BART_cc_map/BART_cc_map.png");
    }*/

  public GridPane showView() 
  {
      
      // construct the scene contents over a stacked background.
      GridPane layout = new GridPane();
      Image backgroundImage = new Image("BART_cc_map.png");
        //createKillButton()
  
      // wrap the scene contents in a pannable scroll pane.
      ScrollPane scroll = createScrollPane(layout);
      scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      ImageView imageView = new ImageView(backgroundImage);
      imageView.setFitWidth(300);
      imageView.setFitHeight(300);
      
      
      layout.getChildren().addAll(imageView);
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
              if(imageView.getFitHeight() < 300)
              {
                  imageView.setFitHeight(300);
              }
              
              if(imageView.getFitWidth() < 300)
              {
                  imageView.setFitWidth(300);
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
      return layout; 
  
  }

  private ScrollPane createScrollPane(Pane layout) 
  {
      ScrollPane scroll = new ScrollPane();
      scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); 
      scroll.setPannable(true);
      scroll.setContent(layout);
      return scroll;
  }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
