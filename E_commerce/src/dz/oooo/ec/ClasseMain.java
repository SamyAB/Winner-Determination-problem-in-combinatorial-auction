package dz.oooo.ec;

public class ClasseMain {
	
	public static void main(String[] args) {
		FormuleWDP f=new FormuleWDP("/mnt/Doc1/MEGAsync/m1s2/ecom/instance/in101");
		Solution s=f.rechercheTaboue(1000, 500, 10);
		System.out.println("Solution est "+s.toString());
	}
}
