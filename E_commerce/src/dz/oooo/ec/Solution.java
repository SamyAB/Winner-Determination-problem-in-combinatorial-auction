package dz.oooo.ec;

import java.util.ArrayList;
import java.util.Iterator;

public class Solution {
	private ArrayList<Bid> bids;
	private double gain;
	private short diversite;

	public short getDiversite() {
		return diversite;
	}

	public void setDiversite(short diversite) {
		this.diversite = diversite;
	}

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
		solution.setDiversite(this.diversite);
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

	//Calcule de diversité d'une solution à l'aide de la distance de Hamming
	public void setDiversite(ArrayList<Solution> listeTaboue){
		//Initialisation de la distance à la valaure maximal possible càd le nombre de littéraux
		short diversite=(short)this.bids.size();

		//Parcourt de toute la liste taboue
		Iterator<Solution> LT=listeTaboue.iterator();
		while(LT.hasNext()){
			Solution tmp=LT.next();
			short distance=0;

			//Compare chaque littéral de la solution au littéral corréspendant de l'élément de la liste taboue
			for(int i=0;i<this.bids.size();i++){
				if(!this.bids.get(i).equals(tmp.getBids().get(i))){
					distance++;
				}
			}
			if(distance<diversite){
				diversite=distance;
			}
		}

		//La diversité est égale à la distance minimale des éléments de la liste taboue
		this.diversite=diversite;
	}

	public boolean equals(Solution s){
		if(s==null || s.getGain()!=this.gain || s.getBids().size()!=this.bids.size()){
			return false;
		}
		for(int i=0;i<this.bids.size();i++){
			boolean same=false;
			for(int j=0;j<s.getBids().size();j++){
				if(this.bids.get(i).equals(s.getBids().get(j))){
					same=true;
					break;
				}
			}
			if(!same){
				return false;
			}
		}
		return true;
	}
	
	public boolean equals(Object o){
		Solution s=(Solution) o;
		return this.equals(s);
	}

	public ArrayList<Solution> flipPeriodique(int flip) {
		ArrayList<Solution> searchArea=new ArrayList<Solution>();
		for(int i=0;i>flip;i++){
			Solution s=this.clone();
			for(int j=0;j*flip+i<this.bids.size();j++){
				s.forcedAddBid(s.bids.get(j*flip+i).inverse(s));
			}
			searchArea.add(s);
		}
		return searchArea;
	}
}
