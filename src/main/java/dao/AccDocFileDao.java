package dao;

import domain.accident.accDocFile.AccDocFile;
import domain.accident.accDocFile.AccDocFileList;
import domain.accident.accDocFile.AccDocType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName :  dao
 * fileName : AccDocFileDao
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
public class AccDocFileDao extends Dao implements AccDocFileList {

    public AccDocFileDao() {
        super.connect();
    }

    @Override
    public void create(AccDocFile accDocFile) {
        String query = "insert into acc_doc_file (type, file_address, accident_id, last_modified_date)" +
                "values('%s', '%s', %d, '%s')";
        LocalDateTime now = LocalDateTime.now();
        String formattedQuery = String.format(query,accDocFile.getType().name(),
                accDocFile.getFileAddress(),accDocFile.getAccidentId(), now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        int id = super.create(formattedQuery);
        accDocFile.setId(id);
        accDocFile.setLastModifedDate(now);
        close();
    }

    @Override
    public AccDocFile read(int id) {
        String query = "select * from acc_doc_file where acc_doc_file_id = %d";
        String formattedQuery = String.format(query,id);
        ResultSet rs = super.read(formattedQuery);
        AccDocFile accDocFile = null;
        try {
            if (rs.next()) {
                accDocFile = new AccDocFile();
                accDocFile.setId(rs.getInt("acc_doc_file_id"))
                        .setFileAddress(rs.getString("file_address"))
                        .setAccidentId(rs.getInt("accident_id"))
                        .setType(AccDocType.valueOf(rs.getString("type").toUpperCase()))
                        .setLastModifedDate(rs.getTimestamp("last_modified_date").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        if(accDocFile==null)
            throw new IllegalArgumentException("사고 아이디 ["+id+"]에 해당하는 사고 파일 정보가 존재하지 않습니다.");
        return accDocFile;
    }




    @Override
    public boolean update(int id) {
        String query = "update acc_doc_file set last_modified_date = '%s' where acc_doc_file_id = %d";
        String formattedQuery = String.format(query, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),id);
        super.update(formattedQuery);
        close();
        return true;
    }

    @Override
    public List<AccDocFile> readAllByAccidentId(int accidentId) {
        String query = "select * from acc_doc_file where accident_id = %d";
        String formattedQuery = String.format(query,accidentId);
        ResultSet rs = super.read(formattedQuery);
        List<AccDocFile> accDocFileList = new ArrayList<>();

            try {
                while(rs.next()) {
                    AccDocFile accDocFile = new AccDocFile();
                    accDocFile.setId(rs.getInt("acc_doc_file_id"))
                            .setFileAddress(rs.getString("file_address"))
                            .setAccidentId(rs.getInt("accident_id"))
                            .setType(AccDocType.valueOf(rs.getString("type").toUpperCase()))
                            .setLastModifedDate(rs.getTimestamp("last_modified_date").toLocalDateTime());
                    accDocFileList.add(accDocFile);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {

                close();
            }
        if(accDocFileList.size()==0)
            throw new IllegalArgumentException("사고 아이디 ["+accidentId+"]에 해당하는 사고 파일 정보가 존재하지 않습니다.");
        return accDocFileList;
    }


    @Override
    public boolean delete(int id) {
        return false;
    }
}
