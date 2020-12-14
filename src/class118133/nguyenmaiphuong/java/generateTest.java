package class118133.nguyenmaiphuong.java;

import java.util.Random;

public class generateTest {
    public int rd(int min, int max)
    {
        Random generator = new Random();
        return generator.nextInt((max - min) + 1) + min;
    }

    public static void main(String[] args) {
        generateTest gen = new generateTest();
        int N, A, C;
        N = gen.rd(5000, 6000);
        
        int[] c = new int[N];
        int[] a = new int[N];
        int[] f = new int[N];
        int[] m = new int[N];
        int sumC = 0, sumA = 0;
        int minC = Integer.MAX_VALUE, minA = Integer.MAX_VALUE;
        for(int i =0; i<N; ++i)
        {
            c[i] = gen.rd(1, 50);
            a[i] = gen.rd(1, 50);
            f[i] = gen.rd(1, 50);
            m[i] = gen.rd(1, 50);
            sumC += c[i]*m[i];
            sumA += a[i]*m[i];
            minC = Math.min(minC, c[i] * m[i]);
            minA = Math.min(minA, a[i] * m[i]);
        }
            
        A = gen.rd((int)sumA/2, Integer.MAX_VALUE);
        C = gen.rd((int)sumC/2, Integer.MAX_VALUE);

        System.out.println(N);
        System.out.println(A);
        System.out.println(C);
        for(int i =0; i<N; ++i)
            System.out.print(c[i] + " ");
        System.out.println();
        for(int i =0; i<N; ++i)
            System.out.print(a[i] + " ");
        System.out.println();
        for(int i =0; i<N; ++i)
            System.out.print(f[i] + " ");
        System.out.println();
        for(int i =0; i<N; ++i)
            System.out.print(m[i] + " ");
        System.out.println();
    }
}