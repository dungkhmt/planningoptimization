package class118133.tranhoangchuan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

class Indi implements Comparable<Indi>{
	NQueenGA nq;
	Random rd = new Random();
	public int[] gen;
	int N;
	int fitness;
	Indi (NQueenGA nq){
		this.nq =nq;
		gen = new int[nq.N];
		this.N = nq.N;
	}
	public void init () {
		ArrayList<Integer> tmp = new ArrayList();
		for (int i = 0; i <N; ++i) tmp.add(i);
		for (int i = 0; i <N;++i) {
			int j = rd.nextInt(tmp.size());
			gen[i] = tmp.get(j);
			tmp.remove(j);
		}
		this.setFitness();
	}
	public boolean checkThangHang(int i, int j) {
		return (gen[i] != gen[j]);
	}
	public boolean checkCheo(int i, int j) {
		return (Math.abs(i-j) !=  Math.abs(gen[i]-gen[j]));
	}
	public void setFitness() {
		fitness = 0;
		for (int i = 0; i < N; ++i) {
			for (int j = i+1; j < N; ++j) {
				if (!checkCheo(i, j)) fitness++;
			}
		}
	}
	public int getFitness() {
		return this.fitness;
	}
	public void print() {
		for (int i = 0; i < N; ++i) {
			System.out.print(gen[i] + "  ");
		}
		System.out.println();
	}
	@Override
	public int compareTo(Indi o) {
		int res = this.fitness - o.fitness;
		if (res > 0) return 1;
		if (res < 0) return -1;
		return 0;
	}
	public boolean checkAll() {
		for (int i =0; i <N; ++i) {
			for (int j = i+1; j <N; ++j) {
				if (!(checkThangHang(i, j) && checkCheo(i, j))) {
					if (!checkThangHang(i, j)) System.out.println("Kp thang");
					System.err.println(i+ "   " + j + " " + gen[i] + "  " + gen[j]  );
					return false;
				}
			}
		}
		return true;
	}
}
public class NQueenGA {
	public int N;
	public int popSize = 200;
	double pm = 0.9;
	double px = 0.2;
	public Random rd = new Random();
	public int genSize = 1500;
	public NQueenGA(int n) {
		this.N = n;
	}
	public ArrayList<Indi> crossover(Indi indi1, Indi indi2, NQueenGA nq){
		int p1, p2;
		do {
			p1 = rd.nextInt(N);
			p2 = p1 + rd.nextInt(N-p1);
		} while (p1 == p2);
//		System.err.println(p1 + "    " + p2);
		boolean t1[] = new boolean[N];
		boolean t2[] = new boolean[N];
		Arrays.fill(t1, false);
		Arrays.fill(t2, false);

		Indi child1 = new Indi(nq);
		Indi child2 = new Indi(nq);
		// Init 
		for (int it = p1; it <= p2; ++it) {
			child1.gen[it] = indi2.gen[it];
			child2.gen[it] = indi1.gen[it];
//			System.out.println(child1.gen[it]);
			t1[child1.gen[it]] = true;
			t2[child2.gen[it]] = true;
		}
		ArrayList<Integer> thua1 = new ArrayList<Integer>();
		ArrayList<Integer> thua2 = new ArrayList<Integer>();
		ArrayList<Integer> indx1 = new ArrayList<Integer>();
		ArrayList<Integer> indx2 = new ArrayList<Integer>();
		for (int it =0; it < p1; ++it) {
//			System.out.println(it);
			int xet = indi1.gen[it];
			if (!t1[xet]) {
				child1.gen[it] = indi1.gen[it];
				t1[xet] = true;
			} else {
				indx1.add(it);
			}
			xet = indi2.gen[it];
			if (!t2[xet]) {
				child2.gen[it] = indi2.gen[it];
				t2[xet] = true;
			} else {
				indx2.add(it);
			}
		}
		for (int it =p2+1; it < N; ++it) {
//			System.out.println(it);
			int xet = indi1.gen[it];
			if (!t1[xet]) {
				child1.gen[it] = indi1.gen[it];
				t1[xet] = true;
			} else {
				indx1.add(it);
			}
			xet = indi2.gen[it];
			if (!t2[xet]) {
				child2.gen[it] = indi2.gen[it];
				t2[xet] = true;
			} else {
				indx2.add(it);
			}
		}
		for (int it = 0; it < N; ++it) {
			if(!t1[it]) thua1.add(it);
			if(!t2[it]) thua2.add(it);
		}
//		System.out.println(thua1.size() + "    " + indx1.size());
//		System.out.println("Thua1");
//		for (int i: thua1) System.out.println(i);
//		System.out.println("Indx1");
//		for (int i : indx1) System.out.println(i);
		for (int ind: indx1) {
			int r = rd.nextInt(thua1.size());
			child1.gen[ind] = thua1.remove(r);
		}
		for (int ind: indx2) {
			int r = rd.nextInt(thua2.size());
			child2.gen[ind] = thua2.remove(r);
		}
		child1.setFitness();
		child2.setFitness();
		ArrayList<Indi>  offspring = new ArrayList<>();
		offspring.add(child1);
		offspring.add(child2);
		return offspring;
	}
	public void mutation(Indi i, NQueenGA nq) {
		int k, h;
		do {
			k= nq.rd.nextInt(nq.N);
			h = nq.rd.nextInt(nq.N);
		} while (k ==h);
		int tmp = i.gen[k];
		i.gen[k] = i.gen[h];
		i.gen[h] = tmp;
		i.setFitness();
	}
	public Indi solve(NQueenGA nq) {
		// Init pop
		ArrayList<Indi> pop = new ArrayList();
		for (int i = 0; i< popSize; ++i) {
			Indi tmp = new Indi(nq);
			tmp.init();
			pop.add(tmp);
		}
		// Selection
		for (int gen = 0; gen < genSize; ++gen) {
			Collections.sort(pop);
			ArrayList<Indi> newPop = new ArrayList<Indi>(pop.subList(0, 30));
			while (newPop.size() < popSize) {
				double r = rd.nextDouble();
				ArrayList<Indi> offspring;
					int i = rd.nextInt(pop.size());
					int j = rd.nextInt(pop.size());
					offspring = crossover(pop.get(i), pop.get(j), nq);
				
				r = rd.nextDouble();
				if (r < px) {
					mutation(offspring.get(0), nq);
					mutation(offspring.get(1), nq);
				}
				newPop.add(offspring.get(0));
				newPop.add(offspring.get(1));
			}
			pop = newPop;
			System.out.println("Gen: "+ gen + " Fit: "+ pop.get(0).getFitness());
		}
		return pop.get(0);
	}
	public static void main(String[] args) {
		NQueenGA nq = new NQueenGA(500);
		Indi a = new Indi(nq); 
		Indi b = new Indi(nq);
//		a.init();
//		b.init();
//		ArrayList<Indi> res = nq.crossover(a, b, nq);
//		a.print();
//		b.print();
//		res.get(0).print();
//		res.get(1).print();
//		System.out.println(a.checkAll() + "  " + b.checkAll());
		Indi i = nq.solve(nq);
		System.out.println(i.checkAll());
	}
}