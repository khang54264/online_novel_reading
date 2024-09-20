package Activity;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.example.btlandroid.ConnectionHelper;
import com.example.btlandroid.Novel;
import Adapter.NovelAdapter;
import com.example.btlandroid.R;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    String User;
    Button btnLogin;
    Connection connect;
    String ConnectionResult="";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        User = intent.getStringExtra("User");
        if (User==null || User.equals("")) {
            User="Đăng nhập";
        }
//        Listview tìm kiếm
        ListView novelS = findViewById(R.id.novelSearch);
        novelS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Novel> data = new ArrayList<Novel>();
                data = getnovel(searchView.getQuery().toString().trim());
                Novel novel = data.get(position);
                String tt = novel.getNovelName();
                String mt = "";
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                if(connect==null) {
                    connect=connectionHelper.conclass();
                }
                if(connect!=null) {
                    try {
                        String query = "Select MaTruyen from Truyen where TenTruyen=N'"+tt+"'";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            mt=rs.getString(1).toString().trim();
                        }
                        connect.close();
                    } catch (Exception exception) {
                        Log.e("error ", exception.getMessage());
                    }
                }
                else
                {
                    ConnectionResult="Check Connection";
                }
                Intent intent = new Intent(view.getContext(), NovelMain.class);
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen",mt);
                startActivity(intent);
            }
        });
//        Thanh tìm kiếm
        searchView = findViewById(R.id.searchView);
        GetNovel("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GetNovel(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                GetNovel(query);
                return true;
            }
        });

        ImageButton btnMenu, btnHome, btnFavorite;
//        Nút Menu
        btnMenu = (ImageButton) findViewById(R.id.btnSearch);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout menu = findViewById(R.id.overlayMenu);
                if (menu.getHeight()==0){
                    RelativeLayout.LayoutParams h = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 460);
                    menu.setLayoutParams(h);
                }
                else if (menu.getHeight()>0){
                    RelativeLayout.LayoutParams h = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
                    menu.setLayoutParams(h);
                }
            }
        });
//        Nút Logo Home
        btnHome = findViewById(R.id.logohome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("User",User);
                startActivity(intent);
            }
        });
//        Nút Danh Sách Yêu Thích
        btnFavorite = findViewById(R.id.btnFavoriteList);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tdn = User;
                if (tdn.equals("Đăng nhập")) {
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), AccountFavorite.class);
                    intent.putExtra("User", tdn);
                    startActivity(intent);
                }
            }
        });
//        Nút Login/Register
        btnLogin = findViewById(R.id.btnLoginActivity);
        if (User.equals("") || User.equals("Đăng nhập")){
            btnLogin.setText("Đăng nhập");
        }
        else {
            btnLogin.setText(User);
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnLogin);
                String tdn = User;
                String MaQuyen="1";
                if (tdn.equals("Đăng nhập")) {
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    connect = connectionHelper.conclass();
                    if(connect==null) {
                        connect=connectionHelper.conclass();
                    }
                    if(connect!=null) {
                        try {
                            String query = "Select Quyen from TaiKhoan where TenDangNhap='"+tdn+"'";
                            Statement st = connect.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            while (rs.next()) {
                                MaQuyen=rs.getString(1).toString().trim();
                                }
                            connect.close();
                        } catch (Exception exception) {
                            Log.e("error ", exception.getMessage());
                        }
                    }
                    else
                    {
                        ConnectionResult="Check Connection";
                    }
                    if (MaQuyen.equals("1")){
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_user, popupMenu.getMenu());
                    } else if(MaQuyen.equals("0")) {
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_admin, popupMenu.getMenu());
                    } else {
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_user, popupMenu.getMenu());
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int itemID = item.getItemId() ;
                            if (itemID==R.id.AccountInfor) {
                                Intent intent1 = new Intent(v.getContext(), AccountSetting.class);
                                intent1.putExtra("User", tdn);
                                startActivity(intent1);
                            }else if (itemID==R.id.AccountManage){
                                Intent intent2 = new Intent(v.getContext(), AccountManage.class);
                                intent2.putExtra("User",tdn);
                                startActivity(intent2);
                            }else if (itemID==R.id.LogOut){
                                Intent intent3 = new Intent(v.getContext(), MainActivity.class);
                                intent3.putExtra("User","Đăng nhập");
                                startActivity(intent3);
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            }
        });
//        Các truyện
        ImageButton imgN1,imgN2,imgN3,imgN4,imgN5,imgN6;
        ImageButton imgVN1,imgVN2,imgVN3,imgVN4,imgVN5,imgVN6;
        ImageButton imgF1,imgF2,imgF3,imgF4,imgF5,imgF6;
        imgN1 = findViewById(R.id.illusN1);
        imgN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T01");
                startActivity(intent);
            }
        });
        imgN2 = findViewById(R.id.illusN2);
        imgN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T09");
                startActivity(intent);
            }
        });
        imgN3 = findViewById(R.id.illusN3);
        imgN3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T08");
                startActivity(intent);
            }
        });
        imgN4 = findViewById(R.id.illusN4);
        imgN4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T02");
                startActivity(intent);
            }
        });
        imgN5 = findViewById(R.id.illusN5);
        imgN5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T04");
                startActivity(intent);
            }
        });
        imgN6 = findViewById(R.id.illusN6);
        imgN6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T05");
                startActivity(intent);
            }
        });
        imgF1 = findViewById(R.id.illusF1);
        imgF1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T01");
                startActivity(intent);
            }
        });
        imgF2 = findViewById(R.id.illusF2);
        imgF2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T02");
                startActivity(intent);
            }
        });
        imgF3 = findViewById(R.id.illusF3);
        imgF3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T03");
                startActivity(intent);
            }
        });
        imgF4 = findViewById(R.id.illusF4);
        imgF4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T04");
                startActivity(intent);
            }
        });
        imgF5 = findViewById(R.id.illusF5);
        imgF5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T05");
                startActivity(intent);
            }
        });
        imgF6 = findViewById(R.id.illusF6);
        imgF6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T06");
                startActivity(intent);
            }
        });
        imgVN1 = findViewById(R.id.illusVN1);
        imgVN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T07");
                startActivity(intent);
            }
        });
        imgVN2 = findViewById(R.id.illusVN2);
        imgVN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T08");
                startActivity(intent);
            }
        });
        imgVN3 = findViewById(R.id.illusVN3);
        imgVN3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T09");
                startActivity(intent);
            }
        });
        imgVN4 = findViewById(R.id.illusVN4);
        imgVN4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T10");
                startActivity(intent);
            }
        });
        imgVN5 = findViewById(R.id.illusVN5);
        imgVN5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T11");
                startActivity(intent);
            }
        });
        imgVN6 = findViewById(R.id.illusVN6);
        imgVN6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NovelMain.class);
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                intent.putExtra("User",User);
                intent.putExtra("Ma_Truyen","T12");
                startActivity(intent);
            }
        });
    }
//    Các hàm chức năng
    NovelAdapter nad;
    public List<Novel> getnovel(String tt){
        List<Novel> data = new ArrayList<Novel>();
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if(connect!=null) {
                String query;
                if (tt.equals("")) {
                    query = "";
                }
                else {
                    query = "Select Top(3) TenTruyen, g.TenTacGia, AnhMinhHoa from Truyen t RIGHT JOIN TacGia g on g.MaTacGia=t.MaTacGia where TenTruyen LIKE N'%"+tt+"%'";
                }
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    String name = rs.getString(1);
                    String author = rs.getString(2);
                    String url = rs.getString(3);
                    int imageResource = getResources().getIdentifier(url, null, getPackageName());
                    data.add(new Novel(rs.getString(1),rs.getString(2),imageResource));
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
    public void GetNovel(String tt){
        String TenTruyen = tt;
        ListView dsnovel = (ListView) findViewById(R.id.novelSearch);
        List<Novel> MyNovelList = getnovel(TenTruyen);
        nad = new NovelAdapter(MainActivity.this, R.layout.novel_lists, MyNovelList);
        dsnovel.setAdapter(nad);
        nad.notifyDataSetChanged();
    }
}