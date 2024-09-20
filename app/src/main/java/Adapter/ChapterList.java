package Adapter;

import com.example.btlandroid.ConnectionHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChapterList {
    Connection connect;
    String ConnectionResult="";
    public List<Map<String,String>>getlist(String mt){
        List<Map<String,String>> data = null;
        data = new ArrayList<Map<String,String>>();
        String MaTruyen="";
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if(connect!=null) {
                String query = "Select * from ChuongTruyen where MaTruyen='"+mt+"' order by SoChuong ASC";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    Map<String,String> dtname = new HashMap<String,String>();
                    dtname.put("chapterNumber",rs.getString(2));
                    dtname.put("chapterTitle",rs.getString(3));
                    data.add(dtname);
                }
                ConnectionResult="success";
                connect.close();
            }
            else{
                ConnectionResult="failed";
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return data;
    }
}
