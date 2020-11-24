package class118133.nguyenvanchuc.practice;

import java.util.Random;

public class NQueenHeuristic_2 {
    int[] q; // mang luu vi tri cua quan hau trong tung hang
    int n; // kich thuoc ban co
    int hang;

    private void khoiTao(){
        // khoi tao ngau nhien vi tri ban co
        Random rd= new Random();
        q= new int[n];
        for (int i=0; i<n; i++) q[i]= rd.nextInt(n);
    }

    private void inBanCo(){
        // ham nay ho tro in ban co de quan sat cho truc quan
        for (int i=0; i<n; i++){
            for(int j=0; j<n;j++){
                if(q[i]== j) System.out.printf(" x ");
                else{
                    System.out.printf(" . ");
                }
            }
            System.out.println("");
        }
    }

    private int chonHang(){
        int[] pos= new int[n]; // mang luu cac gia tri toi uu
        int count=0; // bien luu gia tri lon nhat cua cac o
        int maxVal= -1; // gia tri toi uu

        for (int i=0; i<n; i++){
            int dem = kiemTra(i); // dem cac vi pham cua quan hau hang thu i
            if(dem > maxVal){
                maxVal= dem;
                count=0;
                pos[count]= maxVal;
            }else if (dem== maxVal){
                count++;
                pos[count]=i;
            }
        }
        hang= pos[new Random().nextInt(count+1)];
        return maxVal;
    }

    private int kiemTra(int x){
        //dem so luong vi pham voi ham x
        int soViPham=0;
        for(int i=0; i<n; i++){
            if (vipham(x,i)){
                soViPham++;
            }
        }
        return soViPham;
    }

    private boolean vipham(int i, int j) {
        // kiem tra xem quan hau o hang i va hang j cos vi pham hay khong
        if (i == j) return false;
        if (q[i] == q[j]) return true;// hai quan hau tren cung 1 cot
        if (q[i] - i == q[j] - j) return true; // hai quan hau tren cung 1 duong cheo
        if (q[i] + i == q[j] + j) return true; // hai quan hau tren cung 1 duong cheo
        return false;
    }

    private void chonCot(){
        int cotHienTai= q[hang];
        int minVal= n+1;
        int[] pos= new int[n];
        int count=0;

        for(int i=0; i<n; i++){
            if (i != cotHienTai){
                q[hang]=i; // thu dua den cot thu i
                int dem= kiemTra(hang);
                if(dem < minVal){
                    minVal= dem;
                    count=0;
                    pos[count]= i;
                }
                else if(dem == minVal) {
                    count++;
                    pos[count]= i;
                }
            }
        }
        q[hang]= pos[new Random().nextInt(count+1)];
        System.out.println(" Hang: "+ hang + " dua tu cot " + cotHienTai + " sang cot " + q[hang]);


    }
    public static void main(String[] args) {
        NQueenHeuristic_2 banco= new NQueenHeuristic_2();
        banco.n=8; // khoi tao kich thuoc ban co
        banco.khoiTao();
        int hang; // gia tri luu lai hang da duoc chon de thay doi gia tri

        //bat dau qua trinh lap toi uu
        for (int z=0; z<1000000; z++){
            System.out.println("........................................................");
            System.out.println("Day la vong lap thu "+ (z+1));
            banco.inBanCo();//ket qua ban co

            // ket qua rang buoc sau khi toi uu duoc luu vao "kq"
            int kq= banco.chonHang();
//            System.out.println("chon hang " +banco.hang+ " de thay doi");
            System.out.println("so luong rang buoc hien tai "+ kq);
            if(kq==0) break;

            banco.chonCot();// doi voi hang co nhieu vi pham nhat, chon cot co it vi pham nhat de chuyen toi
        }
    }
}
