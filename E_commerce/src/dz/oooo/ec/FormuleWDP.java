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
			while(ligne!=null){
				ligne=br.readLine();
				mots=ligne.split(" ");
				Bid bid=new Bid();
				bid.setGain(Double.parseDouble(mots[0]));
				for(int i=1;i<mots.length;i++){
					bid.addLot(Short.parseShort(mots[i]));
				}
				bid.checkAddToConflict(this.bids);
				this.bids.add(bid);
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
	
}
