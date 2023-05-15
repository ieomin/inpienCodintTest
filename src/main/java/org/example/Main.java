package org.example;

import org.example.entity.InspienXmldataInfo;
import org.example.service.DbConnector;
import org.example.service.FtpConnector;
import org.example.service.Parser;
import org.example.service.Separator;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        // INPUT DATA
        String url = "http://211.106.171.36:50000/RESTAdapter/RecruitingTest";
        String name = "이범진";
        String phoneNumber = "010-2676-5735";
        String email = "dlqjawls98@naver.com";

        // PARSER DO
        String parsedString = Parser.getParsedString(url, name, phoneNumber, email);

        // OUTPUT DATA
        String xmlString = Separator.getXmlString(parsedString);
        String jsonString = Separator.getJsonString(parsedString);

        // DB INSERT
        List<InspienXmldataInfo> list = DbConnector.getList(xmlString);
        DbConnector.insert(list, name);

        // FTP INSERT
        String fileName = FtpConnector.getFile(jsonString, name);
        FtpConnector.insert(fileName);
    }
}