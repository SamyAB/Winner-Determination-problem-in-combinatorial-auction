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
		this.conflict=new ArrayList<Bid>();
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
	
	public boolean equals(Bid b){
		if(this.gain!=b.getGain()||this.lots.size()!=b.getLots().size()){
			return false;
		}
		for(int i=0;i<this.lots.size();i++){
			if((short)(this.lots.get(i))!=(short)(b.getLots().get(i))){
				return false;
			}
		}
		return true;
	}
	
	public boolean equals(Object o){
		return this.equals((Bid) o);
	}
	
	public boolean isInConflictWith(Bid b){
		if(this.conflict.contains(b)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isInConflict(ArrayList<Bid> b){
		Iterator<Bid> bids=b.iterator();
		while(bids.hasNext()){
			if(this.isInConflictWith(bids.next())){
				return true;
			}
		}
		return false;
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
	
	public Bid clone(){
		Bid b=new Bid();
		Iterator<Short> lots=this.lots.iterator();
		while(lots.hasNext()){
			b.addLot(lots.next());
		}
		Iterator<Bid> conflict=this.conflict.iterator();
		while(conflict.hasNext()){
			b.addConflict(conflict.next());
		}
		b.setGain(this.gain);
		
		return b;
	}

	public Bid inverse(Solution s){
		Iterator<Bid> conflictIterator=this.conflict.iterator();
		Bid b=null;
		double gp=s.getGain();
		while(conflictIterator.hasNext()){
			Bid tmpBid=conflictIterator.next();
			double tmpGp=0;
			Iterator<Bid> tmpConflict=tmpBid.getConflict().iterator();
			while(tmpConflict.hasNext()){
				Bid tmpTmpBid=tmpConflict.next();
				if(s.getBids().contains(tmpTmpBid)){
					tmpGp+=tmpTmpBid.getGain();
				}
			}
			if(tmpGp<gp){
				gp=tmpGp;
				b=tmpBid;
			}
		}
		return b;
	}
}
