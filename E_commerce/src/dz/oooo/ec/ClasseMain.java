package dz.oooo.ec;

public class ClasseMain {
	private static int nbChances=4;
	
	public static int getNbChances() {
		return nbChances;
	}

	public static void setNbChances(int nbChances) {
		ClasseMain.nbChances = nbChances;
	}

	public static void main(String[] args) {
		FormuleWDP f=new FormuleWDP("/mnt/Doc1/MEGAsync/m1s2/ecom/instance/in101");
		Solution s=f.rechercheTaboue(1000, 500, 10);
		System.out.println("Solution est "+s.toString());
	}
}
