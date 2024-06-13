import com.mybank.data.DataSource;
import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.reporting.CustomerReport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

public class Main extends JFrame {

    private JPanel Panel;
    private JButton Show;
    private JButton Report;
    private JButton About;
    private JComboBox<String> comboBox1;
    private JTextPane textPane1;

    public Main() {
        setContentPane(Panel);
        setSize(350, 400);
        setTitle("Bank");
        setVisible(true);

        Show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer customer = Bank.getCustomer(comboBox1.getSelectedIndex());
                StringBuilder text;

                text = new StringBuilder(String.format(
                        "<b>%s %s</b>, customer #%d<br>%s<br>",
                        customer.getFirstName(), customer.getLastName(), comboBox1.getSelectedIndex() + 1,
                        "-".repeat(30)
                ));

                for (int i = 0; i < customer.getNumberOfAccounts(); i++) {
                    Account account = customer.getAccount(i);

                    String accountType = account instanceof CheckingAccount ? "Checking" : "Savings";

                    text.append(String.format(
                            "#%d - <b>%s</b>: $%.2f<br>", i, accountType, account.getBalance()
                    ));
                }

                textPane1.setText(text.toString());
            }
        });

        Report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(byteStream);

                System.setOut(printStream);

                new CustomerReport().generateReport();

                System.out.flush();
                System.setOut(System.out);

                textPane1.setText(byteStream.toString().replace("\n", "<br>"));
            }
        });

        About.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textPane1.setText("BankClient");
            }
        });



        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            comboBox1.addItem(Bank.getCustomer(i).getLastName() + " " + Bank.getCustomer(i).getFirstName());
        }

    }

    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Locale.setDefault(Locale.US);

        DataSource dataSource = new DataSource("./data/test.dat");
        dataSource.loadData();

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        new Main();
    }
}