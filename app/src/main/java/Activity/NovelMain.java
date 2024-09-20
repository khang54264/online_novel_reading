package Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import Adapter.ChapterList;
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
import java.util.Map;

public class NovelMain extends AppCompatActivity{
    Button btnLogin;
    private SearchView searchView;
    String User;
    Connection connect;
    String ConnectionResult="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novel_main);
        Intent intent = getIntent();
        String MaTruyen = intent.getStringExtra("Ma_Truyen");
        User = intent.getStringExtra("User");
        if (User==null || User.equals("")) {
            User="Đăng nhập";
        }
        ImageView anhMinhHoa;
        anhMinhHoa = findViewById(R.id.anhminhhoa);
        TextView tenNovel, tenTacGia, tinhTrang;
        tenNovel = findViewById(R.id.tentieuthuyet);
        tenTacGia = findViewById(R.id.tentacgia);
        tinhTrang = findViewById(R.id.tinhtrang);
        Button showChaps = findViewById(R.id.chapterButton);
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
                PopupMenu popupMenu = new PopupMenu(NovelMain.this, btnLogin);
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
//      Thông tin truyện
        ListView dschapters = findViewById(R.id.chapters);
        ConnectionHelper connectionHelper = new ConnectionHelper();
        connect = connectionHelper.conclass();
        if(connect==null) {
            connect=connectionHelper.conclass();
        }
        if(connect!=null) {
            try {
                String query = "Select * from Truyen where MaTruyen='"+MaTruyen+"'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    tenNovel.setText(rs.getString(2));
                    tinhTrang.setText(rs.getString(5));

                    String url = rs.getString(3);
                    int imageResource = getResources().getIdentifier(url, null, getPackageName());
                    Drawable res = getResources().getDrawable(imageResource);
                    anhMinhHoa.setImageDrawable(res);
                }
                query = "SELECT t.MaTacGia,g.TenTacGia FROM Truyen t INNER JOIN TacGia g on g.MaTacGia=t.MaTacGia WHERE t.MaTruyen='"+MaTruyen+"'";
                st = connect.createStatement();
                rs = st.executeQuery(query);

                while (rs.next()) {
                    tenTacGia.setText(rs.getString(2));
                }

                String state="0";
                ImageButton favor = findViewById(R.id.FavoriteState);
                query = "Select count(MaTruyen) from YeuThich where TenDangNhap=N'"+User+"' and MaTruyen=N'"+MaTruyen+"'";
                st = connect.createStatement();
                rs = st.executeQuery(query);

                while (rs.next()) {
                    state = rs.getString(1).toString().trim();
                }
                if(state.equals("0")) {
                    favor.setBackgroundResource(R.drawable.favorite_icon);
                } else {
                    favor.setBackgroundResource(R.drawable.favorite_full_icon);
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
        //Danh sách yêu thích
        dschapters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ChapterContent.class);
                intent.putExtra("Ma_Truyen",MaTruyen);
                String i = String.valueOf(position);
                intent.putExtra("So_Chuong",i);
                startActivity(intent);
            }
        });
        //    Nút yêu thích
        ImageButton favor = findViewById(R.id.FavoriteState);
        favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                if(connect==null) {
                    connect=connectionHelper.conclass();
                }
                if(connect!=null) {
                    try {
                        String state="0";
                        ImageButton favor = findViewById(R.id.FavoriteState);
                        String query = "Select count(MaTruyen) from YeuThich where TenDangNhap=N'"+User+"' and MaTruyen=N'"+MaTruyen+"'";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);

                        while (rs.next()) {
                            state = rs.getString(1).toString().trim();
                        }

                        if(state.equals("0")) {
                            query = "INSERT INTO YeuThich (TenDangNhap, MaTruyen) Select N'"+User+"',N'"+MaTruyen+"'";
                            st = connect.createStatement();
                            st.executeUpdate(query);
                            Toast.makeText(NovelMain.this,"Đã thêm vào danh sách yêu thích",Toast.LENGTH_SHORT).show();
                            favor.setBackgroundResource(R.drawable.favorite_full_icon);
                        } else {
                            query = "Delete from YeuThich where TenDangNhap=N'"+User+"' and MaTruyen=N'"+MaTruyen+"'";
                            st = connect.createStatement();
                            st.executeUpdate(query);
                            Toast.makeText(NovelMain.this,"Đã xóa khỏi danh sách yêu thích",Toast.LENGTH_SHORT).show();
                            favor.setBackgroundResource(R.drawable.favorite_icon);
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
            }
        });
    }
//        Danh sách chương truyện
    SimpleAdapter ad;
    public void GetList(View v){
        Intent intent = getIntent();
        String MaTruyen = intent.getStringExtra("Ma_Truyen");
        ListView dschapters = (ListView) findViewById(R.id.chapters);
        List<Map<String,String>> MyChaptersList = null;
        ChapterList Chapter = new ChapterList();
        MyChaptersList = Chapter.getlist(MaTruyen);
        String[] Fromw = {"chapterNumber","chapterTitle"};
        int[] Tow = {R.id.chapterNumber,R.id.chapterTitle};
        ad = new SimpleAdapter(NovelMain.this, MyChaptersList, R.layout.novel_chapters, Fromw, Tow);
        dschapters.setAdapter(ad);
        int lenght = dschapters.getCount();
        ad.notifyDataSetChanged();

        if (dschapters.getHeight()==0){
            LinearLayout.LayoutParams h = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, lenght*60);
            dschapters.setLayoutParams(h);
        }
        else if (dschapters.getHeight()>0){
            LinearLayout.LayoutParams h = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
            dschapters.setLayoutParams(h);
        }
    }
    NovelAdapter nad;
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
//    Chọn truyện từ listview
    public void GetNovel(String tt){
        String TenTruyen = tt;
        ListView dsnovel = (ListView) findViewById(R.id.novelSearch);
        List<Novel> MyNovelList = getnovel(TenTruyen);
        nad = new NovelAdapter(NovelMain.this, R.layout.novel_lists, MyNovelList);
        dsnovel.setAdapter(nad);
        nad.notifyDataSetChanged();
    }
}
