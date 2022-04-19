package acp.db.service.impl.dbcp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.ISourceManagerEdit;
import acp.db.service.impl.dbcp.all.ManagerBaseDbcp;
import acp.db.service.impl.dbcp.all.ManagerUtilDbcp;
import acp.forms.dto.SourceDto;
import acp.utils.*;

public class SourceManagerEditDbcp extends ManagerBaseDbcp implements ISourceManagerEdit {
  private static Logger logger = LoggerFactory.getLogger(SourceManagerEditDbcp.class);

  private ManagerUtilDbcp mngUtil = new ManagerUtilDbcp();

  @Override
  public SourceDto select(Long objId) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select msss_id, msss_name");
    sbQuery.append("  from mss_source");
    sbQuery.append(" where msss_id=?");
    String strQuery = sbQuery.toString();
    // ------------------------------------------------------
    SourceDto sourceObj = null;
    try {
      Connection conn = dbConnect.getConnection();
      PreparedStatement ps = conn.prepareStatement(strQuery);
      ps.setLong(1, objId);
      ResultSet rsq = ps.executeQuery();
      if (rsq.next()) {
        String rsqName = rsq.getString("msss_name");
        // ---------------------
        sourceObj = new SourceDto();
        sourceObj.setId(objId);
        sourceObj.setName(rsqName);
        // ---------------------
      }
      rsq.close();
      ps.close();
      dbConnect.close(conn);
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      sourceObj = null;
    }
    // ------------------------------------------------------
    return sourceObj;
  }

  @Override
  public Long insert(SourceDto newObj) {
    // ------------------------------------------------------
    Long objId = null;
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("insert into mss_source");
    sbQuery.append(" (msss_id, msss_name, msss_dt_create, msss_dt_modify, msss_owner)");
    sbQuery.append(" values (?, ?, sysdate, sysdate, user)");
    String strQuery = sbQuery.toString();
    // ------------------------------------------------------
    try {
      Connection conn = dbConnect.getConnection();
      objId = mngUtil.getValueL("select msss_seq.nextval from dual", conn);
      PreparedStatement ps = conn.prepareStatement(strQuery);
      ps.setLong(1, objId);
      ps.setString(2, newObj.getName());
      // --------------------------
      ps.executeUpdate();
      // --------------------------
      ps.close();
      dbConnect.close(conn);
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      objId = null;
    }
    // -----------------------------------------------------
    return objId;
  }

  @Override
  public boolean update(SourceDto newObj) {
    boolean res = false;
    // -----------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("update mss_source");
    sbQuery.append("   set msss_name=?");
    sbQuery.append("      ,msss_dt_modify=sysdate");
    sbQuery.append("      ,msss_owner=user");
    sbQuery.append(" where msss_id=?");
    String strQuery = sbQuery.toString();
    // -----------------------------------------
    try {
      Connection conn = dbConnect.getConnection();
      PreparedStatement ps = conn.prepareStatement(strQuery);
      ps.setString(1, newObj.getName());
      ps.setLong(2, newObj.getId());
      // --------------------------
      ps.executeUpdate();
      // --------------------------
      ps.close();
      dbConnect.close(conn);
      res = true;
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      res = false;
    }
    // -----------------------------------------------------
    return res;
  }

  @Override
  public boolean delete(Long objId) {
    boolean res = false;
    // -----------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("delete from mss_source where msss_id=?");
    String strQuery = sbQuery.toString();
    // -----------------------------------------------------
    try {
      Connection conn = dbConnect.getConnection();
      PreparedStatement ps = conn.prepareStatement(strQuery);
      ps.setLong(1, objId);
      // --------------------------
      ps.executeUpdate();
      // --------------------------
      ps.close();
      dbConnect.close(conn);
      res = true;
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      res = false;
    }
    // -----------------------------------------------------
    return res;
  }

  private List<String[]> getListStringByQuery(String strQuery) {
    ArrayList<String[]> cache = new ArrayList<>();
    int cntCols = 2; 
    try {
      Connection conn = dbConnect.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(strQuery);
      while (rs.next()) {
        //---------------------------------------
        String[] record = new String[cntCols];
        for (int i = 0; i < cntCols; i++) {
          record[i] = rs.getString(i+1);
        }
        cache.add(record);
        //---------------------------------------
      }
      rs.close();
      stmt.close();
      dbConnect.close(conn);
    } catch (Exception e) {
      cache = new ArrayList<>();
      DialogUtils.errorPrint(e,logger);
    }
    return cache;
  }

  @Override
  public List<String[]> getSources() {
    String strQuery = "select msss_id, msss_name from mss_source order by msss_name";
    List<String[]> arrayString = getListStringByQuery(strQuery);
    return arrayString;
  }

}
