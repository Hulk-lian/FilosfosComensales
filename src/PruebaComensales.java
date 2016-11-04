class Silla{
	private int nsillas=4;
	public synchronized void Comer(int i) throws InterruptedException{
		while(nsillas==0){
			wait();
		}
		System.out.println("Filosofo: "+i+" se sienta a comer");
		nsillas--;
	}
	
	public synchronized void Come(int i){
		System.out.println(i+" esta comiendo");
	}
	
	public synchronized void DejarComer(int i){
		nsillas++;
		System.out.println("Filosofo: "+i+" deja de comer y se va a pensar");
		notify();
	}
}
class Palillo{
	private int id;
	private boolean libre=true;
	public Palillo(int id){
		this.id=id;
	}
	public synchronized void usarPalillo(int i) throws InterruptedException{
		while(!libre){
			wait();
		}
		System.out.println("Filosofo "+i+" coge el palillo "+this.id);
		libre= false;
	}
	public synchronized void dejarPalillo(int i){
		libre=true;
		System.out.println("Filosofo "+i+ " suelta el palillo "+this.id);
		notify();
	}
}
class Filosofo extends Thread{
	private int id;
	private Palillo iz,der;
	private Silla s;
	
	public Filosofo (int id,Palillo i,Palillo d, Silla s){
		this.id=id;
		this.iz=i;
		this.der=d;
		this.s=s;
	}

	@Override
	public void run(){
		while(true){
			try {
				s.Comer(id);
				iz.usarPalillo(id);
				der.usarPalillo(id);
				
				//a com�h
				s.Come(id);
				//ya ha comio
				iz.dejarPalillo(id);
				der.dejarPalillo(id);
				s.DejarComer(id);
				Thread.sleep(200);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
public class PruebaComensales {

	public static void main(String[] args) {
		Silla s = new Silla();
		Palillo[] palillos=new Palillo[5];
		Filosofo[] filosofos= new Filosofo[5];
		for(int i=0;i<palillos.length;i++){
			palillos[i]=new Palillo(i);
		}
		for(int i=0;i<filosofos.length;i++){
			filosofos[i]=new Filosofo(i,palillos[i],palillos[(i+1)%5],s);
		}
		for(int i=0;i<filosofos.length;i++){
			filosofos[i].start();
		}
		
	}

}
