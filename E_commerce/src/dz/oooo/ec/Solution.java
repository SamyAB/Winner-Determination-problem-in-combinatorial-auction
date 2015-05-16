package dz.oooo.ec;

import java.util.ArrayList;
import java.util.Iterator;

public class Solution {
	private ArrayList<Bid> bids;
	private double gain;
	
	public Solution(){
		this.bids=new ArrayList<Bid>();
		this.gain=0;
	}

	public ArrayList<Bid> getBids() {
		return bids;
	}

	public void setBids(ArrayList<Bid> bids) {
		this.bids = bids;
	}
	
	public void addBid(Bid b){
		this.bids.add(b);
		this.gain+=b.getGain();
	}
	
	public void forcedAddBid(Bid b){
		Iterator<Bid> bids=this.bids.iterator();
		ArrayList<Bid> toRemove=new ArrayList<Bid>();
		while(bids.hasNext()){
			Bid tmp=bids.next();
			if(b.isInConflictWith(tmp)){
				toRemove.add(tmp);
				this.gain-=tmp.getGain();
			}
		}
		Iterator<Bid> removes=toRemove.iterator();
		while(removes.hasNext()){
			this.bids.remove(removes.next());
		}
		this.bids.add(b);
		this.gain+=b.getGain();
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}
	
	public void setGain(){
		this.gain=0;
		Iterator<Bid> bids=this.bids.iterator();
		while(bids.hasNext()){
			this.gain+=bids.next().getGain();
		}
	}
	
	public Solution clone(){
		Solution solution=new Solution();
		Iterator<Bid> bids=this.bids.iterator();
		while(bids.hasNext()){
			solution.addBid(bids.next());
		}
		return solution;
	}
	
	public String toString(){
		String s="Gain de la solution = "+this.gain+"\n";
		s+="Enchères de la solutions : \n";
		Iterator<Bid> bids=this.bids.iterator();
		while(bids.hasNext()){
			s+=bids.next().toString();
		}
		return s;
	}
}
