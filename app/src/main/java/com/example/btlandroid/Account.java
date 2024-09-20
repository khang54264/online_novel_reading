package com.example.btlandroid;

public class Account {
    private String TenDangNhap;
    private String MatKhau;
    private String Email;
    private String Quyen;
    public Account(String tenDangNhap, String matKhau, String email, String quyen) {
        TenDangNhap = tenDangNhap;
        MatKhau = matKhau;
        Email = email;
        Quyen = quyen;
    }
    public String getTenDangNhap() {
        return TenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.TenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        this.MatKhau = matKhau;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getQuyen() {
        return Quyen;
    }

    public void setQuyen(String quyen) {
        this.Quyen = quyen;
    }

}
