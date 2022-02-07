public class Rating {
    public static int rate(int list, int depth){
        int count=0;
        count+=rateAttack();
        count+=ratePosition();
        count+=ratePieces();
        count+=rateMoveability(list, depth);
        AlphaBetaChess.flipBoard();
        count-=rateAttack();
        count-=ratePosition();
        count-=ratePieces();
        count-=rateMoveability(list, depth);
        AlphaBetaChess.flipBoard();

        return -(count+50*depth);
    }
    public static int rateAttack(){
        int count=0;
        return count;
    }
    public static int ratePosition(){
        int count=0;
        int numBishop = 0;
        for(int i=0; i<64; i++){
            switch(AlphaBetaChess.chessBoard[i/8][i%8]){
                case "P":
                    count+=100;
                    break;
                case "R":
                    count+=500;
                    break;
                case "N":
                    count+=300;
                    break;
                case "B":
                    numBishop++;
                    break;
                case "Q":
                    count+=900;
                    break;
            }}
        if(numBishop>=2) count+=300*numBishop;
        else if(numBishop==1) count+=250;
        return count;
    }
    public static int ratePieces(){
        int count=0;
        return count;
    }
    public static int rateMoveability(int listLenght, int depth){
        int count=0;
        count+=listLenght;
        if(listLenght==0){
            System.out.println("no moves");
            if(AlphaBetaChess.kingSafe()){
                return -150000*depth;
            }
            else return -200000;
        }
        return count;
    }
}
