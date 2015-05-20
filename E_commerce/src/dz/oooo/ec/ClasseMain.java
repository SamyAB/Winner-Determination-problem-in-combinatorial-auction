package dz.oooo.ec;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ClasseMain {
	private static int nbChances=4;

	public static int getNbChances() {
		return nbChances;
	}

	public static void setNbChances(int nbChances) {
		ClasseMain.nbChances = nbChances;
	}

	public static void main(String[] args) {
		PrintWriter output=null;
		try {
			int nbIterations=500;
			double moyTemps=0,moyQualite=0;
			for(int i=101;i<=101;i++){
				output=new PrintWriter(new BufferedWriter(new FileWriter("outputBSO"+i+".csv")));

				FormuleWDP f=new FormuleWDP("in"+i);
				output.print("in"+i+",");

				/*double wp=0.1;
				for(int j=0;j<6;j++){
					long debut=System.nanoTime();
					Solution s=f.RechercheLocaleStochastique(wp, nbIterations);
					long fin=System.nanoTime();
					moyQualite+=s.getGain();
					moyTemps+=(fin-debut)/1000000000.0;
				}
				moyQualite/=6;
				moyTemps/=6;
				output.print(moyTemps+","+moyQualite+",");
				moyQualite=0;
				moyTemps=0;

				int tenure=40;
				int nbIterationBis=6;
				for(int j=0;j<6;j++){
					long debut=System.nanoTime();
					Solution s=f.rechercheTaboue(nbIterations, tenure, nbIterationBis);
					long fin=System.nanoTime();
					moyQualite+=s.getGain();
					moyTemps+=(fin-debut)/1000000000.0;
				}
				moyQualite/=6;
				moyTemps/=6;
				output.print(moyTemps+","+moyQualite+",");
				moyQualite=0;
				moyTemps=0;*/


				nbIterations=250;
				int flip=4;					
				int nbChancesMax=4;
				long debut=System.nanoTime();
				Solution s=f.beeSwarmOptimisation(nbIterations, flip, nbChancesMax);
				long fin=System.nanoTime();
				s.setGain();
				moyQualite=s.getGain();
				moyTemps=(fin-debut)/1000000000.0;
				output.print(moyTemps+","+moyQualite+",");
				moyQualite=0;
				moyTemps=0;
				

				output.print("\n");
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(output!=null){
				output.close();
			}
		}
		


	}
}
