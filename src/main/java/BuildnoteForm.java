import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import constant.GlobalEnv;
import model.*;
import org.apache.commons.lang3.StringUtils;
import util.ButtonGroupUtil;
import util.MyCellRenderer;
import util.RedmineHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by 140179 on 2015-06-11.
 */
public class BuildnoteForm extends JFrame implements ActionListener {
    private Issue issue;
    private Buildnote buildnote;

    private ButtonGroup btnGroup;
    private JPanel rootPanel;
    private JPanel loginPanel;
    private JTextField inputRedmineId;
    private JPasswordField inputRedminePw;
    private JButton btnLogin;
    private JCheckBox chkSaveLogin;
    private JTextField inputRedmineTaskNo;
    private JButton btnSearchTaskRedmine;
    private JTextField inputTaskRedmineTitle;
    private JTextField inputDevStartDate;
    private JTextField inputDueDate;
    private JTextField inputQaDate;
    private JList listChangeSystems;
    private JList listDistSystems;
    private JList listRequesters;
    private JTextField inputRedmineSqlNo;
    private JButton btnSearchSqlRedmine;
    private JTextField inputSqlRedmineTitle;
    private JRadioButton warRadioButton;
    private JRadioButton jarRadioButton;
    private JTextArea inputRequirements;
    private JTextArea inputFiles;
    private JButton btnRegister;

    ArrayList<Author> authorList = null;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public BuildnoteForm() {
        // Button Group 추가
        btnGroup = new ButtonGroup();
        btnGroup.add(warRadioButton);
        btnGroup.add(jarRadioButton);

        // 로그인정보 저장되어있는지 여부를 확인하여 보여줌
        Config config = readConfigFile();
        if (config != null) {
            inputRedmineId.setText(config.getId());
            inputRedminePw.setText(config.getPwd());
            chkSaveLogin.setSelected(true);
        }

        // Buildnote 초기화
        buildnote = new Buildnote();

        btnLogin.addActionListener(this);
        btnSearchTaskRedmine.addActionListener(this);
        btnSearchSqlRedmine.addActionListener(this);
        btnRegister.addActionListener(this);
        inputRedminePw.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(inputRedminePw) || e.getSource().equals(btnLogin)) {
            login();
        } else if (e.getSource().equals(btnSearchTaskRedmine)) {
            String issueNo = inputRedmineTaskNo.getText();
            getRedmineTaskIssue(issueNo);
        } else if (e.getSource().equals(btnSearchSqlRedmine)) {
            String issueNo = inputRedmineSqlNo.getText();
            getRedmineSqlIssue(issueNo);
        } else if (e.getSource().equals(btnRegister)) {
            register();
        }
    }

    public void getRedmineTaskIssue(String issueNo) {
        Issue issue = RedmineHelper.getIssue(issueNo);
        if (issue != null) {
            System.out.println(issue.toString());
            String subject = issue.getSubject();
            String startDate = issue.getStartDate();
            String dueDate = issue.getDueDate();

            inputTaskRedmineTitle.setText(subject);
            inputDevStartDate.setText(startDate);
            inputDueDate.setText(dueDate);

            Iterator itr = authorList.iterator();
            Author author = null;
            while (itr.hasNext()) {
                Author temp = (Author) itr.next();
                if (temp.getId() == issue.getAuthor().getId()) {
                    author = temp;
                    break;
                }
            }
            listRequesters.setSelectedValue(author, true);

            buildnote.setTaskIssue(issue);
        } else {
            JOptionPane.showMessageDialog(rootPanel, "Task 조회에 실패하였습니다.");
        }
    }

    public void getRedmineSqlIssue(String issueNo) {
        Issue issue = RedmineHelper.getIssue(issueNo);
        if (issue != null) {
            System.out.println(issue.toString());

            inputSqlRedmineTitle.setText(issue.getSubject());

            buildnote.setSqlIssue(issue);
        } else {
            JOptionPane.showMessageDialog(rootPanel, "SQL 검수요청 Task 조회에 실패하였습니다.");
        }
    }

    public void login() {
        String id = inputRedmineId.getText();
        String pwd = new String(inputRedminePw.getPassword());

        if (StringUtils.isBlank(id) || StringUtils.isBlank(pwd)) {
            JOptionPane.showMessageDialog(rootPanel, "아이디 또는 비밀번호를 입력하세요.");
        } else {
            System.out.println("id: " + id);

            User user = RedmineHelper.login(id, pwd);

            if (user != null) {
                // 변경작업시스템 리스트 구성
                DefaultListModel changeSystemModel = new DefaultListModel();
                for (int i = 0; i < GlobalEnv.systemList.length; i++) {
                    changeSystemModel.add(i, GlobalEnv.systemList[i]);
                }
                listChangeSystems.setModel(changeSystemModel);

                // 배포시스템 리스트 구성
                DefaultListModel distSystemModel = new DefaultListModel();
                for (int i = 0; i < GlobalEnv.distSystemList.length; i++) {
                    distSystemModel.add(i, GlobalEnv.distSystemList[i]);
                }
                listDistSystems.setModel(distSystemModel);

                // 요청자/기획자 구성
                DefaultListModel<Author> requesterModel = new DefaultListModel<Author>();
                authorList = RedmineHelper.getAuthorList();
                for (int i = 0; i < authorList.size(); i++) {
                    requesterModel.add(i, authorList.get(i));
                }
                listRequesters.setModel(requesterModel);
                listRequesters.setCellRenderer(new MyCellRenderer());

                buildnote.setUser(user);

                // 로그인정보 저장 여부에 따라 config 파일 작성
                if (chkSaveLogin.isSelected()) {
                    System.out.println("config 파일 작성");

                    Config config = new Config();
                    config.setId(id);
                    config.setPwd(pwd);

                    writeConfigFile(config);
                }

                CardLayout layout = (CardLayout) rootPanel.getLayout();
                layout.show(rootPanel, "BuildnoteForm");
            } else {
                inputRedmineId.setText("");
                inputRedminePw.setText("");
                JOptionPane.showMessageDialog(rootPanel, "로그인에 실패하였습니다.");
            }
        }
    }

    public void register() {
        // 변경작업시스템
        buildnote.setChangeSystems(convertListToStringArray(listChangeSystems.getSelectedValuesList()));

        // 배포시스템
        buildnote.setDistSystems(convertListToStringArray(listDistSystems.getSelectedValuesList()));

        // 요청자 / 기획담당자
        Iterator itr = listRequesters.getSelectedValuesList().iterator();
        List<Author> requesters = new ArrayList<Author>();
        while (itr.hasNext()) {
            requesters.add((Author) itr.next());
        }
        buildnote.setRequesters(requesters);

        // 기획자 테스트 요구사항
        String[] requirements = StringUtils.split(inputRequirements.getText(), "\n");
        // 기획자 테스트 요구사항 텍스트 trim
        for (int i = 0; i < requirements.length; i++) {
            requirements[i] = StringUtils.trim(requirements[i]);
        }
        buildnote.setRequirements(requirements);

        // 소스 파일 경로
        String[] files = StringUtils.split(inputFiles.getText(), "\n");
        // 소스 파일 경로 텍스트 trim
        for (int i = 0; i < files.length; i++) {
            files[i] = StringUtils.trim(files[i]);
        }
        buildnote.setFiles(files);

        // 배포타입
        buildnote.setDistType(ButtonGroupUtil.getSelectedButtonText(btnGroup));

        // 기간
        buildnote.setDevStartDate(inputDevStartDate.getText());
        buildnote.setQaDate(inputQaDate.getText());
        buildnote.setDueDate(inputDueDate.getText());

        // 제목
        buildnote.setSubject(inputTaskRedmineTitle.getText());

        System.out.println(buildnote.getBuildnoteIssueString());
        Issue result = RedmineHelper.registerBuildnote(buildnote.getBuildnoteIssueString());

        if (result != null) {

            try {
                URI uri = new URI("https://redmine.ssgadm.com/redmine/issues/" + result.getId());
                openWebpage(uri);
                JOptionPane.showMessageDialog(rootPanel, "빌드노트 등록이 완료되었습니다.\n" + uri.toString());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(rootPanel, "빌드노트 등록 실패했습니다.");
            }


        } else {
            JOptionPane.showMessageDialog(rootPanel, "빌드노트 등록 실패했습니다.");
        }
    }

    public String[] convertListToStringArray(List list) {
        return (String[]) list.toArray(new String[list.size()]);
    }

    public Config readConfigFile() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("config.dat")));
            Config config = (Config) ois.readObject();
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't find config.dat file");
            return null;
        }
    }

    public void writeConfigFile(Config config) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("config.dat")));
            config.setLastLoginTime((new Date()).toString());
            oos.writeObject(config);
            oos.flush();
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new CardLayout(15, 20));
        rootPanel.setBackground(new Color(-1513240));
        rootPanel.setMaximumSize(new Dimension(850, 750));
        rootPanel.setPreferredSize(new Dimension(850, 750));
        loginPanel = new JPanel();
        loginPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:d:grow,fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;30px):noGrow,left:5dlu:noGrow,fill:max(d;4px):grow", "center:d:grow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:5dlu:noGrow,center:d:noGrow,top:5dlu:noGrow,center:d:noGrow,top:5dlu:noGrow,center:d:grow"));
        loginPanel.setBackground(new Color(-1513240));
        rootPanel.add(loginPanel, "Login");
        final JLabel label1 = new JLabel();
        label1.setText("아이디");
        CellConstraints cc = new CellConstraints();
        loginPanel.add(label1, cc.xy(3, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        inputRedmineId = new JTextField();
        inputRedmineId.setToolTipText("레드마인 아이디(사번)");
        loginPanel.add(inputRedmineId, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        label2.setText("비밀번호");
        loginPanel.add(label2, cc.xy(3, 7, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        inputRedminePw = new JPasswordField();
        inputRedminePw.setToolTipText("레드마인 비밀번호");
        loginPanel.add(inputRedminePw, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        btnLogin = new JButton();
        btnLogin.setText("로그인");
        loginPanel.add(btnLogin, cc.xywh(7, 5, 1, 3));
        chkSaveLogin = new JCheckBox();
        chkSaveLogin.setText("로그인정보 저장");
        loginPanel.add(chkSaveLogin, cc.xy(5, 9));
        final JLabel label3 = new JLabel();
        label3.setText("레드마인 아이디/비밀번호를 입력해주세요.");
        loginPanel.add(label3, cc.xyw(3, 3, 5));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        rootPanel.add(scrollPane1, "BuildnoteForm");
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:155px:noGrow,left:4dlu:noGrow,fill:34px:grow,left:5dlu:noGrow,fill:d:noGrow", "center:36px:noGrow,top:4dlu:noGrow,center:d:noGrow,top:6dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:5dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,bottom:max(d;4px):grow"));
        scrollPane1.setViewportView(panel1);
        final JLabel label4 = new JLabel();
        label4.setText("Redmine #");
        panel1.add(label4, cc.xy(1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        inputRedmineTaskNo = new JTextField();
        panel1.add(inputRedmineTaskNo, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        btnSearchTaskRedmine = new JButton();
        btnSearchTaskRedmine.setText("조회");
        panel1.add(btnSearchTaskRedmine, cc.xy(5, 1));
        final JLabel label5 = new JLabel();
        label5.setText("Redmine 내역");
        panel1.add(label5, cc.xy(1, 3, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        inputTaskRedmineTitle = new JTextField();
        panel1.add(inputTaskRedmineTitle, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label6 = new JLabel();
        label6.setText("QA 반영일자");
        panel1.add(label6, cc.xy(1, 7, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        inputQaDate = new JTextField();
        inputQaDate.setToolTipText("YYYY-MM-DD");
        panel1.add(inputQaDate, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("center:d:grow,left:4dlu:noGrow,center:max(d;4px):grow,left:5dlu:noGrow,center:max(d;4px):grow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        panel1.add(panel2, cc.xyw(1, 9, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label7 = new JLabel();
        label7.setText("변경작업시스템");
        panel2.add(label7, cc.xy(1, 1));
        final JLabel label8 = new JLabel();
        label8.setText("배포시스템");
        panel2.add(label8, cc.xy(3, 1));
        final JLabel label9 = new JLabel();
        label9.setText("요청자/기획담당자");
        panel2.add(label9, cc.xy(5, 1));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
        listChangeSystems = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        listChangeSystems.setModel(defaultListModel1);
        scrollPane2.setViewportView(listChangeSystems);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel2.add(scrollPane3, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.FILL));
        listDistSystems = new JList();
        scrollPane3.setViewportView(listDistSystems);
        final JScrollPane scrollPane4 = new JScrollPane();
        panel2.add(scrollPane4, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.FILL));
        listRequesters = new JList();
        scrollPane4.setViewportView(listRequesters);
        final JLabel label10 = new JLabel();
        label10.setText("SQL 검수요청 Task #");
        panel1.add(label10, cc.xy(1, 15, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        inputRedmineSqlNo = new JTextField();
        panel1.add(inputRedmineSqlNo, cc.xy(3, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        btnSearchSqlRedmine = new JButton();
        btnSearchSqlRedmine.setText("조회");
        panel1.add(btnSearchSqlRedmine, cc.xy(5, 15));
        final JLabel label11 = new JLabel();
        label11.setText("SQL 검수요청 내역");
        panel1.add(label11, cc.xy(1, 17, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        inputSqlRedmineTitle = new JTextField();
        panel1.add(inputSqlRedmineTitle, cc.xy(3, 17, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:p:grow,left:5dlu:noGrow,fill:p:grow", "center:16px:noGrow,top:4dlu:noGrow,center:d:noGrow"));
        panel1.add(panel3, cc.xyw(1, 13, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label12 = new JLabel();
        label12.setText("기획자 테스트 요구사항");
        panel3.add(label12, cc.xy(1, 1));
        final JLabel label13 = new JLabel();
        label13.setText("소스 파일 경로");
        panel3.add(label13, cc.xy(3, 1));
        final JScrollPane scrollPane5 = new JScrollPane();
        panel3.add(scrollPane5, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
        inputRequirements = new JTextArea();
        inputRequirements.setRows(10);
        scrollPane5.setViewportView(inputRequirements);
        final JScrollPane scrollPane6 = new JScrollPane();
        panel3.add(scrollPane6, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.FILL));
        inputFiles = new JTextArea();
        inputFiles.setRows(10);
        scrollPane6.setViewportView(inputFiles);
        final JSeparator separator1 = new JSeparator();
        panel1.add(separator1, cc.xyw(1, 11, 5, CellConstraints.FILL, CellConstraints.FILL));
        btnRegister = new JButton();
        btnRegister.setText("빌드노트 등록");
        panel1.add(btnRegister, cc.xyw(1, 21, 5));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow"));
        panel1.add(panel4, cc.xy(3, 19, CellConstraints.DEFAULT, CellConstraints.FILL));
        warRadioButton = new JRadioButton();
        warRadioButton.setSelected(true);
        warRadioButton.setText("War");
        panel4.add(warRadioButton, cc.xy(1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        jarRadioButton = new JRadioButton();
        jarRadioButton.setText("Jar");
        panel4.add(jarRadioButton, cc.xy(3, 1));
        final JLabel label14 = new JLabel();
        label14.setText("배포유형");
        panel1.add(label14, cc.xy(1, 19, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label15 = new JLabel();
        label15.setText("개발기간");
        panel1.add(label15, cc.xy(1, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow"));
        panel1.add(panel5, cc.xy(3, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
        inputDevStartDate = new JTextField();
        inputDevStartDate.setToolTipText("YYYY-MM-DD");
        panel5.add(inputDevStartDate, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        inputDueDate = new JTextField();
        inputDueDate.setToolTipText("YYYY-MM-DD");
        panel5.add(inputDueDate, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label16 = new JLabel();
        label16.setHorizontalAlignment(0);
        label16.setHorizontalTextPosition(0);
        label16.setText(" ~ ");
        panel5.add(label16, cc.xy(3, 1));
        inputRedmineId.setNextFocusableComponent(inputRedminePw);
        inputRedmineTaskNo.setNextFocusableComponent(inputTaskRedmineTitle);
        inputTaskRedmineTitle.setNextFocusableComponent(inputDevStartDate);
        inputRedmineSqlNo.setNextFocusableComponent(inputSqlRedmineTitle);
        inputRequirements.setNextFocusableComponent(inputFiles);
        inputDevStartDate.setNextFocusableComponent(inputDueDate);
        inputDueDate.setNextFocusableComponent(inputQaDate);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(jarRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}

