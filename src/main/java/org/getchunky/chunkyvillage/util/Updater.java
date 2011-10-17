package org.getchunky.chunkyvillage.util;

import org.bukkit.plugin.Plugin;
import org.getchunky.chunkyvillage.ChunkyVillage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Updater {
    private static Plugin plugin;

    public static Boolean updateCheck(String address, String file){
        URLConnection conn = null;
        try {
            Plugin plugin = ChunkyVillage.getInstance();
            URL url = new URL (address);
            conn = url.openConnection();
            File localfile = new File("plugins",file);
            long lastmodifiedurl = conn.getLastModified();
            long lastmodifiedfile = localfile.lastModified();
            if (lastmodifiedurl > lastmodifiedfile){
                System.out.println("Updating...");
                download(address, localfile);
                return true;
            }else{
                System.out.println("No updates available :)");
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        plugin.getServer().notify();
        return false;
    }
    public static void download(String address, File output){
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try{
            URL url = new URL (address);
            out = new BufferedOutputStream(new FileOutputStream(output));
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1){
                out.write(buffer,0,numRead);
                numWritten += numRead;
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                if (in != null){
                    in.close();
                }
                if (out != null){
                    out.close();
                }
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }
}