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

	//Méthode beeInit
	public Solution beeInit(){
		Solution s=new Solution();
		ArrayList<Bid> bidsPrime=new ArrayList<>();
		bidsPrime.addAll(this.bids);
		while(!bidsPrime.isEmpty()){
			double maxGain=0;
			Iterator<Bid> bidsIterator=bidsPrime.iterator();
			Bid best=null;
			while(bidsIterator.hasNext()){
				Bid tmpBid=bidsIterator.next();
				if(tmpBid.getGain()>maxGain){
					maxGain=tmpBid.getGain();
					best=tmpBid;
				}
			}
			if(!best.isInConflict(s.getBids())){
				s.addBid(best);
			}
			bidsPrime.remove(best);
		}
		return s;
	}
	
	//Méthode de choix de la solution sRef de la méthode beeSwarmOptimisation
	public Solution choixSRef(ArrayList<Solution> LT,ArrayList<Solution> danse,Solution previousSRef,int nbChancesMax){
		//Initialisations
		double maxGain=0;
		Iterator<Solution> danses=danse.iterator();
		Solution sRef=null;

		//Boucle de calcule de la meilleure solution (en qualité) de la table dance 
		//Tant qu'il reste des éléments dans la table danse
		while(danses.hasNext()){
			Solution tmp=danses.next();
			if(tmp.getGain()>maxGain && !LT.contains(tmp)){
				maxGain=tmp.getGain();
				sRef=tmp;
			}
		}

		//Calcule de la différence de qualité en la meilleur solution de la table dance et le précedent sRef
		double difference=sRef.getGain()-previousSRef.getGain();

		//Si la meilleure solution de la table danse est meilleure que le précédant sRef
		if(difference>0){
			ClasseMain.setNbChances(nbChancesMax); 
		}
		else{
			//Décrémentation du nombre de chances
			ClasseMain.setNbChances(ClasseMain.getNbChances()-1);
			//Si le nombre de chances restant n'est pas nul
			if(ClasseMain.getNbChances()<=0){
				//Reset du nombre de chances au nombre max de chances
				ClasseMain.setNbChances(nbChancesMax); 

				//Calcule de la meilleure solution (non taboue) en diversité
				short bestDiversite=0;
				danses=danse.iterator();
				while(danses.hasNext()){
					Solution tmp=danses.next();
					tmp.setDiversite(LT);
					short tmpDiversite=tmp.getDiversite();
					if(tmpDiversite>bestDiversite&& !LT.contains(tmp)){
						bestDiversite=tmpDiversite;
						sRef=tmp;
					}
				}
				System.out.println("Qualité de la solution divercifiée "+sRef.getGain()+" avec une distance ="+bestDiversite);
			}
		}
		return sRef;

	}

	//Méthode d'optimisation par essaim d'abeilles
	public Solution beeSwarmOptimisation(int nbIterationsMax,int flip,int nbChancesMax){
		//BeeInit génère par la méthode beeInit
		Solution sRef=beeInit(),best=sRef.clone();

		//Initialisations
		ArrayList<Solution> listeTaboue=new ArrayList<Solution>();
		int nbIteration=0;
		int indiceTaboue=0,tailleTaboue=this.nbBids;
		ArrayList<Solution> danse=null;

		//Tant que le nombre de clause SAT est différant du nombre total de clauses
		//Et que nombre d'itérations est inférieur au nombre max d'itération
		while(nbIteration<nbIterationsMax){
			//Gestion de la liste taboue selon le principe de tenure
			if(listeTaboue.size()>=tailleTaboue){
				listeTaboue.remove(indiceTaboue);
			}

			//Ajout de sRef à la liste taboue
			listeTaboue.add(indiceTaboue,sRef);
			indiceTaboue=(indiceTaboue+1)%tailleTaboue;

			//Création de la serachArea avec un flip périodique de valeur flip
			Iterator<Solution> searchArea=sRef.flipPeriodique(flip).iterator();

			//Vidange/initialisation de la table danse
			danse=new ArrayList<Solution>();

			//Recherche des abeilles successivement
			while(searchArea.hasNext()){
				Solution tmp=searchArea.next();
				//System.out.println("le meilleur voisin à un nombre de clasuses sat= "+bestVoisin.getNbClausesSat());
				Bid b=bestBid(tmp);
				tmp.forcedAddBid(b);
				danse.add(tmp);
			}

			//Choix du prochain sRef
			sRef=choixSRef(listeTaboue, danse, sRef,nbChancesMax);

			//Comparaison du meilleur résultat avec le nouveau sRef
			if(sRef.getGain()>best.getGain()){
				System.out.println("Nouvelle meilleure performance:" +sRef.getGain());
				best=sRef.clone();
			}
			nbIteration++;
		}
		System.out.println("La meilleure performance obtenue est "+best.getGain());
		return best;
	}

}
