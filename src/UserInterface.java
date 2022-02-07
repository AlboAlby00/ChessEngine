import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener {

    static int exitBarHeight = 35;
    static int squareSize=64;
    static int x=0, y=0;
    static BufferedImage piecesImage;
    static Image[] pieces;
    private int mouseClickedX,mouseClickedY,mouseReleasedX,mouseReleasedY,mouseX,mouseY,mousePressedX,mousePressedY;

    public UserInterface(){
        loadPieces();
        addMouseListener(this);
        addMouseMotionListener(this);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);
        drawBoard(g);
        drawPieces(g);





    }

    public void drawBoard(Graphics g){
        boolean white=true;
        for(int xb=0; xb<8; xb++){
            for(int yb=0; yb<8; yb++){
                if(white) g.setColor(Color.white.darker());
                else g.setColor(new Color(125,135,150));
                g.fillRect(xb*squareSize,yb*squareSize,squareSize,squareSize);
                white=!white;
            }
         white=!white;
        }
    }

    public void drawPieces(Graphics g){
        int k= AlphaBetaChess.humanAsWhite==1 ? 0 : 6;
        for(int i=0; i<64; i++){
            if(i!=mousePressedY/squareSize*8+mousePressedX/squareSize||mousePressedX==0){
            switch(AlphaBetaChess.chessBoard[i/8][i%8]){
                case "P":
                    g.drawImage(pieces[5+k],i%8*64,i/8*64,this);
                    break;
                case "R":
                    g.drawImage(pieces[4+k],i%8*64,i/8*64,this);
                    break;
                case "N":
                    g.drawImage(pieces[3+k],i%8*64,i/8*64,this);
                    break;
                case "B":
                    g.drawImage(pieces[2+k],i%8*64,i/8*64,this);
                    break;
                case "Q":
                    g.drawImage(pieces[1+k],i%8*64,i/8*64,this);
                    break;
                case "K":
                    g.drawImage(pieces[0+k],i%8*64,i/8*64,this);
                    break;
                case "p":
                    g.drawImage(pieces[11-k],i%8*64,i/8*64,this);
                    break;
                case "r":
                    g.drawImage(pieces[10-k],i%8*64,i/8*64,this);
                    break;
                case "n":
                    g.drawImage(pieces[9-k],i%8*64,i/8*64,this);
                    break;
                case "b":
                    g.drawImage(pieces[8-k],i%8*64,i/8*64,this);
                    break;
                case "q":
                    g.drawImage(pieces[7-k],i%8*64,i/8*64,this);
                    break;
                case "k":
                    g.drawImage(pieces[6-k],i%8*64,i/8*64,this);
                    break;
            }}
            else{
                switch(AlphaBetaChess.chessBoard[mousePressedY/squareSize][mousePressedX/squareSize]){
                    case "P":
                        g.drawImage(pieces[5+k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "R":
                        g.drawImage(pieces[4+k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "N":
                        g.drawImage(pieces[3+k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "B":
                        g.drawImage(pieces[2+k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "Q":
                        g.drawImage(pieces[1+k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "K":
                        g.drawImage(pieces[0+k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "p":
                        g.drawImage(pieces[11-k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "r":
                        g.drawImage(pieces[10-k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "n":
                        g.drawImage(pieces[9-k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "b":
                        g.drawImage(pieces[8-k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "q":
                        g.drawImage(pieces[7-k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                    case "k":
                        g.drawImage(pieces[6-k],mouseX-squareSize/2,mouseY-squareSize/2,this);
                        break;
                }repaint();}
            repaint();

        }
    }

    public static void loadPieces(){
        try{
            piecesImage= ImageIO.read(new File("Images/chess.png"));
            //getScaledInstance(64*5,64*2,Image.SCALE_SMOOTH);
        } catch (Exception e){e.printStackTrace();}
        int orWidth=piecesImage.getWidth()/6, orHeight = piecesImage.getWidth()/6;
        pieces=new Image[12];
        for(int j=0; j<2; j++ ){
            for(int i=0;i<6; i++ ){
                pieces[j*6+i]=piecesImage.getSubimage(i*orWidth,j*orHeight,orWidth,orHeight).getScaledInstance(64,64,Image.SCALE_SMOOTH);
            }
        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {

        mouseClickedX=e.getX();
        mouseClickedY=e.getY();
        System.out.println("click: "+(mouseClickedX)+" "+(mouseClickedY));

        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressedX=e.getX();
        mousePressedY=e.getY();
        //System.out.println((mouseX/64)+" "+(mouseY/64));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseReleasedX=e.getX();
        mouseReleasedY=e.getY();
        String move;
        if(e.getButton()==MouseEvent.BUTTON1){
            if(mousePressedY/squareSize==1&&AlphaBetaChess.chessBoard[mousePressedY/squareSize][mousePressedX/squareSize].equals("P")){
                Scanner in= new Scanner(System.in);
                System.out.println("ciao");
                String promotionPiece="Q";
                /*Set<String> promotionPieces= new HashSet<>();
                promotionPieces.add("Q");promotionPieces.add("N");promotionPieces.add("R");promotionPieces.add("B");
                while(!promotionPieces.contains(promotionPiece)){
                    System.out.println("select piece: Q N R B");
                    promotionPiece=in.next();
                }*/
                move=""+(mousePressedX/squareSize)+(mouseReleasedX/squareSize)+AlphaBetaChess.chessBoard[mouseReleasedY/squareSize][mouseReleasedX/squareSize]+promotionPiece+"P";

            }
            else{
                move=""+(mousePressedY/squareSize)+(mousePressedX/squareSize)+(mouseReleasedY/squareSize)+(mouseReleasedX/squareSize)+AlphaBetaChess.chessBoard[mouseReleasedY/squareSize][mouseReleasedX/squareSize];
            }
            String userPossibilities = AlphaBetaChess.possibleMoves();
            if(userPossibilities.replaceAll(move,"").length()<userPossibilities.length()){
                mousePressedX=0;
                mousePressedY=0;
                AlphaBetaChess.makeMove(move);
                paintComponent(getGraphics());
                AlphaBetaChess.flipBoard();
                AlphaBetaChess.makeMove(AlphaBetaChess.alphaBeta(AlphaBetaChess.globalDepth,Integer.MAX_VALUE,Integer.MIN_VALUE,"",0));
                AlphaBetaChess.flipBoard();
                repaint();
                //System.out.println("move: "+move);
            }
            //System.out.println("release: "+(mouseReleasedX/64)+" "+(mouseReleasedY/64));
        }
        repaint();
        mousePressedX=0;
        mousePressedY=0;

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX=e.getX();
        mouseY=e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX=e.getX();
        mouseY=e.getY();
    }
}
