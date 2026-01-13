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
import java.io.File;
import java.util.Set;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author HARUN AL RASYID
 */
public class tbl_lahan { 
    
    // --- Variabel Konfigurasi (Disesuaikan dengan DB Anda) ---
    private final String namadb = "PBO2_2310010162"; 
    private final String url = "jdbc:mysql://localhost:3306/" + namadb;
    private final String username = "root";
    private final String password = "";
    private Connection koneksi;
    
    // --- Variabel untuk Validasi/Transfer Data (VAR) ---
    public String VAR_idlahan = null; 
    public String VAR_idpetani = null; 
    public Double VAR_luas = null;
    public String VAR_lokasi = null;
    public boolean validasi = false;
    
    // Constructor untuk membuka koneksi
    public tbl_lahan(){
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("tbl_lahan: Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    // METHOD 1: READ data Petani (Untuk JComboBox)
    public ResultSet getPetaniData() throws SQLException {
        String sql = "SELECT id_petani, nama_petani FROM petani ORDER BY nama_petani";
        return koneksi.createStatement().executeQuery(sql);
    }
    
    // METHOD 2: READ data Lahan (Menggunakan JOIN)
    public ResultSet getAllLahan() throws SQLException {
        String sql = "SELECT l.id_lahan, l.id_petani, p.nama_petani, l.luas_lahan_ha, l.lokasi " +
                     "FROM lahan l JOIN petani p ON l.id_petani = p.id_petani";
        return koneksi.createStatement().executeQuery(sql);
    }
    
    // -------------------------------------------------------------------
    // 3. SIMPAN ANGGOTA (CREATE) - Diadaptasi untuk lahan
    // Menggunakan String untuk ID karena desain DB Anda menggunakan String
    // -------------------------------------------------------------------
    public void SimpanAnggota(String idlahan, String idpetugas, double luas, String lokasi){
        try {
            // Query INSERT ke tabel lahan
            String sql = "INSERT INTO lahan(id_lahan, id_petani, luas_lahan_ha, lokasi) "
                       + "VALUES('"+idlahan+"', '"+idpetugas+"', "+luas+", '"+lokasi+"')";

            // Cek apakah ID sudah ada
            String cekPrimary = "SELECT * FROM lahan WHERE id_lahan = '"+idlahan+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID Lahan sudah terdaftar!");
                // Mengambil data yang sudah ada
                this.VAR_idlahan = data.getString("id_lahan");
                this.VAR_idpetani = data.getString("id_petani");
                this.VAR_luas = data.getDouble("luas_lahan_ha");
                this.VAR_lokasi = data.getString("lokasi");
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
    // 4. UBAH ANGGOTA (UPDATE) - Diadaptasi untuk lahan
    // -------------------------------------------------------------------
    public void ubahAnggota(String idlahan, String idpetugas, double luas, String lokasi){
        try {
            // Query UPDATE menggunakan PreparedStatement
            String sql = "UPDATE lahan SET id_petani=?, luas_lahan_ha=?, lokasi=? WHERE id_lahan=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, idpetugas); // Foreign Key
            perintah.setDouble(2, luas);
            perintah.setString(3, lokasi);
            perintah.setString(4, idlahan); // Primary Key
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // -------------------------------------------------------------------
    // 5. HAPUS ANGGOTA (DELETE) - Diadaptasi untuk lahan
    // -------------------------------------------------------------------
    public void hapusAnggota01(String idlahan){
        try {
            String sql = "DELETE FROM lahan WHERE id_lahan='"+idlahan+"'";
            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void cetakLaporan(String fileLaporan, String SQL){
        try {
            File file = new File(fileLaporan);
            JasperDesign jasDes = JRXmlLoader.load(file);
            JRDesignQuery query = new JRDesignQuery();
            query.setText(SQL);
            jasDes.setQuery(query);
            JasperReport jr = JasperCompileManager.compileReport(jasDes);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, this.koneksi);
            JasperViewer.viewReport(jp);
            
        } catch (Exception e) {
    JOptionPane.showMessageDialog(null, 
        "Gagal cetak laporan:\n" + e.getMessage());
    e.printStackTrace();
}

    }

    public ResultSet cariDataLahan(String keyword) {
    try {
        String sql =
            "SELECT l.id_lahan, l.id_petani, p.nama_petani, l.luas_lahan_ha, l.lokasi " +
            "FROM lahan l JOIN petani p ON l.id_petani = p.id_petani " +
            "WHERE l.id_lahan LIKE '%" + keyword + "%' " +
            "OR p.nama_petani LIKE '%" + keyword + "%' " +
            "OR l.lokasi LIKE '%" + keyword + "%'";

        return koneksi.createStatement().executeQuery(sql);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal mencari data lahan: " + e.getMessage());
        return null;
    }
}

}