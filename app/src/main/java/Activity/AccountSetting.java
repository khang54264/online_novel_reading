package Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btlandroid.ConnectionHelper;
import com.example.btlandroid.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccountSetting extends AppCompatActivity{
    String User, Email, Pass, Per;
    Connection connect;
    String ConnectionResult="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);
        Intent intent = getIntent();
        User = intent.getStringExtra("User").trim();
        EditText edtUser, edtEmail, edtPass, edtType;
        edtUser = findViewById(R.id.edtTTUser);
        edtEmail = findViewById(R.id.edtTTEmail);
        edtPass = findViewById(R.id.edtTTPass);
        edtType = findViewById(R.id.edtTTType);
        ConnectionHelper connectionHelper = new ConnectionHelper();
        connect = connectionHelper.conclass();
        if(connect==null) {
            connect=connectionHelper.conclass();
        }
        if(connect!=null) {
            try {
                String query = "Select * from TaiKhoan where TenDangNhap='"+User+"'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    edtUser.setText(rs.getString(1).trim());
                    edtEmail.setText(rs.getString(4).trim());
                    edtPass.setText(rs.getString(2).trim());
                    String quyen = rs.getString(3).trim();
                    Email = rs.getString(4).trim();
                    Pass = rs.getString(2).trim();
                    Per = rs.getString(3).trim();
                    if (quyen.equals("0")){
                        edtType.setText("Quản lý");
                    }
                    if (quyen.equals("1")) {
                        edtType.setText("Người dùng");
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
        Button btnSuaTT, btnDoiMK, btnXoaTK;
        btnSuaTT = findViewById(R.id.btnSuaTT);
        btnSuaTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSuaEmail();
            }
        });
        btnDoiMK = findViewById(R.id.btnDoiMK);
        btnDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSuaPass();
            }
        });
        btnXoaTK = findViewById(R.id.btnXoaTK);
        btnXoaTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogXoaTK();
            }
        });
    }
    public void DialogSuaEmail(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_email);
        EditText edtEmail = dialog.findViewById(R.id.editTextSuaEmail);
        Button btnChange, btnCancel;
        btnChange = dialog.findViewById(R.id.btnChangeEmail);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailMoi = edtEmail.getText().toString();
                char a = '@';
                char b = '.';
                int ca = 0;
                int cb = 0;
                for (int i = 0; i < emailMoi.length(); i++) {
                    if (emailMoi.charAt(i)==a) {
                        ca++;
                    }
                }
                for (int i = 0; i < emailMoi.length(); i++) {
                    if (emailMoi.charAt(i)==b) {
                        cb++;
                    }
                }
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                if (emailMoi.equalsIgnoreCase("")){
                    Toast.makeText(AccountSetting.this,"Vui lòng nhập email",Toast.LENGTH_SHORT).show();
                } else if (ca!=1 || cb!=1){
                    Toast.makeText(AccountSetting.this,"Email không đúng định dạng",Toast.LENGTH_SHORT).show();
                } else {
                    if(connect==null) {
                        connect=connectionHelper.conclass();
                    }
                    if(connect!=null) {
                        try {
                            String query = "Update TaiKhoan Set Email='"+emailMoi+"' Where TenDangNhap='"+User+"'";
                            Statement st = connect.createStatement();
                            st.executeUpdate(query);
                            Toast.makeText(AccountSetting.this, "Đổi Email thành công", Toast.LENGTH_SHORT).show();
                            EditText edtTTEmail = findViewById(R.id.edtTTEmail);
                            edtTTEmail.setText(emailMoi.trim());
                            connect.close();
                            }
                        catch (Exception exception) {
                            Toast.makeText(AccountSetting.this, "Đổi Email thất bại", Toast.LENGTH_SHORT).show();
                            Log.e("error ", exception.getMessage());
                        }
                    }
                    else
                    {
                        ConnectionResult="Check Connection";
                    }
                    dialog.dismiss();
                }
            }
        });
        btnCancel = dialog.findViewById(R.id.btnCancelEmail);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void DialogSuaPass(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_password);
        EditText edtPassCheck = findViewById(R.id.edtTTPass);
        EditText edtPassOld = dialog.findViewById(R.id.editTextPassOld);
        EditText edtPassNew = dialog.findViewById(R.id.editTextPassNew);
        Button btnChange, btnCancel;
        btnChange = dialog.findViewById(R.id.btnChangePass);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passCheck = edtPassCheck.getText().toString();
                String passOld = edtPassOld.getText().toString();
                String passNew = edtPassNew.getText().toString();
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                if (passOld.equals("")||passNew.equals("")){
                    Toast.makeText(AccountSetting.this,"Vui lòng không để trống",Toast.LENGTH_SHORT).show();
                } else if (!passOld.equals(passCheck)){
                    Toast.makeText(AccountSetting.this,"Không đúng mật khẩu",Toast.LENGTH_SHORT).show();
                } else {
                    if(connect==null) {
                        connect=connectionHelper.conclass();
                    }
                    if(connect!=null) {
                        try {
                            String query = "Update TaiKhoan Set MatKhau='"+passNew+"' Where TenDangNhap='"+User+"'";
                            Statement st = connect.createStatement();
                            st.executeUpdate(query);
                            Toast.makeText(AccountSetting.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            EditText edtTTPass = findViewById(R.id.edtTTPass);
                            edtTTPass.setText(passNew.trim());
                            connect.close();
                        }
                        catch (Exception exception) {
                            Toast.makeText(AccountSetting.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                            Log.e("error ", exception.getMessage());
                        }
                    }
                    else
                    {
                        ConnectionResult="Check Connection";
                    }
                    dialog.dismiss();
                }
            }
        });
        btnCancel = dialog.findViewById(R.id.btnCancelPass);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void DialogXoaTK(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_account);
        Button btnDelete, btnCancel;
        btnDelete = dialog.findViewById(R.id.btnDeleteAccount);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                if(connect==null) {
                    connect=connectionHelper.conclass();
                }
                if(connect!=null) {
                    try {
                        String query = "Delete from TaiKhoan Where TenDangNhap='"+User+"'";
                        Statement st = connect.createStatement();
                        st.executeUpdate(query);
                        Toast.makeText(AccountSetting.this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                        connect.close();
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    catch (Exception exception) {
                        Toast.makeText(AccountSetting.this, "Xóa tài khoản thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("error ", exception.getMessage());
                    }
                }
                else
                {
                    ConnectionResult="Check Connection";
                }
                dialog.dismiss();
            }
        });
        btnCancel = dialog.findViewById(R.id.btnCancelAccount);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
