import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class N_queen_heuristic {
    int n = 300 ; // so quan hau
    int [] board = new int[n];
    int violate = 2*n;
    public void initState(){
        Random random = new Random();
        for(int i = 0; i < n; i++){
            board[i] = random.nextInt(n);
        }
    }
    public void improve(){
        Random random = new Random();
        List<Integer> listResult = new ArrayList<>();
        List<Integer> selectQueen = new ArrayList<>();
        int index = 0;
        int maxViolateLevel = 0;
        for (int i = 0 ; i < n; i++){
            int temp = 0;
            for(int j = 0; j < n; j++){
                if(i!=j && ((Math.abs(board[i]-board[j]) == Math.abs(i - j)) || board[i] == board[j])){
                    temp ++ ;
                }
            }
            listResult.add(temp);
            if(temp>maxViolateLevel){
                index = i;
                maxViolateLevel = temp;
            }
        }
        for (int i = 0 ; i < listResult.size(); i++){
            if(listResult.get(i) == maxViolateLevel){
                selectQueen.add(i);
            }
        }
        index = selectQueen.get(random.nextInt(selectQueen.size()));

        // tim hang de di chuyen, con hau index, can tim board[index]. (board[index],index)
        listResult.clear();
        selectQueen.clear();
        int minViolate = n;
        for(int row = 0; row < n ; row++){
            int temp = 0;
            for(int i = 0; i < n; i++){
                if(i != index && (Math.abs(row - board[i]) == Math.abs(index - i) || board[i] == row )){
                    temp++;
                }
            }
            listResult.add(temp);
            if(temp < minViolate){
                minViolate = temp;
                board[index] = row;
            }
        }
        for (int i = 0 ; i < listResult.size(); i++){
            if(listResult.get(i) == minViolate){
                selectQueen.add(i);
            }
        }
        board[index] = selectQueen.get(random.nextInt(selectQueen.size()));
        // compute violate
        violate = 0;
        for (int i = 0 ; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i!=j && ((Math.abs(board[i]-board[j]) == Math.abs(i - j)) || board[i] == board[j])){
                    violate ++ ;
                }
            }
        }
    }
    public void printSolution(){
        System.out.println("Solution : ");
        for (int i = 0 ; i < n ; i++){
            System.out.print(board[i] + " ");
        }
    }
    public void printBoard(){
        for (int i = 0 ;i < n; i++){
            for (int j = 0; j < n; j++){
                if(board[j] == i);
            }
        }
    }
    public void solve(){
        /*
        1. Sinh loi giai ban dau, moi quan hau tren mot cot
        2. Improve: Chon con hau dang chieu nhieu nhat, index
        3. Chon hang r sao cho khi di chuyen con hau index den thi so vi pham giam nhieu nhat
         */
        initState();
        while(violate > 0){
            System.out.println(violate);
            improve();
        }
        printSolution();
    }
    public static void main(String[] args) {
        N_queen_heuristic n_queen_heuristic = new N_queen_heuristic();
        n_queen_heuristic.solve();
    }
}

