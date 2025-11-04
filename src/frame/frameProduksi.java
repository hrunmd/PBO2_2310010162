/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frame;
import configdb.tbl_produksi; // Wajib: Import DAO Produksi
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel; // Untuk mengisi JComboBox
import javax.swing.table.DefaultTableModel; // Untuk JTable
import javax.swing.event.ListSelectionListener; // Untuk interaksi klik JTable
import javax.swing.event.ListSelectionEvent;
/**
 *
 * @author HARUN AL RASYID
 */
public class frameProduksi extends javax.swing.JFrame {
    private tbl_produksi objekProduksi;
    /**
     * Creates new form frameProduksi
     */
    public frameProduksi() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        objekProduksi = new tbl_produksi(); 
        tampilDataProduksi();
        
        // PANGGIL METHOD UNTUK MENGISI JCOMBOBOXES
        loadLahan(); 
        loadPupuk(); 
        // tampilDataProduksi(); // Hanya jika Anda menggunakan JTable
        tabelProduksi.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { 
                    tabelProduksiMouseClicked(); 
                }
            }
        });
    
    }
    private void loadLahan() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            java.sql.ResultSet rs = objekProduksi.getLahanData();
            model.addElement("-- Pilih Lahan --"); 
            while (rs.next()) {
                String idLahan = rs.getString("id_lahan");
                String lokasi = rs.getString("lokasi");
                // Format: ID - Lokasi
                model.addElement(idLahan + " - " + lokasi); 
            }
            cmbIdLahan.setModel(model); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data lahan: " + e.getMessage());
        }
    }
    
    // =========================================================
    // METHOD 2: MENGISI JCOMBOBOX PUPUK (FK 2)
    // =========================================================
    private void loadPupuk() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            java.sql.ResultSet rs = objekProduksi.getPupukData();
            model.addElement("-- Pilih Pupuk --"); 
            while (rs.next()) {
                String idPupuk = rs.getString("id_pupuk");
                String namaPupuk = rs.getString("nama_pupuk");
                // Format: ID - Nama Pupuk
                model.addElement(idPupuk + " - " + namaPupuk); 
            }
            cmbIdPupuk.setModel(model); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data pupuk: " + e.getMessage());
        }
    }
    
    // =========================================================
    // METHOD 3: MENGAMBIL ID LAHAN DARI JCOMBOBOX YANG TERPILIH
    // =========================================================
    private String getSelectedLahanId() {
        String selectedItem = (String) cmbIdLahan.getSelectedItem();
        if (selectedItem != null && selectedItem.contains(" - ")) {
            return selectedItem.split(" - ")[0]; 
        }
        return null; 
    }
    
    // =========================================================
    // METHOD 4: MENGAMBIL ID PUPUK DARI JCOMBOBOX YANG TERPILIH
    // =========================================================
    private String getSelectedPupukId() {
        String selectedItem = (String) cmbIdPupuk.getSelectedItem();
        if (selectedItem != null && selectedItem.contains(" - ")) {
            return selectedItem.split(" - ")[0]; 
        }
        return null; 
    }
    private void tampilDataProduksi() {
    // PERHATIAN: Tambahkan import javax.swing.table.DefaultTableModel di atas!
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Prod.");
    model.addColumn("ID Lahan");    // Kolom FK (Disembunyikan)
    model.addColumn("ID Pupuk");    // Kolom FK (Disembunyikan)
    model.addColumn("Lokasi Lahan"); 
    model.addColumn("Pupuk");      
    model.addColumn("Tahun");
    model.addColumn("Jenis Padi");
    model.addColumn("Hasil (Kg)");

    try {
        java.sql.ResultSet rs = objekProduksi.getAllProduksi(); 
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_produksi"),
                rs.getString("id_lahan"),  // Ambil ID Lahan
                rs.getString("id_pupuk"),  // Ambil ID Pupuk
                rs.getString("lokasi"),    // Dari JOIN Lahan
                rs.getString("nama_pupuk"),// Dari JOIN Pupuk
                rs.getString("tahun"),
                rs.getString("jenis_padi"),
                rs.getInt("hasil_panen_kg")
            });
        }
        tabelProduksi.setModel(model);
        
        // KODE WAJIB: Sembunyikan Kolom ID Lahan (Index 1) dan ID Pupuk (Index 2)
        tabelProduksi.getColumnModel().getColumn(1).setMinWidth(0);
        tabelProduksi.getColumnModel().getColumn(1).setMaxWidth(0);
        tabelProduksi.getColumnModel().getColumn(2).setMinWidth(0);
        tabelProduksi.getColumnModel().getColumn(2).setMaxWidth(0);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error saat mengisi tabel: " + e.getMessage());
    }
}
    private void tabelProduksiMouseClicked() {
    int baris = tabelProduksi.getSelectedRow();
    if (baris != -1) { 
        // Ambil data dari kolom tersembunyi
        String idProduksi = tabelProduksi.getValueAt(baris, 0).toString();
        String idLahan = tabelProduksi.getValueAt(baris, 1).toString(); 
        String idPupuk = tabelProduksi.getValueAt(baris, 2).toString(); 
        
        // Ambil data tampilan
        String lokasi = tabelProduksi.getValueAt(baris, 3).toString();
        String namaPupuk = tabelProduksi.getValueAt(baris, 4).toString();

        // Mengisi fields
        txtIdProduksi.setText(idProduksi);
        txtTahun.setText(tabelProduksi.getValueAt(baris, 5).toString());
        txtJenisPadi.setText(tabelProduksi.getValueAt(baris, 6).toString());
        txtHasilKg.setText(tabelProduksi.getValueAt(baris, 7).toString());
        
        // Mengisi JComboBox Lahan (FK 1)
        cmbIdLahan.setSelectedItem(idLahan + " - " + lokasi); 
        
        // Mengisi JComboBox Pupuk (FK 2)
        cmbIdPupuk.setSelectedItem(idPupuk + " - " + namaPupuk); 

        // Kunci ID (PK)
        txtIdProduksi.setEnabled(false);
    }
}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtIdProduksi = new javax.swing.JTextField();
        cmbIdLahan = new javax.swing.JComboBox<>();
        cmbIdPupuk = new javax.swing.JComboBox<>();
        txtTahun = new javax.swing.JTextField();
        txtJenisPadi = new javax.swing.JTextField();
        txtHasilKg = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelProduksi = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("ID Produksi");

        jLabel2.setText("Lahan & Lokasi");

        jLabel3.setText("Pupuk Digunakan");

        jLabel4.setText("Tahun Panen");

        jLabel5.setText("Jenis Padi");

        jLabel6.setText("Hasil Panen (Kg)");

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        tabelProduksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Produksi", "Lahan & Lokasi", "Pupuk Digunakan", "Tahun Panen", "jenis Padi", "Hasil Panen (Kg)"
            }
        ));
        jScrollPane1.setViewportView(tabelProduksi);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIdProduksi)
                    .addComponent(cmbIdLahan, 0, 92, Short.MAX_VALUE)
                    .addComponent(cmbIdPupuk, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTahun)
                    .addComponent(txtJenisPadi)
                    .addComponent(txtHasilKg))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnTambah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtIdProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTambah))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbIdLahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUbah))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cmbIdPupuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHapus))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtTahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtJenisPadi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtHasilKg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(121, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        String idProduksi = txtIdProduksi.getText();
        String idLahanFK = getSelectedLahanId(); 
        String idPupukFK = getSelectedPupukId(); 
        String tahun = txtTahun.getText();
        String jenisPadi = txtJenisPadi.getText();

        int hasilKg = 0;
        try {
            // Pastikan input adalah angka bulat
            hasilKg = Integer.parseInt(txtHasilKg.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Hasil Panen harus berupa angka bulat!");
            return;
        }

        if (idProduksi.isEmpty() || idLahanFK == null || idPupukFK == null || jenisPadi.isEmpty() 
            || idLahanFK.contains("-- Pilih Lahan --") || idPupukFK.contains("-- Pilih Pupuk --")) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }
        tampilDataProduksi();
        
        objekProduksi.SimpanAnggota(idProduksi, idLahanFK, idPupukFK, tahun, jenisPadi, hasilKg);
        
        // Kosongkan fields dan reset dropdown
        txtIdProduksi.setText("");
        txtTahun.setText("");
        txtJenisPadi.setText("");
        txtHasilKg.setText("");
        cmbIdLahan.setSelectedIndex(0); 
        cmbIdPupuk.setSelectedIndex(0); 
    
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        // TODO add your handling code here:
        String idProduksi = txtIdProduksi.getText();
        String idLahanFK = getSelectedLahanId(); 
        String idPupukFK = getSelectedPupukId(); 
        String tahun = txtTahun.getText();
        String jenisPadi = txtJenisPadi.getText();

        int hasilKg = 0;
        try {
            hasilKg = Integer.parseInt(txtHasilKg.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Hasil Panen harus berupa angka bulat!");
            return;
        }
        
        if (idProduksi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan ID Produksi yang akan diubah!");
            return;
        }
        tampilDataProduksi();
        
        objekProduksi.ubahAnggota(idProduksi, idLahanFK, idPupukFK, tahun, jenisPadi, hasilKg);
    
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        String idProduksi = txtIdProduksi.getText();
        if (idProduksi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan ID Produksi yang akan dihapus!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin hapus data ID " + idProduksi + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (konfirmasi == JOptionPane.YES_OPTION) {
             objekProduksi.hapusAnggota(idProduksi);
             // Kosongkan fields
             txtIdProduksi.setText("");
             txtTahun.setText("");
             txtJenisPadi.setText("");
             txtHasilKg.setText("");
             cmbIdLahan.setSelectedIndex(0); 
             cmbIdPupuk.setSelectedIndex(0); 
        }
        tampilDataProduksi();
    
    }//GEN-LAST:event_btnHapusActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frameProduksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameProduksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameProduksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameProduksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frameProduksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbIdLahan;
    private javax.swing.JComboBox<String> cmbIdPupuk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelProduksi;
    private javax.swing.JTextField txtHasilKg;
    private javax.swing.JTextField txtIdProduksi;
    private javax.swing.JTextField txtJenisPadi;
    private javax.swing.JTextField txtTahun;
    // End of variables declaration//GEN-END:variables
}
