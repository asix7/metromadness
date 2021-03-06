package com.unimelb.swen30006.metromadness;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Simulation {
	
	private ArrayList<Station> stations;
	private ArrayList<Line> lines;
	private ArrayList<Train> trains;
	public PassengerGenerator g;
	
	public Simulation(String fileName){
		// Create a map reader and read in the file
		MapReader m = new MapReader(fileName);
		m.process();
		
		// Create a list of lines
		this.lines = new ArrayList<Line>();
		this.lines.addAll(m.getLines());
				
		// Create a list of stations
		this.stations = new ArrayList<Station>();
		this.stations.addAll(m.getStations());
		
		// Create a list of trains
		this.trains = new ArrayList<Train>();
		this.trains.addAll(m.getTrains());
		this.g = new PassengerGenerator(this.stations, this.trains);
	}
	
	
	// Update all the trains in the simulation
	public void update(){
		
		for(Station s: this.stations){
			//Create a random number of passengers less than the maxPax
			int maxpax = s.getMaxPax();
			ArrayList<Passenger> waiting = s.getWaitingList();
			
			//If the number of people waiting is less than the max capacity of the station
			// multiplied by a random number and by 0.5, create passengers
			//This gaves each station a reasonable random number of passengers that is less than
			//the maxpac
			if(waiting.size()<(int)(0.5*maxpax *Math.random())){
			
				Passenger[] ps = this.g.generatePassengers(s, trains);
				for(Passenger p: ps){
					//While there is space in the station
					while(waiting.size()<(maxpax *Math.random())){
						s.addWaiting(p);
					}
				}
			}
		}
		
		
		// Update all the trains
		for(Train t: this.trains){
			t.update(Gdx.graphics.getDeltaTime());
		}
	}
	
	public void render(ShapeRenderer renderer){
		for(Line l: this.lines){
			l.render(renderer);
		}

		for(Train t: this.trains){
			t.render(renderer);
		}
		for(Station s: this.stations){
			s.render(renderer);
		}
	}
	
	
}
