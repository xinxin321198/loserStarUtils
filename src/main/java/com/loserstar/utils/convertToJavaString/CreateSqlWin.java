package com.loserstar.utils.convertToJavaString;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;


/**
 * 
 * author: loserStar
 * date: 2017年6月1日下午4:15:16
 * remarks: 格式化后的sql转Java字符串工具
 */
@SuppressWarnings("serial")  
public class CreateSqlWin extends JFrame {  
  
    private JPanel contentPane;  
    private JTextField txtStr;  
    private JRadioButton rdbtnString;  
    private JRadioButton rdbtnStringbuffer;  
    private JSplitPane splitPane;  
    private JTextArea newSql;  
    private JTextArea oldSql;  
      
  
    /** 
     * Launch the application. 
     */  
    public static void main(String[] args) {  
        EventQueue.invokeLater(new Runnable() {  
            public void run() {  
                try {  
                    CreateSqlWin frame = new CreateSqlWin();  
                    frame.setVisible(true);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        });  
    }  
  
    /** 
     * Create the frame. 
     */  
    public CreateSqlWin() {  
//        setIconImage(ico.getImage());  
        setMinimumSize(new Dimension(840, 600));  
        setTitle("SQL转JAVA字符串");  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        setBounds(100, 100, 842, 605);  
        contentPane = new JPanel();  
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));  
        setContentPane(contentPane);  
        contentPane.setLayout(new BorderLayout(0, 0));  
          
        JPanel panel = new JPanel();  
        panel.setPreferredSize(new Dimension(10, 80));  
        contentPane.add(panel, BorderLayout.NORTH);  
        panel.setLayout(new BorderLayout(0, 0));  
          
        JPanel panel_1 = new JPanel();  
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));  
        panel_1.setPreferredSize(new Dimension(300, 10));  
        panel.add(panel_1, BorderLayout.CENTER);  
        panel_1.setLayout(null);  
          
        JLabel label = new JLabel("选择生成方式：");  
        label.setBounds(10, 10, 153, 20);  
        panel_1.add(label);  
          
        rdbtnString = new JRadioButton("String");  
        rdbtnString.setSelected(true);  
        rdbtnString.setBounds(52, 36, 79, 23);  
        panel_1.add(rdbtnString);  
          
        rdbtnStringbuffer = new JRadioButton("StringBuffer");  
        rdbtnStringbuffer.setBounds(144, 36, 107, 23);  
        panel_1.add(rdbtnStringbuffer);  
          
        ButtonGroup bGroup = new ButtonGroup();  
        bGroup.add(rdbtnString);  
        bGroup.add(rdbtnStringbuffer);  
          
        txtStr = new JTextField();  
        txtStr.setText("str");  
        txtStr.setBounds(313, 31, 180, 33);  
        panel_1.add(txtStr);  
        txtStr.setColumns(10);  
          
        JLabel label_1 = new JLabel("输入变量名：");  
        label_1.setBounds(276, 13, 87, 15);  
        panel_1.add(label_1);  
          
        JPanel panel_3 = new JPanel();  
        panel_3.setBorder(new MatteBorder(1, 0, 1, 1, (Color) new Color(0, 0, 0)));  
        panel_3.setPreferredSize(new Dimension(200, 10));  
        panel.add(panel_3, BorderLayout.EAST);  
        panel_3.setLayout(new BorderLayout(0, 0));  
          
        JButton button = new JButton("生成");  
        button.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                //生成SQL  
                String oldSqlStr = oldSql.getText();  
                if(oldSqlStr.equals("")){  
                    JOptionPane.showMessageDialog(CreateSqlWin.this, "请在左侧输入SQL再执行！");  
                    return;  
                }  
                //清空  
                if(!newSql.getText().equals("")){  
                    newSql.setText("");  
                }  
                String valibleName = txtStr.getText();  
                if(valibleName.trim().equals("")){  
                    JOptionPane.showMessageDialog(CreateSqlWin.this, "请输入变量名！");  
                    return;  
                }  
                String[] sqls = oldSqlStr.split("\n");  
                StringBuffer result = new StringBuffer();  
                //对SQL进行拼接  
                if(rdbtnString.isSelected()){  
                    //string形式  
                    for(int i=0;i<sqls.length-1;i++){  
                        if(result.toString().equals("")){  
                            result.append(valibleName+" = \" "+sqls[i]+" \"\n");  
                        }  
                        else {  
                            result.append(" +\" "+sqls[i]+" \"\n");  
                        }  
                    }  
                    result.append(" +\" "+sqls[sqls.length-1]+" \";\n");  
                }  
                else{  
                    //string形式  
                    for(int i=0;i<sqls.length;i++){  
                        result.append(valibleName+".append(\" "+sqls[i]+" \");\n");  
                    }  
                }  
                newSql.setText(result.toString());  
            }  
        });  
        button.setFont(new Font("楷体", Font.PLAIN, 32));  
        panel_3.add(button, BorderLayout.CENTER);  
          
        JPanel panel_2 = new JPanel();  
        panel_2.setBorder(new MatteBorder(0, 1, 1, 1, (Color) new Color(0, 0, 0)));  
        contentPane.add(panel_2, BorderLayout.CENTER);  
        panel_2.setLayout(new BorderLayout(0, 0));  
          
        splitPane = new JSplitPane();  
        splitPane.addComponentListener(new ComponentAdapter() {  
            @Override  
            public void componentResized(ComponentEvent e) {  
                divider();  
            }  
        });  
        panel_2.add(splitPane, BorderLayout.CENTER);  
          
        JScrollPane scrollPane = new JScrollPane();  
        splitPane.setLeftComponent(scrollPane);  
          
        oldSql = new JTextArea();  
        scrollPane.setViewportView(oldSql);  
          
        JScrollPane scrollPane_1 = new JScrollPane();  
        splitPane.setRightComponent(scrollPane_1);  
          
        newSql = new JTextArea();  
        scrollPane_1.setViewportView(newSql);  
          
        JPanel panel_4 = new JPanel();  
        FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();  
        flowLayout.setAlignment(FlowLayout.LEFT);  
        panel_4.setPreferredSize(new Dimension(10, 30));  
        panel_2.add(panel_4, BorderLayout.NORTH);  
          
        JLabel lblsql = new JLabel("请在左侧输入你要格式化的SQL语句：");  
        lblsql.setHorizontalAlignment(SwingConstants.LEFT);  
        panel_4.add(lblsql);  
    }  
      
    public void divider(){  
        splitPane.setDividerLocation(0.4);  
    }  
}  