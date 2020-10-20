package class118133.nguyenvanchuc;

import java.util.Random;

public class NQueenHeuristic {
    int[] q;
    int n;
    int hang;
    public static void main(String[] args) {
        NQueenHeuristic banco= new NQueenHeuristic();
        banco.n=300;
        banco.khoitao();

        for(int z=0; z<1000000; z++){
            System.out.println("..........................");
            System.out.println("Sl"+z);
//            banco.inbanco();
            int kt=banco.chonhang();
            System.out.println("chon"+banco.hang);
            System.out.println(kt);
            if(kt==0) break;
            banco.choncot();
        }


//        banco.inbanco();
    }
    private void choncot(){
        int now = q[hang];
        int demMin = n+1;
        int[] pos = new int[n];
        int count = 0 ;
        for (int i=0; i<n; i++)
            if (i!=now){
                q[hang] = i;
                int dem = kiemtra(hang);
                if (dem<demMin) {
                    demMin = dem;
                    count = 0 ;
                    pos[count] = i ;

                } else if (dem == demMin){
                    count++ ;
                    pos[count] = i ;
                }
        }
        q[hang] = pos[ new Random().nextInt(count+1)];
    }
    private void khoitao(){
        Random rd= new Random();
        q= new int[n];
        for(int i=0; i<n;i++){
            q[i]= rd.nextInt(n);
        }
    }
    private void inbanco(){
//        for(int i=0; i< n; i++){
//            System.out.println(q[i]);
//        }

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(q[i]==j) System.out.printf(" x ");
                else{
                    System.out.printf(" . ");
                }
            }
            System.out.println("");

        }
    }
    private boolean vipham(int i, int j){
        if(i==j) return false;
        if(q[i]==q[j]) return true;
        if (q[i]-i== q[j]-j) return  true;
        if (q[i]+i== q[j]+j) return  true;
        return false;
    }
    private int kiemtra(int x){
        int dem =0;
        for(int i=0; i<n; i++){
            if(vipham(x,i)) {
                dem++;
//                System.out.println("vi pham "+ i);
            }
        }
        return dem;
    }
    private int chonhang(){
        int[] pos = new int[n];
        int count = 0 ;
        int demMax = -1;
        for (int i=0; i<n; ++i){
            int dem = kiemtra(i);
            if (dem>demMax) {
                demMax = dem;
                count = 0 ;
                pos[count] = i ;

            } else if (dem == demMax){
                count++ ;
                pos[count] = i ;
            }
        }
        hang =  pos[ new Random().nextInt(count+1)];
        return demMax;
    }
}
