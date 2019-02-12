package com.nk.bart.bart_maven;

import javafx.scene.control.*;
import javafx.util.*;
import javafx.animation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class Clock extends Label
{
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E MM-dd-yyyy h:mm:ss a");
	public Clock()
	{
		bindTheTime();
	}
	public void bindTheTime()
	{
		try
		{
			Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(0),event -> setText(LocalDateTime.now().format(dtf))),new KeyFrame(Duration.seconds(1)));
			timeLine.setCycleCount(Animation.INDEFINITE);
			timeLine.play();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
