/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frame;
import configdb.tbl_lahan; 
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
/**
 *
 * @author HARUN AL RASYID
 */
public class frameLahan extends javax.swing.JFrame {
   private tbl_lahan objekLahan;
    /**
     * Creates new form frameLahan
     */
    public frameLahan() {
    initComponents();
    this.setLocationRelativeTo(null);
    objekLahan = new tbl_lahan();
    loadPetani(); 

    // WAJIB: Panggil method READ
    tampilDataLahan(); 

    // WAJIB: Tambahkan LISTENER JTABLE
    tabelLahan.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) { 
                tabelLahanMouseClicked(); 
            }
        }
    });

    }

    private void loadPetani() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            java.sql.ResultSet rs = objekLahan.getPetaniData();
            model.addElement("-- Pilih Petani --"); 
            while (rs.next()) {
                String idPetani = rs.getString("id_petani");
                String namaPetani = rs.getString("nama_petani");
                model.addElement(idPetani + " - " + namaPetani); 
            }
            cmbIdPetani.setModel(model); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data petani: " + e.getMessage());
        }
    }
        
    private String getSelectedPetaniId() {
        String selectedItem = (String) cmbIdPetani.getSelectedItem();
        if (selectedItem != null && selectedItem.contains(" - ")) {
            return selectedItem.split(" - ")[0]; 
        }
        return null; 
    }
    private void tampilDataLahan() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Lahan");
    model.addColumn("ID Petani"); // Kolom FK (Akan disembunyikan)
    model.addColumn("Nama Petani"); 
    model.addColumn("Luas (ha)");
    model.addColumn("Lokasi");

    try {
        java.sql.ResultSet rs = objekLahan.getAllLahan(); 
        
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_lahan"),
                rs.getString("id_petani"), 
                rs.getString("nama_petani"), 
                rs.getString("luas_lahan_ha"),
                rs.getString("lokasi")
            });
        }
        tabelLahan.setModel(model);
        
        // KODE WAJIB: Menyembunyikan kolom ID Petani (Index 1)
        tabelLahan.getColumnModel().getColumn(1).setMinWidth(0);
        tabelLahan.getColumnModel().getColumn(1).setMaxWidth(0);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error saat mengisi tabel: " + e.getMessage());
    }
}
    
    private void cariDataLahan() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Lahan");
    model.addColumn("ID Petani"); // FK (disembunyikan)
    model.addColumn("Nama Petani");
    model.addColumn("Luas (ha)");
    model.addColumn("Lokasi");

    try {
        ResultSet rs = objekLahan.cariDataLahan(txtCari.getText());

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_lahan"),
                rs.getString("id_petani"),
                rs.getString("nama_petani"),
                rs.getString("luas_lahan_ha"),
                rs.getString("lokasi")
            });
        }

        tabelLahan.setModel(model);

        // Sembunyikan kolom ID Petani
        tabelLahan.getColumnModel().getColumn(1).setMinWidth(0);
        tabelLahan.getColumnModel().getColumn(1).setMaxWidth(0);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Pencarian gagal: " + e.getMessage());
    }
}

    private void tabelLahanMouseClicked() {
    int baris = tabelLahan.getSelectedRow();
    if (baris != -1) { 
        String idLahan = tabelLahan.getValueAt(baris, 0).toString();
        String idPetani = tabelLahan.getValueAt(baris, 1).toString(); // Ambil ID dari kolom tersembunyi
        String namaPetani = tabelLahan.getValueAt(baris, 2).toString();
        
        // Mengisi fields
        txtIdLahan.setText(idLahan);
        txtluasLahan.setText(tabelLahan.getValueAt(baris, 3).toString());
        txtLokasi.setText(tabelLahan.getValueAt(baris, 4).toString());
        
        // Mengisi JComboBox
        String itemToSelect = idPetani + " - " + namaPetani;
        cmbIdPetani.setSelectedItem(itemToSelect); 
        
        // Kunci ID Lahan
        txtIdLahan.setEnabled(false);
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
        txtIdLahan = new javax.swing.JTextField();
        cmbIdPetani = new javax.swing.JComboBox<>();
        txtluasLahan = new javax.swing.JTextField();
        txtLokasi = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelLahan = new javax.swing.JTable();
        txtCari = new javax.swing.JTextField();
        btnLaporan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("ID Lahan");

        jLabel2.setText("Pemilik Petani");

        jLabel3.setText("Luas Lahan");

        jLabel4.setText("Lokasi");

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

        tabelLahan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Lahan", "Pemilik Petani", "Luas Lahan", "Lokasi"
            }
        ));
        jScrollPane1.setViewportView(tabelLahan);

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        btnLaporan.setText("Cetak Laporan");
        btnLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtLokasi, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtluasLahan, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbIdPetani, javax.swing.GroupLayout.Alignment.LEADING, 0, 97, Short.MAX_VALUE)
                            .addComponent(txtIdLahan, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnLaporan))
                        .addGap(51, 51, 51)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtIdLahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTambah))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbIdPetani, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUbah))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtluasLahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHapus))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLaporan))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(244, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        String idLahan = txtIdLahan.getText();
        String idPetaniFK = getSelectedPetaniId(); 
        String lokasi = txtLokasi.getText();

        double luas = 0.0;
        try {
            luas = Double.parseDouble(txtluasLahan.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Luas Lahan harus berupa angka!");
            return;
        }

        if (idLahan.isEmpty() || idPetaniFK == null || idPetaniFK.contains("-- Pilih Petani --")) {
            JOptionPane.showMessageDialog(this, "ID Lahan dan Pemilik wajib diisi!");
            return;
        }
        
        objekLahan.SimpanAnggota(idLahan, idPetaniFK, luas, lokasi);
        tampilDataLahan(); 
        txtIdLahan.setEnabled(true);
        
        // Kosongkan fields
        txtIdLahan.setText("");
        txtluasLahan.setText("");
        txtLokasi.setText("");
        cmbIdPetani.setSelectedIndex(0); 
    
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        // TODO add your handling code here:
        String idLahan = txtIdLahan.getText();
        String idPetaniFK = getSelectedPetaniId(); 
        String lokasi = txtLokasi.getText();
        
        double luas = 0.0;
        try {
            luas = Double.parseDouble(txtluasLahan.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Luas Lahan harus berupa angka!");
            return;
        }
        
        if (idLahan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan ID Lahan yang akan diubah!");
            return;
        }
        
        objekLahan.ubahAnggota(idLahan, idPetaniFK, luas, lokasi);
        tampilDataLahan(); 
        txtIdLahan.setEnabled(true);
    
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        String idLahan = txtIdLahan.getText();
        if (idLahan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan ID Lahan yang akan dihapus!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin hapus data ID Lahan " + idLahan + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (konfirmasi == JOptionPane.YES_OPTION) {
             objekLahan.hapusAnggota01(idLahan);
             tampilDataLahan(); 
             txtIdLahan.setEnabled(true);
             
             // Kosongkan fields setelah hapus
             txtIdLahan.setText("");
             txtluasLahan.setText("");
             txtLokasi.setText("");
             cmbIdPetani.setSelectedIndex(0); 
        }

    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtCariActionPerformed

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariKeyPressed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        // TODO add your handling code here:
         if (txtCari.getText().isEmpty()) {
        tampilDataLahan();
    } else {
        cariDataLahan();
    }
    }//GEN-LAST:event_txtCariKeyReleased

    private void btnLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanActionPerformed
        // TODO add your handling code here:
        objekLahan.cetakLaporan(
    "src/laporan/laporanLahan.jrxml",
    "SELECT l.id_lahan, p.nama_petani, l.luas_lahan_ha, l.lokasi " +
    "FROM lahan l JOIN petani p ON l.id_petani = p.id_petani"
);

    }//GEN-LAST:event_btnLaporanActionPerformed

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
            java.util.logging.Logger.getLogger(frameLahan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameLahan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameLahan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameLahan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frameLahan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbIdPetani;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelLahan;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtIdLahan;
    private javax.swing.JTextField txtLokasi;
    private javax.swing.JTextField txtluasLahan;
    // End of variables declaration//GEN-END:variables
}
