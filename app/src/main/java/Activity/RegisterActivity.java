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

public class RegisterActivity extends AppCompatActivity{
    Connection connect;
    String ConnectionResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        LinearLayout LogRegisCtrl= findViewById(R.id.LoginRegisterControl);
        Button btnCtrl, btnLoginAct, btnRegisAct, btnRegister;
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
        btnRegister = findViewById(R.id.btnRegis);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                EditText edtUser, edtEmail, edtPass, edtXNPass;
                edtUser = findViewById(R.id.edtTenDangNhap);
                edtEmail = findViewById(R.id.edtEmail);
                edtPass = findViewById(R.id.edtMatKhau);
                edtXNPass = findViewById(R.id.edtXNMatKhau);
                String user = String.valueOf(edtUser.getText());
                String email = String.valueOf(edtEmail.getText());
                String pass = String.valueOf(edtPass.getText().toString().trim());
                String xnpass = String.valueOf(edtXNPass.getText().toString().trim());
                int c1 = 0, c2 = 0;
                char a = '@';
                char b = '.';
                int ca = 0;
                int cb = 0;
                for (int i = 0; i < email.length(); i++) {
                    if (email.charAt(i)==a) {
                        ca++;
                    }
                }
                for (int i = 0; i < email.length(); i++) {
                    if (email.charAt(i)==b) {
                        cb++;
                    }
                }
                if(connect==null) {
                    connect=connectionHelper.conclass();
                }
                if (!pass.equals(xnpass)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng nhau", Toast.LENGTH_SHORT).show();
                    edtXNPass.setText("");
                    edtPass.setText("");
                } else if (email.equalsIgnoreCase("")){
                    Toast.makeText(RegisterActivity.this,"Vui lòng nhập email",Toast.LENGTH_SHORT).show();
                } else if (ca!=1 || cb!=1){
                    Toast.makeText(RegisterActivity.this,"Email không đúng định dạng",Toast.LENGTH_SHORT).show();
                } else if(connect!=null) {
                    try {
                        String query = "Select count(TenDangNhap) from TaiKhoan where TenDangNhap=N'"+user+"'";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            c1 = rs.getInt(1);
                        }

                        query = "Select count(Email) from TaiKhoan where Email=N'"+email+"'";
                        st = connect.createStatement();
                        rs = st.executeQuery(query);
                        while (rs.next()) {
                            c2 = rs.getInt(1);
                        }

                        if (c1==1) {
                            Toast.makeText(RegisterActivity.this, "Đã tồn tại tên người dùng", Toast.LENGTH_SHORT).show();
                            edtXNPass.setText("");
                            edtPass.setText("");
                            edtEmail.setText("");
                            edtUser.setText("");
                        } else if (c2==1) {
                            Toast.makeText(RegisterActivity.this, "Đã tồn tại email", Toast.LENGTH_SHORT).show();
                            edtXNPass.setText("");
                            edtPass.setText("");
                            edtEmail.setText("");
                            edtUser.setText("");
                        } else {
                            String t = "";
                            try {
                                query = "INSERT INTO TaiKhoan (TenDangNhap, MatKhau, Email, Quyen) VALUES " +
                                        "(N'"+user+"',N'"+pass+"',N'"+email+"',N'1')";
                                st = connect.createStatement();
                                st.executeUpdate(query);
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                connect.close();
                                edtXNPass.setText("");
                                edtPass.setText("");
                                edtEmail.setText("");
                                edtUser.setText("");
                                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                                startActivity(intent);
                            } catch (Exception exception) {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                Log.e("error ", exception.getMessage());
                            }
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
