/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.PostBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import util.DataConnect;

/**
 *
 * @author Sirac
 */
public class SavesDAO {

    public static ArrayList<PostBean> getSavedPosts(int userid) {
         ArrayList<PostBean> postlar = new ArrayList<PostBean>();
        try {
                Connection con = DataConnect.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT DISTINCT * FROM POSTS INNER JOIN SAVES ON POSTS.POSTID=SAVES.POSTID WHERE SAVES.USERID = ? ORDER BY SAVES.SAVEID DESC");
                ps.setInt(1,userid);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    String tarih = rs.getString("CREATEDATE");
                    tarih = tarih.substring(0,16);
                    String bugun = String.valueOf(new Timestamp(System.currentTimeMillis()));
                    if(tarih.substring(0,10).equals(bugun.substring(0,10))){
                        if(tarih.substring(11,13).equals(bugun.substring(11,13))){
                            tarih = String.valueOf(Integer.valueOf(bugun.substring(14,16)) - Integer.valueOf(tarih.substring(14,16))) + " dk";
                        }
                        else{
                           if(Integer.valueOf(bugun.substring(11,13)) - Integer.valueOf(tarih.substring(11,13)) == 1 && Integer.valueOf(bugun.substring(14,16)) < Integer.valueOf(tarih.substring(14,16))){

                                tarih = String.valueOf(Integer.valueOf(bugun.substring(14,16)) - Integer.valueOf(tarih.substring(14,16)) + 60) + " dk";
                            
                            }
                            else{
                                if(Integer.valueOf(bugun.substring(14,16)) > Integer.valueOf(tarih.substring(14,16))){
                                    tarih = String.valueOf(Integer.valueOf(bugun.substring(11,13)) - Integer.valueOf(tarih.substring(11,13))) + " saat " + String.valueOf(Integer.valueOf(bugun.substring(14,16)) - Integer.valueOf(tarih.substring(14,16))) + " dk";
                                }
                                else{
                                    if(Integer.valueOf(bugun.substring(14,16)) - Integer.valueOf(tarih.substring(14,16)) == 0){
                                        tarih = String.valueOf(Integer.valueOf(bugun.substring(11,13)) - Integer.valueOf(tarih.substring(11,13))) + " saat";
                                    }
                                    else{
                                        tarih = String.valueOf(Integer.valueOf(bugun.substring(11,13)) - Integer.valueOf(tarih.substring(11,13)) - 1) + " saat " + String.valueOf(Integer.valueOf(bugun.substring(14,16)) - Integer.valueOf(tarih.substring(14,16)) + 60) + " dk";
                                    }
                                }
                            }
                        }
                    }
                    String photouri = rs.getString("PHOTOURI");
                    if (photouri.equals("empty")){
                        photouri = null;
                    }
                    postlar.add(new PostBean(rs.getInt("USERID"),rs.getInt("POSTID"),rs.getString("CONTENT"),rs.getInt("LIKECOUNT"),rs.getInt("COMMENTCOUNT"),tarih,photouri));
                   
                }
                DataConnect.close(con);
                 
        } catch (SQLException ex) {
                System.out.println("Giri?? hatas??");
        }
        
        return postlar;
        
    
    }
    public static boolean Save(int userId, int postId){
        
        try {
                Connection con = DataConnect.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO SAVES (USERID,POSTID) VALUES (?,?)");
                ps.setInt(1, userId);
                ps.setInt(2, postId);
                ps.executeUpdate();
                con.close();
                return true;
        } catch (SQLException ex) {

                return false;
        }
    }
    public static boolean unSave(int userId, int postId){
        
        try {
                Connection con = DataConnect.getConnection();
                PreparedStatement ps = con.prepareStatement("DELETE FROM SAVES WHERE USERID=? AND POSTID=?");
                ps.setInt(1, userId);
                ps.setInt(2, postId);
                ps.executeUpdate();
                con.close();
                return true;
        } catch (SQLException ex) {

                return false;
        }
    }
    public static boolean isSaved(int userId, int postId){
        
        try {
                Connection con = DataConnect.getConnection();
                PreparedStatement ps = con.prepareStatement("Select * from SAVES where USERID = ? and POSTID = ?");
                ps.setInt(1, userId);
                ps.setInt(2, postId);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    con.close();
                    return true;
                    
                }
        } catch (SQLException ex) {

                return false;
        }
        return false;
    }
}
