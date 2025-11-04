/*
 * File: tbl_pupuk.java
 * Package: configdb
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
 */
public class tbl_pupuk { 
    
    // --- Variabel Konfigurasi Koneksi Database ---
    // Pastikan nama database sesuai dengan setup Anda (contoh: PBO2_2310010162)
    private final String namadb = "PBO2_2310010162"; 
    private final String url = "jdbc:mysql://localhost:3306/" + namadb;
    private final String username = "root";
    private final String password = "";
    private Connection koneksi;
    
    // --- Variabel untuk Validasi/Transfer Data (VAR) ---
    public String VAR_idpupuk = null; 
    public String VAR_nama = null;
    public String VAR_jenis = null; 
    public Double VAR_dosis = null; // Menggunakan Double untuk dosis per ha (kg)
    public boolean validasi = false;
    
    
    // Konstruktor: Mencoba membuat koneksi ke database
    public tbl_pupuk(){
        try {
            // Menggunakan driver MySQL modern (cj.jdbc.Driver)
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("tbl_pupuk: Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Koneksi Gagal: " + error.getMessage());
        }
    }
    
    // Method untuk mengambil semua data pupuk (meski tidak digunakan di Frame, ini standar DAO)
    public ResultSet getAllPupuk() throws SQLException {
        String sql = "SELECT id_pupuk, nama_pupuk, jenis_pupuk, dosis_per_ha_kg FROM pupuk";
        return koneksi.createStatement().executeQuery(sql);
    }
    
    // -------------------------------------------------------------------
    // 1. SIMPAN ANGGOTA (CREATE)
    // -------------------------------------------------------------------
    public void SimpanAnggota(String idPupuk, String nama, String jenis, double dosis){
        try {
            String sql = "INSERT INTO pupuk(id_pupuk, nama_pupuk, jenis_pupuk, dosis_per_ha_kg) "
                       + "VALUES('"+idPupuk+"', '"+nama+"', '"+jenis+"', "+dosis+")"; 

            // Cek apakah id (Primary Key) sudah ada
            String cekPrimary = "SELECT * FROM pupuk WHERE id_pupuk = '"+idPupuk+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID Pupuk sudah terdaftar!");
                this.validasi = true;
                // Anda bisa mengisi VARs di sini jika ingin menampilkan data duplikat ke field
            } else {
                this.validasi = false;
                Statement perintah = koneksi.createStatement();
                perintah.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // -------------------------------------------------------------------
    // 2. UBAH ANGGOTA (UPDATE)
    // -------------------------------------------------------------------
    public void ubahAnggota(String idPupuk, String nama, String jenis, double dosis){
        try {
            String sql = "UPDATE pupuk SET nama_pupuk=?, jenis_pupuk=?, dosis_per_ha_kg=? WHERE id_pupuk=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, nama);
            perintah.setString(2, jenis);
            perintah.setDouble(3, dosis); 
            perintah.setString(4, idPupuk);
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // -------------------------------------------------------------------
    // 3. HAPUS ANGGOTA (DELETE)
    // -------------------------------------------------------------------
    public void hapusAnggota(String idPupuk){
        try {
            String sql = "DELETE FROM pupuk WHERE id_pupuk='"+idPupuk+"'";
            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus: " + e.getMessage());
            e.printStackTrace();
        }
    }
}