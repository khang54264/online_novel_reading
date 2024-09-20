package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.btlandroid.ConnectionHelper;
import com.example.btlandroid.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity{

    Connection connect;
    String ConnectionResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LinearLayout LogRegisCtrl= findViewById(R.id.LoginRegisterControl);
        Button btnCtrl, btnLoginAct, btnRegisAct, btnLogin;
        btnCtrl = findViewById(R.id.btnControl);
        btnCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LogRegisCtrl.getHeight()==0){
                    LinearLayout.LayoutParams h = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
                    LogRegisCtrl.setLayoutParams(h);
                }
                else if (LogRegisCtrl.getHeight()>0){
                    LinearLayout.LayoutParams h = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
                    LogRegisCtrl.setLayoutParams(h);
                }
            }
        });
        btnLoginAct = findViewById(R.id.btnLoginActivity);
        btnLoginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btnRegisAct = findViewById(R.id.btnRegisterActivity);
        btnRegisAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                EditText edtUser, edtPass;
                edtUser = findViewById(R.id.edtTenDangNhap);
                edtPass = findViewById(R.id.edtMatKhau);
                String user = String.valueOf(edtUser.getText().toString().trim());
                String pass = String.valueOf(edtPass.getText().toString().trim());
                int c1 = 0, c2 = 0;
                if(connect==null) {
                    connect=connectionHelper.conclass();
                }
                if(connect!=null) {
                    try {
                        String query = "Select count(TenDangNhap) from TaiKhoan where TenDangNhap='"+user+"' and MatKhau='"+pass+"'";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            c1 = rs.getInt(1);
                        }

                        query = "Select count(TenDangNhap) from TaiKhoan where Email='"+user+"' and MatKhau='"+pass+"'";
                        st = connect.createStatement();
                        rs = st.executeQuery(query);
                        while (rs.next()) {
                            c2 = rs.getInt(1);
                        }

                        if (c1==1 || c2==1) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            String t = "";
                            try {
                                query = "Select TenDangNhap from TaiKhoan where TenDangNhap='"+user+"' or Email='"+user+"'";
                                st = connect.createStatement();
                                rs = st.executeQuery(query);
                                while (rs.next()) {
                                    t = rs.getString(1);
                                }
                            } catch (Exception exception) {
                                Log.e("error ", exception.getMessage());
                            }
                            connect.close();
                            edtPass.setText("");
                            edtUser.setText("");
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            intent.putExtra("User",t);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Sai thông tin đề nghị nhập lại", Toast.LENGTH_SHORT).show();
                            edtPass.setText("");
                            edtUser.setText("");
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
}
