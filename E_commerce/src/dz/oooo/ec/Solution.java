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

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}
	
	public String toString(){
		String s="Gain de la solution = "+this.gain+"\n";
		s+="Enchères de la solutions : ";
		Iterator<Bid> bids=this.bids.iterator();
		while(bids.hasNext()){
			s+=bids.next().toString();
		}
		return s;
	}
	
}
