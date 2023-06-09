package org.example.service;

import org.example.entity.InspienXmldataInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbConnector {

    private static final String HOST = "211.106.171.36";
    private static final String PORT = "11527";
    private static final String SID = "POS";
    private static final String USER = "inspien01";
    private static final String PASSWORD = "inspien01";
    private static final String TABLENAME = "INSPIEN_XMLDATA_INFO";

    public static List<InspienXmldataInfo> getList(String xmlString) {
        List<InspienXmldataInfo> list = new ArrayList<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xmlString))); doc.getDocumentElement().normalize();
            NodeList headerList = doc.getElementsByTagName("HEADER");
            NodeList detailList = doc.getElementsByTagName("DETAIL");
            Map<String, Element> headerMap = new HashMap<>();
            for (int i = 0; i < headerList.getLength(); i++) {
                Element header = (Element) headerList.item(i);
                String orderNum = header.getElementsByTagName("ORDER_NUM").item(0).getTextContent();
                headerMap.put(orderNum, header);
            }
            for (int i = 0; i < detailList.getLength(); i++) {
                Element detail = (Element) detailList.item(i);
                String orderNum = detail.getElementsByTagName("ORDER_NUM").item(0).getTextContent();
                if (headerMap.containsKey(orderNum)) {
                    Element header = headerMap.get(orderNum);
                    InspienXmldataInfo inspienXmldataInfo = new InspienXmldataInfo();
                    inspienXmldataInfo.setOrderNum(orderNum);
                    inspienXmldataInfo.setOrderId(header.getElementsByTagName("ORDER_ID").item(0).getTextContent());
                    inspienXmldataInfo.setOrderDate(header.getElementsByTagName("ORDER_DATE").item(0).getTextContent());
                    inspienXmldataInfo.setOrderPrice(Integer.parseInt(header.getElementsByTagName("ORDER_PRICE").item(0).getTextContent()));
                    inspienXmldataInfo.setOrderQty(Integer.parseInt(header.getElementsByTagName("ORDER_QTY").item(0).getTextContent()));
                    inspienXmldataInfo.setReceiverName(header.getElementsByTagName("RECEIVER_NAME").item(0).getTextContent());
                    inspienXmldataInfo.setReceiverNo(header.getElementsByTagName("RECEIVER_NO").item(0).getTextContent());
                    inspienXmldataInfo.setEtaDate(header.getElementsByTagName("ETA_DATE").item(0).getTextContent());
                    inspienXmldataInfo.setDestination(header.getElementsByTagName("DESTINATION").item(0).getTextContent());
                    inspienXmldataInfo.setDescription(header.getElementsByTagName("DESCIPTION").item(0).getTextContent());
                    inspienXmldataInfo.setItemSeq(Integer.parseInt(detail.getElementsByTagName("ITEM_SEQ").item(0).getTextContent()));
                    inspienXmldataInfo.setItemName(detail.getElementsByTagName("ITEM_NAME").item(0).getTextContent());
                    inspienXmldataInfo.setItemQty(Integer.parseInt(detail.getElementsByTagName("ITEM_QTY").item(0).getTextContent()));
                    inspienXmldataInfo.setItemColor(detail.getElementsByTagName("ITEM_COLOR").item(0).getTextContent());
                    inspienXmldataInfo.setItemPrice(Integer.parseInt(detail.getElementsByTagName("ITEM_PRICE").item(0).getTextContent()));
                    list.add(inspienXmldataInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insert(List<InspienXmldataInfo> list, String name) throws SQLException {
        String URL = "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SID;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO " + TABLENAME +  " (" +
                        "  ORDER_NUM, ITEM_SEQ, ORDER_ID, ORDER_DATE, ORDER_PRICE, ORDER_QTY, RECEIVER_NAME, " +
                        "  RECEIVER_NO, ETA_DATE, DESTINATION, DESCIPTION, ITEM_NAME, ITEM_QTY, ITEM_COLOR, " +
                        "  ITEM_PRICE, SENDER, CURRENT_DT" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)"
        );
        for (InspienXmldataInfo data : list) {
            pstmt.setString(1, data.getOrderNum());
            pstmt.setString(2, String.valueOf(data.getItemSeq()));
            pstmt.setString(3, data.getOrderId());
            pstmt.setString(4, data.getOrderDate());
            pstmt.setDouble(5, data.getOrderPrice());
            pstmt.setInt(6, data.getOrderQty());
            pstmt.setString(7, data.getReceiverName());
            pstmt.setString(8, data.getReceiverNo());
            pstmt.setString(9, data.getEtaDate());
            pstmt.setString(10, data.getDestination());
            pstmt.setString(11, data.getDescription());
            pstmt.setString(12, data.getItemName());
            pstmt.setInt(13, data.getItemQty());
            pstmt.setString(14, data.getItemColor());
            pstmt.setDouble(15, data.getItemPrice());
            pstmt.setString(16, name);
            pstmt.addBatch();
        }
        pstmt.executeBatch();
    }
}
