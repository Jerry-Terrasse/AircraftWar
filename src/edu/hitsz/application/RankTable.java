package edu.hitsz.application;

import edu.hitsz.rank.Record;
import edu.hitsz.rank.RecordDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class RankTable {
    private JPanel rankPanel;
    private JPanel titlePanel;
    private JPanel rankListPanel;
    private JPanel buttonPanel;
    private JLabel mainTitle;
    private JScrollPane rankListScroll;
    private JTable rankListTable;
    private JButton deleteButton;
    private JLabel hardLevelLabel;
    private RecordDao recordDao;

    public RankTable(RecordDao recordDao) {
        this.recordDao = recordDao;
        String[] columnNames = Record.getColumnNames();
        String[][] rowData = recordDao.getTableData();
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rankListTable.setModel(tableModel);

        rankListScroll.setViewportView(rankListTable);

        deleteButton.addActionListener((ActionEvent e) -> {
            int row = rankListTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "请选择要删除的记录", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int record_id = Integer.parseInt((String)rankListTable.getValueAt(row, 4));
            System.out.println("deteting record " + record_id);

            tableModel.removeRow(row);
            recordDao.doDelete(record_id);
        });
    }

    public JPanel getMainPanel() {
        return rankPanel;
    }
}
