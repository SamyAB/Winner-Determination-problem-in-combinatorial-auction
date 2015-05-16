package dz.oooo.ec;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class FormuleWDP {
	private ArrayList<Bid> bids;
	private short nbBids;
	private short nbLots;
	
	public FormuleWDP(){
		this.bids=new ArrayList<Bid>();
		this.nbBids=0;
		this.nbLots=0;
	}
	
	public FormuleWDP(String nomFichier){
		this();
		System.out.println("Début de la création de la formule WDP");
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(nomFichier)));
			String ligne=br.readLine();
			String[] mots=ligne.split(" ");
			this.nbLots=Short.parseShort(mots[0]);
			this.nbBids=Short.parseShort(mots[1]);
			ligne=br.readLine();
			while(ligne!=null){
				mots=ligne.split(" ");
				Bid bid=new Bid();
				bid.setGain(Double.parseDouble(mots[0]));
				for(int i=1;i<mots.length;i++){
					bid.addLot(Short.parseShort(mots[i]));
				}
				bid.checkAddToConflict(this.bids);
				this.bids.add(bid);
				ligne=br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Fin de la création de la formule WDP");
	}

	public ArrayList<Bid> getBids() {
		return bids;
	}

	public void setBids(ArrayList<Bid> bids) {
		this.bids = bids;
	}

	public short getNbBids() {
		return nbBids;
	}

	public void setNbBids(short nbBids) {
		this.nbBids = nbBids;
	}

	public short getNbLots() {
		return nbLots;
	}

	public void setNbLots(short nbLots) {
		this.nbLots = nbLots;
	}
	
	public String toString(){
		String s="Nombre de lots : "+this.nbLots+"\n";
		s+="Nombre d'enchères : "+this.nbBids+"\n";
		Iterator<Bid> bids=this.bids.iterator();
		while(bids.hasNext()){
			s+=bids.next().toString();
		}
		s+="\n";
		return s;
	}
	
	//Random key encoding algorithme modifié
	public Solution genererRandom(){
		Solution s=new Solution();
		
		//Génération du vecteur r
		ArrayList<Integer> r=new ArrayList<Integer>();
		for(int i=0;i<this.nbBids;i++){
			int tmp=(int)(Math.random()*10000)%this.nbLots;
			r.add(tmp);
		}
		
		int i=this.nbBids-1;
		while(i>=0){
			int index=r.indexOf(i);
			while(index==-1){
				i--;
				index=r.indexOf(i);
			}
			Bid tmp=this.bids.get(index);
			r.set(index,-1);
			if(!tmp.isInConflict(s.getBids())){
				s.addBid(tmp);
			}
		}
		
		return s;	
	}
	
	public Bid bestBid(Solution s){
		Iterator<Bid> bids=this.bids.iterator();
		Bid bestBid= null;
		double bestGainBid=-10000;
		while(bids.hasNext()){
			Bid tmp=bids.next();
			if(!s.getBids().contains(tmp)){
				Iterator<Bid> conflicts=tmp.getConflict().iterator();
				double perte=0;
				while(conflicts.hasNext()){
					Bid tmpConflinct=conflicts.next();
					if(s.getBids().contains(tmpConflinct)){
						perte+=tmpConflinct.getGain();
					}
				}
				double gainBid=tmp.getGain()-perte;
				if(gainBid>bestGainBid){
					bestGainBid=gainBid;
					bestBid=tmp.clone();
				}
			}
		}
		return bestBid;
	}
	
	public Solution RechercheLocaleStochastique(double wp,int nbIterationsMax){
		Solution s=genererRandom(),best=s.clone();
		for(int nbIteration=0;nbIteration<nbIterationsMax;nbIteration++){
			Bid bid=null;
			double r=Math.random();
			if(r<wp){
				bid=this.bids.get((int)(Math.random()*10000)%this.nbBids);
			}
			else{
				bid=bestBid(s);
			}
			if(bid!=null){
				s.forcedAddBid(bid);
			}
			if(s.getGain()>best.getGain()){
				System.out.println("nouveau meilleur gain "+s.getGain());
				best=s.clone();
			}
		}
		return best;
	}
	
	public Solution rechercheTaboue(int nbIterationsMax,int tenure,int iterDiversification){
		Solution s=genererRandom(),best=s.clone();
		ArrayList<Bid> listeTaboue=new ArrayList<Bid>();
		int indiceTaboue=0,iterationSansAmelioration=0;
		for(int nbIterations=0;nbIterations<nbIterationsMax;nbIterations++){			
			Bid bid=bestBid(s);
			if(bid!=null && !listeTaboue.contains(bid)){
				//Gestion de tenure liste taboue
				if(listeTaboue.size()>=tenure){
					listeTaboue.remove(indiceTaboue);
				}

				s.forcedAddBid(bid);
				
				listeTaboue.add(indiceTaboue,bid);
				indiceTaboue=(indiceTaboue+1)%tenure;
				
				if(s.getGain()>best.getGain()){
					best=s.clone();
					iterationSansAmelioration=0;
				}
				else{
					iterationSansAmelioration++;
					if(iterationSansAmelioration>=iterDiversification){
						//Diversification
						s=genererRandom();
						iterationSansAmelioration=0;
					}
				}
			}
			else{
				iterationSansAmelioration++;
				if(iterationSansAmelioration>=iterDiversification){
					//Diversification
					s=genererRandom();
					iterationSansAmelioration=0;
				}
			}
			
		}
		
		return best;
	}
	
}
