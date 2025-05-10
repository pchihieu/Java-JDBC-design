package com.vgb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PrintToStandardOutput {
	
	    public static void printThisPuppy() {
	        String fileName = "data/output.txt";
	        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                System.out.println(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	

}
