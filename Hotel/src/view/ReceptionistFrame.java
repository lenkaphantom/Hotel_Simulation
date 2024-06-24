package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import controler.ReservationControler;
import entity.Reservation;
import manage.ManageHotel;
import model.GuestModel;
import model.ReservationModel;
import model.RoomModel;
import net.miginfocom.swing.MigLayout;
import view.addedit.AddEditGuestDialog;
import view.addedit.AddEditReservationDialog;
import view.comboBoxRenderer.AdditionalServicesCellRenderer;
import view.popup.AdditionalServicesPopup;

public class ReceptionistFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private ManageHotel manager = ManageHotel.getInstance();
	private ReservationControler controler;

	private static final Color BACKGROUND_COLOR = new Color(214, 204, 194);
	private static final Color FOREGROUND_COLOR = new Color(102, 0, 34);

	private JPanel contentPane;

	private JTable reservationTable;
	private JTable roomTable;
	private JTable guestTable;

	private JTextField tfSearchReservation;
	private JTextField tfSearchRoom;
	private JTextField tfSearchGuest;

	private TableRowSorter<AbstractTableModel> reservationTableSorter;
	private TableRowSorter<AbstractTableModel> roomTableSorter;
	private TableRowSorter<AbstractTableModel> guestTableSorter;

	private JButton addBtn = new JButton();
	private JButton confirmBtn = new JButton();
	private JButton checkInBtn = new JButton();
	private JButton checkOutBtn = new JButton();

	private JButton addBtnGuest = new JButton();
	private JButton editBtnGuest = new JButton();
	private JButton removeBtnGuest = new JButton();

	Image img = new ImageIcon("img/add.png").getImage();
	Image newimg = img.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);

	Image img1 = new ImageIcon("img/edit.png").getImage();
	Image newimg1 = img1.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);

	Image img2 = new ImageIcon("img/remove.png").getImage();
	Image newimg2 = img2.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReceptionistFrame frame = new ReceptionistFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ReceptionistFrame() {
		setBackground(BACKGROUND_COLOR);
		setForeground(FOREGROUND_COLOR);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("img/hotel.png").getImage());
		setBounds(100, 100, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[5px][grow]", "[5px,grow]"));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "cell 1 0,grow");

		addBtn.setIcon(new ImageIcon(newimg));

		confirmBtn.setText("Potvrdi");
		checkInBtn.setText("Check in");
		checkOutBtn.setText("Check out");

		ReservationModel reservationModel = new ReservationModel("");
		controler = reservationModel.getControler();

		reservationTable = new JTable(reservationModel);
		reservationTableSorter = new TableRowSorter<>((AbstractTableModel) reservationTable.getModel());
		reservationTable.setRowSorter(reservationTableSorter);
		
		reservationTable.setRowHeight(20);

		TableCellRenderer additionalServicesRenderer = new AdditionalServicesCellRenderer();
		reservationTable.getColumnModel().getColumn(4).setCellRenderer(additionalServicesRenderer);

		reservationTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = reservationTable.rowAtPoint(e.getPoint());
				int column = reservationTable.columnAtPoint(e.getPoint());
				int modelRow = reservationTable.convertRowIndexToModel(row);

				if (column == 4) {
					Reservation reservation = manager.getReservationsMan().getReservations().get(modelRow + 1);
					new AdditionalServicesPopup(ReceptionistFrame.this,
							manager.getAdditionalServicesList(reservation.getId())).setVisible(true);
				}
			}
		});

		JPanel reservationPanel = createPanelWithTable(reservationTable, addBtn, confirmBtn, checkInBtn, checkOutBtn);
		tabbedPane.addTab("Rezervacije", null, reservationPanel, null);

		roomTable = new JTable(new RoomModel("", null));
		roomTableSorter = new TableRowSorter<>((AbstractTableModel) roomTable.getModel());
		roomTable.setRowSorter(roomTableSorter);
		
		roomTable.setRowHeight(20);

		JPanel roomPanel = createRoomPanel(roomTable);
		tabbedPane.addTab("Sobe", null, roomPanel, null);

		addBtnGuest.setIcon(new ImageIcon(newimg));
		editBtnGuest.setIcon(new ImageIcon(newimg1));
		removeBtnGuest.setIcon(new ImageIcon(newimg2));

		guestTable = new JTable(new GuestModel());
		guestTableSorter = new TableRowSorter<>((AbstractTableModel) guestTable.getModel());
		guestTable.setRowSorter(guestTableSorter);
		
		guestTable.setRowHeight(20);

		JPanel guestPanel = createGuestPanel(guestTable, addBtnGuest, editBtnGuest, removeBtnGuest);
		tabbedPane.addTab("Gosti", null, guestPanel, null);

		initActions();
	}

	private JPanel createPanelWithTable(JTable table, JButton addBtn, JButton confirmButton, JButton checkInButton,
			JButton checkOutButton) {
		JPanel panel = new JPanel(new MigLayout("", "[grow]", "[][][grow]"));

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(addBtn);
		toolBar.add(confirmButton);
		toolBar.add(checkInButton);
		toolBar.add(checkOutButton);
		panel.add(toolBar, "flowx,cell 0 0");

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setForeground(FOREGROUND_COLOR);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane, "cell 0 1 1 2,grow");

		int[] columnWidths = new int[] { 50, 100, 100, 100, 150, 100, 100, 100, 100 };
		setTableColumnWidths(table, columnWidths);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanel.setBackground(BACKGROUND_COLOR);
		JTextField searchField = new JTextField(20);
		searchPanel.add(new JLabel("Pretraga:"));
		searchPanel.add(searchField);

		panel.add(searchPanel, "cell 0 0, grow");

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				filterTable();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filterTable();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				filterTable();
			}

			private void filterTable() {
				String text = searchField.getText();
				if (text.trim().length() == 0) {
					reservationTableSorter.setRowFilter(null);
				} else {
					reservationTableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}
		});

		return panel;
	}

	private void setTableColumnWidths(JTable table, int[] columnWidths) {
		for (int i = 0; i < columnWidths.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
		}
	}

	private JPanel createRoomPanel(JTable table) {
		JPanel panel = new JPanel(new MigLayout("", "[grow]", "[][][grow]"));

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);

		panel.add(toolBar, "flowx,cell 0 0");

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setForeground(FOREGROUND_COLOR);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane, "cell 0 1 1 2,grow");

		int[] columnWidths = new int[] { 50, 100, 100, 100, 150, 100 };
		setTableColumnWidths(table, columnWidths);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanel.setBackground(BACKGROUND_COLOR);
		JTextField searchField = new JTextField(20);
		searchPanel.add(new JLabel("Pretraga:"));
		searchPanel.add(searchField);

		panel.add(searchPanel, "cell 0 0, grow");

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				filterTable();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filterTable();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				filterTable();
			}

			private void filterTable() {
				String text = searchField.getText();
				if (text.trim().length() == 0) {
					roomTableSorter.setRowFilter(null);
				} else {
					roomTableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}
		});

		return panel;
	}

	private JPanel createGuestPanel(JTable table, JButton addBtn, JButton editBtn, JButton removeBtn) {
		JPanel panel = new JPanel(new MigLayout("", "[grow]", "[][][grow]"));

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(addBtn);
		toolBar.add(editBtn);
		toolBar.add(removeBtn);
		panel.add(toolBar, "flowx,cell 0 0");

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setForeground(FOREGROUND_COLOR);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane, "cell 0 1 1 2,grow");

		int[] columnWidths = new int[] { 50, 100, 100, 100, 150, 100, 100, 100, 100 };
		setTableColumnWidths(table, columnWidths);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanel.setBackground(BACKGROUND_COLOR);
		JTextField searchField = new JTextField(20);
		searchPanel.add(new JLabel("Pretraga:"));
		searchPanel.add(searchField);

		panel.add(searchPanel, "cell 0 0, grow");

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				filterTable();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filterTable();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				filterTable();
			}

			private void filterTable() {
				String text = searchField.getText();
				if (text.trim().length() == 0) {
					guestTableSorter.setRowFilter(null);
				} else {
					guestTableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}
		});

		return panel;
	}

	private void initActions() {
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddEditReservationDialog addEditReservationDialog = new AddEditReservationDialog(ReceptionistFrame.this,
						0, controler);
				addEditReservationDialog.setVisible(true);
				refreshReservationTable();
			}
		});

		addBtnGuest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddEditGuestDialog addEditGuestDialog = new AddEditGuestDialog(ReceptionistFrame.this, 0);
				addEditGuestDialog.setVisible(true);
				refreshGuestTable();
			}
		});

		editBtnGuest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = guestTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = guestTable.convertRowIndexToModel(selectedRow);
					AddEditGuestDialog addEditGuestDialog = new AddEditGuestDialog(ReceptionistFrame.this, modelRow + 1);
					addEditGuestDialog.setVisible(true);
					refreshGuestTable();
				} else {
					JOptionPane.showMessageDialog(ReceptionistFrame.this, "Morate izabrati red u tabeli.", "Greška",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		removeBtnGuest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = guestTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = guestTable.convertRowIndexToModel(selectedRow);
					manager.getGuestsMan().removeGuest(modelRow + 1);
					refreshGuestTable();
				} else {
					JOptionPane.showMessageDialog(ReceptionistFrame.this, "Morate izabrati red u tabeli.", "Greška",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	public void refreshReservationTable() {
		((AbstractTableModel) reservationTable.getModel()).fireTableDataChanged();
	}

	public void refreshGuestTable() {
		((AbstractTableModel) guestTable.getModel()).fireTableDataChanged();
	}
}
