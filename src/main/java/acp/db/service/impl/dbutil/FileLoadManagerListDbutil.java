package acp.db.service.impl.dbutil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.impl.dbutil.all.ManagerListDbutil;
import acp.forms.dto.FileLoadDto;
import acp.utils.*;

public class FileLoadManagerListDbutil extends ManagerListDbutil<FileLoadDto> {
  private static Logger logger = LoggerFactory.getLogger(FileLoadManagerListDbutil.class);

  private String tableName;
  private String[] fields;
  private String[] headers;
  private Class<?>[] types;

  private String pkColumn;
  private Long seqId;

  private String strFields;
  private String strFrom;
  private String strAwhere;
  private String strWhere;
  private String strOrder;

  private String strQuery;
  private String strQueryCnt;

  private List<FileLoadDto> cacheObj = new ArrayList<>();

  public FileLoadManagerListDbutil() {
    tableName = "mss_files";
    
    fields = new String[] { "mssf_id", "mssf_name", "mssf_md5", "mssf_owner",
        "mssf_dt_work", "mssf_rec_all"
//        ,"extract(mssf_statistic,'statistic/records/all/text()').getStringval() rec_count"
      };

    headers = new String[] { 
        "ID"
      , Messages.getString("Column.FileName")
      , "MD5"
      , Messages.getString("Column.Owner")
      , Messages.getString("Column.DateWork")
      , Messages.getString("Column.RecordCount") 
    };
    
    types = new Class<?>[] { 
        Long.class
      , String.class
      , String.class
      , String.class
      , Timestamp.class
      , int.class
    };

    pkColumn = fields[0];
    seqId = 1000L;

    strFields = QueryUtils.buildSelectFields(fields, null);
    strFrom = tableName;
    strAwhere = null;
    strWhere = strAwhere;
    strOrder = pkColumn;

    prepareQuery(null);
  }

  @Override
  public String[] getHeaders() {
    return headers;    
  }

  @Override
  public Class<?>[] getTypes() {
    return types;    
  }

  @Override
  public Long getSeqId() {
    return seqId;
  }

  @Override
  public void prepareQuery(Map<String,String> mapFilter) {
    if (mapFilter != null) {
      setWhere(mapFilter);
    } else {
      strWhere = strAwhere;
    }
    strQuery = QueryUtils.buildQuery(strFields, strFrom, strWhere, strOrder);
    strQueryCnt = QueryUtils.buildQuery("select count(*) cnt", strFrom, strWhere, null);
  }

  private void setWhere(Map<String,String> mapFilter) {
    // ----------------------------------
    String vName = mapFilter.get("name");
    String vOwner = mapFilter.get("owner");
    String vDateWorkBeg = mapFilter.get("dateWorkBeg");
    String vDateWorkEnd = mapFilter.get("dateWorkEnd");
    String vRecAllBeg = mapFilter.get("recAllBeg");
    String vRecAllEnd = mapFilter.get("recAllEnd");
    // ----------------------------------
    String phWhere = null;
    String str = null;
    String vField = null;
    // ---
    if (!QueryUtils.emptyString(vName)) {
      str = "upper(mssf_name) like upper('" + vName + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    if (!QueryUtils.emptyString(vOwner)) {
      str = "upper(mssf_owner) like upper('" + vOwner + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    //---
    vField = "mssf_dt_work";
    String dtBeg = null;
    String dtEnd = null;
    if (!QueryUtils.emptyString(vDateWorkBeg)) {
      dtBeg = "to_date('" + vDateWorkBeg + "','dd.mm.yyyy')";
    }
    if (!QueryUtils.emptyString(vDateWorkEnd)) {
      dtEnd = "to_date('" + vDateWorkEnd + " 23:59:59" + "','dd.mm.yyyy hh24:mi:ss')";
    }
    str = null;
    if (!QueryUtils.emptyString(dtBeg) && !QueryUtils.emptyString(dtEnd)) {
      str = vField + " between " + dtBeg + " and " + dtEnd;
    } else if (!QueryUtils.emptyString(dtBeg) && QueryUtils.emptyString(dtEnd)) {
      str = vField + " >= " + dtBeg;
    } else if (QueryUtils.emptyString(dtBeg) && !QueryUtils.emptyString(dtEnd)) {
      str = vField + " <= " + dtEnd;
    }
    if (!QueryUtils.emptyString(str)) {
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    //---
    vField = "mssf_rec_all";
    String intBeg = null;
    String intEnd = null;
    if (!QueryUtils.emptyString(vRecAllBeg)) {
      intBeg = vRecAllBeg;
    }
    if (!QueryUtils.emptyString(vRecAllEnd)) {
      intEnd = vRecAllEnd;
    }
    str = null;
    if (!QueryUtils.emptyString(intBeg) && !QueryUtils.emptyString(intEnd)) {
      str = vField + " between " + intBeg + " and " + intEnd;
    } else if (!QueryUtils.emptyString(intBeg) && QueryUtils.emptyString(intEnd)) {
      str = vField + " >= " + intBeg;
    } else if (QueryUtils.emptyString(intBeg) && !QueryUtils.emptyString(intEnd)) {
      str = vField + " <= " + intEnd;
    }
    if (!QueryUtils.emptyString(str)) {
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    strWhere = QueryUtils.strAddAnd(strAwhere, phWhere);
  }

  @Override
  public List<FileLoadDto> queryAll() {
    cacheObj = fetchPage(-1,-1);
    return cacheObj;    
  }

  @Override
  public List<FileLoadDto> fetchPage(int startPos, int cntRows) {
    String queryPage = prepareQueryPage(strQuery,startPos,cntRows);
    try {
      Connection conn = dbConnect.getConnection();
      // ==================
      fillCache(conn, queryPage);
      // ==================
      dbConnect.close(conn);
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
    }
    return cacheObj;
  }  
 
  private void fillCache(Connection conn, String sqlQuery) throws SQLException {
    ResultSetHandler<List<Object[]>> handler = new ArrayListHandler();
    // ===============================================================
    List<Object[]> objList = queryRunner.query(conn,sqlQuery,handler);
    // ===============================================================
    cacheObj = new ArrayList<>();
    for (int i=0; i < objList.size(); i++) {
      Object[] obj = (Object[]) objList.get(i);
      cacheObj.add(getObject(obj));
    }
  }

  private FileLoadDto getObject(Object[] obj) {
    //---------------------------------------
    Long rsId = ((BigDecimal) obj[0]).longValue();
    String rsName = (String) obj[1];
    String rsMd5 = (String) obj[2];
    String rsOwner = (String) obj[3];
    Timestamp rsDateWork = (Timestamp) obj[4];
    Integer rsRecAll = ((BigDecimal) obj[5]).intValue();
    //---------------------------------------
    FileLoadDto objDto = new FileLoadDto();
    objDto.setId(rsId);
    objDto.setName(rsName);
    objDto.setMd5(rsMd5);
    objDto.setOwner(rsOwner);
    objDto.setDateWork(rsDateWork);
    objDto.setRecAll(rsRecAll);
    //---------------------------------------
    return objDto;
  }

  @Override
  public long countRecords() {
    System.out.println("count: " + strQueryCnt);
    long cntRecords = 0; 
    ScalarHandler<BigDecimal> handler = new ScalarHandler<>();
    try {
      Connection conn = dbConnect.getConnection();
      cntRecords = queryRunner.query(conn, strQueryCnt, handler).longValue();
      dbConnect.close(conn);
    } catch (Exception e) {
      DialogUtils.errorPrint(e,logger);
      cntRecords = 0; 
    }
    return cntRecords;    
  }

}
