package Activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.btlandroid.ConnectionHelper;
import com.example.btlandroid.Novel;
import Adapter.NovelAdapter;
import com.example.btlandroid.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountFavorite extends AppCompatActivity{
    Connection connect;
    String ConnectionResult="";
    private SearchView searchView;
    Button btnLogin;
    String User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_favorite);
        Intent intent = getIntent();
        User = intent.getStringExtra("User");
//        Listview yêu thích
        GetFavorite(User);
        ListView novelF = findViewById(R.id.novelFavorite);
        novelF.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Novel> data = new ArrayList<Novel>();
                data = getfavorite(User);
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
//        Nút menu
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
//        Nút logo home
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
                if (User==null || User.equals("")) {
                    User="Đăng nhập";
                }
                String tdn = User;
                if (tdn.equals("Đăng nhập")) {
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), AccountFavorite.class);
                    intent.putExtra("User", tdn);
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
                PopupMenu popupMenu = new PopupMenu(AccountFavorite.this, btnLogin);
                String tdn = String.valueOf(btnLogin.getText().toString().trim());
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
    }
    NovelAdapter nad;
//    Tìm kiếm truyện
    public List<Novel> getnovel(String tt){
        List<Novel> data = new ArrayList<Novel>();
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if(connect!=null) {
                String query;
                if (tt=="") {
                    query = "";
                }
                else {
                    query = "Select Top(3) TenTruyen, g.TenTacGia, AnhMinhHoa from Truyen t RIGHT JOIN TacGia g on g.MaTacGia=t.MaTacGia where TenTruyen LIKE'%"+tt+"%'";
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
        nad = new NovelAdapter(AccountFavorite.this, R.layout.novel_lists, MyNovelList);
        dsnovel.setAdapter(nad);
        nad.notifyDataSetChanged();
    }
//    Load danh sách truyện yêu thích
    public List<Novel> getfavorite(String user){
        List<Novel> data = new ArrayList<Novel>();
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if(connect!=null) {
                String query;
                if (user=="") {
                    query = "";
                }
                else {
                    query = "Select TenTruyen, g.TenTacGia, AnhMinhHoa from Truyen t RIGHT JOIN TacGia g on g.MaTacGia=t.MaTacGia RIGHT JOIN YeuThich f on t.MaTruyen=f.MaTruyen RIGHT JOIN TaiKhoan a on f.TenDangNhap=a.TenDangNhap where a.TenDangNhap=N'"+User+"' ";
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
    public void GetFavorite(String user){
        ListView dsyeuthich = (ListView) findViewById(R.id.novelFavorite);
        List<Novel> MyNovelList = getfavorite(user);
        nad = new NovelAdapter(AccountFavorite.this, R.layout.novel_lists, MyNovelList);
        dsyeuthich.setAdapter(nad);
        nad.notifyDataSetChanged();
    }
}
