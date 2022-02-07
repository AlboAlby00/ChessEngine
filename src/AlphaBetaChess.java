import javax.swing.*;
import java.util.Locale;
import java.util.Scanner;

public class AlphaBetaChess {

    static int kingPositionC, kingPositionL;
    static int humanAsWhite; //1 human white, 0 computer white
    public static int globalDepth=4;

    static String[][] chessBoard = {
            {"r","n","b","q","k","b","n","r"},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {"R","N","B","Q","K","B","N","R"},
    };

    public static void printBoard(){
        for(String[] row: chessBoard){
            for(String cell : row) System.out.print(cell+",");
            System.out.println("");
        }
    }


    public static void main(String[] args) {
        while(!chessBoard[kingPositionC/8][kingPositionC%8].equals("K")) kingPositionC++;
        while(!chessBoard[kingPositionL/8][kingPositionL%8].equals("k")) kingPositionL++;

        JFrame f = new JFrame("Chess");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(UserInterface.squareSize*8,UserInterface.squareSize*8+UserInterface.exitBarHeight);
        UserInterface ui= new UserInterface();
        f.add(ui);
        f.setVisible(true);
        //makeMove(alphaBeta(globalDepth,Integer.MAX_VALUE,Integer.MIN_VALUE,"aaaaa",1));
        //System.out.println(alphaBeta(globalDepth,Integer.MAX_VALUE,Integer.MIN_VALUE,"aaaaa",1));
        //printBoard();

        Object[] options= {"Computer","Human"};
        humanAsWhite=JOptionPane.showOptionDialog(null,"Who plays as white","Choose first player", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
        if(humanAsWhite==0){
            makeMove(alphaBeta(globalDepth,Integer.MAX_VALUE,Integer.MIN_VALUE,"",0));
            flipBoard();
            f.repaint();
        }
        /*
        printBoard();
        System.out.println(possibleMoves());
        flipBoard();
        System.out.println("");
        printBoard();
        System.out.println(possibleMoves());*/



    }

    public static int rating(){
        return 0;
    }
    public static void flipBoard(){
        String temp;
        for(int i=0; i<32; i++){

            int r=i/8,c=i%8;
            if(Character.isUpperCase(chessBoard[r][c].charAt(0))){
                temp=chessBoard[r][c].toLowerCase();
            }
            else{
                temp=chessBoard[r][c].toUpperCase();
            }
            if(Character.isUpperCase(chessBoard[7-r][7-c].charAt(0))){
                chessBoard[r][c]=chessBoard[7-r][7-c].toLowerCase();
            }
            else{
                chessBoard[r][c]=chessBoard[7-r][7-c].toUpperCase();
            }
            chessBoard[7-r][7-c]=temp;
        }
        int kingTemp=kingPositionC;
        kingPositionC=63-kingPositionL;
        kingPositionL=63-kingTemp;
    }
    public static String alphaBeta(int depth, int beta, int alpha, String move, int player){
        String list = possibleMoves();

        if(list.length()==0||depth==0) return move+(Rating.rate(list.length(),depth)*(player*2-1));

        player=1-player;

        for(int i=0; i<list.length();i+=5){
            makeMove(list.substring(i,i+5));
            flipBoard();
            String returnString = alphaBeta(depth-1,beta,alpha,list.substring(i,i+5),player);
            int value=Integer.parseInt(returnString.substring(5));
            flipBoard();
            undoMove(list.substring(i,i+5));
            if(player==0){
                if(value<=beta){
                    beta=value;
                    if(depth==globalDepth) move=returnString.substring(0,5);
                }

            }else{
                if(value>alpha){
                    alpha=value;
                    if(depth==globalDepth) move=returnString.substring(0,5);
                }

            }
            if(alpha>=beta) {if(player==0) return move+beta; else return move+alpha;}
        }
        if(player==0){return move+beta;} else return move+alpha;
    }

    public static void makeMove(String move){
        if(move.charAt(4)!='P'){
            //move=r+c+nr+nc+pieceKilled; not promotion
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
            if("K".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3 ))]))
                kingPositionC=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
        }else{
            //move=c+nc+pieceKilled+promotionPiece+"P"; promotion
            chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(3));
            chessBoard[1][Character.getNumericValue(move.charAt(0))]=" ";

        }
    }
    public static void undoMove(String move){
        if(move.charAt(4)!='P'){
            //move=r+c+nr+nc+pieceKilled; not promotion
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
            if(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))].equals("K")){
                kingPositionC=8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));
            }

        }else{
            //move=c+nc+pieceKilled+promotionPiece+"P"; promotion
            chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(2));
            chessBoard[1][Character.getNumericValue(move.charAt(0))]="P";

        }
    }

    public static String possibleMoves(){
        String list="";
        for(int i=0; i<64; i++){
            switch(chessBoard[i/8][i%8]){
                case "P":
                    list+=possibleP(i);
                    break;
                case "R":
                    list+=possibleR(i);
                    break;
                case "N":
                    //System.out.println("NNN");
                    list+=possibleN(i);
                    break;
                case "B":
                    list+=possibleB(i);
                    break;
                case "Q":
                    list+=possibleQ(i);
                    break;
                case "K":
                    list+=possibleK(i);
                    break;
            }
        }
        return list;
    }

    public static String possibleP(int i){
        String list = "";
        String pieceKilled=" ";
        int r=i/8,c=i%8;
        for(int j=-1; j<=1; j+=2){
            try{
                //kill
                if(Character.isLowerCase(chessBoard[r-1][c+j].charAt(0))&&i>=16) {
                    pieceKilled=chessBoard[r-1][c+j];
                    chessBoard[r][c]=" ";
                    chessBoard[r-1][c+j]="P";
                    if(kingSafe()){
                        list=list+r+c+(r-1)+(c+j)+pieceKilled;
                    }
                    chessBoard[r][c]="P";
                    chessBoard[r-1][c+j]=pieceKilled;
                }
                //kill and promotion
                if(Character.isLowerCase(chessBoard[r-1][c+j].charAt(0))&&i<16) {
                    pieceKilled=chessBoard[r-1][c+j];
                    String[] promotionPieces={"Q","N","R","B"};
                    for(String promotionPiece : promotionPieces){
                        chessBoard[r][c]=" ";
                        chessBoard[r-1][c+j]="P";
                        if(kingSafe()){
                            list=list+c+(c+j)+pieceKilled+promotionPiece+"P";
                            //c+(c+j)+pieceKilled+promotionPiece+"P"
                        }
                        chessBoard[r][c]="P";
                        chessBoard[r-1][c+j]=pieceKilled;
                    }
                }
        }catch (Exception e) {}}
        //one up
        try {
            if(chessBoard[r-1][c].equals(" ")&&i>=16){
                chessBoard[r][c]=" ";
                chessBoard[r-1][c]="P";
                if(kingSafe()){
                    list=list+r+c+(r-1)+(c)+" ";
                }
                chessBoard[r][c]="P";
                chessBoard[r-1][c]=" ";
            }

        }catch (Exception e) {}
        //one up and promotion
        try {
            if(chessBoard[r-1][c].equals(" ")&&i<16){
                String[] promotionPieces={"Q","N","R","B"};
                for(String promotionPiece : promotionPieces){
                    chessBoard[r][c]=" ";
                    chessBoard[r-1][c]="P";
                    if(kingSafe()){
                        list=list+c+(c)+" "+promotionPiece+"P";
                    }
                    chessBoard[r][c]="P";
                    chessBoard[r-1][c]=" ";}
            }

        }catch (Exception e) {}
        //two up
        try {
            if(chessBoard[r-1][c].equals(" ")&&chessBoard[r-2][c].equals(" ")&&i>=48){
                chessBoard[r][c]=" ";
                chessBoard[r-1][c]="P";
                if(kingSafe()){
                    list=list+r+c+(r-2)+(c)+" ";
                }
                chessBoard[r][c]="P";
                chessBoard[r-1][c]=" ";
            }

        }catch (Exception e) {}

        //System.out.println(list);
        return list;
    }
    public static String possibleR(int i){
        String list = "";
        int r=i/8,c=i%8;
        int temp=1;
        String pieceKilled=" ";
        for(int j=-1; j<=1; j++){
            for(int k=-1; k<=1; k++){
                if(k!=0||j!=0){
                try{
                    if(k==0||j==0){
                    while(chessBoard[r+temp*j][c+temp*k].equals(" ")){
                        pieceKilled=" ";
                        chessBoard[r+temp*j][c+temp*k]="R";
                        chessBoard[r][c]=" ";
                        if(kingSafe()){
                            list=list+r+c+(r+temp*j)+(c+temp*k)+pieceKilled;
                        }
                        chessBoard[r+temp*j][c+temp*k]=" ";
                        chessBoard[r][c]="R";
                        temp++;
                    }
                    if(Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))){
                        pieceKilled=chessBoard[r+temp*j][c+temp*k];
                        chessBoard[r+temp*j][c+temp*k]="R";
                        chessBoard[r][c]=" ";
                        if(kingSafe()){
                            list=list+r+c+(r+temp*j)+(c+temp*k)+pieceKilled;
                        }
                        chessBoard[r+temp*j][c+temp*k]=pieceKilled;
                        chessBoard[r][c]="R";
                    }}
                }
                catch (Exception e){}
                temp=1;
            }}
        }
        //System.out.println(list);
        return list;
    }
    public static String possibleN(int i){
        String list = "";
        int r=i/8,c=i%8;
        String pieceKilled=" ";
        for(int j=-1; j<=1; j+=2){
            for(int k=-1; k<=1; k+=2){
                try { pieceKilled=chessBoard[r+j][c+k*2];
                    if(Character.isLowerCase(pieceKilled.charAt(0))||pieceKilled.equals(" ")){

                        chessBoard[r+j][c+k*2]="N";
                        chessBoard[r][c]=" ";
                        if(kingSafe()){
                            list=list+r+c+(r+j)+(c+k*2)+pieceKilled;
                        }
                        chessBoard[r+j][c+k*2]=pieceKilled;
                        chessBoard[r][c]="N";
                    }}
                catch (Exception e){ }
                try{ pieceKilled=chessBoard[r+j*2][c+k];
                    if(Character.isLowerCase(pieceKilled.charAt(0))||pieceKilled.equals(" ")){

                        chessBoard[r+j*2][c+k]="N";
                        chessBoard[r][c]=" ";
                        if(kingSafe()){
                            list=list+r+c+(r+j*2)+(c+k)+pieceKilled;
                        }
                        chessBoard[r+j*2][c+k]=pieceKilled;
                        chessBoard[r][c]="N";
                    }
                }catch (Exception e){}
                }
        }
        //System.out.println(list);
        //System.out.println("Ciao");
        return list;
    }
    public static String possibleB(int i){
        String list = "";
        int r=i/8,c=i%8;
        int temp=1;
        String pieceKilled=" ";
        for(int j=-1; j<=1; j++){
            for(int k=-1; k<=1; k++){
                if(k!=0||j!=0){
                try{
                    if(k!=0&&j!=0){
                        while(chessBoard[r+temp*j][c+temp*k].equals(" ")){
                            pieceKilled=" ";
                            chessBoard[r+temp*j][c+temp*k]="B";
                            chessBoard[r][c]=" ";
                            if(kingSafe()){
                                list=list+r+c+(r+temp*j)+(c+temp*k)+pieceKilled;
                            }
                            chessBoard[r+temp*j][c+temp*k]=" ";
                            chessBoard[r][c]="B";
                            temp++;
                        }
                        if(Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))){
                            pieceKilled=chessBoard[r+temp*j][c+temp*k];
                            chessBoard[r+temp*j][c+temp*k]="B";
                            chessBoard[r][c]=" ";
                            if(kingSafe()){
                                list=list+r+c+(r+temp*j)+(c+temp*k)+pieceKilled;
                            }
                            chessBoard[r+temp*j][c+temp*k]=pieceKilled;
                            chessBoard[r][c]="B";
                        }}
                }
                catch (Exception e){}
                temp=1;
            }}
        }
        //System.out.println(list);
        return list;
    }
    public static String possibleQ(int i){
        String list = "";
        int r=i/8,c=i%8;
        int temp=1;
        String pieceKilled=" ";
        for(int j=-1; j<=1; j++){
            for(int k=-1; k<=1; k++){
                if(k!=0||j!=0){
                    try{
                        while(chessBoard[r+temp*j][c+temp*k].equals(" ")){
                            pieceKilled=" ";
                            chessBoard[r+temp*j][c+temp*k]="Q";
                            chessBoard[r][c]=" ";
                            if(kingSafe()){
                                list=list+r+c+(r+temp*j)+(c+temp*k)+pieceKilled;
                            }
                            chessBoard[r+temp*j][c+temp*k]=" ";
                            chessBoard[r][c]="Q";
                            temp++;
                        }
                        if(Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))){
                            pieceKilled=chessBoard[r+temp*j][c+temp*k];
                            chessBoard[r+temp*j][c+temp*k]="Q";
                            chessBoard[r][c]=" ";
                            if(kingSafe()){
                                list=list+r+c+(r+temp*j)+(c+temp*k)+pieceKilled;
                            }
                            chessBoard[r+temp*j][c+temp*k]=pieceKilled;
                            chessBoard[r][c]="Q";
                        }
                    }
                    catch (Exception e){}
                    temp=1;
                }}
        }
        //System.out.println(list);
        return list;
    }
    public static String possibleK(int i){
        String list = "";
        int r=i/8,c=i%8;
        for(int j=0; j<9; j++){
            try{
                if(j!=4){
                    if(chessBoard[r-1+j/3][c-1+j%3].equals(" ")||Character.isLowerCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0))){
                        String pieceKilled=chessBoard[r-1+j/3][c-1+j%3];
                        kingPositionC=8*(r-1+j/3)+c-1+j%3;
                        int tempKing=kingPositionC;
                        chessBoard[r-1+j/3][c-1+j%3]="K";
                        chessBoard[r][c]=" ";
                        if(kingSafe()){
                            list=list+r+c+(r-1+j/3)+(c-1+j%3)+pieceKilled;
                        }
                        kingPositionC=tempKing;
                        chessBoard[r-1+j/3][c-1+j%3]=pieceKilled;
                        chessBoard[r][c]="K";

                    }
                }
            }
            catch (Exception e){}

        }
        //System.out.println(list);
        return list;
    }

    public static boolean kingSafe() {
        //bishop/queen
        int temp=1;
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    while(" ".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j])) {temp++;}
                    if ("b".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j]) ||
                            "q".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j])) {
                        return false;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        //rook/queen
        for (int i=-1; i<=1; i+=2) {
            try {
                while(" ".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i])) {temp++;}
                if ("r".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i]) ||
                        "q".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i])) {
                    return false;
                }
            } catch (Exception e) {}
            temp=1;
            try {
                while(" ".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8])) {temp++;}
                if ("r".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8]) ||
                        "q".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8])) {
                    return false;
                }
            } catch (Exception e) {}
            temp=1;
        }
        //knight
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    if ("k".equals(chessBoard[kingPositionC/8+i][kingPositionC%8+j*2])) {
                        return false;
                    }
                } catch (Exception e) {}
                try {
                    if ("k".equals(chessBoard[kingPositionC/8+i*2][kingPositionC%8+j])) {
                        return false;
                    }
                } catch (Exception e) {}
            }
        }
        //pawn
        if (kingPositionC>=16) {
            try {
                if ("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8-1])) {
                    return false;
                }
            } catch (Exception e) {}
            try {
                if ("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8+1])) {
                    return false;
                }
            } catch (Exception e) {}
            //king
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    if (i!=0 || j!=0) {
                        try {
                            if ("a".equals(chessBoard[kingPositionC/8+i][kingPositionC%8+j])) {
                                return false;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        return true;
    }



}
