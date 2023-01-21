package cn.minloha.Type;
import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private Object[][] list;
    public Matrix(Object[][] list){this.list=list;}

    public Matrix(List<List<?>> list){
        this.list = new Object[list.size()][list.get(0).size()];
        for(int line=0;line<list.size();line++){
            for(int point=0;point<list.get(line).size();point++){
                this.list[line][point] = list.get(line).get(point);
            }
        }
    }

    public Matrix(List<?> list, int width, int height){
        int index = 0;
        this.list = new Object[width][height] ;
        for(int y = 0;y<height;y++){
            for(int x = 0;x<width;x++){
                this.list[x][y] = list.get(index);
                index++;
            }
        }
    }

    public Vector getDimen(){
        return new Vector(list.length,list[0].length);
    }

    public Object[][] getMatrix(){
        return this.list;
    }

    public static Matrix OutProduct(Matrix m1 , Matrix m2) {
        Object[][] Mlist = new Object[m1.getMatrix().length][m2.getMatrix()[0].length];
        for(int i = 0;i<Mlist.length;i++){
            for(int j = 0;j<Mlist[0].length;j++){
                double res = 0.0;
                for(int t = 0;t<m1.getMatrix()[0].length;t++){
                     res += (Double)m1.getMatrix()[i][t] * (Double)m2.getMatrix()[t][j];
                }
                Mlist[i][j] = res;
            }
        }

        return new Matrix(Mlist);
    }

    public static Matrix pointMul(Matrix m1 , Matrix m2){
        Object[][] mat = new Object[0][];
        if(m1.getDimen().vecCompare(m2.getDimen())){
            mat = new Object[m1.list.length][m1.list[0].length];
            for(int x=0;x<m1.list.length;x++){
                for(int y=0;y<m2.list[0].length;y++){
                    mat[y][x] = (Double) m1.list[x][y] * (Double) m2.list[x][y];
                }
            }
        }
        return new Matrix(mat);
    }

    public static Matrix addRowVector(Matrix aim, Vector vector){
        Object[][] back = new Object[aim.getMatrix().length+1][aim.getMatrix()[0].length];
        for(int x = 0;x<aim.getMatrix().length;x++){
            for(int y = 0;y<aim.getMatrix()[0].length;y++){
                back[x][y] = aim.getMatrix()[x][y];
            }
        }
        for(int y = 0;y<aim.getMatrix()[0].length;y++){
            back[aim.getMatrix().length + 1][y] = vector.getAsList().get(y);
        }
        return new Matrix(back);
    }

    public static Matrix pointMul(double k,Matrix m1){
        Object[][] back = m1.getMatrix();
         for(int x = 0;x<m1.getMatrix().length;x++){
                for(int y = 0;y<m1.getMatrix()[x].length;y++){
                    back[x][y] = ((Double) back[x][y] * k);
                }
         }
         return new Matrix(back);
    }

    public static Matrix add(Matrix m1,Matrix m2){
        Object[][] mat = new Object[m1.getMatrix().length][m1.getMatrix()[0].length];
        for(int x = 0;x<m1.getMatrix().length;x++){
            for(int y = 0;y<m1.getMatrix()[x].length;y++){
                mat[x][y] = (Double)m1.getMatrix()[x][y] + (Double)m2.getMatrix()[x][y];
            }
        }
        return new Matrix(mat);
    }

    public List<Vector> getColumnVector(){
        List<Vector> list = new ArrayList<>();
        for(int i = 0;i<this.list[0].length;i++){
            List<Double> temp = new ArrayList<>();
            for (Object[] objects : this.list) {
                temp.add((Double) objects[i]);
            }
            list.add(new Vector(temp));
        }
        return list;
    }

    public static Matrix superMatrix(Vector ...column){
        Object[][] ooo = new Object[column.length][column[0].getDimens()];
        for(int i = 0;i<column.length;i++){
            for(int j = 0;j<column[i].getDimens();j++){
                ooo[i][j] = (Object) column[i].getAsList().get(j);
            }
        }
        Matrix m = new Matrix(ooo);
        return m.Translate();
    }

    public Matrix Translate(){
        Object[][] back = new Object[this.list[0].length][this.list.length];
        for(int x = 0;x<this.list.length;x++){
            for(int y = 0;y<this.list[x].length;y++){
                back[y][x] = this.list[x][y];
            }
        }
        this.list = back;
        return new Matrix(back);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int line=0;line<list.length;line++){
            sb.append("W:[");
            for(int point=0;point<list[line].length;point++){
                sb.append(list[line][point]).append(point==list[line].length-1?"":",");
            }
            sb.append("]");
            if(line != list.length-1) sb.append(",\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
