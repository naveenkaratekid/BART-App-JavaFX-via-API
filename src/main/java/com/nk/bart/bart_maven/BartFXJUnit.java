package com.nk.bart.bart_maven;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.*;
import org.junit.*;


public class BartFXJUnit{

	@Test
	public void run()
	{
		new BARTUI().start(new Stage());
	}
}
