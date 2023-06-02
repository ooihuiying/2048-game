import java.util.*;

public class Game{
    public static void main(String[] args){
        // initialise board
        BoardService service = new BoardService();
        Scanner sc = new Scanner(System.in);
        while(true){
            String command = sc.nextLine();
            if(command.equals("EXIT")) break;
           
            if(command.equals(Move.UP.name())){
                service.move(Move.UP);
            } else if (command.equals(Move.DOWN.name())){
                service.move(Move.DOWN);
            } else if (command.equals(Move.LEFT.name())){
                service.move(Move.LEFT);
            } else {
                service.move(Move.RIGHT);
            }

            if(service.hasGameBeenWon()){
                System.out.println("CONGRATS!!! 2048 number has been formed :)");
                break;
            }
        }
        sc.close();
    }


}

enum Move{
    UP, DOWN, LEFT, RIGHT
}

class BoardService{
    Board board;

    public BoardService(){
        this.board = new Board();
        board.addRandomCell();
        board.addRandomCell();
        System.out.println("Initialising Board ... ");
        System.out.println(board); 
    }

    public void move(Move move){
        switch(move){
            case UP:
                board.slideUp();
                break;
            case DOWN:
                board.slideDown();
                break;
            case LEFT:
                board.slideLeft();
                break;
            case RIGHT:
                board.slideRight();
                break;
        }

        board.addRandomCell();
        System.out.println(board); 
    }

    public boolean hasGameBeenWon(){
        return board.hasGameBeenWon();
    }

}

class Board {
    Cell[][] items;

    public Board(){
        items = new Cell[4][4];
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                items[i][j]=new Cell(0);
            }
        }
    }

    public Boolean hasGameBeenWon(){
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(items[i][j].value == 2048) return true;
            }
        }
        return false;
    }

    public void putNonZerosToLeft(Cell[] row){
        int count = 0;
        for(int i=0; i<row.length; i++){
            Cell curr = row[i];
            if(curr.value != 0){
                row[count++] = row[i];
            }
        }
        while(count<row.length){
            row[count++]= new Cell(0);
        }
    }

    // right to left
    public void slide(Cell[] row){
        putNonZerosToLeft(row);
    
        // Combine similar numbers
        for(int i=0; i<row.length-1; i++){
            if(row[i].value==row[i+1].value){
                row[i].value*=2;
                row[i+1].value=0;
            }
        }

        putNonZerosToLeft(row);
    }

    public void slideLeft(){
        for(int i=0; i<4; i++){
            slide(items[i]);
        }
    }

    public void reverse(Cell[] items, Cell[] copy){
        int count2 = 0;
        for(int j=3; j>=0; j--){
            items[j]=copy[count2++];
        }
    }

    public void slideRight(){
        for(int i=0; i<4; i++){
            Cell[] reversedArr = new Cell[4];
            reverse(reversedArr, items[i]);
            slide(reversedArr);
            reverse(items[i], reversedArr);
        }
    }
    
    // Up to down map to left to right
    public void makeColIntoRow(Cell[] row, int col){
        for(int j=0; j<4; j++){
            row[j]=items[j][col];
        }
    }

    public void makeRowIntoCol(Cell[] colArr, int col){
        for(int j=0; j<4; j++){
            items[j][col]=colArr[j];
        }
    }

    public void slideUp(){
        for(int i=0; i<4; i++){
            Cell[] row = new Cell[4];
            makeColIntoRow(row, i);
            slide(row);
            makeRowIntoCol(row, i);
        }
    }

    public void slideDown(){
        for(int i=0; i<4; i++){
            Cell[] row = new Cell[4];
            makeColIntoRow(row, i);
            Cell[] reversedArr = new Cell[4];
            reverse(reversedArr, row);
            slide(reversedArr);
            reverse(row, reversedArr);
            makeRowIntoCol(row, i);
        }
    }

    public boolean isGridFull(){
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(items[i][j].value == 0) return false;
            }
        }
        return true;
    }

    public void addRandomCell(){
        if(isGridFull()) return;
        while(true){
            int randomRow = (int) (Math.random() * 4);
            int randomCol = (int) (Math.random() * 4);
            if(items[randomRow][randomCol].value == 0){
                items[randomRow][randomCol].value = 2;
                break;
            }
        }
    }

    public String toString(){
        String result = "";
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                result += items[i][j].toString() + " ";
            }
            result += "\n";
        }
        return result;
    }

}


class Cell{
    int value;
    public Cell(int val){
        this.value = val;
    }
    public String toString(){
        return String.valueOf(value);
    }
}
