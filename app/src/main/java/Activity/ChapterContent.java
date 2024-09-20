package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btlandroid.ConnectionHelper;
import com.example.btlandroid.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChapterContent extends AppCompatActivity{
    Connection connect;
    String ConnectionResult="";
    public int p, c, n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_content);
        Intent intent = getIntent();
        String MaTruyen = intent.getStringExtra("Ma_Truyen");
        String SoChuong = intent.getStringExtra("So_Chuong");
        TextView cnumber, ctitle, ccontent1, ccontent2, ccontent3,ccontent4, ccontent5,ccontent6,ccontent7,ccontent8,ccontent9,ccontent10;
        cnumber = findViewById(R.id.chapterNumber);
        ctitle = findViewById(R.id.chapterTitle);
        ccontent1 = findViewById(R.id.chapterContent1);
        ccontent2 = findViewById(R.id.chapterContent2);
        ccontent3 = findViewById(R.id.chapterContent3);
        ccontent4 = findViewById(R.id.chapterContent4);
        ccontent5 = findViewById(R.id.chapterContent5);
        ccontent6 = findViewById(R.id.chapterContent6);
        ccontent7 = findViewById(R.id.chapterContent7);
        ccontent8 = findViewById(R.id.chapterContent8);
        ccontent9 = findViewById(R.id.chapterContent9);
        ccontent10 = findViewById(R.id.chapterContent10);
        Button btnP, btnH, btnN;
        btnP = findViewById(R.id.chapterPrevious);
        btnH = findViewById(R.id.novelMain);
        btnN = findViewById(R.id.chapterNext);
        ScrollView svc = findViewById(R.id.scrollViewChapter);
        ConnectionHelper connectionHelper = new ConnectionHelper();
        connect = connectionHelper.conclass();
        if(connect==null) {
            connect=connectionHelper.conclass();
        }
        if(connect!=null) {
            try {
                String query = "Select * from ChuongTruyen where MaTruyen='"+MaTruyen+"' and Chuong='"+SoChuong+"';";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    cnumber.setText(rs.getString(2));
                    ctitle.setText(rs.getString(3));
                    ccontent1.setText(rs.getString(4).toString());
                    ccontent2.setText(rs.getString(5).toString());
                    ccontent3.setText(rs.getString(6).toString());
                    ccontent4.setText(rs.getString(7).toString());
                    ccontent5.setText(rs.getString(8).toString());
                    ccontent6.setText(rs.getString(9).toString());
                    ccontent7.setText(rs.getString(10).toString());
                    ccontent8.setText(rs.getString(11).toString());
                    ccontent9.setText(rs.getString(12).toString());
                    ccontent10.setText(rs.getString(13).toString());
                }
            } catch (Exception exception) {
                Log.e("error ", exception.getMessage());
            }
        }
        else
        {
            ConnectionResult="Check Connection";
        }
        p = Integer.parseInt(SoChuong)-1;
        c = Integer.parseInt(SoChuong);
        n = Integer.parseInt(SoChuong)+1;
        btnP.setEnabled(true);
        btnN.setEnabled(true);
        int kiemtra = 0;
        if (c==0){
            btnP.setEnabled(false);
            btnN.setEnabled(true);
        }
        else {
            if(connect!=null) {
                try {
                    String query = "Select count(Chuong) from ChuongTruyen where MaTruyen='"+MaTruyen+"'";
                    Statement st = connect.createStatement();
                    ResultSet rs = st.executeQuery(query);

                    while (rs.next()) {
                        kiemtra = Integer.parseInt(rs.getString(1));
                        if (n==kiemtra) {
                            btnP.setEnabled(true);
                            btnN.setEnabled(false);
                        }
                    }
                } catch (Exception exception) {
                    Log.e("error ", exception.getMessage());
                }
            }
            else
            {
                ConnectionResult="Check Connection";
            }
        }
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView svc = findViewById(R.id.scrollViewChapter);
                btnP.setEnabled(true);
                btnN.setEnabled(true);
                if(connect!=null) {
                    try {
                        String query = "Select * from ChuongTruyen where MaTruyen='"+MaTruyen+"' and Chuong='"+p+"';";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);

                        while (rs.next()) {
                            cnumber.setText(rs.getString(2).toString());
                            ctitle.setText(rs.getString(3).toString());
                            ccontent1.setText(rs.getString(4).toString());
                            ccontent2.setText(rs.getString(5).toString());
                            ccontent3.setText(rs.getString(6).toString());
                            ccontent4.setText(rs.getString(7).toString());
                            ccontent5.setText(rs.getString(8).toString());
                            ccontent6.setText(rs.getString(9).toString());
                            ccontent7.setText(rs.getString(10).toString());
                            ccontent8.setText(rs.getString(11).toString());
                            ccontent9.setText(rs.getString(12).toString());
                            ccontent10.setText(rs.getString(13).toString());
                            svc.fullScroll(ScrollView.FOCUS_UP);
                        }
                    } catch (Exception exception) {
                        Log.e("error ", exception.getMessage());
                    }
                }
                else
                {
                    ConnectionResult="Check Connection";
                }
                c = p;
                p = c-1;
                n = c+1;
                int kiemtra = 0;
                if (c==0){
                    btnP.setEnabled(false);
                    btnN.setEnabled(true);
                }
                else {
                    if(connect!=null) {
                        try {
                            String query = "Select count(Chuong) from ChuongTruyen where MaTruyen='"+MaTruyen+"'";
                            Statement st = connect.createStatement();
                            ResultSet rs = st.executeQuery(query);

                            while (rs.next()) {
                                kiemtra = Integer.parseInt(rs.getString(1));
                                if (n==kiemtra) {
                                    btnP.setEnabled(true);
                                    btnN.setEnabled(false);
                                }
                            }
                        } catch (Exception exception) {
                            Log.e("error ", exception.getMessage());
                        }
                    }
                    else
                    {
                        ConnectionResult="Check Connection";
                    }
                }
                svc.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        btnH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                intent.putExtra("Ma_Truyen",MaTruyen);
                startActivity(intent);
            }
        });
        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView svc = findViewById(R.id.scrollViewChapter);
                btnP.setEnabled(true);
                btnN.setEnabled(true);
                if(connect!=null) {
                    try {
                        String query = "Select * from ChuongTruyen where MaTruyen='"+MaTruyen+"' and Chuong='"+n+"';";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);

                        while (rs.next()) {
                            cnumber.setText(rs.getString(2));
                            ctitle.setText(rs.getString(3));
                            ccontent1.setText(rs.getString(4).toString());
                            ccontent2.setText(rs.getString(5).toString());
                            ccontent3.setText(rs.getString(6).toString());
                            ccontent4.setText(rs.getString(7).toString());
                            ccontent5.setText(rs.getString(8).toString());
                            ccontent6.setText(rs.getString(9).toString());
                            ccontent7.setText(rs.getString(10).toString());
                            ccontent8.setText(rs.getString(11).toString());
                            ccontent9.setText(rs.getString(12).toString());
                            ccontent10.setText(rs.getString(13).toString());
                            svc.fullScroll(ScrollView.FOCUS_UP);
                        }
                    } catch (Exception exception) {
                        Log.e("error ", exception.getMessage());
                    }
                }
                else
                {
                    ConnectionResult="Check Connection";
                }
                c = n;
                p = c-1;
                n = c+1;
                int kiemtra = 0;
                if (c==0){
                    btnP.setEnabled(false);
                    btnN.setEnabled(true);
                }
                else {
                    if(connect!=null) {
                        try {
                            String query = "Select count(Chuong) from ChuongTruyen where MaTruyen='"+MaTruyen+"'";
                            Statement st = connect.createStatement();
                            ResultSet rs = st.executeQuery(query);

                            while (rs.next()) {
                                kiemtra = Integer.parseInt(rs.getString(1));
                                if (n==kiemtra) {
                                    btnP.setEnabled(true);
                                    btnN.setEnabled(false);
                                }
                            }
                        } catch (Exception exception) {
                            Log.e("error ", exception.getMessage());
                        }
                    }
                    else
                    {
                        ConnectionResult="Check Connection";
                    }
                }
                svc.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }
}
