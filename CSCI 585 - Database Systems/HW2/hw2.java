import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import oracle.sql.STRUCT;
import oracle.spatial.geometry.JGeometry; 

public class hw2 extends javax.swing.JFrame {

    public hw2() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        this.con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:hw2", "system", "hw2");
        con.setAutoCommit(true);
        initComponents();
    if(jPanel1!=null)
    {
        g = jPanel1.getGraphics();
        g2D = (Graphics2D)g;
    }   
    }
    
    Connection con;  
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jFrame1 = new javax.swing.JFrame();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField3 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

		try{
        jFrame1.setTitle("Nidhi Prvaeen Jain 7249597229");

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nidhi Praveen Jain 7249597229");

        jCheckBox1.setText("Building");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("AS");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Student");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Whole Region");
        jRadioButton1.setName(""); // NOI18N
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jRadioButton1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jRadioButton1FocusLost(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Point Query");
        jRadioButton2.setName(""); // NOI18N
        jRadioButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButton2MouseClicked(evt);
            }
        });
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Range Query");
        jRadioButton3.setName(""); // NOI18N
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("Surrounding Student");
        jRadioButton4.setName(""); // NOI18N
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("Emergency Query");
        jRadioButton5.setName(""); // NOI18N
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });

        jButton1.setText("Submit Query");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Active Feature Type");

        jLabel3.setText("Query");

        jLabel4.setText("X-Coordinate");

        jLabel5.setText("Y-Cordinate");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("map.jpg"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
        });
        jLabel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel2MouseMoved(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(826, 826, 826)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox3)
                        .addGap(58, 58, 58))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                                    .addComponent(jTextField3))
                                .addGap(21, 21, 21))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton3)
                                    .addComponent(jRadioButton4)
                                    .addComponent(jRadioButton2)
                                    .addComponent(jRadioButton1)
                                    .addComponent(jRadioButton5))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 190, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(96, 96, 96)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton5)
                .addGap(88, 88, 88)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jButton1)
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 78, Short.MAX_VALUE)))
        );

        pack();
		}
		catch(Exception e)
		{}
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
        jPanel1.paint(jPanel1.getGraphics());
        surroundingStudentsQuery = 1;
        xPoints.clear();
        yPoints.clear();
        rangeQueryReady = 0;
        rangeQuery = 0;
        rangeQueryClick = 0;
        nPoints = 0;
        pointQueryReady = 0;
        pointQuery = 0;
        emergencyQueryReady = 0;
        emergencyQuery = 0;
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            if(jRadioButton1.isSelected())
            {
                jPanel1.paint(jPanel1.getGraphics());
                if(jCheckBox3.isSelected())
                    locateStudent();
                if(jCheckBox1.isSelected())
                    locateBuildings();
                if(jCheckBox2.isSelected())
                    locateAnnouncementSystems();
            }
            
            if(jRadioButton1.isSelected() || jRadioButton2.isSelected() || jRadioButton3.isSelected())
                if(!jCheckBox3.isSelected() && !jCheckBox1.isSelected() && !jCheckBox2.isSelected())
                    JOptionPane.showMessageDialog(null, "Please select atleast one active feature type");
            if(!jRadioButton1.isSelected() && !jRadioButton2.isSelected() && !jRadioButton3.isSelected() && !jRadioButton4.isSelected() && !jRadioButton5.isSelected())
                JOptionPane.showMessageDialog(null, "Please select atleast one query type");
            
            if(pointQueryReady == 1)
            {
                jPanel1.paint(jPanel1.getGraphics());
                g.setColor(Color.RED);
                g.fillRect((int)(pointQueryX-2.5), (int)(pointQueryY-2.5), 5, 5);
                g.drawOval((int)(pointQueryX - 50), (int)(pointQueryY - 50), 100, 100);
                if(jCheckBox3.isSelected())
                {
                String pointQueryStudent = "SELECT * FROM students s WHERE SDO_RELATE(sloc, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,4), mdsys.sdo_ordinate_array("+pointQueryX+","+(pointQueryY-50)+","+(pointQueryX+50)+","+pointQueryY+","+pointQueryX+","+(pointQueryY+50)+")), 'mask=ANYINTERACT') = 'TRUE'";
                queryStringCounter++;
                queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryStudent + ";\n";
                jTextArea1.setText(queryStringTextBox);
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(pointQueryStudent);
                double min = 1000000;
                double x = -1;
                double y = -1;
                while(rs.next())
                {
                    String sId = rs.getString("personId");
                    STRUCT st = (oracle.sql.STRUCT) rs.getObject("sLoc");
                    JGeometry geom = JGeometry.load(st);
                    double a[] = geom.getFirstPoint();
                    double distance = Math.sqrt((a[0]-pointQueryX)*(a[0]-pointQueryX) + (a[1]-pointQueryY)*(a[1]-pointQueryY));
                    if(distance<min)
                    {
                        min = distance;
                        x = a[0];
                        y = a[1];
                    }
                    if(g != null)
                    {
                        g.setColor(Color.GREEN);
                        g.fillRect((int)a[0] - 5, (int)a[1] - 5, 10, 10);
                    }
                }
                if(g != null)
                    {
                        g.setColor(Color.YELLOW);
                        g.fillRect((int)x - 5, (int)y - 5, 10, 10);
                    }
                }
                if(jCheckBox2.isSelected())
                {
                String pointQueryAS = "SELECT * FROM announcementSystems s WHERE SDO_RELATE(s.asArea, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,4), mdsys.sdo_ordinate_array("+pointQueryX+","+(pointQueryY-50)+","+(pointQueryX+50)+","+pointQueryY+","+pointQueryX+","+(pointQueryY+50)+")), 'mask=ANYINTERACT') = 'TRUE'";
                queryStringCounter++;
                queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryAS + ";\n";
                jTextArea1.setText(queryStringTextBox);
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(pointQueryAS);
                double min = 1000000;
                double x = -1;
                double y = -1;
                while(rs.next())
                {
                    STRUCT st = (oracle.sql.STRUCT) rs.getObject("asLoc");
                    JGeometry geom = JGeometry.load(st);
                    double a[] = geom.getFirstPoint();
                    int radius = Integer.parseInt(rs.getString("asRadius"));
                    if(g != null)
                    {
                        g.setColor(Color.GREEN);
                        g.fillRect((int)(a[0]-7.5), (int)(a[1]-7.5), 15, 15);
                        g.drawOval((int)(a[0] - radius), (int)(a[1] - radius), radius*2, radius*2);
                    }
                }
                String pointQueryAS1 = "SELECT * FROM announcementSystems s WHERE SDO_NN(s.asArea, mdsys.sdo_geometry(2001,NULL,SDO_POINT_TYPE(" + pointQueryX + "," + pointQueryY + ",NULL), NULL, NULL),'sdo_num_res=1') = 'TRUE' AND SDO_RELATE(s.asArea, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,4), mdsys.sdo_ordinate_array("+pointQueryX+","+(pointQueryY-50)+","+(pointQueryX+50)+","+pointQueryY+","+pointQueryX+","+(pointQueryY+50)+")), 'mask=ANYINTERACT') = 'TRUE'";
                queryStringCounter++;
                queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryAS1 + ";\n";
                jTextArea1.setText(queryStringTextBox);
                statement = con.createStatement();
                rs = statement.executeQuery(pointQueryAS1);
                while(rs.next())
                {
                    STRUCT st = (oracle.sql.STRUCT) rs.getObject("asLoc");
                    JGeometry geom = JGeometry.load(st);
                    double a[] = geom.getFirstPoint();
                    int radius = Integer.parseInt(rs.getString("asRadius"));
                    if(g != null)
                    {
                        g.setColor(Color.YELLOW);
                        g.fillRect((int)(a[0]-7.5), (int)(a[1]-7.5), 15, 15);
                        g.drawOval((int)(a[0] - radius), (int)(a[1] - radius), radius*2, radius*2);
                    }
                }
                }
                if(jCheckBox1.isSelected())
                {
                String pointQueryB = "SELECT * FROM buildings s WHERE SDO_RELATE(s.bloc, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,4), mdsys.sdo_ordinate_array("+pointQueryX+","+(pointQueryY-50)+","+(pointQueryX+50)+","+pointQueryY+","+pointQueryX+","+(pointQueryY+50)+")), 'mask=ANYINTERACT') = 'TRUE'";
                queryStringCounter++;
                queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryB + ";\n";
                jTextArea1.setText(queryStringTextBox);
                Statement statement = con.createStatement();
                ResultSet rs= statement.executeQuery(pointQueryB);
                //min = 1000000;
                double x = -1;
                double y = -1;
                while(rs.next())
                {
                    String bId = rs.getString("buildingId");
                    int nPoints = Integer.parseInt(rs.getString("noOfVertices"));
                    STRUCT st = (oracle.sql.STRUCT) rs.getObject("bLoc");
                    JGeometry geom = JGeometry.load(st);
                    double a[] = geom.getOrdinatesArray();
                    if(a!=null)
                    {
                        int x1[] = new int[nPoints];
                        int y1[] = new int[nPoints];
                        for(int i=0; i<nPoints*2;i++)
                            if(i%2==0)
                                x1[i/2] = (int)a[i];
                            else y1[i/2] = (int)a[i];
                        g.setColor(Color.GREEN);
                        g.drawPolygon(x1, y1, nPoints);
                    }
                }
                String pointQueryAS1 = "SELECT * FROM buildings s WHERE SDO_NN(s.bLoc, mdsys.sdo_geometry(2001,NULL,SDO_POINT_TYPE(" + pointQueryX + "," + pointQueryY + ",NULL), NULL, NULL),'sdo_num_res=1') = 'TRUE' AND SDO_RELATE(s.bloc, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,4), mdsys.sdo_ordinate_array("+pointQueryX+","+(pointQueryY-50)+","+(pointQueryX+50)+","+pointQueryY+","+pointQueryX+","+(pointQueryY+50)+")), 'mask=ANYINTERACT') = 'TRUE'";
                queryStringCounter++;
                queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryAS1 + ";\n";
                jTextArea1.setText(queryStringTextBox);
                statement = con.createStatement();
                rs = statement.executeQuery(pointQueryAS1);
                while(rs.next())
                {
                    String bId = rs.getString("buildingId");
                    int nPoints = Integer.parseInt(rs.getString("noOfVertices"));
                    STRUCT st = (oracle.sql.STRUCT) rs.getObject("bLoc");
                    JGeometry geom = JGeometry.load(st);
                    double a[] = geom.getOrdinatesArray();
                    if(a!=null)
                    {
                        int x1[] = new int[nPoints];
                        int y1[] = new int[nPoints];
                        for(int i=0; i<nPoints*2;i++)
                            if(i%2==0)
                                x1[i/2] = (int)a[i];
                            else y1[i/2] = (int)a[i];
                        g.setColor(Color.YELLOW);
                        g.drawPolygon(x1, y1, nPoints);
                    }
                }
                }
            }
            
            if(surroundingStudentsQueryReady == 1)
            {
                String pointQueryStudent = "SELECT * FROM students s WHERE SDO_RELATE(sloc, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,4), mdsys.sdo_ordinate_array("+SSPassX+","+(SSPassY-SSRadiusPass)+","+(SSPassX+SSRadiusPass)+","+SSPassY+","+SSPassX+","+(SSPassY+SSRadiusPass)+")), 'mask=ANYINTERACT') = 'TRUE'";
                queryStringCounter++;
                queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryStudent + ";\n";
                jTextArea1.setText(queryStringTextBox);
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(pointQueryStudent);
                while(rs.next())
                {
                    String sId = rs.getString("personId");
                    STRUCT st = (oracle.sql.STRUCT) rs.getObject("sLoc");
                    JGeometry geom = JGeometry.load(st);
                    double a[] = geom.getFirstPoint();
                    if(g != null)
                    {
                        g.setColor(Color.GREEN);
                        g.fillRect((int)a[0] - 5, (int)a[1] - 5, 10, 10);
                    }
                }
            }
            if(emergencyQueryReady == 1)
            {
                String pointQueryStudent = "SELECT * FROM students s WHERE SDO_RELATE(sloc, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,4), mdsys.sdo_ordinate_array("+EPassX+","+(EPassY-ERadiusPass)+","+(EPassX+ERadiusPass)+","+EPassY+","+EPassX+","+(EPassY+ERadiusPass)+")), 'mask=ANYINTERACT') = 'TRUE'";
                queryStringCounter++;
                queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryStudent + ";\n";
                jTextArea1.setText(queryStringTextBox);
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(pointQueryStudent);
                Random r = new Random();
                Map<String,Color> colorMap = new HashMap();
                while(rs.next())
                {
                    String sId = rs.getString("personId");
                    STRUCT st = (oracle.sql.STRUCT) rs.getObject("sLoc");
                    JGeometry geom = JGeometry.load(st);
                    double a[] = geom.getFirstPoint();
                    Color  c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
                    String pointQueryAS = "SELECT * FROM announcementSystems where asId != '" + emergencyQueryASId + "' and SDO_NN(asArea, mdsys.sdo_geometry(2001,NULL,SDO_POINT_TYPE(" + a[0] + "," + a[1] + ",NULL), NULL, NULL),'sdo_num_res=2') = 'TRUE'";
                    try
                    {
                        Statement statement1 = con.createStatement();
                        ResultSet rs1 = statement1.executeQuery(pointQueryAS);
                        while(rs1.next())
                        {
                            String asId = rs1.getString("asId");
                            if(!colorMap.containsKey(asId))
                            {
                                colorMap.put(asId,c);
                                g.setColor(c);
                                g.fillRect((int)a[0] - 5, (int)a[1] - 5, 10, 10);
                            }
                            else
                            {
                                Color c1 = colorMap.get(asId);
                                g.setColor(c1);
                                g.fillRect((int)a[0] - 5, (int)a[1] - 5, 10, 10);
                            }
                            STRUCT st1 = (oracle.sql.STRUCT) rs1.getObject("asLoc");
                            JGeometry geom1 = JGeometry.load(st1);
                            double a1[] = geom1.getFirstPoint();
                            int radius1 = Integer.parseInt(rs1.getString("asRadius"));
                            if(g != null)
                            {
                                g.fillRect((int)(a1[0]-7.5), (int)(a1[1]-7.5), 15, 15);
                                g.drawOval((int)(a1[0] - radius1), (int)(a1[1] - radius1), radius1*2, radius1*2);
                            }
                        }
                    }
                    catch(SQLException e)
                    {
                        
                    }
                }
            }
            if(rangeQueryReady == 1)
            {
                jPanel1.paint(jPanel1.getGraphics());
                int[] x = new int[xPoints.size()];
                for(int i = 0;i < x.length;i++)
                  x[i] = xPoints.get(i);
                int[] y = new int[yPoints.size()];
                for(int i = 0;i < y.length;i++)
                  y[i] = yPoints.get(i);
                String rangeQueryArray = "";
                for(int i = 0; i<xPoints.size();i++)
                    rangeQueryArray += x[i] + "," + y[i] + ", ";
                rangeQueryArray += x[0] + "," + y[0];
                g.setColor(Color.red);
                g.drawPolygon(x, y, xPoints.size());
                if(jCheckBox3.isSelected())
                {
                    String pointQueryStudent = "SELECT * FROM students WHERE SDO_RELATE(sloc, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,1), mdsys.sdo_ordinate_array("+rangeQueryArray+")), 'mask=ANYINTERACT') = 'TRUE'";
                    queryStringCounter++;
                    queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryStudent + ";\n";
                    jTextArea1.setText(queryStringTextBox);
                    Statement statement = con.createStatement();
                    ResultSet rs = statement.executeQuery(pointQueryStudent);
                    while(rs.next())
                    {
                        String sId = rs.getString("personId");
                        STRUCT st = (oracle.sql.STRUCT) rs.getObject("sLoc");
                        JGeometry geom = JGeometry.load(st);
                        double a[] = geom.getFirstPoint();
                        if(g != null)
                        {
                            g.setColor(Color.GREEN);
                            g.fillRect((int)a[0]-5, (int)a[1]-5, 10, 10);
                        }
                    }
                }
                if(jCheckBox2.isSelected())
                {
                    String selectS = "SELECT * FROM announcementSystems WHERE SDO_RELATE(asArea, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,1), mdsys.sdo_ordinate_array("+rangeQueryArray+")), 'mask=ANYINTERACT') = 'TRUE'";
                    queryStringCounter++;
                    queryStringTextBox += "Query " + queryStringCounter + ": " + selectS + ";\n";
                    jTextArea1.setText(queryStringTextBox);
                    PreparedStatement stmt = con.prepareStatement(selectS);
                    ResultSet rs = stmt.executeQuery(selectS);
                    while(rs.next())
                    {
                        String sId = rs.getString("asId");
                        int radius = Integer.parseInt(rs.getString("asRadius"));
                        STRUCT st = (oracle.sql.STRUCT) rs.getObject("asLoc");
                        JGeometry geom = JGeometry.load(st);
                        double a[] = geom.getFirstPoint();
                        if(g != null)
                        {
                            g.setColor(Color.RED);
                            g.fillRect((int)(a[0]-7.5), (int)(a[1]-7.5), 15, 15);
                            g.drawOval((int)(a[0] - radius), (int)(a[1] - radius), radius*2, radius*2);
                        }
                    }
                } 
                if(jCheckBox1.isSelected())
                {
                    String selectS = "SELECT * FROM buildings WHERE SDO_RELATE(bLoc, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,1), mdsys.sdo_ordinate_array("+rangeQueryArray+")), 'mask=ANYINTERACT') = 'TRUE'";
                    queryStringCounter++;
                    queryStringTextBox += "Query " + queryStringCounter + ": " + selectS + ";\n";
                    jTextArea1.setText(queryStringTextBox);
                    PreparedStatement stmt = con.prepareStatement(selectS);
                    ResultSet rs = stmt.executeQuery(selectS);
                    while(rs.next())
                    {
                        String bId = rs.getString("buildingId");
                        int nPoints = Integer.parseInt(rs.getString("noOfVertices"));
                        STRUCT st = (oracle.sql.STRUCT) rs.getObject("bLoc");
                        JGeometry geom = JGeometry.load(st);
                        double a[] = geom.getOrdinatesArray();
                        if(a!=null)
                        {
                            int x1[] = new int[nPoints];
                            int y1[] = new int[nPoints];
                            for(int i=0; i<nPoints*2;i++)
                                if(i%2==0)
                                    x1[i/2] = (int)a[i];
                                else y1[i/2] = (int)a[i];
                            g.setColor(Color.YELLOW);
                            g.drawPolygon(x1, y1, nPoints);
                        }
                     }
                } 
            }
        } catch (SQLException ex) {
            Logger.getLogger(hw2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        jPanel1.paint(jPanel1.getGraphics());
        xPoints.clear();
        yPoints.clear();
        rangeQueryReady = 0;
        rangeQuery = 0;
        rangeQueryClick = 0;
        nPoints = 0;
        surroundingStudentsQueryReady = 0;
        surroundingStudentsQuery = 0;
        emergencyQueryReady = 0;
        emergencyQuery = 0;
        pointQuery = 0;
        pointQueryReady = 0;
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        jPanel1.paint(jPanel1.getGraphics());
        pointQuery = 1;
        xPoints.clear();
        yPoints.clear();
        rangeQueryReady = 0;
        rangeQuery = 0;
        rangeQueryClick = 0;
        nPoints = 0;
        surroundingStudentsQueryReady = 0;
        surroundingStudentsQuery = 0;
        emergencyQueryReady = 0;
        emergencyQuery = 0;
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        jPanel1.paint(jPanel1.getGraphics());
        rangeQuery = 1;
        emergencyQueryReady = 0;
        emergencyQuery = 0;
        pointQueryReady = 0;
        pointQuery = 0;
        surroundingStudentsQueryReady = 0;
        surroundingStudentsQuery = 0;
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // TODO add your handling code here:
        jPanel1.paint(jPanel1.getGraphics());
        emergencyQuery = 1;
        xPoints.clear();
        yPoints.clear();
        rangeQueryReady = 0;
        rangeQuery = 0;
        rangeQueryClick = 0;
        nPoints = 0;
        pointQueryReady = 0;
        pointQuery = 0;
        surroundingStudentsQueryReady = 0;
        surroundingStudentsQuery = 0; 
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jLabel2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseMoved
        // TODO add your handling code here:
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen( mouseLocation , jLabel2 );
        int x = (int)mouseLocation.getX();
        int y = (int)mouseLocation.getY();
        jTextField3.setText(Integer.toString(x));
        jTextField4.setText(Integer.toString(y));
    }//GEN-LAST:event_jLabel2MouseMoved

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jRadioButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton2MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jRadioButton2MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        //jPanel1.paint(jPanel1.getGraphics());
        if(pointQuery == 1)
        {
            jPanel1.paint(jPanel1.getGraphics());
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen( mouseLocation , jLabel2 );
            pointQueryX = (int)mouseLocation.getX();
            pointQueryY = (int)mouseLocation.getY();
            g.setColor(Color.RED);
            g.fillRect((int)(pointQueryX-2.5), (int)(pointQueryY-2.5), 5, 5);
            g.drawOval((int)(pointQueryX - 50), (int)(pointQueryY - 50), 100, 100);
            pointQueryReady = 1;
        }
        if(surroundingStudentsQuery == 1)
        {
            jPanel1.paint(jPanel1.getGraphics());
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen( mouseLocation , jLabel2 );
            surroundingStudentsQueryX = (int)mouseLocation.getX();
            surroundingStudentsQueryY = (int)mouseLocation.getY();
            g.setColor(Color.RED);
            g.fillRect((int)(surroundingStudentsQueryX-2.5), (int)(surroundingStudentsQueryY-2.5), 5, 5);
            String pointQueryAS = "SELECT * FROM announcementSystems where SDO_NN(asArea, mdsys.sdo_geometry(2003,NULL,NULL, mdsys.sdo_elem_info_array(1,1003,4), mdsys.sdo_ordinate_array("+surroundingStudentsQueryX+","+(surroundingStudentsQueryY-50)+","+(surroundingStudentsQueryX+50)+","+surroundingStudentsQueryY+","+surroundingStudentsQueryX+","+(surroundingStudentsQueryY+50)+")),'sdo_num_res=1') = 'TRUE'";
            queryStringCounter++;
            queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryAS + ";\n";
            jTextArea1.setText(queryStringTextBox);
            try
            {
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(pointQueryAS);
                double min = 1000000;
                while(rs.next())
                {
                    String id = rs.getString("asId");
                    SSRadiusPass = Integer.parseInt(rs.getString("asRadius"));
                    STRUCT st = (oracle.sql.STRUCT) rs.getObject("asLoc");
                    JGeometry geom = JGeometry.load(st);
                    double a[] = geom.getFirstPoint();
                    SSPassX = (int)a[0];
                    SSPassY = (int)a[1];
                    if(g != null)
                    {
                        g.setColor(Color.RED);
                        g.fillRect((int)(a[0]-7.5), (int)(a[1]-7.5), 15, 15);
                        g.drawOval((int)(a[0] - SSRadiusPass), (int)(a[1] - SSRadiusPass), (int)SSRadiusPass*2, (int)SSRadiusPass*2);
                    }
                }
            }
            catch(SQLException e)
            {
            }
            surroundingStudentsQueryReady = 1;
        }
        if(emergencyQuery == 1)
        {
            jPanel1.paint(jPanel1.getGraphics());
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen( mouseLocation , jLabel2 );
            emergencyQueryX = (int)mouseLocation.getX();
            emergencyQueryY = (int)mouseLocation.getY();
            g.setColor(Color.RED);
            g.fillRect((int)(emergencyQueryX-2.5), (int)(emergencyQueryY-2.5), 5, 5);
            mouseLocation = MouseInfo.getPointerInfo().getLocation();
            String pointQueryAS = "SELECT * FROM announcementSystems where SDO_NN(asArea, mdsys.sdo_geometry(2001,NULL,SDO_POINT_TYPE(" + emergencyQueryX + "," + emergencyQueryY + ",NULL), NULL, NULL),'sdo_num_res=1') = 'TRUE'";
            queryStringCounter++;
            queryStringTextBox += "Query " + queryStringCounter + ": " + pointQueryAS + ";\n";
            jTextArea1.setText(queryStringTextBox);
            try
            {
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(pointQueryAS);
                double min = 1000000;
                while(rs.next())
                {
                    emergencyQueryASId = rs.getString("asId");
                    ERadiusPass = Integer.parseInt(rs.getString("asRadius"));
                    STRUCT st = (oracle.sql.STRUCT) rs.getObject("asLoc");
                    JGeometry geom = JGeometry.load(st);
                    double a[] = geom.getFirstPoint();
                    EPassX = (int)a[0];
                    EPassY = (int)a[1];
                    if(g != null)
                    {
                        g.setColor(Color.RED);
                        g.fillRect((int)(a[0]-7.5), (int)(a[1]-7.5), 15, 15);
                        g.drawOval((int)(a[0] - ERadiusPass), (int)(a[1] - ERadiusPass), (int)ERadiusPass*2, (int)ERadiusPass*2);
                    }
                }
            }
            catch(SQLException e)
            {
			}
            emergencyQueryReady = 1;
        }
        
        if(rangeQuery == 1 && rangeQueryClick == 0 && SwingUtilities.isLeftMouseButton(evt))
        {
            jPanel1.paint(jPanel1.getGraphics());
            rangeQueryClick++;
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen( mouseLocation , jLabel2 );
            rangeQueryX = (int)mouseLocation.getX();
            rangeQueryY = (int)mouseLocation.getY();
            xPoints.add(rangeQueryX);
            yPoints.add(rangeQueryY);
            nPoints++;
            return;
        }
        if(rangeQuery == 1 && rangeQueryClick >= 1)
        {
            if(SwingUtilities.isRightMouseButton(evt))
            {
                Object x[] = xPoints.toArray();
                Object y[] = yPoints.toArray();
                g.setColor(Color.RED);
                g.drawLine((Integer)x[nPoints-1], (Integer)y[nPoints-1], (Integer)x[0], (Integer)y[0]);
                rangeQueryReady = 1;
                rangeQuery = 0;
                return;
            }
            rangeQueryClick++;
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen( mouseLocation , jLabel2 );
            rangeQueryX1 = (int)mouseLocation.getX();
            rangeQueryY1 = (int)mouseLocation.getY();
            g.setColor(Color.RED);
            g.drawLine(rangeQueryX, rangeQueryY, rangeQueryX1, rangeQueryY1);
            rangeQueryX = rangeQueryX1;
            rangeQueryY = rangeQueryY1;
            xPoints.add(rangeQueryX);
            yPoints.add(rangeQueryY);
            nPoints++;
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jRadioButton1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jRadioButton1FocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jRadioButton1FocusLost

    public static void main(String args[]) throws SQLException {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new hw2().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(hw2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        String url = new String();
        url = "jdbc:oracle:thin:@localhost:1521:xe";
        String userId = "nidhi";
        String password = "nidhi";
        Connection con = DriverManager.getConnection(url, userId, password);
        con.setAutoCommit(true);
    }
    
    public void locateStudent() throws SQLException
    {
        String selectS = "select * from students";
        queryStringCounter++;
        queryStringTextBox += "Query " + queryStringCounter + ": " + selectS + ";\n";
        jTextArea1.setText(queryStringTextBox);    
        PreparedStatement stmt = con.prepareStatement(selectS);
        ResultSet rs = stmt.executeQuery(selectS);
        while(rs.next())
        {
            String sId = rs.getString("personId");
            STRUCT st = (oracle.sql.STRUCT) rs.getObject("sLoc");
            JGeometry geom = JGeometry.load(st);
            double a[] = geom.getFirstPoint();
            if(g != null)
            {
                g.setColor(Color.GREEN);
                g.fillRect((int)a[0]-5, (int)a[1]-5, 10, 10);
            }
        }
    }
    
    public void locateBuildings() throws SQLException
    {
        String selectS = "select * from buildings";
        queryStringCounter++;
        queryStringTextBox += "Query " + queryStringCounter + ": " + selectS + ";\n";
        jTextArea1.setText(queryStringTextBox);    
        PreparedStatement stmt = con.prepareStatement(selectS);
        ResultSet rs = stmt.executeQuery(selectS);
        while(rs.next())
        {
            String bId = rs.getString("buildingId");
            int nPoints = Integer.parseInt(rs.getString("noOfVertices"));
            STRUCT st = (oracle.sql.STRUCT) rs.getObject("bLoc");
            JGeometry geom = JGeometry.load(st);
            double a[] = geom.getOrdinatesArray();
            if(a!=null)
            {
                int x[] = new int[nPoints];
                int y[] = new int[nPoints];
                for(int i=0; i<nPoints*2;i++)
                    if(i%2==0)
                        x[i/2] = (int)a[i];
                    else y[i/2] = (int)a[i];
                g.setColor(Color.YELLOW);
                g.drawPolygon(x, y, nPoints);
            }
         }
    }
    
    public void locateAnnouncementSystems() throws SQLException
    {
        String selectS = "select * from announcementSystems";
        queryStringCounter++;
        queryStringTextBox += "Query " + queryStringCounter + ": " + selectS + ";\n";
        jTextArea1.setText(queryStringTextBox);    
        PreparedStatement stmt = con.prepareStatement(selectS);
        ResultSet rs = stmt.executeQuery(selectS);
        while(rs.next())
        {
            String sId = rs.getString("asId");
            int radius = Integer.parseInt(rs.getString("asRadius"));
            STRUCT st = (oracle.sql.STRUCT) rs.getObject("asLoc");
            JGeometry geom = JGeometry.load(st);
            double a[] = geom.getFirstPoint();
            if(g != null)
            {
                g.setColor(Color.RED);
                g.fillRect((int)(a[0]-7.5), (int)(a[1]-7.5), 15, 15);
                g.drawOval((int)(a[0] - radius), (int)(a[1] - radius), radius*2, radius*2);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
    private Graphics g = null;
    private Graphics2D g2D = null;
    int pointQuery = 0;
    int pointQueryClick = 0;
    int pointQueryReady = 0;
    int pointQueryX = -1;
    int pointQueryY = -1;
    int surroundingStudentsQuery = 0;
    int surroundingStudentsQueryClick = 0;
    int surroundingStudentsQueryReady = 0;
    int surroundingStudentsQueryX = -1;
    int surroundingStudentsQueryY = -1;
    int surroundingSystemASX = -1;
    int surroundingSystemASY = -1;
    int SSPassX = -1;
    int SSPassY = -1;
    double SSRadiusPass = -1;
    double rad = -1;
    int rangeQuery = 0;
    int rangeQueryClick = 0;
    int rangeQueryReady = 0;
    int rangeQueryX = -1;
    int rangeQueryY = -1;
    int rangeQueryX1 = -1;
    int rangeQueryY1 = -1;
    List<Integer> xPoints = new ArrayList<Integer>();
    List<Integer> yPoints = new ArrayList<Integer>();
    int nPoints = 0;
    int emergencyQuery = 0;
    int emergencyQueryClick = 0;
    int emergencyQueryReady = 0;
    int emergencyQueryX = -1;
    int emergencyQueryY = -1;
    double rad1 = -1;
    String emergencyQueryASId = null;
    int EPassX = -1;
    int EPassY = -1;
    double ERadiusPass = -1;
    String queryStringTextBox = "";
    int queryStringCounter = 0;
}
