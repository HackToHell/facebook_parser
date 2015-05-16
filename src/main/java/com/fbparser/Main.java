package com.fbparser;

import java.io.IOException;

/**
 * Created by Gowtham on 5/16/2015.
 */
public class Main {
    public static void main(String args[])throws IOException {
        if(args.length == 2){
            parser.main(args);
        }
        else{
            System.out.println("Usage: fbparser input_html_file_path output_csv_path");
        }
    }
}
