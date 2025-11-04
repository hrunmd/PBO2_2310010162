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
public class tbl_produksi { 
    
    // --- Variabel Konfigurasi ---
    private final String namadb = "PBO2_2310010162"; 
    private final String url = "jdbc:mysql://localhost:3306/" + namadb;
    private final String username = "root";
    private final String password = "";
    private Connection koneksi;
    
    // --- Variabel untuk Validasi/Transfer Data (VAR) ---
    public String VAR_idproduksi = null; 
    public String VAR_idlahan = null; 
    public String VAR_idpupuk = null; 
    public String VAR_tahun = null;
    public String VAR_jenispadi = null; 
    public Integer VAR_hasilkg = null;
    public boolean validasi = false;
    
    
    public tbl_produksi(){
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("tbl_produksi: Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    // METHOD 1: GET LAHAN DATA (Untuk JComboBox Lahan)
    public ResultSet getLahanData() throws SQLException {
        // Mengambil ID Lahan dan Lokasi untuk ditampilkan
        String sql = "SELECT id_lahan, lokasi FROM lahan ORDER BY lokasi";
        return koneksi.createStatement().executeQuery(sql);
    }

    // METHOD 2: GET PUPUK DATA (Untuk JComboBox Pupuk)
    public ResultSet getPupukData() throws SQLException {
        // Mengambil ID Pupuk dan Nama Pupuk untuk ditampilkan
        String sql = "SELECT id_pupuk, nama_pupuk FROM pupuk ORDER BY nama_pupuk";
        return koneksi.createStatement().executeQuery(sql);
    }
    
    // METHOD 3: READ data Produksi (Menggunakan JOIN ke Lahan dan Pupuk)
    public ResultSet getAllProduksi() throws SQLException {
    // Memastikan semua kolom FK dan data ditampilkan.
    String sql = "SELECT p.id_produksi, p.id_lahan, p.id_pupuk, " +
                 "l.lokasi, pu.nama_pupuk, p.tahun, p.jenis_padi, p.hasil_panen_kg " +
                 "FROM produksi p " +
                 "JOIN lahan l ON p.id_lahan = l.id_lahan " +
                 "JOIN pupuk pu ON p.id_pupuk = pu.id_pupuk";
    
    return koneksi.createStatement().executeQuery(sql);
    }
    
    // -------------------------------------------------------------------
    // 4. SIMPAN ANGGOTA (CREATE)
    // -------------------------------------------------------------------
    public void SimpanAnggota(String idproduksi, String idlahan, String idpupuk, String tahun, String jenispadi, int hasilkg){
        try {
            String sql = "INSERT INTO produksi(id_produksi, id_lahan, id_pupuk, tahun, jenis_padi, hasil_panen_kg) "
                       + "VALUES('"+idproduksi+"', '"+idlahan+"', '"+idpupuk+"', '"+tahun+"', '"+jenispadi+"', "+hasilkg+")"; 

            // Cek apakah ID sudah ada
            String cekPrimary = "SELECT * FROM produksi WHERE id_produksi = '"+idproduksi+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID Produksi sudah terdaftar!");
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
    // 5. UBAH ANGGOTA (UPDATE)
    // -------------------------------------------------------------------
    public void ubahAnggota(String idproduksi, String idlahan, String idpupuk, String tahun, String jenispadi, int hasilkg){
        try {
            String sql = "UPDATE produksi SET id_lahan=?, id_pupuk=?, tahun=?, jenis_padi=?, hasil_panen_kg=? WHERE id_produksi=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, idlahan);
            perintah.setString(2, idpupuk);
            perintah.setString(3, tahun);
            perintah.setString(4, jenispadi);
            perintah.setInt(5, hasilkg);
            perintah.setString(6, idproduksi);
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // -------------------------------------------------------------------
    // 6. HAPUS ANGGOTA (DELETE)
    // -------------------------------------------------------------------
    public void hapusAnggota(String idproduksi){
        try {
            String sql = "DELETE FROM produksi WHERE id_produksi='"+idproduksi+"'";
            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
}