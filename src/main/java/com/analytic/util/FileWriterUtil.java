package com.analytic.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.google.gson.JsonArray;

public enum FileWriterUtil {
	
	INSTANCE;

    public static final String[] DEFAULT_KEYS = { "date", "website", "visits" };
    public static final String EMPTY_JSON = "{}";

    public void writeData(File dir, String filename, JsonArray array) throws IOException {
        // write json array into output file
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, filename)), "utf-8"));
            writer.write(array.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }
    
    public void emptyDir(File dir) {
    	if (!dir.exists()) {
    		dir.mkdirs();
        } else {
            // TODO: empty output dir properly
            boolean deleted = true;
            for (File file : dir.listFiles()) {
                if (!file.delete()) {
                    deleted = false;
                }
            }
            if (!deleted) {
                System.err.println("Not able to empty folder " + dir.getAbsolutePath());
            }
        }
    }
}