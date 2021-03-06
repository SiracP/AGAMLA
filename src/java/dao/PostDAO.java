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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import util.DataConnect;

/**
 *
 * @author Sirac
 */
public class PostDAO {
    public static ArrayList<PostBean> getPosts(){
        ArrayList<PostBean> postlar = new ArrayList<PostBean>();
        try {
                Connection con = DataConnect.getConnection();
                PreparedStatement ps = con.prepareStatement("Select * from POSTS ORDER BY CREATEDATE DESC");
                
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
    
    public static PostBean getPost(int postid){
        PostBean post = new PostBean();
        
        try {
                Connection con = DataConnect.getConnection();
                PreparedStatement ps = con.prepareStatement("Select * from POSTS where POSTID = ? ORDER BY CREATEDATE DESC");
                ps.setInt(1, postid);
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
                    post = new PostBean(rs.getInt("USERID"),rs.getInt("POSTID"),rs.getString("CONTENT"),rs.getInt("LIKECOUNT"),rs.getInt("COMMENTCOUNT"),tarih,photouri);
                   
                }
                DataConnect.close(con);
                 
        } catch (SQLException ex) {
                System.out.println("Giri?? hatas??");
        }
        
        return post;
    }
    
    public static ArrayList<PostBean> homeSayfasiPostlariGetir(int userId){
        
        //SELECT DISTINCT POSTS.POSTID, POSTS.USERID, POSTS.CONTENT, POSTS.LIKECOUNT, POSTS.COMMENTCOUNT, POSTS.CREATEDATE, POSTS.PHOTOURI, POSTS.VIDEOURI FROM POSTS INNER JOIN FOLLOWERS ON POSTS.USERID=FOLLOWERS.FOLLOWINGID WHERE FOLLOWERS.FOLLOWERID = 7 OR POSTS.USERID = 7 ORDER BY CREATEDATE DESC
        
        ArrayList<PostBean> postlar = new ArrayList<PostBean>();
        try {
                Connection con = DataConnect.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT DISTINCT POSTS.POSTID, POSTS.USERID, POSTS.CONTENT, POSTS.LIKECOUNT, POSTS.COMMENTCOUNT, POSTS.CREATEDATE, POSTS.PHOTOURI FROM POSTS LEFT JOIN FOLLOWERS ON POSTS.USERID=FOLLOWERS.FOLLOWINGID WHERE FOLLOWERS.FOLLOWERID = ? OR POSTS.USERID = ? ORDER BY CREATEDATE DESC");
                ps.setInt(1,userId);
                ps.setInt(2,userId);
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
   
}
