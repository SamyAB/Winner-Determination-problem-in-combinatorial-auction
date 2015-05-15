package dz.oooo.ec;

import java.util.ArrayList;
import java.util.Iterator;

public class Bid {
	private ArrayList<Short> lots;
	private ArrayList<Bid> conflict;
	private double gain;
	
	public Bid(){
		this.lots=new ArrayList<Short>();
		this.gain=0;
	}

	public ArrayList<Short> getLots() {
		return lots;
	}

	public void setLots(ArrayList<Short> lots) {
		this.lots = lots;
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}
	
	public void addLot(short lot){
		this.lots.add(lot);
	}
	
	public ArrayList<Bid> getConflict() {
		return conflict;
	}

	public void setConflict(ArrayList<Bid> conflict) {
		this.conflict = conflict;
	}
	
	public void checkAddToConflict(Bid b){
		Iterator<Short> lots=this.lots.iterator();
		while(lots.hasNext()){
			if(b.getLots().contains(lots.next())){
				this.conflict.add(b);
				b.addConflict(this);
				break;
			}
		}
	}
	
	public void checkAddToConflict(ArrayList<Bid> bidsFormule){
		Iterator<Bid> bids=bidsFormule.iterator();
		while(bids.hasNext()){
			this.checkAddToConflict(bids.next());
		}
	}
	
	public void addConflict(Bid b){
		this.conflict.add(b);
	}
	
	public boolean isInConflictWith(Bid b){
		if(this.conflict.contains(b)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public String toString(){
		String s="Gain = "+this.gain+"\n";
		s+="Lots :";
		Iterator<Short> lots=this.lots.iterator();
		while(lots.hasNext()){
			s+=" "+lots.next();
		}
		s+="\n";
		return s;
	}
}
