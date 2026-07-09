package app.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import app.AppException;
import app.checkin.CheckInRoomForm;
import app.checkout.CheckOutRoomForm;
import app.model.CheckinResult;
import app.model.CheckoutResult;
import app.model.ReservationResult;
import app.reservation.ReserveRoomForm;
import util.DateUtil;

/**
 * GUI class for Hotel Reservation System.
 * 
 * This class only handles screen input/output.
 * The actual business logic is delegated to Form/Control classes.
 */
public class HotelReservationFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextArea messageArea;

    private JTextField reservationDateField;
    private JTextField reservationNumberForCheckInField;
    private JTextField roomNumberForCheckOutField;

    public HotelReservationFrame() {
        initializeFrame();
        initializeComponents();
    }

    private void initializeFrame() {
        setTitle("Hotel Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 560);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(760, 500));
    }

    private void initializeComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(245, 247, 250));

        JPanel headerPanel = createHeaderPanel();
        JPanel menuPanel = createMenuPanel();
        JPanel contentPanel = createContentPanel();

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(menuPanel, BorderLayout.WEST);
        rootPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(rootPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(38, 70, 83));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel titleLabel = new JLabel("Hotel Reservation System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel subtitleLabel = new JLabel("Reservation / Check-in / Check-out");
        subtitleLabel.setForeground(new Color(220, 230, 235));
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.SOUTH);

        panel.add(textPanel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(233, 236, 239));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));
        panel.setLayout(new GridBagLayout());

        JButton reservationButton = createMenuButton("Reservation");
        JButton checkInButton = createMenuButton("Check-in");
        JButton checkOutButton = createMenuButton("Check-out");

        reservationButton.addActionListener(e -> showCard("reservation"));
        checkInButton.addActionListener(e -> showCard("checkin"));
        checkOutButton.addActionListener(e -> showCard("checkout"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 12, 0);

        gbc.gridy = 0;
        panel.add(reservationButton, gbc);

        gbc.gridy = 1;
        panel.add(checkInButton, gbc);

        gbc.gridy = 2;
        panel.add(checkOutButton, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        panel.add(new JLabel(""), gbc);

        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setPreferredSize(new Dimension(160, 42));
        return button;
    }

    private JPanel createContentPanel() {
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(new Color(245, 247, 250));
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createReservationPanel(), "reservation");
        mainPanel.add(createCheckInPanel(), "checkin");
        mainPanel.add(createCheckOutPanel(), "checkout");

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setRows(6);
        messageArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Message"));

        wrapperPanel.add(mainPanel, BorderLayout.CENTER);
        wrapperPanel.add(scrollPane, BorderLayout.SOUTH);

        showCard("reservation");
        appendMessage("System started.");

        return wrapperPanel;
    }

    private JPanel createReservationPanel() {
        JPanel panel = createBasePanel("Room Reservation");

        reservationDateField = new JTextField(20);
        reservationDateField.setText("2026/07/10");

        JButton submitButton = new JButton("Reserve Room");
        submitButton.addActionListener(e -> reserveRoom());

        addFormRow(panel, 1, "Arrival date", reservationDateField);
        addFormNote(panel, 2, "Input format: yyyy/mm/dd");
        addButtonRow(panel, 3, submitButton);

        return panel;
    }

    private JPanel createCheckInPanel() {
        JPanel panel = createBasePanel("Check-in");

        reservationNumberForCheckInField = new JTextField(20);

        JButton submitButton = new JButton("Check-in");
        submitButton.addActionListener(e -> checkInRoom());

        addFormRow(panel, 1, "Reservation number", reservationNumberForCheckInField);
        addFormNote(panel, 2, "Use the reservation number displayed after reservation.");
        addButtonRow(panel, 3, submitButton);

        return panel;
    }

    private JPanel createCheckOutPanel() {
        JPanel panel = createBasePanel("Check-out");

        roomNumberForCheckOutField = new JTextField(20);

        JButton submitButton = new JButton("Check-out");
        submitButton.addActionListener(e -> checkOutRoom());

        addFormRow(panel, 1, "Room number", roomNumberForCheckOutField);
        addFormNote(panel, 2, "Use the room number displayed after check-in.");
        addButtonRow(panel, 3, submitButton);

        return panel;
    }

    private JPanel createBasePanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(38, 70, 83));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 24, 0);

        panel.add(titleLabel, gbc);
        return panel;
    }

    private void addFormRow(JPanel panel, int row, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));

        textField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        textField.setPreferredSize(new Dimension(260, 34));

        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.gridx = 0;
        labelGbc.gridy = row;
        labelGbc.anchor = GridBagConstraints.WEST;
        labelGbc.insets = new Insets(0, 0, 12, 18);

        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.gridx = 1;
        fieldGbc.gridy = row;
        fieldGbc.anchor = GridBagConstraints.WEST;
        fieldGbc.insets = new Insets(0, 0, 12, 0);

        panel.add(label, labelGbc);
        panel.add(textField, fieldGbc);
    }

    private void addFormNote(JPanel panel, int row, String noteText) {
        JLabel noteLabel = new JLabel(noteText);
        noteLabel.setForeground(new Color(108, 117, 125));
        noteLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 20, 0);

        panel.add(noteLabel, gbc);
    }

    private void addButtonRow(JPanel panel, int row, JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 0, 0, 0);

        panel.add(button, gbc);
    }

    private void showCard(String name) {
        cardLayout.show(mainPanel, name);
    }

    private void reserveRoom() {
        try {
            String dateText = reservationDateField.getText();

            if (dateText == null || dateText.trim().length() == 0) {
                appendMessage("Invalid input: arrival date is empty.");
                return;
            }

            Date stayingDate = DateUtil.convertToDate(dateText.trim());

            if (stayingDate == null) {
                appendMessage("Invalid input: date format must be yyyy/mm/dd.");
                return;
            }

			ReserveRoomForm form = new ReserveRoomForm();
			form.setStayingDate(stayingDate);

			ReservationResult result = form.submitReservationDetail();
			String reservationNumber = result.getReservationNumber();

			appendMessage("Reservation completed.");
			appendMessage("Arrival date: " + DateUtil.convertToString(result.getStayingDate()));
			appendMessage("Price: " + result.getPrice());
			appendMessage("Reservation number: " + reservationNumber);

            reservationNumberForCheckInField.setText(reservationNumber);
            showCard("checkin");
        }
        catch (AppException e) {
            appendError(e);
        }
    }

    private void checkInRoom() {
        try {
            String reservationNumber = reservationNumberForCheckInField.getText();

            if (reservationNumber == null || reservationNumber.trim().length() == 0) {
                appendMessage("Invalid input: reservation number is empty.");
                return;
            }

			CheckInRoomForm form = new CheckInRoomForm();
			form.setReservationNumber(reservationNumber.trim());

			CheckinResult result = form.checkInDetail();
			String roomNumber = result.getRoomNumber();

			appendMessage("Check-in completed.");
			appendMessage("Room number: " + roomNumber);
			appendMessage("Check-in date: " + DateUtil.convertToString(result.getCheckinDate()));
			appendMessage("Check-out date: " + DateUtil.convertToString(result.getCheckoutDate()));

            roomNumberForCheckOutField.setText(roomNumber);
            showCard("checkout");
        }
        catch (AppException e) {
            appendError(e);
        }
    }

    private void checkOutRoom() {
        try {
            String roomNumber = roomNumberForCheckOutField.getText();

            if (roomNumber == null || roomNumber.trim().length() == 0) {
                appendMessage("Invalid input: room number is empty.");
                return;
            }

			CheckOutRoomForm form = new CheckOutRoomForm();
			form.setRoomNumber(roomNumber.trim());
			CheckoutResult result = form.checkOutDetail();

			appendMessage("Check-out completed.");
			appendMessage("Room number: " + result.getRoomNumber());
			appendMessage("Check-in date: " + DateUtil.convertToString(result.getCheckinDate()));
			appendMessage("Check-out date: " + DateUtil.convertToString(result.getCheckoutDate()));
			appendMessage("Price: " + result.getPrice());
			appendMessage("Thank you for using our hotel.");
        }
        catch (AppException e) {
            appendError(e);
        }
    }

    private void appendMessage(String message) {
        messageArea.append(message + System.lineSeparator());
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    private void appendError(AppException e) {
        appendMessage("Error occurred.");
        appendMessage(e.getFormattedDetailMessages(System.lineSeparator()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception e) {
                // Use default look and feel if system look and feel is not available.
            }

            HotelReservationFrame frame = new HotelReservationFrame();
            frame.setVisible(true);
        });
    }
}
