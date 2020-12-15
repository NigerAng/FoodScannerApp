package com.example.niger.nigerangyidong_150271176_co3320;
/**
 Learnt and adapted from "How to read CSV files in android application"
 By Indragni Soft Solutions
 https://www.youtube.com/watch?v=svxKakcKnxE
 **/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Access csv file and read it line by line
 * Seperate values by comma
 */
public class CSVReader {
    InputStream inputStream;

    public CSVReader(InputStream is){
        this.inputStream = is;
    }

    public List<String[]> read(){
        List<String[]> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try{
            String line;
            while((line = reader.readLine()) != null){
                String[] row = line.split(",");
                resultList.add(row);
            }

        }catch(IOException e){
            throw new RuntimeException("Error in reading csv file:" + e);
        }finally {
            try{
                inputStream.close();
            }catch (IOException e){
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }
}
