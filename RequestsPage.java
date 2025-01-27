import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class RequestsPage extends JFrame implements ActionListener
{
    static final int WIDTH = 1920;
    static final int LENGTH = 1080;
    BankAutomated BA;
    HomePage home;
    CA customer;

    private final JComboBox<String> selectType;
    private final JButton backToHome;
    private final JButton completeButton;
    private final JTextArea detailsField;

    public RequestsPage(BankAutomated BA, HomePage home, CA customer)
    {
        this.setTitle("Make a Request");
        this.setLayout(null);
        this.home = home;
        this.customer = customer;
        this.BA = BA;

        Font labels = new Font("Raleway", Font.BOLD, 30);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);

        JLabel requestType = new JLabel("Type of Request:");
        requestType.setFont(labels);
        requestType.setBorder(emptyBorder);
        requestType.setForeground(Color.black);
        requestType.setBounds(200,150,300,40);
        this.add(requestType);

        String[] accounts = {"Select Request Type", "Request System Changes", "Request Meeting with Admin",
                "Request Support from Customer Service"};
        selectType = new JComboBox<>(accounts);
        selectType.setFont(new Font("Arial", Font.PLAIN, 20));
        selectType.setBounds(600, 150, 500, 40);
        selectType.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectType.addActionListener(this);
        this.add(selectType);

        String instructions = "Enter the details of your request here. If requesting a meeting, make\nsure to " +
                "include a few dates/timings for the admins to pick one, as well as\nyour preferred meeting setting" +
                " (in-person or virtual)\nPlease clear this box before adding your details.";
        detailsField = new JTextArea(instructions);
        detailsField.setFont(new Font("SansSerif", Font.PLAIN, 22));
        detailsField.setBackground(Color.white);
        detailsField.setForeground(Color.BLACK);
        detailsField.setBounds(300, 210, 700, 300);

        JScrollPane scroll = new JScrollPane(detailsField);
        scroll.setBounds(300, 210, 700, 300);
        scroll.setBorder(border);
        detailsField.setEditable(true);
        this.add(scroll);

        completeButton = new JButton("Submit Request");
        completeButton.setFont(new Font("SansSerif", Font.PLAIN, 22));
        completeButton.setBounds(475, 525, 350, 40);
        completeButton.setBackground(Color.black);
        completeButton.setForeground(Color.white);
        completeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        completeButton.setBorder(emptyBorder);
        completeButton.addActionListener(this);
        this.add(completeButton);

        backToHome = new JButton("Back to Home");
        backToHome.setFont(new Font("SansSerif", Font.PLAIN, 22));
        backToHome.setBounds(475, 575, 350, 50);
        backToHome.setBackground(Color.white);
        backToHome.setForeground(new Color(57, 107, 170));
        backToHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToHome.setContentAreaFilled(false);
        backToHome.setFocusPainted(false);
        backToHome.setBorder(emptyBorder);
        backToHome.setContentAreaFilled(false);
        backToHome.addActionListener(this);
        this.add(backToHome);

        this.addWindowListener(new WindowEventHandler() {
            @Override
            public void windowClosing(WindowEvent evt) {
                //BA.logout (logic.logout) would be called here
                //Write all changes to the file
                BA.logout();

                Window[] windows = Window.getWindows();
                for (Window window : windows) {
                    window.dispose();
                }

                System.exit(0);
            }
        });
        this.getContentPane().setBackground(Color.white);
        this.getRootPane().setDefaultButton(completeButton);
        this.setSize(WIDTH, LENGTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        Color myRed = new Color(230, 30, 30);
        Color myBlack = new Color(160, 32, 32);
        GradientPaint redToBlack = new GradientPaint(0, 0, myRed, 0, 150, myBlack);
        g2.setPaint(redToBlack);
        g2.fillRect(0, 0, WIDTH+1, 150);

        Font regFont = new Font("Raleway", Font.BOLD, 60);
        g2.setFont(regFont);
        g2.setColor(new Color(250, 185, 60));
        g2.drawString("Make a Request", 25, 110);
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == backToHome)
        {
            this.setVisible(false);
            home.setVisible(true);
        }
        else if (e.getSource() == completeButton)
        {
            String requestDescription = detailsField.getText();
            if (requestDescription.equals("") || requestDescription.contains("Enter the details of your request here.")
                || requestDescription.contains("If requesting a meeting")
                || requestDescription.contains("Please clear this box before adding your details."))
            {
                JOptionPane.showMessageDialog(this, "Please clear the text box and add details" +
                        " to your request before submitting.");
            }
            else if (selectType.getSelectedIndex()==0)
            {
                JOptionPane.showMessageDialog(this, "Please select request type before submitting.");
            }
            else
            {
                String selectedType = String.valueOf(selectType.getSelectedItem());
                switch (selectedType)
                {
                    case "Request System Changes":
                        JOptionPane.showMessageDialog(this, "Thank you for making a request to" +
                                " make our system better. Here at BCS, we strive\nto keep our customers satisfied. Our " +
                                "maintenance team will look into your request\nshortly.");
                        BA.makeRequest(customer, "1", requestDescription);
                        break;
                    case "Request Meeting with Admin":
                        JOptionPane.showMessageDialog(this, "Thank you for making a request to" +
                                " meet an admin. An admin will review it and\ncontact you shortly as per your notification" +
                                " preferences.");
                        BA.makeRequest(customer, "2", requestDescription);
                        break;
                    case "Request Support from Customer Service":
                        JOptionPane.showMessageDialog(this, "Thank you for making a request to" +
                                "get support from customer service. An customer service representative\nwill review it and " +
                                "contact you shortly as per your notification preferences.");
                        BA.makeRequest(customer, "3", requestDescription);
                        break;
                    default:
                        System.out.println("ERROR: Something went wrong.");
                }
                this.setVisible(false);
                home.setVisible(true);
            }
        }
    }
}
