package dz.oooo.ec;

public class ClasseMain {
	private static int nbIteration=1000;
	
	public static void main(String[] args) {
		FormuleWDP f=new FormuleWDP("/mnt/Doc1/MEGAsync/m1s2/ecom/instance/in101");
		f.RechercheLocale((float) 0.5);
	}

	public static int getNbIteration() {
		return nbIteration;
	}

	public static void setNbIteration(int nbIteration) {
		ClasseMain.nbIteration = nbIteration;
	}

}
