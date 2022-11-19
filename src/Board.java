public class Board {
    private Tile[][] tiles;
    private int size;
    private int[] empty_tile = new int[2];
    public Board(int size){
        tiles = new Tile[size][size];
        this.size = size;
        int tile_number = 0;
        for (int i = 0;i <size;i++){
            for (int j = 0; j < size; j++){
                tiles[i][j] = new Tile(Integer.toString(tile_number));
                tile_number++;
            }
        }
        tiles[0][0] = null;
        empty_tile[0] = 0;empty_tile[1] = 0;
    }
    public int size(){
        return size;
    }
    public Tile[][] getTiles(){
        return tiles;
    }
    public void left(){
        try {
            tiles[empty_tile[0]][empty_tile[1]] = tiles[empty_tile[0]][empty_tile[1] + 1];
            tiles[empty_tile[0]][empty_tile[1]+1] = null;
            empty_tile[1]+=1;
        } catch (IndexOutOfBoundsException e){}
    }
    public void right(){
        try {
            tiles[empty_tile[0]][empty_tile[1]] = tiles[empty_tile[0]][empty_tile[1] - 1];
            tiles[empty_tile[0]][empty_tile[1]-1] = null;
            empty_tile[1]-=1;
        } catch (IndexOutOfBoundsException e){}
    }
    public void up(){
        try {
            tiles[empty_tile[0]][empty_tile[1]] = tiles[empty_tile[0] + 1][empty_tile[1]];
            tiles[empty_tile[0]+1][empty_tile[1]] = null;
            empty_tile[0]+=1;
        } catch (IndexOutOfBoundsException e){}
    }
    public void down(){
        try {
            tiles[empty_tile[0]][empty_tile[1]] = tiles[empty_tile[0] - 1][empty_tile[1]];
            tiles[empty_tile[0]-1][empty_tile[1]] = null;
            empty_tile[0]-=1;
        } catch (IndexOutOfBoundsException e){}
    }
}