/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configdb; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author HARUN AL RASYID
 */
public class tbl_petani { // Disesuaikan dengan penamaan file Anda
    
    // --- Variabel Konfigurasi ---
    private final String namadb = "PBO2_2310010162"; 
    private final String url = "jdbc:mysql://localhost:3306/" + namadb;
    private final String username = "root";
    private final String password = "";
    private Connection koneksi;
    
    // --- Variabel untuk Validasi/Transfer Data (VAR) ---
    public String VAR_idpetani = null; 
    public String VAR_nama = null;
    public String VAR_alamat = null;
    public String VAR_nohp = null;
    public boolean validasi = false;
    
    // Constructor untuk membuka koneksi
    public tbl_petani(){
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("tbl_petani: Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    
    // Method Tambahan untuk READ (Membaca data)
    public ResultSet getAllPetani() throws SQLException {
        String sql = "SELECT id_petani, nama_petani, alamat, no_hp FROM petani";
        return koneksi.createStatement().executeQuery(sql);
    }
    
    // -------------------------------------------------------------------
    // 1. SIMPAN ANGGOTA (CREATE)
    // -------------------------------------------------------------------
    public void SimpanAnggota(String idpetani, String nama, String alamat, String nohp){
        try {
            String sql = "INSERT INTO petani(id_petani, nama_petani, alamat, no_hp) "
                       + "VALUES('"+idpetani+"', '"+nama+"', '"+alamat+"', '"+nohp+"')";

            // Cek apakah ID sudah ada
            String cekPrimary = "SELECT * FROM petani WHERE id_petani = '"+idpetani+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID Petani sudah terdaftar!");
                // Mengambil data yang sudah ada
                this.VAR_idpetani = data.getString("id_petani");
                this.VAR_nama = data.getString("nama_petani");
                this.VAR_alamat = data.getString("alamat");
                this.VAR_nohp = data.getString("no_hp");
                this.validasi = true;
            } else {
                this.validasi = false;
                Statement perintah = koneksi.createStatement();
                perintah.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // -------------------------------------------------------------------
    // 2. UBAH ANGGOTA (UPDATE)
    // -------------------------------------------------------------------
    public void ubahAnggota(String idpetani, String nama, String alamat, String nohp){
        try {
            String sql = "UPDATE petani SET nama_petani=?, alamat=?, no_hp=? WHERE id_petani=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, nama);
            perintah.setString(2, alamat);
            perintah.setString(3, nohp);
            perintah.setString(4, idpetani);
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // -------------------------------------------------------------------
    // 3. HAPUS ANGGOTA (DELETE)
    // -------------------------------------------------------------------
    public void hapusAnggota(String idpetani){

          try {
        String sql = "DELETE FROM petani WHERE id_petani='"+idpetani+"'";
        Statement perintah = koneksi.createStatement();
        perintah.execute(sql);
        
        // [1] NOTIFIKASI SUKSES (DITEMPATKAN DI SINI KARENA QUERY BERHASIL)
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
    } catch (Exception e) {
        // [2] TANGKAP DAN TAMPILKAN ERROR
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
        e.printStackTrace();
    }
    }
}