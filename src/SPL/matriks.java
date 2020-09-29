package SPL;

import java.util.Scanner;

/**
 * matriks
 */
public class matriks {

    private int baris,kolom;
    private float[][] tabFloat;

    /*CONSTRUCTOR*/
    public void makeSPL(){
        try (Scanner scan = new Scanner(System.in)) {
            System.out.println("Input your row of matrix : ");
            int row = scan.nextInt();
            System.out.println("Input your column of matrix : ");
            int column = scan.nextInt();
            
            this.setBaris(row);
            this.setKolom(column);
            this.tabFloat = new float[row][column];

            for (int i = 0; i < this.getBaris(); i++) {
                for (int j = 0; j < this.getKolom(); j++) {
                    System.out.println("Masukkan nilai elemen baris ke-" + (i+1) + " kolom ke-" + (j+1) +": ");
                    this.setElmt(scan.nextFloat(), i,j);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Invalid input");
        }

    }

    /*SETTER*/
    public void setBaris(int row){
        this.baris = row;
    }

    public void setKolom(int column){
        this.kolom = column;
    }
    
    public void setElmt(float value, int row, int column){
        this.tabFloat[row][column] = value;
    }

    public void setWholeTabFloat(float[][] tabInput){
        this.tabFloat = tabInput;
    }

    /*GETTER*/
    public int getBaris(){
        return this.baris;
    }

    public int getKolom(){
        return this.kolom;
    }

    public float getElmt(int row, int column){
        return this.tabFloat[row][column];
    }

    /*METHOD*/
    public void copyMatriks(matriks MSource){
        this.baris = MSource.baris;
        this.kolom = MSource.kolom;
        this.tabFloat = MSource.tabFloat;
    }

    private void divideAllbyLeading(int rowLeading, int columnLeading, float[][] MTarget){
        float divider = MTarget[rowLeading][columnLeading];
        for (int j=0; j<this.getKolom();j++){
            float newValue = MTarget[rowLeading][j]/divider;
            MTarget[rowLeading][j] = newValue;
        }
    }

    private void exchangeRow(int row1, int row2, float[][] MTarget){
        float temp;
        for (int j=0; j<this.getKolom(); j++){
            temp = MTarget[row1][j];
            MTarget[row1][j] = MTarget[row2][j];
            MTarget[row2][j] = temp;
        }
    }

    private void searchNonZeroValue(int row, int column, float[][] MTarget){
        
        boolean ketemu = false;
        int i = row;
        int j = column;

        while (i < this.getBaris() && !ketemu && j>=0){
            if (MTarget[i][j]!=0){
                exchangeRow(row, i, MTarget);
                ketemu = true;
            }
            
            if (i==this.getBaris()-1 && ketemu==false){        //belum ketemu dan sudah mentok
                i = row;
                j -= 1;
            }else{
                i+=1;
            }  
        }
    }

    private void makeElmtBeforeLeadingToZero(int row, int column, float[][] MTarget){
        for (int j=0; j<column; j++){
            if (MTarget[row][j]!=0){
                substractionRow(row, j, MTarget);
            }
        }

    }

    private void substractionRow(int row1, int row2, float[][] MTarget){
        float multiplier = MTarget[row1][row2] / MTarget[row2][row2];
        for (int j=0; j<this.getKolom(); j++){
            float valRow1 = MTarget[row1][j];
            float valRow2 = MTarget[row2][j];

            float newValue = valRow1 - (valRow2*multiplier);
            
            MTarget[row1][j] = newValue;
        }
    }

    public float[][] matriksToGauss(){
        
        float[][] MGauss = new float[this.baris][this.kolom];
        MGauss = this.tabFloat;

        int rowLeading=0;
        int columnLeading=0;

        for (int i=0; i<this.getBaris(); i++){
            
            for (int j=0; j<columnLeading+1 && j<this.getKolom(); j++){
                if (i!=rowLeading || j!=columnLeading){
                    if (MGauss[i][j]!=0){
                        makeElmtBeforeLeadingToZero(j, j+1, MGauss);
                    }
                }else{
                    if (MGauss[rowLeading][columnLeading]==0){
                        
                        boolean mentok = false;
                        while (MGauss[rowLeading][columnLeading]==0 && !mentok){
                            searchNonZeroValue(rowLeading, columnLeading, MGauss);
                            makeElmtBeforeLeadingToZero(rowLeading, columnLeading, MGauss);
                        }
                        if (MGauss[rowLeading][columnLeading    ]==0){   // jika setelah dicari element leading masih 0
                            boolean nol = true;
                            int k = columnLeading;      //variabel iterasi di kolom baris i
                            while (nol && k<this.getKolom()){
                                if (MGauss[rowLeading][k]!=0){       //elemen yang tidak bernilai 0 ketemu
                                    nol = false;
                                    divideAllbyLeading(rowLeading, k, MGauss);
                                    columnLeading = k;
                                }else{
                                    k+=1;
                                }
                                
                            }
                            
                               /*else{
                                boolean rowNonZero = false;
                                int l = this.getBaris()-1;       //variabel iterasi baris untuk mengecek baris yang tidak semua elemennya 0
                                while (!rowNonZero && l > i ){
                                    if (!isRowAllZero(l, MGauss)){
                                        exchangeRow(i, l, MGauss);
                                        rowNonZero = true;
                                    }else{
                                        l -= 1;
                                    }
                                }
                            }*/
                        }else{
                            makeElmtBeforeLeadingToZero(i, j, MGauss);
                            divideAllbyLeading(i, j, MGauss);
                        } 
                                       
                    }else if (MGauss[rowLeading][columnLeading]!=1 && MGauss[rowLeading][columnLeading]!=0){
                        divideAllbyLeading(i, j, MGauss);
                    }
                }
            }
            rowLeading+=1;
            columnLeading+=1;
        }



        return MGauss;
    }

}