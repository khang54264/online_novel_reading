package Activity;

import static java.util.Locale.filter;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.btlandroid.Account;
import Adapter.AccountAdapter;
import com.example.btlandroid.ConnectionHelper;
import com.example.btlandroid.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountManage extends AppCompatActivity{
    String tdn;
    private ListView listAcc;
    private AccountAdapter ad;
    Connection connect;
    String ConnectionResult="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_manage);
        GetAccount();
        listAcc = findViewById(R.id.listQLAccount);
        listAcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Account> arrayList = gettaikhoan();
                Account account = arrayList.get(position);
                DialogSuaTK(account.getTenDangNhap());
            }
        });
        ad = new AccountAdapter(AccountManage.this, R.layout.account_lists, gettaikhoan());
        ad.setOnItemClickListener(new AccountAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(Account item) {
                DialogXoaTK(item.getTenDangNhap());
            }
        });
        listAcc.setAdapter(ad);
        Button btnTaoTK = findViewById(R.id.createAccount);
        btnTaoTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogThemTK();
            }
        });
    }
//    Load DS Account
    public List<Account> gettaikhoan(){
        List<Account> data = new ArrayList<Account>();
        ConnectionHelper connectionHelper = new ConnectionHelper();
        connect = connectionHelper.conclass();
        try{
            if(connect==null){
                connect = connectionHelper.conclass();
            }
            if(connect!=null) {
                String query= "Select * from TaiKhoan";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    String user = rs.getString(1);
                    String pass = rs.getString(2);
                    String per = rs.getString(3);
                    String email = rs.getString(4);
                    data.add(new Account(user,pass,email,per));
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
    public void GetAccount(){
        ListView listAccount= (ListView) findViewById(R.id.listQLAccount);
        List<Account> MyAccountList = gettaikhoan();
        ad = new AccountAdapter(AccountManage.this, R.layout.account_lists, MyAccountList);
        listAccount.setAdapter(ad);
        ad.notifyDataSetChanged();
    }
    private List<String> getQuyen() {
        List<String> data = new ArrayList<>();
        data.add("Quản lý");
        data.add("Người dùng");
        return data;
    }
//    Tạo TK
    public void DialogThemTK(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_create_account);
        EditText edtUser, edtEmail, edtPass;
        edtUser = dialog.findViewById(R.id.edtCreateTenDangNhap);
        edtEmail = dialog.findViewById(R.id.edtCreateEmail);
        edtPass = dialog.findViewById(R.id.edtCreateMatKhau);
        Spinner spnPer = dialog.findViewById(R.id.spnCreateQuyen);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getQuyen());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPer.setAdapter(adapter);
        ConnectionHelper connectionHelper = new ConnectionHelper();
        connect = connectionHelper.conclass();
        Button btnCreate, btnCancel;
        btnCreate = dialog.findViewById(R.id.btnCreateAccount);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User = edtUser.getText().toString();
                String Email = edtEmail.getText().toString();
                String Pass = edtPass.getText().toString();
                Spinner spinner = dialog.findViewById(R.id.spnCreateQuyen);
                String Per = spinner.getSelectedItem().toString().trim();
                if(Per.equals("Quản lý")) {
                    Per="0";
                } else {
                    Per="1";
                }
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                if(connect==null) {
                    connect=connectionHelper.conclass();
                }
                if(connect!=null) {
                    try {
                        String query = "INSERT INTO TaiKhoan (TenDangNhap, MatKhau, Email, Quyen) VALUES " +
                                        "(N'"+User+"',N'"+Pass+"',N'"+Email+"',N'"+Per+"')";
                        Statement st = connect.createStatement();
                        st.executeUpdate(query);
                        Toast.makeText(AccountManage.this, "Đã tạo tài khoản", Toast.LENGTH_SHORT).show();
                        GetAccount();
                        connect.close();
                    }
                    catch (Exception exception) {
                        Toast.makeText(AccountManage.this, "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("error ", exception.getMessage());
                    }
                }
                else
                {
                    ConnectionResult="Check Connection";
                }
                ad.notifyDataSetChanged();
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
        GetAccount();
        dialog.show();
    }
//    Chỉnh sửa TK
    public void DialogSuaTK(String tdn){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_account);
        EditText edtUser, edtEmail, edtPass;
        edtUser = dialog.findViewById(R.id.editTextSuaUser);
        edtEmail = dialog.findViewById(R.id.editTextSuaEmail);
        edtPass = dialog.findViewById(R.id.editTextSuaPass);
        Spinner spnPer = dialog.findViewById(R.id.spinnerQuyen);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getQuyen());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPer.setAdapter(adapter);
        ConnectionHelper connectionHelper = new ConnectionHelper();
        connect = connectionHelper.conclass();
        if(connect==null) {
            connect=connectionHelper.conclass();
        }
        if(connect!=null) {
            try {
                String query = "Select * from TaiKhoan Where TenDangNhap='"+tdn+"'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while(rs.next()){
                    edtUser.setText(rs.getString(1));
                    edtPass.setText(rs.getString(2));
                    String oPer = rs.getString(3);
                    if(oPer.equals("0")){
                        spnPer.setSelection(0);
                    } else {
                        spnPer.setSelection(1);
                    }
                    edtEmail.setText(rs.getString(4));
                }
                connect.close();;
            }
            catch (Exception exception) {
                Log.e("error ", exception.getMessage());
            }
        }
        else
        {
            ConnectionResult="Check Connection";
        }
        Button btnChange, btnCancel;
        btnChange = dialog.findViewById(R.id.btnChangeAccount);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User = edtUser.getText().toString();
                String Email = edtEmail.getText().toString();
                String Pass = edtPass.getText().toString();
                Spinner spinner = dialog.findViewById(R.id.spinnerQuyen);
                String Per = spinner.getSelectedItem().toString().trim();
                if(Per.equals("Quản lý")) {
                    Per="0";
                } else {
                    Per="1";
                }
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                if(connect==null) {
                    connect=connectionHelper.conclass();
                }
                if(connect!=null) {
                    try {
                        String query = "Update TaiKhoan Set MatKhau='"+Pass+"', Quyen='"+Per+"', Email='"+Email+"' Where TenDangNhap='"+User+"'";
                        Statement st = connect.createStatement();
                        st.executeUpdate(query);
                        Toast.makeText(AccountManage.this, "Đã cập nhật tài khoản", Toast.LENGTH_SHORT).show();
                        GetAccount();
                        connect.close();
                    }
                    catch (Exception exception) {
                        Toast.makeText(AccountManage.this, "Cập nhật tài khoản thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("error ", exception.getMessage());
                    }
                }
                else
                {
                    ConnectionResult="Check Connection";
                }
                GetAccount();
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
//    Xóa TK
    public void DialogXoaTK(String tdn){
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
                        String query = "Delete from TaiKhoan Where TenDangNhap='"+tdn+"'";
                        Statement st = connect.createStatement();
                        st.executeUpdate(query);
                        Toast.makeText(AccountManage.this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                        GetAccount();
                        connect.close();
                    }
                    catch (Exception exception) {
                        Toast.makeText(AccountManage.this, "Xóa tài khoản thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("error ", exception.getMessage());
                    }
                }
                else
                {
                    ConnectionResult="Check Connection";
                }
                GetAccount();
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
