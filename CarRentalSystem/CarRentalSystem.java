import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class CarRentalSystem extends JFrame implements MouseListener {
    private JPanel panel, navigationPanel;
    private JLabel bannerLabel;

    private JLabel[] imageLabels, priceLabels, availableLabels, modelLabels;
    private JButton saveButton, confirmButton, clearButton;
    private JLabel totalLabel, carLabel, dayLabel, daySelectLabel, revenueLabel;

    private JTextArea orderHistoryArea;
    private JSpinner daySpinner;
    private ImageIcon[] icons;

    private String[] carNames = {"Toyota", "Honda", "Nissan", "Mitsubishi", "Mazda", "Audi", "BMW", "Mercedes", "Ford"};
    private int[] prices = {160, 145, 157, 136, 120, 185, 200, 220, 180};
    private int[] availability = {5, 3, 4, 2, 6, 4, 3, 5, 2};

    private int selectedCarIndex = 0;
    private int selectedDays = 1;
    private int totalRevenue = 0;

    public CarRentalSystem() {
        super("Car Rental System");
        setBounds(20, 10, 1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(210, 230, 255));
        panel.setPreferredSize(new Dimension(1500, 1400));

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        imageLabels = new JLabel[9];
        priceLabels = new JLabel[9];
        availableLabels = new JLabel[9];
        modelLabels = new JLabel[9];
        icons = new ImageIcon[9];

        for (int i = 0; i < 9; i++) {
            int column = i / 3;
            int row = i % 3;
            int x = 65 + (column * 210);
            int y = 190 + (row * 300);

            icons[i] = new ImageIcon("Pic/C" + (i + 1) + ".jpg");

            JPanel imagePanel = new JPanel();
            imagePanel.setBounds(x, y, 200, 200);
            imagePanel.setBackground(new Color(240, 240, 255));
            imageLabels[i] = new JLabel(icons[i]);
            imagePanel.add(imageLabels[i]);
            panel.add(imagePanel);

            JPanel modelPanel = new JPanel();
            modelPanel.setBounds(x, y + 210, 200, 25);
            modelPanel.setBackground(Color.WHITE);

            final int index = i;
            modelLabels[i] = new JLabel("Model: " + carNames[i]);
            modelLabels[i].setForeground(Color.RED);
            modelLabels[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            modelLabels[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selectedCarIndex = index;
                    updateLabels();
                    JOptionPane.showMessageDialog(panel, "You selected: " + carNames[selectedCarIndex]);
                }
            });

            modelPanel.add(modelLabels[i]);
            panel.add(modelPanel);

            JPanel pricePanel = new JPanel();
            pricePanel.setBounds(x, y + 240, 200, 25);
            pricePanel.setBackground(Color.WHITE);
            priceLabels[i] = new JLabel("Rate/Day: $" + prices[i]);
            pricePanel.add(priceLabels[i]);
            panel.add(pricePanel);

            JPanel availPanel = new JPanel();
            availPanel.setBounds(x, y + 270, 200, 25);
            availPanel.setBackground(Color.WHITE);
            availableLabels[i] = new JLabel("Available: " + availability[i]);
            availPanel.add(availableLabels[i]);
            panel.add(availPanel);
        }

        navigationPanel = new JPanel();
        navigationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 100, 10));
        navigationPanel.setBounds(0, 0, 1500, 40);
        navigationPanel.setBackground(new Color(70, 130, 180));

        String[] navigationItems = {"Home", "About", "Deals", "Help", "Contact"};
        for (String item : navigationItems) {
            JLabel label = new JLabel(item);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            navigationPanel.add(label);
        }
        panel.add(navigationPanel);

        ImageIcon bannerIcon = new ImageIcon("Pic/Banner.png");
        Image bannerImage = bannerIcon.getImage().getScaledInstance(1200, 130, Image.SCALE_SMOOTH);
        bannerIcon = new ImageIcon(bannerImage);
        bannerLabel = new JLabel(bannerIcon);
        bannerLabel.setBounds(150, 45, 1200, 130);
        panel.add(bannerLabel);

        daySelectLabel = new JLabel("Select Days:");
        daySelectLabel.setBounds(800, 240, 100, 30);
        panel.add(daySelectLabel);

        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        daySpinner.setBounds(900, 240, 60, 30);
        daySpinner.addChangeListener(e -> {
            selectedDays = (Integer) daySpinner.getValue();
            updateLabels();
        });
        panel.add(daySpinner);

        carLabel = new JLabel("Selected Car : None");
        carLabel.setBounds(800, 300, 200, 30);
        carLabel.setBackground(Color.BLACK);
        carLabel.setForeground(Color.WHITE);
        carLabel.setOpaque(true);
        panel.add(carLabel);

        dayLabel = new JLabel("Selected days : 00");
        dayLabel.setBounds(800, 350, 200, 30);
        dayLabel.setBackground(Color.BLACK);
        dayLabel.setForeground(Color.WHITE);
        dayLabel.setOpaque(true);
        panel.add(dayLabel);

        saveButton = new JButton("Save");
        saveButton.setBounds(850, 450, 100, 30);
        saveButton.setBackground(Color.CYAN);
        saveButton.addMouseListener(this);
        panel.add(saveButton);

        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(960, 450, 100, 30);
        confirmButton.setBackground(Color.ORANGE);
        confirmButton.addMouseListener(this);
        panel.add(confirmButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(1070, 450, 100, 30);
        clearButton.setBackground(Color.RED);
        clearButton.addMouseListener(this);
        panel.add(clearButton);

        totalLabel = new JLabel("Total Price : $0");
        totalLabel.setBounds(800, 500, 200, 30);
        totalLabel.setBackground(Color.WHITE);
        totalLabel.setForeground(Color.BLUE);
        totalLabel.setOpaque(true);
        panel.add(totalLabel);

        revenueLabel = new JLabel("Total Revenue: $0");
        revenueLabel.setBounds(800, 530, 200, 30);
        revenueLabel.setForeground(Color.MAGENTA);
        panel.add(revenueLabel);

        orderHistoryArea = new JTextArea();
        orderHistoryArea.setEditable(false);
        JScrollPane historyScrollPane = new JScrollPane(orderHistoryArea);
        historyScrollPane.setBounds(800, 570, 500, 150);
        panel.add(historyScrollPane);

        loadOrderHistory();
    }

    private void updateLabels() {
        carLabel.setText("Selected Car : " + carNames[selectedCarIndex]);
        dayLabel.setText("Selected days : " + selectedDays);
        totalLabel.setText("Total Price : $" + (prices[selectedCarIndex] * selectedDays));
    }

    private void loadOrderHistory() {
        try {
            File file = new File("rentaldata.txt");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    orderHistoryArea.append(line + "\n");
                }
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAvailabilityLabels() {
        for (int i = 0; i < availability.length; i++) {
            availableLabels[i].setText("Available: " + availability[i]);
        }
    }

    public void mouseClicked(MouseEvent me) {
        selectedDays = (Integer) daySpinner.getValue();
        int rate = prices[selectedCarIndex];
        int total = rate * selectedDays;

        if (availability[selectedCarIndex] == 0) {
            JOptionPane.showMessageDialog(this, "No more cars available for " + carNames[selectedCarIndex]);
            return;
        }

        if (me.getSource() == saveButton) {
            try {
                updateLabels();
                FileWriter writer = new FileWriter("rentaldata.txt", true);
                String order = "Car: " + carNames[selectedCarIndex] + ", Days: " + selectedDays + ", Total: $" + total;
                writer.write(order + "\n");
                writer.close();

                JOptionPane.showMessageDialog(this, "Rental data saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
            }

        } else if (me.getSource() == confirmButton) {
            availability[selectedCarIndex]--;
            updateAvailabilityLabels();

            String order = "Car: " + carNames[selectedCarIndex] + ", Days: " + selectedDays + ", Total: $" + total;
            orderHistoryArea.append(order + "\n");
            totalRevenue += total;
            revenueLabel.setText("Total Revenue: $" + totalRevenue);

            try {
                FileWriter writer = new FileWriter("rentaldata.txt", true);
                writer.write(order + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Rental Confirmed!");

        } else if (me.getSource() == clearButton) {
            try {
                new FileWriter("rentaldata.txt").close();
                orderHistoryArea.setText("");
                totalRevenue = 0;
                revenueLabel.setText("Total Revenue: $0");
                JOptionPane.showMessageDialog(this, "Order history cleared.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void mousePressed(MouseEvent me) {}
    public void mouseReleased(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}

    public static void main(String[] args) {
        new CarRentalSystem().setVisible(true);
    }
}

